package Main;

import JTAF.Command;
import JTAF.Function;
import JTAF.Library;
import JTAF.Parameter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFUILibrary {
    public double LIBRARY_TOTAL_WIDTH = 1000;
    public double LIBRARY_KEY_WIDTH = 400;
    public double LIBRARY_DATA_WIDTH = 600;
    public Color LIBRARY_BACKGROUND_COLOR = Color.WHITE;
    public Color CATEGORY_HEADER_COLOR = Color.web("#015DA6");
    public Color LIBRARY_HEADER_SELECTED_COLOR = Color.web("#66B4E3");
    public Color LIBRARY_HEADER_UNSELECTED_COLOR = Color.web("#3393DA");
    public Color LIBRARY_ATTRIBUTE_COLOR = Color.web("#BEBDB7");
    public Color LIBRARY_ATTRIBUTE_CHILD_COLOR = Color.web("#DEDEDB");
    public Color LIBRARY_FOUND_COLOR = Color.web("#B1D036");
    public String FINRA_FONT = "Georgia";
    private String libraryName;
    private ScrollPane libraryPane;
    private ArrayList<JTAFUICommand> uiCommands = new ArrayList<>();
    private ArrayList<JTAFUIFunction> uiFunctions = new ArrayList<>();
    private ArrayList<StackPane> uiFunctionBodies = new ArrayList<>();

    public JTAFUILibrary(Library library) {
        libraryName = library.getLibraryName();
        libraryPane = new ScrollPane();
        libraryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        libraryPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        VBox libraryBox = new VBox();
        VBox commandBox = new VBox();
        VBox functionBox = new VBox();

        libraryBox.setPrefWidth(LIBRARY_TOTAL_WIDTH);
        libraryBox.setPrefHeight(498);
        libraryBox.setBackground(new Background(new BackgroundFill(LIBRARY_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded

        ArrayList<Command> commands = library.getCommands();
        ArrayList<Function> functions = library.getFunctions();

        if(!commands.isEmpty()) {
            StackPane commandBoxHeader = createCategoryHeader("COMMANDS",25, CATEGORY_HEADER_COLOR);
            commandBox.getChildren().add(commandBoxHeader);
            for (int i = 0; i < commands.size(); i++) {
                Command command = commands.get(i);
                JTAFUICommand uiCommand = new JTAFUICommand(command);
                uiCommands.add(uiCommand);
                StackPane commandHeader = uiCommand.getCommandHeader();
                GridPane commandGridPane = uiCommand.getCommandGridPane();
                commandBox.getChildren().add(commandHeader);
                commandBox.getChildren().add(commandGridPane);
            }

            //commands header
            commandBoxHeader.setOnMouseClicked(event -> {
                int sum = 0;
                for (Node node : commandBox.getChildren()) {
                    if (node.getClass().equals(GridPane.class)) {
                        if (node.isVisible() || node.isManaged()) {
                            sum++;
                        }
                    }
                }
                if (sum == 0)
                    maximizeHeaders(commandBox);
                else minimizeHeaders(commandBox);
            });

            //new command button
//            setNewCommandButtonEffects(newCommandButton);
        }
        if(!functions.isEmpty()) {
            StackPane functionBoxHeader = createCategoryHeader("FUNCTIONS",25,CATEGORY_HEADER_COLOR);
            functionBox.getChildren().add(functionBoxHeader);
            for (int i = 0; i < functions.size(); i++) {
                Function function = functions.get(i);
                JTAFUIFunction uiFunction = new JTAFUIFunction(function);
                uiFunctions.add(uiFunction);
                uiFunctionBodies.addAll(uiFunction.getFunctionBodyPanes());
                StackPane functionHeader = uiFunction.getFunctionHeader();
                GridPane functionGridPane = uiFunction.getFunctionGridPane();
                functionBox.getChildren().add(functionHeader);
                functionBox.getChildren().add(functionGridPane);
            }
            //functions header
            functionBoxHeader.setOnMouseClicked(event -> {
                int sum = 0;
                for (Node node : functionBox.getChildren()) {
                    if (node.getClass().equals(GridPane.class)) {
                        if (node.isVisible() || node.isManaged()) {
                            sum++;
                        }
                    }
                }
                if (sum == 0)
                    maximizeHeaders(functionBox);
                else minimizeHeaders(functionBox);
            });

            //new command button
//            setNewFunctionButtonEffects(newFunctionButton);
        }

        libraryBox.getChildren().add(commandBox);
        libraryBox.getChildren().add(functionBox);
        libraryPane.setContent(libraryBox);
    }

    public String getLibraryName() {
        return this.libraryName;
    }

    public ScrollPane getLibraryPane() {
        return this.libraryPane;
    }

    public ArrayList<JTAFUICommand> getUICommands() {
        return this.uiCommands;
    }

    public ArrayList<JTAFUIFunction> getUIFunctions() {
        return this.uiFunctions;
    }

    public ArrayList<StackPane> getUIFunctionBodies() {
        return this.uiFunctionBodies;
    }

    private StackPane createCategoryHeader(String name, int fontSize, Color color) {
        StackPane categoryHeader = new StackPane();

        Label categoryHeaderLabel = new Label(name);
        categoryHeaderLabel.setTextFill(Color.WHITE);
        categoryHeaderLabel.setPadding(new Insets(7));
        categoryHeaderLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        categoryHeaderLabel.setFont(new Font(FINRA_FONT, fontSize));
        categoryHeaderLabel.setPrefWidth(LIBRARY_TOTAL_WIDTH);
        categoryHeader.getChildren().add(categoryHeaderLabel);
        return categoryHeader;
    }

    private void minimizeHeaders(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(false);
                node.setManaged(false);
            }
        }
    }

    private void maximizeHeaders(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(true);
                node.setManaged(true);
            }
        }
    }

    public void setFunctionBodies(ArrayList<JTAFUILibrary> testLibraryUI) {
        for (int i = 0; i < uiFunctionBodies.size(); i+=2) {
            StackPane functionBodyTypePane = uiFunctionBodies.get(i);
            StackPane functionBodyHyperLinkPane = uiFunctionBodies.get(i+1);
            Hyperlink functionBodyHyperLink = (Hyperlink) functionBodyHyperLinkPane.getChildren().get(0);

            Object searchResult = null;
            String bodyType = "null";
            for (JTAFUILibrary jtafuiLibrary : testLibraryUI) {
                searchResult = searchInTestLibraryUI(jtafuiLibrary, functionBodyHyperLink.getText());
                if (searchResult != null) {
                    if (searchResult.getClass().equals(JTAFUICommand.class))
                        bodyType = "Command";
                    if (searchResult.getClass().equals(JTAFUIFunction.class))
                        bodyType = "Function";
                    break;
                }
            }
            functionBodyTypePane.getChildren().add(new Text(bodyType));

            if (searchResult == null)
                functionBodyHyperLink.setDisable(true);
            else if (searchResult.getClass().equals(JTAFUICommand.class)) {
                JTAFUICommand uiCommand = (JTAFUICommand) searchResult;

                functionBodyHyperLink.setOnAction(event -> {
                    Stage windowStage = uiCommand.getCommandWindow();
                    windowStage.setTitle(uiCommand.getCommandName());
                    if (windowStage.isShowing())
                        windowStage.close();
                    windowStage.show();
                });
            } else if (searchResult.getClass().equals(JTAFUIFunction.class)) {
                JTAFUIFunction uiFunction = (JTAFUIFunction) searchResult;

                functionBodyHyperLink.setOnAction(event -> {
                    Stage windowStage = uiFunction.getFunctionWindow();
                    windowStage.setTitle(uiFunction.getFunctionName());
                    if (windowStage.isShowing())
                        windowStage.close();
                    windowStage.show();
                });
            }
        }
    }

    private Object searchInTestLibraryUI(JTAFUILibrary jtafuiLibrary, String searchedName) {
        ArrayList<JTAFUICommand> uiCommands = jtafuiLibrary.getUICommands();
        ArrayList<JTAFUIFunction> uiFunctions = jtafuiLibrary.getUIFunctions();
        for (JTAFUICommand  uiCommand: uiCommands) {
            if (uiCommand.getCommandName().equalsIgnoreCase(searchedName)) {
                return uiCommand;
            }
        }

        for (JTAFUIFunction uiFunction : uiFunctions) {
            if (uiFunction.getFunctionName().equalsIgnoreCase(searchedName)) {
                return uiFunction;
            }
        }
        return null;
    }
}