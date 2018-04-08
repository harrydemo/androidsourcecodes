package efan.zz.data.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RecipeMedicineMapSQLBuilder
{
  private static final String FILE_NAME = "zz_data_recipe_medicine_map";
  private static final String INPUT_FILE = SQLBuilder.DATA_DIR_INPUT + FILE_NAME + ".txt";
  private static final String OUTPUT_FILE = SQLBuilder.DATA_DIR_OUTPUT + FILE_NAME + ".sql";
  
  private static final String SQL_INIT = "insert into MAP_RX_RECIPE_MEDICINE (RX_RECIPE_ID, MEDICINE_ID, QUANTITY, ORDER_NUM) values (";
  private static final String SQL_FIND_RECIPE_ID = "(select PK_ID from RX_RECIPE where NAME = '";
  private static final String SQL_FIND_MEDICINE_ID = "(select PK_ID from MEDICINE where NAME = '";
  
  public static void build() throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE), SQLBuilder.DECODING));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE), SQLBuilder.ENCODING));
    String row = null;
    while ((row = in.readLine()) != null)
    {
      if (row.trim().length() == 0)
        continue;
      
      String[] columns = row.split("\t");

      StringBuilder recipeHead = new StringBuilder(SQL_INIT);
      recipeHead.append(SQL_FIND_RECIPE_ID).append(columns[0].trim()).append("'), ");
      
      for (int i=1; i<columns.length; i+=2)
      {
        out.append(recipeHead.toString());
        out.append(SQL_FIND_MEDICINE_ID).append(columns[i].trim()).append("'), ");
        out.append(columns[i+1].trim()).append(", ");
        out.append("" + i/2).append(");");
        
        out.newLine();
      }
    }

    out.close();
    in.close();
  }
}
