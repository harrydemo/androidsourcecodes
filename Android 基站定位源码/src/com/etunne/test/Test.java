package com.etunne.test;

import java.security.PublicKey;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.etunne.test.Get.CellIDInfo;

public class Test extends Activity {
	

	private CdmaCellLocation location = null;
	private Button btnGetInfo = null; 
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Get get = new Get();

        btnGetInfo = (Button)findViewById(R.id.btnGet);
        btnGetInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText tv = (EditText)findViewById(R.id.editText1);
				// TODO Auto-generated method stub
				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				int type = tm.getNetworkType();
				//���й����ƶ���2G��EGDE����ͨ��2GΪGPRS�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO 
				//String OperatorName = tm.getNetworkOperatorName(); 
				Location loc = null;
				ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
				//�й�����ΪCTC
				//NETWORK_TYPE_EVDO_A���й�����3G��getNetworkType
				//NETWORK_TYPE_CDMA����2G��CDMA
				if (type == TelephonyManager.NETWORK_TYPE_EVDO_A || type == TelephonyManager.NETWORK_TYPE_CDMA || type ==TelephonyManager.NETWORK_TYPE_1xRTT)
				{
					location = (CdmaCellLocation) tm.getCellLocation();
					int cellIDs = location.getBaseStationId();
					int networkID = location.getNetworkId();
					StringBuilder nsb = new StringBuilder();
					nsb.append(location.getSystemId());
					CellIDInfo info = new CellIDInfo();
	                info.cellId = cellIDs;
                    info.locationAreaCode = networkID; //ok
                    info.mobileNetworkCode = nsb.toString();
                    info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
                    info.radioType = "cdma";
                    CellID.add(info);
				}
				//�ƶ�2G�� + CMCC + 2 
				//type = NETWORK_TYPE_EDGE
				else if(type == TelephonyManager.NETWORK_TYPE_EDGE)
				{
					GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
					int cellIDs = location.getCid();  
					int lac = location.getLac(); 
					CellIDInfo info = new CellIDInfo();
                    info.cellId = cellIDs;
                    info.locationAreaCode = lac;
                    info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);   
                    info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
                    info.radioType = "gsm";
                    CellID.add(info);
				}
				//��ͨ��2G�������� China Unicom   1 NETWORK_TYPE_GPRS
				else if(type == TelephonyManager.NETWORK_TYPE_GPRS)
				{
					GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
					int cellIDs = location.getCid();  
					int lac = location.getLac(); 
					CellIDInfo info = new CellIDInfo();
                    info.cellId = cellIDs;
                    info.locationAreaCode = lac;
                    //�������ԣ���ȡ��ͨ�����������б���ȥ�����������ִ��󣬴�������ΪJSON Parsing Error
                    //info.mobileNetworkCode = tm.getNetworkOperator().substring(0, 3);   
                    //info.mobileCountryCode = tm.getNetworkOperator().substring(3);
                    info.radioType = "gsm";
                    CellID.add(info);
				}
				else
				{
					tv.setText("Current Not Support This Type.");
				}
				
				loc = get.callGear(CellID);
				//callGear(CellID);
				
				if(loc != null)
				{
					try {
						
						StringBuilder sb = new StringBuilder();
						String pos =  get.getLocation(loc);
						sb.append("CellID:");
						sb.append(CellID.get(0).cellId);
						sb.append("+\n");
						
						sb.append("home_mobile_country_code:");
						sb.append(CellID.get(0).mobileCountryCode);
						sb.append("++\n");
						
						sb.append("mobileNetworkCode:");
						sb.append(CellID.get(0).mobileNetworkCode);
						sb.append("++\n");
						
						sb.append("locationAreaCode:");
						sb.append(CellID.get(0).locationAreaCode);
						sb.append("++\n");
						sb.append(pos);
						
						tv.setText(sb.toString());
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
        
    }
}