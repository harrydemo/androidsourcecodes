package com.bn.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class DBUtil
{
	static SQLiteDatabase sld;
	static EditText et;
	public static void createOrOpenDatabase() throws Exception
    {
    	sld=SQLiteDatabase.openDatabase
    	(
    			"/data/data/com.bn.helper/sd.dbdb", //数据库所在路径
    			null, 								//CursorFactory
    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //读写、若不存在则创建
    	);	    	
    	String sql1="create table if not exists lines(L_number INTEGER(5),L_name varchar2(20),L_description varchar2(100))";
    	sld.execSQL(sql1); 
    	String sql2="create table if not exists station(S_number INTEGER(5),S_name varchar(20),S_description varchar2(40),S_jd varchar2(20),S_wd varchar2(20))";
    	sld.execSQL(sql2); 
    	String sql3="create table if not exists ls(XZ_id INTEGER(5),L_number INTEGER(5),S_number INTEGER(5),num INTEGER(3))";
    	sld.execSQL(sql3); 
    	String sql4="create table if not exists linerevise(id INTEGER(5),num INTEGER(3),XZ_ID INTEGER(5),jd varchar2(20),wd varchar2(20))";
    	sld.execSQL(sql4); 
    	String sql5="create table if not exists temp(num INTEGER(3),jd varchar2(20),wd varchar2(20))";
    	sld.execSQL(sql5); 
    	String sql6="create table if not exists tranfrostop(id INTEGER(5),name varchar2(20),jd varchar2(20),wd varchar2(20),L_name varchar2(20))";
    	sld.execSQL(sql6); 
    }
    
    public static void closeDatabase() throws Exception
    {
	    sld.close(); 
    }
    
    //插入记录的方法
    public static void insert()throws Exception
    { 	
    	String[] sqlsb={
    			"insert into lines values(00002,'地铁2号线','')",
    			"insert into lines values(00001,'地铁1号线','')",
    			"insert into lines values(00013,'地铁13号线','')",
    	};
    	for(String sql:sqlsb)
    	{
    		sld.execSQL(sql);
    	}
    	
    	String[] sqlsc={
    			"insert into station values(00001,'西直门站','','116.3551452636719','39.940277770390324')",
    			"insert into station values(00002,'车公庄站','','116.35594367980957','39.93191969562437')",
    			"insert into station values(00003,'阜成门站','','116.35637283325195','39.92290236029078')",    			
    			"insert into station values(00004,'长椿街站','','116.36332511901855','39.89910274087739')",
    			"insert into station values(00005,'宣武门站','','116.37439727783203','39.899497822790494')",
    			"insert into station values(00006,'和平门站','','116.38426780700683','39.89989290242579')",
    			"insert into station values(00007,'前门站','','116.39800071716308','39.89992582562591')",
    			"insert into station values(00008,'崇文门站','','116.4175271987915','39.900847668807245')",
    			"insert into station values(00009,'北京站','','116.42718315124512','39.90486412650293')",    			
    			"insert into station values(00010,'朝阳门站','','116.43469333648681','39.924350479594196')",
    			"insert into station values(00011,'东四十条站','','116.43435001373291','39.93343343839839')",
    			"insert into station values(00012,'东直门站','','116.43374919891357','39.941001650090925')",
    			"insert into station values(00013,'雍和宫站','','116.41705513000488','39.94909541971909')",
    			"insert into station values(00014,'鼓楼大街站','','116.39370918273926','39.94876642336431')",
    			"insert into station values(00015,'积水潭站','','116.37332439422607','39.94856902479194')",
    			
    			"insert into station values(00016,'苹果园站','','116.1779308319098','39.92645678026888')",
    			"insert into station values(00017,'古城站','','116.19080543518066','39.907330022383197')",
    			"insert into station values(00018,'八宝山革命公墓站','','116.23582363128662','39.90730022383197')",
    			"insert into station values(00019,'玉泉路站','','116.25316143035888','39.90726730417519')",
    			"insert into station values(00020,'五棵松站','','116.27423286437988','39.907201464814136')",
    			"insert into station values(00021,'木樨地站','','116.33796215057373','39.907168545109904')",
    			"insert into station values(00022,'南礼士路站','','116.35268211364746','39.9070368661347')",
    			"insert into station values(00023,'复兴门站','','116.35667324066162','39.90713562538983')",
    			"insert into station values(00024,'西单站','','116.37439727783203','39.907201464814136')",
    			"insert into station values(00025,'天安门西站','','116.39182090759271','39.90733314347294')",
    			"insert into station values(00026,'天安门东站','','116.4016056060791','39.907596500031254')",
    			"insert into station values(00027,'王府井站','','116.4116907119751','39.90792569430544')",
    			"insert into station values(00028,'东单站','','116.42014503479004','39.908123210110666')",
    			"insert into station values(00029,'建国门站','','116.43593788146972','39.90845240185384')",
    			"insert into station values(00030,'永安门站','','116.45057201385498','39.908254886997746')",
    			"insert into station values(00031,'大望路站','','116.47679328918457','39.908024452279236')",
    			"insert into station values(00032,'四惠路站','','116.49906635284424','39.908419482750716')",
    			"insert into station values(00033,'四惠东站','','116.51618977519531','39.90832072534639')",
    			"insert into station values(00034,'高碑店站','','116.5325403213501','39.9091107805945')",
    			"insert into station values(00035,'双桥站','','116.57721519470215','39.9101312551376')",
    			"insert into station values(00036,'管庄站','','116.5995740890503','39.90897910535256')",
    			"insert into station values(00037,'八里桥站','','116.61828517913818','39.905687142046275')",
    			"insert into station values(00038,'通州北苑站','','116.63738250732422','39.903481438133426')",
    			"insert into station values(00039,'果园站','','116.64656639099121','39.893209165276026')",
    			"insert into station values(00040,'九棵树站','','116.65763854980469','39.89001522821706')",
    			"insert into station values(00041,'梨园站','','116.66853904724121','39.88359397660328')",
    			"insert into station values(00042,'临河里站','','116.6788387298584','39.87522898285956')",
    			"insert into station values(00043,'土桥站','','116.68664932250976','39.87177071491688')",
    			
    			"insert into station values(00044,'西直门站','','116.3551452636719','39.940277770390324')",
    			"insert into station values(00045,'大钟寺站','','116.34512901306152','39.96652996217967')",
    			"insert into station values(00046,'知春路站','','116.34000062942505','39.97642948739513')",
    			"insert into station values(00047,'清华园站','','116.33914232254028','39.98261185212121')",
    			"insert into station values(00048,'五道口站','','116.33761882781982','39.99285427306812')",
    			"insert into station values(00049,'上地站','','116.32000207901001','40.03293783519874')",
    			"insert into station values(00050,'清河站','','116.31500244140625','40.041184995048646')",
    			"insert into station values(00051,'龙泽站','','116.31963729858398','40.07069902257262')",
    			"insert into station values(00052,'回龙观站','','116.33648157119751','40.07046913520271')",
    			"insert into station values(00053,'霍营站','','116.36128664016723','40.07056765845624')",
    			"insert into station values(00054,'北苑站','','116.43465042114258','40.04294272685822')",
    			"insert into station values(00055,'望京西站','','116.44604444503784','39.992788516034295')",
    			"insert into station values(00056,'芍药居站','','116.4361310005188','39.977843588238784')",
    			"insert into station values(00057,'光熙门站','','116.43190383911133','39.96825672678314')",
    			"insert into station values(00058,'柳芳站','','116.43267631530762','39.958191542192644')",
    			"insert into station values(00059,'东直门站','','116.43374919891357','39.941001650090925')",

    			"insert into station values(00060,'复兴门站','','116.35658740997314','39.907234384502566')",
    			"insert into station values(00061,'建国门站','','116.43585205078125','39.90862522688575')",
    		};
    	for(String sql:sqlsc)
    	{
    		sld.execSQL(sql);
    	}
   //id,L_number,S_number,num
    	String[] sqlsd={
    			"insert into ls values(00001,00002,00001,001)",
    			"insert into ls values(00002,00002,00002,002)",
    			"insert into ls values(00003,00002,00003,003)",
    			"insert into ls values(00004,00002,00004,008)",
    			"insert into ls values(00005,00002,00005,009)",
    			"insert into ls values(00006,00002,00006,010)",
    			"insert into ls values(00007,00002,00007,011)",
    			"insert into ls values(00008,00002,00008,012)",
    			"insert into ls values(00009,00002,00009,015)",
    			"insert into ls values(00010,00002,00010,019)",
    			"insert into ls values(00011,00002,00011,020)",
    			"insert into ls values(00012,00002,00012,021)",
    			"insert into ls values(00013,00002,00013,026)",
    			"insert into ls values(00014,00002,00014,027)",
    			"insert into ls values(00015,00002,00015,028)",
    			"insert into ls values(00060,00002,00060,004)",
    			"insert into ls values(00061,00002,00061,018)",
    			
    			"insert into ls values(00016,00001,00016,001)",
    			"insert into ls values(00017,00001,00017,003)",
    			"insert into ls values(00018,00001,00018,004)",
    			"insert into ls values(00019,00001,00019,005)",
    			"insert into ls values(00020,00001,00020,006)",
    			"insert into ls values(00021,00001,00021,007)",
    			"insert into ls values(00022,00001,00022,008)",
    			"insert into ls values(00023,00001,00023,009)",
    			"insert into ls values(00024,00001,00024,010)",
    			"insert into ls values(00025,00001,00025,011)",
    			"insert into ls values(00026,00001,00026,012)",
    			"insert into ls values(00027,00001,00027,013)",
    			"insert into ls values(00028,00001,00028,014)",
    			"insert into ls values(00029,00001,00029,015)",
    			"insert into ls values(00030,00001,00030,016)",
    			"insert into ls values(00031,00001,00031,017)",
    			"insert into ls values(00032,00001,00032,019)",
    			"insert into ls values(00033,00001,00033,020)",
    			"insert into ls values(00034,00001,00034,023)",
    			"insert into ls values(00035,00001,00035,025)",
    			"insert into ls values(00036,00001,00036,027)",
    			"insert into ls values(00037,00001,00037,030)",
    			"insert into ls values(00038,00001,00038,032)",
    			"insert into ls values(00039,00001,00039,034)",
    			"insert into ls values(00040,00001,00040,035)",
    			"insert into ls values(00041,00001,00041,037)",
    			"insert into ls values(00042,00001,00042,038)",
    			"insert into ls values(00043,00001,00043,040)",
    			
    			"insert into ls values(00044,00013,00044,001)",
    			"insert into ls values(00045,00013,00045,006)",
    			"insert into ls values(00046,00013,00046,010)",
    			"insert into ls values(00047,00013,00047,011)",
    			"insert into ls values(00048,00013,00048,013)",
    			"insert into ls values(00049,00013,00049,016)",
    			"insert into ls values(00050,00013,00050,018)",
    			"insert into ls values(00051,00013,00051,024)",
    			"insert into ls values(00052,00013,00052,025)",
    			"insert into ls values(00053,00013,00053,026)",
    			"insert into ls values(00054,00013,00054,030)",
    			"insert into ls values(00055,00013,00055,033)",
    			"insert into ls values(00056,00013,00056,034)",
    			"insert into ls values(00057,00013,00057,036)",
    			"insert into ls values(00058,00013,00058,037)",
    			"insert into ls values(00059,00013,00059,039)",
    	};
    	for(String sql:sqlsd)
    	{
    		sld.execSQL(sql);
    	}
    	//id num XZ_id,jd wd
    	String[] sqlse={
    			"insert into linerevise values(00001,005,00060,'116.35742425918579','39.901292124481444')",
    			"insert into linerevise values(00002,006,00060,'116.35838985443125','39.90028797978328')",
    			"insert into linerevise values(00003,007,00060,'116.36010646820068','39.89941551424642')",
    			"insert into linerevise values(00004,013,00008,'116.41988754272461','39.9012592019378')",
    			"insert into linerevise values(00005,014,00008,'116.42563819885254','39.90442946811022')",
    			"insert into linerevise values(00006,016,00009,'116.43563747406006','39.9049299681102')",
    			"insert into linerevise values(00007,017,00009,'116.43604516983032','39.905193333906674')",
    			"insert into linerevise values(00008,022,00012,'116.43353402219238','39.94814132593099')",
    			"insert into linerevise values(00009,023,00012,'116.43256902694702','39.94921056806945')",
    			"insert into linerevise values(00010,024,00012,'116.43347024917602','39.9483880756002')",
    			"insert into linerevise values(00011,025,00012,'116.43126010874775','39.94960536094206')",
    			"insert into linerevise values(00012,029,00015,'116.35703802108764','39.94397934740511')",
    			"insert into linerevise values(00013,030,00015,'116.35602951049804','39.943370657386836')",
    			"insert into linerevise values(00014,031,00015,'116.3556432723999','39.94274551065026')",
    			
    			"insert into linerevise values(00015,002,00016,'116.18784427642822','39.907234384502561')",
    			"insert into linerevise values(00016,018,00031,'116.49417400360107','39.90832072534639')",
    			"insert into linerevise values(00017,021,00033,'116.5184211730957','39.9075306609866')",
    			"insert into linerevise values(00018,022,00033,'116.52833461761474','39.90907786180775')",
    			"insert into linerevise values(00019,024,00034,'116.56893253326416','39.90927537424098')",
    			"insert into linerevise values(00020,026,00035,'116.58833026885986','39.909209536859834')",
    			"insert into linerevise values(00021,028,00036,'116.60674095153808','39.90878159201512')",
    			"insert into linerevise values(00022,029,00036,'116.6149377822876','39.90575298286265')",
    			"insert into linerevise values(00023,031,00037,'116.63536548614502','39.90539085758962')",
    			"insert into linerevise values(00024,033,00038,'116.64437770843506','39.89406524682466')",
    			"insert into linerevise values(00025,036,00041,'116.66261672973633','39.888500524630384')",
    			"insert into linerevise values(00026,039,00043,'116.68222904205322','39.87308817088368')",
    			
    			"insert into linerevise values(00027,002,00044,'116.35351896286011','39.94118261881951')",
    			"insert into linerevise values(00028,003,00044,'116.35257482528686','39.94448932676474')",
    			"insert into linerevise values(00029,004,00044,'116.3522744178772','39.949736958059866')",
    			"insert into linerevise values(00030,005,00044,'116.34538650512695','39.96243488890367')",
    			"insert into linerevise values(00031,007,00045,'116.34489297866821','39.96866785478372')",
    			"insert into linerevise values(00032,008,00045,'116.3430905342102','39.97146345961307')",
    			"insert into linerevise values(00033,009,00045,'116.34066581726074','39.97420961891167')",
    			"insert into linerevise values(00034,012,00047,'116.33798360824585','39.988399090876804')",
    			"insert into linerevise values(00035,014,00048,'116.33731842041015','40.00758225321084')",
    			"insert into linerevise values(00036,015,00048,'116.32781267166137','40.01688422842362')",
    			"insert into linerevise values(00037,017,00049,'116.31843566894531','40.0365522511334')",
    			"insert into linerevise values(00038,019,00050,'116.30000352859497','40.06505013679061')",
    			"insert into linerevise values(00039,020,00050,'116.30038976669311','40.06720136654671')",
    			"insert into linerevise values(00040,021,00050,'116.30099058151245','40.068153797718764')",
    			"insert into linerevise values(00041,022,00050,'116.30375862121582','40.06992725476079')",
    			"insert into linerevise values(00042,023,00050,'116.30635499054223','40.0704362940865')",
    			"insert into linerevise values(00043,027,00053,'116.36750936508179','40.07018998521041')",
    			"insert into linerevise values(00044,028,00053,'116.38525485992431','40.063867752575646')",
    			"insert into linerevise values(00045,029,00053,'116.38697147369385','40.06263608053034')",
    			"insert into linerevise values(00046,031,00054,'116.45868301391601','40.03247780488371')",
    			"insert into linerevise values(00047,032,00054,'116.4594554901123','40.02812379289596')",
    			"insert into linerevise values(00048,035,00056,'116.43188238143921','39.970657679361146')",
    			"insert into linerevise values(00049,038,00058,'116.43469333648681','39.942663254075484')",
    	};
    	for(String sql:sqlse)
    	{
    		sld.execSQL(sql);
    	}
    	String[] sqlsf={
    			"insert into tranfrostop values(00001,'西直门站','116.3551452636719','39.940277770390324','地铁2号线')",
    			"insert into tranfrostop values(00002,'西直门站','116.3551452636719','39.940277770390324','地铁13号线')",
    			"insert into tranfrostop values(00003,'东直门站','116.43374919891357','39.941001650090925','地铁2号线')",
    			"insert into tranfrostop values(00004,'东直门站','116.43374919891357','39.941001650090925','地铁13号线')",
    			"insert into tranfrostop values(00005,'复兴门站','116.35658740997314','39.907234384502566','地铁2号线')",
    			"insert into tranfrostop values(00006,'复兴门站','116.35658740997314','39.907234384502566','地铁1号线')",
    			"insert into tranfrostop values(00007,'建国门站','116.43585205078125','39.90862522688575','地铁1号线')",
    			"insert into tranfrostop values(00008,'建国门站','116.43585205078125','39.90862522688575','地铁2号线')",
    	};
    	for(String sql:sqlsf)
    	{
    		sld.execSQL(sql);
    	}
    }
    
    //删除记录的方法
    public static void delete() throws Exception
    {
    	String sql1="delete from lines;";
    	sld.execSQL(sql1);
    	String sql2="delete from station;";
    	sld.execSQL(sql2);
    	String sql3="delete from ls;";
    	sld.execSQL(sql3);
    	String sql4="delete from linerevise;";
    	sld.execSQL(sql4);
    	String sql5="delete from temp;";
    	sld.execSQL(sql5);
    	String sql6="delete from tranfrostop;";
    	sld.execSQL(sql6);
    }  
    
    public static void initData()
    {
    	try
    	{
    	    createOrOpenDatabase();
	    	delete();
	    	insert();
	    	closeDatabase();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    /*=====================================begin==========================================================*/
    //获得线路名称列表
    public static List<String> searchLineList()
    {
    	List<String> list=new ArrayList<String>();
    	try{
    		createOrOpenDatabase();
    		String sql="select L_name from lines";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
	    	while(cur.moveToNext())
	    	{
	    		list.add(cur.getString(0));
	    		
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return list;
    	
    }
    //获得所有的站点的经纬度和站点名
    public static Vector<String> searchjwsn()
    {
    	Vector<String> result=new Vector<String>();
    	try{
    		createOrOpenDatabase();
    		String sql="select S_jd,S_wd,S_name from station";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
	    	while(cur.moveToNext())
	    	{
	    		result.add(cur.getString(0));
	    		result.add(cur.getString(1));
	    		result.add(cur.getString(2));
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return result;
    	
    }
    //获得指定线路对应的站列表
    public static List<String> searchStationList(String L_name)
    {
    	List<String> list=new ArrayList<String>();
    	try{
    		createOrOpenDatabase();
    		String sql="select station.S_name from lines,station,ls where ls.L_number=lines.L_number AND ls.S_number=station.S_number AND lines.L_name='"+L_name+"' group by station.S_name order by station.S_number";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
	    	while(cur.moveToNext())
	    	{
	    		list.add(cur.getString(0));
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return list;
    }
    //获得指定路线站的经纬度
    public static String[] searchJWD(String L_name,String S_name)
    {
    	String[] result=new String[2];
    	
    	try{
    		createOrOpenDatabase();
    		String sql="select station.S_jd,station.S_wd from lines,station,ls where ls.L_number=lines.L_number AND ls.S_number=station.S_number AND lines.L_name='"+L_name+"' AND station.S_name='"+S_name+"'";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
	    	if(cur.moveToNext())
	    	{
	    		result[0]=cur.getString(0);
	    		result[1]=cur.getString(1);
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return result;	
    }
    //获得所有线路站的经纬度
    public static List<String> searchTotaljw()
    {
    	List<String> list=new ArrayList<String>();
    	try
    	{
    		createOrOpenDatabase();
	    	String sql="select station.S_jd,station.S_wd,lines.L_name,station.S_name from lines,station,LS where LS.L_number=lines.L_number AND LS.S_number=station.S_number";
	    	Cursor cur=sld.rawQuery(sql, new String[]{});
	    	while(cur.moveToNext())
	    	{
	    		list.add(cur.getString(0));  //经度
	    		list.add(cur.getString(1));  //纬度
	    		list.add(cur.getString(2));  //线路名
	    		list.add(cur.getString(3));  //站名
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return list;    	
    	
    }
    
    public static Vector<String> searchSinglejw(String L_name)
    {
    	Vector<String> list=new Vector<String>();
    	try
    	{
    		createOrOpenDatabase();
	    	String sql="select station.S_jd,station.S_wd from station,ls,lines where station.S_number=ls.S_number and ls.L_number=lines.L_number and lines.L_name='"+L_name+"'";
	    	Cursor cur=sld.rawQuery(sql, new String[]{});
	    	while(cur.moveToNext())
	    	{
	    		list.add(cur.getString(0));  //经度
	    		list.add(cur.getString(1));  //纬度	
	    		//list.add(cur.getString(2));
	    	}
	    	cur.close();
	    	closeDatabase();
    	}
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return list;    	
    	
    }
  //将2部分查询结果放在一个新表中的 
    public static Vector<String> inserttotemp(String L_name)
    {
    	Vector<String> list=new Vector<String>();
    	try
    	{
    		createOrOpenDatabase();
	    	String sql1="insert into temp(num,jd,wd) select ls.num,station.S_jd,station.S_wd from ls,station,lines where ls.S_number=station.S_number AND ls.L_number=lines.L_number AND lines.L_name='"+L_name+"'";
	    	String sql2="insert into temp(num,jd,wd) select linerevise.num,linerevise.jd,linerevise.wd from linerevise,ls,lines,station where linerevise.XZ_ID=ls.XZ_id AND ls.S_number=station.S_number AND ls.L_number=lines.L_number AND lines.L_name='"+L_name+"'";
	    	String sql3="select jd,wd from temp order by num";
	    	String sql4="delete from temp;";
	    	sld.execSQL(sql1);
	    	sld.execSQL(sql2);	    	
	    	Cursor cur2=sld.rawQuery(sql3, new String[]{});	    	
	    	while(cur2.moveToNext())
	    	{
	    		list.add(cur2.getString(0));  //经度
	    		list.add(cur2.getString(1));  //纬度	 
	    		
	    	}
	    	//cur.close();
	    	sld.execSQL(sql4);
	    	//cur1.close();
	    	
	    	cur2.close();
	    	closeDatabase();
    	}
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return list;
 }
    
  
    //是否是中转站
    public static Vector<String> checktransforstop(String L_name)
    {
    	Vector<String> result=new Vector<String>();
    	try
    	{
    		createOrOpenDatabase();	    	
	    	String sql="select jd,wd from tranfrostop where L_name='"+L_name+"'";
	    	Cursor cur=sld.rawQuery(sql, new String[]{});	    		    	
	    	while(cur.moveToNext())
		    {
		    	result.add(cur.getString(0));  //经度
		    	result.add(cur.getString(1));  //纬度			    	
		    }
		    	
	    	cur.close();
	    	closeDatabase();
    	}
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return result;
    	
    }
   
    /*=====================================end==========================================================*/
}
