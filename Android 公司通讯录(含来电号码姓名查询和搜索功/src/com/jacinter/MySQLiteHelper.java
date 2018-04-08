/**
 * 
 */
package com.jacinter;
//数据库服务类，主要用来增加号码，创建数据库
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author admin
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    //调用父类构造器  
    public MySQLiteHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        super(context, name, factory, version);  
    }  

	
	/* (非 Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
		db.execSQL("drop table if exists jacnamelist");
        db.execSQL("create table if not exists jacnamelist("  
                + "id integer primary key,"
        		+ "department interger,"
                + "name varchar,"  
                + "telephone varchar,"
                + "finditems varchar)");  
        //db.execSQL("alter table jacnamelist add column finditems varchar");
        db.execSQL("insert into jacnamelist values (null,0,'明珠酒店','05512216688','明珠酒店mingzhumz05512216688')");
        db.execSQL("insert into jacnamelist values (null,0,'古井假日','05512206666','古井假日gujinggj05512206666')");
        db.execSQL("insert into jacnamelist values (null,1,'万豪','05512238810','万豪wanhaowh05512238810')");
        db.execSQL("insert into jacnamelist values (null,1,'塞纳河畔','05513816222','塞纳河畔sainasn05513816222')");
        db.execSQL("insert into jacnamelist values (null,1,'世家商务','05512238008','世家商务shijiasj05512238008')");
        db.execSQL("insert into jacnamelist values (null,1,'齐云山庄','05512286088','齐云山庄qiyunqy05512286088')");
        db.execSQL("insert into jacnamelist values (null,2,'安徽饭店','05512218089','安徽饭店anhuifandianahfd05512218089')");
        db.execSQL("insert into jacnamelist values (null,2,'外经','05513492588','外经大厦waijingwj05513492588')");
        db.execSQL("insert into jacnamelist values (null,2,'蓝宫(早茶)','05512828231','蓝宫(早茶)langong(zaocha)lg(zc)05512828231')");
        db.execSQL("insert into jacnamelist values (null,3,'梦城大酒楼','05512670077','梦城大酒楼mengchengdajiuloumcdjl05512670077')");
        db.execSQL("insert into jacnamelist values (null,3,'香格里拉(中心)','05512645888','香格里拉(中心)xianggelila(zhongxin)xgll(zx)05512645888')");
        db.execSQL("insert into jacnamelist values (null,3,'金满楼(花园酒都)','05512877777','金满楼(花园酒都)jinmanlou(huayuanjiudong)jml(hyjd)05512877777')");
        db.execSQL("insert into jacnamelist values (null,4,'假日快捷','05512252888','假日快捷jiarikuaijiejrkj05512252888')");
        db.execSQL("insert into jacnamelist values (null,4,'丰乐大厦','05512239990','丰乐大厦fengledaxiafldx05512239990')");
        db.execSQL("insert into jacnamelist values (null,4,'三河(蓝波湾)','05515669911','三河酒家(蓝波湾)sanhejiujia(lanbowan)shjj(lbw)05515669911')");
        db.execSQL("insert into jacnamelist values (null,4,'三河(梅山路)','05512843588','三河酒家(梅山路)sanhejiujia(meishanlu)shjj(msl)05512843588')");
        db.execSQL("insert into jacnamelist values (null,4,'三河(望江路)','05513655577','三河酒家(望江路)sanhejiujia(wangjianglu)shjj(wjl)05513655577')");
        db.execSQL("insert into jacnamelist values (null,5,'金满楼(东方海鲜楼)','05512818777','金满楼(东方海鲜楼)jinmanlou(dongfanghaixianlou)jml(dfhxl)05512818777')");
        db.execSQL("insert into jacnamelist values (null,5,'和平','05512282288','和平hepinghp05512282288')");
        db.execSQL("insert into jacnamelist values (null,5,'源牌','05513816778','源牌yuanpaiyp05513816778')");
        db.execSQL("insert into jacnamelist values (null,5,'梦都','05513659977','梦都mengdongmd05513659977')");
        db.execSQL("insert into jacnamelist values (null,5,'炊神','05513814338','炊神chuishencs05513814338')");
        db.execSQL("insert into jacnamelist values (null,6,'绿水雅客','05514399999','绿水雅客lvshuiyakelsyk05514399999')");
        db.execSQL("insert into jacnamelist values (null,6,'凤凰美食城','05513421666','凤凰美食城fenghuangmeishichengfhmsc05513421666')");
        db.execSQL("insert into jacnamelist values (null,6,'和煦园文星阁','05513417988','和煦园文星阁hexuyuanwenxinggehzywxg05513417988')");
        db.execSQL("insert into jacnamelist values (null,7,'金宝酒店','05513442668','金宝酒店jinbaojiudianjbjd05513442668')");
        db.execSQL("insert into jacnamelist values (null,7,'希尔顿','05512808888','希尔顿xidongdongxed05512808888')");
        db.execSQL("insert into jacnamelist values (null,7,'农家乐','05512159999','农家乐nongjialenjl05512159999')");
        db.execSQL("insert into jacnamelist values (null,8,'江南春(南七)','05515669911','江南春(南七)jiangnanchun(nanqi)jnc(nq)05515669911')");
        db.execSQL("insert into jacnamelist values (null,8,'浮庄','05512884888','浮庄fuzhuangfz05512884888')");
        db.execSQL("insert into jacnamelist values (null,8,'状元楼','05512829767','状元楼zhuangyuanlouzyl05512829767')");
        db.execSQL("insert into jacnamelist values (null,9,'粥天粥夜','05514669911','粥天粥夜zhoutianzhouyeztzy05514669911')");
        db.execSQL("insert into jacnamelist values (null,9,'工行严璐璐','05512621123','工行严璐璐gongxingyan璐璐gxyzz05512621123')");
        db.execSQL("insert into jacnamelist values (null,9,'工行徐会计','05512620172','工行徐会计gongxingxuhuijigxxhj05512620172')");
        db.execSQL("insert into jacnamelist values (null,9,'EMS曹师傅','18955176307','EMS曹师傅EMScaoshifuemscsf18955176307')");
        db.execSQL("insert into jacnamelist values (null,10,'DHL','8008108000','DHLDHLdhl8008108000')");
        db.execSQL("insert into jacnamelist values (null,10,'TNT','15955176165','TNTTNTtnt15955176165')");
        db.execSQL("insert into jacnamelist values (null,10,'顺丰','18605512778','顺丰shunfengsf18605512778')");
        db.execSQL("insert into jacnamelist values (null,11,'DHL杨先生','13365519002','DHL杨先生DHLyangxianshengdhlyxs13365519002')");
        db.execSQL("insert into jacnamelist values (null,11,'长途汽车','05512657012','长途汽车changtuqichectqc05512657012')");
        db.execSQL("insert into jacnamelist values (null,12,'飞机票张经理','13966691944','飞机票张经理feijipiaozhangjinglifjpzjl13966691944')");
        db.execSQL("insert into jacnamelist values (null,12,'火车票夏先生','13063480373','火车票夏先生huochepiaoxiahcpxlaoxia13063480373')");
        db.execSQL("insert into jacnamelist values (null,13,'世纪经源','05516868888','世纪经源shijijingyuansjjy05516868888')");
        db.execSQL("insert into jacnamelist values (null,14,'同庆楼庐州府','05515360777','同庆楼庐州府tongqinglouluzhoufutqllzf05515360777')");
        db.execSQL("insert into jacnamelist values (null,15,'同庆楼花园店','05513513777','同庆楼花园店tongqinglouhuayuandiantqlhyd05513513777')");
        db.execSQL("insert into jacnamelist values (null,16,'湘鄂情','05516116777','湘鄂情xiangdongqingxeq05516116777')");
        db.execSQL("insert into jacnamelist values (null,16,'测试号码','15555215556','')");
        db.execSQL("insert into jacnamelist values (null,16,'来电号码','15555215554','')");


	}

	/* (非 Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根

	}

}
