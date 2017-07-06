package Main;

import JTAF.Command;
import JTAF.Parameter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Michael on 07/05/2017.
 */
public class JTAFUICommand {
    public double LIBRARY_TOTAL_WIDTH = 1000;
    public double LIBRARY_KEY_WIDTH = 400;
    public double LIBRARY_DATA_WIDTH = 600;
    public Color LIBRARY_BACKGROUND_COLOR = Color.WHITE;
    public Color CATEGORY_HEADER_COLOR = Color.web("#015DA6");
    public Color LIBRARY_HEADER_SELECTED_COLOR = Color.web("#66B4E3");
    public Color LIBRARY_HEADER_UNSELECTED_COLOR = Color.web("#3393DA");
    public Color LIBRARY_ATTRIBUTE_COLOR = Color.web("#BEBDB7");
    public Color LIBRARY_ATTRIBUTE_CHILD_COLOR = Color.web("#DEDEDB");
    public Color LIBRARY_SEARCH_COLOR = Color.web("#B1D036");
    public String FINRA_FONT = "Georgia";

    private String commandName;
    private StackPane commandHeader;
    private GridPane commandGridPane;
    private Stage commandWindow;
    private int row = 0;
    private double height = 0;
    public JTAFUICommand(Command command) {
        commandName = command.getCommandName();
        commandHeader = createLibraryHeader(command.getCommandName());
        commandGridPane = createCommandGridPane(command);
        commandWindow = createCommandWindow(command); //must be created before mouse effects set
        setCommandMouseEffects(command);

    }

    public String getCommandName() {
        return this.commandName;
    }

    public StackPane getCommandHeader() {
        return this.commandHeader;
    }

    public GridPane getCommandGridPane() {
        return this.commandGridPane;
    }

    public Stage getCommandWindow() {
        return this.commandWindow;
    }

    public double getHeight() {
        return this.height;
    }

    private StackPane createLibraryHeader(String name) {
        StackPane libraryHeader = new StackPane();
        libraryHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        Text libraryHeaderText = new Text(name);
        libraryHeader.setAlignment(Pos.CENTER_LEFT);
        libraryHeader.setPadding(new Insets(5,5,5,7));
        libraryHeaderText.setFill(Color.WHITE);
        libraryHeaderText.setFont(new Font(FINRA_FONT, 20));
        libraryHeader.setPrefWidth(LIBRARY_TOTAL_WIDTH);
        libraryHeader.getChildren().add(libraryHeaderText);

        Button openWindowButton = new Button();
        Image img1 = new Image(getClass().getResourceAsStream("images\\ic_open_in_new_white_18dp.png"));
        ImageView imgView1 = new ImageView(img1);
        openWindowButton.setGraphic(imgView1);
        openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        Button editWindowButton = new Button();
        Image img2 = new Image(getClass().getResourceAsStream("images\\ic_create_white_18dp.png"));
        ImageView imgView2 = new ImageView(img2);
        editWindowButton.setGraphic(imgView2);
        editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        libraryHeader.getChildren().add(editWindowButton);
        libraryHeader.getChildren().add(openWindowButton);
        StackPane.setAlignment(editWindowButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(editWindowButton, new Insets(0, 90, 0, 0));
        StackPane.setAlignment(openWindowButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(openWindowButton, new Insets(0, 30, 0, 0));
        return libraryHeader;
    }

    private GridPane createCommandGridPane(Command command) {
        final GridPane commandGridPane = new GridPane();
        commandGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(LIBRARY_KEY_WIDTH);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(LIBRARY_DATA_WIDTH);
        commandGridPane.getColumnConstraints().addAll(col1,col2);

        row = 0;
        height = 0;
        createCommandClassPanes(command, commandGridPane);
        if (command.hasCommandUsage()) {
            createCommandUsagePanes(command, commandGridPane);
        }
        if (command.hasRequiredParameters()) {
            createParameterPanes("Required Parameters",command.getRequiredParameters(),commandGridPane);
        }
        if (command.hasOptionalParameters()) {
            createParameterPanes("Optional Parameters",command.getOptionalParameters(),commandGridPane);
        }
        if (command.hasCommandResults()) {
            createParameterPanes("Produces",command.getCommandResults(),commandGridPane);
        }
        commandGridPane.setAlignment(Pos.CENTER);
        commandGridPane.setPrefSize(LIBRARY_TOTAL_WIDTH, height);
        commandGridPane.setVisible(false);
        commandGridPane.setManaged(false);

        return commandGridPane;
    }

    private void createCommandClassPanes(Command command, GridPane commandGridPane) {
        StackPane classPane = new StackPane();
        classPane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        classPane.getChildren().add(new Text("Class"));
        classPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(classPane, 0, row);
        StackPane classPaneInfo = new StackPane();
        classPaneInfo.getChildren().add(new Text(command.getCommandClass()));
        classPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(classPaneInfo, 1, row);
        row++;
        RowConstraints rowConstraint = new RowConstraints(50);
        commandGridPane.getRowConstraints().add(rowConstraint);
        height+=rowConstraint.getPrefHeight();
    }

    private void createCommandUsagePanes(Command command, GridPane commandGridPane) {
        StackPane usagePane = new StackPane();
        usagePane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        usagePane.getChildren().add(new Text("Usage"));
        usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(usagePane, 0, row);
        StackPane usagePaneInfo = new StackPane();
        Text commandUsageText = new Text(command.getCommandUsage().trim());
        commandUsageText.setWrappingWidth(LIBRARY_DATA_WIDTH-10);
        commandUsageText.setTextAlignment(TextAlignment.CENTER);
        usagePaneInfo.getChildren().add(commandUsageText); //should make height variable
        usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        commandGridPane.add(usagePaneInfo, 1, row);
        row++;
        double rowHeight = usagePaneInfo.getBoundsInParent().getHeight();
        if (rowHeight < 50)
            rowHeight = 50;
        RowConstraints rowConstraint = new RowConstraints(rowHeight);
        commandGridPane.getRowConstraints().add(rowConstraint);
        height += rowConstraint.getPrefHeight();
    }

    private void createParameterPanes(String parameter, ArrayList<Parameter> parameterList, GridPane gridPane) {
        StackPane paramPane = new StackPane();
        paramPane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        paramPane.getChildren().add(new Text(parameter));
        paramPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        gridPane.add(paramPane, 0, row);
        StackPane paramPaneInfo = new StackPane();
        paramPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        gridPane.add(paramPaneInfo, 1, row);
        row++;
        RowConstraints rowConstraint = new RowConstraints(50);
        gridPane.getRowConstraints().add(rowConstraint);
        height+=rowConstraint.getPrefHeight();

        for(int i = 0; i < parameterList.size() * 3; i++) {
            Text requiredParamKey = new Text("");
            Text requiredParamData = new Text("");
            int commandIndex = i/3;
            if (i % 3 == 0) {
                requiredParamKey.setText("Name");
                requiredParamData.setText(parameterList.get(commandIndex).getName());
            }
            if (i % 3 == 1) {
                requiredParamKey.setText("Tag");
                requiredParamData.setText(parameterList.get(commandIndex).getTag());
            }
            if (i % 3 == 2) {
                requiredParamKey.setText("Text");
                requiredParamData.setWrappingWidth(LIBRARY_DATA_WIDTH-10);
                requiredParamData.setTextAlignment(TextAlignment.CENTER);
                requiredParamData.setText(parameterList.get(commandIndex).getText());
            }
            StackPane paramChildPane = new StackPane();
            paramChildPane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_CHILD_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            paramChildPane.getChildren().add(requiredParamKey);
            paramChildPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            gridPane.add(paramChildPane, 0, row);
            StackPane paramChildPaneInfo = new StackPane();
            paramChildPaneInfo.getChildren().add(requiredParamData);
            paramChildPaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            gridPane.add(paramChildPaneInfo, 1, row);
            row++;
            double rowHeight = paramChildPaneInfo.getBoundsInParent().getHeight();
            if (rowHeight < 25)
                rowHeight = 25;
            rowConstraint = new RowConstraints(rowHeight);
            gridPane.getRowConstraints().add(rowConstraint);
            height+=rowConstraint.getPrefHeight();
        }
    }

    private Stage createCommandWindow(Command command) {
        Stage windowStage = new Stage();
        VBox windowedVBox = new VBox();
//        StackPane windowedCommandHeader = createCategoryHeader(command.getCommandName(),25, CATEGORY_HEADER_COLOR);
        GridPane windowedCommand = createCommandGridPane(command);
        windowedCommand.setVisible(true);
        windowedCommand.setManaged(true);
//        windowedVBox.getChildren().addAll(windowedCommandHeader, windowedCommand);
        windowedVBox.getChildren().addAll(windowedCommand);
        windowStage.setScene(new Scene(windowedVBox,LIBRARY_TOTAL_WIDTH,windowedVBox.getPrefHeight()));
        return windowStage;
    }

    private void setCommandMouseEffects(Command command) {
        DropShadow glow = new DropShadow();
        glow.setOffsetY(0);
        glow.setOffsetX(0);
        glow.setColor(Color.WHITE);
        glow.setWidth(25);
        glow.setHeight(25);

        Text commandText = (Text) commandHeader.getChildren().get(0);
        Button editWindowButton = (Button) commandHeader.getChildren().get(1);
        ImageView imgView1 = (ImageView) editWindowButton.getGraphic();
        Button openWindowButton = (Button) commandHeader.getChildren().get(2);
        ImageView imgView2 = (ImageView) openWindowButton.getGraphic();

        editWindowButton.setOnMouseEntered(event -> {
            imgView1.setEffect(glow);
        });
        editWindowButton.setOnMouseExited(event -> {
            imgView1.setEffect(null);
        });

        editWindowButton.setOnMouseClicked(event -> {
            //EDIT COMMAND
        });

        openWindowButton.setOnMouseEntered(event -> {
            imgView2.setEffect(glow);
        });
        openWindowButton.setOnMouseExited(event -> {
            imgView2.setEffect(null);
        });

        //command in new window
        openWindowButton.setOnMouseClicked(event -> {
            Stage windowStage = commandWindow;
            windowStage.setTitle(command.getCommandName());
            if (windowStage.isShowing())
                windowStage.close();
            windowStage.show();
        });

        commandHeader.setOnMouseEntered(event -> {
//            commandHeader.setEffect(glow);
            if (!commandHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                commandHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        commandHeader.setOnMouseExited(event -> {
//            commandHeader.setEffect(null);
            if (!commandHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                commandHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        commandHeader.setOnMouseClicked(event -> {
            if (commandHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                commandHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            if (commandGridPane.isVisible()) {
                commandGridPane.setVisible(false);
                commandGridPane.setManaged(false);
            } else {
                commandGridPane.setVisible(true);
                commandGridPane.setManaged(true);
            }

        });
    }

}
