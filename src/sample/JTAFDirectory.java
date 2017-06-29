package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Michael on 06/29/2017.
 */
public class JTAFDirectory {
    private String PROJECT_DIR;
    private final String TEST_LIBRARY_DIR; //change depending on version
    private ScrollPane directoryPane;
    private ArrayList<String> testLibrary = new ArrayList();
    public JTAFDirectory(String projectPath, String libraryPath) throws IOException {
        this.PROJECT_DIR = projectPath;
        this.TEST_LIBRARY_DIR = libraryPath;

        File testLibraryPath = new File(PROJECT_DIR+TEST_LIBRARY_DIR);
        testLibrary = new ArrayList<>();
        if (!testLibraryPath.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText("Invalid test library directory!");
            alert.showAndWait();
        } else {
            testLibrary = searchLibrary(testLibraryPath.listFiles(), testLibrary, "", ".xml");
            System.out.println("Searched test library: " + testLibraryPath.getPath());
        }
    }

    public ScrollPane getDirectoryPane() {
        directoryPane = new ScrollPane();
        directoryPane.setPrefHeight(600);
        directoryPane.setPrefWidth(200);
        VBox directoryVBox = new VBox();
        for (String library: testLibrary) {
            ToggleButton libraryPathButton = new ToggleButton(library);
//            libraryPathButton.setBorder(new Border(new BorderStroke(Color.BLACK,
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            libraryPathButton.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            libraryPathButton.setPrefSize(200,25);
            directoryVBox.getChildren().add(libraryPathButton);
        }
        directoryPane.setContent(directoryVBox);
        return this.directoryPane;
    }

    public ArrayList<String> getTestLibrary() {
        return this.testLibrary;
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

}
