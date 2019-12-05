package org.goblinframework.core.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract public class XmlUtils {

  public static Document parseDocumentQuietly(InputStream inStream) {
    Objects.requireNonNull(inStream, "InputStream is required");
    try {
      return parseDocument(inStream);
    } catch (Exception ex) {
      if (ex instanceof RuntimeException) {
        throw (RuntimeException) ex;
      }
      throw new RuntimeException(ex);
    }
  }

  public static Document parseDocument(InputStream inStream) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    DocumentBuilder documentBuilder = factory.newDocumentBuilder();
    return documentBuilder.parse(inStream);
  }

  public static Element getSingleChildElement(Element parent, String name) {
    NodeList nodeList = parent.getElementsByTagName(name);
    return nodeList.getLength() == 0 ? null : (Element) nodeList.item(0);
  }

  public static List<Element> getChildElements(Element parent, String name) {
    NodeList nodeList = parent.getElementsByTagName(name);
    List<Element> elements = new ArrayList<>(nodeList.getLength());
    for (int i = 0; i < nodeList.getLength(); i++) {
      elements.add((Element) nodeList.item(i));
    }
    return elements;
  }

  public static String getChildElementText(Element parent, String name) {
    Element child = getSingleChildElement(parent, name);
    if (child == null) {
      return null;
    }
    String textContent = child.getTextContent();
    return textContent == null ? null : textContent.trim();
  }
}
