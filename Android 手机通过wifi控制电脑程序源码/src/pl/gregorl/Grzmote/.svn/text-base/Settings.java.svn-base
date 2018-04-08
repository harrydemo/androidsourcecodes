package pl.gregorl.Grzmote;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	public Settings(SharedPreferences preferences){
		sharedPreferences = preferences;
	}
	
	private static SharedPreferences sharedPreferences;
	private static String serverAddress="";
	public static final String settingsPath = ".grzmote_settings";
	
	public static String getServerAddress() {
		return serverAddress;
	}

	public static void setServerAddress(String serverAddr) {
		serverAddress = serverAddr;
	}

	public static void Save(){
		
		Editor ed = sharedPreferences.edit();
		ed.putString("ServerAddress", serverAddress);
		ed.commit();
//		try {
//			PrintWriter pw = new PrintWriter(new File(this.settingsPath));
//			pw.write(this.serverAddress);
//			pw.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void Load(){
		
		Map<String,?> prefs = sharedPreferences.getAll();
		if(prefs.containsKey("ServerAddress")){
			serverAddress = (String)prefs.get("ServerAddress");
		}
		
//		try {
//			BufferedReader br = new BufferedReader( new FileReader(new File(this.settingsPath)));
//			this.serverAddress = br.readLine();
//			br.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}
	
}
