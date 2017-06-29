package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    private final String STARTING_DIR = "C:\\";
    private String PROJECT_DIR = "";
    private String TEST_LIBRARY_DIR; //change depending on version
    private String COMMANDS_DIR;
    private HashMap<String, Set<String>> testLibraryMap;
    ArrayList<String> totalCommands = new ArrayList<>();
    private HashMap<String,List<String>> commandNameLibrary = new HashMap<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        new JTAFViewer();
        Button btn = new Button();
        btn.setText("Find Existing Project");
        Text text = new Text();
        text.setText("Parameters for a JTAF project: Must contain logs, " +
                "profiles, src, target, and testscripts folders.");
        text.setWrappingWidth(280);

        Text toolBarText = new Text("Select version:");
        ToolBar toolBar = new ToolBar();
        CheckBox chkBox1 = new CheckBox();
        chkBox1.setText("JTAF open source");
        CheckBox chkBox2 = new CheckBox();
        chkBox2.setText("FINRA JTAF");
        CheckBox chkBox3 = new CheckBox();
        chkBox3.setText("custom loadout");
        toolBar.getItems().addAll(chkBox1, chkBox2, chkBox3);
        for (Node b : toolBar.getItems()) {
            CheckBox chkBox = (CheckBox) b;
            ((CheckBox) b).setOnAction(event -> {
                if (chkBox.isSelected()) {
                    System.out.println(chkBox.getText() + " selected!");
                    if (chkBox.equals(chkBox1)) { //JTAF open source
                        TEST_LIBRARY_DIR = "\\src\\test\\resources\\testlibrary";
                        COMMANDS_DIR = "\\src\\test\\java";
                    }
                    if (chkBox.equals(chkBox2)) { //FINRA JTAF
                        TEST_LIBRARY_DIR = "\\testlibrary";
                        COMMANDS_DIR = "\\src\\main\\java";
                    }
                    if (chkBox.equals(chkBox3)) { //custom

                    }
                    //unselect other checkboxes
                    for (Node c : toolBar.getItems()) {
                        CheckBox unselect = (CheckBox) c;
                        if (!chkBox.equals(unselect)) {
                            if (unselect.isSelected())
                                unselect.setSelected(false);
                        }
                    }
                }
                //load properties
            });
        }
        chkBox1.fire();

        btn.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File defaultDirectory = new File(STARTING_DIR);
            directoryChooser.setTitle("Choose project directory");
            directoryChooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                PROJECT_DIR = selectedDirectory.getPath();
                if (FindExistingProject.search(PROJECT_DIR)) {
//                    try {
////                        createProjectViewer();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (SAXException e) {
//                        e.printStackTrace();
//                    } catch (ParserConfigurationException e) {
//                        e.printStackTrace();
//                    }
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
        StackPane.setAlignment(text, Pos.TOP_CENTER);
        StackPane.setMargin(text, new Insets(30));
        StackPane.setAlignment(toolBar, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(toolBarText, Pos.BOTTOM_LEFT);
        StackPane.setMargin(toolBarText, new Insets(0,0,40,20));
        root.getChildren().add(text);
        root.getChildren().add(btn);
        root.getChildren().add(toolBarText);
        root.getChildren().add(toolBar);
        primaryStage.setTitle("JTAF-UI Welcome Screen");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

//    private void createProjectViewer() throws IOException, SAXException, ParserConfigurationException {
//        Group root = new Group();
//        Stage newStage = new Stage();
//        newStage.setScene(new Scene(root));
//        newStage.setTitle(PROJECT_DIR);
//        BorderPane defaultBorderPane = new BorderPane();
//        final TabPane tabPane = new TabPane();
//        tabPane.setPrefSize(1200, 800);
//        tabPane.setSide(Side.TOP);
//        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
//        final Tab tab1 = new Tab();
//        tab1.setText("Strategies");
//        tab1.setContent(createStrategiesPane());
//
//        final Tab tab2 = new Tab();
//        tab2.setText("Test Library");
//        tab2.setContent(createTestLibraryPane(tabPane));
//
//        final Tab tab3 = new Tab();
//        tab3.setText("Commands");
//        tab3.setContent(createCommandsPane());
//
//        final Tab tab4 = new Tab();
//        tab4.setText("JTAF Properties");
//
//        //tab 2
//        ScrollPane tab2Content = (ScrollPane) tab2.getContent();
////        ListView<String> testLibraryListView = (ListView<String>) tab2Content.getContent();
//        //tab 3
//        ScrollPane tab3Content = (ScrollPane) tab3.getContent();
//        VBox tab3Box = (VBox) tab3Content.getContent();
//        ButtonBar commandsButtonBar = (ButtonBar) tab3Box.getChildren().get(0);
//        Button btn1= (Button) commandsButtonBar.getButtons().get(0);
//        btn1.setOnMouseClicked(event -> {
//            tab3Box.getChildren().remove(1);
//            tab3Box.getChildren().add(sortByLibrary());
//            tab3Content.setContent(tab3Box);
//        });
//
//        Button btn2 = (Button) commandsButtonBar.getButtons().get(1);
//        btn2.setOnMouseClicked(event -> {
//            tab3Box.getChildren().remove(1);
//            tab3Box.getChildren().add(sortByAll());
//            tab3Content.setContent(tab3Box);
//        });
//        ComboBox<String> fileTypeBox = (ComboBox<String>) commandsButtonBar.getButtons().get(2);
////        Clicking on stuff
////        testLibraryListView.setOnMouseClicked(event -> {
////            String clickedLibrary = testLibraryListView.getSelectionModel().getSelectedItem();
////            System.out.println("Clicked on: " + clickedLibrary);
////        });
//        fileTypeBox.valueProperty().addListener((ov, before, after) -> {
//            System.out.println("Clicked: " + after);
//        });
//
//        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
//        defaultBorderPane.setCenter(tabPane);
//        root.getChildren().add(defaultBorderPane);
//        newStage.show();
//
////        StackPane projectViewer = new StackPane();
////        StackPane.setAlignment(toolBar, Pos.TOP_CENTER);
////        projectViewer.getChildren().add(toolBar);
////
////        Stage projectViewerStage = new Stage();
////        projectViewerStage.setTitle(selectedDirectory.getName());
////        projectViewerStage.setScene(new Scene(projectViewer, 1200, 800));
////        projectViewerStage.show();
//
//    }

    private Pane createStrategiesPane() {
        StackPane strategiesPane = new StackPane();
        return strategiesPane;
    }

//    private ScrollPane createTestLibraryPane(TabPane tabPane) throws ParserConfigurationException, IOException, SAXException {
//        ScrollPane testLibraryPane = new ScrollPane();
//        testLibraryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        testLibraryPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        testLibraryPane.setFitToWidth(true);
//
//        ArrayList<String> testLibraryCommands = new ArrayList<>();
//        File testLibraryPath = new File(PROJECT_DIR+TEST_LIBRARY_DIR);
//        if (!testLibraryPath.isDirectory()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("");
//            alert.setContentText("Invalid test library directory!");
//            alert.showAndWait();
//        } else {
//            testLibraryCommands = searchForCommands(testLibraryPath.listFiles(), testLibraryCommands, "", ".xml");
//            System.out.println("Searched test library: " + testLibraryCommands);
//        }
//
////        final ListView<String> testLibraryListView = new ListView<>();
////        testLibraryListView.setItems(FXCollections.observableArrayList(testLibraryCommands));
////        testLibraryListView.setPrefHeight(800);
//
//        Accordion testLibraryAccordion = new Accordion();
//        testLibraryMap = new HashMap<>();
//        for (String testLibraryName : testLibraryCommands) {
//            String testLibraryChild = PROJECT_DIR+TEST_LIBRARY_DIR+"\\"+testLibraryName;
//            LibraryParser testLibraryParser = null;
//            try {
//                testLibraryParser = new LibraryParser(testLibraryChild);
//                HashMap<String, HashMap<String, String>> commandChildren = testLibraryParser.getCommandChildren();
////                commandNameLibrary.put(testLibraryName, commandChildren.keySet());
//                testLibraryMap.put(testLibraryName, commandChildren.keySet()); //??
//                ArrayList<String> commandClassList = new ArrayList<>();
//
//                //iterates through each command in each test library
//                final ListView<String> commandListView = new ListView<>();
//                for (String commandName : commandChildren.keySet()) {
//                    HashMap<String, String> commandData = commandChildren.get(commandName);
//                    String tagName = commandData.get("tag").toUpperCase();
//                    commandListView.getItems().add(tagName);
//                    String className = "";
//                    if(commandData.get("class") != null) {
//                        className = " (" + commandData.get("class") + ")";
//                        commandClassList.add(commandData.get("class"));
//                    }
//                    commandListView.getItems().add("name: " + commandName+className);
//
//                    for (String c : commandData.keySet()) {
//                        if (!c.equals("class") && !c.equals("tag") && !c.equals("") &&
//                                !c.contains("optionalParameter") && !c.contains("requiredParameter"))
//                            commandListView.getItems().add(c + ": " + commandData.get(c));
//                    }
//                    if(!testLibraryParser.getRequiredParameters().isEmpty()) {
//                        commandListView.getItems().add("REQUIRED PARAMETER(S):\n");
//                        commandListView.getItems().add(testLibraryParser.getRequiredParameters().get(commandName));
//                    }
//                    if (!testLibraryParser.getOptionalParameters().isEmpty()) {
//                        commandListView.getItems().add("OPTIONAL PARAMETER(S):\n");
//                        for (String param : testLibraryParser.getOptionalParameters().keySet()) {
//                            commandListView.getItems().add(param);
//                        }
//                    }
//                    commandListView.getItems().add("");
//                }
//                commandNameLibrary.put(testLibraryName, commandClassList);
//
//                TitledPane testLibraryTP = new TitledPane(testLibraryName,commandListView);
//                testLibraryAccordion.getPanes().add(testLibraryTP);
////                System.out.println(commandChildren.keySet());
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (SAXException e) {
//                e.printStackTrace();
//            }
//        }
//
////        testLibraryListView.setOnMouseClicked(event -> {
////            String clickedLibrary = testLibraryListView.getSelectionModel().getSelectedItem();
////            System.out.println("Clicked on: " + clickedLibrary);
////            tabPane.getSelectionModel().select(2);
////        });
//        testLibraryPane.setContent(testLibraryAccordion);
//        return testLibraryPane;
//    }

    private ScrollPane createCommandsPane() throws ParserConfigurationException, IOException, SAXException {
        ScrollPane commandsPane = new ScrollPane();
        commandsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        commandsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        commandsPane.setFitToWidth(true);

        File commandsPath = new File(PROJECT_DIR+COMMANDS_DIR);
        if (!commandsPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid command directory!");
            alert.showAndWait();
        } else {
            totalCommands = searchForCommands(commandsPath.listFiles(), totalCommands, ".java");
            ArrayList<String> newTotalCommands = new ArrayList<>();
            for(String command : totalCommands) {
                if (command.toLowerCase().endsWith("cmd.java") || command.toLowerCase().endsWith("command.java")) { //need to improve
                    newTotalCommands.add(command.replace(".java",""));
                }
            }
            totalCommands = newTotalCommands;
            System.out.println("Searched for commands: " + totalCommands);
        }
        ListView<String> commandsView = sortByAll();

        ButtonBar buttonBar = new ButtonBar();
        Button sortBtn1 = new Button();
        sortBtn1.setText("Sort by library");
        Button sortBtn2 = new Button();
        sortBtn2.setText("Sort by all");
        ComboBox<String> fileTypeBox = new ComboBox<>();
        fileTypeBox.setItems(FXCollections.observableArrayList("class name", "test library name"));
        fileTypeBox.setEditable(false);
        fileTypeBox.setValue(fileTypeBox.getItems().get(0));
        buttonBar.getButtons().addAll(sortBtn1, sortBtn2, fileTypeBox);

        VBox commandsBox = new VBox();
        commandsBox.getChildren().add(buttonBar);
        commandsBox.getChildren().add(commandsView);
        commandsPane.setContent(commandsBox);

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