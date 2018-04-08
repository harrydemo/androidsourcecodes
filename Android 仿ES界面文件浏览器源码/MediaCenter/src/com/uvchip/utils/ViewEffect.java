package com.uvchip.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.uvchip.files.FilePropertyAdapter;
import com.uvchip.mediacenter.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEffect {
	
	public static AlertDialog dialog;
	private static Toast mToast;

//	public static AlertDialog createCapacityDialog(final Context context,int titleId,CapacityAdapater adapter,OnCancelListener listener){
//		BrowserActivity mcontext = (BrowserActivity)context;
//		LayoutInflater flater = (LayoutInflater)mcontext.getLayoutInflater();
//		final View view = flater.inflate(R.layout.show_capacity_dialog, null);
//		ListView listView = (ListView)view.findViewById(R.id.lv_capacities);
//		listView.setAdapter(adapter);
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(titleId);
//		
//		builder.setView(view);
//		AlertDialog dialog = builder.create();
//		if(listener!=null){
//			dialog.setOnCancelListener(listener);
//		}
//		return dialog;
//	}
//	
	public static AlertDialog newFolderDialog(final Context context,int titleId,final CustomListener positiveListener,final CustomListener negativeListener){
		
		LayoutInflater flater = LayoutInflater.from(context);
		final View view = flater.inflate(R.layout.rename_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(titleId);
		builder.setView(view);
		if(positiveListener!=null){
			builder.setPositiveButton(context.getText(R.string.confirm), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					positiveListener.onListener();
				}
			});
		}
		if(negativeListener!=null){
			builder.setNegativeButton(context.getText(R.string.cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					negativeListener.onListener();
				}
			});
		}
		dialog = builder.create();
		return dialog;
	}
	
	public static AlertDialog createRenameDialog(Context context,int titleId,String originName,final CustomListener positiveListener,final CustomListener negativeListener){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.rename_dialog, null);
		((EditText)view.findViewById(R.id.rename)).setText(originName);
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(titleId);
		builder.setView(view);
		if(positiveListener!=null){
			builder.setPositiveButton(context.getText(R.string.btn_positive), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					positiveListener.onListener();
				}
			});
		}
		if(negativeListener!=null){
			builder.setNegativeButton(context.getText(R.string.btn_negative), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					negativeListener.onListener();
				}
			});
		}
		return builder.create();
	}
	
	public static AlertDialog createComfirDialog(Context context,int titleId,
			int msgId,final CustomListener positiveListener,final CustomListener negativeListener){
		AlertDialog dialog = new AlertDialog.Builder(context)
		.setTitle(titleId)
		.setMessage(msgId)
		.create();
		if(positiveListener != null){
			String btnText = context.getResources().getText(R.string.btn_positive).toString();
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, btnText, 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					positiveListener.onListener();
				}
			});
		}
		if(negativeListener != null){
			String btnText = context.getResources().getText(R.string.btn_negative).toString();
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, btnText, 
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					negativeListener.onListener();
				}
			});
		}
		return dialog;
	}
//
	public static AlertDialog createCustProgressDialog(Context context,int titleId,int max,
            final CustomListener positiveListener,final CustomListener negativeListener,OnCancelListener cancelListener){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cust_progress, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
        .setTitle(titleId)
        .setView(view)
        .create();
        if(positiveListener != null){
            String btnText = context.getResources().getText(R.string.btn_hide).toString();
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, btnText, 
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    positiveListener.onListener();
                }
            });
        }
        if(negativeListener != null){
            String btnText = context.getResources().getText(R.string.btn_negative).toString();
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, btnText, 
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    negativeListener.onListener();
                }
            });
        }
        if(cancelListener != null){
            dialog.setOnCancelListener(cancelListener);
        }
        TextView tv = (TextView)view.findViewById(R.id.tvNumber);
        tv.setText("0/" + max);
        return dialog;
    }
//
//	
	public static AlertDialog createTheDialog(Context context,int titleId,OnCancelListener listener,
	        OnCheckedChangeListener checkedChangeListener,
	        android.widget.CompoundButton.OnCheckedChangeListener checkBoxChangeListener){
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflater.inflate(R.layout.has_same_file_check, null);
	    AlertDialog dialog = new AlertDialog.Builder(context)
	    .setView(view)
        .setTitle(titleId)
        .setOnCancelListener(listener)
        .create();
	    RadioGroup rg = (RadioGroup)view.findViewById(R.id.whichOperation);
	    rg.setOnCheckedChangeListener(checkedChangeListener);
	    CheckBox cb = (CheckBox)view.findViewById(R.id.doitasSame);
	    cb.setOnCheckedChangeListener(checkBoxChangeListener);
	    return dialog;
	}
//	
	public static AlertDialog createPropertyDialog(Context context,int titleId,
	         final FilePropertyAdapter adapter){
	    
       LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.read_property, null);
       AlertDialog dialog = new AlertDialog.Builder(context)
       .setView(view)
       .setTitle(titleId)
       .create();
       TextView tvFileName = (TextView)view.findViewById(R.id.fileName);
       TextView tvFileType = (TextView)view.findViewById(R.id.fileType);
       TextView tvFilePath = (TextView)view.findViewById(R.id.filePath);
       final TextView tvInclude = (TextView)view.findViewById(R.id.include);
       final TextView tvFileSize = (TextView)view.findViewById(R.id.fileSize);
       TextView tvCreateDate = (TextView)view.findViewById(R.id.createDate);
       TextView tvModifyDate = (TextView)view.findViewById(R.id.modifyDate);
       TextView tvCanWrite = (TextView)view.findViewById(R.id.canWrite);
       TextView tvCanRead = (TextView)view.findViewById(R.id.canRead);
       TextView tvIsHide = (TextView)view.findViewById(R.id.isHide);
       tvFileName.setText(adapter.name);
       tvFileType.setText(adapter.type);
       tvFilePath.setText(adapter.preFolder);
       final Handler handler = new Handler();
       final Runnable r = new Runnable(){
           @Override
           public void run() {
               tvFileSize.setText(adapter.size);
               tvInclude.setText(adapter.includeStr);
           }
       };
       adapter.getSize(handler,r);
       tvCreateDate.setText(adapter.createDate);
       tvModifyDate.setText(adapter.modifyDate);
       tvCanWrite.setText(adapter.canWrite);
       tvCanRead.setText(adapter.canRead);
       tvIsHide.setText(adapter.isHiden);
       dialog.setOnCancelListener(new OnCancelListener(){
           @Override
           public void onCancel(DialogInterface dialog) {
               adapter.stopGetSize();
               handler.removeCallbacks(r);
           }
       });
       return dialog;
   }
//	
//	public static AlertDialog createAboutUsDialog(Context context,int titleId){
//		 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	        View view = inflater.inflate(R.layout.about_us, null);
//	        TextView tvVersion = (TextView)view.findViewById(R.id.tvAbout);
//	        PackageManager manager = context.getPackageManager();    
//	        try {
//	            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
//	            tvVersion.setText(packageInfo.versionName);
//	        } catch (NameNotFoundException e) {
//	            e.printStackTrace();
//	        }
//	        AlertDialog dialog = new AlertDialog.Builder(context)
//	        .setTitle(titleId)
//	        .setView(view)
//	        .create();
//	        
//	        return dialog;
//	}
//	
	public static void showToast(Context context,
			int toastId) {
		if(mToast!=null){
			mToast.cancel();
		}
		mToast = Toast.makeText(context, context.getText(toastId).toString(), Toast.LENGTH_SHORT);
		mToast.show();
	}
	public static void showToast(Context context,String msg){
		if(mToast!=null){
			mToast.cancel();
		}
		mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static void showToastLongTime(Context context,
			String formatStr) {
		if(mToast!=null){
			mToast.cancel();
		}
		mToast = Toast.makeText(context, formatStr, Toast.LENGTH_LONG);
		mToast.show();
	}
	
	public static void cancelToast(){
		if(mToast!=null){
			mToast.cancel();
		}
	}

	/*public static void cancelDialog(){
		if(dialog!=null){			
			dialog.cancel();
		}
	}*/
}
