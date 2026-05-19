package com.studentrecord.ui;

import com.studentrecord.manager.StudentManager;
import com.studentrecord.model.Student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SearchStudentView extends VBox
{
    private StudentManager     manager;
    private TableView<Student> table;
    private TextField          searchField;
    private ToggleButton       toggleByName;
    private ToggleButton       toggleByID;
    private Label              resultLabel;


    public SearchStudentView(StudentManager manager)
    {
        this.manager = manager;
        buildView();
    }


    private void buildView()
    {
        setSpacing(16);
        setPadding(new Insets(30, 40, 30, 40));
        setStyle("-fx-background-color: #f4f6f8;");

        Text heading = new Text("Search Students");
        heading.setFont(Font.font("System", FontWeight.BOLD, 20));
        heading.setStyle("-fx-fill: #2c3e50;");

        HBox toggleRow = buildToggleRow();
        HBox searchRow = buildSearchRow();

        resultLabel = new Label("");
        resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        table = buildTable();

        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        getChildren().addAll(heading, toggleRow, searchRow, resultLabel, table);
    }


    private HBox buildToggleRow()
    {
        toggleByName = new ToggleButton("Search by Name");
        toggleByID = new ToggleButton("Search by ID");

        ToggleGroup group = new ToggleGroup();
        toggleByName.setToggleGroup(group);
        toggleByID.setToggleGroup(group);
        toggleByName.setSelected(true);

        String baseStyle =
                "-fx-font-size: 12px;" +
                        "-fx-padding: 6 14 6 14;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5;";

        String activeStyle   = baseStyle + "-fx-background-color: #2c3e50; -fx-text-fill: white;";
        String inactiveStyle = baseStyle + "-fx-background-color: #dde1e7; -fx-text-fill: #2c3e50;";

        toggleByName.setStyle(activeStyle);
        toggleByID.setStyle(inactiveStyle);

        toggleByName.selectedProperty().addListener((obs, wasSelected, isSelected) ->
        {
            toggleByName.setStyle(isSelected ? activeStyle : inactiveStyle);
            toggleByID.setStyle(isSelected ? inactiveStyle : activeStyle);
            searchField.clear();
            searchField.setPromptText(isSelected ? "Enter student name..." : "Enter student ID...");
            clearTable();
        });

        HBox row = new HBox(10, toggleByName, toggleByID);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    private HBox buildSearchRow()
    {
        searchField = new TextField();
        searchField.setPromptText("Enter student name...");
        searchField.setPrefWidth(280);
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ccd1d9;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 7 10 7 10;" +
                        "-fx-font-size: 13px;"
        );

        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch(newVal));

        Button btnClear = new Button("Clear");
        btnClear.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 7 16 7 16;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );

        btnClear.setOnAction(e ->
        {
            searchField.clear();
            clearTable();
        });

        HBox row = new HBox(10, searchField, btnClear);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    private void handleSearch(String input)
    {
        if (input == null || input.trim().isEmpty())
        {
            clearTable();
            return;
        }

        ObservableList<Student> results = FXCollections.observableArrayList();

        if (toggleByName.isSelected())
        {
            results.addAll(manager.searchByName(input.trim()));
        }
        else
        {
            try
            {
                int id = Integer.parseInt(input.trim());
                Student student = manager.searchByID(id);

                if (student != null)
                {
                    results.add(student);
                }
            }
            catch (NumberFormatException e)
            {
                // non-numeric input while searching by ID — just show nothing
            }
        }

        table.setItems(results);

        if (results.isEmpty())
        {
            resultLabel.setText("No matching records found.");
        }
        else
        {
            resultLabel.setText(results.size() + " record(s) found.");
        }
    }


    private void clearTable()
    {
        table.setItems(FXCollections.observableArrayList());
        resultLabel.setText("");
    }


    private TableView<Student> buildTable()
    {
        TableView<Student> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dde1e7;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        TableColumn<Student, Integer> colID    = new TableColumn<>("Student ID");
        TableColumn<Student, String>  colName  = new TableColumn<>("Full Name");
        TableColumn<Student, Integer> colAge   = new TableColumn<>("Age");
        TableColumn<Student, String>  colEmail = new TableColumn<>("Email");
        TableColumn<Student, String>  colPhone = new TableColumn<>("Phone");
        TableColumn<Student, String>  colGrade = new TableColumn<>("Grade");

        colID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        colID.setStyle("-fx-alignment: CENTER;");
        colAge.setStyle("-fx-alignment: CENTER;");
        colGrade.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().addAll(colID, colName, colAge, colEmail, colPhone, colGrade);

        tableView.setPlaceholder(
                new Label("Use the search bar above to find a student.")
        );

        return tableView;
    }
}