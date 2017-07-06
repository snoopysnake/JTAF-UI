package Main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    private final String STARTING_DIR = "C:\\Users\\Michael\\Documents\\Github\\JTAF-XCore";
    private String PROJECT_DIR; //change depending on version
    private String TEST_LIBRARY_DIR = "\\src\\test\\resources\\testlibrary"; //change depending on version

    //remove
    private HashMap<String, Set<String>> testLibraryMap;
    ArrayList<String> totalCommands = new ArrayList<>();
    private HashMap<String,List<String>> commandNameLibrary = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Button findProjectButton = new Button();
        findProjectButton.setText("Find Existing Project");
        Text jtafDescription = new Text("Parameters for a JTAF project: Must contain logs, " +
                "profiles, src, target, and testscripts folders.");
        jtafDescription.setWrappingWidth(280);

        Text toolBarText = new Text("Select version:");
//        ToolBar toolBar = new ToolBar();
//        CheckBox chkBox1 = new CheckBox();
//        chkBox1.setText("JTAF open source");
//        CheckBox chkBox2 = new CheckBox();
//        chkBox2.setText("FINRA JTAF");
//        CheckBox chkBox3 = new CheckBox();
//        chkBox3.setText("custom loadout");
//        toolBar.getItems().addAll(chkBox1, chkBox2, chkBox3);
//        for (Node b : toolBar.getItems()) {
//            CheckBox chkBox = (CheckBox) b;
//            ((CheckBox) b).setOnAction(event -> {
//                if (chkBox.isSelected()) {
//                    System.out.println(chkBox.getText() + " selected!");
//                    if (chkBox.equals(chkBox1)) { //JTAF open source
//                        TEST_LIBRARY_DIR = "\\src\\test\\resources\\testlibrary";
//                        COMMANDS_DIR = "\\src\\test\\java";
//                    }
//                    if (chkBox.equals(chkBox2)) { //FINRA JTAF
//                        TEST_LIBRARY_DIR = "\\testlibrary";
//                        COMMANDS_DIR = "\\src\\main\\java";
//                    }
//                    if (chkBox.equals(chkBox3)) { //custom
//
//                    }
//                    //unselect other checkboxes
//                    for (Node c : toolBar.getItems()) {
//                        CheckBox unselect = (CheckBox) c;
//                        if (!chkBox.equals(unselect)) {
//                            if (unselect.isSelected())
//                                unselect.setSelected(false);
//                        }
//                    }
//                }
//                //load properties
//            });
//        }
//        chkBox1.fire();

        findProjectButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File defaultDirectory = new File(STARTING_DIR);
            directoryChooser.setTitle("Choose project directory");
            directoryChooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                PROJECT_DIR = selectedDirectory.getPath();
                if (FindExistingProject.search(PROJECT_DIR)) {
                    try {
                        new JTAFUIViewer(PROJECT_DIR, TEST_LIBRARY_DIR);
                    } catch (Exception e) {
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

        StackPane root = new StackPane();
        StackPane.setAlignment(jtafDescription, Pos.TOP_CENTER);
        StackPane.setMargin(jtafDescription, new Insets(30));
//        StackPane.setAlignment(toolBar, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(toolBarText, Pos.BOTTOM_LEFT);
        StackPane.setMargin(toolBarText, new Insets(0,0,40,20));
        root.getChildren().add(jtafDescription);
        root.getChildren().add(findProjectButton);
        root.getChildren().add(toolBarText);
//        root.getChildren().add(toolBar);
        primaryStage.setTitle("JTAF-UI Welcome Screen");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
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

    //does not include extra repositories
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

    private ListView<String> sortByLibrary() {
        final ListView<String> commandsView = new ListView<>();

        ArrayList<String> testLibraryCmds = new ArrayList<>();

        //all the commands searched
        for (String lib : commandNameLibrary.keySet()) {
            commandsView.getItems().add("TEST LIBRARY NAME: " + lib);
            for (String cl : commandNameLibrary.get(lib)) {
                String[] clSplit = cl.split("\\.");
                String fileName = clSplit[clSplit.length-1];
                testLibraryCmds.add(fileName);
                String status = " (NOT FOUND IN SOURCE)";

                //missing command or extra test library command
                if (totalCommands.contains(fileName)) {
                    System.out.println(fileName + " found!");
                    status = "";
                }
                commandsView.getItems().add(fileName+status);
            }
        }
        commandsView.getItems().add("");
        commandsView.getItems().add("NOT FOUND IN TEST LIBRARY:");
        //extra command or missing test library command
        for (String searched : totalCommands) {
            if (!testLibraryCmds.contains(searched)) {
                commandsView.getItems().add(searched);
            }
        }
        commandsView.setPrefHeight(800);
        commandsView.setOnMouseClicked(event -> {
            String clickedCommand = commandsView.getSelectionModel().getSelectedItem();
            System.out.println("Clicked on: " + clickedCommand);
        });
        return commandsView;
    }

    private ListView<String> sortByAll() {
        final ListView<String> commandsView = new ListView<>();
        commandsView.setItems(FXCollections.observableArrayList(totalCommands));
        commandsView.setPrefHeight(800);
        commandsView.setOnMouseClicked(event -> {
            String clickedCommand = commandsView.getSelectionModel().getSelectedItem();
            System.out.println("Clicked on: " + clickedCommand);
        });
        return commandsView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}