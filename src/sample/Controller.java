package sample;

import dialogs.EditLabel;
import dialogs.EditStatus;
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
    private RadioButton f1, f2;
    @FXML
    private CheckBox ch1;
    private List<Task> taskList = new ArrayList<>();
    private Comparator<Task> byDateDown= Comparator.comparing(obj -> obj.getDate().getValue().toString());
    private Comparator<Task> byDateUp = byDateDown.reversed();

    private void readTaskist() {
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

    private void writeTaskListToTable() {
        tasks.getItems().clear();
        for (Task task: taskList){
            if (!task.getStatus().getSelectionModel().getSelectedItem().equals("Удалить"))
                tasks.getItems().add(task);
        }
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
        readTaskist();
        writeTaskListToTable();
    }

    public void addNewTask() {
        taskList.add(new Task(newTask.getText(), dateTask.getValue().toString()));
        tasks.getItems().add(new Task(newTask.getText(), dateTask.getValue().toString()));
        newTask.clear();
        dateTask.getEditor().clear();
        dateTask.setValue(LocalDate.now());
        radioButtonClicked();
    }

    public void saveTasks() {
        writeTaskListToFile();
        radioButtonClicked();
        if (ch1.isSelected())
            writeTaskListToTableWithoutClosed();
        else writeTaskListToTable();
    }

    public void radioButtonClicked() {
        if (f1.isSelected())
            Collections.sort(taskList, byDateUp);
        if (f2.isSelected())
            Collections.sort(taskList, byDateDown);
        if (ch1.isSelected())
            writeTaskListToTableWithoutClosed();
        else
            writeTaskListToTable();
    }

    public void checkBoxClicked() {
        if (ch1.isSelected())
            writeTaskListToTableWithoutClosed();
        else writeTaskListToTable();
    }

    private void writeTaskListToTableWithoutClosed() {
        tasks.getItems().clear();
        for (Task task: taskList){
            if (!task.getStatus().getSelectionModel().getSelectedItem().equals("Удалить") &&
                    !task.getStatus().getSelectionModel().getSelectedItem().equals("Закрыта") &&
                    !task.getStatus().getSelectionModel().getSelectedItem().equals("Отложено"))
                tasks.getItems().add(task);
        }
    }



    public void editStatuses() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane page = loader.load(EditStatus.class.getResource("editStatus.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редастирование статусов");
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.myPrimaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            readTaskist();
            radioButtonClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editLabels() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane page = loader.load(EditLabel.class.getResource("editLabel.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редастирование меток");
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.myPrimaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            readTaskist();
            radioButtonClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
