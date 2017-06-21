package sample;

import org.junit.Test;

/**
 * Created by Alex on 7/18/2016.
 */
public class ParserTest {

    @Test
    public void testTestLibraryParser() throws Exception {
        TestLibraryParser libraryParser = new TestLibraryParser("A:\\My Documents\\Programming\\JTAF-XCore\\seedProject\\src\\main\\resources\\testlibrary\\helloworld.commands.xml");
        libraryParser.getCommands();
    }

//    @Test
//    public void testTestScriptParser() throws Exception {
//        TestScriptParser scriptParser = new TestScriptParser();
//        scriptParser.parseTestScript("A:\\My Documents\\Programming\\JTAF-XCore\\seedProject\\testscripts\\HelloWorldTest.xml");
//    }
}