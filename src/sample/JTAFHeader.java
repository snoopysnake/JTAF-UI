package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFHeader {
    public double HEADER_TOTAL_WIDTH;

    private HBox headerBox;
    public JTAFHeader(double libraryWidth) {
        HEADER_TOTAL_WIDTH = libraryWidth;
        headerBox = new HBox();
        VBox headerRightHalfBox = new VBox();
        HBox headerLeftHalfBox = new HBox();
        HBox tabsBox = new HBox();
        BorderPane tabBarPane = new BorderPane();
        tabBarPane.setPrefHeight(50);

        headerRightHalfBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        headerRightHalfBox.setPrefWidth(HEADER_TOTAL_WIDTH);

        Label logo = new Label("LOGO"); //TODO: change to png
        logo.setPrefWidth(200); //same as directory/scrollpane
        logo.setPrefHeight(100);
        logo.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        headerLeftHalfBox.getChildren().add(logo);

        //design tab buttons
//        Label tabButtonLabel1 = new Label("Test Library");
//        tabButtonLabel1.setTextFill(Color.WHITE);
//        tabButtonLabel1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//        tabButtonLabel1.setFont(new Font("Arial", 30));
//        Label tabButtonLabel2 = new Label("Something1");
//        tabButtonLabel2.setTextFill(Color.WHITE);
//        tabButtonLabel2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
//        tabButtonLabel2.setFont(new Font("Arial", 30));

        //add tab buttons
        ToggleButton tabButton1 = new ToggleButton();
        tabButton1.setText("Test Library");
        tabButton1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton1.setPrefHeight(50);
        ToggleButton tabButton2 = new ToggleButton();
        tabButton2.setText("Something1");
        tabButton2.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        tabButton2.setPrefHeight(50);
        tabButton1.setAlignment(Pos.BOTTOM_LEFT);
        tabButton2.setAlignment(Pos.BOTTOM_LEFT);
        tabsBox.getChildren().addAll(tabButton1,tabButton2);


        HBox searchBarBox = new HBox();
        Button temp = new Button("search");
        searchBarBox.getChildren().add(temp);
        searchBarBox.setAlignment(Pos.BOTTOM_RIGHT);

        tabBarPane.setRight(searchBarBox);
        tabBarPane.setLeft(tabsBox);

        StackPane libraryNamePane = new StackPane(); //Important
        Label headerLabel = new Label("");
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY))); //make color coded
        headerLabel.setFont(new Font("Arial", 30));
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
}
