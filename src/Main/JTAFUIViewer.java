package Main;

import JTAF.Library;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 6/23/2017.
 */
public class JTAFUIViewer extends Stage {
    public double LIBRARY_TOTAL_WIDTH;
    public double LIBRARY_KEY_WIDTH;
    public double LIBRARY_DATA_WIDTH;
    public Color LIBRARY_BACKGROUND_COLOR;
    public Color TAB_SELECTED_COLOR;
    public Color TAB_UNSELECTED_COLOR;
    public Color DIRECTORY_SELECTED_COLOR;
    public Color DIRECTORY_UNSELECTED_COLOR;
    public Color LIBRARY_HEADER_SELECTED_COLOR;
    public Color LIBRARY_HEADER_UNSELECTED_COLOR;
    public Color LIBRARY_SEARCH_COLOR;
    public Color LIBRARY_DUPLICATE_COLOR;
    private final String PROJECT_DIR;
    private final String TEST_LIBRARY_DIR; //change depending on version
    public ArrayList<Library> testLibrary;
    public ArrayList<JTAFUILibrary> testLibraryUI = new ArrayList<>();
    private Node[] centerState = new Node[2]; //update basted on tabs
    private Node[] directoryState = new Node[2]; //update based on tabs

    JTAFUIViewer(String projectPath, String libraryPath) throws Exception {
        this.PROJECT_DIR = projectPath;
        this.TEST_LIBRARY_DIR = libraryPath;
        testLibrary = getTestLibrary(); //sets ArrayList of Libraries

        Group root = new Group();
        Stage jtafStage = new Stage();
        jtafStage.setScene(new Scene(root));
        jtafStage.setTitle(PROJECT_DIR);

        BorderPane defaultBorderPane = new BorderPane();
        defaultBorderPane.setPrefHeight(600);
//        HashMap<String, ArrayList<StackPane>> totalLibraryBodyPanes = new HashMap<>();
//        HashMap<String, ArrayList<StackPane>> totalWindowedLibraryBodyPanes = new HashMap<>();
//        HashMap<String, Stage> totalCommandWindows = new HashMap<>(); //maps name to stage (keyset = all commands)
//        HashMap<String, Stage> totalFunctionWindows = new HashMap<>(); //keyset = all functions
//        HashMap<String, ScrollPane> testLibraryMap = new HashMap<>(); //maps library name to library scrollpane

        for (int i = 0; i < testLibrary.size(); i++) {
            //creates gridpanes for library
//            JTAFLibrary jtafLibrary = new JTAFLibrary(PROJECT_DIR+TEST_LIBRARY_DIR+"\\"+testLibrary.get(i).getLibraryName());
            JTAFUILibrary jtafLibrary = new JTAFUILibrary(testLibrary.get(i));

            //dimensions
            getLibraryGlobalVars(jtafLibrary);

            //maps library name to library pane for directory buttons
//            ScrollPane libraryPane = jtafLibrary.getLibraryPane();
            testLibraryUI.add(jtafLibrary);
//            getLibraryBody(jtafLibrary, totalLibraryBodyPanes, totalWindowedLibraryBodyPanes);
//            getLibraryWindows(jtafLibrary, totalCommandWindows, totalFunctionWindows);
        }

        //fill out windowed library function bodies
//        setLibraryBodyHyperLinks(totalLibraryBodyPanes, totalCommandWindows, totalFunctionWindows);
//        setLibraryBodyHyperLinks(totalWindowedLibraryBodyPanes, totalCommandWindows, totalFunctionWindows);

//        ScrollPane emptyLibraryPane = new ScrollPane();
//        emptyLibraryPane.setContent(emptyPane);
//        centerState[0] = emptyLibraryPane;
//        emptyLibraryPane.setPrefSize(LIBRARY_TOTAL_WIDTH, 500);

        StackPane emptyPane = new StackPane();
        emptyPane.setPrefHeight(500);
        emptyPane.setBackground(new Background(new BackgroundFill(LIBRARY_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        centerState[0] = emptyPane;

        JTAFUIHeader jtafHeader = new JTAFUIHeader(LIBRARY_TOTAL_WIDTH);
        jtafHeader.setTabEvents(directoryState, centerState, defaultBorderPane);
        Label headerLabel = jtafHeader.getHeaderLabel();
        HBox headerBox = jtafHeader.getHeaderBox();
        this.TAB_SELECTED_COLOR = jtafHeader.TAB_SELECTED_COLOR;
        this.TAB_UNSELECTED_COLOR = jtafHeader.TAB_UNSELECTED_COLOR;

        JTAFUIDirectory directory = new JTAFUIDirectory();
        directory.setAsTestLibrary(testLibrary, directoryState);
        directory.setDirectoryPaneEvents(testLibraryUI, headerLabel, centerState, defaultBorderPane);
        this.DIRECTORY_SELECTED_COLOR = directory.DIRECTORY_SELECTED_COLOR;
        this.DIRECTORY_UNSELECTED_COLOR = directory.DIRECTORY_UNSELECTED_COLOR;

        ScrollPane directoryPane = directory.getDirectoryPane();
        jtafHeader.setTestLibrarySearch(directory.getDirectoryButtons(),testLibraryUI);

//        setTabEvents(defaultBorderPane, tabsBox, directoryPane);
//        setSearchBarEvents(defaultBorderPane, jtafHeader.getSearchBarBox(), directoryPane, testLibraryMap);

        defaultBorderPane.setTop(headerBox);
        defaultBorderPane.setCenter(emptyPane);
        defaultBorderPane.setLeft(directoryPane);

        root.getChildren().add(defaultBorderPane);
        jtafStage.show();
    }

    private ArrayList<String> searchLibrary(File[] fileList, ArrayList<String> arrayList, String directoryName, String typeOfFile) throws IOException {
        for (File file : fileList) {
            if (file.isDirectory()) {
                arrayList = searchLibrary(file.listFiles(), arrayList, directoryName+file.getName()+"\\", typeOfFile); //adds directories to path
            }
            else if(file.getName().endsWith(typeOfFile))
                arrayList.add(directoryName+file.getName());
        }
        return arrayList;
    }

    public ArrayList<Library> getTestLibrary() throws Exception {
        File testLibraryPath = new File(PROJECT_DIR+TEST_LIBRARY_DIR);
        ArrayList<String> libraryPaths = new ArrayList<>();
        if (!testLibraryPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid test library directory!");
            alert.showAndWait();
        } else {
            libraryPaths = searchLibrary(testLibraryPath.listFiles(), libraryPaths, "", ".xml");
            System.out.println("Searched test library: " + testLibraryPath.getPath());
        }

        ArrayList<Library> testLibrary = new ArrayList<>();
        for (String libraryPath : libraryPaths) {
            LibraryParser libraryParser = new LibraryParser(testLibraryPath.getPath()+"\\"+libraryPath);
            testLibrary.add(libraryParser.getLibrary());
        }

        return testLibrary;
    }

    public void getLibraryGlobalVars(JTAFUILibrary jtafLibrary) {
        this.LIBRARY_TOTAL_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
        this.LIBRARY_KEY_WIDTH = jtafLibrary.LIBRARY_KEY_WIDTH;
        this.LIBRARY_DATA_WIDTH = jtafLibrary.LIBRARY_TOTAL_WIDTH;
        this.LIBRARY_HEADER_SELECTED_COLOR  = jtafLibrary.LIBRARY_HEADER_SELECTED_COLOR;
        this.LIBRARY_HEADER_UNSELECTED_COLOR = jtafLibrary.LIBRARY_HEADER_UNSELECTED_COLOR;
        this.LIBRARY_BACKGROUND_COLOR = jtafLibrary.LIBRARY_BACKGROUND_COLOR;
        //add more?
    }

//    public void getLibraryWindows(JTAFUILibrary jtafLibrary, HashMap<String, Stage> totalCommandWindows,
//                                  HashMap<String, Stage> totalFunctionWindows) {
//        totalCommandWindows.putAll(jtafLibrary.getCommandWindows());
//        totalFunctionWindows.putAll(jtafLibrary.getFunctionWindows());
//    }
//
//    public void getLibraryBody(JTAFUILibrary jtafLibrary, HashMap<String, ArrayList<StackPane>> totalLibraryBodyPanes,
//                               HashMap<String, ArrayList<StackPane>> totalWindowedLibraryBodyPanes) {
//        totalLibraryBodyPanes.putAll(jtafLibrary.getLibraryBodyPanes());
//        totalWindowedLibraryBodyPanes.putAll(jtafLibrary.getWindowedLibraryBodyPanes());
    }

//    public void setLibraryBodyHyperLinks(HashMap<String, ArrayList<StackPane>> bodyPanes,
//        HashMap<String, Stage> totalCommandWindows, HashMap<String, Stage> totalFunctionWindows) {
//        //fill out library function bodies
//        for (String libraryName : bodyPanes.keySet()) {
//            ArrayList<StackPane> libraryBodyPanes = bodyPanes.get(libraryName); //gets panes of each library
//            //starts at index 2, skips library header
//            for (int i = 2; i < libraryBodyPanes.size(); i += 2) {
//                Text bodyChildNameText = (Text) libraryBodyPanes.get(i).getChildren().get(0);
//                String bodyChild = bodyChildNameText.getText(); //name of command or function
//                //checks map and then gets window from command/function name
//                if (totalCommandWindows.keySet().contains(bodyChild)) {
//                    Hyperlink openBodyChild = new Hyperlink("Open Command");
//                    openBodyChild.setOnMouseClicked(event -> {
//                        Stage window = totalCommandWindows.get(bodyChild);
//                        if (window.isShowing())
//                            window.close();
//                        window.show();
//                    });
//                    libraryBodyPanes.get(i + 1).getChildren().add(openBodyChild); //set
//                } else if (totalFunctionWindows.keySet().contains(bodyChild)) {
//                    Hyperlink openBodyChild = new Hyperlink("Open Function");
//                    openBodyChild.setOnMouseClicked(event -> {
//                        Stage window = totalFunctionWindows.get(bodyChild);
//                        if (window.isShowing())
//                            window.close();
//                        window.show();
//                    });
//                    libraryBodyPanes.get(i + 1).getChildren().add(openBodyChild); //set
//                } else {
//                    Text bodyChildNotFound = new Text("Not found!");
//                    libraryBodyPanes.get(i + 1).getChildren().add(bodyChildNotFound); //set
//                }
//            }
//        }
//    }
