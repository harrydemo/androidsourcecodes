package wyf.wpf;			//���������
import android.graphics.Bitmap;		//���������
import android.graphics.Canvas;		//���������
import android.graphics.Color;		//���������
import android.graphics.Paint;			//���������
/*
 * ����Ϊ�Զ����gallery��Ϊʵ��Gallery��Ч��
 */
public class CustomGallery{
	Bitmap [] bmpContent;		//GalleryҪ��ʾ������ͼƬ
	int length;					//GalleryҪ��ʾ��ͼƬ�����С
	int currIndex;				//��ǰ����ʾ��ͼƬ������
	int startX;					//����Galleryʱ�����Ͻ�����Ļ�е�X����
	int startY;					//����Galleryʱ�����Ͻ�����Ļ�е�Y����
	int cellWidth;				//ÿ��ͼƬ�Ŀ��
	int cellHeight;				//ÿ��ͼƬ�ĸ߶�	
	//����������ʼ����Ҫ��Ա����
	public CustomGallery(int startX,int startY,int cellWidth,int cellHeight){
		this.startX = startX;
		this.startY = startY;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}	
	public void setContent(Bitmap [] bmpContent){	//������ΪGallery������ʾ����
		this.bmpContent = bmpContent;
		this.length = bmpContent.length;
	}	
	public void setCurrent(int index){				//���������õ�ǰ��ʾ��ͼƬ
		if(index >=0 && index < length){
			this.currIndex = index;
		}
	}	
	public void drawGallery(Canvas canvas,Paint paint){//�����������Լ�
		//���������Ļ���
		Paint paintBack = new Paint();
		paintBack.setARGB(220, 99, 99, 99);	
		//�����߿�Ļ���
		Paint paintBorder = new Paint();   
		paintBorder.setStyle(Paint.Style.STROKE);   
		paintBorder.setStrokeWidth(4.5f);
		paintBorder.setARGB(255, 150, 150, 150);
		//����ߵ�ͼƬ
		if(currIndex >0){
			canvas.drawRect(startX, startY, startX+cellWidth, startY+cellHeight, paintBack);	//����
			canvas.drawBitmap(bmpContent[currIndex-1], startX, startY, paint);					//��ͼƬ			
			canvas.drawRect(startX, startY, startX+cellWidth, startY+cellHeight, paintBorder);	//�����ͼƬ�ı߿�
		}		
		//����ѡ�е�ͼƬ
		canvas.drawRect(startX+cellWidth, startY, startX+cellWidth*2, startY+cellHeight, paintBack);	//����
		canvas.drawBitmap(bmpContent[currIndex], startX+cellWidth, startY, paint);						//��ͼƬ	
		//���ұߵ�ͼƬ
		if(currIndex<length-1){
			canvas.drawRect(startX+cellWidth*2, startY, startX+cellWidth*3, startY+cellHeight, paintBack);	//����
			canvas.drawBitmap(bmpContent[currIndex+1], startX+cellWidth*2, startY, paint);					//��ͼƬ			
			paintBorder.setARGB(255, 150, 150, 150);														//���ұ�ͼƬ�ı߿�
			canvas.drawRect(startX+cellWidth*2, startY, startX+cellWidth*3, startY+cellHeight, paintBorder);
		}
		//��ѡ�еı߿�	
		paintBorder.setColor(Color.RED);		
		canvas.drawRect(startX+cellWidth, startY, startX+cellWidth*2, startY+cellHeight, paintBorder);
	}	
	public void galleryTouchEvnet(int x,int y){		//������Gallery�Ĵ������¼�����
		if(x>startX && x<startX+cellWidth){						//�������������ͼƬ
			if(currIndex > 0){					//�жϵ�ǰͼƬ����߻���û��ͼƬ
				currIndex --;					//���õ�ǰͼƬΪ��ߵ�ͼƬ
			}
		}
		else if(x>startX+cellWidth*2 && x<startX+cellWidth*3){		//�������ұ�����ͼƬ
			if(currIndex < length-1){			//�жϵ�ǰͼƬ���ұ߻���û��ͼƬ
				currIndex++;					//���õ�ǰͼƬΪ�ұߵ�ͼƬ
			}
		}
	}
}