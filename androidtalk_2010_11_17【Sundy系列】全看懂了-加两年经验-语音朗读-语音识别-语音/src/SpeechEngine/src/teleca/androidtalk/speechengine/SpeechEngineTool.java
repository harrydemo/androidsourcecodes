/**
 * @description a static class to conert the command format .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine;


public class SpeechEngineTool {
	public static SpeechCommandResult analysisSystemCommand(String speakCommand)
	{
		SpeechCommandResult result = new SpeechCommandResult();
		
		speakCommand = speakCommand.toLowerCase().trim();
		// remove the '[' and ']'
		if (speakCommand.startsWith("[") && speakCommand.endsWith("]")) {
			speakCommand = speakCommand.substring(1, speakCommand.length() - 1).trim();
		}

		if(speakCommand.contains(OperationCommandConst.DIALING))
		{
			result.setOpType(OperationType.Dialing) ;
			result.setOpData(speakCommand.replaceAll(OperationCommandConst.DIALING, "").trim()) ;
		}else if(speakCommand.contains(OperationCommandConst.GOTO_WEB))
		{
			result.setOpType(OperationType.GotoWeb) ;
			result.setOpData(speakCommand.replaceAll(OperationCommandConst.GOTO_WEB, "").trim()) ;
		}else if(speakCommand.contains(OperationCommandConst.SEARCH_GOOGLE))
		{
			result.setOpType(OperationType.SearchGoogle) ;
			result.setOpData(speakCommand.replaceAll(OperationCommandConst.SEARCH_GOOGLE, "").trim()) ;
		}else if(speakCommand.contains(OperationCommandConst.START_APP))
		{
			result.setOpType(OperationType.StartApp) ;
			result.setOpData(speakCommand.replaceAll(OperationCommandConst.START_APP, "").trim()) ;
		}else
		{
			result.setOpType(OperationType.Unknow) ;
			result.setOpData(speakCommand) ;
		}
		return result ;
	}
}
