package com.renren.api.connect.android.example.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author 李勇 2010-9-3
 */

public class FriendHandler extends RestHandler {

    private List<Map<String, String>> friends = new ArrayList<Map<String, String>>();

    private Map<String, String> friend;

    private StringBuilder currentTagValue = new StringBuilder();

    @Override
    public Object getResult() {
        return this.friends;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes)
            throws SAXException {
        if ("friend".equalsIgnoreCase(localName)) {
            this.friend = new HashMap<String, String>();
        }
        this.currentTagValue.delete(0, this.currentTagValue.length());
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.currentTagValue.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        String value = this.currentTagValue.toString().trim();
        if ("friend".equalsIgnoreCase(localName)) {
            this.friends.add(friend);
        } else if ("tinyurl".equalsIgnoreCase(localName)) {
            this.friend.put("headurl", value);
        } else if ("name".equalsIgnoreCase(localName)) {
            this.friend.put("name", value);
        }
    }
}
