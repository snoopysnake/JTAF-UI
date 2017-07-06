package Main;

import JTAF.Library;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFUIDirectory {
    public Color DIRECTORY_UNSELECTED_COLOR = Color.web("#73787D");
    public Color DIRECTORY_SELECTED_COLOR = Color.WHITE; //TODO: change

    private ScrollPane directoryPane;
    private VBox directoryVBox;
    private ArrayList<ToggleButton> directoryButtons = new ArrayList<>();

    public JTAFUIDirectory() {
        directoryPane = new ScrollPane();
//        directoryPane.setPrefHeight(400);
        directoryPane.setPrefWidth(200);
        directoryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //TODO
        directoryVBox = new VBox();
        directoryVBox.setPrefHeight(500);
        directoryVBox.setBackground(new Background(new BackgroundFill(DIRECTORY_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        directoryPane.setContent(directoryVBox);
    }

    public ScrollPane getDirectoryPane() {
        return this.directoryPane;
    }

    public ArrayList<ToggleButton> getDirectoryButtons() {
        return directoryButtons;
    }

    public void setAsTestLibrary(ArrayList<Library> testLibrary, Node[] directoryState) {
        directoryState[0] = directoryPane;

        for (Library library: testLibrary) {
            String libraryName = library.getLibraryName();
            ToggleButton libraryPathButton = new ToggleButton(libraryName);
//            libraryPathButton.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            libraryPathButton.setBackground(new Background(new BackgroundFill(DIRECTORY_UNSELECTED_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
            libraryPathButton.setPrefSize(200,25);
            directoryButtons.add(libraryPathButton);
            directoryVBox.getChildren().add(libraryPathButton);
        }
    }

    public void setDirectoryPaneEvents(ArrayList<JTAFUILibrary> testLibraryUI, Label headerLabel,
                                       Node[] centerState, BorderPane defaultBorderPane) {
        for (int i = 0; i < testLibraryUI.size(); i++) {
            JTAFUILibrary jtafuiLibrary = testLibraryUI.get(i);
            ToggleButton libraryPathButton = directoryButtons.get(i);
            if (jtafuiLibrary.getLibraryName().equals(libraryPathButton.getText())) {
                libraryPathButton.setOnAction(event -> {
                    toggleUnselectAll(libraryPathButton, directoryButtons, DIRECTORY_UNSELECTED_COLOR);
                    toggleSelect(libraryPathButton, DIRECTORY_SELECTED_COLOR);
    //                if (testLibraryMap.containsKey(libraryPathButton.getText())) {
                        headerLabel.setText(libraryPathButton.getText()); //changes label in header
                        defaultBorderPane.setCenter(jtafuiLibrary.getLibraryPane());
                        centerState[0] = jtafuiLibrary.getLibraryPane();
    //                }

                    if (!libraryPathButton.isSelected()) {
                        libraryPathButton.setSelected(true);
                    }
                });
            }
        }
    }

    private void toggleUnselectAll(ToggleButton selectedButton, ArrayList<ToggleButton> otherButtons, Color unselected) {
        for (ToggleButton unselectedButton : otherButtons) {
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
