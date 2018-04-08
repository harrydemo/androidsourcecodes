package efan.zz.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import efan.zz.android.R;
import efan.zz.android.ZZ;
import efan.zz.android.util.ZzUtil;

//ҽҩ�������
public class MedicineDetail extends Activity
{
  private String medId;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.medicine_detail);
    
    init();
  }

  private void init()
  {
    final Intent intent = getIntent();
    this.medId = Uri.decode(intent.getDataString());

    // query DB for detail...  �����ݿ��в���
    final String sql = getResources().getString(R.string.SQL_QUERY_MEDICINE_BY_ID);
    final Cursor cursor = ZZ.db.rawQuery(sql, new String[]{medId});
    String name = null;
    String alias = null;
    String keyCodes;
    String unit;
    String poison;
    String syndromeId;
    String property;
    String efficacy;
    String indication;
    String synergist;
    String desc = null;
    
    try
    {
      cursor.moveToNext();
      name = cursor.getString(0);
      alias = cursor.getString(1);
      keyCodes = cursor.getString(2);
      unit = cursor.getString(3);
      poison = cursor.getString(4);
      syndromeId = cursor.getString(5);
      property = cursor.getString(6);
      efficacy = cursor.getString(7);
      indication = cursor.getString(8).replace("\\n", "\n");
      synergist = cursor.getString(9);
      desc = cursor.getString(10).replace("\\n", "\n");
    }
    finally
    {
      cursor.close();
    }
    
    final TextView nameView = (TextView) findViewById(R.id.medicineName);
    nameView.setText(name);

    final TextView detailView = (TextView) findViewById(R.id.medicineDetail);
    StringBuilder detail = new StringBuilder();
    detail.append("��������").append(alias).append("\n");
    detail.append("��ҩ�ԡ�").append(property).append("\n");
    detail.append("����Ч��").append(efficacy).append("\n");
    detail.append("��Ӧ�á�\n").append(indication).append("\n");
    detail.append("������(��ҩ3�ֶ�)��").append(poison).append("\n");
    detail.append("��ҩ����λ��").append(unit).append("\n");
    detail.append(desc);
    detailView.setText(detail);
  }

  //��ʾ����
  public static void show(String medicineId, Activity caller)
  {
    Uri data = Uri.parse(Uri.encode(medicineId));
    Intent nextAct = new Intent();
    nextAct.setData(data);
    nextAct.setAction(Intent.ACTION_VIEW);
    nextAct.setClass(caller, MedicineDetail.class);
    caller.startActivity(nextAct);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.medicine, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case R.id.menu_item_home:
      ZzUtil.goHome(this);
      break;
    
    case R.id.menu_item_prescription:
      ZzUtil.goHome(this);
      break;
    
      // TODO
    
    default:
      ZzUtil.youngGirlWarning();
      break;
    }

    return true;
  }
}
