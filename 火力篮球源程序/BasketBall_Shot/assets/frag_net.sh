precision mediump float;
varying vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   vec4 cTemp=texture2D(sTexture, vTextureCoord); 
   
   if(cTemp.r==0.0&&cTemp.g==0.0&&cTemp.b==0.0||cTemp.a==0.0)
   {
   		gl_FragColor=vec4(0.3,0.3,0.3,0.0);
   }
   else
   {     
    gl_FragColor = cTemp;
   }
}              