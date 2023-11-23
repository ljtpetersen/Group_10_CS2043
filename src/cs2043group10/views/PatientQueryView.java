package cs2043group10.views;

import java.util.function.Consumer;
import java.util.function.Predicate;

import cs2043group10.DatabaseException;
import cs2043group10.IReversable;
import cs2043group10.IReversableManager;
import cs2043group10.data.PatientQuery.PatientEntry;
import cs2043group10.misc.ReadOnlyObservableValue;
import cs2043group10.misc.WordFilter;
import cs2043group10.data.IQuery;
import cs2043group10.data.PatientInformation;
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

public class PatientQueryView implements IReversable {
	private final int doctorId;
	private IQuery<PatientEntry> data;
	private final IReversableManager manager;
	private WordFilter filter;
	private static TextField searchField;
	private static Button filterButton;
	private static Button clearFilter;
	private static Button viewButton;
	private static Button editButton;
	private static Button medicalHistoryButton;
	private static Button financialHistoryButton;
	private static Button createButton;
	private static VBox view;
	private static TableView<PatientEntry> table;
	private static ObservableList<PatientEntry> list;
	private static FilteredList<PatientEntry> filteredList;
	private static SortedList<PatientEntry> sortedList;
	private static Consumer<Integer> doubleClickHandler;
	
	public PatientQueryView(IReversableManager manager, int doctorId) throws DatabaseException {
		data = manager.getDatabaseManager().queryPatientsUnderDoctor(doctorId);
		this.doctorId = doctorId;
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
		searchField.setPromptText("Enter Name");
		clearFilter = new Button("\u2a2f");
		filterButton = new Button("Search");
		searchField.setMaxWidth(200);
		searchField.setMinWidth(200);
		
		
		HBox topRow = new HBox(3);
		topRow.getChildren().addAll(searchField, clearFilter, filterButton);
		topRow.setAlignment(Pos.BASELINE_RIGHT);
		view.getChildren().add(topRow);
		
		table = new TableView<PatientEntry>();
		TableColumn<PatientEntry, Integer> idColumn = new TableColumn<PatientEntry, Integer>("Id");
		idColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<Integer>(p.getValue().id));
		idColumn.setEditable(false);
		idColumn.setSortable(true);
		TableColumn<PatientEntry, String> nameColumn = new TableColumn<PatientEntry, String>("Name");
		nameColumn.setCellValueFactory(p -> new ReadOnlyObservableValue<String>(p.getValue().name));
		nameColumn.setEditable(false);
		nameColumn.setSortable(true);
		nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(5).divide(6));
		idColumn.prefWidthProperty().bind(table.widthProperty().divide(6));
		table.getColumns().setAll(idColumn, nameColumn);
		
		view.getChildren().add(table);
		
		list = FXCollections.observableArrayList();
		filteredList = new FilteredList<PatientEntry>(list);
		sortedList = new SortedList<PatientEntry>(filteredList);
		sortedList.comparatorProperty().bind(table.comparatorProperty());
		table.setRowFactory(tv -> {
			TableRow<PatientEntry> row = new TableRow<PatientEntry>();
			row.setOnMouseClicked(e -> {
				if (e.getClickCount() == 2 && !row.isEmpty()) {
					doubleClickHandler.accept(row.getItem().id);
				}
			});
			return row;
		});
		table.setItems(sortedList);
		
		viewButton = new Button("View");
		editButton = new Button("Edit");
		medicalHistoryButton = new Button("Medical Documents");
		financialHistoryButton = new Button("Financial Documents");
		createButton = new Button("Create New Patient");
		Region rg = new Region();
		HBox.setHgrow(rg, Priority.ALWAYS);
		HBox bottomRow = new HBox(3);
		bottomRow.setAlignment(Pos.CENTER_LEFT);
		bottomRow.getChildren().addAll(viewButton, editButton, medicalHistoryButton, financialHistoryButton, rg, createButton);
		view.getChildren().add(bottomRow);
		GridPane.setMargin(view, new Insets(0, 4, 4, 4));
	}

	@Override
	public void beforeShow() {
		list.setAll(data.getEntries());
		filteredList.setPredicate(e -> filter.test(e.name));
		searchField.setText(filter.getCurrentFilter());
		searchField.setOnAction(this::filterEvent);
		filterButton.setOnAction(this::filterEvent);
		clearFilter.setOnAction(this::clearFilterEvent);
		doubleClickHandler = this::viewEntry;
		viewButton.setOnAction(this::changeViewEvent);
		medicalHistoryButton.setOnAction(this::changeViewEvent);
		financialHistoryButton.setOnAction(this::changeViewEvent);
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
		return "Query Patients";
	}

	@Override
	public void destroy() {}

	@Override
	public void refresh() {
		try {
			IQuery<PatientEntry> query = manager.getDatabaseManager().queryPatientsUnderDoctor(doctorId);
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
		Predicate<? super PatientEntry> pred = filteredList.getPredicate();
		filteredList.setPredicate(null);
		filteredList.setPredicate(pred);
	}
	
	private void clearFilterEvent(ActionEvent event) {
		searchField.setText("");
		filterEvent(event);
	}
	
	private void viewEntry(Integer id) {
		try {
			PatientInformation data = manager.getDatabaseManager().queryPatientInformation(id);
			manager.pushNewNode(new PatientDocumentView(manager, data));
		} catch(DatabaseException e) {
			e.display();
		}
	}
	
	private void viewMedicalHistory(Integer id) {
		try {
			manager.pushNewNode(new MedicalQueryView(manager, id));
		} catch(DatabaseException e) {
			e.display();
		}
	}
	
	private void viewFinancialHistory(Integer id) {
		try {
			manager.pushNewNode(new FinancialQueryView(manager, id));
		} catch(DatabaseException e) {
			e.display();
		}
	}
	
	private void editEntry(Integer id) {
		try {
			manager.pushNewNode(new PatientCreateView(manager, manager.getDatabaseManager().queryPatientInformation(id)));
		} catch (DatabaseException e) {
			e.display();
		}
	}
	
	private void changeViewEvent(ActionEvent event) {
		PatientEntry selectedItem = table.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		int id = selectedItem.id;
		if (event.getSource() == viewButton) {
			viewEntry(id);
		} else if (event.getSource() == editButton) {
			editEntry(id);
		} else if (event.getSource() == medicalHistoryButton) {
			viewMedicalHistory(id);
		} else {
			viewFinancialHistory(id);
		}
	}
	
	private void createEvent(ActionEvent event) {
		manager.pushNewNode(new PatientCreateView(manager));
	}
}
