package sample;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 7/18/2016.
 */
public class CommandParser {
    SAXParser parser;

    public CommandParser() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            parser = parserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void getCommands() throws IOException, SAXException {
        String path = "A:\\My Documents\\Programming\\JTAF-XCore\\seedProject\\testscripts\\HelloWorldTest.xml";
        File file = new File(path);
        parser.parse(file, new DefaultHandler());
    }
}
