package Main;

import JTAF.Command;
import JTAF.Function;
import JTAF.Library;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFUIHeader {
    public double HEADER_TOTAL_WIDTH;
    public double HEADER_TAB_HEIGHT = 40;
    public Color LOGO_COLOR   = Color.web("#233E66");
    public Color HEADER_COLOR = Color.web("#D8E1E8");
    public Color TAB_SELECTED_COLOR = Color.WHITE;
    public Color TAB_UNSELECTED_COLOR = Color.web("#73787D");
    public Color LIBRARY_DUPLICATE_COLOR;
    public String FINRA_FONT = "Georgia";

    private HBox headerBox;
    private HBox searchBarBox;
    private Label headerLabel;
    private ArrayList<ToggleButton> tabButtons = new ArrayList<>();
    private StackPane lastResultHeader = null;

    public JTAFUIHeader(double libraryWidth) {
        this.HEADER_TOTAL_WIDTH = libraryWidth;
        headerBox = new HBox();
        VBox headerRightHalfBox = new VBox();
        HBox headerLeftHalfBox = new HBox();
        HBox tabsBox = new HBox();
        BorderPane tabBarPane = new BorderPane();
        tabBarPane.setPrefHeight(50);

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0,LOGO_COLOR),new Stop(1,HEADER_COLOR));
        headerRightHalfBox.setBackground(new Background(new BackgroundFill(gradient,CornerRadii.EMPTY, Insets.EMPTY)));
        headerRightHalfBox.setPrefWidth(HEADER_TOTAL_WIDTH);

        Label logo = new Label("LOGO"); //TODO: change to png
        logo.setPrefWidth(200); //same as directory/scrollpane
        logo.setPrefHeight(100);
        logo.setBackground(new Background(new BackgroundFill(LOGO_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        headerLeftHalfBox.getChildren().add(logo);

        //add tab buttons
        ToggleButton tabButton1 = new ToggleButton();
        tabButton1.setText("Test Library");
        tabButton1.setBackground(new Background(new BackgroundFill(TAB_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton1.setPrefHeight(HEADER_TAB_HEIGHT);

        ToggleButton tabButton2 = new ToggleButton();
        tabButton2.setText("Something1");
        tabButton2.setBackground(new Background(new BackgroundFill(TAB_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton2.setPrefHeight(HEADER_TAB_HEIGHT);

        tabButton1.setAlignment(Pos.CENTER);
        tabButton2.setAlignment(Pos.CENTER);
        tabsBox.getChildren().addAll(tabButton1,tabButton2);
        tabsBox.setAlignment(Pos.BOTTOM_LEFT);

        tabButtons.add(tabButton1);
        tabButtons.add(tabButton2);

        searchBarBox = new HBox();
        Label searchMessage = new Label("");
        searchMessage.setTextFill(Color.RED); //TODO
        searchMessage.setPadding(new Insets(5));

        TextField searchTextField = new TextField();
        searchTextField.setPromptText("Enter command or function name");
        searchTextField.setPrefWidth(200);
        Button searchButton = new Button("search");
        searchBarBox.getChildren().add(searchMessage);
        searchBarBox.getChildren().add(searchTextField);
        searchBarBox.getChildren().add(searchButton);
        searchBarBox.setAlignment(Pos.BOTTOM_RIGHT);

        tabBarPane.setRight(searchBarBox);
        tabBarPane.setLeft(tabsBox);

        StackPane libraryNamePane = new StackPane(); //Important
        headerLabel = new Label("");
        headerLabel.setPadding(new Insets(5));
        headerLabel.setTextFill(Color.BLACK);
        headerLabel.setBackground(new Background(new BackgroundFill(TAB_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        headerLabel.setFont(new Font(FINRA_FONT, 30));
        headerLabel.setPrefWidth(HEADER_TOTAL_WIDTH);

        libraryNamePane.getChildren().add(headerLabel);
        headerRightHalfBox.getChildren().add(tabBarPane);
        headerRightHalfBox.getChildren().add(libraryNamePane);
        headerRightHalfBox.setAlignment(Pos.BOTTOM_LEFT);

        headerBox.getChildren().addAll(headerLeftHalfBox,headerRightHalfBox);
    }

    public HBox getHeaderBox() {
        return this.headerBox;
    }

    public HBox getSearchBarBox() {
        return this.searchBarBox;
    }

    public Label getHeaderLabel() {
        return this.headerLabel;
    }

    public void setTabEvents(Node[] directoryState, Node[] centerState, BorderPane defaultBorderPane) {
        for (ToggleButton tabButton : tabButtons) {
                tabButton.setOnMouseClicked(event -> {
                //unselect everything else
                toggleUnselectAll(tabButton, tabButtons,TAB_UNSELECTED_COLOR);
                //select tabButton
                toggleSelect(tabButton,TAB_SELECTED_COLOR);
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

    public void setTestLibrarySearch(ArrayList<ToggleButton> directoryButtons, ArrayList<JTAFUILibrary> testLibraryUI) {
        Label searchMessage = (Label) searchBarBox.getChildren().get(0);
        TextField searchTextField = (TextField) searchBarBox.getChildren().get(1);
        Button searchButton = (Button) searchBarBox.getChildren().get(2);

        searchTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchButton.fire();
            } else {
//                searchMessage.setText("");
            }
        });

        searchButton.setOnAction(event -> {
            String searchedName = searchTextField.getText();
            boolean isFound = false;
            if (!searchedName.isEmpty()) {
                System.out.println("Searched for: "+searchedName);
                searchedName = searchedName.trim();
                if (searchedName.endsWith(".xml")) {
                    searchedName = searchedName.substring(0, searchedName.length()-4);
                }
                //update header and directory
                for (int i = 0; i < testLibraryUI.size(); i++) {
                    JTAFUILibrary jtafuiLibrary = testLibraryUI.get(i);
                    ArrayList<JTAFUICommand> uiCommands = jtafuiLibrary.getUICommands();
                    ArrayList<JTAFUIFunction> uiFunctions = jtafuiLibrary.getUIFunctions();
                    ToggleButton libraryPathButton = directoryButtons.get(i);
                    for (JTAFUICommand  uiCommand: uiCommands) {
                        if (uiCommand.getCommandName().equalsIgnoreCase(searchedName)) {
                            searchMessage.setText("");
                            System.out.println(uiCommand.getCommandName() +" found in "+jtafuiLibrary.getLibraryName()+"!");
                            if (!isFound) {
                                openTestLibraryResult(uiCommand, jtafuiLibrary);
                                libraryPathButton.fire();
                                isFound = true;
                            }
                            else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("");
                                alert.setContentText("Duplicate command in "+jtafuiLibrary.getLibraryName()+"!");
                                alert.showAndWait();
                            }
                        }
                    }

                    for (JTAFUIFunction uiFunction : uiFunctions) {
                        if (uiFunction.getFunctionName().equalsIgnoreCase(searchedName)) {
                            searchMessage.setText("");
                            libraryPathButton.fire();
                            System.out.println(uiFunction.getFunctionName() +" found in "+jtafuiLibrary.getLibraryName()+"!");
                            if (!isFound) {
                                openTestLibraryResult(uiFunction, jtafuiLibrary);
                                libraryPathButton.fire();
                                isFound = true;
                            }
                            else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("");
                                alert.setContentText("Duplicate function in "+jtafuiLibrary.getLibraryName()+"!");
                                alert.showAndWait();
                            }
                        }
                    }
                }
                if(!isFound)
                    searchMessage.setText("Not found!");
            }
        });
    }

    private void openTestLibraryResult(JTAFUICommand foundCommand, JTAFUILibrary jtafuiLibrary) {
        setHeaderColor(foundCommand.getCommandHeader(), jtafuiLibrary.LIBRARY_FOUND_COLOR);
        foundCommand.getCommandGridPane().setVisible(true);
        foundCommand.getCommandGridPane().setManaged(true);

        if (lastResultHeader != null) {
            if (lastResultHeader != foundCommand.getCommandHeader())
                setHeaderColor(lastResultHeader, jtafuiLibrary.LIBRARY_HEADER_UNSELECTED_COLOR);
        }
        lastResultHeader = foundCommand.getCommandHeader();

        //gets height of all commands
        double totalHeight = 44; //height of library header
        double resultHeight = 44;
        for (int i = 0; i < jtafuiLibrary.getUICommands().size(); i++) {
            totalHeight += 42; //height of each command header
            JTAFUICommand uiCommand = jtafuiLibrary.getUICommands().get(i);
            if (uiCommand.getCommandName().equals(foundCommand.getCommandName())) {
                resultHeight = totalHeight;
            }
            if (uiCommand.getCommandGridPane().isVisible() &&
                    uiCommand.getCommandGridPane().isManaged()) {
                totalHeight += uiCommand.getHeight();
            }
        }
        jtafuiLibrary.getLibraryPane().setVvalue(resultHeight / totalHeight);
    }

    private void openTestLibraryResult(JTAFUIFunction foundFunction, JTAFUILibrary jtafuiLibrary) {
        setHeaderColor(foundFunction.getFunctionHeader(), jtafuiLibrary.LIBRARY_FOUND_COLOR);
        foundFunction.getFunctionGridPane().setVisible(true);
        foundFunction.getFunctionGridPane().setManaged(true);

        if (lastResultHeader != null) {
            if (lastResultHeader != foundFunction.getFunctionHeader())
                setHeaderColor(lastResultHeader, jtafuiLibrary.LIBRARY_HEADER_UNSELECTED_COLOR);
        }
        lastResultHeader = foundFunction.getFunctionHeader();

        //gets height of all functions
        double totalHeight = 44; //height of library header
        double resultHeight = 44;
        for (int i = 0; i < jtafuiLibrary.getUIFunctions().size(); i++) {
            totalHeight += 42; //height of each function header
            JTAFUIFunction uiFunction = jtafuiLibrary.getUIFunctions().get(i);
            if (uiFunction.getFunctionName().equals(foundFunction.getFunctionName())) {
                resultHeight = totalHeight;
            }
            if (uiFunction.getFunctionGridPane().isVisible() &&
                    uiFunction.getFunctionGridPane().isManaged()) {
                totalHeight += uiFunction.getHeight();
            }
        }
        jtafuiLibrary.getLibraryPane().setVvalue(resultHeight / totalHeight);
    }

    private void setHeaderColor(StackPane header, Color color) {
        Button editWindowButton = (Button) header.getChildren().get(1);
        Button openWindowButton = (Button) header.getChildren().get(2);

        header.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))); //TODO: set effect
        openWindowButton.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        editWindowButton.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void toggleUnselectAll(ToggleButton selectedButton, ArrayList<ToggleButton> otherButtons, Color unselected) {
        for (Node node : otherButtons) {
            ToggleButton unselectedButton = (ToggleButton) node;
            if (!unselectedButton.equals(selectedButton)) {
                unselectedButton.setBackground(new Background(new BackgroundFill(unselected, CornerRadii.EMPTY, Insets.EMPTY)));
                unselectedButton.setSelected(false);
            }
        }
    }

    private void toggleSelect(ToggleButton selectedButton, Color selected) {
        selectedButton.setBackground(new Background(new BackgroundFill(selected, CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
