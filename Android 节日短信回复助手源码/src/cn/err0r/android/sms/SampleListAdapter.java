package cn.err0r.android.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.err0r.android.sms.database.SMSSampleDao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SampleListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;    
    private List<Map<String, Object>> mData;   
    public static Map<Integer, Boolean> isSelected;
    Context context;
        
    SMSSampleDao smssampledao;
    public SampleListAdapter(Context context) {    
        mInflater = LayoutInflater.from(context);   
        this.context = context;	
        init(context);    
    }
    
	//��ʼ��    
    private void init(Context context) {    
        mData=new ArrayList<Map<String, Object>>(); 
        isSelected = new HashMap<Integer, Boolean>();
        
        smssampledao = new SMSSampleDao(context);
        Cursor cursor = smssampledao.select();
            
        int i=0;
        while (cursor.moveToNext()) {
        	Map<String, Object> map = new HashMap<String, Object>();    
        	map.put("sid", cursor.getInt(0));
            map.put("class", cursor.getString(1));  
            map.put("body", cursor.getString(2));
            mData.add(map);
            //����isSelected���map�Ǽ�¼���ٻظ���Ϣ��״̬
            if(cursor.getString(3).equals("1"))
            	isSelected.put(i, true); 
            else isSelected.put(i, false); 
			i++;
		}
//        Log.i("isselect",isSelected.toString());
        cursor.close();
        smssampledao.close();
    }  
    
    
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size(); 
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub		
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;    
		
		
        //convertViewΪnull��ʱ���ʼ��convertView��    
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.samplesmslist, null);    
            holder.sample_body = (TextView) convertView.findViewById(R.id.samplesms_tv1); 
            holder.sample_cBox = (CheckBox) convertView.findViewById(R.id.samplesms_cb);
            holder.sample_btn=(Button) convertView.findViewById(R.id.samplesms_btn1);
            convertView.setTag(holder);    
        } else {    
            holder = (ViewHolder) convertView.getTag();    
        }    
        String txtBody = mData.get(position).get("body").toString();
        holder.sample_body.setText(txtBody.length()>33?txtBody.substring(0, 32)+"...":txtBody);
        holder.sample_body.setTag(txtBody);
        holder.sample_cBox.setChecked(isSelected.get(position)); 
        holder.sample_cBox.setTag(mData.get(position).get("sid").toString());
        holder.sample_btn.setTag(mData.get(position).get("sid").toString());
        
        //����onclick����
        holder.sample_cBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int sid=Integer.valueOf(v.getTag().toString());
				smssampledao.updatafastreplyState(sid);				//�������ݿ�
//				Log.i("clicked", v.getTag().toString());
				isSelected.put(sid-1,!isSelected.get(sid-1));		//����isSelected��¼
//				Log.i("isselect",isSelected.toString());
			}
		});
        
        
        holder.sample_btn.setOnClickListener(new OnClickListener() {
        	int sid;
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sid=Integer.valueOf(v.getTag().toString());
				
				new AlertDialog.Builder(context)					//ɾ��ȷ�϶Ի���
		        .setTitle(context.getResources().getText(R.string.alert))
		        .setMessage(context.getResources().getText(R.string.delete_comfirm))
		        .setIcon(android.R.drawable.ic_dialog_info)
		        .setPositiveButton(context.getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	smssampledao.delete(sid);
		        	Toast.makeText(context, context.getResources().getText(R.string.delete_succeed), Toast.LENGTH_SHORT).show();
		        	SampleListAdapter.this.init(context);			//ɾ���ɹ����ʼ��map������listview
		        	SampleListAdapter.this.notifyDataSetChanged();
		        }
		        })
		        .setNegativeButton(context.getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		         //ȡ����ť�¼�
		        }
		        })
		        .show();
			}
		});
		
        return convertView;
	}
	
	
	
	public final class ViewHolder {    
        public TextView sample_body;
        public CheckBox sample_cBox;    
        public Button sample_btn;
    }

}
