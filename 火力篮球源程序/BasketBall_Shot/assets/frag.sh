precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   vec4 cTemp=texture2D(sTexture, vTextureCoord); 
     gl_FragColor = cTemp;
}              