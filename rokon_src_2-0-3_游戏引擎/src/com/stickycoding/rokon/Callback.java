package com.stickycoding.rokon;

/**
 * Callback.java
 * Provies a reference for a Callback for invoking Methods
 * 
 * @author Richard
 */
public class Callback {
	
	protected String methodName;
	protected Class<?>[] parameterTypes;
	protected Object[] parameters;
	
	/**
	 * Creates a Callback, defining a method by it's name only.
	 * When used, this will assume there is only 1 method with
	 * the given methodName, otherwise it will simply choose
	 * the first occurance.
	 * 
	 * @param methodName valid method name
	 */
	public Callback(String methodName) {
		this.methodName = methodName;
	}
	
	/**
	 * Creates a Callback, defining a method by it's name,
	 * and one parameter. This assumes there is only one
	 * method with this name and one parameter - parameter
	 * types are not checked for validity.
	 * 
	 * @param methodName valid method name
	 * @param parameter a parameter to be passed on
	 */
	public Callback(String methodName, Object parameter) {
		this.methodName = methodName;
		this.parameters = new Object[] { parameter };
	}
	
	/**
	 * Creates a Callback, defining a method by it's name,
	 * and an array of parameters. This does not check
	 * the parameter types for validity.
	 * 
	 * @param methodName valid method name
	 * @param parameters an array of parameters to be passed on
	 */
	public Callback(String methodName, Object[] parameters) {
		this.methodName = methodName;
		this.parameters = parameters;
	}
	
	/**
	 * Creates a Callback, defining a method by it's name,
	 * a given parameter type and a given parameter object.
	 * This is guaranteed to match only 1 possible method.
	 * 
	 * @param methodName valid method name
	 * @param parameterType a Class which represents the parameter type
	 * @param parameter a parameter to be passed on
	 */
	public Callback(String methodName, Class<?> parameterType, Object parameter) {
		this.methodName = methodName;
		this.parameterTypes = new Class<?>[] { parameterType };
		this.parameters = new Object[] { parameter };
	}
	
	/**
	 * Creates a Callback, defining a method by it's name,
	 * an array of parameter types and objects. This is
	 * guaranteed to match only 1 possible method.
	 * 
	 * @param methodName valid method name
	 * @param parameterTypes an array of Classes which represent the parameter types
	 * @param parameters an array of parameters to be passed on
	 */
	public Callback(String methodName, Class<?>[] parameterTypes, Object[] parameters) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
	}
	
	/**
	 * Returns the method name assigned to this Callback
	 * 
	 * @return NULL if not set
	 */
	public String getMethodName() {
		return methodName;
	}
	
	/**
	 * Returns the array of parameter types for this Callback
	 * 
	 * @return a Class array, NULL if not set
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;		
	}
	
	/**
	 * Returns the array of parameters for this Callback
	 * 
	 * @return an Object array, NULL if not set
	 */
	public Object[] getParameters() {
		return parameters;
	}

}
