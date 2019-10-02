package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {
    private TextArea task = new TextArea();
    private ComboBox<String> status = new ComboBox<>();
    private ComboBox<String> label = new ComboBox<>();
    private DatePicker date = new DatePicker();
    private List<String> statuses = new ArrayList<String>();
    private List<String> labels = new ArrayList<String>();

    private void generateLabelList(){
        try {
            labels.clear();
            BufferedReader reader = new BufferedReader(new FileReader("labels.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                labels.add(line);
            }
            reader.close();
        } catch (IOException ignored){}
    }

    private void generateStatusList(){
        try {
            statuses.clear();
            BufferedReader reader = new BufferedReader(new FileReader("statuses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                statuses.add(line);
            }
            reader.close();
        } catch (IOException ignored){}
    }


    Task(String ... args){
        this.task.setMaxSize(350, 20);
        this.task.setText(args[0]);
        this.date.setValue(LocalDate.parse(args[1]));
        generateStatusList();
        generateLabelList();
        this.status.getItems().addAll(statuses);
        this.label.getItems().addAll(labels);
        this.status.getSelectionModel().select(0);
        this.label.getSelectionModel().select(0);
        if (args.length == 3){
            this.status.getSelectionModel().select(args[2]);
        }
        if (args.length == 4){
            this.label.getSelectionModel().select(args[3]);
        }
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
        return task.getText() + "_"+ date.getValue().toString() + "_" + status.getSelectionModel().getSelectedItem()
                +"_" + label.getSelectionModel().getSelectedItem()+  "\n";
    }

    public ComboBox<String> getLabel() {
        return label;
    }

    public void setLabel(ComboBox<String> label) {
        this.label = label;
    }
}
