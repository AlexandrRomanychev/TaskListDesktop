package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage myPrimaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        myPrimaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Список задач");
        primaryStage.setScene(new Scene(root, 700, 530));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
