package Main;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Created by Alex on 7/23/2016.
 */
public class FindExistingProject {
    public FindExistingProject() {

    }

    public static boolean search(String directory) {
//        Iterator iter =  FileUtils.iterateFiles(directory,
//                new String[]{"txt", "java"}, true);
//
//        while(iter.hasNext()) {
//            File file = (File) iter.next();
//            searchInFile(file);
//        }
        ArrayList<Path> projectDirectory = new ArrayList<>();
        Path dir = Paths.get(directory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path: stream) {
                if(Files.isDirectory(path))
                projectDirectory.add(path.getFileName());
            }
            System.out.println("Searched project dir: " + projectDirectory);
            if (projectDirectory.containsAll(getRequiredProjectDirectories())) {
                return true;
            }
        } catch (IOException ex) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setContentText(ex.toString());
            alert.showAndWait();
        }
        return false;
    }

    private static ArrayList<Path> getRequiredProjectDirectories() {
        ArrayList<Path> requiredProjectDirs = new ArrayList<>();
//        requiredProjectDirs.add(Paths.get("logs"));
        requiredProjectDirs.add(Paths.get("profiles"));
        requiredProjectDirs.add(Paths.get("src"));
        requiredProjectDirs.add(Paths.get("target"));
        requiredProjectDirs.add(Paths.get("testscripts"));
        return requiredProjectDirs;
    }
}
