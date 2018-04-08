/*
 * Copyright 2009 eFANsoftware
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package efan.zz.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

public class RxRecipeDetail extends Activity
{
  private String rxId;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.rx_recipe_detail);
    
    init();
    
    editRecipeAction();
  }

  private void init()
  {
    final Intent intent = getIntent();
    this.rxId = Uri.decode(intent.getDataString());

    // query DB for detail...
    final String sql = getResources().getString(R.string.SQL_QUERY_RX_RECIPE_BY_ID);
    final Cursor cursor = ZZ.db.rawQuery(sql, new String[]{rxId});
    String name = null;
    String desc = null;
    String alias = null;
    try
    {
      cursor.moveToNext();
      name = cursor.getString(0);
      desc = cursor.getString(2);
      alias = cursor.getString(3);
    }
    finally
    {
      cursor.close();
    }
    
    final TextView nameView = (TextView) findViewById(R.id.rxFormulaName);
    if (alias != null && alias.trim().length() > 0)
      name += " (" + alias + ")";
    nameView.setText(name);

    final TextView descView = (TextView) findViewById(R.id.rxFormulaDesc);
    descView.setText(desc.replace("\\n", "\n"));
    
    // query medicines & display in table
    final TableLayout medTab = (TableLayout) findViewById(R.id.rxFormulaDetailTab);
    loadRxMedicines(rxId, medTab);

    // Draw bottom line to close the table
    final View lineView = new View(this);
    lineView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 4));
    lineView.setBackgroundResource(R.color.tabLineColor);
    medTab.addView(lineView);
  }

  private void loadRxMedicines(final String rxId, final TableLayout medTab)
  {
    final String sql = getResources().getString(R.string.SQL_LOAD_RX_MEDICINES_BY_ID);
    final Cursor cursor = ZZ.db.rawQuery(sql, new String[]{rxId});
    try
    {
      float defTextSize = getResources().getDimension(R.dimen.default_text_size);
      while (cursor.moveToNext())
      {
        String name = cursor.getString(1);
        String quantity = cursor.getString(2);
        String unit = cursor.getString(3);
        boolean isOptional = "Y".equals(cursor.getString(4));
  
        TextView nameView = new TextView(this);
        nameView.setText(name);
        nameView.setGravity(Gravity.LEFT);
        nameView.setTextSize(defTextSize);

        TextView quantityView = new TextView(this);
        quantityView.setText(quantity + unit.toString());
        quantityView.setGravity(Gravity.CENTER);
        quantityView.setTextSize(defTextSize);
        try
        {
          if (Float.parseFloat(quantity) <= 0.0f)
          {
            quantityView.setTextColor(Color.RED);
          }
        } catch (Exception e)
        {
          Log.d(this.getClass().getName(), "", e);
        }
        
        CheckBox isOptionalCB = new CheckBox(this);
        isOptionalCB.setChecked(isOptional);
        isOptionalCB.setClickable(false);
        isOptionalCB.setFocusable(false);
        isOptionalCB.setGravity(Gravity.RIGHT);
        isOptionalCB.setLines(1);
        isOptionalCB.setLineSpacing(0, 1);
        
        TableRow row = new TableRow(this);
        row.addView(nameView);
        row.addView(quantityView);
        row.addView(isOptionalCB);
        
        medTab.addView(row);
      }
    }
    finally
    {
      cursor.close();
    }
  }
  
  private void editRecipeAction()
  {
    final Button btn = (Button) findViewById(R.id.rxFormulaEditBtn);
    btn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        Uri data = Uri.parse(Uri.encode(""+rxId));
        Intent nextAct = new Intent();
        nextAct.setData(data);
        nextAct.setAction(Intent.ACTION_INSERT_OR_EDIT);
        nextAct.setClass(RxRecipeDetail.this, RxRecipeDetailEdit.class);
        startActivity(nextAct);
      }
    });
  }
  
  public static void show(String rxId, Activity caller)
  {
    Uri data = Uri.parse(Uri.encode(rxId));
    Intent nextAct = new Intent();
    nextAct.setData(data);
    nextAct.setAction(Intent.ACTION_VIEW);
    nextAct.setClass(caller, RxRecipeDetail.class);
    caller.startActivity(nextAct);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.prescription, menu);

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
    
    case R.id.menu_item_medicine:
      ZzUtil.gotoMedicine(this);
      break;
      
      // TODO
    
    default:
      ZzUtil.youngGirlWarning();
      break;
    }

    return true;
  }
}
