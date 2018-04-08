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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import efan.zz.android.R;
import efan.zz.android.ZZ;
import efan.zz.android.common.IdentifiedString;
import efan.zz.android.common.android.IdentifiedStringAdapter;
import efan.zz.android.util.ZzUtil;

public class MedicineQuery extends Activity
{
  private IdentifiedStringAdapter selectedMedicineAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.medicine_query);

    init();
  }

  private void init()
  {
    final IdentifiedStringAdapter syndromeAdapter = new IdentifiedStringAdapter(this,
            android.R.layout.simple_spinner_item);
    String sql = getResources().getString(R.string.SQL_LOAD_SYNDROME_FOR_AC);
    syndromeAdapter.loadNameOrKeyAutoCompAdapter(sql);
    syndromeAdapter.insert(new IdentifiedString("------", -1), 0);

    final IdentifiedStringAdapter allMedicineAdapter = new IdentifiedStringAdapter(this,
            android.R.layout.simple_dropdown_item_1line);
    sql = getResources().getString(R.string.SQL_LOAD_MEDICINE_FOR_AC);
    allMedicineAdapter.preLoadNameOrKeyAutoCompAdapter(sql);

    final Spinner syndromeText = (Spinner) findViewById(R.id.syndrome_subject);
    final AutoCompleteTextView medicineText = (AutoCompleteTextView) findViewById(R.id.key_code_medicine);
    final Button queryBtn = (Button) findViewById(R.id.queryButton);

    syndromeText.setAdapter(syndromeAdapter);
    syndromeText.setOnItemSelectedListener(new OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
      {
        IdentifiedString selectedSyndrome = syndromeAdapter.getItem(position);
        if (selectedSyndrome.id == -1)
          return;
        
        String sql = getResources().getString(R.string.SQL_LOAD_MEDICINE_FOR_AC_BY_SYNDROME);
        sql = sql.replace("?", ""+selectedSyndrome.id);

        IdentifiedStringAdapter partMedicineAdapter = new IdentifiedStringAdapter(MedicineQuery.this,
            android.R.layout.simple_dropdown_item_1line);
        partMedicineAdapter.loadNameOrKeyAutoCompAdapter(sql);
        selectedMedicineAdapter = partMedicineAdapter;
        medicineText.setAdapter(selectedMedicineAdapter);
        ZZ.inputMethodMgr.hideSoftInputFromWindow(medicineText.getWindowToken(), 0);
        medicineText.showDropDown();
      }

      public void onNothingSelected(AdapterView<?> parent)
      {}
    });
    
    medicineText.setAdapter(allMedicineAdapter);
    selectedMedicineAdapter = allMedicineAdapter;
    medicineText.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        IdentifiedString selectedMedicine = selectedMedicineAdapter.getItem(position);

        MedicineDetail.show("" + selectedMedicine.id, MedicineQuery.this);
      }
    });

    queryBtn.setOnClickListener(new Button.OnClickListener()
    {
      public void onClick(View v)
      {
        selectedMedicineAdapter = allMedicineAdapter;
        medicineText.setText("");
        medicineText.setAdapter(selectedMedicineAdapter);
        ZZ.inputMethodMgr.hideSoftInputFromWindow(medicineText.getWindowToken(), 0);
        medicineText.showDropDown();
      }
    });
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

    // TODO
    
    default:
      ZzUtil.youngGirlWarning();
      break;
    }

    return true;
  }

}
