package dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.*;

public class EditStatus {

    @FXML
    private TextField newItem;
    @FXML
    private TableColumn<String, String> name;
    @FXML
    private TableView<String> statuses;
    private List<String> status = new ArrayList<>();

    public void initialize() {
        name.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        try {
            status.clear();
            BufferedReader reader = new BufferedReader(new FileReader("statuses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                status.add(line);
            }
            reader.close();
        } catch (IOException ignored){}
        writeToTable();
    }

    private void writeToTable(){
        statuses.getItems().clear();
        for (String item: status)
            statuses.getItems().add(item);
    }

    public void addNewItem() {
        status.add(newItem.getText());
        newItem.clear();
        writeToTable();
    }

    public void deleteItem() {
        try {
            int index = statuses.getSelectionModel().getFocusedIndex();
            statuses.getItems().remove(index);
            status.remove(index);

        }catch (Exception ignored){}
    }

    public void saveElements() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("statuses.txt"));
            for (String task : statuses.getItems()) {
                bufferedWriter.write(task+"\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveUp(MouseEvent mouseEvent) {
        int index = statuses.getSelectionModel().getFocusedIndex();
        if ( index != 0){
            String elemUp = statuses.getItems().get(index);
            String elemDown = statuses.getItems().get(index-1);
            statuses.getItems().remove(index-1, index+1);
            statuses.getItems().add(index-1, elemDown);
            statuses.getItems().add(index-1, elemUp);
        }
    }

    public void moveDown(MouseEvent mouseEvent) {
        int index = statuses.getSelectionModel().getFocusedIndex();
        if ( index != statuses.getItems().size()){
            String elemDown = statuses.getItems().get(index);
            statuses.getItems().remove(index, index+1);
            statuses.getItems().add(index+1, elemDown);
        }
    }
}
