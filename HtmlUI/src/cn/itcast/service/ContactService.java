package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.domain.Contact;

public class ContactService {

	public List<Contact> getContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact(78, "�ŷ�", "1384949494"));
		contacts.add(new Contact(12, "�", "194505555"));
		contacts.add(new Contact(89, "��ޱ", "1785959595"));
		return contacts;
	}
}
