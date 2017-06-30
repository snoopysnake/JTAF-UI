package sample;

import JTAF.Command;
import JTAF.Function;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFLibrary {
    private ScrollPane libraryPane;
    private HashMap<String, Stage> commandWindows = new HashMap<>();
    private HashMap<String, Stage> functionWindows = new HashMap<>();
    private HashMap<String, ArrayList<StackPane>> libraryBodyPanes = new HashMap<>();
    private HashMap<String, ArrayList<StackPane>> windowedLibraryBodyPanes = new HashMap<>();

    public JTAFLibrary(String libraryPath) throws IOException, SAXException, ParserConfigurationException {
        libraryPane = new ScrollPane();
        libraryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        libraryPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        VBox libraryBox = new VBox();
        VBox commandBox = new VBox();
        VBox functionBox = new VBox();

        libraryBox.setPrefWidth(800);
        libraryBox.setPrefHeight(600);

        LibraryParser libraryParser = new LibraryParser(libraryPath);
        ArrayList<Command> commands = libraryParser.getCommands();
        ArrayList<Function> functions = libraryParser.getFunctions();

        if(!commands.isEmpty()) {
            StackPane commandBoxHeader = createBoxHeader("COMMANDS",25, Color.DARKBLUE);
            commandBox.getChildren().add(commandBoxHeader);
            for (int i = 0; i < commands.size(); i++) {
                Command command = commands.get(i);
                StackPane commandHeader = createLibraryHeader(command.getCommandName());
                GridPane commandGridPane = createCommandGridPane(command);
                createCommandWindows(command); //must be created before mouse effects set
                commandMouseEffects(commandHeader, commandGridPane, command);
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
                    maximizeCommands(commandBox);
                else minimizeCommands(commandBox);
            });
        }
        if(!functions.isEmpty()) {
            StackPane functionBoxHeader = createBoxHeader("FUNCTIONS",25,Color.DARKBLUE);
            functionBox.getChildren().add(functionBoxHeader);
            for (int i = 0; i < libraryParser.getFunctions().size(); i++) {
                Function function = functions.get(i);
                StackPane functionHeader = createLibraryHeader(function.getFunctionName());
                GridPane functionGridPane = createFunctionGridPane(function, libraryBodyPanes);
                createFunctionWindows(function); //must be created before mouse effects set
                functionMouseEffects(functionHeader, functionGridPane, function);
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
                    maximizeCommands(functionBox);
                else minimizeCommands(functionBox);
            });
        }

        libraryBox.getChildren().add(commandBox);
        libraryBox.getChildren().add(functionBox);
        libraryPane.setContent(libraryBox);
    }

    public ScrollPane getLibraryPane() {
        return this.libraryPane;
    }

    public StackPane createBoxHeader(String name, int fontSize, Color color) {
        StackPane boxHeader = new StackPane();

        Label boxHeaderLabel = new Label(name);
        boxHeaderLabel.setTextFill(Color.WHITE);
        boxHeaderLabel.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        boxHeaderLabel.setFont(new Font("Arial", fontSize));
        boxHeaderLabel.setPrefWidth(800);
        boxHeader.getChildren().add(boxHeaderLabel);
        return boxHeader;
    }

    public StackPane createLibraryHeader(String name) {
        StackPane libraryHeader = new StackPane();
        libraryHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Label libraryHeaderLabel = new Label(name);
        libraryHeaderLabel.setTextFill(Color.WHITE);
        libraryHeaderLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        libraryHeaderLabel.setFont(new Font("Arial", 20));
        libraryHeaderLabel.setPrefWidth(800);
        libraryHeader.getChildren().add(libraryHeaderLabel);

        Button classWindowButton = new Button();
        Image img = new Image(getClass().getResourceAsStream("images\\ic_open_in_new_white_12dp.png"));
        ImageView imgView = new ImageView(img);
        classWindowButton.setGraphic(imgView);
        classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        libraryHeader.getChildren().add(classWindowButton);
        StackPane.setAlignment(classWindowButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(classWindowButton, new Insets(0, 30, 0, 0));

        return libraryHeader;
    }

    public GridPane createCommandGridPane(Command command) {
        final GridPane commandGridPane = new GridPane();
        commandGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(300);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(500);
        commandGridPane.getColumnConstraints().addAll(col1,col2);

        int row = 0;
        int height = 0;
        StackPane classPane = new StackPane();
        classPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        classPane.getChildren().add(new Text("Class"));
        classPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(classPane,0,row);
        StackPane classPaneInfo = new StackPane();
        classPaneInfo.getChildren().add(new Text(command.getCommandClass()));
        classPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(classPaneInfo,1,row);
        row++;
        RowConstraints rowConstraint = new RowConstraints(50);
        commandGridPane.getRowConstraints().add(rowConstraint);
        height+=rowConstraint.getMaxHeight();
        if (command.hasCommandUsage()) {
            StackPane usagePane = new StackPane();
            usagePane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            usagePane.getChildren().add(new Text("Usage"));
            usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(usagePane, 0, row);
            StackPane usagePaneInfo = new StackPane();
            Text commandUsageText = new Text(command.getCommandUsage().trim());
            commandUsageText.setWrappingWidth(500);
            commandUsageText.setTextAlignment(TextAlignment.CENTER);
            usagePaneInfo.getChildren().add(commandUsageText); //should make height variable
            usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(usagePaneInfo, 1, row);
            row++;
            double rowHeight = usagePaneInfo.getBoundsInParent().getHeight();
            if (rowHeight < 50)
                rowHeight = 50;
            rowConstraint = new RowConstraints(rowHeight);
            commandGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();
        }
        if (command.hasRequiredParameters()) {
            StackPane requiredParamPane = new StackPane();
            requiredParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            requiredParamPane.getChildren().add(new Text("Required Parameters"));
            requiredParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(requiredParamPane, 0, row);
            StackPane requiredParamPaneInfo = new StackPane();
            requiredParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(requiredParamPaneInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            commandGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < command.getRequiredParameters().size() * 3; i++) {
                Text requiredParamKey = new Text("");
                Text requiredParamData = new Text("");
                int commandIndex = i/3;
                if (i % 3 == 0) {
                    requiredParamKey.setText("Name");
                    requiredParamData.setText(command.getRequiredParameters().get(commandIndex).getName());
                }
                if (i % 3 == 1) {
                    requiredParamKey.setText("Tag");
                    requiredParamData.setText(command.getRequiredParameters().get(commandIndex).getTag());
                }
                if (i % 3 == 2) {
                    requiredParamKey.setText("Text");
                    requiredParamData.setWrappingWidth(500);
                    requiredParamData.setTextAlignment(TextAlignment.CENTER);
                    requiredParamData.setText(command.getRequiredParameters().get(commandIndex).getText());
                }
                StackPane requiredParamChildPane = new StackPane();
                requiredParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                requiredParamChildPane.getChildren().add(requiredParamKey);
                requiredParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(requiredParamChildPane, 0, row);
                StackPane requiredParamChildPaneInfo = new StackPane();
                requiredParamChildPaneInfo.getChildren().add(requiredParamData);
                requiredParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(requiredParamChildPaneInfo, 1, row);
                row++;
                double rowHeight = requiredParamChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                commandGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        if (command.hasOptionalParameters()) {
            StackPane optionalParamPane = new StackPane();
            optionalParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            optionalParamPane.getChildren().add(new Text("Optional Parameters"));
            optionalParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(optionalParamPane, 0, row);
            StackPane optionalParamPaneInfo = new StackPane();
            optionalParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(optionalParamPaneInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            commandGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < command.getOptionalParameters().size() * 3; i++) {
                Text optionalParamKey = new Text("");
                Text optionalParamData = new Text("");
                int commandIndex = i/3;
                if (i % 3 == 0) {
                    optionalParamKey.setText("Name");
                    optionalParamData.setText(command.getOptionalParameters().get(commandIndex).getName());
                }
                if (i % 3 == 1) {
                    optionalParamKey.setText("Tag");
                    optionalParamData.setText(command.getOptionalParameters().get(commandIndex).getTag());
                }
                if (i % 3 == 2) {
                    optionalParamKey.setText("Text");
                    optionalParamData.setWrappingWidth(500);
                    optionalParamData.setTextAlignment(TextAlignment.CENTER);
                    optionalParamData.setText(command.getOptionalParameters().get(commandIndex).getText());
                }
                StackPane optionalParamChildPane = new StackPane();
                optionalParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                optionalParamChildPane.getChildren().add(optionalParamKey);
                optionalParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(optionalParamChildPane, 0, row);
                StackPane optionalParamChildPaneInfo = new StackPane();
                optionalParamChildPaneInfo.getChildren().add(optionalParamData);
                optionalParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(optionalParamChildPaneInfo, 1, row);
                row++;
                double rowHeight = optionalParamChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                commandGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        if (command.hasCommandResults()) {
            StackPane resultsPane = new StackPane();
            resultsPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            resultsPane.getChildren().add(new Text("Produces"));
            resultsPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(resultsPane, 0, row);
            StackPane resultPanesInfo = new StackPane();
            resultPanesInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            commandGridPane.add(resultPanesInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            commandGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < command.getCommandResults().size() * 3; i++) {

                Text resultsParamKey = new Text("");
                Text resultsParamData = new Text("");
                int commandIndex = i/3;
                if (i % 3 == 0) {
                    resultsParamKey.setText("Name");
                    resultsParamData.setText(command.getCommandResults().get(commandIndex).getName());
                }
                if (i % 3 == 1) {
                    resultsParamKey.setText("Tag");
                    resultsParamData.setText(command.getCommandResults().get(commandIndex).getTag());
                }
                if (i % 3 == 2) {
                    resultsParamKey.setText("Text");
                    resultsParamData.setWrappingWidth(500);
                    resultsParamData.setTextAlignment(TextAlignment.CENTER);
                    resultsParamData.setText(command.getCommandResults().get(commandIndex).getText());
                }
                StackPane resultsChildPane = new StackPane();
                resultsChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                resultsChildPane.getChildren().add(resultsParamKey);
                resultsChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(resultsChildPane, 0, row);
                StackPane resultsChildPaneInfo = new StackPane();
                resultsChildPaneInfo.getChildren().add(resultsParamData);
                resultsChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                commandGridPane.add(resultsChildPaneInfo, 1, row);
                row++;
                double rowHeight = resultsChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                commandGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        commandGridPane.setAlignment(Pos.CENTER);
        commandGridPane.setPrefSize(800, height);
        commandGridPane.setVisible(false);
        commandGridPane.setManaged(false);

//        for (int i = 0; i < commandGridPane.getChildren().size()/2; i++) {
//            RowConstraints rows = new RowConstraints(50);
//            commandGridPane.getRowConstraints().add(rows);
//        }

        return commandGridPane;
    }

    public void commandMouseEffects(StackPane commandHeader, GridPane commandGridPane, Command command) {
        DropShadow glow = new DropShadow();
        glow.setOffsetY(0);
        glow.setOffsetX(0);
        glow.setColor(Color.AQUA);
        glow.setWidth(25);
        glow.setHeight(25);

        Label commandLabel = (Label) commandHeader.getChildren().get(0);
        Button classWindowButton = (Button) commandHeader.getChildren().get(1);
        ImageView imgView = (ImageView) classWindowButton.getGraphic();

        classWindowButton.setOnMouseEntered(event -> {
            imgView.setEffect(glow);
        });
        classWindowButton.setOnMouseExited(event -> {
            imgView.setEffect(null);
        });

        //command in new window
        classWindowButton.setOnMouseClicked(event -> {
            Stage windowStage = commandWindows.get(command.getCommandName());
            if (windowStage.isShowing())
                windowStage.close();
            windowStage.show();
        });

        commandHeader.setOnMouseEntered(event -> {
                    commandHeader.setEffect(glow);
                    commandHeader.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    commandLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                }
        );
        commandHeader.setOnMouseExited(event -> {
                    commandHeader.setEffect(null);
                    commandHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    commandLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                }
        );

        commandHeader.setOnMouseClicked(event -> {
            if (commandGridPane.isVisible()) {
                commandGridPane.setVisible(false);
                commandGridPane.setManaged(false);
            } else {
                commandGridPane.setVisible(true);
                commandGridPane.setManaged(true);
            }

        });
    }

    public void minimizeCommands(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(false);
                node.setManaged(false);
            }
        }
    }

    public void maximizeCommands(VBox commandBox) {
        for (Node node : commandBox.getChildren()) {
            if (node.getClass().equals(GridPane.class)) {
                node.setVisible(true);
                node.setManaged(true);
            }
        }
    }

    public GridPane createFunctionGridPane(Function function, HashMap<String, ArrayList<StackPane>> totalBodyPanes) {
        final GridPane functionGridPane = new GridPane();
        functionGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(300);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(500);
        functionGridPane.getColumnConstraints().addAll(col1,col2);

        int row = 0;
        int height = 0;
        RowConstraints rowConstraint;
        if (function.hasFunctionUsage()) {
            StackPane usagePane = new StackPane();
            usagePane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            usagePane.getChildren().add(new Text("Usage"));
            usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(usagePane, 0, row);
            StackPane usagePaneInfo = new StackPane();
            Text functionUsageText = new Text(function.getFunctionUsage().trim());
            functionUsageText.setWrappingWidth(500);
            functionUsageText.setTextAlignment(TextAlignment.CENTER);
            usagePaneInfo.getChildren().add(functionUsageText); //should make height variable
            usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(usagePaneInfo, 1, row);
            row++;
            double rowHeight = usagePaneInfo.getBoundsInParent().getHeight();
            if (rowHeight < 50)
                rowHeight = 50;
            rowConstraint = new RowConstraints(rowHeight);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();
        }
        if (function.hasRequiredParameters()) {
            StackPane requiredParamPane = new StackPane();
            requiredParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            requiredParamPane.getChildren().add(new Text("Required Parameters"));
            requiredParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(requiredParamPane, 0, row);
            StackPane requiredParamPaneInfo = new StackPane();
            requiredParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(requiredParamPaneInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < function.getRequiredParameters().size() * 3; i++) {
                Text requiredParamKey = new Text("");
                Text requiredParamData = new Text("");
                int functionIndex = i/3;
                if (i % 3 == 0) {
                    requiredParamKey.setText("Name");
                    requiredParamData.setText(function.getRequiredParameters().get(functionIndex).getName());
                }
                if (i % 3 == 1) {
                    requiredParamKey.setText("Tag");
                    requiredParamData.setText(function.getRequiredParameters().get(functionIndex).getTag());
                }
                if (i % 3 == 2) {
                    requiredParamKey.setText("Text");
                    requiredParamData.setWrappingWidth(500);
                    requiredParamData.setTextAlignment(TextAlignment.CENTER);
                    requiredParamData.setText(function.getRequiredParameters().get(functionIndex).getText());
                }
                StackPane requiredParamChildPane = new StackPane();
                requiredParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                requiredParamChildPane.getChildren().add(requiredParamKey);
                requiredParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(requiredParamChildPane, 0, row);
                StackPane requiredParamChildPaneInfo = new StackPane();
                requiredParamChildPaneInfo.getChildren().add(requiredParamData);
                requiredParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(requiredParamChildPaneInfo, 1, row);
                row++;
                double rowHeight = requiredParamChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                functionGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        if (function.hasOptionalParameters()) {
            StackPane optionalParamPane = new StackPane();
            optionalParamPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            optionalParamPane.getChildren().add(new Text("Optional Parameters"));
            optionalParamPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(optionalParamPane, 0, row);
            StackPane optionalParamPaneInfo = new StackPane();
            optionalParamPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(optionalParamPaneInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < function.getOptionalParameters().size() * 3; i++) {
                Text optionalParamKey = new Text("");
                Text optionalParamData = new Text("");
                int functionIndex = i/3;
                if (i % 3 == 0) {
                    optionalParamKey.setText("Name");
                    optionalParamData.setText(function.getOptionalParameters().get(functionIndex).getName());
                }
                if (i % 3 == 1) {
                    optionalParamKey.setText("Tag");
                    optionalParamData.setText(function.getOptionalParameters().get(functionIndex).getTag());
                }
                if (i % 3 == 2) {
                    optionalParamKey.setText("Text");
                    optionalParamData.setWrappingWidth(500);
                    optionalParamData.setTextAlignment(TextAlignment.CENTER);
                    optionalParamData.setText(function.getOptionalParameters().get(functionIndex).getText());
                }
                StackPane optionalParamChildPane = new StackPane();
                optionalParamChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                optionalParamChildPane.getChildren().add(optionalParamKey);
                optionalParamChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(optionalParamChildPane, 0, row);
                StackPane optionalParamChildPaneInfo = new StackPane();
                optionalParamChildPaneInfo.getChildren().add(optionalParamData);
                optionalParamChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(optionalParamChildPaneInfo, 1, row);
                row++;
                double rowHeight = optionalParamChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                functionGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        if (function.hasFunctionBody()) {
            ArrayList<StackPane> bodyPanes = new ArrayList<>();
            StackPane functionBodyPane = new StackPane();
            functionBodyPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            functionBodyPane.getChildren().add(new Text("Body"));
            functionBodyPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            bodyPanes.add(functionBodyPane); //add pane 1
            functionGridPane.add(functionBodyPane, 0, row);
            StackPane functionBodyPaneInfo = new StackPane();
            functionBodyPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            bodyPanes.add(functionBodyPaneInfo); //add pane 2
            functionGridPane.add(functionBodyPaneInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height += rowConstraint.getMaxHeight();

            for (int i = 0; i < function.getFunctionBody().size(); i++) {
                String functionBodyCommandName = function.getFunctionBody().get(i);
                StackPane functionBodyChildPane = new StackPane();
                functionBodyChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                functionBodyChildPane.getChildren().add(new Text(functionBodyCommandName));
                functionBodyChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                bodyPanes.add(functionBodyChildPane);
                functionGridPane.add(functionBodyChildPane, 0, row);
                StackPane functionBodyChildPaneInfo = new StackPane();
                functionBodyChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                bodyPanes.add(functionBodyChildPaneInfo);
                functionGridPane.add(functionBodyChildPaneInfo, 1, row);
                row++;
                rowConstraint = new RowConstraints(25);
                functionGridPane.getRowConstraints().add(rowConstraint);
                height += rowConstraint.getMaxHeight();
            }
            totalBodyPanes.put(function.getFunctionName(), bodyPanes);
        }
        if (function.hasFunctionResults()) {
            StackPane resultsPane = new StackPane();
            resultsPane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            resultsPane.getChildren().add(new Text("Produces"));
            resultsPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(resultsPane, 0, row);
            StackPane resultPanesInfo = new StackPane();
            resultPanesInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            functionGridPane.add(resultPanesInfo, 1, row);
            row++;
            rowConstraint = new RowConstraints(50);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getMaxHeight();

            for(int i = 0; i < function.getFunctionResults().size() * 3; i++) {
                Text resultsParamKey = new Text("");
                Text resultsParamData = new Text("");
                int functionIndex = i/3;
                if (i % 3 == 0) {
                    resultsParamKey.setText("Name");
                    resultsParamData.setText(function.getFunctionResults().get(functionIndex).getName());
                }
                if (i % 3 == 1) {
                    resultsParamKey.setText("Tag");
                    resultsParamData.setText(function.getFunctionResults().get(functionIndex).getTag());
                }
                if (i % 3 == 2) {
                    resultsParamKey.setText("Text");
                    resultsParamData.setWrappingWidth(500);
                    resultsParamData.setTextAlignment(TextAlignment.CENTER);
                    resultsParamData.setText(function.getFunctionResults().get(functionIndex).getText());
                }
                StackPane resultsChildPane = new StackPane();
                resultsChildPane.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                resultsChildPane.getChildren().add(resultsParamKey);
                resultsChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(resultsChildPane, 0, row);
                StackPane resultsChildPaneInfo = new StackPane();
                resultsChildPaneInfo.getChildren().add(resultsParamData);
                resultsChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                functionGridPane.add(resultsChildPaneInfo, 1, row);
                row++;
                double rowHeight = resultsChildPaneInfo.getBoundsInParent().getHeight();
                if (rowHeight < 25)
                    rowHeight = 25;
                rowConstraint = new RowConstraints(rowHeight);
                functionGridPane.getRowConstraints().add(rowConstraint);
                height+=rowConstraint.getMaxHeight();
            }
        }
        functionGridPane.setAlignment(Pos.CENTER);
        functionGridPane.setPrefSize(800, height);
        functionGridPane.setVisible(false);
        functionGridPane.setManaged(false);

//        for (int i = 0; i < commandGridPane.getChildren().size()/2; i++) {
//            RowConstraints rows = new RowConstraints(50);
//            commandGridPane.getRowConstraints().add(rows);
//        }

        return functionGridPane;
    }

    public void functionMouseEffects(StackPane functionHeader, GridPane functionGridPane, Function function) {
        DropShadow glow = new DropShadow();
        glow.setOffsetY(0);
        glow.setOffsetX(0);
        glow.setColor(Color.AQUA);
        glow.setWidth(25);
        glow.setHeight(25);

        Label commandLabel = (Label) functionHeader.getChildren().get(0);
        Button classWindowButton = (Button) functionHeader.getChildren().get(1);
        ImageView imgView = (ImageView) classWindowButton.getGraphic();

        classWindowButton.setOnMouseEntered(event -> {
            imgView.setEffect(glow);
        });
        classWindowButton.setOnMouseExited(event -> {
            imgView.setEffect(null);
        });

        //command in new window
        classWindowButton.setOnMouseClicked(event -> {
            Stage windowStage = functionWindows.get(function.getFunctionName());
            if (windowStage.isShowing())
                windowStage.close();
            windowStage.show();
        });

        functionHeader.setOnMouseEntered(event -> {
                    functionHeader.setEffect(glow);
                    functionHeader.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    commandLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                }
        );
        functionHeader.setOnMouseExited(event -> {
                    functionHeader.setEffect(null);
                    functionHeader.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    commandLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    classWindowButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                }
        );

        functionHeader.setOnMouseClicked(event -> {
            if (functionGridPane.isVisible()) {
                functionGridPane.setVisible(false);
                functionGridPane.setManaged(false);
            } else {
                functionGridPane.setVisible(true);
                functionGridPane.setManaged(true);
            }

        });
    }

    public HashMap<String, ArrayList<StackPane>> getLibraryBodyPanes() {
        return this.libraryBodyPanes;
    }

    public HashMap<String, ArrayList<StackPane>> getWindowedLibraryBodyPanes() {
        return this.windowedLibraryBodyPanes;
    }

    public void createCommandWindows(Command command) {
        Stage windowStage = new Stage();
        VBox windowedVBox = new VBox();
        StackPane windowedCommandHeader = createBoxHeader(command.getCommandName(),25, Color.DARKBLUE);
        GridPane windowedCommand = createCommandGridPane(command);
        windowedCommand.setVisible(true);
        windowedCommand.setManaged(true);
        windowedVBox.getChildren().addAll(windowedCommandHeader, windowedCommand);
        windowStage.setScene(new Scene(windowedVBox,300,windowedCommand.getPrefHeight()));
        commandWindows.put(command.getCommandName(),windowStage);
    }

    public void createFunctionWindows(Function function) {
        Stage windowStage = new Stage();
        VBox windowedVBox = new VBox();
        StackPane windowedFunctionHeader = createBoxHeader(function.getFunctionName(),25, Color.DARKBLUE);
        GridPane windowedFunction = createFunctionGridPane(function, windowedLibraryBodyPanes);
        windowedFunction.setVisible(true);
        windowedFunction.setManaged(true);
        windowedVBox.getChildren().addAll(windowedFunctionHeader, windowedFunction);
        windowStage.setScene(new Scene(windowedVBox,300,windowedFunction.getPrefHeight()));
        functionWindows.put(function.getFunctionName(), windowStage);
    }


    public HashMap<String, Stage> getCommandWindows() {
        return this.commandWindows;
    }

    public HashMap<String, Stage> getFunctionWindows() {
        return this.functionWindows;
    }
}