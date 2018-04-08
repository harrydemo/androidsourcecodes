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
				//在中国，移动的2G是EGDE，联通的2G为GPRS，电信的2G为CDMA，电信的3G为EVDO 
				//String OperatorName = tm.getNetworkOperatorName(); 
				Location loc = null;
				ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
				//中国电信为CTC
				//NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
				//NETWORK_TYPE_CDMA电信2G是CDMA
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
				//移动2G卡 + CMCC + 2 
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
				//联通的2G经过测试 China Unicom   1 NETWORK_TYPE_GPRS
				else if(type == TelephonyManager.NETWORK_TYPE_GPRS)
				{
					GsmCellLocation location = (GsmCellLocation)tm.getCellLocation();  
					int cellIDs = location.getCid();  
					int lac = location.getLac(); 
					CellIDInfo info = new CellIDInfo();
                    info.cellId = cellIDs;
                    info.locationAreaCode = lac;
                    //经过测试，获取联通数据以下两行必须去掉，否则会出现错误，错误类型为JSON Parsing Error
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