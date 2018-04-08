uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�任����
uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;	//�����λ��
attribute vec3 aPosition;  //����λ��
attribute vec3 aNormal;    //���㷨����
//���ڴ��ݸ�ƬԪ��ɫ���ı���
varying vec4 vambient;
varying vec4 vdiffuse;
varying vec4 vspecular;
void pointLight(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 vambient,			//����������ǿ��
  inout vec4 vdiffuse,				//ɢ�������ǿ��
  inout vec4 vspecular,			//���������ǿ��
  in vec3 ulightLocation,			//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  vambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(ulightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=50.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  vdiffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  vspecular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}



void main()     
{ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��  
   
   vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
   
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,vec4(0.1,0.1,0.1,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   
   vambient=ambientTemp;
   vdiffuse=diffuseTemp;
   vspecular=specularTemp;
}                      