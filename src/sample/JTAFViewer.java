package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
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

/**
 * Created by Alex on 6/23/2017.
 */
public class JTAFViewer extends Stage {
    public double LIBRARY_TOTAL_WIDTH;
    public double LIBRARY_KEY_WIDTH;
    public double LIBRARY_DATA_WIDTH;

    private String PROJECT_DIR;
    private final String TEST_LIBRARY_DIR; //change depending on version
    private Node[] centerState = new Node[2]; //update basted on tabs
    private Node[] directoryState = new Node[2]; //update based on tabs
    ArrayList<String> totalCommands = new ArrayList<>();
    HashMap<String, ScrollPane> testLibraryMap = new HashMap<>();

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
        HashMap<String, Stage> totalCommandWindows = new HashMap<>();
        HashMap<String, Stage> totalFunctionWindows = new HashMap<>();

        //directory buttons
        for (int i = 0; i < testLibrary.size(); i++) {
            JTAFLibrary jtafLibrary = new JTAFLibrary(PROJECT_DIR + TEST_LIBRARY_DIR + "\\" + testLibrary.get(i));
            //dimensions
            LIBRARY_TOTAL_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
            LIBRARY_KEY_WIDTH = jtafLibrary.LIBRARY_KEY_WIDTH;
            LIBRARY_DATA_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
            ScrollPane libraryPane = jtafLibrary.getLibraryPane();
            testLibraryMap.put(testLibrary.get(i), libraryPane); //maps library name to library pane for directory buttons
            HashMap<String, ArrayList<StackPane>> libraryBodyPanes = jtafLibrary.getLibraryBodyPanes(); //maps library name to body panes
            HashMap<String, ArrayList<StackPane>> windowedLibraryBodyPanes = jtafLibrary.getWindowedLibraryBodyPanes(); //maps windowed library name to body panes
            HashMap<String, Stage> commandWindows = jtafLibrary.getCommandWindows(); //maps command to stage/window
            HashMap<String, Stage> functionWindows = jtafLibrary.getFunctionWindows(); //maps function to stage/window
            totalLibraryBodyPanes.putAll(libraryBodyPanes);
            totalWindowedLibraryBodyPanes.putAll(windowedLibraryBodyPanes);
            totalCommandWindows.putAll(commandWindows);
            totalFunctionWindows.putAll(functionWindows);
        }

        //fill out library function bodies
        for (String libraryName : totalLibraryBodyPanes.keySet()) {
            ArrayList<StackPane> libraryBodyPanes = totalLibraryBodyPanes.get(libraryName); //gets panes of each library
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

        //fill out windowed library function bodies
        for (String libraryName : totalWindowedLibraryBodyPanes.keySet()) {
            ArrayList<StackPane> windowedLibraryBodyPanes = totalWindowedLibraryBodyPanes.get(libraryName); //gets panes of each library
            //starts at index 2, skips library header
            for (int i = 2; i < windowedLibraryBodyPanes.size(); i += 2) {
                Text bodyChildNameText = (Text) windowedLibraryBodyPanes.get(i).getChildren().get(0);
                String bodyChild = bodyChildNameText.getText(); //name of command or function
//                checks map and then gets window from command/function name
//                if (totalCommandWindows.keySet().contains(bodyChild)) {
//                    Hyperlink openBodyChild = new Hyperlink("Open Command");
//                    openBodyChild.setOnMouseClicked(event -> {
//                        Stage window = totalCommandWindows.get(bodyChild);
//                        if (window.isShowing())
//                            window.close();
//                        window.show();
//                    });
//                    libraryBodyPanes.get(i+1).getChildren().add(openBodyChild); //set
//                }
//                else if (totalFunctionWindows.keySet().contains(bodyChild)) {
//                    Hyperlink openBodyChild = new Hyperlink("Open Function");
//                    openBodyChild.setOnMouseClicked(event -> {
//                        Stage window = totalFunctionWindows.get(bodyChild);
//                        if (window.isShowing())
//                            window.close();
//                        window.show();
//                    });
//                    libraryBodyPanes.get(i+1).getChildren().add(openBodyChild); //set
//                }
//                else {
                Text bodyChildNotFound = new Text("Not found!");
                windowedLibraryBodyPanes.get(i + 1).getChildren().add(bodyChildNotFound); //set
//                }
            }
        }

        ScrollPane emptyLibraryPane = new ScrollPane();
        centerState[0] = emptyLibraryPane;
        emptyLibraryPane.setPrefSize(LIBRARY_TOTAL_WIDTH, 600);

        HBox headerBox = new JTAFHeader(LIBRARY_TOTAL_WIDTH).getHeaderBox();

        setDirectoryPaneEvents(defaultBorderPane, headerBox, directoryPane);
        setHeaderBoxEvents(defaultBorderPane, headerBox, directoryPane);

        //create other tabs
//        HBox viewerHeader = createViewerHeader(defaultBorderPane, libraryPane);

        defaultBorderPane.setTop(headerBox);
        defaultBorderPane.setCenter(emptyLibraryPane);
        defaultBorderPane.setLeft(directoryPane);

        root.getChildren().add(defaultBorderPane);
        jtafStage.show();
    }

    public void setDirectoryPaneEvents(BorderPane defaultBorderPane, HBox headerBox, ScrollPane directoryPane) {
        VBox directoryVBox = (VBox) directoryPane.getContent();

        VBox headerRightHalfBox = (VBox) headerBox.getChildren().get(1);
        StackPane libraryNamePane = (StackPane) headerRightHalfBox.getChildren().get(1);
        Label headerLabel = (Label) libraryNamePane.getChildren().get(0);

        for (Node node : directoryVBox.getChildren()) {
            ToggleButton libraryPathButton = (ToggleButton) node;
            libraryPathButton.setOnMouseClicked(event -> {
                toggleUnselectAll(libraryPathButton, directoryVBox.getChildren());
                toggleSelect(libraryPathButton);
                if (testLibraryMap.containsKey(libraryPathButton.getText())) {
                    headerLabel.setText(libraryPathButton.getText()); //changes label in header
                    defaultBorderPane.setCenter(testLibraryMap.get(libraryPathButton.getText()));
                    centerState[0] = testLibraryMap.get(libraryPathButton.getText());
                }

                if (!libraryPathButton.isSelected()) {
                    libraryPathButton.setSelected(true);
                }
            });
        }
    }

    public void setHeaderBoxEvents(BorderPane defaultBorderPane, HBox headerBox, ScrollPane directoryPane) {
        //HBox -> VBox -> BorderPane -> HBox
        VBox headerRightHalfBox = (VBox) headerBox.getChildren().get(1);
        BorderPane tabBarPane = (BorderPane) headerRightHalfBox.getChildren().get(0);
        HBox tabBarBox = (HBox) tabBarPane.getChildren().get(0);

        for (Node node : tabBarBox.getChildren()) {
            if (node.getClass().equals(ToggleButton.class)) {
                ToggleButton tabButton = (ToggleButton) node;
                tabButton.setOnMouseClicked(event -> {
                    //unselect everything else
                    toggleUnselectAll(tabButton, tabBarBox.getChildren());
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

//        tabButton1.setOnMouseClicked(event -> {
//            tabButton2.setSelected(false);
//            tabButton2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//
//            tabButton1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            defaultBorderPane.setCenter(tab1);
//            //keeps selected
//            if(!tabButton1.isSelected()) {
//                tabButton1.setSelected(true);
//            }
//        });
//
//        tabButton2.setOnMouseClicked(event -> {
//            tabButton1.setSelected(false);
//            tabButton1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//
//            tabButton2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            defaultBorderPane.setCenter(new VBox());
//            //keeps selected
//            if(!tabButton2.isSelected()) {
//                tabButton2.setSelected(true);
//            }
//        });
    }

    public void toggleUnselectAll(ToggleButton selectedButton, List<Node> otherButtons) {
        for (Node node : otherButtons) {
            ToggleButton unselectedButton = (ToggleButton) node;
            if (!unselectedButton.equals(selectedButton)) {
                unselectedButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                unselectedButton.setSelected(false);
            }
        }
    }

    public void toggleSelect(ToggleButton selectedButton) {
        selectedButton.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}