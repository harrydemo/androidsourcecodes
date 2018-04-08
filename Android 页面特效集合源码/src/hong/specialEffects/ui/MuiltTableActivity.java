package hong.specialEffects.ui;


import hong.specialEffects.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MuiltTableActivity extends TabActivity
{
    //����TabHost����
    TabHost mTabHost;
   
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_widget);
       
        //ȡ��TabHost����
        mTabHost = getTabHost();
       
       
        //�½�һ��newTabSpec(newTabSpec)
        //�������ǩ��ͼ��(setIndicator)
        //��������(setContent)
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
                .setIndicator("TAB 1",getResources().getDrawable(R.drawable.france))
                .setContent(R.id.textview1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
                .setIndicator("TAB 2",getResources().getDrawable(R.drawable.usa))
                .setContent(R.id.textview2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
                .setIndicator("TAB 3",getResources().getDrawable(R.drawable.ukraine))
                .setContent(R.id.textview3));
       
        //����TabHost�ı�����ɫ
        mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
        //����TabHost�ı���ͼƬ��Դ
        //mTabHost.setBackgroundResource(R.drawable.bg0);
       
        //���õ�ǰ��ʾ��һ����ǩ
        mTabHost.setCurrentTab(0);
       
        //��ǩ�л��¼�����setOnTabChangedListener
        mTabHost.setOnTabChangedListener(new OnTabChangeListener()
        {
            // TODO Auto-generated method stub
            @Override
            public void onTabChanged(String tabId)
            {
                    Dialog dialog = new AlertDialog.Builder(MuiltTableActivity.this)
                            .setTitle("��ʾ")
                            .setMessage("��ǰѡ�У�"+tabId+"��ǩ")
                            .setPositiveButton("ȷ��",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
								public void onClick(DialogInterface dialog, int whichButton)
                                {
                                    dialog.cancel();
                                }
                            }).create();//������ť
             
                    dialog.show();
            }           
        });
    }
}
