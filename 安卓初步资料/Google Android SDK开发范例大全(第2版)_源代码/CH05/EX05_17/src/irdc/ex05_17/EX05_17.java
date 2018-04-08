package irdc.ex05_17;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class EX05_17 extends Activity
{
  private TextView mTextView01;
  private CheckBox mCheckBox01;
  
  /* ����WiFiManager���� */
  private WifiManager mWiFiManager01;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    mTextView01 = (TextView) findViewById(R.id.myTextView1);
    mCheckBox01 = (CheckBox) findViewById(R.id.myCheckBox1);
    
    /* ��getSystemServiceȡ��WIFI_SERVICE */
    mWiFiManager01 = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    
    /* �ж�ִ�г�����WiFi״̬�Ƿ������������� */
    if(mWiFiManager01.isWifiEnabled())
    {
      /* �ж�WiFi״̬�Ƿ������� */
      if(mWiFiManager01.getWifiState()==WifiManager.WIFI_STATE_ENABLED)
      {
        /* ��WiFi������������ȡ��� */
        mCheckBox01.setChecked(true);
        /* ���ĺ�ȡ������Ϊ�ر�WiFi*/
        mCheckBox01.setText(R.string.str_uncheck);
      }
      else
      {
        /* ��WiFiδ����������ȡ�ѡȡ�� */
        mCheckBox01.setChecked(false);
        /* �����ȡ������Ϊ����WiFi*/
        mCheckBox01.setText(R.string.str_checked);
      }
    }
    else
    {
      mCheckBox01.setChecked(false);
      mCheckBox01.setText(R.string.str_checked);
    }
    
    /* ��׽CheckBox�ĵ���¼� */
    mCheckBox01.setOnClickListener(new CheckBox.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* ����ȡ��Ϊȡ����ȡ״̬ */
        if(mCheckBox01.isChecked()==false)
        {
          /* ���Թر�Wi-Fi���� */
          try
          {
            /* �ж�WiFi״̬�Ƿ�Ϊ������ */
            if(mWiFiManager01.isWifiEnabled() )
            {
              /* �ر�WiFi */
              if(mWiFiManager01.setWifiEnabled(false))
              {
                mTextView01.setText(R.string.str_stop_wifi_done);
              }
              else
              {
                mTextView01.setText(R.string.str_stop_wifi_failed);
              }
            }
            else
            {
              /* WiFi״̬��Ϊ������״̬ʱ */
              switch(mWiFiManager01.getWifiState())
              {
                /* WiFi�������������У������޷��ر�... */
                case WifiManager.WIFI_STATE_ENABLING:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_enabling)
                  );
                  break;
                /* WiFi���ڹرչ����У������޷��ر�... */
                case WifiManager.WIFI_STATE_DISABLING:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_disabling)
                  );
                  break;
                /* WiFi�Ѿ��ر� */
                case WifiManager.WIFI_STATE_DISABLED:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_disabled)
                  );
                  break;
                /* �޷�ȡ�û��ʶWiFi״̬ */
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_unknow)
                  );
                  break;
              }
              mCheckBox01.setText(R.string.str_checked);
            }
          }
          catch (Exception e)
          {
            Log.i("HIPPO", e.toString());
            e.printStackTrace();
          }
        }
        else if(mCheckBox01.isChecked()==true)
        {
          /* ���Կ���Wi-Fi���� */
          try
          {
            /* ȷ��WiFi�����ǹر��Ҳ������������� */
            if(!mWiFiManager01.isWifiEnabled() && mWiFiManager01.getWifiState()!=WifiManager.WIFI_STATE_ENABLING )
            {
              if(mWiFiManager01.setWifiEnabled(true))
              {
                switch(mWiFiManager01.getWifiState())
                {
                  /* WiFi���������������У������޷�����... */
                  case WifiManager.WIFI_STATE_ENABLING:
                    mTextView01.setText
                    (
                      getResources().getText(R.string.str_wifi_enabling)
                    );
                    break;
                  /* WiFi�Ѿ�Ϊ�������޷��ٴ�����... */
                  case WifiManager.WIFI_STATE_ENABLED:
                    mTextView01.setText
                    (
                      getResources().getText(R.string.str_start_wifi_done)
                    );
                    break;
                  /* ����δ֪�Ĵ��� */
                  default:
                    mTextView01.setText
                    (
                      getResources().getText(R.string.str_start_wifi_failed)+":"+
                      getResources().getText(R.string.str_wifi_unknow)
                    );
                    break;
                }
              }
              else
              {
                mTextView01.setText(R.string.str_start_wifi_failed);
              }
            }
            else
            {
              switch(mWiFiManager01.getWifiState())
              {
                /* WiFi�������������У������޷�����... */
                case WifiManager.WIFI_STATE_ENABLING:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_start_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_enabling)
                  );
                  break;
                /* WiFi���ڹرչ����У������޷�����... */
                case WifiManager.WIFI_STATE_DISABLING:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_start_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_disabling)
                  );
                  break;
                /* WiFi�Ѿ��ر� */
                case WifiManager.WIFI_STATE_DISABLED:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_start_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_disabled)
                  );
                  break;
                /* �޷�ȡ�û��ʶWiFi״̬ */
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                  mTextView01.setText
                  (
                    getResources().getText(R.string.str_start_wifi_failed)+":"+
                    getResources().getText(R.string.str_wifi_unknow)
                  );
                  break;
              }
            }
            mCheckBox01.setText(R.string.str_uncheck);
          }
          catch (Exception e)
          {
            Log.i("HIPPO", e.toString());
            e.printStackTrace();
          }
        }
      }
    });
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX05_17.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX05_17.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    
    /* ��onResume��д�¼�Ϊȡ�ÿ�������ǰWiFi��״̬ */
    try
    {
      switch(mWiFiManager01.getWifiState())
      {
        /* WiFi�Ѿ�Ϊ����״̬... */
        case WifiManager.WIFI_STATE_ENABLED:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_enabling)
          );
          break;
        /* WiFi��������������״̬... */
        case WifiManager.WIFI_STATE_ENABLING:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_enabling)
          );
          break;
        /* WiFi���ڹرչ�����... */
        case WifiManager.WIFI_STATE_DISABLING:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_disabling)
          );
          break;
        /* WiFi�Ѿ��ر� */
        case WifiManager.WIFI_STATE_DISABLED:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_disabled)
          );
          break;
        /* �޷�ȡ�û��ʶWiFi״̬ */
        case WifiManager.WIFI_STATE_UNKNOWN:
        default:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_unknow)
          );
          break;
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.getStackTrace();
    }
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
  }
}

