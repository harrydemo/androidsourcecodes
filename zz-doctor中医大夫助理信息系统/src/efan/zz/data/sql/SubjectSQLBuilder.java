package efan.zz.data.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SubjectSQLBuilder
{
  private static final String FILE_NAME = "zz_data_subject";
  private static final String INPUT_FILE = SQLBuilder.DATA_DIR_INPUT + FILE_NAME + ".txt";
  private static final String OUTPUT_FILE = SQLBuilder.DATA_DIR_OUTPUT + FILE_NAME + ".sql";
  
  private static final int COLUMN_NUM = 4;
  private static final String SQL_INIT = "insert into SYNDROME_SUBJECT (NAME, KEY_CODES, PARENT_ID, DESCRIPTION) values ('";
  private static final String SQL_FIND_PARENT_ID = "(select PK_ID from SYNDROME_SUBJECT where NAME = '";
  
  public static void build() throws IOException
  {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE), SQLBuilder.DECODING));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE), SQLBuilder.ENCODING));
    String row = null;
    while ((row = in.readLine()) != null)
    {
      if (row.trim().length() == 0)
        continue;
      
      String[] columns = row.split("\t", COLUMN_NUM);

      out.append(SQL_INIT);
      out.append(columns[0].trim()).append("', '");
      out.append(columns[1].trim()).append("', ");
      
      if (columns[2].trim().length() == 0)
      {
        out.append("null, '");
      }
      else
      {
        out.append(SQL_FIND_PARENT_ID).append(columns[2].trim()).append("'), '");
      }
      
      out.append(columns[COLUMN_NUM-1].trim()).append("');");
      
      out.newLine();
    }

    out.close();
    in.close();
  }
}
