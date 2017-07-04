package Tests;

import JTAF.Command;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import Main.LibraryParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 7/18/2016.
 */
public class ParserTest {
    LibraryParser libraryParser;

    @Before
    public void setUp() throws Exception {
        libraryParser = new LibraryParser("src\\TestData\\TestLibrary1.xml");
    }

    @Test
    public void testCommandNames() throws Exception {
        ArrayList<Command> actualCommands = libraryParser.getCommands();
        for (int i = 0; i < actualCommands.size(); i++) {
            String expectedCommandName = "Command"+(i+1);
            String actualCommandName = actualCommands.get(i).getCommandName();
            System.out.println("Expected: "+expectedCommandName+" Actual: "+actualCommandName);
            Assert.assertEquals(expectedCommandName, actualCommandName);
        }
    }

    @Test
    public void testCommandClasses() throws Exception {
        ArrayList<Command> actualCommands = libraryParser.getCommands();
        for (int i = 0; i < actualCommands.size(); i++) {
            String expectedCommandClass = "command"+(i+1)+".java";
            String actualCommandClass = actualCommands.get(i).getCommandClass();
            System.out.println("Expected: "+expectedCommandClass+" Actual: "+actualCommandClass);
            Assert.assertEquals(expectedCommandClass, actualCommandClass);
        }
    }

    @Test
    public void testCommandRequiredParameters() throws Exception {
        //zzzzz
    }
}