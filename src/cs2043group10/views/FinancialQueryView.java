package cs2043group10.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Predicate;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.FinancialDocument;
import cs2043group10.data.FinancialQuery.FinancialEntry;
import cs2043group10.data.IQuery;
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

public class FinancialQueryView implements IReversable {
	private final int patientId;
	private IQuery<FinancialEntry> data;
	private final IReversableManager manager;
	private WordFilter filter;
	private static TextField searchField;
	private static Button filterButton;
	private static Button clearFilter;
	private static Button viewButton;
	private static Button createButton;
	private static VBox view;
	private static TableView<FinancialEntry> table;
	private static ObservableList<FinancialEntry> list;
	private static FilteredList<FinancialEntry> filteredList;
	private static SortedList<FinancialEntry> sortedList;
	private static Consumer<Integer> doubleClickHandler;
	
	public FinancialQueryView(IReversableManager manager, int patientId) throws DatabaseException {
		data = manager.getDatabaseManager().queryFinancialDocumentsUnderPatient(patientId);
		this.patientId = patientId;
		this.manager = manager;
		filter = new WordFilter();
		if (view == null) {
			createView();
		}
	}
	
	@SuppressWarnings("unchecked")
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
		
		table = new TableView<FinancialEntry>();
		TableColumn<FinancialEntry, String> titleColumn = new TableColumn<FinancialEntry, String>("Title");
		titleColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<String>(p.getValue().title));
		titleColumn.setEditable(false);
		titleColumn.setSortable(true);
		TableColumn<FinancialEntry, Double> typeColumn = new TableColumn<FinancialEntry, Double>("Amount");
		typeColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<Double>(p.getValue().amount / 100.0));
		typeColumn.setEditable(false);
		typeColumn.setSortable(true);
		TableColumn<FinancialEntry, Integer> idColumn = new TableColumn<FinancialEntry, Integer>("Id");
		idColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<Integer>(p.getValue().documentId));
		idColumn.setEditable(false);
		idColumn.setSortable(true);
		TableColumn<FinancialEntry, LocalDateTime> createdColumn = new TableColumn<FinancialEntry, LocalDateTime>("Created");
		createdColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<LocalDateTime>(LocalDateTime.ofInstant(Instant.ofEpochSecond(p.getValue().createTimestamp), TimeZone.getDefault().toZoneId())));
		createdColumn.setEditable(false);
		createdColumn.setSortable(true);
		table.getColumns().setAll(idColumn, titleColumn, typeColumn, createdColumn);
		
		view.getChildren().add(table);
		
		list = FXCollections.observableArrayList();
		filteredList = new FilteredList<FinancialEntry>(list);
		sortedList = new SortedList<FinancialEntry>(filteredList);
		sortedList.comparatorProperty().bind(table.comparatorProperty());
		table.setRowFactory(tv -> {
			TableRow<FinancialEntry> row = new TableRow<FinancialEntry>();
			row.setOnMouseClicked(e -> {
				if (e.getClickCount() == 2 && !row.isEmpty()) {
					doubleClickHandler.accept(row.getItem().documentId);
				}
			});
			return row;
		});
		table.setItems(sortedList);
		
		viewButton = new Button("View");
		Region rg = new Region();
		createButton = new Button("Create New Financial Document");
		HBox bottomRow = new HBox(3);
		HBox.setHgrow(rg, Priority.ALWAYS);
		bottomRow.getChildren().addAll(viewButton, rg, createButton);
		view.getChildren().add(bottomRow);
		GridPane.setMargin(view, new Insets(0, 4, 4, 4));
	}

	@Override
	public void beforeShow() {
		list.setAll(data.getEntries());
		filteredList.setPredicate(e -> filter.test(new String[] { e.title }));
		searchField.setText(filter.getCurrentFilter());
		searchField.setOnAction(this::filterEvent);
		filterButton.setOnAction(this::filterEvent);
		clearFilter.setOnAction(this::clearFilterEvent);
		doubleClickHandler = this::viewEntry;
		viewButton.setOnAction(this::changeViewEvent);
		createButton.setOnAction(this::createEvent);
	}

	@Override
	public void afterHide() {}

	@Override
	public Node getNode() {
		return view;
	}

	@Override
	public String getTitle() {
		return "Query Financial Documents";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			IQuery<FinancialEntry> query = manager.getDatabaseManager().queryFinancialDocumentsUnderPatient(patientId);
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
	
	private void filterEvent(ActionEvent event) {
		filter.setCurrentFilter(searchField.getText());
		Predicate<? super FinancialEntry> pred = filteredList.getPredicate();
		filteredList.setPredicate(null);
		filteredList.setPredicate(pred);
	}
	
	private void clearFilterEvent(ActionEvent event) {
		searchField.setText("");
		filterEvent(event);
	}
	
	private void viewEntry(Integer id) {
		try {
			FinancialDocument data = manager.getDatabaseManager().queryFinancialDocument(id);
			manager.pushNewNode(new FinancialDocumentView(manager, data));
		} catch(DatabaseException e) {
			e.display();
		}
	}
	
	private void changeViewEvent(ActionEvent event) {
		FinancialEntry selectedItem = table.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		int id = selectedItem.documentId;
		viewEntry(id);
	}
	
	private void createEvent(ActionEvent event) {
		manager.pushNewNode(new FinancialCreateView(manager, patientId));
	}
}
