package sample;

import JTAF.Command;
import JTAF.Function;
import JTAF.Parameter;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 6/23/2017.
 */
public class JTAFViewer extends Stage {
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
            JTAFLibrary jtafLibrary = new JTAFLibrary(PROJECT_DIR+TEST_LIBRARY_DIR+"\\"+testLibrary.get(i));
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
        for(String libraryName : totalLibraryBodyPanes.keySet()) {
            ArrayList<StackPane> libraryBodyPanes = totalLibraryBodyPanes.get(libraryName); //gets panes of each library
            //starts at index 2, skips library header
            for (int i = 2; i < libraryBodyPanes.size(); i+=2) {
                Text bodyChildNameText = (Text) libraryBodyPanes.get(i).getChildren().get(0);
                String bodyChild = bodyChildNameText.getText(); //name of command or function
               //checks map and then gets window from command/function name
                System.out.println(bodyChild + " " + totalFunctionWindows.keySet());

                if (totalCommandWindows.keySet().contains(bodyChild)) {
                    Hyperlink openBodyChild = new Hyperlink("Open Command");
                    openBodyChild.setOnMouseClicked(event -> {
                        Stage window = totalCommandWindows.get(bodyChild);
                        if (window.isShowing())
                            window.close();
                        window.show();
                    });
                    libraryBodyPanes.get(i+1).getChildren().add(openBodyChild); //set
                }
                else if (totalFunctionWindows.keySet().contains(bodyChild)) {
                    Hyperlink openBodyChild = new Hyperlink("Open Function");
                    openBodyChild.setOnMouseClicked(event -> {
                        Stage window = totalFunctionWindows.get(bodyChild);
                        if (window.isShowing())
                            window.close();
                        window.show();
                    });
                    libraryBodyPanes.get(i+1).getChildren().add(openBodyChild); //set
                }
                else {
                    Text bodyChildNotFound = new Text("Not found!");
                    libraryBodyPanes.get(i+1).getChildren().add(bodyChildNotFound); //set
                }
            }
        }

        //fill out windowed library function bodies
        for(String libraryName : totalWindowedLibraryBodyPanes.keySet()) {
            ArrayList<StackPane> windowedLibraryBodyPanes = totalWindowedLibraryBodyPanes.get(libraryName); //gets panes of each library
            //starts at index 2, skips library header
            for (int i = 2; i < windowedLibraryBodyPanes.size(); i+=2) {
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
                windowedLibraryBodyPanes.get(i+1).getChildren().add(bodyChildNotFound); //set
//                }
            }
        }

        ScrollPane emptyLibraryPane = new ScrollPane();
        centerState[0] = emptyLibraryPane;
        emptyLibraryPane.setPrefSize(800,600);

        HBox headerBox = new JTAFHeader().getHeaderBox();

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
                System.out.println(libraryPathButton.getText());
                toggleUnselectAll(libraryPathButton, directoryVBox.getChildren());
                toggleSelect(libraryPathButton);
                if(testLibraryMap.containsKey(libraryPathButton.getText())) {
                    headerLabel.setText(libraryPathButton.getText()); //changes label in header
                    defaultBorderPane.setCenter(testLibraryMap.get(libraryPathButton.getText()));
                    centerState[0] = testLibraryMap.get(libraryPathButton.getText());
                }

                if(!libraryPathButton.isSelected()) {
                    libraryPathButton.setSelected(true);
                }
            });
        }
    }

    public void setHeaderBoxEvents(BorderPane defaultBorderPane, HBox headerBox, ScrollPane directoryPane) {
        VBox headerRightHalfBox = (VBox) headerBox.getChildren().get(1);
        HBox tabBarBox = (HBox) headerRightHalfBox.getChildren().get(0);

        for (Node node : tabBarBox.getChildren()) {
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
                if (tabButton.getText().equals("Something1"))
                    System.out.println("Do something");

                if (!tabButton.isSelected()) {
                    tabButton.setSelected(true);
                }
            });
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

    public HBox createViewerHeader(BorderPane defaultBorderPane, Node tab1) {
        HBox viewerHeader = new HBox();
        VBox headerRightHalf = new VBox();
        HBox headerLeftHalf = new HBox();
        HBox tabBar = new HBox();

        headerRightHalf.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        headerRightHalf.setPrefWidth(800);

        Label logo = new Label("LOGO"); //TODO: change to png
        logo.setPrefWidth(200); //same as directory/scrollpane
        logo.setPrefHeight(100);
        logo.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        headerLeftHalf.getChildren().add(logo);

        //design tab buttons
        Label tabButtonLabel1 = new Label("Test Library");
        tabButtonLabel1.setTextFill(Color.WHITE);
        tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButtonLabel1.setFont(new Font("Arial", 30));
        Label tabButtonLabel2 = new Label("Something1");
        tabButtonLabel2.setTextFill(Color.WHITE);
        tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButtonLabel2.setFont(new Font("Arial", 30));

        //add tab buttons
        ToggleButton tabButton1 = new ToggleButton();
        tabButton1.setGraphic(tabButtonLabel1);
        tabButton1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton1.setPrefHeight(50);
        ToggleButton tabButton2 = new ToggleButton();
        tabButton2.setGraphic(tabButtonLabel2);
        tabButton2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton2.setPrefHeight(50);

        tabBar.getChildren().addAll(tabButton1,tabButton2);
        tabBar.setAlignment(Pos.BOTTOM_LEFT);

//        StackPane libraryName = createBoxHeader("libraryName",30, Color.RED); //Important
        headerRightHalf.getChildren().add(tabBar);
//        headerRightHalf.getChildren().add(libraryName);
        headerRightHalf.setAlignment(Pos.BOTTOM_LEFT);

        viewerHeader.getChildren().addAll(headerLeftHalf,headerRightHalf);

        //tab events
        tabButton1.setOnMouseClicked(event -> {
            tabButton2.setSelected(false);
            tabButton2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            tabButton1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            defaultBorderPane.setCenter(tab1);
            //keeps selected
            if(!tabButton1.isSelected()) {
                tabButton1.setSelected(true);
            }
        });

        tabButton2.setOnMouseClicked(event -> {
            tabButton1.setSelected(false);
            tabButton1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            tabButton2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            defaultBorderPane.setCenter(new VBox());
            //keeps selected
            if(!tabButton2.isSelected()) {
                tabButton2.setSelected(true);
            }
        });

        return viewerHeader;
    }

//    public ScrollPane createLibrary() throws IOException, SAXException, ParserConfigurationException {
//        //ScrollePane -> VBox -> VBox -> GridPane
//        ScrollPane libraryPane = new ScrollPane();
//        libraryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        libraryPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        VBox libraryBox = new VBox();
//        VBox commandBox = new VBox();
//        VBox functionBox = new VBox();
//
//        libraryBox.setPrefWidth(800);
//        libraryBox.setPrefHeight(600);
//
//        LibraryParser libraryParser = new LibraryParser("C:\\Users\\Michael\\Documents\\GitHub\\JTAF-XCore\\src\\test\\resources\\testlibrary\\context.test.commands.xml");
//        ArrayList<Command> commands = libraryParser.getCommands();
//        ArrayList<Function> functions = libraryParser.getFunctions();
//
//        if(!commands.isEmpty()) {
//            StackPane commandBoxHeader = createBoxHeader("COMMANDS",25,Color.DARKBLUE);
//            commandBox.getChildren().add(commandBoxHeader);
//            for (int i = 0; i < commands.size(); i++) {
//                Command command = commands.get(i);
//                StackPane commandHeader = createLibraryHeader(command.getCommandName());
//                GridPane commandGridPane = createCommandGridPane(command);
//                commandMouseEffects(commandHeader, commandGridPane, command);
//                commandBox.getChildren().add(commandHeader);
//                commandBox.getChildren().add(commandGridPane);
//            }
//
//            //commands header
//            commandBoxHeader.setOnMouseClicked(event -> {
//                int sum = 0;
//                for (Node node : commandBox.getChildren()) {
//                    if (node.getClass().equals(GridPane.class)) {
//                        if (node.isVisible() || node.isManaged()) {
//                            sum++;
//                        }
//                    }
//                }
//                if (sum == 0)
//                    maximizeCommands(commandBox);
//                else minimizeCommands(commandBox);
//            });
//        }
//        if(!functions.isEmpty()) {
//            StackPane functionBoxHeader = createBoxHeader("FUNCTIONS",25,Color.DARKBLUE);
//            functionBox.getChildren().add(functionBoxHeader);
//            for (int i = 0; i < libraryParser.getFunctions().size(); i++) {
//                Function function = functions.get(i);
//                StackPane functionHeader = createLibraryHeader(function.getFunctionName());
//                GridPane functionGridPane = createFunctionGridPane(function);
//                functionMouseEffects(functionHeader, functionGridPane, function);
//                functionBox.getChildren().add(functionHeader);
//                functionBox.getChildren().add(functionGridPane);
//            }
//
//            //functions header
//            functionBoxHeader.setOnMouseClicked(event -> {
//                int sum = 0;
//                for (Node node : functionBox.getChildren()) {
//                    if (node.getClass().equals(GridPane.class)) {
//                        if (node.isVisible() || node.isManaged()) {
//                            sum++;
//                        }
//                    }
//                }
//                if (sum == 0)
//                    maximizeCommands(functionBox);
//                else minimizeCommands(functionBox);
//            });
//        }
//
//        libraryBox.getChildren().add(commandBox);
//        libraryBox.getChildren().add(functionBox);
//        libraryPane.setContent(libraryBox);
//        return libraryPane;
//    }
//
//    public StackPane createBoxHeader(String name, int fontSize, Color color) {
//        StackPane boxHeader = new StackPane();
//
//        Label boxHeaderLabel = new Label(name);
//        boxHeaderLabel.setTextFill(Color.WHITE);
//        boxHeaderLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
//        boxHeaderLabel.setFont(new Font("Arial", fontSize));
//        boxHeaderLabel.setPrefWidth(800);
//        boxHeader.getChildren().add(boxHeaderLabel);
//        return boxHeader;
//    }
//
//    public StackPane createLibraryHeader(String name) {
//        StackPane libraryHeader = new StackPane();
//        libraryHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//
//        Label libraryHeaderLabel = new Label(name);
//        libraryHeaderLabel.setTextFill(Color.WHITE);
//        libraryHeaderLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
//        libraryHeaderLabel.setFont(new Font("Arial", 20));
//        libraryHeaderLabel.setPrefWidth(800);
//        libraryHeader.getChildren().add(libraryHeaderLabel);
//
//        Button classWindowButton = new Button();
//        Image img = new Image(getClass().getResourceAsStream("images\\ic_open_in_new_white_12dp.png"));
//        ImageView imgView = new ImageView(img);
//        classWindowButton.setGraphic(imgView);
//        classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//
//        libraryHeader.getChildren().add(classWindowButton);
//        StackPane.setAlignment(classWindowButton, Pos.CENTER_RIGHT);
//        StackPane.setMargin(classWindowButton, new Insets(0, 30, 0, 0));
//
//        return libraryHeader;
//    }
//
//    public GridPane createCommandGridPane(Command command) {
//        final GridPane commandGridPane = new GridPane();
//        commandGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setPrefWidth(300);
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPrefWidth(500);
//        commandGridPane.getColumnConstraints().addAll(col1,col2);
//
//        int row = 0;
//        int height = 0;
//        StackPane classPane = new StackPane();
//        classPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//        classPane.getChildren().add(new Text("Class"));
//        classPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        commandGridPane.add(classPane,0,row);
//        StackPane classPaneInfo = new StackPane();
//        classPaneInfo.getChildren().add(new Text(command.getCommandClass()));
//        classPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        commandGridPane.add(classPaneInfo,1,row);
//        row++;
//        RowConstraints rowConstraint = new RowConstraints(50);
//        commandGridPane.getRowConstraints().add(rowConstraint);
//        height+=rowConstraint.getMaxHeight();
//        if (command.hasCommandUsage()) {
//            StackPane usagePane = new StackPane();
//            usagePane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            usagePane.getChildren().add(new Text("Usage"));
//            usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(usagePane, 0, row);
//            StackPane usagePaneInfo = new StackPane();
//            usagePaneInfo.getChildren().add(new Text(command.getCommandUsage().trim())); //should make height variable
//            usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(usagePaneInfo, 1, row);
//            row++;
//            if (usagePane.getMaxHeight() > 50)
//                rowConstraint = new RowConstraints(usagePane.getMaxHeight());
//            commandGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//        }
//        if (command.hasRequiredParameters()) {
//            StackPane requiredParamPane = new StackPane();
//            requiredParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            requiredParamPane.getChildren().add(new Text("Required Parameters"));
//            requiredParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(requiredParamPane, 0, row);
//            StackPane requiredParamPaneInfo = new StackPane();
//            requiredParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(requiredParamPaneInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            commandGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < command.getRequiredParameters().size() * 3; i++) {
//                String requiredParamKey = "";
//                String requiredParamData = "";
//                int commandIndex = i/3;
//                if (i % 3 == 0) {
//                    requiredParamKey = "Name";
//                    requiredParamData = command.getRequiredParameters().get(commandIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    requiredParamKey = "Tag";
//                    requiredParamData = command.getRequiredParameters().get(commandIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    requiredParamKey = "Text";
//                    requiredParamData = command.getRequiredParameters().get(commandIndex).getText();
//                }
//                StackPane requiredParamChildPane = new StackPane();
//                requiredParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                requiredParamChildPane.getChildren().add(new Text(requiredParamKey));
//                requiredParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(requiredParamChildPane, 0, row);
//                StackPane requiredParamChildPaneInfo = new StackPane();
//                requiredParamChildPaneInfo.getChildren().add(new Text(requiredParamData));
//                requiredParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(requiredParamChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = requiredParamChildPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                commandGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        if (command.hasOptionalParameters()) {
//            StackPane optionalParamPane = new StackPane();
//            optionalParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            optionalParamPane.getChildren().add(new Text("Optional Parameters"));
//            optionalParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(optionalParamPane, 0, row);
//            StackPane optionalParamPaneInfo = new StackPane();
//            optionalParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(optionalParamPaneInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            commandGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < command.getOptionalParameters().size() * 3; i++) {
//                String optionalParamKey = "";
//                String optionalParamData = "";
//                int commandIndex = i/3;
//                if (i % 3 == 0) {
//                    optionalParamKey = "Name";
//                    optionalParamData = command.getOptionalParameters().get(commandIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    optionalParamKey = "Tag";
//                    optionalParamData = command.getOptionalParameters().get(commandIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    optionalParamKey = "Text";
//                    optionalParamData = command.getOptionalParameters().get(commandIndex).getText();
//                }
//                StackPane optionalParamChildPane = new StackPane();
//                optionalParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                optionalParamChildPane.getChildren().add(new Text(optionalParamKey));
//                optionalParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(optionalParamChildPane, 0, row);
//                StackPane optionalParamChildPaneInfo = new StackPane();
//                optionalParamChildPaneInfo.getChildren().add(new Text(optionalParamData));
//                optionalParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(optionalParamChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = optionalParamPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                commandGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        if (command.hasCommandResults()) {
//            StackPane resultsPane = new StackPane();
//            resultsPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            resultsPane.getChildren().add(new Text("Produces"));
//            resultsPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(resultsPane, 0, row);
//            StackPane resultPanesInfo = new StackPane();
//            resultPanesInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            commandGridPane.add(resultPanesInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            commandGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < command.getCommandResults().size() * 3; i++) {
//                String resultsParamKey = "";
//                String resultsParamData = "";
//                int commandIndex = i/3;
//                if (i % 3 == 0) {
//                    resultsParamKey = "Name";
//                    resultsParamData = command.getCommandResults().get(commandIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    resultsParamKey = "Tag";
//                    resultsParamData = command.getCommandResults().get(commandIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    resultsParamKey = "Text";
//                    resultsParamData = command.getCommandResults().get(commandIndex).getText();
//                }
//                StackPane resultsChildPane = new StackPane();
//                resultsChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                resultsChildPane.getChildren().add(new Text(resultsParamKey));
//                resultsChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(resultsChildPane, 0, row);
//                StackPane resultsChildPaneInfo = new StackPane();
//                resultsChildPaneInfo.getChildren().add(new Text(resultsParamData));
//                resultsChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                commandGridPane.add(resultsChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = resultsChildPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                commandGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        commandGridPane.setAlignment(Pos.CENTER);
//        commandGridPane.setPrefSize(800, height);
//        commandGridPane.setVisible(false);
//        commandGridPane.setManaged(false);
//
////        for (int i = 0; i < commandGridPane.getChildren().size()/2; i++) {
////            RowConstraints rows = new RowConstraints(50);
////            commandGridPane.getRowConstraints().add(rows);
////        }
//
//        return commandGridPane;
//    }
//
//    public void commandMouseEffects(StackPane commandHeader, GridPane commandGridPane, Command command) {
//        DropShadow glow = new DropShadow();
//        glow.setOffsetY(0);
//        glow.setOffsetX(0);
//        glow.setColor(Color.AQUA);
//        glow.setWidth(25);
//        glow.setHeight(25);
//
//        Label commandLabel = (Label) commandHeader.getChildren().get(0);
//        Button classWindowButton = (Button) commandHeader.getChildren().get(1);
//        ImageView imgView = (ImageView) classWindowButton.getGraphic();
//
//        classWindowButton.setOnMouseEntered(event -> {
//            imgView.setEffect(glow);
//        });
//        classWindowButton.setOnMouseExited(event -> {
//            imgView.setEffect(null);
//        });
//
//        //command in new window
//        classWindowButton.setOnMouseClicked(event -> {
//            Stage commandWindow = new Stage();
//            VBox windowedVBox = new VBox();
//            StackPane windowedCommandHeader = createBoxHeader(command.getCommandName(),25, Color.DARKBLUE);
//            GridPane windowedCommand = createCommandGridPane(command);
//            windowedCommand.setVisible(true);
//            windowedCommand.setManaged(true);
//            windowedVBox.getChildren().addAll(windowedCommandHeader, windowedCommand);
//            commandWindow.setScene(new Scene(windowedVBox,300,windowedCommand.getPrefHeight()));
//            commandWindow.show();
//        });
//
//        commandHeader.setOnMouseEntered(event -> {
//            commandHeader.setEffect(glow);
//            commandHeader.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//            commandLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//            classWindowButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//            }
//        );
//        commandHeader.setOnMouseExited(event -> {
//            commandHeader.setEffect(null);
//            commandHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//            commandLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//            classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//            }
//        );
//
//        commandHeader.setOnMouseClicked(event -> {
//            if (commandGridPane.isVisible()) {
//                commandGridPane.setVisible(false);
//                commandGridPane.setManaged(false);
//            } else {
//                commandGridPane.setVisible(true);
//                commandGridPane.setManaged(true);
//            }
//
//        });
//    }
//
//    public void minimizeCommands(VBox commandBox) {
//        for (Node node : commandBox.getChildren()) {
//            if (node.getClass().equals(GridPane.class)) {
//                node.setVisible(false);
//                node.setManaged(false);
//            }
//        }
//    }
//
//    public void maximizeCommands(VBox commandBox) {
//        for (Node node : commandBox.getChildren()) {
//            if (node.getClass().equals(GridPane.class)) {
//                node.setVisible(true);
//                node.setManaged(true);
//            }
//        }
//    }
//
//    public GridPane createFunctionGridPane(Function function) {
//        final GridPane functionGridPane = new GridPane();
//        functionGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        ColumnConstraints col1 = new ColumnConstraints();
//        col1.setPrefWidth(300);
//        ColumnConstraints col2 = new ColumnConstraints();
//        col2.setPrefWidth(500);
//        functionGridPane.getColumnConstraints().addAll(col1,col2);
//
//        int row = 0;
//        int height = 0;
//        RowConstraints rowConstraint = new RowConstraints(50);
//        if (function.hasFunctionUsage()) {
//            StackPane usagePane = new StackPane();
//            usagePane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            usagePane.getChildren().add(new Text("Usage"));
//            usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(usagePane, 0, row);
//            StackPane usagePaneInfo = new StackPane();
//            usagePaneInfo.getChildren().add(new Text(function.getFunctionUsage().trim())); //should make height variable
//            usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(usagePaneInfo, 1, row);
//            row++;
//            if (usagePane.getMaxHeight() > 50)
//                rowConstraint = new RowConstraints(usagePane.getMaxHeight());
//            functionGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//        }
//        if (function.hasRequiredParameters()) {
//            StackPane requiredParamPane = new StackPane();
//            requiredParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            requiredParamPane.getChildren().add(new Text("Required Parameters"));
//            requiredParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(requiredParamPane, 0, row);
//            StackPane requiredParamPaneInfo = new StackPane();
//            requiredParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(requiredParamPaneInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            functionGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < function.getRequiredParameters().size() * 3; i++) {
//                String requiredParamKey = "";
//                String requiredParamData = "";
//                int functionIndex = i/3;
//                if (i % 3 == 0) {
//                    requiredParamKey = "Name";
//                    requiredParamData = function.getRequiredParameters().get(functionIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    requiredParamKey = "Tag";
//                    requiredParamData = function.getRequiredParameters().get(functionIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    requiredParamKey = "Text";
//                    requiredParamData = function.getRequiredParameters().get(functionIndex).getText();
//                }
//                StackPane requiredParamChildPane = new StackPane();
//                requiredParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                requiredParamChildPane.getChildren().add(new Text(requiredParamKey));
//                requiredParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(requiredParamChildPane, 0, row);
//                StackPane requiredParamChildPaneInfo = new StackPane();
//                requiredParamChildPaneInfo.getChildren().add(new Text(requiredParamData));
//                requiredParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(requiredParamChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = requiredParamChildPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                functionGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        if (function.hasOptionalParameters()) {
//            StackPane optionalParamPane = new StackPane();
//            optionalParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            optionalParamPane.getChildren().add(new Text("Optional Parameters"));
//            optionalParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(optionalParamPane, 0, row);
//            StackPane optionalParamPaneInfo = new StackPane();
//            optionalParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(optionalParamPaneInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            functionGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < function.getOptionalParameters().size() * 3; i++) {
//                String optionalParamKey = "";
//                String optionalParamData = "";
//                int functionIndex = i/3;
//                if (i % 3 == 0) {
//                    optionalParamKey = "Name";
//                    optionalParamData = function.getOptionalParameters().get(functionIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    optionalParamKey = "Tag";
//                    optionalParamData = function.getOptionalParameters().get(functionIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    optionalParamKey = "Text";
//                    optionalParamData = function.getOptionalParameters().get(functionIndex).getText();
//                }
//                StackPane optionalParamChildPane = new StackPane();
//                optionalParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                optionalParamChildPane.getChildren().add(new Text(optionalParamKey));
//                optionalParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(optionalParamChildPane, 0, row);
//                StackPane optionalParamChildPaneInfo = new StackPane();
//                optionalParamChildPaneInfo.getChildren().add(new Text(optionalParamData));
//                optionalParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(optionalParamChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = optionalParamPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                functionGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        if (function.hasFunctionResults()) {
//            StackPane resultsPane = new StackPane();
//            resultsPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//            resultsPane.getChildren().add(new Text("Produces"));
//            resultsPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(resultsPane, 0, row);
//            StackPane resultPanesInfo = new StackPane();
//            resultPanesInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            functionGridPane.add(resultPanesInfo, 1, row);
//            row++;
//            rowConstraint = new RowConstraints(50);
//            functionGridPane.getRowConstraints().add(rowConstraint);
//            height+=rowConstraint.getMaxHeight();
//
//            for(int i = 0; i < function.getFunctionResults().size() * 3; i++) {
//                String resultsParamKey = "";
//                String resultsParamData = "";
//                int functionIndex = i/3;
//                if (i % 3 == 0) {
//                    resultsParamKey = "Name";
//                    resultsParamData = function.getFunctionResults().get(functionIndex).getName();
//                }
//                if (i % 3 == 1) {
//                    resultsParamKey = "Tag";
//                    resultsParamData = function.getFunctionResults().get(functionIndex).getTag();
//                }
//                if (i % 3 == 2) {
//                    resultsParamKey = "Text";
//                    resultsParamData = function.getFunctionResults().get(functionIndex).getText();
//                }
//                StackPane resultsChildPane = new StackPane();
//                resultsChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
//                resultsChildPane.getChildren().add(new Text(resultsParamKey));
//                resultsChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(resultsChildPane, 0, row);
//                StackPane resultsChildPaneInfo = new StackPane();
//                resultsChildPaneInfo.getChildren().add(new Text(resultsParamData));
//                resultsChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
//                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//                functionGridPane.add(resultsChildPaneInfo, 1, row);
//                row++;
//                double rowHeight = resultsChildPaneInfo.getHeight();
//                if (rowHeight < 25)
//                    rowHeight = 25;
//                rowConstraint = new RowConstraints(rowHeight);
//                functionGridPane.getRowConstraints().add(rowConstraint);
//                height+=rowConstraint.getMaxHeight();
//            }
//        }
//        functionGridPane.setAlignment(Pos.CENTER);
//        functionGridPane.setPrefSize(800, height);
//        functionGridPane.setVisible(false);
//        functionGridPane.setManaged(false);
//
////        for (int i = 0; i < commandGridPane.getChildren().size()/2; i++) {
////            RowConstraints rows = new RowConstraints(50);
////            commandGridPane.getRowConstraints().add(rows);
////        }
//
//        return functionGridPane;
//    }
//
//    public void functionMouseEffects(StackPane functionHeader, GridPane functionGridPane, Function function) {
//        DropShadow glow = new DropShadow();
//        glow.setOffsetY(0);
//        glow.setOffsetX(0);
//        glow.setColor(Color.AQUA);
//        glow.setWidth(25);
//        glow.setHeight(25);
//
//        Label commandLabel = (Label) functionHeader.getChildren().get(0);
//        Button classWindowButton = (Button) functionHeader.getChildren().get(1);
//        ImageView imgView = (ImageView) classWindowButton.getGraphic();
//
//        classWindowButton.setOnMouseEntered(event -> {
//            imgView.setEffect(glow);
//        });
//        classWindowButton.setOnMouseExited(event -> {
//            imgView.setEffect(null);
//        });
//
//        //command in new window
//        classWindowButton.setOnMouseClicked(event -> {
//            Stage commandWindow = new Stage();
//            VBox windowedVBox = new VBox();
//            StackPane windowedFunctionHeader = createBoxHeader(function.getFunctionName(),25, Color.DARKBLUE);
//            GridPane windowedCommand = createFunctionGridPane(function);
//            windowedCommand.setVisible(true);
//            windowedCommand.setManaged(true);
//            windowedVBox.getChildren().addAll(windowedFunctionHeader, windowedCommand);
//            commandWindow.setScene(new Scene(windowedVBox,300,windowedCommand.getPrefHeight()));
//            commandWindow.show();
//        });
//
//        functionHeader.setOnMouseEntered(event -> {
//                    functionHeader.setEffect(glow);
//                    functionHeader.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//                    commandLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//                }
//        );
//        functionHeader.setOnMouseExited(event -> {
//                    functionHeader.setEffect(null);
//                    functionHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//                    commandLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
//                }
//        );
//
//            functionHeader.setOnMouseClicked(event -> {
//                if (functionGridPane.isVisible()) {
//                    functionGridPane.setVisible(false);
//                    functionGridPane.setManaged(false);
//                } else {
//                    functionGridPane.setVisible(true);
//                    functionGridPane.setManaged(true);
//                }
//
//        });
//    }
}

