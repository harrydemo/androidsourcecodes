uniform mat4 uMVPMatrix; //�ܱ任����

attribute vec3 aPosition;  //����λ��
uniform int uraodon;//�Ŷ�ֵ

attribute vec2 aTexCoor;    //������������
varying vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
void main()     
{                  
   vec4 rPosition=vec4(aPosition,1);
   float raodonR=0.0;
   if(uraodon==1)
   {
     
     if(rPosition.y<0.46)
     {
       raodonR=sqrt(rPosition.y)/4.0;
       rPosition.x=rPosition.x+raodonR;
     }
     else
     {
       raodonR=sqrt(1.12-rPosition.y)/4.0;
       rPosition.x=rPosition.x+raodonR;
     }
     
   }
   else if(uraodon==3)
   {
     
     if(rPosition.y<0.46)
     {
       raodonR=sqrt(rPosition.y)/4.0;
       rPosition.x=rPosition.x-raodonR;
     }
     else
     {
       raodonR=sqrt(1.12-rPosition.y)/4.0;
       rPosition.x=rPosition.x-raodonR;
     }
     
   }
   
      gl_Position =uMVPMatrix * rPosition; //�����ܱ任�������˴λ��ƴ˶���λ��
   
  
   
   
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      