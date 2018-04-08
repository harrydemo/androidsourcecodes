package dk.andsen.RecordEditor.types;

/**
 * Holds informations about a single field of a table
 * @author os
 *
 */
public class TableField {
	public static final int TYPE_STRING = 0; 
	public static final int TYPE_INTEGER = 1;
	public static final int TYPE_FLOAT = 2;
	public static final int TYPE_DATE = 3;
	public static final int TYPE_TIME = 4;
	public static final int TYPE_DATETIME = 5; 
	public static final int TYPE_BOOLEAN = 6;
	public static final int TYPE_PHONENO = 7;
  private String name;
  private int type;
  private String value;
  private Boolean notNull;
  private Boolean primaryKey;
  private String defaultValue;
  private String foreignKey;   // on the form tableName(fielInTable)
  private Boolean updateable = true;
  private String hint = null;
  private String displayName = null;;

  /**
   * If this return true the field is updateable in the update view 
   * @return
   */
  public Boolean isUpdateable() {
		return updateable;
	}
	/**
	 * Set this to false if you want to exclude the field from the update view
	 * Default value = true
	 * @param updateable
	 */
	public void setUpdateable(Boolean updateable) {
		this.updateable = updateable;
	}
	
	/**
	 * A hint / description of the field shown Edit fields
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}
	
	/**
	 * Add a Hint / description to a field. Un edit it is used as a hint
	 * @param hint
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	/**
	 * Get the display name of a field. If not set by setDisplayName field name
	 * will be returned
	 * @return
	 */
	public String getDisplayName() {
  	if (displayName == null)
  		return name;
  	else
  		return displayName;
	}
	
	/**
	 * Use this to change the display name in the update view
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Retrieves the name of the field
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the field
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the data type
	 * @return
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Set data type, possible data types: TYPE_STRING, TYPE_INTEGER, TYPE_FLOAT,
	 * TYPE_DATE, TYPE_TIME, TYPE_DATETIME, TYPE_BOOLEAN 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * GEt the value 
	 * @return a String with the value
	 */
	public String getValue() {
		if (value == null)
			return "";
		else
			return value;
	}
	
	/**
	 * Set the value (as a String)
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Return true if the field is not null
	 * @return
	 */
	public Boolean getNotNull() {
		return notNull;
	}
	
	/**
	 * Set the field as not null
	 * @param notNull
	 */
	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}
	
	/**
	 * Return true if the field is the (part of) the primary key
	 * @return
	 */
	public Boolean getPrimaryKey() {
		return primaryKey;
	}
	
	/**
	 * Set the field as the (part of) the primary key
	 * @param primaryKey
	 */
	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	/**
	 * Get the fields default value
	 * @return
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Set the fields default value
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Return a String that defines the fields foreign key
	 * @return
	 */
	public String getForeignKey() {
		return foreignKey;
	}
	
	/**
	 * Define the foreign key for the field
	 * @param foreignKey
	 */
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
}
