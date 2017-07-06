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
    private int row;
    private double height;

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

    public StackPane createCategoryHeader(String name, int fontSize, Color color) {
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

    public void minimizeHeaders(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(false);
                node.setManaged(false);
            }
        }
    }

    public void maximizeHeaders(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(true);
                node.setManaged(true);
            }
        }
    }
}