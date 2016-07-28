package sample;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alex on 7/18/2016.
 */
public class CommandParserTest {

    @Test
    public void testGetCommands() throws Exception {
        CommandParser commandParser = new CommandParser();
        commandParser.getCommands();
    }
}