package wyf.ytl;
/**
 * ����Ϊ���ӵ�һ���߷�
 * ������ʲô����
 * ��ʼ���λ��
 * Ŀ����λ��
 * �Լ���ֵʱ���õ���score
 */
public class ChessMove {
	int ChessID;//������ʲô����
	int fromX;//��ʼ������
	int fromY;
	int toX;//Ŀ�ĵص�����
	int toY;
	int score;//ֵ,��ֵʱ���õ�
	public ChessMove(int ChessID, int fromX,int fromY,int toX,int toY,int score){//������
		this.ChessID = ChessID;//���ӵ�����
		this.fromX = fromX;//���ӵ���ʼ����
		this.fromY = fromY;
		this.toX = toX;//���ӵ�Ŀ���x����
		this.toY = toY;//���ӵ�Ŀ���y����
		this.score = score;
	}
}