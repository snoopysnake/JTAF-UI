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
import java.util.HashMap;

/**
 * Created by Alex on 7/18/2016.
 */
public class TestLibraryParser {
    private DocumentBuilder docBuilder;
    private NodeList commandList;
    private ArrayList<String> names = new ArrayList<>();
    private HashMap<String,HashMap<String,String>> commandChildren = new HashMap<>();
    private HashMap<String,String> reqParamChildren = new HashMap<>();
    private HashMap<String,String> optParamChildren = new HashMap<>();

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
        for (int i = 0; i < commandList.getLength(); i++) {
            if(commandList.item(i).getNodeType() == commandList.item(i).ELEMENT_NODE) {
                Element commandEle = (Element) commandList.item(i);
                if(commandEle.getTagName().equals("command") || commandEle.getTagName().equals("function")) {
                    String commandName = commandEle.getAttribute("name");
                    String commandClass = commandEle.getAttribute("class");
                    String commandTag = commandEle.getTagName();
                    names.add(commandName);
                    HashMap<String, String> commandMap = new HashMap<>();
                    commandMap.put("tag", commandTag);
                    commandChildren.put(commandName, commandMap);
                    if (!commandClass.equals("")) {
                        commandMap.put("class", commandClass);
                        commandChildren.put(commandName, commandMap);
                    }

                    NodeList commandChildList = commandEle.getChildNodes();
                    for (int j = 0; j < commandChildList.getLength(); j++) {
                        if (commandChildList.item(j).getNodeType() == commandChildList.item(j).ELEMENT_NODE) {
                            Element commandChild = (Element) commandChildList.item(j);
                            if (commandChild.getTagName().equals("usage")) {
//                            System.out.println("Description: "+ commandChild.getTextContent());
                                commandMap.put("usage", commandChild.getTextContent());
                                commandChildren.put(commandName,commandMap);
                            }
                            if (commandChild.getTagName().equals("requiredParameters") || commandChild.getTagName().equals("requiredParameter")) {
                                commandMap.put(commandChild.getTagName(), commandTag);
                                commandChildren.put(commandName, commandMap);
                                for(int k = 0; k < commandChild.getChildNodes().getLength(); k++) {
                                    if(commandChild.getChildNodes().item(k).getNodeType() == commandChild.getChildNodes().item(k).ELEMENT_NODE) {
                                        Element requiredParam = (Element) commandChild.getChildNodes().item(k);
                                        String paramName = requiredParam.getAttribute("name");
                                        String paramTag = requiredParam.getTagName();
                                        String paramText = requiredParam.getTextContent();
                                        String paramInfo = "name: "+paramName+"\n" + "type: " +paramTag + "\n" + "description: " +paramText;
                                        reqParamChildren.put(commandName, paramInfo);
                                    }
                                }
                            }
                            if (commandChild.getTagName().equals("optionalParameters") || commandChild.getTagName().equals("optionalParameter")) {
                                commandMap.put(commandChild.getTagName(), commandTag);
                                commandChildren.put(commandName, commandMap);
                                for(int k = 0; k < commandChild.getChildNodes().getLength(); k++) {
                                    if(commandChild.getChildNodes().item(k).getNodeType() == commandChild.getChildNodes().item(k).ELEMENT_NODE) {
                                        Element requiredParam = (Element) commandChild.getChildNodes().item(k);
                                        String paramName = requiredParam.getAttribute("name");
                                        String paramTag = requiredParam.getTagName();
                                        String paramText = requiredParam.getTextContent();
                                        String paramInfo = "name: "+paramName+"\n" + "type: " +paramTag + "\n" + "description: " +paramText;
                                        optParamChildren.put(commandName, paramInfo);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public NodeList getCommands() throws IOException, SAXException {
        return commandList;
    }

    public HashMap<String, String> getRequiredParameters() {
        return reqParamChildren;
    }

    public HashMap<String, String> getOptionalParameters() {
        return optParamChildren;
    }

    public HashMap<String,HashMap<String,String>> getCommandChildren() {
        return commandChildren;
    }

    public int getCommandsSize() {
        return commandList.getLength();
    }

    public ArrayList<String> getNames() {
        for (int i = 0; i < commandList.getLength(); i++) {
            if(commandList.item(i).getNodeType() == commandList.item(i).ELEMENT_NODE) {
                Element commandEle = (Element) commandList.item(i);
                names.add(commandEle.getAttribute("name"));
            }
        }
        return names;
    }

//    private void getRequiredParams(String commandName, Element ele) {
//        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
//            if(ele.getChildNodes().item(i).getNodeType() == ele.getChildNodes().item(i).ELEMENT_NODE) {
//                Element requiredParam = (Element) ele.getChildNodes().item(i);
////                System.out.println(requiredParam.getNodeName());
////                System.out.println(requiredParam.getAttribute("name"));
////                System.out.println(requiredParam.getTextContent());
//            }
//        }
//    }

//    private void getOptionalParams(Element ele) {
//        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
//            if(ele.getChildNodes().item(i).getNodeType() == ele.getChildNodes().item(i).ELEMENT_NODE) {
//                Element requiredParam = (Element) ele.getChildNodes().item(i);
////                System.out.println(requiredParam.getNodeName());
////                System.out.println(requiredParam.getAttribute("name"));
////                System.out.println(requiredParam.getTextContent());
//            }
//        }
//
//    }
}
