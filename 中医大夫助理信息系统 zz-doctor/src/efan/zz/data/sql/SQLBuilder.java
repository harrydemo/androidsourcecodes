package efan.zz.data.sql;

import java.io.IOException;

public class SQLBuilder
{
  public static final String DATA_DIR_INPUT = "./assets/";
  public static final String DATA_DIR_OUTPUT = "./res/raw/";
  
  public static final String DECODING = "UTF-16";
  public static final String ENCODING = "UTF-8";
  
  public static void main(String[] args) throws IOException
  {
    int step = 0;
    if (args.length > 0)
    {
      step = Integer.parseInt(args[0]);
    }
    
    // Step: positive value: run to the step
    //       negative value: run the step only
    //       Other value: run all
    switch (step)
    {
      case 1:
      case -1:
        SubjectSQLBuilder.build();
        break;
        
      case 2:
        SubjectSQLBuilder.build();
      case -2:
        MedicineSQLBuilder.build();
        break;
        
      case 3:
        SubjectSQLBuilder.build();
        SubjectSQLBuilder.build();
      case -3:
        RecipeSQLBuilder.build();
        break;
        
      default:
        SubjectSQLBuilder.build();
        MedicineSQLBuilder.build();
        RecipeSQLBuilder.build();
      case -4:
        RecipeMedicineMapSQLBuilder.build();
        break;
    }
  }
}
