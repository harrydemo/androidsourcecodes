package yzy.Tank;

public class ConstantUtil {
	public static final int pictureWidth = 36;//��Ԫͼ�Ŀ��
	public static final int pictureCount = 50;//����ͼƬ����
	public static final int screenWidth = 854;
	public static final int screenHeight = 480;
	
	/**
	 * ��������Ϸ�����õ��ķ�����
	 * 0��ֹ��1��, 2���ϣ�3�ң�4���£�5�£�6���£�7��8����
	 */
	public static final int DIR_STOP = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT_UP = 2;
	public static final int DIR_RIGHT = 3;
	public static final int DIR_RIGHT_DOWN = 4;
	public static final int DIR_DOWN = 5;
	public static final int DIR_LEFT_DOWN = 6;
	public static final int DIR_LEFT = 7;
	public static final int DIR_LEFT_UP = 8;
	public static final double BooletSpan = 0.02;//�л����ӵ��ĸ���
	public static final double BooletSpan2 = 0.1;//�ؿڷ��ӵ��ĸ���
	public static final int life = 8;//��ҷɻ�������

}
/*canvas.drawBitmap(gdbj, bjX, 0, null);
bjX=bjX-10;
if(bjX<=-960)
	canvas.drawBitmap(gdbj, 1804+bjX, 0, null);
if(bjX==-1804)
	bjX=0;*/