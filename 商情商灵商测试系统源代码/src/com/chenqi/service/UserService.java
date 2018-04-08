package com.chenqi.service;

import java.util.ArrayList;
import java.util.List;

import com.chenqi.domain.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {
	private DBOpenHelper dbOpenHelper;
	
	public UserService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	/*public void payment(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();//��������
		try{
			db.execSQL("update person set amount=amount-10 where personid=?", new Object[]{1});
			db.execSQL("update person set amount=amount+10 where personid=?", new Object[]{2});
			db.setTransactionSuccessful();//���������־Ϊ�ɹ�������������ʱ�ͻ��ύ����
		}finally{
			db.endTransaction();
		}
	}*/
	
	public void save(User user){
		//���Ҫ�����ݽ��и��ģ��͵��ô˷����õ����ڲ������ݿ��ʵ��,�÷����Զ���д��ʽ�����ݿ�
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into userinfo (username,pwd,sex,age,telephone) values(?,?,?,?,?)",
				new Object[]{user.getUsername(),user.getPwd(),user.getSex(),user.getAge(),user.getTelephone()});
	}
	
	public String  findTedianByShengxiaoName(String name){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from shengxiao where shengxiaoname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianByxingzuoName(String name){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from xingzuo where xingzuoname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianByxuexingName(String name){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from xuexing where xuexingname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianBytwoxingzuo(String nanxingzuo,String nvxingzuo){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from peidui where nanxingzuo=? and nvxingzuo=?", new String[]{nanxingzuo.trim(),nvxingzuo.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public void insertPeidui(){
		//���Ҫ�����ݽ��и��ģ��͵��ô˷����õ����ڲ������ݿ��ʵ��,�÷����Զ���д��ʽ�����ݿ�
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		/*db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","������","�������Ļ���,��������������ů��һ������.���ؾɺþ�����,��ȷ�����������°��ֵĽŲ�,�������ĵ绰�����!Ҳ����һ�������������.��������ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��ţ��","���ר�Ƶ����ˣ�������Լ�ֱ�����������һ�������Ҫ�㴩�����Ըе��·�ȥ����ʱ����������Ҳ��Ը���ܣ��������ʹ˸��ա�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","˫����","�ڴ��ϣ�������һ���������쾪ϲ���������գ�������ר�ƣ����㲻ϲ�ܾ����ĸ��ԣ��Ʊ�Ҫ���ͻ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��з��","���Ǹ�ר�Ƶı�����ϲ�����֣���ȴ�Ժ��񾲡����ʼȲ���ͬ����ʹ����¶��������һ�棬��Ҳ�����޷��˵��뿪����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ʨ����","Ϸ�绯��������������̣����ʳ̶Ⱦ��������κΰ������վ磬����ͬ�������������ҵİ����ڴ�����Ȼ�����������ƻ���ƽ�����򡭡�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��Ů��","���������뼤�������֮�У������ǿ�籩���Ƣ�������𾪣������ܲ��˵Ļ������ϲ�����ϰ��--������Ͱ�ǣ����������޿��̡�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","�����","���Ǹ�������ö�����������������֣����ǵ�ȷ�й����ȵ�һ���飬������ר�ƣ�����ֻ������ղ��ڼ����С���������ָ����5�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��Ы��","�����ϸˮ���������飬��ȴ�嶯�������������������Գ���֮�أ���������ԣ���������Գ���֮�ḡ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","������","������һ�������������ɣ����������ֶ����������գ�ֻҪ���ۼ����������ǿ���һֱ�ദ��졣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ħ����","��Ȼ������Ұ�ģ�������ʵ����ȷ�ϲ�������ʵ����뵽���滹�����ҽ������ʦ���Ŷӣ���ʵ��û�ն�������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ˮƿ��","���������������һ����������ﶼ�ܱ��ָ߶ȵ���Ȥ����㵹�����㲻ı���ϡ������갮�����ϵ�ͷ������˯�����磬ʵ�����˲��ҹ�ά��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","˫����","���ĳ嶯���������ԣ�ʵ���������������ϸ���ԭ����������βݲ�����֮���㿪ʼ��ʡ����һ���Ƿ�ֵ�ã���Ȼ���Ƿ񶨵ġ���������ָ����2�쳤�ؾ�ָ����1"});*/
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","˫����","�����Ǽǵð���ѱ�������ʳ��������̫�ж��ˣ��������ҵ�һ������ȫ�չ�����ˡ��ڴ��ϣ����ǵĲ������൱һ�¡�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","ˮƿ��","������ȫ��ѭ���Զ��ɵĶ��ϲ����Ϥ���̶���ģʽ�����������ʽ�����Ż��ˡ���������ָ����3�쳤�ؾ�ָ����2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","ħ����","�����ȵĸ��Ե�ȷץ��ס�������Ĳ������ڴ��ϣ�������ǳ����ϣ���������Ϊ���󷹴�ɨ��ֻҪż����Ҳ�������ӻ����ݼ��ɡ���������ָ����5�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","������","���ƶʡ���ִ�����Ҽ��������һ���Ĺ��ɡ�������������������������������ǣ�һ��Ҫ�ڴ��ϡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","��Ы��","�����ض������ĸ���������ж���"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","�����","���԰����棬���ǵ�ȷ�кܺõĻ����������ǱϾ��Ƿ���������ͬ������ˣ���ϣ��������һ���Ĺ��ɣ���ȴһ�����䣬������ǿ��Ա˴˻������£��������п�Ϊ����������ָ����4�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","��Ů��","ֻҪ˳������ë������������ȫ����ס���������Ӹɾ��ĳ����ʹ��̣�������㲻ı���ϡ�����һ�ԣ�����ʲô���ɿ������أ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","ʨ����","���Ǹ�ռ����ǿ�����Ĺ�������Ǯ����ˮ����Ȼ�ڴ��������������ϵ�������覣�������֮�⣬���޹�ͨ�㡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","��з��","�������������ͻ��ʳ�һ�ԡ�˭�ں�������ϲ�����ڵ���ǰ˯���أ���������Ҳ����߿����ӱ����÷�����㡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","˫����","��ϲ�����Ե���������ȴ�������յľ�Ӫ�����������ܾ����ʱ���㷢��������������ϵ��ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","��ţ��","��ϲ����⿣����Ǹ����ң��������������ޱȡ���С���Ｋ�ط�����û��Ǳ��֡��е�ճ�ֲ�̫ճ���ľ��롣��������ָ����5�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��ţ��","������","�������Ļ���,��������������ů��һ������.���ؾɺþ�����,��ȷ�����������°��ֵĽŲ�,�������ĵ绰�����!Ҳ����һ���������������"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","˫����","����˫����һ��һ�����Ĳ���Ļ������Ⲣ��һ����ʾ�����ܷ�չ�����˹�ϵ��������������Σ����ǻ��Ǳ˴���ѵ��������̵�ʦ����������ָ����2�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ˮƿ��","�����������ĵ�������飬�������ϸ��������Ȼ�Ķ���������Ҫ�ܴ�����ҿռ䣬Զ������������ͷ����ɣ���������ָ����3�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ħ����","��ϲ�����飬����Ҳ�������ܱ�׷�����Ȥ����������������壬���Ƕ�����Ͷ�ϵ�һ�ԣ�ֻҪ������������ר�飬���ǻ��൱���ۡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","������","��Ȼ���ǵĸ�����ԯ���ޣ�����������֮��Ĳ��죬���˶�����Ĭ�ľ��񿴴�����Ȼ���������ˣ�������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��Ы��","���Ƕ������Ͽ��﹵ͨ���ˣ���������ʹ����������������԰����棬���и����ܵ���ϵ����������ָ����5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","�����","������һ��η�ڳ�ŵ����ֻҪ���ڽ����У����������þ���˼�ֺ��㡣�������ϴ��������˻�ζ����������ְɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��Ů��","�зḻ���뷨��������ϸ��ĸ��飬����˵�Ƶ����������Ǻ�������������������Ȼ�޷��������أ�ȴ��һ��ֵ�û�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ʨ����","��ϲ�����飬����Ҳ�������ܱ�׷�����Ȥ����������������壬���Ƕ�����Ͷ�ϵ�һ�ԣ�ֻҪ������������ר�飬���ǻ��൱���ۡ���������ָ����5�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��з��","�����������ĵ�������飬�������ϸ��������Ȼ�Ķ���������Ҫ�ܴ�����ҿռ䣬Զ������������ͷ����ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","˫����","����˫����һ��һ�����Ĳ���Ļ������Ⲣ��һ����ʾ�����ܷ�չ�����˹�ϵ��������������Σ����ǻ��Ǳ˴���ѵ��������̵�ʦ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","������","�����ް�,����˼����,���г�ֵĹ�ͨ,ȴ����һ�㡰���鼤�ء�,������г,ȴ������չ��һ�δ����ꡣ"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��ţ��","��λ����������⽻���ţ��ƺ���Զ�������ң��Ӳ�����Ů�̡������á�·�ϵ�Ů�������ƺ���ÿ���˶�̫�ĵ����ˡ����Ƿֵ�����ɣ�"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","˫����",""});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","ˮƿ��","������ԣ���ʵ����̫ɢ���ˣ�����˵�öࡢ�����١��úõغ���˵�ټ��ɣ��Ͼ���������̫����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","ħ����","���еľ�з���ܲ����㲻�������������ʵ�ϣ���̫���������ˣ���θ���������ú������ڸ����ϣ�������־���ʤ����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","������","ϲ���չ��˵ľ�з����߾������غǻ��㣬��������ԣ�����ʵ̫ճ���ˣ����ǵ�������Գ�����Լһ���£����������ɾͲ���ô�ֹ��ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","��Ы��","������ͬ����˼ϸ�壬���˹����������ǳ��������컵�һҹ,����ӵ�������������������,����֪��,�����Ǳ˴�Ī����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","�����","�����Ǳ����ϻ���������һ�ԣ������ڴ��ϼ���Ǯ�ϵĿ���ʩ�룬�������崺�硣���ԣ���ʲô�õ��ĵ��أ���������ָ����3�쳤�ؾ�ָ����2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","��Ů��","�������ԣ�������и�ɻ����������ˣ�������ԣ�������ƽ������ķ������ӡ����ǣ����������ĸж����㣬���������ǳ����þá���������ָ����4�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","ʨ����","��Ը����Թ�޻ڵĺǻ��㣬�����Ȼ��ճ���㣬��ɲ��С���ʱ���ۣ��������ᣬ�������������ѧ�ᡰ�����밮����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","��з��","����һ�������ҹ˼ң���С�����ǵ��������˵��˱˴ˡ��������������Ƕ����㹻�����Ļ���Ϊ�Է����ˡ���������ָ����4�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","˫����","�������кͶԼҵĿ���ʹ���ǵ������ս��ȶ�������ȴ�����ز���һ�ֿ���Ϣ�ĸо������Ƿ��ְɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","������","��ȫ�С��ȶ���������ʵľӼһ���������������ͬ����ġ���Ȼ�����ƶʶ���������д��ۣ���ֻҪ��Ӯ�����������Σ���ᷢ������һ������������ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��з��","��ţ��","����ʱ���ͻ�������,��ʱȴ�ֲ�����Ӱ.�������,��������ĸо�,���˷�ʱ�䴧��������˼��,�뿪����! "});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","˫����","��Ĭ����Ȥ���ԸУ����ȷ�����Ϊ��������������������͸�����˼�������ܹ��ݡ�����õĶԲߡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","ˮƿ��","����Ⱥ�����������Ķ��������뽻��֮����ᷢ�����Ͼ���������ȫ��һ�����ˣ����Ǿ�������¯��ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","ħ����","���ǳ�����������������㣬�������Ƥ�������������ԡ���ϧ����������˵����̫�����˵㣬�����ᷢ�֣���ֻ�������������Ƶ�è������������Ŀ����Զ��ʨ��������������ָ����5�쳤�ؾ�ָ����2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","������","���Ǹ����Լ���عܵ��������������٣������ǫѷ����������������������ܳ�����ã����ǵ�����൱��Ȥ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","��Ы��","����һ�������飬һ���س徢ʮ�㣬ֻҪ�ڼ�������㹻�ľ��Ӻ��³������ǵ����֤�������䡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","�����","���Ŀ�Ұ���飬������˵�Ǻ��������ջ�һ���㿪ʼ������������������ͻῪʼ���ר�ƣ�һ��Ҳ�����ˡ���������ָ����3�쳤�ؾ�ָ����2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","��Ů��","����ռ������������������飬���ǵĴ����������չ�������ݱ���������ղ��ܡ��������ܺ����ദ�Ŀ���ʱ��ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","ʨ����","�ڴ��ϣ���ı��������е�����˼�飬��Ȼ���Ǻ�����ľ��飬��������Ӣ�۵�����˵����ȷ�С������������˶����ĸп���"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","��з��","��Ұ�ġ������Ը������ʨ�ӣ�ӵ�������������������ʣ���ѡ����ǰ���ǵ��ȿ�������Ƣ�������ӡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","˫����","��Ұ������������������������������������ٳٲ���飬���û���ĵ�����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","������","���ܸ�����󼶵Ĵ����������ƺ��ܶ��������е����£�����ˮ���򣬊�Ҳ֪�����Ǌ��������ɾ���"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ʨ����","��ţ��","��ϲ������һ�У���ᷢ���������ɾ��񼫲�����Լ��º󶼾������Ρ�����һ������ᣬ�������ɣ�"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","˫����","�������θ����ԣ����㽫������ᵽ�����Ŀ��֡�����ʵ�����ϣ����ľ��ڷ�������������飬�ܽ�ϳ����ص���Ȥ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","ˮƿ��","����ҹ���ֵ����ʽ����������̾�Բ��������侲����������ĺ�ɫ��Ĭ����������ά�ּ��ܵ��ȶȣ�̫�þͲ������ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","ħ����","����һ�������ȶ��������ǲ����ϻ�����ϣ���������ͬ�ʹ����һ�ԣ�ֻҪ����һ������û�п˷����˵��ѹء�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","������","�����������ҵĴ�����ü���������������ʱ���˷��ڴ�ɨ��ʵ��̫��������һ���Գ����У��������ԣ�Ҳίʵ̫�����˵㡣��������ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","��Ы��","�����ڷ�������������������ֱ���������Ĵ��䣬�����ܻ�������ϣ�ֻҪ�����ܳ�������Է�����ͬ����ض�������Ȥ����������ָ����4�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","�����","����������սʽ��������ʽ����ȴ��秳�һ�ŵĴ�����ü����ϲ�������Ǽ������òͣ���ȴ��ȡɽ��Ұζ������֮ǰ����ϸ����һ�°ɣ���������ָ����2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","��Ů��","��Ȼ������Ϊ�����С�¶����۲��ݣ�������ǻᷢ�ֱ˴�����������һ�ԣ��������Ȼ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","ʨ����","ֻҪ�úõغ���������Ϊ�㸶��һ�С���Ը��Ϊ���ɨ���䣬ϴˢ������ֻҪ���ڴ��ϻر����飬�������һ�и�������ֵ�õġ���������ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","��з��","��������һ��С�ľ������㣬������ȷ�Ǹ�����������ֵ���������ˣ����Լ���Ƥĥ��һ�㣬�����������ɣ���������ָ����4�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","˫����","��������ͬ����ϸ�ĸо�����ͬ�Ļ��⣬��ϧ���ǵ�֫�幵ͨ��Щ�ϰ����޷���չ�����ܵĹ�ϵ�����Ǿ͵����ĸ���ǰɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","������","�ڴ��ϣ����ƺ�������Ҫ��Ծ����Ʋ����Ȥ��̸�������ڸ����棬�������൱ƥ���һ�ԣ���������������൱��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ů��","��ţ��","����ֱ��������ȷ�����������ܣ������������Ӧ����ʵ��̬�ȣ���"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","˫����","���Ǳ˴�һ�����飬������Ϸ�˼��̬���ս���������ģ�̸���������ԣ����ڹ滮δ�����¾Ͳ����ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","ˮƿ��","���ķ������������Ķ�,���������������·��ȴ���㲻�����,�������ע��Ҫ�޼����ա�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","ħ����","�ڴ��ϣ�����������������һ�ԣ���Ϊ�������޷��־��أ�Ҳ����Ϊ���˵Ĺ�ȥ���ǲ�ʱ���֣������ű˴˰ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","������","���Ǹ��ܰ������ˣ��������Ĵ���������й���������뿪��֮ǰ���������ְɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","��Ы��","��Ҫ����Ҫ���������Ů���Լ�ȴ�����������������顣��������ȫû�а�ȫ�У���ʶ��������ת"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","�����","���Ĳ���ʵ�ʶ��������ʵ������Ǻܲ��������ϡ���Ȼӵ��һҹ��������������˵�ټ�����ʧ�����������δ��Ҳ̫�����ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","��Ů��","����һ��������������·����һ���Լ���ͨ��������һ�������������飬Ҳ���Ǵ������ˣ����ǵĽ�ϣ�������˵����۾��������ȴ�ǳ������ϵ�����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","ʨ����","�����Ƶ�ʾ�⣬����������У����ò��˶�ã���ͻᷢ�֣����������Ǿ�����Ȼ�����ǣ�����������¯�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","��з��","������ϣ����Ƕ��Ա˴������˼��ѵĵ�һӡ�󣬵��ϴ�֮����ᷢ������ȷ�й���֮����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","˫����","��������Ȼ���ң���Ȼ��Ϊ��������ô��ĸı䣬���ƺ�û�о���Ҳ�����顣�뿪���������Լ��ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","������","��ֻ��˵˵���ѣ�������������μӽ�ͷ�˶�������Ϊ������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"�����","��ţ��","������绰˵��������ʱ�����Ѿ��е������Ѱ��ˣ�ʵ���ϣ�������û�����������͡����������߲�ƽ��������ʳ����ȥι����֮���������Լ��ɣ���������ָ����3�쳤�ؾ�ָ����1"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","˫����","�������鼰���׾��񶼲������㣬������ռ����ʵ��̫ǿ����ᱻ���ܵ�ƣ��������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","ˮƿ��","���ڴ��ϵ�����������۽磬�������μ��������Դ��ʱ����ίʵ̫��Ĭ�˵㡣����ôһ������ѡ������У���ֻ�ð��������ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","ħ����","���ϿҶ����꣬��ֵ�����и�����Ķ������ǵĴ������������Ȥ�����������ɫЦ���ڴ��Ͻ��ɣ�����ӱ������ġ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","������","������������������ᷢ�֣���˼ĺ�룬�㻹���޷�����������ͷ�õ����������ƶ��ֱƵ��㴭������������ʵ����Ҫ�ģ��޷���һЩ�仯���ѡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","��Ы��","����һ��������������飬�Ա˴���Ȼ�����ÿ��ȡ���ִ�����ҡ��羡�������ͻ����ʵ�����Ƿǳ����Ƶ�һ�ԡ���������ָ����5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","�����","�������ڴ��ϵ�ȼ��ȫ������飬Ҳ��͸�����������е���Ҫ��������������������������ʱ����ȴ�����������ӡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","��Ů��","���ڴ��ϵ�������ϸ�ģ���ȷ���˸ж���������ʵ�Ŀ������ԣ�������ı�׼��Զ�ĺ��أ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","ʨ����","�����Ǻ�����һ���������ϣ�����ϧ���ǣ��е��ƶʣ�Ů�İԵ������ü�������ɢ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","��з��","�������黯���������֣����������κ������������,��ļ���,ʹ�����ٺò��ɣ����ǿɳƵ����Ǿ��䡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","˫����","��Ҫ�ı������ܸ��Ķ࣬�����������������һ����������������繣���һ����ȴ����ӵ���Լ��Ŀռ䡣ֻ���ڴ��ϣ�������ȫ��ȫ��ġ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","������","���Ĵ��Ϲ���һ�����ϲ����,�ѷ������������Ķ�������������!�������ܻ������Ĵ��ϻ����! ��������ָ����5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"��Ы��","��ţ��","�����Ļ����ź����������˵�ɣ���Ы���������ɶ�һ�㴲��ʵ�ʵĻ���������һ�����������ƶ�"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","˫����","������ʮ�������еĻ������ӣ����ڴ��ϣ����ǵ�ȷ���൱Ͷ�ϵ�һ�ԣ�����һЩ���ɵĿռ�ɣ����������ʱ����Ȼ������ġ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ˮƿ��","һҹ�飬�����������֮�������õĻ��䡣��������ɣ���������Ҫһ�ݰ����ĸ��飬�����޹�ʧ��ǰ��˦�����ɣ���������ָ����3�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ħ����","ϲ��ð�ա����û��룬�����ǹ�ͬ�����ԣ�ͬ������������Ը�ʹ���Ǻ��Ѹ��˴˳�ŵ����������ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","������","������������̬�ȱ����Ͳ����ײ���������Σ�����ÿ��Լ�����Ǻ�������ģ���ʵ���޷����������˸���һ����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��Ы��","����������������������롣��õķ�ʽ����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","�����","ֻҪ�п�����ˮ�����Ϳ��Ի�úúõģ���������Ǵ��ྶͥ�ġ�����Ҫһ�����ȵļҡ��̶��Ĵ������ԣ��������־���ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��Ů��","�����й���๲ͬ��������罻������ܿ�ϧ���ǣ�������Ϊ����������ʱ����ȴ��׼��Ҫ��ʼ��һ��Ư���������������ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","ʨ����","���ݵ����延���Ǵ��п�Ϊ�ģ���λ����ŷ�Ǻܺõĵ���ʥ�֣�������������õĹ�ϵ�𣿺���ϣ������������ָ����4�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��з��","����ʱ��������һ�μ��飬��������ԭ��������������ˣ��������Ե���ɣ��ܿ��ܳɾ�һ����Ե��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","˫����","�������ɵ���������������ɣ���ҲΪ������˲���ȫ�У����У���������С�Ļ������Ǯ��������δ��ɸ����ص���֮ǰ���뿪���ɣ���������ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","������","����������늅������֮�У�����Ϊ��������������Ȥζ��������һ����������ܵ��������ɵ���Ȥ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"������","��ţ��","�����ദ�������ȷ������Ȥ�������Ǹ�������ŵ���ˣ����˷�̫��ʱ������"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","˫����","���������Ϊ��������Ȥ���ڸ������Ȼ��������������������Ҳ�úð������ɣ���������ָ����5�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","ˮƿ��","������һ�ɲ�������ʵ�������������ܣ����ڴ��ϵı������磬��ȫ�޷���������������ٹ۲쿴���ɣ���������ָ����3�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","ħ����","�������ϣ�����������ȫ�ֹ���һ�ԣ����ǳ��ܱ˴����ѶԷ����ǵ����飬û���κ�һ�������ܱ�������Ĭ����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","������","׷���ȶ�������ϣ������ͬ���ض������רע��������������绹������ĺ����ģ�����ֻ��ɽ����֪����ĵ��߰ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","��Ы��","������������������һ�з��գ������ĺɰ��������̤ʵ������ʲô��˵���أ��ú��������ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","�����","���Ǹ������Ҳ����ۺ�ľ��ӣ������Ů��Ҫ���Ǹ�������ֵ����ָ��֡�������ԣ���̫�����ˣ����ǿ�ϧ���������ɳƵ����ǽ�����أ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","��Ů��","�����ԸУ��ִ�����Ұ�ģ���������������������ɼ����Ĺ�Ʊ����Ҳ�˽��������Ŀ���������Ϭ���򸴺���"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","ʨ����","���������ĳ�����ǣ��Լ���ҵ�г�����¶������Ȼ��ɣ��������һ�δ����İ��飬��ȴ������ʵ�ؼ�����˭�ĸ����϶ࡣ��������ָ����3�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","��з��","������ϲ����ҫ�Լ��ĲƸ���Ұ�ġ��������������ΨһԸ���ṩ�ܷ�۵��ˡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","˫����","���ĳ������ڴ�����ʣ��������������ǵ��ʲ�����������ֱ��ٱ����ĸо����ڴ�����Ȼ����ϲ�����������������������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","������","������������������������Ѵ�������ڸ����涼��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"Ħ����","��ţ��","�����������Щ��ʵ���£����ƱͶ��֮�ࡣ���������̰�棬Ϊ������������������Ȥ�����ദ�վã�����Ҳ�޷�����������ʵ���Ͻ������"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","˫����","����֪����������棬���㷢�֣�����Զ�޷��˽������������磬�������˻���á�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","ˮƿ��","��Ȼ��ͷ��˵��Ҫ�������磬����ȴ����������绰���㣬��Ȼ������������鲻���ڴˣ���ȴ��ֻ�����Ǹ����ѡ���ú����й�ʶ��ֻ�����Ǹ����ѡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","ħ����","��Ը���������Ű��ĳ��һ����裬�������ԣ����ǡ����ֶ���Ϊ���أ��������ˣ�������������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","������","���Ǹ���Ȥ��Ĭ���ˣ������ദ��˵������Ц����ϧ���Ĵ��ϱ���̫����ʧ��,��ֻ�÷���������������ָ����4�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","��Ы��","��������ǣ��������޳�ɫ����������һ����Ƶ�����һ�Խ�ͯ��Ů������һ����Ĵ�С��Ƣ���ɣ���ֵ������ô����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","�����","��Ȼֻ˯����Сʱ�������Ǿ������棬����������������������ܴ���֮�ְɣ�����֮�⣬����û��������ָ�����ˡ���������ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","��Ů��","���������ܶ�����ʱ��,��Ҳ���ڸ������ɵĿռ�;���г�һ��,��������㶨��������ȴ���ã�ʱ����δ���졣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","ʨ����","��ĸ����������ӣ�������һЩ�������ҵĿռ䣬���Ķ��ɲ¼ɣ��������ܿ��"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","��з��","��λ�ú���������Ӧ����ǿ������������ռ��ͬ��һ�����ӣ������ò��޲����������ǣ���Ҫ������������ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","˫����","������ǻ�������������㣬����޲���������ӵ����������Ȱ��������"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","������","������һ�����Ŵ󵨵������������ǵ��԰���ϵ���Ǳ���������������ϡ������������أ�һ����Ĭ��ʮ�㡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"ˮƿ��","��ţ��","λ����������������������㣬��Ƥ�Ĵ�������"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","˫����","�������������ͬ�㣬�˴�����ĶԴ�����ȷ�öԷ��е����ģ���ż������Ҫ������ʵ�������ɣ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ˮƿ��","���Է��棬������������е��ޱ���ܰ�������������Ը�ʵ������ʧ����������н�֣������Ҫ�ܳ����ܳ���һ�ΰ��鳤�ܡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ħ����","��̫��ʵ����̫���Σ�����������Ų���ô��ʵ�Ļ�����ᷢ������ȷ�Ǻܺõ����ˡ���������ָ����5�쳤�ؾ�ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","������","���̬����������׽��������������ĺ�����ȸе����󣬲�С�Ĵ���Ļ�������ʱ�����ơ�������������������ڿ��ܻ�����ร�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��Ы��","������ڴ��ϸ����ܴ�����㣬�������������龭������һ�ٵش��ˡ����������Ȼ���ۣ�ȴ̫���ݡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","�����","��Ȼ���������ĸо��ܰ��������е����ܿ�ͻ��˽⣺��û�취��ʱ�ṩ��һ���ɿ��ߵļ����Ϊ����ʵ��̫æ�ˡ���������ָ����4�쳤�ؾ�ָ����3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��Ů��","�������֮ʱ�������ܸ����������������������㽫��Ϊ����õ���ҵ���ʡ�̸�Ķ��������ӣ����Ƿ���Ī���ˣ������أ�ֻҪ����"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","ʨ����","��һ�����ƺ�̫����Ƹ��ˣ��ڶ�������������һ��������ɬ�����ӣ��������������Լ�Զ���ڹ����㡣���ó����˰ɣ���������ָ����3�쳤�ؾ�ָ����1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��з��","һ�������к����ᣬ�����������ϻ��ϣ����Ƕ��൱��Ͷ������˵������֮�ϡ�"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","˫����","�������飬ʹ��ͣ���˱��Լ���Ԥ�ڵ�ʱ�仹Ҫ��������������Ѷϣ�������������㡣"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","������","����������ʫ�����ʣ�������ú��չ��������Ƹе����ˣ������뵱�������˻���ĸ���أ����ǲ���ı�ģ�������������һ�𣬾ͽ������ɣ���������ָ����4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"˫����","��ţ��","���Ǹ��ӱ���ʵ���ˣ���Ȼ�����İ���������㽫ͽ���޹�����Ȼ���Ĵ��ϱ����൱���ᣬ���������˰ɣ���������ָ����2"});
		
		
	}
	public void insertXuexing(){
		//���Ҫ�����ݽ��и��ģ��͵��ô˷����õ����ڲ������ݿ��ʵ��,�÷����Զ���д��ʽ�����ݿ�
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"O��Ѫ","O���˵Ĵ���̬�������֣������˽��������ĶԱȡ������ƶ����С������ý����顣O��������Ϊ�Է��ǿ�������������ʱ����̬�Ȼ����һ�ٰ�ʮ�ȵı仯��ֱˬ���ʣ�������ͣ����������Ĺ��գ������ǿ�����˵�ɫ�ʣ�ȴ�����������ů��ǰ���ᵽ�������鰮��Ҳ���ֱ��ֳ�������ϲ��ȫ�ҽ��ʡ�����˵����ֵ�ýύ������Ī����O�����ˡ���֮�����϶��Է��ǵ��˵Ļ���O���˵ķ�ӦҲ�ǹ�"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"A��Ѫ","A���˵��ŵ��Ǵ��˳�ʵ���������ŵ��һ��Ӧŵ��ȫ���Ը�������ʧ�š�����һ����˵��A�����ǿ����������ദ���µĺ����ߡ�����һ���棬�Ҿ������������˽������й�������Դ��������Լ����������ˣ�����˾������˾���ˣ����ǻ���һ�������"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"B��Ѫ","���˽���ˣ�����B�����Ǽ���������Į��̬�Ȼᵢ�����Ƿ��Ļ����⡣���ǽӴ������úܺ��ദ�����ǲ��ƽ�С�£���Ƿ�����黳�����ĵ���������������ζ�����е�B���˳����ж���ʾ�Լ����˵�ͬ�����⣬��������Ϊ�ֵ��˽��������µĳ�����Ū�ĵ��������Ǻö�ţ��Ƣ��������ô˵����ƫ��ô˵�����ܱ��˵������ȷ��������ͷ�һͨ�ڣ�˵���ֶ����޹غ�ּ�Ķ�������"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"AB��Ѫ","AB���˺�ȼ��Ѥ���İ���֮�����ְ���������Ϸ��ζ�������е����Ϸ�绯�����������ԡ������Ի���֮�С��ǵ��������ǽ������������Ǳ�������׷�����ְ������к�������Ե�����������������˼�뷽�������а��ÿ��룬����Ƿǵ��ص㣬����AB�������ʵ���Ҫ�������ԡ��ڰ��鷽�棬��������Ҫ���Լ������������е����˹�������ʽ�ģ����������మʽ�ġ�����������£�˵�ù���Щ��"});
		}
	public void insertXingzuo(){
		//���Ҫ�����ݽ��и��ģ��͵��ô˷����õ����ڲ������ݿ��ʵ��,�÷����Զ���д��ʽ�����ݿ�
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"������","����������ϲ���޾��������������ǣ�����Ը�ⲽ����֮�󳾡�������������Լ��ĸ��飬Ҫô�������磬Ҫôŭ����ڡ�������Ը�����裬��Ҳ������Ȼ�ձ����������ڼ��ﻹ�������棬�㶼������ִ�����º�������֮�Ժ󣬴Ӳ��Ǻ����ġ������Ѻ�Σ�յĹ�ͷ�����ܳ�ֱ��ֳ��Լ���Ʒ����������õ����ǵľ��������������������´Ӳ���ϧ���������ɸ����޴�Ĵ��ۣ�ҲҪ����ǰé��","3��21�� - 4��20��"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"��ţ��","��һ�����˶��澳����Ӧ���������ۺ�ʧ�ܳ���ʹ����־���������������Ϊ�ذ��Լ������������ķ�ŭ֮�У��ܾ�������һ�нӴ���һ������������ת�����ֻ����������������Կ�ǰ�Ĺ�������ȥʵ���Լ���Ŀ�ꡣ��ţ������˼�����ڱ��أ���������ơ�����ӵ��һ�������ĲƲ�����ͷ�Ӳ���ȱʱ���㷽�ܸе�̹Ȼ�����������ϣ������ʵ�зǳ�ǿ��ʮ�����ڰ����Լ������ʺͼ�ͥ�����ҵ","4��21�� - 5��21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"˫����","˫�������˶��ϲ�����Լ��Ĳ���������ҵ���棬����Ը�����������Լ����������档��˼���е�һ�������������������ҵ�ϵ��ڷɡ���һ�����˻���һ���������ص㣻�ر����ڵ����Լ����ѵĻ����ԡ�˫���������ʺϴ�����ѧ����ҵ����Ҫ���Ա��������ְҵ������Щ����������ӱ���������⣬�����š���Ӱ�����е���Ҫ���ǡ����͹��ҵĹ����У��Լ��漰�˼ʹ�ϵ����Ĺ����У������ֳ��Ƿ��ĲŸɡ��������ǵ�Ӱ�����ϴ��㽫��Ϊһ����ɫ��ʵҵ�ҡ������еĽ�������������Ӧ�����ڰ���ʱ����������������������ҵ�ϵĳɹ���","5��22�� - 6��21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"��з��","��з�������쿪ʼ�ĵ�һ���������������Ӽ����˼�뱣�غ����е��Ը��������һ�������ˣ����Ǹ���Ҫ���ұ������ˡ���������һ�������ˣ��д�ĸ(�ȸ�)������飬Ҳ�����ź��Ӱ�Ĵ�������棬�к�ǿ�����������������ѧ�ǣ�ʹ�Լ��ͱ��˶��Ҹ���Ը������Լ���˫�ִ�������������Ҹ��������㣬���ܵ������˻��ȥ��һ�Ҹ�����ˣ�����ֵ�ץס���ھ������������ӵ�е�һ�С�","6��22�� - 7��22"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"ʨ����","ʨ�������˲�Ը��������ƽӹ�������У�ͨ����ܿ�ͻ�������־��أ���Ϊ��������������ҵ�ɹ����ؾ�����ߵ�����ʹ�㲻Ը��������խ�ļ�ͥȦ�����ϣ��������ķ�Χ��ȥʩչ�Լ��Ĳ��ǡ���ʱ�����ֵ�ר����裬���������Ϊ���˵��Ҹ���Ŭ�������������ĵز��в�㣡��Ƿ��ĲŻ�ʹ���������������������ְλ�����ϺͿ��������Ը񲻿ɷָ��һ���֡������ڷ���Ϊ�Լ�������","7��23�� - 8��23"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"��Ů��","��Ů�����˶�ÿһ�����鶼Ҫ���ܼƻ�����ϸ���ţ�ϲ�����������ȥ��һĿ��Ȼ���º�����¼�Ա��飬ÿ�֧��������Ŀ����֮�����������һ˿�����;��������ģ���ϲ��ͻ���������������������������ǵ�̫�������ĳһ���򼸸����ǳ����ڴ�Ů��ʱ�������Ը��ص�͸�Ϊ���ԡ���Ů������������ͼ���Լ��ļ���ͳ嶯�������ǵĿ���֮�¡�����˼���ǡ��������ã���ʱ�����Ź�����","8��24�� - 9��23"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"�����","��������������������ٵ������������������������������Ƕ�������Ͷ�������������Ѱ���Ź�ͬ��ͻ����½���������Ͱ����׵ı��ԣ�ʹ��޺͵�����Զ�޷�����������ԡ�������������ᡢ��ţ�����Ҫ�������ֵ������Ҫ���겻�������Ͱ��顣�����˳�������Ը��ϵ���Ҫ�ص㡣���۶�����������Ի���Ů�ԣ������Կ�����Щ���ܿɹ��Ů��Ʒ�ʣ�Ʒ����ֱ��ƽ�׽��ˣ�������ҫ","9��24�� - 10��2"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"��Ы��","��Ы��������Ҫ�������ϵش���æµ֮�С���ϲ�����Զ���ȥ����ϲ�������Լ��Ĺ������������ϲ�������Լ����뷨������ϲ���������º�ӹӹµµ������ǻ�ʹ��ɥʧ�����ͻ�������Ӳ������κ�ʧ�ܣ�����⵽�˴��ۣ��㽫�����ǿ�ҵ������̬��Ӧ�����������㿪ʼ��ƾ����ǿ����־�ͼ��Ͳ��εľ������±���ɹ�����ϲ��Ϸ���Եĳ��棬�᲻ʱ������ǰ���ĵ�·�ϵ���һĻ����","10��24�� - 11��"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"������","��һ�������Ը��ʣ�˼���Ծ��ע���Ļ�������ͬʱ�ֲ����������硣��������ʵ�����˼�볣������ңԶ�Ĺ�ȥ�����õ�δ�������ݵ�˼����Ծ�ţ�һ�����⣬һ��������ǣ�ʹ�˾����������ǰ���ַ·�����ߣ��þ��ü�����־ͬ���ϣ��ַ·��������롣����һ��˼����Ҫ�跨ƽ�����ˡ������������������磬������������ȵļ��顣��Ӳ��ƽϸ��˵ĵ�ʧ��ϲ��ͬʱͶ��������","11��23�� - 12��21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"Ħ����","ħ�������ˣ����䵱�м�������ͬʱ�����������ʱ���㽫��һ��������ʵ����˼����б������ˣ�ͬʱ�������ױ����ҵĸ�����������һ����ǿ�ҵ����Ҿ�����ˡ������ƽ������Į����̫���׽ӽ���ϲ����Ⱥ�������㺦�±��˺��������̸����ռ���㱦���ʱ�䣬Ҳ���ܽ��ܱ��˶���ֱ��������������桢˼�������ʼ����һ���ҳϿɿ�����ֱ���ಢ�������������⣬�������ʯ��������","12��22�� - 1��2"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"ˮƿ��","ˮƿ�������Ǹ����Լ�ǿ���ˣ���������֮�����õ����⣬������Ը���ܸ����ϵ�˿����������ϲ���ḻ���ѵ�˼�뾳�磬ϲ����������ȥ�����Լ�����Ұ����ϲ�������˽���֮���˽����ǵ�˼��۵㡣ˮƿ�����˲��������κ�Լ������Ҳ����ǿ���Լ�ȥ�����κμ��ɡ����ĳ���������������Ȥ������Ϊ֮�����޴��Ŭ����������ζ�������ʹ���ķ����ң������ܲ���һ��ʹ����Χ���˶��޷���","1��21�� - 2��19"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"˫����","��������ܱ�����һ�����桢�Һ�����ʡ�һ���˵�������������칬ͼ��û�����ǹ���Ĳ�������Ӱ�죬��������ľ�ǻ���Ǵ������뷽λ�����ʱ����ᾭ���ܵ����˵���Ӧ�����벻�������顢ͻ�������Ļ�����ϲ�ҵ��Ը񽫻��������㡣��Ҳ�п����вʻ���������·������һλ�ǳ����еĺͿ�����֪�ѡ����ǣ���λ˫�������˾ͻ������һ���µ��⾳�У����ų������������ɫ�ʵ�������","2��20�� - 3��20"});
	}
	public void insertShengxiao(){
		//���Ҫ�����ݽ��и��ģ��͵��ô˷����õ����ڲ������ݿ��ʵ��,�÷����Զ���д��ʽ�����ݿ�
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","��ı���ʹ�����˶�һ�������޿ײ��룬�ܹ��ܺõر����Լ������ܣ�����ȴ��̽���������ܵ�ר�ҡ�������ʹ���Ѽ�����������Ϣ���������ض���죬��������˵Ŀ��Ӷ�������������֮�������˾�����Ź��κ�һ��������Ϣ�Ļ��ᡣ�����˺ܰ������£���������Ǻõġ�"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"ţ","�������ı����ǽ�̤ʵ�أ��Ӳ��������¡���ƾ��������ܸı���ţ�˵��뷨����һЩ�����ϣ��������������ţ�˵��������ţ������֧���пɿ���ȷ�а��յķ��������������������ţ�˴�Ĺ�˾��ʤ�ߣ�����Ҫ��ֵ������������ǻۡ���ţ�˵����ʺܺã�������������ţ�˺����ţ�����Э�������ӱ��˵������������ţ����ע�������������Ĭ�����飬�������������Ҹ���"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�ڶ������ϻ�������Ȩ��������ʹ󵨡���������һ�����췴������עĿ��������׽��������������ܵ���ҵľ�η��ʹ�˺�����������滢һ������������ʹ��ͥ�ܿ��������ѣ������͹�������˶�����������������Ը���ϰ�ߣ���ô���������Χ��ӵ�����ˡ������˵Ļ����Ͷ�������ֹ۾��и�Ⱦ������ỽ���������еĸ��ָ��飬Ψ��û����Į����֮�������˵������߻��Ϊ����"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","̰ͼ���ݡ�����ͻ��Ʒ�ʻ�������˴������ߡ�������������ҷ��ݵĻ��������������������ѡ�������·�������˻�ѡ���ݵ����ʽ��������������Ů���ܰ����������ʵ��·������ü�Ҫһ���ģ�����Ҫ�õġ������˻�������ʿ�ױ��ġ���˿��������Ħ������ͻ����·���һ����Ƥ�����Ƥ���ڼ��ϣ�����ȥ����㣬������Ĵ�����һ�۾Ϳ��Կ����������õġ������ġ������εĻ����"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�񻰴�˵���Ǿ޴󡢺�ΰ����ʹ���ǲ����������롣�������������Ʒ�ʣ����������񣬿϶�Ҳ��������Щ�������������ǵ����С����й����������Żʵۻ����ԣ���������Ȩ����������������˾�˵����������֮�ǡ������ĺ���ϲ�����ص�����ϲ���е���Ҫ���Σ���ʹ��������ڼ�����СҲ����ˡ�����ϴ�ĺ��ӳ����ܱ����ǵĸ�ĸ���ܵ��������������ǵĵܵܡ����õ����Ρ�"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","����ʮ������������ǿ�����࣬��������ʮ����������������ظУ����˼��������Щ��������Ϊ�������յġ���������׽�������˲����־�С���ʵ��������Ϊ�ߵ�����̫����ÿ��һ��Ƥ���ܹ����һ������������ر��Ʒ�������������˾��к�ǿ����������������ÿһ��ս�����㶼�ܺܿ�ػָ��������й����ڴ�ͳ��������Ϊ����������������������Ϊ����������Ϊ�������߶��ߵ�ʱ�䣬���Զ�����������������������������˳�ӡ����⣬�ں����������������˱��ڻ����������������˸�Ϊ�ֹۣ������׵õ����㡣"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","������˰�����������������������ǿ��Դ������������ɵľٶ������������˺ͼ�����˵���ٶ��Ͽ�����һ�㡣�������Ƿ�ӦѸ�٣��ܵ������ϣ��������Ƕ�ҡ�������Ե�"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�����˳����ֹ���ţ����˸���ͬ���Ķ����˳Ƶ���������ϲ����ͯ��С�������Ȼ�����ߡ��������Ǻܻ���ҡ����������������½���˵Ĺ��������˵��Ѵ�������������ʱ���Ͽ������ڽ�Ǯ�ϴ󷽣�����������޴�����������ϴʱ������Ҫ������������Ѿ���������˴����������˵ġ������ߵ���������˶�ϲ�����˽�������Ը�������˺��������Գ������һ����������ζ�������˽��������������������˲������ܵ�������µİ���ͬ��Ҳ���ܵ��������������İ�����"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�����˶�Ԫ�Ե�Ʒ��֮һ�Ǽᶨ�ԡ����������˵�����Щ�˿�������ô���ߡ���ô�����죬����������ȴ�����Ų��ɶ�ҡ���뷨����������ʾ���������˶��Լ��Ĵ������¸ҵ��������ͣ������˺��������������ͺ�Ļ��ֺͽ�����Ҳ���Խ��������м����κη��Σ������˳��ĳ������Ϊ���˱Ȳ��������ˡ�������������˽���������ˣ��Ͳ��ط���ȥ��Թ�����˶ԡ�����֮�֡����������������������֮�����������˵�ԭ��Ȼ���������г���������˶�˵����˽�ġ����ʵ�Ҳ��׼ȷ��������û��ֱ�Ӳμ�ĳЩ�ʱ���Դ�����Щ����˲��������⡣"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�������в�������Ʒ���ֲ�������Ĳ��㡣�����˾���ǿ�ɣ���֯����ǿ���������棬����ֱ�ʣ����¹��ϡ������˶Բб�����Ϊ��������ָ�����������С����������Ķ��Ƕ�����������ޡ�׷���ƾ������ˡ������˶������Խ�ǿ����������У��ڴ����κ�����ʱ��Ҫ��ȷ�����³�ȥ��ʵ������Щ�������°��µ��˸е�����⡣�������˵��ŵ㻹�Ǻܶ�ġ������˻����Լ������ܼ�������¾���ȥ�������ˡ�ֻ�������˵Ļ����Ķ���������̫����ʾ�Լ��������˶��Լ��ļ�ͥ�ǳ����ģ���ά���Լ���С��ء�"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","�ˣ�����Լ���Լ���������ʹ������Ҳ���Ϊ���ʡ���ʦ������ѧ�ҡ�"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"��","ʵ���ϣ�������Ӧ�������������ߡ�ֻ�������˲����ģ�ϲ��ͬ���˷����Լ������С���������������Ϊ���˸���ʱ��������Ҳ��������档��һ���棬�����˵ľ�������ϴ�ǳ�������У������Ա��˸������˵�����ֻ�ǲ��ں������ʼ硣�������۹�Ҳ��ǳ��ֻ����ǰ��Ҳ������Ϊ����ص㣬��ʹ�������ڱ�Ӧ��ʹ���ʱ����ѳ����������˴Ӳ����ֻ����ù��ء���������������Σ���ľ���Ҳ�����������˹��ֿ�����ɵġ��������˶Ա��������Ҫ���޷����㣬���������ʱ�����ܼ�ʱ�������˲��������ʵ�����Ǽ��ȵľ�ɥ��ʧ����"});
		
	}
	
	
	public void update(User person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update userinfo set username=?,pwd=?,sex=?,age=?,telephone=? where userid=?", 
				new Object[]{person.getUsername(),person.getPwd(),person.getSex(),person.getAge(),person.getTelephone()});
	}
	
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from userinfo where userid=?", new Object[]{id.toString()});
	}
	public void delete(String  username){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from userinfo where username=?", new Object[]{username.trim()});
	}
	public User find(Integer id){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where userid=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			return new User(username,pwd,sex,age,telephone);
			
		}
		return null;
	}
	public User find(String username){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where username=?", new String[]{username.trim()});
		if(cursor.moveToFirst()){
			//String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			return new User(username,pwd,sex,age,telephone);
			
		}
		return null;
	}
	public Boolean checkUser(String username,String pwd){
		//���ֻ�����ݽ��ж�ȡ������ʹ�ô˷���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where username=? and pwd=?", new String[]{username.trim(),pwd.trim()});
		if(cursor.moveToFirst()){
			return true;	
		}
		return false;
	}
	
	public List<User> getScrollData(Integer offset, Integer maxResult){
		List<User> persons = new ArrayList<User>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			persons.add(new User(username,pwd,sex,age,telephone));
		}
		cursor.close();
		return persons;
	}
	
	public Cursor getCursorScrollData(Integer offset, Integer maxResult){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		return db.rawQuery("select userid as _id, username, pwd,sex,age,telephone from userinfo limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
	}
	public Cursor getXingzuoCursor(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		return db.rawQuery("select  _id, xingzuoname, tedian,timerange from xingzuo ",
				null);
	}
	
	public long getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from userinfo", null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
