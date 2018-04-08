/**
 * name IOperation 
 * description an interface of operation definition .
 * version 1.0
 * author sundy
 * date 2010-11-5
 */
package teleca.androidtalk.operation;

import android.content.Context;

/**
 * @author sundy
 *
 */
public interface IOperation {
	/**
	 * operate the starting application , dialing ... when we finish the speech recognition .
	 * @param curContext current context the parameter transport 
	 * @param opData data parameter being transported .
	 * @return
	 */
	public OpResult doOperation(Context curContext ,String opData)  ;
}
