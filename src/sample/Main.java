package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Button btn = new Button();
        btn.setText("Find Existing Project");
        btn.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File defaultDirectory = new File("C:/");
            directoryChooser.setTitle("Choose project directory");
            directoryChooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                if (FindExistingProject.search(selectedDirectory.getPath())) {
                    StackPane root = new StackPane();
                    Stage nextStage = new Stage();
                    nextStage.setTitle("Hello World");
                    nextStage.setScene(new Scene(root, 300, 275));
                    nextStage.show();
                    primaryStage.hide();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("");
                    alert.setContentText("Invalid project directory!");
                    alert.showAndWait();
                }
            }
        });
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
