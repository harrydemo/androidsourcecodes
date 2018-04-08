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
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import efan.zz.android.R;
import efan.zz.android.ZZ;
import efan.zz.android.common.android.IdentifiedAutoCompleteTextView;
import efan.zz.android.common.android.IdentifiedStringAdapter;
import efan.zz.android.util.ZzUtil;

/*
 * Implementation decision: copy&paste RxRecipeDetail.java code,
 * Only pass rx_recipe id as parameter,
 * To query the DB again: if it's slow to user, 
 * the query must be optimised anyway otherwise it's slow in RxRecipeDetail page already!
 */
public class RxRecipeDetailEdit extends Activity
{
  private EditText nameView;
  private EditText aliasView;
  private EditText keyCodeView;
  private EditText descView; 
  private TableLayout medTab;
  private static IdentifiedStringAdapter medsAdapter;
  
  private String rxId0;
  private String name0;
  
  private static OnClickListener insertBtnListener, delBtnListener;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.rx_recipe_detail_edit);
    
    init();
    initSaveAction();
    initCancelAction();
  }

  private void init()
  {
    final Intent intent = getIntent();
    rxId0 = Uri.decode(intent.getDataString());

    // query DB for detail...
    final String sql = getResources().getString(R.string.SQL_QUERY_RX_RECIPE_BY_ID);
    final Cursor cursor = ZZ.db.rawQuery(sql, new String[]{rxId0});
    String name = null;
    String desc = null;
    String alias = null;
    String keyCode = null;
    try
    {
      cursor.moveToNext();
      name = cursor.getString(0);
      keyCode = cursor.getString(1);
      desc = cursor.getString(2);
      alias = cursor.getString(3);
    }
    finally
    {
      cursor.close();
    }
    
    nameView = (EditText) findViewById(R.id.rxFormulaName);
    nameView.setText(name);
    name0 = name;
    
    aliasView = (EditText) findViewById(R.id.rxFormulaAlias);
    aliasView.setText(alias);
    
    keyCodeView = (EditText) findViewById(R.id.rxFormulaKeyCode);
    keyCodeView.setText(keyCode);

    descView = (EditText) findViewById(R.id.rxFormulaDesc);
    descView.setText(desc.replace("\\n", "\n"));
  }
  
  @Override
  public void onStart()
  {
    super.onStart();
    
    // query medicines & display in table
    loadMedsAutoCompAdapter();
    initInsertDelListeners();
    medTab = (TableLayout) findViewById(R.id.rxFormulaDetailTab);
    loadRxMedicines(rxId0, medTab);
    
    // Add empty line at the end for adding medicines to the end
    TableRow nilRow = new TableRow(this);
    Button lastInsertBtn = new Button(this);
    lastInsertBtn.setGravity(Gravity.CENTER | Gravity.LEFT);
    lastInsertBtn.setText("+");
    lastInsertBtn.setOnClickListener(insertBtnListener);
    EditText nilText = new EditText(this);
    nilText.setEnabled(false);
    nilText.setText("...");
    nilRow.addView(lastInsertBtn);
    nilRow.addView(nilText);
    medTab.addView(nilRow);
  }
  
  private void loadMedsAutoCompAdapter()
  {
    if (medsAdapter != null)
      return;
    
    medsAdapter = new IdentifiedStringAdapter(this, android.R.layout.simple_dropdown_item_1line);
    final String sql = getResources().getString(R.string.SQL_LOAD_MEDICINE_FOR_AC);
    medsAdapter.loadNameOrKeyAutoCompAdapter(sql);
  }
  
  private void initInsertDelListeners()
  {
    insertBtnListener = new OnClickListener()
    {
      public void onClick(View v)
      {
        ((TableRow) v.getParent()).requestFocus();
        final TableRow newRow = buildTableRow(-1, "", "", false);
        final View currentRow = medTab.getFocusedChild();
        final int idx = medTab.indexOfChild(currentRow);
        medTab.addView(newRow, idx);
        newRow.requestFocus();
      }
    };
    
    delBtnListener = new OnClickListener()
    {
      public void onClick(View v)
      {
        ((TableRow) v.getParent()).requestFocus();
        final View currentRow = medTab.getFocusedChild();
        medTab.removeView(currentRow);
      }
    };
  }

  private void loadRxMedicines(final String rxId, final TableLayout medTab)
  {
    final String sql = getResources().getString(R.string.SQL_LOAD_RX_MEDICINES_BY_ID);
    final Cursor cursor = ZZ.db.rawQuery(sql, new String[]{rxId});
    try
    {
      while (cursor.moveToNext())
      {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String quantity = cursor.getString(2);
        boolean isOptional = "Y".equals(cursor.getString(4));
        
        TableRow row = buildTableRow(id, name, quantity, isOptional);
        medTab.addView(row);
      }
    }
    finally
    {
      cursor.close();
    }
  }

  private TableRow buildTableRow(int id, String name, String quantity, boolean isOptional)
  {
    Button insertBtn = new Button(this);
    insertBtn.setGravity(Gravity.CENTER | Gravity.CENTER);
    insertBtn.setText("+");
    insertBtn.setOnClickListener(insertBtnListener);

    final IdentifiedAutoCompleteTextView medNameText = new IdentifiedAutoCompleteTextView(this);
    medNameText.setValueId(id);
    
    final EditText qtyText = new EditText(this);

    final StringBuilder isClicked = new StringBuilder();
    medNameText.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        medNameText.setValueId(medsAdapter.getItem(position).id);
        
        isClicked.append(Boolean.TRUE);
        
        qtyText.requestFocus();
      }
    });
    medNameText.setThreshold(1);
    medNameText.setGravity(Gravity.CENTER | Gravity.LEFT);
    medNameText.setAdapter(medsAdapter);
    
    final StringBuilder nameStr0 = new StringBuilder();
    medNameText.setOnFocusChangeListener(new OnFocusChangeListener()
    {
      public void onFocusChange(View v, boolean hasFocus)
      {
        String nameVal0 = medNameText.getText().toString();
        if (hasFocus)
        {
          nameStr0.replace(0, nameStr0.length(), nameVal0);

          if (! medsAdapter.isReady())
            ZzUtil.doWait(medsAdapter);
          
          medNameText.setText("");
          ZZ.inputMethodMgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
          medNameText.showDropDown();
          isClicked.delete(0, isClicked.length());
        }
        else
        {
          medNameText.dismissDropDown();
          if (isClicked.length() == 0)
          {
            medNameText.setText(nameStr0.toString());
          }
        }
      }
    });
    medNameText.setText(name);

    qtyText.setInputType(InputType.TYPE_CLASS_NUMBER);
    qtyText.setGravity(Gravity.CENTER | Gravity.RIGHT);
    qtyText.setText(quantity);
    qtyText.setMaxEms(4);
    
    CheckBox optionalCBox = new CheckBox(this);
    optionalCBox.setGravity(Gravity.CENTER);
    optionalCBox.setChecked(isOptional);
    
    Button delBtn = new Button(this);
    delBtn.setGravity(Gravity.CENTER | Gravity.CENTER);
    delBtn.setText("-");
    delBtn.setOnClickListener(delBtnListener);

    TableRow row = new TableRow(this);
    row.addView(insertBtn);
    row.addView(medNameText);
    row.addView(qtyText);
    row.addView(optionalCBox);
    row.addView(delBtn);
    return row;
  }
  
  private void initSaveAction()
  {
    Button.OnClickListener listener = new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        int confirmMsgId = R.string.rxFormula_detail_edit_save_confirm_insert;
        String name1 = nameView.getText().toString().trim();
        final boolean isNew = ! name0.equals(name1); 
        if (! isNew)
        {
          confirmMsgId = R.string.rxFormula_detail_edit_save_confirm_update;
        }

        new AlertDialog.Builder(RxRecipeDetailEdit.this)
          .setTitle(R.string.alert_dialog_confirm_title)
          .setIcon(R.drawable.alert_dialog_icon)
          .setMessage(confirmMsgId)
          .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() 
          {
              public void onClick(DialogInterface dialog, int whichButton) 
              {
                String rxId = saveData(isNew);
                
                RxRecipeDetail.show(rxId, RxRecipeDetailEdit.this);
              }
          })
          .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() 
          {
              public void onClick(DialogInterface dialog, int whichButton) {}
          })
          .create()
          .show();
      }
    };

    Button btn1 = (Button) findViewById(R.id.rxFormulaSaveBtn1);
    Button btn2 = (Button) findViewById(R.id.rxFormulaSaveBtn2);
    btn1.setOnClickListener(listener);
    btn2.setOnClickListener(listener);
  }
  private void initCancelAction()
  {
    Button btn = (Button) findViewById(R.id.rxFormulaCancelBtn);
    btn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        RxRecipeDetail.show(rxId0, RxRecipeDetailEdit.this);
      }
    });
  }
  
  private String saveData(boolean isNew)
  {
    ZZ.db.beginTransaction();
    try
    {
      final String name = nameView.getText().toString().trim();
      final String alias = aliasView.getText().toString().trim();
      final String keyCode = keyCodeView.getText().toString().trim();
      final String desc = descView.getText().toString().trim();
      
      ContentValues recipeValues = new ContentValues();
      recipeValues.put("NAME", name);
      recipeValues.put("ALIAS", alias.length() != 0 ? alias : null);
      recipeValues.put("KEY_CODES", keyCode);
      recipeValues.put("DESCRIPTION", desc);

      String resultId = rxId0;
      if (isNew)
      // Insert Data
      {
        // Insert RX_RECIPE 1st
        recipeValues.put("BASE_ID", rxId0);
        resultId = "" + ZZ.db.insertOrThrow("RX_RECIPE", "", recipeValues);
        
        // Insert MAP_RX_RECIPE_MEDICINE 2nd
        insertMapRxMeds(ZZ.db, resultId);
      }
      else
      // Update Data
      {
        // Update RX_RECIPE 1st
        ZZ.db.update("RX_RECIPE", recipeValues, "PK_ID=?", new String[]{rxId0});
        
        // delete old MAP_RX_RECIPE_MEDICINE 2nd
        ZZ.db.delete("MAP_RX_RECIPE_MEDICINE", "RX_RECIPE_ID=?", new String[]{rxId0});

        // Insert new MAP_RX_RECIPE_MEDICINE last
        insertMapRxMeds(ZZ.db, rxId0);
      }
      
      ZZ.db.setTransactionSuccessful();
      return resultId;
    }
    finally
    {
      ZZ.db.endTransaction();
    }
  }

  private void insertMapRxMeds(SQLiteDatabase rwDB, String rxId)
  {
    int n = medTab.getChildCount()-1;
    for (int i=2; i<n; i++)     // 0 & 1 are table header & head-line
    {
      TableRow row = (TableRow) medTab.getChildAt(i);

      int medId = -1;
      IdentifiedAutoCompleteTextView nameView = (IdentifiedAutoCompleteTextView) row.getChildAt(1);
      medId = nameView.getValueId();

      float qty = 0.0f;
      try
      {
        qty = Float.parseFloat(((EditText)row.getChildAt(2)).getText().toString().trim());
      } catch (Exception e)
      {
        Log.d(this.getClass().getName(), "", e);
      }
      boolean isOptional = ((CheckBox) row.getChildAt(3)).isChecked(); 
        
      ContentValues mapValues = new ContentValues();
      mapValues.put("RX_RECIPE_ID", rxId);
      mapValues.put("MEDICINE_ID", medId);
      mapValues.put("ORDER_NUM", i);
      mapValues.put("QUANTITY", qty);
      mapValues.put("IS_OPTIONAL", isOptional ? "Y" : null);
      rwDB.insertOrThrow("MAP_RX_RECIPE_MEDICINE", "", mapValues);
    }
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
