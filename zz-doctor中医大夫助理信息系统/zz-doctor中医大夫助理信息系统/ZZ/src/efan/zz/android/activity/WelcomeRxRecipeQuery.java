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
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import efan.zz.android.R;
import efan.zz.android.ZZ;
import efan.zz.android.common.IdentifiedString;
import efan.zz.android.common.android.IdentifiedStringAdapter;
import efan.zz.android.util.ZzUtil;

public class WelcomeRxRecipeQuery extends Activity
{
  private IdentifiedString selectedRxRecipe;
  private IdentifiedStringAdapter selectedRxRecipeAdapter;
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.welcome);

    init();
  }

  private void init()
  {
    final IdentifiedStringAdapter syndromeAdapter = new IdentifiedStringAdapter(this,
            android.R.layout.simple_spinner_item);
    String sql = getResources().getString(R.string.SQL_LOAD_SYNDROME_FOR_AC);
    syndromeAdapter.loadNameOrKeyAutoCompAdapter(sql);
    syndromeAdapter.insert(new IdentifiedString("------", -1), 0);

    final IdentifiedStringAdapter allRxRecipeAdapter = new IdentifiedStringAdapter(this,
            android.R.layout.simple_dropdown_item_1line);
    sql = getResources().getString(R.string.SQL_LOAD_RX_RECIPE_FOR_AC);
    allRxRecipeAdapter.preLoadNameOrKeyAutoCompAdapter(sql);

    final Spinner syndromeText = (Spinner) findViewById(R.id.syndrome_subject);
    final AutoCompleteTextView rxRecipeText = (AutoCompleteTextView) findViewById(R.id.key_code_formula);
    final Button queryBtn = (Button) findViewById(R.id.queryButton);

    syndromeText.setAdapter(syndromeAdapter);
    syndromeText.setOnItemSelectedListener(new OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        IdentifiedString selectedSyndrome = syndromeAdapter.getItem(position);
        if (selectedSyndrome.id == -1)
          return;
        
        String sql = getResources().getString(R.string.SQL_LOAD_RX_RECIPE_FOR_AC_BY_SYNDROME);
        sql = sql.replace("?", ""+selectedSyndrome.id);

        IdentifiedStringAdapter partAdapter = new IdentifiedStringAdapter(WelcomeRxRecipeQuery.this,
            android.R.layout.simple_dropdown_item_1line);
        partAdapter.loadNameOrKeyAutoCompAdapter(sql);
        selectedRxRecipeAdapter = partAdapter;
        rxRecipeText.setAdapter(selectedRxRecipeAdapter);
        rxRecipeText.setText("");
        rxRecipeText.refreshDrawableState();
        ZZ.inputMethodMgr.hideSoftInputFromWindow(rxRecipeText.getWindowToken(), 0);
        rxRecipeText.showDropDown();
      }

      public void onNothingSelected(AdapterView<?> parent)
      {}
    });
    
    rxRecipeText.setAdapter(allRxRecipeAdapter);
    selectedRxRecipeAdapter = allRxRecipeAdapter;
    rxRecipeText.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        selectedRxRecipe = selectedRxRecipeAdapter.getItem(position);

        RxRecipeDetail.show("" + selectedRxRecipe.id, WelcomeRxRecipeQuery.this);
      }
    });

    queryBtn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        selectedRxRecipeAdapter = allRxRecipeAdapter;
        rxRecipeText.setAdapter(selectedRxRecipeAdapter);
        rxRecipeText.setText("");
        rxRecipeText.refreshDrawableState();
        ZZ.inputMethodMgr.hideSoftInputFromWindow(rxRecipeText.getWindowToken(), 0);
        rxRecipeText.showDropDown();
        return;
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the home menu XML resource.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.home, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case R.id.menu_item_medicine:
      ZzUtil.gotoMedicine(this);
      break;

    case R.id.menu_item_about:
      showAbout();
      break;

      // TODO
    
    default:
      ZzUtil.youngGirlWarning();
      break;
    }

    return true;
  }
  
  private void showAbout()
  {
    StringBuilder about = new StringBuilder();
    about.append("杏林书童1.0.0版 \n")
         .append("\n") 
         .append("【功能】: 方便快捷查询实用合理的方剂精华和药物；支持方剂增减以及新增验方。 \n") 
         .append("\n") 
         .append("【鸣谢】: 数据主要来源于: \n") 
         .append("      1. 中医e百 (tcm100) \n") 
         .append("      2. 中国医药网 (pharmnet) \n") 
         .append("\n") 
         .append("公元2010年元旦"); 
    final AlertDialog pop = new AlertDialog.Builder(this).create();
    pop.setMessage(about);
    pop.setButton(DialogInterface.BUTTON_POSITIVE, "市场更新", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        try
        {
          ZzUtil.searchMarket(WelcomeRxRecipeQuery.this, "pub", "efansoftware");
        } catch (Exception e)
        {
          Log.w(WelcomeRxRecipeQuery.this.getClass().getName(), "", e);
          ZzUtil.toastMessage("对不起, 市场应用工作异常, 请检查网络或稍后再试...", true);
        }
      }
    });
    pop.setButton(DialogInterface.BUTTON_NEGATIVE, "返回", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        pop.dismiss();
      }
    });
    pop.setCancelable(true);
    pop.setCanceledOnTouchOutside(true);
    pop.show();
  }

}
