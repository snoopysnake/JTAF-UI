package Tests;

import org.junit.Test;
import sample.LibraryParser;

import java.util.ArrayList;

/**
 * Created by Alex on 7/18/2016.
 */
public class ParserTest {

    @Test
    public void testTestLibraryParser() throws Exception {
        LibraryParser libraryParser = new LibraryParser("C:\\Users\\Michael\\Documents\\GitHub\\JTAF-XCore\\src\\test\\resources\\testlibrary\\context.test.commands.xml");
        System.out.println(libraryParser.getFunctions());
    }

//    @Test
//    public void testTestScriptParser() throws Exception {
//        TestScriptParser scriptParser = new TestScriptParser();
//        scriptParser.parseTestScript("A:\\My Documents\\Programming\\JTAF-XCore\\seedProject\\testscripts\\HelloWorldTest.xml");
//    }
}