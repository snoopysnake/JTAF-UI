package Tests;

import org.junit.Test;
import Main.FindExistingProject;

/**
 * Created by Alex on 7/23/2016.
 */
public class FindExistingProjectTest {

    @Test
    public void testSearch() throws Exception {
        FindExistingProject.search("A:\\My Documents\\Programming\\JTAF-UI");
        System.out.println(FindExistingProject.search("A:\\My Documents\\Programming\\JTAF-XCore\\seedProject"));
    }
}