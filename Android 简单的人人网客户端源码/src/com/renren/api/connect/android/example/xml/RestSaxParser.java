package com.renren.api.connect.android.example.xml;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class RestSaxParser {

    public static Object parse(String xml, RestHandler handler) {
        Object obj = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            InputSource is = new InputSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            reader.setContentHandler(handler);
            reader.parse(is);
            obj = handler.getResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return obj;
    }
}
