package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Alex on 6/23/2017.
 */
public class JTAFViewer extends Stage {
    public double LIBRARY_TOTAL_WIDTH;
    public double LIBRARY_KEY_WIDTH;
    public double LIBRARY_DATA_WIDTH;
    public Color HEADER_COLOR = Color.RED;
    public Color HEADER_UNSELECTED_COLOR = Color.GRAY;

    private String PROJECT_DIR;
    private final String TEST_LIBRARY_DIR; //change depending on version
    private Node[] centerState = new Node[2]; //update basted on tabs
    private Node[] directoryState = new Node[2]; //update based on tabs
    private HashMap<String, ArrayList<String>> totalTestLibrary = new HashMap<>();
    private StackPane lastResult = null;

    JTAFViewer(String projectPath, String libraryPath) throws ParserConfigurationException, SAXException, IOException {
        this.PROJECT_DIR = projectPath;
        this.TEST_LIBRARY_DIR = libraryPath;

        Group root = new Group();
        Stage jtafStage = new Stage();
        jtafStage.setScene(new Scene(root));
        jtafStage.setTitle(PROJECT_DIR);

        BorderPane defaultBorderPane = new BorderPane();
        defaultBorderPane.setPrefHeight(600);

        //get all library paths
        JTAFDirectory directory = new JTAFDirectory(PROJECT_DIR, TEST_LIBRARY_DIR); //add more paths later.
        ArrayList<String> testLibrary = directory.getTestLibrary();
        ScrollPane directoryPane = directory.getDirectoryPane();
        directoryState[0] = directoryPane;

        HashMap<String, ArrayList<StackPane>> totalLibraryBodyPanes = new HashMap<>();
        HashMap<String, ArrayList<StackPane>> totalWindowedLibraryBodyPanes = new HashMap<>();
        HashMap<String, Stage> totalCommandWindows = new HashMap<>(); //maps name to stage (keyset = all commands)
        HashMap<String, Stage> totalFunctionWindows = new HashMap<>(); //keyset = all functions
        HashMap<String, ScrollPane> testLibraryMap = new HashMap<>(); //maps library name to library scrollpane

        for (int i = 0; i < testLibrary.size(); i++) {
            //creates gridpanes for library
            JTAFLibrary jtafLibrary = new JTAFLibrary(PROJECT_DIR + TEST_LIBRARY_DIR + "\\" + testLibrary.get(i));
            totalTestLibrary.put(testLibrary.get(i), jtafLibrary.getLibrary());
            //dimensions
            getLibraryGlobalVars(jtafLibrary);
            //maps library name to library pane for directory buttons
            ScrollPane libraryPane = jtafLibrary.getLibraryPane();
            testLibraryMap.put(testLibrary.get(i), libraryPane);
            getLibraryBody(jtafLibrary, totalLibraryBodyPanes, totalWindowedLibraryBodyPanes);
            getLibraryWindows(jtafLibrary, totalCommandWindows, totalFunctionWindows);
        }

        //fill out windowed library function bodies
        setLibraryBodyHyperLinks(totalLibraryBodyPanes, totalCommandWindows, totalFunctionWindows);
        setLibraryBodyHyperLinks(totalWindowedLibraryBodyPanes, totalCommandWindows, totalFunctionWindows);

        ScrollPane emptyLibraryPane = new ScrollPane();
        centerState[0] = emptyLibraryPane;
        emptyLibraryPane.setPrefSize(LIBRARY_TOTAL_WIDTH, 400);

        JTAFHeader jtafHeader = new JTAFHeader(LIBRARY_TOTAL_WIDTH);
        HBox headerBox = jtafHeader.getHeaderBox();
        HBox tabsBox = jtafHeader.getTabsBox();
        Label headerLabel = jtafHeader.getHeaderLabel();
        this.HEADER_COLOR = jtafHeader.HEADER_COLOR;
        this.HEADER_UNSELECTED_COLOR = jtafHeader.HEADER_UNSELECTED_COLOR;

        setTestLibraryDirectoryEvents(defaultBorderPane, headerLabel, directoryPane, testLibraryMap);
        setTabEvents(defaultBorderPane, tabsBox, directoryPane);
        setSearchBarEvents(defaultBorderPane, jtafHeader.getSearchBarBox(), directoryPane, testLibraryMap);

        defaultBorderPane.setTop(headerBox);
        defaultBorderPane.setCenter(emptyLibraryPane);
        defaultBorderPane.setLeft(directoryPane);

        root.getChildren().add(defaultBorderPane);
        jtafStage.show();
    }

    public void getLibraryGlobalVars(JTAFLibrary jtafLibrary) {
        LIBRARY_TOTAL_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
        LIBRARY_KEY_WIDTH = jtafLibrary.LIBRARY_KEY_WIDTH;
        LIBRARY_DATA_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
        //add more?
    }

    public void getLibraryWindows(JTAFLibrary jtafLibrary, HashMap<String, Stage> totalCommandWindows,
                                  HashMap<String, Stage> totalFunctionWindows) {
        totalCommandWindows.putAll(jtafLibrary.getCommandWindows());
        totalFunctionWindows.putAll(jtafLibrary.getFunctionWindows());
    }

    public void getLibraryBody(JTAFLibrary jtafLibrary, HashMap<String, ArrayList<StackPane>> totalLibraryBodyPanes,
                               HashMap<String, ArrayList<StackPane>> totalWindowedLibraryBodyPanes) {
        totalLibraryBodyPanes.putAll(jtafLibrary.getLibraryBodyPanes());
        totalWindowedLibraryBodyPanes.putAll(jtafLibrary.getWindowedLibraryBodyPanes());
    }

    public void setLibraryBodyHyperLinks(HashMap<String, ArrayList<StackPane>> bodyPanes,
        HashMap<String, Stage> totalCommandWindows, HashMap<String, Stage> totalFunctionWindows) {
        //fill out library function bodies
        for (String libraryName : bodyPanes.keySet()) {
            ArrayList<StackPane> libraryBodyPanes = bodyPanes.get(libraryName); //gets panes of each library
            //starts at index 2, skips library header
            for (int i = 2; i < libraryBodyPanes.size(); i += 2) {
                Text bodyChildNameText = (Text) libraryBodyPanes.get(i).getChildren().get(0);
                String bodyChild = bodyChildNameText.getText(); //name of command or function
                //checks map and then gets window from command/function name
                if (totalCommandWindows.keySet().contains(bodyChild)) {
                    Hyperlink openBodyChild = new Hyperlink("Open Command");
                    openBodyChild.setOnMouseClicked(event -> {
                        Stage window = totalCommandWindows.get(bodyChild);
                        if (window.isShowing())
                            window.close();
                        window.show();
                    });
                    libraryBodyPanes.get(i + 1).getChildren().add(openBodyChild); //set
                } else if (totalFunctionWindows.keySet().contains(bodyChild)) {
                    Hyperlink openBodyChild = new Hyperlink("Open Function");
                    openBodyChild.setOnMouseClicked(event -> {
                        Stage window = totalFunctionWindows.get(bodyChild);
                        if (window.isShowing())
                            window.close();
                        window.show();
                    });
                    libraryBodyPanes.get(i + 1).getChildren().add(openBodyChild); //set
                } else {
                    Text bodyChildNotFound = new Text("Not found!");
                    libraryBodyPanes.get(i + 1).getChildren().add(bodyChildNotFound); //set
                }
            }
        }
    }

    //set pane events
    public void setTestLibraryDirectoryEvents(BorderPane defaultBorderPane, Label headerLabel, ScrollPane directoryPane,
                                   HashMap<String, ScrollPane> testLibraryMap) {
        VBox directoryVBox = (VBox) directoryPane.getContent();

        for (Node node : directoryVBox.getChildren()) {
            ToggleButton libraryPathButton = (ToggleButton) node;
            libraryPathButton.setOnAction(event -> {
                toggleUnselectAll(libraryPathButton, directoryVBox.getChildren());
                toggleSelect(libraryPathButton);
//                if (testLibraryMap.containsKey(libraryPathButton.getText())) {
                    headerLabel.setText(libraryPathButton.getText()); //changes label in header
                    defaultBorderPane.setCenter(testLibraryMap.get(libraryPathButton.getText()));
                    centerState[0] = testLibraryMap.get(libraryPathButton.getText());
//                }

                if (!libraryPathButton.isSelected()) {
                    libraryPathButton.setSelected(true);
                }
            });
        }
    }

    public void setTabEvents(BorderPane defaultBorderPane, HBox tabsBox, ScrollPane directoryPane) {
        //HBox -> VBox -> BorderPane -> HBox

        for (Node node : tabsBox.getChildren()) {
            if (node.getClass().equals(ToggleButton.class)) {
                ToggleButton tabButton = (ToggleButton) node;
                tabButton.setOnMouseClicked(event -> {
                    //unselect everything else
                    toggleUnselectAll(tabButton, tabsBox.getChildren());
                    //select tabButton
                    toggleSelect(tabButton);
                    if (tabButton.getText().equals("Test Library")) {
                        defaultBorderPane.setCenter(centerState[0]);
                        defaultBorderPane.setLeft(directoryState[0]);
                    }
                    if (tabButton.getText().equals("Something1")) {
                        System.out.println("Do something");
                        //ScrollPane libraryPane = (ScrollPane) centerState[0];
                        //libraryPane.setVvalue(libraryPane.getVmax());
                        //defaultBorderPane.setCenter(libraryPane);
                    }
                    if (!tabButton.isSelected()) {
                        tabButton.setSelected(true);
                    }
                });
            }
        }
    }

    public void setSearchBarEvents(BorderPane defaultBorderPane, HBox searchBarBox,
                                   ScrollPane directoryPane, HashMap<String, ScrollPane> testLibraryMap) {
        VBox directoryVBox = (VBox) directoryPane.getContent();

        TextField searchTextField = (TextField) searchBarBox.getChildren().get(0);
        Button searchButton = (Button) searchBarBox.getChildren().get(1);

        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String searchedName = searchTextField.getText();
                if (!searchedName.isEmpty()) {
                    System.out.println("Searched for: "+searchedName);
                    String libraryName = searchTestLibrary(searchedName);
                    if(!libraryName.isEmpty()) {
//                        defaultBorderPane.setCenter(testLibraryMap.get(libraryName));
//                        centerState[0] = testLibraryMap.get(libraryName);
                        System.out.println(searchedName +" found in "+libraryName+"!");
                        //update header and directory
                        for (Node node : directoryVBox.getChildren()) {
                            ToggleButton libraryPathButton = (ToggleButton) node;
                            if (libraryPathButton.getText().equals(libraryName)) {
                                libraryPathButton.fire();
                                getSearchResult(testLibraryMap, libraryName, searchedName); //must be after library pane is set
                                break;
                            }
                        }
                    } else {
                        //set scrollpane as empty.
                    }
                }
            }
        });
    }

    public void toggleUnselectAll(ToggleButton selectedButton, List<Node> otherButtons) {
        for (Node node : otherButtons) {
            ToggleButton unselectedButton = (ToggleButton) node;
            if (!unselectedButton.equals(selectedButton)) {
                unselectedButton.setBackground(new Background(new BackgroundFill(HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                unselectedButton.setSelected(false);
            }
        }
    }

    public void toggleSelect(ToggleButton selectedButton) {
        selectedButton.setBackground(new Background(new BackgroundFill(HEADER_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public String searchTestLibrary(String name) {
        name = name.toLowerCase();
        name = name.trim();
        if (name.endsWith(".xml")) {
            name = name.substring(0, name.length()-4);
        }
        for (String libraryName : totalTestLibrary.keySet()) {
            ArrayList<String> testLibraryChildren = totalTestLibrary.get(libraryName);
            for (String child : testLibraryChildren) {
                if (name.equalsIgnoreCase(child)) {
                    return libraryName;
                }
            }
        }
        return "";
    }

    public void getSearchResult(HashMap<String, ScrollPane> testLibraryMap, String libraryName, String searchedName) {
        //ScrollPane -> VBox -> VBox -> StackPane
//        ScrollPane libraryPane = testLibraryMap.get(libraryName);
        ScrollPane libraryPane = (ScrollPane) centerState[0];
        VBox libraryBox = (VBox) libraryPane.getContent();
        VBox commandBox = (VBox) libraryBox.getChildren().get(0);
        VBox functionBox = (VBox) libraryBox.getChildren().get(1);
        double defaultLibraryPaneHeight = 400; //TODO
        double actualLibraryPaneHeight = 0; //height of command header (is variable)
        double resultHeight = 0;
        if (!commandBox.getChildren().isEmpty()) {
            actualLibraryPaneHeight += 44;
            for (int i = 1; i < commandBox.getChildren().size(); i++) {
                if (commandBox.getChildren().get(i).getClass().equals(StackPane.class)) {
                    actualLibraryPaneHeight += 42; //StackPane == header
                    StackPane commandHeader = (StackPane) commandBox.getChildren().get(i);
                    Text commandHeaderText = (Text) commandHeader.getChildren().get(0);
                    if(commandHeaderText.getText().equalsIgnoreCase(searchedName)) {
                        resultHeight = actualLibraryPaneHeight;
                        minimizeHeaders(commandBox);

                        GridPane commandGridPane = (GridPane) commandBox.getChildren().get(i+1);
                        commandGridPane.setVisible(true);
                        commandGridPane.setManaged(true);

                        commandHeader.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))); //TODO: set effect
                        if (lastResult != null) {
                            if (!lastResult.equals(commandHeader))
                                lastResult.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); //TODO: set effect
                        }
                        lastResult = commandHeader;
                    }
                }
            }
        }
        if (!functionBox.getChildren().isEmpty()) {
            actualLibraryPaneHeight += 44;
            for (int i = 1; i < functionBox.getChildren().size(); i++) {
                if (functionBox.getChildren().get(i).getClass().equals(StackPane.class)) {
                    actualLibraryPaneHeight += 42; //StackPane == header
                    StackPane functionHeader = (StackPane) functionBox.getChildren().get(i);
                    Text functionHeaderText = (Text) functionHeader.getChildren().get(0);
                    if(functionHeaderText.getText().equalsIgnoreCase(searchedName)) {
                        resultHeight = actualLibraryPaneHeight;
                        minimizeHeaders(functionBox);

                        GridPane functionGridPane = (GridPane) functionBox.getChildren().get(i+1);
                        functionGridPane.setVisible(true);
                        functionGridPane.setManaged(true);

                        functionHeader.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))); //TODO: set effect
                        if (lastResult != null) {
                            if (!lastResult.equals(functionHeader))
                                lastResult.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); //TODO: set effect
                        }
                        lastResult = functionHeader;
                    }
                }
            }
        }
        if (resultHeight > defaultLibraryPaneHeight) { //result header is outside of view
            if (actualLibraryPaneHeight > defaultLibraryPaneHeight)
                libraryPane.setVvalue(resultHeight / actualLibraryPaneHeight);
            else libraryPane.setVvalue(resultHeight / defaultLibraryPaneHeight);
        }

    }

    public void minimizeHeaders(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(false);
                node.setManaged(false);
            }
        }
    }

}