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

/**
 * Created by Alex on 8/3/2016.
 */
public class TestScriptParser {
    private DocumentBuilder docBuilder;
    private NodeList testSuite;
    public TestScriptParser(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        File file = new File(path);
        Document doc = docBuilder.parse(file);
        doc.normalize();
        Node testScript = doc.getFirstChild();
        NodeList docNodeList = doc.getChildNodes();
        for (int i = 0; i < docNodeList.getLength(); i++) {
            if (docNodeList.item(i).getNodeType() == docNodeList.item(i).ELEMENT_NODE) {
                Element docChildNode = (Element) docNodeList.item(i);
                if (docChildNode.getTagName().equals("testsuite")) {
                    testScript = docNodeList.item(i);
                }
            }
        }
        testSuite = testScript.getChildNodes();
//        NodeList testList = testScript.getChildNodes();
//        for (int i = 0; i < testList.getLength(); i++) {
//            if(testList.item(i).getNodeType() == testList.item(i).ELEMENT_NODE) {
//                Element testEle = (Element) testList.item(i);
//                System.out.println(testEle.getAttribute("name"));
//                NodeList testChildList = testEle.getChildNodes();
//                for (int j = 0; j < testChildList.getLength(); j++) {
//                    if(testChildList.item(j).getNodeType() == testChildList.item(j).ELEMENT_NODE) {
//                        Element testChild = (Element) testChildList.item(j);
//                        if (testChild.getTagName().equals("desc")) {
//                            System.out.println("Description: "+ testChild.getTextContent());
//                        }
//                        if (testChild.getNodeName().equals("coverage")) {
//                            System.out.println("Jenkins Test Name: "+ testChild.getTextContent());
//                        }
//                        if (testChild.getNodeName().equals("automationValue")) {
//                            System.out.println("Priority: "+ testChild.getTextContent());
//                        }
//                        if (testChild.getNodeName().equals("teststeps")) {
//                            System.out.println("Test steps: ");
//                        }
//                    }
//                }
//            }
//        }
    }

    public void parseTestScript(String path) throws IOException, SAXException {
    }

}
