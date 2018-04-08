/**
 * @Andrew OperationFactory
 * @description A simple factory patterns of operation classed .
 * @version 1.0
 * @author Andrew
 * @date 2010-11-5
 */
package teleca.androidtalk.operation;
public class OperationFactory {
	public static IOperation createOperation(OperationType opType) {
		switch (opType) {
		case Dialing:
			return new OperationDialing();
		case StartApp:
			return new OperationStartApp();
		case GotoWeb:
			return new OperationConnect();
		case SearchGoogle:
			return new OperationSearch();
		default:
			break;
		}
		return null;
	}
}
