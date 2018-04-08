package org.metalslug;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.metalslugd.mission.stages.Stage_2;
import org.metalslugd.mission.stages.Stage_Logo;
import org.metalslugd.mission.stages.Stage_Menu;
import org.redengine.engine.frame.RActivity;
import org.redengine.engine.frame.REngineListener;
import org.redengine.engine.manager.RSceneManager;
import org.redengine.systems.graphsystem.RCamera;
import org.redengine.utils.ioutils.RFileIOUtils;

public class MetalSlugDActivity extends RActivity {

	@Override
	public void EngineSetting() {
		this.setEngineListener(new REngineListener(){
			public void engineInitialize() {
				
			}

			public void engineStart() {
				
			}

			public void enginePause() {
				
			}

			public void engineActive() {
				
			}

			public void engineEnd() {
				System.out.println("use end");
			}
		});
		
		this.setFullScreen();
		this.setLandScape();
	}

	@Override
	public void gameInitialize() {

		File f=new File(RFileIOUtils.getPrivateSystemDir()+"/gameinfo.dat");
		if(!f.exists()){
			OutputStream os=RFileIOUtils.writeFile("gameinfo.dat");
			try {
				ObjectOutputStream oos=new ObjectOutputStream(os);
				oos.writeInt(1);
				oos.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//TODO 创建场景
		Stage_2 stage1=new Stage_2(RCamera.getCamera());
		Stage_Logo logo=new Stage_Logo(RCamera.getCamera());
		Stage_Menu menu=new Stage_Menu(RCamera.getCamera());
		RCamera.getCamera().createdCamera(-10, -10, 860, 550, 0, 0);
		//State1_2 menu=new State1_2(RCamera.getCamera());//new StageMenu(RCamera.getCamera());
		
		//TODO 添加场景
		RSceneManager.getSceneManager().addScene("logo", logo);
		RSceneManager.getSceneManager().addScene("menu", menu);
		RSceneManager.getSceneManager().addScene("stage1", stage1);
		
		//TODO 设置首个场景
		this.setFirstSceneName("logo");
	}
   
}