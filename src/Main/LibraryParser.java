package Main;

import JTAF.Command;
import JTAF.Function;
import JTAF.Library;
import JTAF.Parameter;
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
public class LibraryParser {
    public String libraryName;
    private Library library;
    private ArrayList<Command> commands = new ArrayList<>();
    private ArrayList<Function> functions = new ArrayList<>();

    public LibraryParser(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setIgnoringComments(true);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        File file = new File(path);
        libraryName = file.getName();
        Document doc = docBuilder.parse(file);
//        doc.normalize();
        Node library = doc.getFirstChild();
        //checks if file contains library tag
        NodeList docNodeList = doc.getChildNodes();
        for (int i = 0; i < docNodeList.getLength(); i++) {
            if (docNodeList.item(i).getNodeType() == docNodeList.item(i).ELEMENT_NODE) {
                Element docChildNode = (Element) docNodeList.item(i);
                if (docChildNode.getTagName().equals("library")) {
                    library = docNodeList.item(i);
                } else {
                    System.out.println("Invalid file");
                }
            }
        }

        NodeList libraryNodeList = library.getChildNodes();
        for (int i = 0; i < libraryNodeList.getLength(); i++) {
            if(libraryNodeList.item(i).getNodeType() == libraryNodeList.item(i).ELEMENT_NODE) {
                Element libraryChild = (Element) libraryNodeList.item(i);
                if(libraryChild.getTagName().equals("command")) {
                    addCommand(libraryChild);
                }
                if(libraryChild.getTagName().equals("function")) {
                    addFunction(libraryChild);
                }
            }
        }

        setLibrary(libraryName, commands, functions);
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void addCommand(Element commandEle) {
        String commandName = commandEle.getAttribute("name");
        String commandClass = commandEle.getAttribute("class");
        Command command = new Command(commandName,commandClass);
        System.out.println("Parsing command: "+commandName);

        //find usage, required params, optional params, produces
        NodeList commandChildList = commandEle.getChildNodes();
        for (int j = 0; j < commandChildList.getLength(); j++) {
            if (commandChildList.item(j).getNodeType() == commandChildList.item(j).ELEMENT_NODE) {
                Element commandChild = (Element) commandChildList.item(j);
                if (commandChild.getTagName().equals("usage")) {
                    String commandResult = commandChild.getTextContent();
                    command.setCommandUsage(commandResult);
                }

                if (commandChild.getTagName().equals("requiredParameters") || commandChild.getTagName().equals("requiredParameter")) {
                    NodeList requiredParameterNodeList = commandChild.getChildNodes();
                    for(int k = 0; k < requiredParameterNodeList.getLength(); k++) {
                        if(requiredParameterNodeList.item(k).getNodeType() == requiredParameterNodeList.item(k).ELEMENT_NODE) {
                            Element requiredParameterChild = (Element) requiredParameterNodeList.item(k);
                            command.addRequiredParameters(createParameter(requiredParameterChild));
                        }
                    }
                }

                if (commandChild.getTagName().equals("optionalParameters") || commandChild.getTagName().equals("optionalParameter")) {
                    NodeList optionalParameterNodeList = commandChild.getChildNodes();
                    for(int k = 0; k < optionalParameterNodeList.getLength(); k++) {
                        if(optionalParameterNodeList.item(k).getNodeType() == optionalParameterNodeList.item(k).ELEMENT_NODE) {
                            Element optionalParameterChild = (Element) optionalParameterNodeList.item(k);
                            command.addOptionalParameters(createParameter(optionalParameterChild));
                        }
                    }
                }

                if (commandChild.getTagName().equals("produces")) {
                    NodeList resultNodeList = commandChild.getChildNodes();
                    for(int k = 0; k < resultNodeList.getLength(); k++) {
                        if(resultNodeList.item(k).getNodeType() == resultNodeList.item(k).ELEMENT_NODE) {
                            Element resultChild = (Element) resultNodeList.item(k);
                            command.addCommandResult(createParameter(resultChild));
                        }
                    }
                }
            }
        }

        commands.add(command);
    }

    public void addFunction(Element functionEle) {
        String functionName = functionEle.getAttribute("name");
        Function function = new Function(functionName);
        System.out.println("Parsing function: "+functionName);

        //find usage, required params, optional params, produces
        NodeList functionChildList = functionEle.getChildNodes();
        for (int j = 0; j < functionChildList.getLength(); j++) {
            if (functionChildList.item(j).getNodeType() == functionChildList.item(j).ELEMENT_NODE) {
                Element functionChild = (Element) functionChildList.item(j);

                if (functionChild.getTagName().equals("usage")) {
                    String functionResult = functionChild.getTextContent();
                    function.setFunctionUsage(functionResult);
                }
                //should use body's command's req params
                if (functionChild.getTagName().equals("requiredParameters") || functionChild.getTagName().equals("requiredParameter")) {
                    NodeList requiredParameterNodeList = functionChild.getChildNodes();
                    for(int k = 0; k < requiredParameterNodeList.getLength(); k++) {
                        if(requiredParameterNodeList.item(k).getNodeType() == requiredParameterNodeList.item(k).ELEMENT_NODE) {
                            Element requiredParameterChild = (Element) requiredParameterNodeList.item(k);
                            function.addRequiredParameters(createParameter(requiredParameterChild));
                        }
                    }
                }
                //should use body's command's opt params
                if (functionChild.getTagName().equals("optionalParameters") || functionChild.getTagName().equals("optionalParameter")) {
                    NodeList optionalParameterNodeList = functionChild.getChildNodes();
                    for(int k = 0; k < optionalParameterNodeList.getLength(); k++) {
                        if(optionalParameterNodeList.item(k).getNodeType() == optionalParameterNodeList.item(k).ELEMENT_NODE) {
                            Element optionalParameterChild = (Element) optionalParameterNodeList.item(k);
                            function.addOptionalParameters(createParameter(optionalParameterChild));
                        }
                    }
                }

                if (functionChild.getTagName().equals("produces")) {
                    NodeList resultNodeList = functionChild.getChildNodes();
                    for(int k = 0; k < resultNodeList.getLength(); k++) {
                        if(resultNodeList.item(k).getNodeType() == resultNodeList.item(k).ELEMENT_NODE) {
                            Element resultChild = (Element) resultNodeList.item(k);
                            function.addFunctionResult(createParameter(resultChild));
                        }
                    }
                }

                //body
                if (functionChild.getTagName().equals("body")) {
                    NodeList bodyNodeList = functionChild.getChildNodes();
                    for(int k = 0; k < bodyNodeList.getLength(); k++) {
                        if(bodyNodeList.item(k).getNodeType() == bodyNodeList.item(k).ELEMENT_NODE) {
                            Element bodyChild = (Element) bodyNodeList.item(k);
                            String commandName = bodyChild.getTagName();
                            function.addToFunctionBody(commandName);
                        }
                    }
                }

            }
        }

        functions.add(function);
    }

    public Parameter createParameter(Element child) {
        //removes subsequent children
        for (int i = 1; i < child.getChildNodes().getLength(); i++) {
            child.removeChild(child.getChildNodes().item(i));
        }
        String parameterName = child.getAttribute("name");
        String parameterTag = child.getTagName();
        String parameterText = child.getTextContent();

        Parameter parameter = new Parameter(parameterName,parameterTag,parameterText);
        return parameter;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public void setLibrary(String libraryName, ArrayList<Command> commands, ArrayList<Function> functions) {
        library = new Library(libraryName);
        library.setCommands(commands);
        library.setFunctions(functions);
    }

    public Library getLibrary() {
        return this.library;
    }

    //allCommands = whole test library
//    public void validateFunctions(ArrayList<Command> allCommands) {
//        ArrayList<Parameter> newRequiredParameters = new ArrayList<>();
//        ArrayList<Parameter> newOptionalParameters = new ArrayList<>();
//        ArrayList<String> allCommandNames = new ArrayList<>();
//
//        //gets every command name
//        for (Command command : allCommands) {
//            String commandName = command.getCommandName();
//            allCommandNames.add(commandName);
//        }
//
//        for (Function function : functions) {
//            ArrayList<String> functionBody = function.getFunctionBody();
//            //validate if body contains commands
//            for (String functionBodyCommandName : functionBody) {
//                if (!allCommandNames.contains(functionBodyCommandName))
//                    System.out.println("Does not contain "+functionBodyCommandName+"!");
//                else {
//                    //replace required+optional parameters
//                }
//            }
//        }
//    }

}
