/**
 * @description Analysis result of command format .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine;

/**
 * @author Administrator
 *
 */
public class SpeechCommandResult {
	private OperationType opType  ;
	private String opData  ;
	//get operation type
	public OperationType getOpType() {
		return opType;
	}
	
	//Set operation type
	public void setOpType(OperationType opType) {
		this.opType = opType;
	}
	//get operation data 
	public String getOpData() {
		return opData;
	}
	//set operation data .
	public void setOpData(String opData) {
		this.opData = opData;
	}
	
	

}
