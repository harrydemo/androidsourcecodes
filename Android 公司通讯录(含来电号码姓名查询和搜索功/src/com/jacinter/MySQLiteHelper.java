/**
 * 
 */
package com.jacinter;
//���ݿ�����࣬��Ҫ�������Ӻ��룬�������ݿ�
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author admin
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    //���ø��๹����  
    public MySQLiteHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        super(context, name, factory, version);  
    }  

	
	/* (�� Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �Զ����ɵķ������
		db.execSQL("drop table if exists jacnamelist");
        db.execSQL("create table if not exists jacnamelist("  
                + "id integer primary key,"
        		+ "department interger,"
                + "name varchar,"  
                + "telephone varchar,"
                + "finditems varchar)");  
        //db.execSQL("alter table jacnamelist add column finditems varchar");
        db.execSQL("insert into jacnamelist values (null,0,'����Ƶ�','05512216688','����Ƶ�mingzhumz05512216688')");
        db.execSQL("insert into jacnamelist values (null,0,'�ž�����','05512206666','�ž�����gujinggj05512206666')");
        db.execSQL("insert into jacnamelist values (null,1,'���','05512238810','���wanhaowh05512238810')");
        db.execSQL("insert into jacnamelist values (null,1,'���ɺ���','05513816222','���ɺ���sainasn05513816222')");
        db.execSQL("insert into jacnamelist values (null,1,'��������','05512238008','��������shijiasj05512238008')");
        db.execSQL("insert into jacnamelist values (null,1,'����ɽׯ','05512286088','����ɽׯqiyunqy05512286088')");
        db.execSQL("insert into jacnamelist values (null,2,'���շ���','05512218089','���շ���anhuifandianahfd05512218089')");
        db.execSQL("insert into jacnamelist values (null,2,'�⾭','05513492588','�⾭����waijingwj05513492588')");
        db.execSQL("insert into jacnamelist values (null,2,'����(���)','05512828231','����(���)langong(zaocha)lg(zc)05512828231')");
        db.execSQL("insert into jacnamelist values (null,3,'�γǴ��¥','05512670077','�γǴ��¥mengchengdajiuloumcdjl05512670077')");
        db.execSQL("insert into jacnamelist values (null,3,'�������(����)','05512645888','�������(����)xianggelila(zhongxin)xgll(zx)05512645888')");
        db.execSQL("insert into jacnamelist values (null,3,'����¥(��԰�ƶ�)','05512877777','����¥(��԰�ƶ�)jinmanlou(huayuanjiudong)jml(hyjd)05512877777')");
        db.execSQL("insert into jacnamelist values (null,4,'���տ��','05512252888','���տ��jiarikuaijiejrkj05512252888')");
        db.execSQL("insert into jacnamelist values (null,4,'���ִ���','05512239990','���ִ���fengledaxiafldx05512239990')");
        db.execSQL("insert into jacnamelist values (null,4,'����(������)','05515669911','���ӾƼ�(������)sanhejiujia(lanbowan)shjj(lbw)05515669911')");
        db.execSQL("insert into jacnamelist values (null,4,'����(÷ɽ·)','05512843588','���ӾƼ�(÷ɽ·)sanhejiujia(meishanlu)shjj(msl)05512843588')");
        db.execSQL("insert into jacnamelist values (null,4,'����(����·)','05513655577','���ӾƼ�(����·)sanhejiujia(wangjianglu)shjj(wjl)05513655577')");
        db.execSQL("insert into jacnamelist values (null,5,'����¥(��������¥)','05512818777','����¥(��������¥)jinmanlou(dongfanghaixianlou)jml(dfhxl)05512818777')");
        db.execSQL("insert into jacnamelist values (null,5,'��ƽ','05512282288','��ƽhepinghp05512282288')");
        db.execSQL("insert into jacnamelist values (null,5,'Դ��','05513816778','Դ��yuanpaiyp05513816778')");
        db.execSQL("insert into jacnamelist values (null,5,'�ζ�','05513659977','�ζ�mengdongmd05513659977')");
        db.execSQL("insert into jacnamelist values (null,5,'����','05513814338','����chuishencs05513814338')");
        db.execSQL("insert into jacnamelist values (null,6,'��ˮ�ſ�','05514399999','��ˮ�ſ�lvshuiyakelsyk05514399999')");
        db.execSQL("insert into jacnamelist values (null,6,'�����ʳ��','05513421666','�����ʳ��fenghuangmeishichengfhmsc05513421666')");
        db.execSQL("insert into jacnamelist values (null,6,'����԰���Ǹ�','05513417988','����԰���Ǹ�hexuyuanwenxinggehzywxg05513417988')");
        db.execSQL("insert into jacnamelist values (null,7,'�𱦾Ƶ�','05513442668','�𱦾Ƶ�jinbaojiudianjbjd05513442668')");
        db.execSQL("insert into jacnamelist values (null,7,'ϣ����','05512808888','ϣ����xidongdongxed05512808888')");
        db.execSQL("insert into jacnamelist values (null,7,'ũ����','05512159999','ũ����nongjialenjl05512159999')");
        db.execSQL("insert into jacnamelist values (null,8,'���ϴ�(����)','05515669911','���ϴ�(����)jiangnanchun(nanqi)jnc(nq)05515669911')");
        db.execSQL("insert into jacnamelist values (null,8,'��ׯ','05512884888','��ׯfuzhuangfz05512884888')");
        db.execSQL("insert into jacnamelist values (null,8,'״Ԫ¥','05512829767','״Ԫ¥zhuangyuanlouzyl05512829767')");
        db.execSQL("insert into jacnamelist values (null,9,'������ҹ','05514669911','������ҹzhoutianzhouyeztzy05514669911')");
        db.execSQL("insert into jacnamelist values (null,9,'��������','05512621123','��������gongxingyan��gxyzz05512621123')");
        db.execSQL("insert into jacnamelist values (null,9,'��������','05512620172','��������gongxingxuhuijigxxhj05512620172')");
        db.execSQL("insert into jacnamelist values (null,9,'EMS��ʦ��','18955176307','EMS��ʦ��EMScaoshifuemscsf18955176307')");
        db.execSQL("insert into jacnamelist values (null,10,'DHL','8008108000','DHLDHLdhl8008108000')");
        db.execSQL("insert into jacnamelist values (null,10,'TNT','15955176165','TNTTNTtnt15955176165')");
        db.execSQL("insert into jacnamelist values (null,10,'˳��','18605512778','˳��shunfengsf18605512778')");
        db.execSQL("insert into jacnamelist values (null,11,'DHL������','13365519002','DHL������DHLyangxianshengdhlyxs13365519002')");
        db.execSQL("insert into jacnamelist values (null,11,'��;����','05512657012','��;����changtuqichectqc05512657012')");
        db.execSQL("insert into jacnamelist values (null,12,'�ɻ�Ʊ�ž���','13966691944','�ɻ�Ʊ�ž���feijipiaozhangjinglifjpzjl13966691944')");
        db.execSQL("insert into jacnamelist values (null,12,'��Ʊ������','13063480373','��Ʊ������huochepiaoxiahcpxlaoxia13063480373')");
        db.execSQL("insert into jacnamelist values (null,13,'���;�Դ','05516868888','���;�Դshijijingyuansjjy05516868888')");
        db.execSQL("insert into jacnamelist values (null,14,'ͬ��¥®�ݸ�','05515360777','ͬ��¥®�ݸ�tongqinglouluzhoufutqllzf05515360777')");
        db.execSQL("insert into jacnamelist values (null,15,'ͬ��¥��԰��','05513513777','ͬ��¥��԰��tongqinglouhuayuandiantqlhyd05513513777')");
        db.execSQL("insert into jacnamelist values (null,16,'�����','05516116777','�����xiangdongqingxeq05516116777')");
        db.execSQL("insert into jacnamelist values (null,16,'���Ժ���','15555215556','')");
        db.execSQL("insert into jacnamelist values (null,16,'�������','15555215554','')");


	}

	/* (�� Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������

	}

}
