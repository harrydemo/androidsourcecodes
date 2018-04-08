precision mediump float;
uniform int uisShadowFrag;//�Ƿ������Ӱ
uniform int uisLanbanFrag;//�Ƿ�������������ϵ���Ӱ
uniform sampler2D usTextureBall;//����������������
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
varying vec4 vfragLosition;
void main()                         
{    
   //�����������ɫ����ƬԪ
   vec4 finalColor;
   if(uisShadowFrag==1)
   {//������Ӱ������������������
    finalColor=vec4(0.3,0.3,0.3,0.2);
   	if(uisLanbanFrag==1)
   	{
      if(vfragLosition.x<-9.6||vfragLosition.x>9.6||vfragLosition.y<4.24||vfragLosition.y>10.5)
      {
        finalColor=vec4(0.3,0.3,0.3,0.0);
      }
    }
     
     gl_FragColor = finalColor;//����ƬԪ��ɫֵ
   }
   else
   {//����������������������
     finalColor=texture2D(usTextureBall, vTextureCoord);
     gl_FragColor = finalColor*vambient+finalColor*vspecular+finalColor*vdiffuse;//����ƬԪ��ɫֵ
   }
}              