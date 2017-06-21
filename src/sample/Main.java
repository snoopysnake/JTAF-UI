package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

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
                    try {
                        createProjectViewer(selectedDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    primaryStage.hide();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("");
                    alert.setContentText("Invalid project directory!");
                    alert.showAndWait();
                }
            }
        });
        Text text = new Text();
        text.setText("Parameters for a JTAF project: Must contain logs, " +
                "profiles, src, target, and testscripts folders.");
        text.setWrappingWidth(280);

        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        StackPane root = new StackPane();
        StackPane.setAlignment(text, Pos.TOP_CENTER);
        StackPane.setMargin(text, new Insets(30));
        root.getChildren().add(text);
        root.getChildren().add(btn);
        primaryStage.setTitle("JTAF-UI Welcome Screen");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private void createProjectViewer(File selectedDirectory) throws IOException, SAXException, ParserConfigurationException {
        Group root = new Group();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle(selectedDirectory.getName());
        BorderPane defaultBorderPane = new BorderPane();
        final TabPane tabPane = new TabPane();
        tabPane.setPrefSize(1200, 800);
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        final Tab tab1 = new Tab();
        tab1.setText("Strategies");
        tab1.setContent(createStrategiesPane());

        final Tab tab2 = new Tab();
        tab2.setText("Commands");

        final Tab tab3 = new Tab();
        tab3.setText("Test Library");
        tab3.setContent(createTestLibraryPane(selectedDirectory.getPath()));

        final Tab tab4 = new Tab();
        tab4.setText("JTAF Properties");

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
        defaultBorderPane.setCenter(tabPane);
        root.getChildren().add(defaultBorderPane);
        newStage.show();

//        StackPane projectViewer = new StackPane();
//        StackPane.setAlignment(toolBar, Pos.TOP_CENTER);
//        projectViewer.getChildren().add(toolBar);
//
//        Stage projectViewerStage = new Stage();
//        projectViewerStage.setTitle(selectedDirectory.getName());
//        projectViewerStage.setScene(new Scene(projectViewer, 1200, 800));
//        projectViewerStage.show();

    }

    private Pane createStrategiesPane() {
        StackPane strategiesPane = new StackPane();
        return strategiesPane;
    }

    private ScrollPane createTestLibraryPane(String projectPath) throws ParserConfigurationException, IOException, SAXException {
        ScrollPane testLibraryPane = new ScrollPane();
        testLibraryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        testLibraryPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        testLibraryPane.setFitToWidth(true);

        ArrayList<String> testLibraryCommands = new ArrayList<>();
        File testLibraryPath = new File(projectPath+"\\src\\main\\resources\\testlibrary");
        if (!testLibraryPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid test library directory!");
            alert.showAndWait();
        } else {
            testLibraryCommands = searchForCommands(testLibraryPath.listFiles(), testLibraryCommands, "");
        }

        final ListView<String> testLibraryListView = new ListView<String>();
        testLibraryListView.setItems(FXCollections.observableArrayList(testLibraryCommands));
        testLibraryListView.setPrefHeight(800);
        testLibraryListView.setOnMouseClicked(event -> {
            String clickedLibrary = testLibraryListView.getSelectionModel().getSelectedItem();
            System.out.println("Clicked on: " + clickedLibrary);
            String testLibraryChild = projectPath + "\\src\\main\\resources\\testlibrary\\"+clickedLibrary;
            TestLibraryParser testLibraryParser = null;
            try {
                testLibraryParser = new TestLibraryParser(testLibraryChild);
                System.out.println(testLibraryParser.getNames());
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < testLibraryParser.getCommandsSize(); j++) {

            }
        });

        testLibraryPane.setContent(testLibraryListView);
        return testLibraryPane;
    }

    private ArrayList<String> searchForCommands(File[] fileList, ArrayList<String> testLibraryCommands, String directoryName) throws IOException {
        for (File file : fileList) {
            if (file.isDirectory()) {
                testLibraryCommands = searchForCommands(file.listFiles(), testLibraryCommands, directoryName+file.getName()+"\\");
            }
            else if(Files.probeContentType(file.toPath()).equals("text/xml"))
                testLibraryCommands.add(directoryName+file.getName());
        }
        System.out.print("Searched test library: " + testLibraryCommands);
        return testLibraryCommands;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
