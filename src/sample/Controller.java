package sample;

import dialogs.EditLabel;
import dialogs.EditStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Controller {
    @FXML
    private TextArea newTask;
    @FXML
    private DatePicker dateTask;
    @FXML
    private TableView<Task> tasks;
    @FXML
    private ComboBox<String> curStatus;
    @FXML
    private TableColumn<Task, TextArea> task;
    @FXML
    private TableColumn<Task, DatePicker> data;
    @FXML
    private TableColumn<Task, ComboBox<String>> status;
    @FXML
    private TableColumn<Task, ComboBox<String>> label;
    @FXML
    private TableColumn<Task, ComboBox<String>> percent;
    @FXML
    private TableColumn<Task, ComboBox<String>> priority;
    @FXML
    private RadioButton f1, f2;
    @FXML
    private Menu labelList, statusList, priorityList;
    private List<Task> taskList = new ArrayList<>();
    private Comparator<Task> byDateDown= Comparator.comparing(obj -> obj.getDate().getValue().toString());
    private Comparator<Task> byDateUp = byDateDown.reversed();
    private Set<String> labelFilter = new HashSet<>();
    private Set<String> statusFilter = new HashSet<>();
    private Set<String> priorityFilter = new HashSet<>();

    private void readTaskList() {
        try {
            taskList.clear();
            BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String [] tasks = line.split("_");
                taskList.add(new Task(tasks));
            }
            reader.close();
        } catch (IOException ignored){}
    }

    private void writeTaskListToFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tasks.txt"));
            for (Task task : taskList) {
                if (!task.getStatus().getSelectionModel().getSelectedItem().equals("Удалить"))
                    bufferedWriter.write(task.toString());
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        dateTask.setValue(LocalDate.now());
        task.setCellValueFactory(new PropertyValueFactory<>("task"));
        data.setCellValueFactory(new PropertyValueFactory<>("date"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        percent.setCellValueFactory(new PropertyValueFactory<>("percent"));
        priority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        curStatus.getItems().addAll(getListInformation("statuses.txt"));
        readTaskList();
        generateCheckLabelMenu();
        generateCheckStatusMenu();
        generateCheckPriorityMenu();
        filter();
    }

    private void generateCheckStatusMenu() {
        List<String> statuses = getListInformation("statuses.txt");
        statusFilter.clear();
        statusFilter.addAll(statuses);
        statusList.getItems().clear();
        for (String label: statuses){
            CheckMenuItem item = new CheckMenuItem(label);
            item.setSelected(true);
            item.setOnAction(event -> {
                if (!item.isSelected())
                    statusFilter.remove(item.getText());
                else statusFilter.add(item.getText());
                radioButtonClicked();
            });
            statusList.getItems().add(item);
        }
    }

    private void generateCheckPriorityMenu() {
        List<String> priorities = getListInformation("priorities.txt");
        priorityFilter.clear();
        priorityFilter.addAll(priorities);
        priorityList.getItems().clear();
        for (String label: priorities){
            CheckMenuItem item = new CheckMenuItem(label);
            item.setSelected(true);
            item.setOnAction(event -> {
                if (!item.isSelected())
                    priorityFilter.remove(item.getText());
                else priorityFilter.add(item.getText());
                radioButtonClicked();
            });
            priorityList.getItems().add(item);
        }
    }

    private List<String> getListInformation(String filename){
        List<String> status = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                status.add(line);
            }
            reader.close();
        } catch (IOException ignored){}
        return status;
    }

    private void generateCheckLabelMenu() {
        List<String> labels = getListInformation("labels.txt");
        labelFilter.clear();
        labelFilter.addAll(labels);
        labelList.getItems().clear();
        for (String label: labels){
            CheckMenuItem item = new CheckMenuItem(label);
            item.setSelected(true);
            item.setOnAction(event -> {
                if (!item.isSelected())
                    labelFilter.remove(item.getText());
                else labelFilter.add(item.getText());
                radioButtonClicked();
            });
            labelList.getItems().add(item);
        }
    }

    private void filter(Boolean ... args){
        tasks.getItems().clear();
        for (Task task: taskList){
            if (statusFilter.contains(task.getStatus().getSelectionModel().getSelectedItem()) &&
                    labelFilter.contains(task.getLabel().getSelectionModel().getSelectedItem()) &&
                    priorityFilter.contains(task.getPriority().getSelectionModel().getSelectedItem())) {
                tasks.getItems().add(task);
            }
            if (statusFilter.isEmpty() || labelFilter.isEmpty() || priorityFilter.isEmpty())
                tasks.getItems().add(task);
        }
    }

    public void addNewTask() {
        taskList.add(new Task(newTask.getText(), dateTask.getValue().toString(),
                curStatus.getSelectionModel().getSelectedItem()));
        tasks.getItems().add(new Task(newTask.getText(), dateTask.getValue().toString(),
                curStatus.getSelectionModel().getSelectedItem()));
        newTask.clear();
        dateTask.getEditor().clear();
        dateTask.setValue(LocalDate.now());
        radioButtonClicked();
    }

    public void saveTasks() {
        writeTaskListToFile();
        readTaskList();
        radioButtonClicked();
    }

    public void radioButtonClicked() {
        if (f1.isSelected())
            Collections.sort(taskList, byDateUp);
        if (f2.isSelected())
            Collections.sort(taskList, byDateDown);
        filter();
    }

    public void showModalWindow(String resource, String title){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane page = loader.load(EditStatus.class.getResource(resource));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.myPrimaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            readTaskList();
            generateCheckStatusMenu();
            generateCheckLabelMenu();
            generateCheckPriorityMenu();
            radioButtonClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editStatuses() {
        showModalWindow("editStatus.fxml", "Редактирование статусов");
    }

    public void editLabels() {
        showModalWindow("editLabel.fxml", "Редактирование меток");
    }

    public void editPriority() {
        showModalWindow("editPriority.fxml", "Редактирование приоритета");
    }
}
