package cs2043group10.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.IQuery;
import cs2043group10.data.MedicalDocument;
import cs2043group10.data.MedicalQuery.MedicalEntry;
import cs2043group10.misc.ReadOnlyObservableValue;
import cs2043group10.misc.WordFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * This is the view which shows a list of medical history entries and allows one to perform an action on them.
 * 
 * @author James Petersen
 */
public class MedicalQueryView implements IReversable {
	/**
	 * The patient whose medical history is to be queried.
	 */
	private final int patientId;
	/**
	 * The results of the query.
	 */
	private IQuery<MedicalEntry> data;
	/**
	 * The manager which manages this view.
	 */
	private final IReversableManager manager;
	/**
	 * The current filter on the entries.
	 */
	private WordFilter filter;
	/**
	 * The field within which to enter a filter.
	 */
	private static TextField searchField;
	/**
	 * The button to press to apply the filter.
	 */
	private static Button filterButton;
	/**
	 * The button to press to clear the filter.
	 */
	private static Button clearFilter;
	/**
	 * The button to press to view a selected entry.
	 */
	private static Button viewButton;
	/**
	 * The button to press to create an entry.
	 */
	private static Button createButton;
	/**
	 * The button to press to edit a selected entry.
	 */
	private static Button editButton;
	/**
	 * The view which contains the table.
	 */
	private static VBox view;
	/**
	 * The table which contains the entries.
	 */
	private static TableView<MedicalEntry> table;
	/**
	 * The list of entries the table relies on.
	 */
	private static ObservableList<MedicalEntry> list;
	/**
	 * The list after the filter has been applied.
	 */
	private static FilteredList<MedicalEntry> filteredList;
	/**
	 * The list after it has been sorted.
	 */
	private static SortedList<MedicalEntry> sortedList;
	/**
	 * The method to be called when an entry is double-clicked.
	 */
	private static Consumer<Integer> doubleClickHandler;
	
	/**
	 * Initialize a new medical query view.
	 * @param manager The manager within which the view resides.
	 * @param patientId The id of the patient whose medical history is to be queried.
	 * @throws DatabaseException
	 */
	public MedicalQueryView(IReversableManager manager, int patientId) throws DatabaseException {
		data = manager.getDatabaseManager().queryMedicalDocumentsUnderPatient(patientId);
		this.patientId = patientId;
		this.manager = manager;
		filter = new WordFilter();
		if (view == null) {
			createView();
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Create the view for this class.
	 */
	private static void createView() {
		view = new VBox(5);
		searchField = new TextField();
		searchField.setPromptText("Enter Search");
		clearFilter = new Button("\u2a2f");
		filterButton = new Button("Search");
		searchField.setMaxWidth(200);
		searchField.setMinWidth(200);
		
		HBox topRow = new HBox(3);
		topRow.getChildren().addAll(searchField, clearFilter, filterButton);
		topRow.setAlignment(Pos.BASELINE_RIGHT);
		view.getChildren().add(topRow);
		
		table = new TableView<MedicalEntry>();
		TableColumn<MedicalEntry, String> titleColumn = new TableColumn<MedicalEntry, String>("Title");
		titleColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<String>(p.getValue().title));
		titleColumn.setEditable(false);
		titleColumn.setSortable(true);
		TableColumn<MedicalEntry, String> typeColumn = new TableColumn<MedicalEntry, String>("Type");
		typeColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<String>(p.getValue().type));
		typeColumn.setEditable(false);
		typeColumn.setSortable(true);
		TableColumn<MedicalEntry, Integer> idColumn = new TableColumn<MedicalEntry, Integer>("Id");
		idColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<Integer>(p.getValue().documentId));
		idColumn.setEditable(false);
		idColumn.setSortable(true);
		TableColumn<MedicalEntry, LocalDateTime> createdColumn = new TableColumn<MedicalEntry, LocalDateTime>("Created");
		createdColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<LocalDateTime>(LocalDateTime.ofInstant(Instant.ofEpochSecond(p.getValue().createTimestamp), TimeZone.getDefault().toZoneId())));
		createdColumn.setEditable(false);
		createdColumn.setSortable(true);
		TableColumn<MedicalEntry, LocalDateTime> modifiedColumn = new TableColumn<MedicalEntry, LocalDateTime>("Modified");
		modifiedColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<LocalDateTime>(LocalDateTime.ofInstant(Instant.ofEpochSecond(p.getValue().modifyTimestamp), TimeZone.getDefault().toZoneId())));
		modifiedColumn.setEditable(false);
		modifiedColumn.setSortable(true);
		table.getColumns().setAll(idColumn, titleColumn, typeColumn, createdColumn, modifiedColumn);
		
		view.getChildren().add(table);
		
		list = FXCollections.observableArrayList();
		filteredList = new FilteredList<MedicalEntry>(list);
		sortedList = new SortedList<MedicalEntry>(filteredList);
		sortedList.comparatorProperty().bind(table.comparatorProperty());
		table.setRowFactory(tv -> {
			TableRow<MedicalEntry> row = new TableRow<MedicalEntry>();
			row.setOnMouseClicked(e -> {
				if (e.getClickCount() == 2 && !row.isEmpty()) {
					doubleClickHandler.accept(row.getItem().documentId);
				}
			});
			return row;
		});
		table.setItems(sortedList);
		
		viewButton = new Button("View");
		editButton = new Button("Edit");
		Region rg = new Region();
		createButton = new Button("Create New Medical Document");
		HBox bottomRow = new HBox(3);
		HBox.setHgrow(rg, Priority.ALWAYS);
		bottomRow.getChildren().addAll(viewButton, editButton, rg, createButton);
		view.getChildren().add(bottomRow);
		GridPane.setMargin(view, new Insets(0, 4, 4, 4));
	}

	@Override
	public void beforeShow() {
		list.setAll(data.getEntries());
		filteredList.setPredicate(e -> filter.test(new String[] { e.title, e.type }));
		searchField.setText(filter.getCurrentFilter());
		searchField.setOnAction(this::filterEvent);
		filterButton.setOnAction(this::filterEvent);
		clearFilter.setOnAction(this::clearFilterEvent);
		doubleClickHandler = this::viewEntry;
		viewButton.setOnAction(this::changeViewEvent);
		createButton.setOnAction(this::createEvent);
		editButton.setOnAction(this::changeViewEvent);
	}

	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Query Medical Documents";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			IQuery<MedicalEntry> query = manager.getDatabaseManager().queryMedicalDocumentsUnderPatient(patientId);
			this.data = query;
			list.setAll(data.getEntries());
		} catch (DatabaseException e) {
			e.display();
		}
	}

	@Override
	public void afterShow() {}

	@Override
	public void beforeHide() {}
	
	/**
	 * The event handler which is called when the user tries to filter the query.
	 * @param event The event.
	 */
	private void filterEvent(ActionEvent event) {
		filter.setCurrentFilter(searchField.getText());
		Predicate<? super MedicalEntry> pred = filteredList.getPredicate();
		filteredList.setPredicate(null);
		filteredList.setPredicate(pred);
	}
	
	/**
	 * The event handler which is called when the user tries to clear the filter.
	 * @param event The event.
	 */
	private void clearFilterEvent(ActionEvent event) {
		searchField.setText("");
		filterEvent(event);
	}
	
	/**
	 * The method which is called when the user tries to view a certain entry.
	 * @param id The id of the entry.
	 */
	private void viewEntry(Integer id) {
		try {
			MedicalDocument data = manager.getDatabaseManager().queryMedicalDocument(id);
			manager.pushNewNode(new MedicalDocumentView(manager, data));
		} catch(DatabaseException e) {
			e.display();
		}
	}
	
	/**
	 * The method which is called when the user tries to edit a certain entry.
	 * @param id The id of the entry.
	 */
	private void editEntry(Integer id) {
		try {
			MedicalDocument data = manager.getDatabaseManager().queryMedicalDocument(id);
			manager.pushNewNode(new MedicalCreateView(manager, data));
		} catch (DatabaseException e) {
			e.display();
		}
	}
	
	/**
	 * The event handler which is called when the user tries to perform an action on an entry.
	 * @param event The event.
	 */
	private void changeViewEvent(ActionEvent event) {
		MedicalEntry selectedItem = table.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		int id = selectedItem.documentId;
		if (event.getSource() == viewButton) {
			viewEntry(id);
		} else {
			editEntry(id);
		}
	}
	
	/**
	 * The event handler which is called when the user tries to create a new entry.
	 * @param event The event.
	 */
	private void createEvent(ActionEvent event) {
		manager.pushNewNode(new MedicalCreateView(manager, patientId));
	}
}
