package cn.com.ldci.plants;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Zombies {
	
	int x =400;//僵尸的坐标
	int y;
	int type;//僵尸的类型
	int life;//生命
	int i=0;
	public boolean status;

	Bitmap[] ZomebieBitmap;
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		
		canvas.drawBitmap(ZomebieBitmap[i], x, y, paint);
		i=(i+1)%8;
	}
	public Zombies(int y,int type,int life){
		this.y=y;
		this.type=type;
		this.life=life;	
		this.status=true;
	}
	public void move(){
		x--;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
