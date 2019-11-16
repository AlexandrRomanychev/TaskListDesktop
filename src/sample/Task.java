package sample;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

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
    private ComboBox<String> percent = new ComboBox<>();
    private ComboBox<String> priority = new ComboBox<>();
    private DatePicker date = new DatePicker();

    private List<String> generateItems(String filename){
        List<String> items = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(line);
            }
            reader.close();

        } catch (IOException ignored){}
        return items;
    }


    Task(String ... args){
        this.task.setMaxSize(350, 20);
        this.task.setText(args[0]);
        this.date.setValue(LocalDate.parse(args[1]));
        List<String> statuses = generateItems("statuses.txt");
        List<String> labels = generateItems("labels.txt");
        List<String> prioryties = generateItems("prioryties.txt");
        this.status.getItems().addAll(statuses);
        this.label.getItems().addAll(labels);
        this.priority.getItems().addAll(prioryties);
        this.status.getSelectionModel().select(0);
        this.label.getSelectionModel().select(0);
        this.priority.getSelectionModel().select(0);
        for (int i=0;i<=100;i+=10)
            this.percent.getItems().addAll(i+"%");
        this.percent.getSelectionModel().select(0);
        switch(args.length){
            case 5: this.percent.getSelectionModel().select(args[4]);
            case 4: this.label.getSelectionModel().select(args[3]);
            case 3: this.status.getSelectionModel().select(args[2]); break;
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
                +"_" + label.getSelectionModel().getSelectedItem()+"_"+percent.getSelectionModel().getSelectedItem()+"\n";
    }

    public ComboBox<String> getLabel() {
        return label;
    }

    public void setLabel(ComboBox<String> label) {
        this.label = label;
    }

    public ComboBox<String> getPercent() {
        return percent;
    }

    public void setPercent(ComboBox<String> percent) {
        this.percent = percent;
    }

    public ComboBox<String> getPriority() {
        return priority;
    }

    public void setPriority(ComboBox<String> priority) {
        this.priority = priority;
    }
}
