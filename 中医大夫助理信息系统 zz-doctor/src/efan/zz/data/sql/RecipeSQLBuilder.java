package efan.zz.data.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RecipeSQLBuilder
{
  private static final String FILE_NAME = "zz_data_recipe";
  private static final String INPUT_FILE = SQLBuilder.DATA_DIR_INPUT + FILE_NAME + ".txt";
  private static final String OUTPUT_FILE = SQLBuilder.DATA_DIR_OUTPUT + FILE_NAME + ".sql";
  
  private static final int COLUMN_NUM = 9;
  private static final String SQL_INIT = "insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('";
  private static final String SQL_FIND_SYNDROME_ID = "(select PK_ID from SYNDROME_SUBJECT where NAME = '";
  
  public static void build() throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE), SQLBuilder.DECODING));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE), SQLBuilder.ENCODING));
    
    int index = 0;
    String row = null;
    while ((row = in.readLine()) != null)
    {
      if (row.trim().length() == 0)
        continue;
      
      while (row.split("\"", -1).length % 2 == 0)
      {
        row += " \\n" + in.readLine();
      }
      
      row = row.replaceAll("\"", "");
      
      String[] columns = row.split("\t", COLUMN_NUM);

      out.append(SQL_INIT);
      out.append("" + (++index)).append("', '");      // PK_ID
      out.append(columns[0].trim()).append("', ");  // 名称
      out.append(SQL_FIND_SYNDROME_ID).append(columns[1].trim()).append("'), '");   // 类别

      for (int i=2; i<4; i++)   // 别名 以及 代码
      {
        out.append(columns[i].trim()).append("', '");
      }

      StringBuilder descBuilder = new StringBuilder();
      descBuilder.append("【功用】").append(columns[4].trim()).append('\n');
      descBuilder.append("【主治】").append(columns[5].trim()).append('\n');
      descBuilder.append("【用法及注意】").append(columns[6].trim()).append('\n');
      descBuilder.append("【歌诀】:\n").append(columns[7].trim()).append('\n');
      out.append(descBuilder.toString()).append("');");
      
      out.newLine();
    }

    out.close();
    in.close();
  }
}
