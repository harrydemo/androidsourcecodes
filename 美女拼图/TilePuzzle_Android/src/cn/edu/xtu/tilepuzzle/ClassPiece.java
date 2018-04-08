package cn.edu.xtu.tilepuzzle;


import cn.edu.xtu.tilepuzzle.R;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ������
 * */
public class ClassPiece {
	boolean isblank=false;
    Bitmap bitmap;
    boolean addString=false;//�Ƿ���ÿ�����������ͼƬ��ԭʼλ��
    /**����Ŀ�*/
    float  cellWidth;
    /**����ĸ�*/
    float  cellHeight;
    /**
     * �����ˮƽƫ����
     * */
    float cellOffset_x=0;
    /**
     * ����Ĵ�ֱƫ����
     * */
    float cellOffset_y=0;
    float  spaces=1;
    
    int serial; // serial number for ordering
    //����ĳ�ʼ��λ��
    int ix; // initial location in grid coordinates
    int iy; // initial location in grid coordinates
    //����ĵ�ǰλ��
    public int x; // current location in grid coordinates
    public int y; // current location in grid coordinates
    /**
     * @param bitmap  ͼƬ
     * @ser	int	ͼƬ�������к�
     * @nx	int	����ĳ�ʼ��λ��
     * @ny	int ����ĳ�ʼ��λ��
     * @cellWidth	����Ŀ�
     * @cellHeight	����ĸ�
     * @cellOffset_x	�������ԭ���ˮƽƫ����
     * @cellOffset_y	�������ԭ��Ĵ�ֱƫ����
     * */
    ClassPiece(Bitmap bitmap_,int ser, int nx, int ny,float  cellWidth,float  cellHeight,float cellOffset_x,float cellOffset_y,float spaces) {
    	this.cellOffset_x=cellOffset_x;
    	this.cellOffset_y=cellOffset_y;
    	bitmap=bitmap_;
        serial = ser;
        x = ix = nx;
        y = iy = ny;
        this.cellHeight=cellHeight;
        this.cellWidth=cellWidth;
        this.spaces=spaces;
    }
    
    void setImg(Bitmap bitmap_){
        bitmap=bitmap_;
    }
    void setLocation(int nx, int ny) {
        this.x = nx;
        this.y = ny;
    }
    
    
    // assumes background is white
    /**
     * @canvas ����
     * @strHeight �ַ����߶�
     * */
    public void paint(Canvas canvas,int strHeight) {
    	float px = y * cellWidth; //������
    	float py = x * cellHeight; //������        
        canvas.drawBitmap(
        		bitmap, 
        		cellOffset_x+spaces+px, 
        		cellOffset_y+spaces+py,
        		new Paint());
       if(isblank){    	   
    	   canvas.drawText(
					"�ո�", 
					cellOffset_x+spaces+px + (bitmap.getWidth() - ClassPaint.paintAddStr.measureText("�ո�")) / 2, 
					cellOffset_y+spaces+py + (bitmap.getHeight()) / 2, 
					ClassPaint.paintAddStr);
       }
       else {
			if (addString) {
				String str = (1 + this.ix) + " , " + (1 + this.iy);
				canvas.drawText(
						str, 
						cellOffset_x+spaces+px + (bitmap.getWidth() - ClassPaint.paintAddStr.measureText(str)) / 2, 
						cellOffset_y+spaces+py + (bitmap.getHeight()) / 2, 
						ClassPaint.paintAddStr);
			}
		}
    }
    boolean isHome() {
        return (x == ix) && (y == iy);
    }
    
    /**
     * �ѵ�ǰ��IX,IY�����ƶ�����ʼ��x,y����
     * */
    public void goHome() {
        this.x=ix;
        this.y=iy;
    }
    
    public float getCellWidth() {
        return cellWidth;
    }
    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }
    public float getCellHeight() {
        return cellHeight;
    }
    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }
}
