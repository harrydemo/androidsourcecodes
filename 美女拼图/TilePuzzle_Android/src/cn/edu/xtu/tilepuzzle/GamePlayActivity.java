package cn.edu.xtu.tilepuzzle;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class GamePlayActivity extends Activity {
	private ClassBoardModel classBoardModel ;
	/** ���ڵĿ��*/
	private int screenWidth = 0;
	/**  ���ڵĸ߶�*/
	private int screenHeight = 0;
	
	private ClassSetScreenWH classSetScreenWH;
	//private BoardView boardView;
	
	private Menu menu;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		classSetScreenWH=new ClassSetScreenWH(GamePlayActivity.this);
		//�������п���ʡȥ
        this.screenHeight=classSetScreenWH.getScreenHeight();
        this.screenWidth=classSetScreenWH.getScreenWidth();
        
        Log.d("GPA", "��ȡGamePlayActivity��Ļ���Գɹ�");        
        
        this.classBoardModel = (ClassBoardModel) getApplication();   
        this.classBoardModel.setScreenWidth(this.screenWidth);
        this.classBoardModel.setScreenHeight(this.screenHeight);
        this.classBoardModel.initData();
        
        init();
        
        //this.boardView=new BoardView(GamePlayActivity.this, this.boardModel);
        setContentView(new BoardView(GamePlayActivity.this, this.classBoardModel));
	}
	
	private void init(){
		// ����Ϸ
		if (classBoardModel.getGameState() != ClassGameDB.PLAYING) {
			
			// ��ͼƬ���/ȥ������
			if (classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_addString].equals("Y")) {
				classBoardModel.addString();
			} else 
				classBoardModel.removeString();
			
			System.out.println("��ʼ��Ϸ......");
			//Ȥζϴ��
			if (classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_funny].equals("Y")) {
				if(classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_hard].equals("Y")){
					classBoardModel.rearrangeFunnily(true);
				}else {
					classBoardModel.rearrangeFunnily(false);
				}
			}else{//����Ȥζϴ��
				if(classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_hard].equals("Y"))
					classBoardModel.randomize(true);
				else {
					classBoardModel.randomize(false);
				}
			}

			classBoardModel.starTime = System.currentTimeMillis();
			classBoardModel.sumTime = 0;
		}
		classBoardModel.setGameState(ClassGameDB.PLAYING);
	}
	
	/*����menu*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		//����menu����Ϊres/menu/menu.xml
		inflater.inflate(R.menu.menuplay, menu);
		this.menu=menu;
		
		menu.findItem(R.menu_play_id.continueItem).setEnabled(false);
		menu.findItem(R.menu_play_id.stopItem).setEnabled(true);
		return true;
	}
/*
	@Override	
	public void onCreateContextMenu(Menu menu, View v,ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		//����menu����Ϊres/menu/menu.xml
		inflater.inflate(R.menu.menuplay, menu);
		this.menu=menu;
		//super.onCreateContextMenu(menu, v, menuInfo);
		super.onCreateContextMenu(menu, boardView, menuInfo);
	}
*/
	/*����˵��¼�*/
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//�õ���ǰѡ�е�MenuItem��ID,
		int item_id = item.getItemId();

		switch (item_id)
		{
			case R.menu_play_id.stopItem:
				/* �½�һ��Intent���� */
				//Intent intent = new Intent();
				/* ָ��intentҪ�������� */
				//intent.setClass(Activity01.this, Activity02.class);
				/* ����һ���µ�Activity */
				//startActivity(intent);
				/* �رյ�ǰ��Activity */
				//Activity01.this.finish();
				Log.d("MENU", "stopItem:id = "+item_id);		
				//item.isEnabled();
				//item.setEnabled(false);
				item.setEnabled(false);
				menu.findItem(R.menu_play_id.continueItem).setEnabled(true);
				
				classBoardModel.sumTime+=System.currentTimeMillis()-classBoardModel.starTime;				
				classBoardModel.starTime=0;
				Log.d("TIME", classBoardModel.getTimeStringByS(classBoardModel.sumTime));
				
				break;
			case R.menu_play_id.exitItem:
				Log.d("MENU", "exitItem:id = "+item_id);	
				GamePlayActivity.this.finish();
				this.onDestroy();
				break;
			case R.menu_play_id.continueItem:
				Log.d("MENU", "continueItem:id = "+item_id);
				item.setEnabled(false);
				menu.findItem(R.menu_play_id.stopItem).setEnabled(true);
				classBoardModel.starTime = System.currentTimeMillis();
				break;
			case R.menu_play_id.flagItem:
				Log.d("MENU", "continueItem:id = "+item_id);
				
				if (classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_addString].equals("N")) {
					classBoardModel.addString();
					classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_addString]="Y";
				} else if (classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_addString].equals("Y")){
					classBoardModel.removeString();
					classBoardModel.gameSetData[ClassGameDB.IndexInGameSetDatat_addString]="N";
				}
				classBoardModel.classSQLite.updateGameSetData(classBoardModel.gameSetData);
				break;
			case R.menu_play_id.testItem:
				Log.d("MENU", "testItem:id = "+item_id);
				 classBoardModel.setGameState(ClassGameDB.WON);
				 this.showWON(classBoardModel.sumTime);
				break;
			default:
				break;
		}
		return true;
	}
	ProgressDialog m_Dialog;
	/**
	 * ����ʤ��Dialog����
	 * */
	public void showWON(long sumTime) {
		Dialog dialog = new AlertDialog.Builder(GamePlayActivity.this)
				.setTitle("��Ϸ����")// ���ñ���
				.setMessage("����ʱ��"+classBoardModel.getTimeStringByS(sumTime)+".�Ƿ�����������ֵ�����б�")// ��������
				.setPositiveButton("���",// ����ȷ����ť
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// ProgressDialog m_Dialog;
								// �����ȷ����ת���½��
								LayoutInflater factory = LayoutInflater
										.from(GamePlayActivity.this);
								// �õ��Զ���Ի���
								final View DialogView = factory.inflate(
										R.layout.dialog_won, null);
								// �����Ի���
								AlertDialog dlg = new AlertDialog.Builder(
										GamePlayActivity.this)
										.setTitle("�������")
										.setView(DialogView)
										// �����Զ���Ի������ʽ
										.setPositiveButton(
												"ȷ��", // ����"ȷ��"��ť
												new DialogInterface.OnClickListener() // �����¼�����
												{
													public void onClick(DialogInterface dialog, int whichButton) {														
														m_Dialog = ProgressDialog.show(
																		GamePlayActivity.this,
																		"��ȴ�...",
																		"����Ϊ�����...",
																		true);
														/***************/
														String userName=((EditText) DialogView.findViewById(R.id.usernameEditText)).getText().toString();
														System.out.println(userName);
														classBoardModel.classSQLite.addUserInfo(userName, classBoardModel.sumTime/1000);
														classBoardModel.showUserInfo();
														/********************/
														new Thread() {
															public void run() {
																try {
																	sleep(3000);
																} catch (Exception e) {
																	e
																			.printStackTrace();
																} finally {
																	// ��¼������ȡ��m_Dialog�Ի���
																	m_Dialog.dismiss();
																}
															}
														}.start();
														// ���"�˳�"��ť֮���Ƴ�����
														GamePlayActivity.this.finish();
														Intent intent =new Intent();				
														intent.setClass(GamePlayActivity.this, ShowUserInfoActivity.class);
														GamePlayActivity.this.startActivity(intent);
													}
													
												})
										.setNegativeButton("����", // ���á�ȡ������ť
												new DialogInterface.OnClickListener() {
													public void onClick( DialogInterface dialog, int whichButton) {
														// ���"ȡ��"��ť֮���˳�����
														GamePlayActivity.this.finish();
													}
												}).create();// ����
								dlg.show();// ��ʾ
							}
						}).setNeutralButton("�����",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// ���"�˳�"��ť֮���Ƴ�����
								GamePlayActivity.this.finish();
							}
						}).create();// ������ť

		// ��ʾ�Ի���
		dialog.show();
		
	}
	
	@Override
	protected void onStart() {
		System.out.println("GamePlayActivity===>>onStart");	
		super.onStart();
	}

	@Override
	protected void onRestart() {
		System.out.println("GamePlayActivity===>>onRestart");
		super.onRestart();		
	}

	@Override
	protected void onResume() {
		System.out.println("GamePlayActivity===>>onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		System.out.println("GamePlayActivity===>>onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("GamePlayActivity===>>onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		System.out.println("GamePlayActivity====>>onDestroy");
		super.onDestroy();
	}
}
