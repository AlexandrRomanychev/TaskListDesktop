package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {
    private TextArea task = new TextArea();
    private ComboBox<String> status = new ComboBox<>();
    private DatePicker date = new DatePicker();
    private List<String> statuses = new ArrayList<String>(Arrays.asList("Новая", "Выполняется", "Решена", "Закрыта", "Доработка", "Отложено", "Удалить"));

    Task(String task, String date) {
        this.task.setMaxSize(350, 20);
        this.task.setText(task);
        this.date.setValue(LocalDate.parse(date));
        this.status.getItems().addAll(statuses);
        this.status.getSelectionModel().select(0);
    }

    Task(String task, String date, String status) {
        this.task.setMaxSize(350, 20);
        this.task.setText(task);
        this.date.setValue(LocalDate.parse(date));
        this.status.getItems().addAll(statuses);
        this.status.getSelectionModel().select(status);
    }

    public TextArea getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task.setText(task);
    }

    public ComboBox<String> getStatus() {
        return status;
    }

    public void setStatus(ComboBox<String> status) {
        this.status = status;
    }

    public DatePicker getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.setValue(LocalDate.parse(date));
    }

    @Override
    public String toString() {
        return task.getText() + "_"+ date.getValue().toString() + "_" + status.getSelectionModel().getSelectedItem()+ "\n";
    }
}
