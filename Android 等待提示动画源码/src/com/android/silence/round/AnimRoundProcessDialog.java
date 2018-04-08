package com.android.silence.round;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ���ַ�ʽ�Զ���Բ�ν�����
 * 
 * @author silence
 * @version [�汾��, 2011-5-27]
 * @see [�����/����]
 * @since [��Ʒ/ģ��汾]
 */
public class AnimRoundProcessDialog extends Activity implements OnClickListener
{
    private Button anim;

    private Button color;

    private Button icon;

    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initPage();
        setClickListener();
    }

    private void initPage()
    {
        anim = (Button) findViewById(R.id.anim);
        color = (Button) findViewById(R.id.color);
        icon = (Button) findViewById(R.id.icon);
    }

    private void setClickListener()
    {
        anim.setOnClickListener(this);
        color.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.anim:
                showRoundProcessDialog(this, R.layout.loading_process_dialog_anim);
                break;
            case R.id.color:
                showRoundProcessDialog(this, R.layout.loading_process_dialog_color);
                break;
            case R.id.icon:
                showRoundProcessDialog(this, R.layout.loading_process_dialog_icon);
                break;
            default:
                break;
        }
    }

    public void showRoundProcessDialog(Context mContext, int layout)
    {
        OnKeyListener keyListener = new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH)
                {
                    return true;
                }
                return false;
            }
        };

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setOnKeyListener(keyListener);
        mDialog.show();
        // ע��˴�Ҫ����show֮�� ����ᱨ�쳣
        mDialog.setContentView(layout);
    }
}
