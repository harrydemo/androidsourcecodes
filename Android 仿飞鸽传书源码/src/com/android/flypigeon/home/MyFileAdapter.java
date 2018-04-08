package com.android.flypigeon.home;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.flypigeon.R;
import com.android.flypigeon.util.Constant;
import com.android.flypigeon.util.FileName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
public class MyFileAdapter extends BaseAdapter
{
  private LayoutInflater mInflater;
  private List<FileName> filePaths;
  public Context context = null;
  
  private static Map<String,Integer> exts = new HashMap<String,Integer>();
  static{
	  exts.put("doc", R.drawable.doc);exts.put("docx", R.drawable.doc);exts.put("xls", R.drawable.xls);exts.put("xlsx", R.drawable.xls);exts.put("ppt", R.drawable.ppt);exts.put("pptx", R.drawable.ppt);
	  exts.put("jpg", R.drawable.image);exts.put("jpeg", R.drawable.image);exts.put("gif", R.drawable.image);exts.put("png", R.drawable.image);exts.put("ico", R.drawable.image);
	  exts.put("apk", R.drawable.apk);exts.put("jar", R.drawable.jar);exts.put("rar", R.drawable.rar);exts.put("zip", R.drawable.rar);
	  exts.put("mp3", R.drawable.music);exts.put("wma", R.drawable.music);exts.put("aac", R.drawable.music);exts.put("ac3", R.drawable.music);exts.put("ogg", R.drawable.music);exts.put("flac", R.drawable.music);exts.put("midi", R.drawable.music);
	  exts.put("pcm", R.drawable.music);exts.put("wav", R.drawable.music);exts.put("amr", R.drawable.music);exts.put("m4a", R.drawable.music);exts.put("ape", R.drawable.music);exts.put("mid", R.drawable.music);exts.put("mka", R.drawable.music);
	  exts.put("svx", R.drawable.music);exts.put("snd", R.drawable.music);exts.put("vqf", R.drawable.music);exts.put("aif", R.drawable.music);exts.put("voc", R.drawable.music);exts.put("cda", R.drawable.music);exts.put("mpc", R.drawable.music);
	  exts.put("mpeg", R.drawable.video);exts.put("mpg", R.drawable.video);exts.put("dat", R.drawable.video);exts.put("ra", R.drawable.video);exts.put("rm", R.drawable.video);exts.put("rmvb", R.drawable.video);exts.put("mp4", R.drawable.video);
	  exts.put("flv", R.drawable.video);exts.put("mov", R.drawable.video);exts.put("qt", R.drawable.video);exts.put("asf", R.drawable.video);exts.put("wmv", R.drawable.video);exts.put("avi", R.drawable.video);
	  exts.put("3gp", R.drawable.video);exts.put("mkv", R.drawable.video);exts.put("f4v", R.drawable.video);exts.put("m4v", R.drawable.video);exts.put("m4p", R.drawable.video);exts.put("m2v", R.drawable.video);exts.put("dat", R.drawable.video);
	  exts.put("xvid", R.drawable.video);exts.put("divx", R.drawable.video);exts.put("vob", R.drawable.video);exts.put("mpv", R.drawable.video);exts.put("mpeg4", R.drawable.video);exts.put("mpe", R.drawable.video);exts.put("mlv", R.drawable.video);
	  exts.put("ogm", R.drawable.video);exts.put("m2ts", R.drawable.video);exts.put("mts", R.drawable.video);exts.put("ask", R.drawable.video);exts.put("trp", R.drawable.video);exts.put("tp", R.drawable.video);exts.put("ts", R.drawable.video);
  }

  public void setDatasource(List<FileName> filePaths){
	  this.filePaths = filePaths;
  }

  public MyFileAdapter(Context context,List<FileName> filePaths)
  {
    mInflater = LayoutInflater.from(context);
    this.filePaths = filePaths;
    this.context = context;
  }
  
  @Override
  public int getCount()
  {
    return filePaths.size();
  }
  @Override
  public Object getItem(int position)
  {
    return filePaths.get(position);
  }
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  @Override
  public View getView(int position,View convertView,ViewGroup parent)
  {
	  View fileView = mInflater.inflate(R.layout.row_file_layout, null);
	  fileView.setTag(Integer.valueOf(position));
	  ImageView fileIcon = (ImageView)fileView.findViewById(R.id.file_icon);
	  TextView fileName = (TextView)fileView.findViewById(R.id.file_name);
	  TextView fileSize = (TextView)fileView.findViewById(R.id.file_size);
	  CheckBox fileSelected = (CheckBox)fileView.findViewById(R.id.file_selected);
	  
	  FileName file = filePaths.get(position);
	  String fName = file.fileName;
	  fileName.setText(file.getFileName());
	  if(file.isDirectory)
      {
		  fileIcon.setImageResource(R.drawable.folder_icon);
		  ((TableRow)((TableLayout)fileView).getChildAt(0)).removeViewAt(3);//如果是文件夹则移除最后面的那个文件选择框
      }
      else
      {//根据文件的扩展名设置文件的图标
    	  String ext = fName.substring(fName.lastIndexOf(".")+1);
    	  if(exts.containsKey(ext)){
    		  fileIcon.setImageResource(exts.get(ext));
    	  }else{
    		  fileIcon.setImageResource(R.drawable.gdoc);
    	  }
    	  fileSize.setText(Constant.formatFileSize(file.fileSize));
    	  fileSelected.setTag(position);//设置CheckBox在列表中的序号（也是文件列表项序号），以便根据该序号获得该对象并对其进行相关操作
    	  Boolean isFileSelected = Constant.fileSelectedState.get(Integer.valueOf(position));
    	  if(null!=isFileSelected)fileSelected.setChecked(isFileSelected);
      }
	
    return fileView;
  }
  
}