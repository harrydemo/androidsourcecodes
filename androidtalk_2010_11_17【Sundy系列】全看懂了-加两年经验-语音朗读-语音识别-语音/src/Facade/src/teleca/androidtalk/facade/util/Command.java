package teleca.androidtalk.facade.util;

import java.io.Serializable;

/**
 * @author Jacky Zhang
 * @version 0.1
 * It's a  class used for save defined command
 */
public class Command implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
	 * command id which auto increase
	 */
	private String id;
	/*
	 * command name
	 */
	private String cmdName;
	/*
	 * command category
	 */
	private String cmdCategory;
	/*
	 * the special case,for example telephone number
	 */
	private String relation;

	/*
	 * default construct
	 */
	public Command() {
	}
	
	public Command(String id, String cmdName, String cmdCategory, String relation) {
		this.id = id;
		this.cmdName = cmdName;
		this.cmdCategory = cmdCategory;
		this.relation = relation;
	}

	/*
	 * get command id
	 */
	public String getId() {
		return id;
	}
	
	/*
	 * set command id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * get command name
	 */
	public String getCmdName() {
		return cmdName;
	}

	/*
	 * set command name
	 */
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	/*
	 * get command category
	 */
	public String getCmdCategory() {
		return cmdCategory;
	}

	/*
	 * set command category
	 */
	public void setCmdCategory(String cmdCategory) {
		this.cmdCategory = cmdCategory;
	}

	/*
	 * get the special operation
	 */
	public String getRelation() {
		return relation;
	}

	/*
	 * set the special operation
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Override
	public String toString() {
		return id+" "+cmdName + " " + cmdCategory + " " + relation;
	}
}
