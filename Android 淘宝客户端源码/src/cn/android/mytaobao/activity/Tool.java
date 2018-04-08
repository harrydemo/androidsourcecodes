package cn.android.mytaobao.activity;

import android.content.Context;
import android.widget.Toast;

public class Tool {
   public static void ShowMessage(Context context,String msg){
	   Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
   }
}
