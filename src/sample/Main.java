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
import java.util.ArrayList;
import java.util.HashMap;

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
        tab2.setText("Test Library");
        tab2.setContent(createTestLibraryPane(selectedDirectory.getPath()));

        final Tab tab3 = new Tab();
        tab3.setText("Commands");
        tab3.setContent(createCommandsPane(selectedDirectory.getPath()));

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
        File testLibraryPath = new File(projectPath+"\\testlibrary");
        if (!testLibraryPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid test library directory!");
            alert.showAndWait();
        } else {
            testLibraryCommands = searchForCommands(testLibraryPath.listFiles(), testLibraryCommands, "", ".xml");
            System.out.println("Searched test library: " + testLibraryCommands);
        }

        final ListView<String> testLibraryListView = new ListView<String>();
        testLibraryListView.setItems(FXCollections.observableArrayList(testLibraryCommands));
        testLibraryListView.setPrefHeight(800);
        testLibraryListView.setOnMouseClicked(event -> {
            String clickedLibrary = testLibraryListView.getSelectionModel().getSelectedItem();
            System.out.println("Clicked on: " + clickedLibrary);
            String testLibraryChild = projectPath + "\\testlibrary\\"+clickedLibrary;
            TestLibraryParser testLibraryParser = null;
            try {
                testLibraryParser = new TestLibraryParser(testLibraryChild);
//                System.out.println(testLibraryParser.getNames());
                HashMap<String, HashMap<String, String>> commandChildren = testLibraryParser.getCommandChildren();
//                System.out.println(commandChildren.keySet());
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

    private ScrollPane createCommandsPane(String projectPath) throws ParserConfigurationException, IOException, SAXException {
        ScrollPane commandsPane = new ScrollPane();
        commandsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        commandsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        commandsPane.setFitToWidth(true);

        ArrayList<String> totalCommands = new ArrayList<>();
        File commandsPath = new File(projectPath+"\\src\\main\\java");
        if (!commandsPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid command directory!");
            alert.showAndWait();
        } else {
            totalCommands = searchForCommands(commandsPath.listFiles(), totalCommands, "Cmd.java");
            System.out.println("Searched for commands: " + totalCommands);
        }
        final ListView<String> commandsView = new ListView<String>();
        commandsView.setItems(FXCollections.observableArrayList(totalCommands));
        commandsView.setPrefHeight(800);
        commandsView.setOnMouseClicked(event -> {
            String clickedCommand = commandsView.getSelectionModel().getSelectedItem();
            System.out.println("Clicked on: " + clickedCommand);
        });

        commandsPane.setContent(commandsView);

        return commandsPane;
    }


    private ArrayList<String> searchForCommands(File[] fileList, ArrayList<String> arrayList, String directoryName, String typeOfFile) throws IOException {
        for (File file : fileList) {
            if (file.isDirectory()) {
                arrayList = searchForCommands(file.listFiles(), arrayList, directoryName+file.getName()+"\\", typeOfFile);
            }
            else if(file.getName().endsWith(typeOfFile))
                arrayList.add(directoryName+file.getName());
        }
        return arrayList;
    }

    private ArrayList<String> searchForCommands(File[] fileList, ArrayList<String> arrayList, String typeOfFile) throws IOException {
        for (File file : fileList) {
            if (file.isDirectory()) {
                arrayList = searchForCommands(file.listFiles(), arrayList, typeOfFile);
            }
            else if(file.getName().endsWith(typeOfFile))
                arrayList.add(file.getName());
        }
        return arrayList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
