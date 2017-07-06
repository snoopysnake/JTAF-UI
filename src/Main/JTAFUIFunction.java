package Main;

import JTAF.Command;
import JTAF.Function;
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
import java.util.HashMap;

/**
 * Created by Michael on 07/05/2017.
 */
public class JTAFUIFunction {
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

    private String functionName;
    private StackPane functionHeader;
    private GridPane functionGridPane;
    private Stage functionWindow;
    private int row = 0;
    private double height = 0;

    public JTAFUIFunction(Function function) {
        functionName = function.getFunctionName();
        functionHeader = createLibraryHeader(function.getFunctionName());
        functionGridPane = createFunctionGridPane(function);
        functionWindow = createFunctionWindow(function); //must be created before mouse effects set
        setFunctionMouseEffects(function);
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public StackPane getFunctionHeader() {
        return this.functionHeader;
    }

    public GridPane getFunctionGridPane() {
        return this.functionGridPane;
    }

    public Stage getFunctionWindow() {
        return this.functionWindow;
    }

    public double getHeight() {
        return this.height;
    }

    public StackPane createLibraryHeader(String name) {
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

    public GridPane createFunctionGridPane(Function function) {
        final GridPane functionGridPane = new GridPane();
        functionGridPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(LIBRARY_KEY_WIDTH);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(LIBRARY_DATA_WIDTH);
        functionGridPane.getColumnConstraints().addAll(col1, col2);

        row = 0;
        height = 0;
        if (function.hasFunctionUsage()) {
            createFunctionUsagePanes(function, functionGridPane);
        }
        if (function.hasRequiredParameters()) {
            createParameterPanes("Required Parameters", function.getRequiredParameters(), functionGridPane);
        }
        if (function.hasOptionalParameters()) {
            createParameterPanes("Optional Parameters", function.getOptionalParameters(), functionGridPane);
        }
        if (function.hasFunctionBody()) {
            createFunctionBodyPanes(function, functionGridPane);
        }
        if (function.hasFunctionResults()) {
            createParameterPanes("Produces", function.getFunctionResults(), functionGridPane);
        }
        functionGridPane.setAlignment(Pos.CENTER);
        functionGridPane.setPrefSize(LIBRARY_TOTAL_WIDTH, height);
        functionGridPane.setVisible(false);
        functionGridPane.setManaged(false);

        return functionGridPane;
    }

    public void createFunctionUsagePanes(Function function, GridPane functionGridPane) {
        StackPane usagePane = new StackPane();
        usagePane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        usagePane.getChildren().add(new Text("Usage"));
        usagePane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        functionGridPane.add(usagePane, 0, row);
        StackPane usagePaneInfo = new StackPane();
        Text functionUsageText = new Text(function.getFunctionUsage().trim());
        functionUsageText.setWrappingWidth(LIBRARY_DATA_WIDTH-10);
        functionUsageText.setTextAlignment(TextAlignment.CENTER);
        usagePaneInfo.getChildren().add(functionUsageText); //should make height variable
        usagePaneInfo.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        functionGridPane.add(usagePaneInfo, 1, row);
        row++;
        double rowHeight = usagePaneInfo.getBoundsInParent().getHeight();
        if (rowHeight < 50)
            rowHeight = 50;
        RowConstraints rowConstraint = new RowConstraints(rowHeight);
        functionGridPane.getRowConstraints().add(rowConstraint);
        height += rowConstraint.getPrefHeight();
    }

    public void createFunctionBodyPanes(Function function, GridPane functionGridPane) {
        ArrayList<StackPane> bodyPanes = new ArrayList<>();
        StackPane functionBodyPane = new StackPane();
        functionBodyPane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
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
        RowConstraints rowConstraint = new RowConstraints(50);
        functionGridPane.getRowConstraints().add(rowConstraint);
        height += rowConstraint.getPrefHeight();

        for (int i = 0; i < function.getFunctionBody().size(); i++) {
            String functionBodyCommandName = function.getFunctionBody().get(i);
            StackPane functionBodyChildPane = new StackPane();
            functionBodyChildPane.setBackground(new Background(new BackgroundFill(LIBRARY_ATTRIBUTE_CHILD_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            Text functionBodyCommandNameText = new Text(functionBodyCommandName);
            functionBodyCommandNameText.setWrappingWidth(LIBRARY_KEY_WIDTH-10);
            functionBodyCommandNameText.setTextAlignment(TextAlignment.CENTER);
            functionBodyChildPane.getChildren().add(functionBodyCommandNameText);
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
            double rowHeight = functionBodyChildPane.getBoundsInParent().getHeight();
            if (rowHeight < 30)
                rowHeight = 30;
            rowConstraint = new RowConstraints(rowHeight);
            functionGridPane.getRowConstraints().add(rowConstraint);
            height += rowConstraint.getPrefHeight();
        }
    }

    public void createParameterPanes(String parameter, ArrayList<Parameter> parameterList, GridPane gridPane) {
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

    public Stage createFunctionWindow(Function function) {
        Stage windowStage = new Stage();
        VBox windowedVBox = new VBox();
//        StackPane windowedFunctionHeader = createCategoryHeader(function.getFunctionName(),25, LIBRARY_HEADER_SELECTED_COLOR);
        GridPane windowedFunction = createFunctionGridPane(function);
        windowedFunction.setVisible(true);
        windowedFunction.setManaged(true);
//        windowedVBox.getChildren().addAll(windowedFunctionHeader, windowedFunction);
        windowedVBox.getChildren().addAll(windowedFunction);
        windowStage.setScene(new Scene(windowedVBox,LIBRARY_TOTAL_WIDTH,windowedVBox.getPrefHeight()));
        return windowStage;
    }

    public void setFunctionMouseEffects(Function function) {
        DropShadow glow = new DropShadow();
        glow.setOffsetY(0);
        glow.setOffsetX(0);
        glow.setColor(Color.WHITE);
        glow.setWidth(25);
        glow.setHeight(25);

        Text functionText = (Text) functionHeader.getChildren().get(0);
        Button editWindowButton = (Button) functionHeader.getChildren().get(1);
        ImageView imgView1 = (ImageView) editWindowButton.getGraphic();
        Button openWindowButton = (Button) functionHeader.getChildren().get(2);
        ImageView imgView2 = (ImageView) openWindowButton.getGraphic();

        editWindowButton.setOnMouseEntered(event -> {
            imgView1.setEffect(glow);
        });
        editWindowButton.setOnMouseExited(event -> {
            imgView1.setEffect(null);
        });

        editWindowButton.setOnMouseClicked(event -> {
            //EDIT FUNCTION
        });

        openWindowButton.setOnMouseEntered(event -> {
            imgView2.setEffect(glow);
        });
        openWindowButton.setOnMouseExited(event -> {
            imgView2.setEffect(null);
        });

        //function in new window
        openWindowButton.setOnMouseClicked(event -> {
            Stage windowStage = functionWindow;
            windowStage.setTitle(function.getFunctionName());
            if (windowStage.isShowing())
                windowStage.close();
            windowStage.show();
        });

        functionHeader.setOnMouseEntered(event -> {
//            functionHeader.setEffect(glow);
            if (!functionHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                functionHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        functionHeader.setOnMouseExited(event -> {
//            functionHeader.setEffect(null);
            if (!functionHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                functionHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });

        functionHeader.setOnMouseClicked(event -> {
            if (functionHeader.getBackground().equals(new Background(new BackgroundFill(LIBRARY_SEARCH_COLOR, CornerRadii.EMPTY, Insets.EMPTY)))) {
                functionHeader.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                editWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
                openWindowButton.setBackground(new Background(new BackgroundFill(LIBRARY_HEADER_SELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            if (functionGridPane.isVisible()) {
                functionGridPane.setVisible(false);
                functionGridPane.setManaged(false);
            } else {
                functionGridPane.setVisible(true);
                functionGridPane.setManaged(true);
            }

        });
    }
}