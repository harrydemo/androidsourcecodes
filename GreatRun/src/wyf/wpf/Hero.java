package wyf.wpf;			//���������
public class Hero extends Sprite{
	public Hero(int col, int row){
		super(col, row);
	}
	//�������ж��Ƿ񵽼�,���ռҵ�����ֵ
	public boolean foundHome(int col,int row){
		int hrow = this.row;
		int hcol = this.col;
		if(hrow == row && hcol == col){		//�ж�Ӣ�۵����ĵ����ڵĸ����Ƿ�ͬ�����ڵĸ���һ��	
			return true;
		}
		return false;
	}	
}