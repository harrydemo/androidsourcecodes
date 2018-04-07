/**
 * @description analysis system commands to special format .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine;

/**
 * @description analysis system commands to special format .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
public interface ISystemCommandsAnalysis {
	//convert a string format to command result format .
	SpeechCommandResult analysisSystemCommand(String speakCommand)  ;
}
