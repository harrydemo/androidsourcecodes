package wyf.wpf;			//���������

import java.util.ArrayList;		//���������
import static wyf.wpf.ConstantUtil.*;	//���������

/*
 * ���ฺ�������Ϸ������ƽ�̲㣬�����װ����Ա������ 
 * ͬʱ�ṩ�����������Ϸ�Ĳ���ͨ�����󣬹���ײ���
 */
public class LayerList{
	public ArrayList<Layer> layerList = new ArrayList<Layer>();		//��Ÿ���ƽ�̲�

	public static LayerList getLayerListByStage(int stage){//��̬���������ݲ�ͬ�Ĺؿ����ز�ͬ��LayerLsit����
		LayerList list= new LayerList();
		//��GameData���ж�ȡͼ����Ϣ����װ��Layer������ӵ�ƽ�̲��б���
		for(int i=0;i<GameData.mapData[stage].length;i++){
			Layer layer = new Layer(GameData.mapData[stage][i],GameData.notInData[stage][i]);
			list.layerList.add(layer);
		}		
		return list;
	}
	//�����������ۺ�������ƽ�̲�Ĳ���ͨ������
	public int[][] getTotalNotInMatrix(){
		int[][] result = new int[MAP_ROWS][MAP_COLS];		//�����������ڷ���
		for(int i=0;i<result.length;i++){			//ѭ��ÿһ��
			for(int j=0;j<result[i].length;j++){	//ѭ��ÿһ��
				for(Layer layer:layerList){			//ѭ��ÿһ��ƽ�̲�
					result[i][j] = result[i][j]|layer.notInMarix[i][j];	//�������
				}
			}
		}
		return result;
	}
}