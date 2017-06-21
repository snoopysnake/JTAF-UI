package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 7/18/2016.
 */
public class TestLibraryParser {
    private DocumentBuilder docBuilder;
    private NodeList commandList;

    public TestLibraryParser(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();

        File file = new File(path);
        Document doc = docBuilder.parse(file);
        doc.normalize();
        Node testLibrary = doc.getFirstChild();
        NodeList docNodeList = doc.getChildNodes();
        for (int i = 0; i < docNodeList.getLength(); i++) {
            if (docNodeList.item(i).getNodeType() == docNodeList.item(i).ELEMENT_NODE) {
                Element docChildNode = (Element) docNodeList.item(i);
                if (docChildNode.getTagName().equals("library")) {
                    testLibrary = docNodeList.item(i);
                }
            }
        }
        commandList = testLibrary.getChildNodes();
//        for (int i = 0; i < commandList.getLength(); i++) {
//            if(commandList.item(i).getNodeType() == commandList.item(i).ELEMENT_NODE) {
//                Element commandEle = (Element) commandList.item(i);
//                System.out.println(commandEle.getAttribute("name"));
//                names.add(commandEle.getAttribute("name"));
//                System.out.println(commandEle.getAttribute("class"));
//                NodeList commandChildList = commandEle.getChildNodes();
//                for (int j = 0; j < commandChildList.getLength(); j++) {
//                    if(commandChildList.item(j).getNodeType() == commandChildList.item(j).ELEMENT_NODE) {
//                        Element commandChild = (Element) commandChildList.item(j);
//                        if (commandChild.getTagName().equals("usage")) {
//                            System.out.println("Description: "+ commandChild.getTextContent());
//                        }
//                        if (commandChild.getNodeName().equals("requiredParameters")) {
//                            getRequiredParams(commandChild);
//                        }
//                        if (commandChild.getNodeName().equals("optionalParameters")) {
//                            getOptionalParams(commandChild);
//                        }
//                    }
//                }
//            }
//        }
    }

    public NodeList getCommands() throws IOException, SAXException {
        return commandList;
    }

    public int getCommandsSize() {
        return commandList.getLength();
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < commandList.getLength(); i++) {
            if(commandList.item(i).getNodeType() == commandList.item(i).ELEMENT_NODE) {
                Element commandEle = (Element) commandList.item(i);
                names.add(commandEle.getAttribute("name"));
            }
        }
        return names;
    }

    private void getRequiredParams(Element ele) {
        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
            if(ele.getChildNodes().item(i).getNodeType() == ele.getChildNodes().item(i).ELEMENT_NODE) {
                Element requiredParam = (Element) ele.getChildNodes().item(i);
                System.out.println(requiredParam.getNodeName());
                System.out.println(requiredParam.getAttribute("name"));
                System.out.println(requiredParam.getTextContent());
            }
        }
    }

    private void getOptionalParams(Element ele) {
        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
            if(ele.getChildNodes().item(i).getNodeType() == ele.getChildNodes().item(i).ELEMENT_NODE) {
                Element requiredParam = (Element) ele.getChildNodes().item(i);
                System.out.println(requiredParam.getNodeName());
                System.out.println(requiredParam.getAttribute("name"));
                System.out.println(requiredParam.getTextContent());
            }
        }

    }
}
