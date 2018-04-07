package efan.zz.android.common.android;

import android.content.Context;
import android.widget.ArrayAdapter;

public class UnitAdapter extends ArrayAdapter<String>
{
  public enum Unit
  {
    G("克"), 
    ML("毫升"),
    MEI("枚");
    
    private String chinese;
    private Unit(String chinese)
    {
      this.chinese = chinese;
    }
    
    public String toString()
    {
      return this.chinese;
    }
  }
  
  private static UnitAdapter instance;

  public static UnitAdapter getInstance(Context context)
  {
    if (instance == null)
    {
      instance = new UnitAdapter(context);
    }
    
    return instance;
  }

  private UnitAdapter(Context context)
  {
    super(context, android.R.layout.simple_dropdown_item_1line);
    init();
  }

  private void init()
  {
    this.add(Unit.G.toString());
    this.add(Unit.ML.toString());
    this.add(Unit.MEI.toString());
  }
}
