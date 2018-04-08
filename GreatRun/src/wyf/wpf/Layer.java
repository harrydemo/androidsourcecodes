package wyf.wpf;			//���������
import static wyf.wpf.ConstantUtil.*;	//����ConstantUtil��ľ�̬����
/*
 * ÿһ��Layer�������һ��ͼ�㣬�����а���ͼ��ĵ�ͼ����
 * �͸�ͼ��Ĳ���ͨ������
 */
public class Layer{
	MyDrawable [][] mapMatrix;		//���ƾ���
	int [][] notInMarix;	//ͨ���Ծ���
	//������
	public Layer(int[][] mapMatrix,int[][] noThroughMatrix){
		this.mapMatrix = getMyDrawableMatrix(mapMatrix);
		this.notInMarix = noThroughMatrix;
	}
	//���������ݵ�ͼ���ݴ���һ��MyDrawable����
	public static MyDrawable [][] getMyDrawableMatrix(int [][] mapMatrix){
		MyDrawable [][] result = new MyDrawable[MAP_ROWS][MAP_COLS];
		for(int i=0;i<MAP_ROWS;i++){
			for(int j=0;j<MAP_COLS;j++){
				switch(mapMatrix[i][j]){		//�жϸ�λ�õ�ͼԪ���
				case 1:		//·
					result[i][j] = new MyDrawable(0, TILE_SIZE);
					break;
				case 2:		//�ݵ�     
					result[i][j] = new MyDrawable(1, TILE_SIZE);
					break;
				case 3:		//ľ׮
					result[i][j] = new MyDrawable(2, TILE_SIZE);
					break;
				case 4:		//��
					result[i][j] = new MyDrawable(3, TILE_SIZE*2);
					break;
				case 5:		//��
					result[i][j] = new MyDrawable(4, TILE_SIZE);
					break;
				}
			}
		}		
		return result;
	}
}