package com.bn.d2.bill; 
//������ײ������������Ĺ�����
public class CollisionUtil
{
	//������ƽ�������ĵ��
	public static float dotProduct(float[] vec1,float[] vec2)
	{
		return
			vec1[0]*vec2[0]+
			vec1[1]*vec2[1];		
	} 	
	//��ƽ��������ģ
	public static float mould(float[] vec)
	{
		return (float)Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]);
	}
	
	//������ƽ�������ļн�
	public static float angle(float[] vec1,float[] vec2)
	{
		//������
		float dp=dotProduct(vec1,vec2);
		//��������������ģ
		float m1=mould(vec1);
		float m2=mould(vec2);
		
		float acos=dp/(m1*m2);
		
		//Ϊ�˱������������������
		if(acos>1)
		{
			acos=1;
		}
		else if(acos<-1)
		{
			acos=-1;
		}
		//���ص��ǻ���
		return (float) Math.acos(acos);
	}
	//����������ƽ��
	public static float calcuDisSquare(float[] p1,float[] p2)
	{
		return (p1[0]-p2[0])*(p1[0]-p2[0])+(p1[1]-p2[1])*(p1[1]-p2[1]);
	}
	//����������Ƿ���ײ�ķ���
	public static boolean isTwoBallsCollided(float ballaTempXY[],Ball ballb){
		float BAx=ballaTempXY[0]-ballb.getX();
		float BAy=ballaTempXY[1]-ballb.getY();		
		//������������ģ
		float mvBA=mould(new float[]{BAx,BAy});	
		return (mvBA<Ball.d);		
	}
	//�����������ײ�������ķ���
	public static boolean collisionCalculate(float ballaTempXY[],Ball balla,Ball ballb)
	{		
		/* 
		 * ����ײֱ������B->A ��Ҳ��������������ײ�������������ߵ���������ע����������
		 * 1�����ﲻ��������B->A��A->B��ԭ���ǣ���н�ʱ��������õ�cosֵ��һ���ģ�
		 * ��sinֵû���õ�����ֱ�������ߵ��ٶȷ����õ��������ļ�����������sinֵ
		 * 2��Ҫ��֤��������Ϊ���������������λ�ò������غϣ���Ϊ���滹Ҫ����ĸ��������⣡��
		 * 
		 * �÷���û�иı����λ�ã�ֻ�ı�����ٶȡ�
		 * ע���ʱ��x,y�������������꣬������������������ϽǶ������꣡������������������
		 */	
		float BAx=ballaTempXY[0]-ballb.getX();
		float BAy=ballaTempXY[1]-ballb.getY();
		
		//������������ģ
		float mvBA=mould(new float[]{BAx,BAy});	
		
		//�����������ڵ�����ֱ����û����ײ
		if(mvBA>Ball.d){			
			return false;
		}
		/*
		 * ���������ײ���㷨Ϊ��
		 * 1�����������������ߵ�����
		 * 2����ÿ������ٶȷֽ�Ϊƽ���봹ֱ��������������
		 * 3��������ȫ������ײ��֪ʶ����ײ��ƽ�����������ٶ����򽻻�����ֱ���������ٶȲ���
		 * 4��������װ��������ٶȼ���
		 * vax=vax��ֱ+vbxƽ��    vay=vay��ֱ+vbyƽ��    
		 * vbx=vbx��ֱ+vaxƽ��    vby=vby��ֱ+vayƽ��    
		 */
		
	    //�ֽ�B���ٶ�========================================begin=============	
		
		//��b����ٶȴ�С
		float vB=(float)Math.sqrt(ballb.vx*ballb.vx+ballb.vy*ballb.vy);
		//ƽ�з����XY���ٶ�
		float vbCollX=0;
		float vbCollY=0;
		//��ֱ�����Xy���ٶ�
		float vbVerticalX=0;
		float vbVerticalY=0;
		
		//�����ٶ�С����ֵ����Ϊ�ٶ�Ϊ�㣬���ý��зֽ������
		if(balla.vMin<vB)
		{
			//��B����ٶȷ�����������ײֱ������B->A�ļн�(����)
			float bAngle=angle
			(
				new float[]{ballb.vx,ballb.vy},
			    new float[]{BAx,BAy}
			);
			
			//��B������ײ������ٶȴ�С
			float vbColl=vB*(float)Math.cos(bAngle);
			
			//��B������ײ������ٶ�������ע��Ҫ��֤mvBA��Ϊ�㣡��
			vbCollX=(vbColl/mvBA)*BAx;
			vbCollY=(vbColl/mvBA)*BAy;
			
			//��B������ײ��ֱ������ٶ�����
			vbVerticalX=ballb.vx-vbCollX;
			vbVerticalY=ballb.vy-vbCollY;
		}
		//�ֽ�B���ٶ�========================================end===============
		
		//�ֽ�A���ٶ�========================================begin=============	
		
		//��a����ٶȴ�С
		float vA=(float)Math.sqrt(balla.vx*balla.vx+balla.vy*balla.vy);
		//ƽ�з����Xy���ٶ�
		float vaCollX=0;
		float vaCollY=0;
		//��ֱ�����Xy���ٶ�
		float vaVerticalX=0;
		float vaVerticalY=0;
		
		//�����ٶ�С����ֵ����Ϊ�ٶ�Ϊ�㣬���ý��зֽ������
		if(balla.vMin<vA)
		{
			//��A����ٶȷ�����������ײֱ������B->A�ļн�(����)
			float aAngle=angle
			(
				new float[]{balla.vx,balla.vy},
			    new float[]{BAx,BAy}
			);			
			
			//��A������ײ������ٶȴ�С
			float vaColl=vA*(float)Math.cos(aAngle);
			
			//��A������ײ������ٶ�����
			vaCollX=(vaColl/mvBA)*BAx;
			vaCollY=(vaColl/mvBA)*BAy;
			
			//��A������ײ��ֱ������ٶ�����
			vaVerticalX=balla.vx-vaCollX;
			vaVerticalY=balla.vy-vaCollY;
		}
		//�ֽ�A���ٶ�========================================end===============
		
		//����ײ��AB����ٶ�
		//����˼��Ϊ��ֱ�����ٶȲ��䣬��ײ���������ٶȽ�������ֱ�����ٶȲ���
		balla.vx=vaVerticalX+vbCollX;
		balla.vy=vaVerticalY+vbCollY;
		
		ballb.vx=vbVerticalX+vaCollX;
		ballb.vy=vbVerticalY+vaCollY;	
		
		//========================================
		//�˴����ò���������ײ�����Ĵ���
		//�˴����ò���������ײ�����Ĵ���
		//�˴����ò���������ײ�����Ĵ���
		//========================================
		System.out.println("ball aaaaaaaaaaaaaaaaaaaaaaaaaaaa "+balla.vx+" ======= "+balla.vy);
		System.out.println("ball bbbbbbbbbbbbbbbbbbbbbb "+ballb.vx+" ======= "+ballb.vy);
		return true;
	}	
}
   
