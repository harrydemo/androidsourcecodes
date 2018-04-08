package wyf.wpf;							//���������
import static wyf.wpf.ConstantUtil.*;		//���������
import android.content.res.Resources;		//���������
import android.graphics.Bitmap;				//���������
import android.graphics.BitmapFactory;		//���������
import android.graphics.Canvas;				//���������
import android.graphics.Paint;				//���������
/*
 * �����ṩһЩ��̬�����ͳ�Ա����װ����Ϸ�õ�������ͼƬ��Դ����ֹ������й��ͳһ��������ά��
 */
public class BitmapManager{
	private static Bitmap[] currentStage;//��ŵ�ǰ�ؿ���ͼƬ��Դ
	private static Bitmap[] welcomePublic;//���ϵͳ��Ҫ�õ���ͼƬ��Դ
	private static Bitmap[] gamePublic;	//�����Ϸ����Ҫ�õ���ͼƬ��Դ
	private static Bitmap[] systemPublic;	//���ϵͳҪ�õ���ͼƬ
	private static int [][] heroAniList;	//���Ӣ�۵Ķ������б�
	
	
	//����������
	private BitmapManager(){
	}
	//����������ϵͳ�õ���ͼƬ��Դ
	public static void loadSystemPublic(Resources r){
		systemPublic = new Bitmap[SYSTEM_BITMAP_LENGTH];
		systemPublic[0] = BitmapFactory.decodeResource(r, R.drawable.loading);
	}
	//���������ػ�ӭ����Ҫ�õ���ͼƬ��Դ
	public static void loadWelcomePublic(Resources r){
		welcomePublic = new Bitmap[WELCOME_BITMAP_LENGTH];
		welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back);//��ӭ���汳��
		}
	//���������عؿ�ͼƬ����ÿһ�ؿ�ʼǰ���ã�������0��ʼ
	public static void loadCurrentStage(Resources r,int stage){
		currentStage = null;
		currentStage = new Bitmap[STAGE_BITMAP_LENGTH[stage]];
		switch(stage){
		case 0://��һ�ص�ͼͼƬ
			currentStage[0] = BitmapFactory.decodeResource(r, R.drawable.road_1);//��һ��·
			currentStage[1] = BitmapFactory.decodeResource(r, R.drawable.grass_1);//��һ�زݵ�
			currentStage[2] = BitmapFactory.decodeResource(r, R.drawable.stake_1);//��һ��ľ׮
			currentStage[3] = BitmapFactory.decodeResource(r, R.drawable.tree_1);//��һ����
			currentStage[4] = BitmapFactory.decodeResource(r, R.drawable.flower_1);//��һ�ػ� 		
			break;
		
		}
	}
	//��������Ϸ�����еĹ���ͼƬ����Ӣ��,�ҵ�
	public static void loadGamePublic(Resources r){
		gamePublic = new Bitmap[GAME_BITMAP_LENGTH];
		gamePublic[0] = BitmapFactory.decodeResource(r, R.drawable.game_pass);//��Ϸ����ͼƬ		
		
		Bitmap tmp = BitmapFactory.decodeResource(r, R.drawable.hero);		//����Ӣ�۴�ͼƬ 
		heroAniList = cutBitmap(tmp,gamePublic,3,8,2);						//�и�Ӣ�۴�ͼƬ
		
		gamePublic[35] = BitmapFactory.decodeResource(r, R.drawable.help_view);//����ͼƬ
		gamePublic[36] = BitmapFactory.decodeResource(r, R.drawable.home);		//���ؼ�ͼƬ
	}
	//���������������������и��ͼ
	public static int [][] cutBitmap(Bitmap source,Bitmap [] result,int start,int rows,int cols){
		int [][] aniList = new int[rows][cols];
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				gamePublic[start+i*cols+j] = Bitmap.createBitmap(source, j*SPRITE_WIDTH, i*SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
				aniList[i][j] = start+i*cols+j;		//�洢ͼƬ���±꣬��ID
			}
		}
		return aniList;
	}
	//����������ͼƬID����ϵͳ����ͼƬ
	public static void drawSystemPublic(int imgId,Canvas canvas,int x,int y,Paint paint){
		canvas.drawBitmap(systemPublic[imgId], x, y, paint);
	}
	//����������ͼƬID����ϵͳ��ӭ����ͼƬ
	public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint){
		canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
	//����������ͼƬID������Ϸ�еĹ���ͼƬ
	public static void drawGamePublic(int imgId,Canvas canvas,int x,int y){
		canvas.drawBitmap(gamePublic[imgId], x, y, null);
	}
	//����������ͼƬID���ƹؿ�ͼƬ
	public static void drawCurrentStage(int imgId,Canvas canvas,int x,int y){
		canvas.drawBitmap(currentStage[imgId], x, y, null);
	}
	//���������Ӣ�۵Ķ������б�
	public static int[][] getHeroFrmList() {
		return heroAniList;
	}
	
}