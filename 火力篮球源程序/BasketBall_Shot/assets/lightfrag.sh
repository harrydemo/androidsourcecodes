precision mediump float;
//���մӶ�����ɫ�������Ĳ���
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
void main()                         
{    
   //�����������ɫ����ƬԪ
   vec4 finalColor=vec4(0.9,0.01,0.01,1.0);   
   gl_FragColor = finalColor*vambient+finalColor*vspecular+finalColor*vdiffuse;//����ƬԪ��ɫֵ
}   