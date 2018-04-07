package gl.test.util;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class CropImage {
	
	public static Intent getImageClipIntent(String path){
		System.out.println("当前的路径==========="+ path);
		
		File file =  new File(path);
		Uri uri = Uri.fromFile(file);
		
		//Uri uri = Uri.parse(path);
		

		System.out.println("图片路径Uri" + uri);
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		
		intent.setDataAndType(uri,"image/*");
		
		
		
		
		
		
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
		
	}

}
