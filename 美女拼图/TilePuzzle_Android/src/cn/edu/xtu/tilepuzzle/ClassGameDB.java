package cn.edu.xtu.tilepuzzle;


public class ClassGameDB {
    public static final String author="���ߣ���Ҫ��";
    public static final String school="������̶��ѧ";
    
    //�ֻ���ʼ����ѡ��˵�
    public static final String[] menu = { "��ʼ��Ϸ", "����ͼƬ", "��ѳɼ�", "��Ϸѡ��", "��Ϸ����"};
    //public static final String[] menu = { "��ʼ��Ϸ", "����ͼƬ", "��ѳɼ�", "��Ϸѡ��", "��Ϸ����","��Ϸ����"};
    //public static final String[] menu = { "��ʼ��Ϸ", "����ͼƬ", "��ѳɼ�", "ѡ��", "����","��Ϸ����","����" };
    // �д�С
    public static final int rows=4;
    // �д�С
    public static final int columns = 5;
    
    /**
     * ���ݿ���
     * */
    public static final String DATABASE_TILEPUZZLE_NAME = "DBTilePuzzle.db"; 
    /**
     * ���ݿ����Ϸ������Ϣ�ı���
     * */
    public static final String TABLE_TILEPUZZLE_GAMEDATA = "TableGameData";
    
    /**
     * ���ݿ�ĸ�����Ϸ��Ϣ�ı���
     * */
    public static final String TABLE_TILEPUZZLE_PEOPLEINFO = "TablePeopleInfoData";
    
    /**
	 * ���ݿ����Ϸ��Ϣ��Ľṹ
	 * @orgImageID 0 Ĭ��ͼƬ·����string��
	 * @reversed 1 ����(boolean ��/��)
	 * @funny 2 Ȥζϴ��(boolean ��/��)
	 * @addStringg 3 ��Ƿ��� (boolean ��/��)
	 * @hard 4 ����/��(boolean ����/����)
	 * @rows 5 �д�С(int)
	 * @columns 6 �д�С (int)
	 */
    public static final String GAMEDATA_STRUCTURE = " ("
		+ "orgImageID TEXT," 
		+ "reversed char(1),"
		+ "funny char(1),"
		+ "addString char(1),"
		+ "hard char(1),"
		+ "rows TEXT,"
		+ "columns TEXT"
		+ ")";
    /**
     * ���ݿ�������Ϣ��Ľṹ
     * @NAME ����
     * @TIME ʱ��
     * */
    public static final String PEOPLEINFO_STRUCTURE =" ("
		+ "NAME TEXT," 
		+ "TIME INTEGER"
		+ ");";
  
   
    /**Ĭ��ͼƬ��·��*/ 
    public static final String orgImageID = String.valueOf(R.drawable.mm0);
    
    /**ͼƬ����*/     
    public static final int PHOTO_MM_NUMBERS = 0;
    public static final int PHOTO_DONGMAN_NUMBERS = 1;
    public static final int PHOTO_CHUANGYI_NUMBERS = 2;
    public static final int PHOTO_OTHER_NUMBERS = 3; 

	
	/**
	 * ������������ ��ͼƬԴ
	 * 0	��Ů
	 * 1	����
	 * 2	����
	 * 3	����
	 * */
	public static final int[][] mImageIds ={ 
			{
				R.drawable.mm0, 
				R.drawable.mm1,
				R.drawable.mm2,
				R.drawable.mm3,
			},
			{
				R.drawable.dongman0, 
				R.drawable.dongman1, 
				R.drawable.dongman2,
			},
			{
				R.drawable.chuangyi0, 
				R.drawable.chuangyi1, 
				R.drawable.chuangyi2,
			},
			{
				R.drawable.test480x616,
				R.drawable.test408x480,
				R.drawable.test280x540,
				R.drawable.test240x320
			}
	};
    

    //��Ϸ������Ϣ�ڼ�¼���б����ID
   // public static final int GameSetDataFlag=1;
    
    /**
     * @orgImageID 0 Ĭ��ͼƬ·����string�� 
     * @reversed 1 ����(boolean ��/��)
     * @funny 2 Ȥζϴ��(boolean ��/��)	
     * @addStringg 3 ��Ƿ��� (boolean ��/��)	
     * @hard 4 ����/��(boolean ����/����)	
     * @rows 5 �д�С(int)	
     * @columns 6 �д�С	(int)		
     */
	public static final String gameSetDataName[] = 	{ "orgImageID", "reversed", "funny", "addString", "hard", "rows", 				"columns" };
    public static final String gameSetData[] = 		{ orgImageID, 	"N", 		"N", 		"N", 		"N", 	String.valueOf(rows), String.valueOf(columns), };
    //��Ϸ������Ϣ����ĳ���
    public static final int GameSetDataNum=gameSetData.length;
   
    /**
     * ��Ϸ���ò����������е�λ��
     * */
    public static final int IndexInGameSetDatat_orgImageID = 0;
    public static final int IndexInGameSetDatat_reversed = 1;
    public static final int IndexInGameSetDatat_funny = 2;
    public static final int IndexInGameSetDatat_addString = 3;
    public static final int IndexInGameSetDatat_hard = 4;
    public static final int IndexInGameSetDatat_rows = 5;
    public static final int IndexInGameSetDatat_columns = 6;
    
    /**
     * ����������:����Ϸ
     */ 
    public static final int COMMAND_ID_NEW_GAME = 0;
    /**
     * ����������:����ͼƬ
     */ 
    public static final int COMMAND_ID_SHOW_PHOTO = 1;
    /**
     * ����������:��ѳɼ�
     */ 
    public static final int COMMAND_ID_BEST = 2;
    /**
     * ����������:��Ϸ����
     */ 
    public static final int COMMAND_ID_OPTIONS = 3;
    /**
     * ����������:��Ϸ����
     */ 
    public static final int COMMAND_ID_HELP = 4;
    /**
     * ����������:��Ϸ����
     */ 
    public static final int COMMAND_ID_RESET = 5;
    /**
     * ����������:����
     */ 
    public static final int COMMAND_ID_TEST = 6;
    /**
     * ����������:�˳�
     */ 
    public static final int COMMAND_ID_EXIT = 7;
    /**
     * ��Ϸ�˵���С
     */ 
    static final int MENU_ITEM_COUNT = 8;
    
    /**
     *  ��Ϸ״̬����:��ʼ��״̬
     * */
    public static final int INITIALIZED = 10;
    /**
     *  ��Ϸ״̬����:������Ϸ��
     * */
    public static final int PLAYING = 11;
    /**
     *  ��Ϸ״̬����:ʤ��״̬
     * */
    public static final int WON = 12;
    
    // ��ϷͼƬ����
    public static final int IndexInItemList_MM = 0;
    public static final int IndexInItemList_DONGMAN = 1;
    public static final int IndexInItemList_CHUANGYI = 2;
    public static final int IndexInItemList_ZIHUA = 3;
    public static final int IndexInItemList_OTHER = 4;
    
    //����
  /*  public static final Font MONOSPACE_PLAIN_SMALL_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public static final Font MONOSPACE_BOLD_SMALL_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
    
    public static final Font MONOSPACE_PLAIN_MEDIUM_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    public static final Font MONOSPACE_BOLD_MEDIUM_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    
    public static final Font MONOSPACE_PLAIN_LARGE_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
    public static final Font MONOSPACE_BOLD_LARGE_Font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
	*/
}