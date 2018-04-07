package com.kerence.mine.mineGUI;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.kerence.mine.data_structure.MineMatrix;
import com.kerence.mine.mineGUI.dialog.JEnterNameDialog;
import com.kerence.mine.mineGUI.menu.JMineMenuBar;
import com.kerence.mine.mine_model.Strategy.MineMatrixSettable;
import com.kerence.mine.mine_model.Strategy.PreliminaryLevel;
import com.kerence.mine.record.Rank;

public class JMineSweeperFrame extends JFrame implements WindowListener
{
	JMineMenuBar menuBar = new JMineMenuBar();
	JStatusPanel statusPanel = new JStatusPanel();
	JMineMatrixPanel jMineMatrixPanel = new JMineMatrixPanel();
	public List<Rank> preliminaryLevelRanks = new LinkedList<Rank>();
	public List<Rank> intermediateLevelRanks = new LinkedList<Rank>();
	public List<Rank> advancedLevelRanks = new LinkedList<Rank>();

	/**
	 * 构造方法 对扫雷界面进行初始化，等级为初级，初始化菜单，添加标题栏图标，等操作
	 */
	public JMineSweeperFrame()
	{
		// this.setResizable(false);
		this.setIconImage(new ImageIcon("./image/icon.gif").getImage());
		this.setTitle("扫雷");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(menuBar);
		this.add(statusPanel, BorderLayout.NORTH);
		this.add(jMineMatrixPanel);
		// 初始为初级
		jMineMatrixPanel.addMineSweeperFrame(this);
		statusPanel.addMineSweeperFrame(this);
		setGameMode(new PreliminaryLevel());
		this.addWindowListener(this);
		menuBar.setMineSweeperFrame(this);
		this.setResizable(false);
		this.pack();
	}

	/**
	 * 设置是否开始游戏 用于表示游戏的开始状态
	 * 
	 * @param b
	 *            游戏开始状态
	 */
	public void setIsFirstDig(boolean b)
	{
		isFirstDig = b;
	}

	/**
	 * 得到当前游戏模式中的雷的个数
	 * 
	 * @return 当前游戏模式中雷的个数
	 */
	public int getMineCount()
	{
		return this.jMineMatrixPanel.getMineCount();
	}

	/**
	 * 得到当前游戏模式中的行数
	 * 
	 * @return 当前游戏模式中的行数
	 */
	public int getRowCount()
	{
		return this.jMineMatrixPanel.getRowCount();
	}

	/**
	 * 得到当前游戏模式中的列数
	 * 
	 * @return 当前游戏模式中的列数
	 */
	public int getColumnCount()
	{
		return this.jMineMatrixPanel.getColumnCount();
	}

	private boolean isFirstDig = true;
	private boolean button1Pressed;
	private boolean button3Pressed;
	private JMineBlock currentMineBlock;

	private JMineBlock[][] jMineMatrix;

	private MineMatrix mineMatrix = new MineMatrix();

	/**
	 * 游戏开始 激活状态栏的计时器
	 */
	public void gameCommences()
	{
		// 开始计时
		this.statusPanel.startsTimer();
		// this.jMineMatrixPanel.resetGame();这是不用滴
	}

	/**
	 * 游戏失败 停止状态栏的计时器并将表情设置为哭泣
	 */
	public void gameOver()
	{// 游戏结束
		// 终止游戏 并把笑脸设置为哭泣
		this.statusPanel.stopTimer();
		this.statusPanel.setFaceCry();
	}

	/**
	 * 游戏完成 刷新雷区界面，全部标上红旗，停止计算器，把状态栏的雷数设置为0，跳出输入姓名对话框，并把姓名，等级，时间记录到集合框架中
	 */
	public void gameComplete()
	{// 完成游戏
		// 终止游戏 并把笑脸设置为快乐
		jMineMatrixPanel.refreshMineMatrixPanel();
		this.statusPanel.stopTimer();
		this.statusPanel.setLEDMineCountLeft(0);
		this.statusPanel.setFaceHappy();
		// 判断是哪个等级的
		int time = this.statusPanel.getLEDTime();// 得到计时器上的时间。

		// 弹出对话框输入大名。

		if (getGameModeName().equals("PreliminaryLevel"))
		{// 如果是完成初级
			String name = getHeorName();
			preliminaryLevelRanks.add(new Rank("初级", time, name));
		} 
		else if (getGameModeName().equals("IntermediateLevel"))
		{// 如果是完成中级
			String name = getHeorName();
			intermediateLevelRanks.add(new Rank("中级", time, name));
		} 
		else if (getGameModeName().equals("AdvancedLevel"))
		{// 如果是完成高级
			String name = getHeorName();
			advancedLevelRanks.add(new Rank("高级", time, name));
		}

	}

	/**
	 * 弹出一个对话框显示输入英雄名
	 * 
	 * @return 输入的姓名
	 */
	private String getHeorName()
	{
		JEnterNameDialog d = new JEnterNameDialog();
		d.showDialog(this);
		return d.getHeroName();
	}

	/**
	 * 得到当前游戏模式的名称
	 * 只有四种PreliminaryLevel,IntermediateLevel,AdvancedLevel,SelfDefinedLevel
	 * 
	 * @return 模式名称字符串
	 */
	private String getGameModeName()
	{
		String str[] = this.getGameMode().split("[.]");
		return str[str.length - 1];
	}

	/**
	 * 在雷区上按下左键的操作，将状态置为惊讶
	 */
	public void leftButtonPressedOnMineBlock()
	{
		this.statusPanel.setFaceSurprised();
	}

	/**
	 * 得到游戏模式，返回的是游戏模式的包名和类名
	 */
	private String getGameMode()
	{
		return this.jMineMatrixPanel.getGameMode();
	}

	/**
	 * 左键在雷区上放开 将表情置为笑脸
	 */
	public void leftButtonReleasedOnMineBlock()
	{
		// 在表情上放左键意味着重置游戏 在雷区放左键意味着挖雷 就是把状态栏里的表情变成微笑
		this.statusPanel.setFaceSmile();
	}

	/**
	 * 设置游戏模式
	 * 
	 * @param s
	 *            游戏模式对象
	 */
	public void setGameMode(MineMatrixSettable s)
	{// 设置游戏模式
		// 在游戏模式更改和Frame的构造函数中都会调用这个方法
		// 将雷区重置
		this.statusPanel.setFaceSmile();
		this.jMineMatrixPanel.setMineMatrix(s);
		// 将状态栏重置
		// int mineCount = this.jMineMatrixPanel.getMineCount();
		// // 初始化剩余雷数
		// statusPanel.setLEDMineCountLeft(mineCount);
		// // 停止计时器
		// statusPanel.stopTimer();

		resetGame();

	}

	/**
	 * 设置状态栏中剩余雷数的值
	 * 
	 * @param count
	 *            剩余雷数
	 */
	public void setLEDMineCountLeft(int count)
	{
		this.statusPanel.setLEDMineCountLeft(count);
	}

	/**
	 * 重置游戏 ，重置计时器，设置剩余雷数的值，将表情设置为笑脸，重置雷区，刷新界面
	 */
	public void resetGame()
	{// 重置游戏 对状态栏和雷进行重置
		// 刷新扫雷界面
		this.jMineMatrixPanel.resetGame();// 这里貌似有问题 其实没问题 是下面没有加pack语句.
		this.statusPanel.setLEDMineCountLeft(this.jMineMatrixPanel.getMineCount());
		this.statusPanel.setFaceSmile();
		this.statusPanel.resetTimer();// 重置计时器
		this.jMineMatrixPanel.refreshMineMatrixPanel();
		this.pack();
	}

	/**
	 * 设置可否标记
	 * 
	 * @param b
	 *            标记值
	 */
	public void setMarkable(boolean b)
	{
		this.jMineMatrixPanel.setMarkable(b);
	}

	/**
	 * 设置外挂是否打开
	 * 
	 * @param b
	 *            外挂是否打开
	 */
	public void setRabot(boolean b)
	{
		this.jMineMatrixPanel.setRabot(b);
		// 切换作弊状态后要刷新屏幕哦
		this.jMineMatrixPanel.refreshMineMatrixPanel();
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 窗口从最小化中恢复，重新启动计时器
	 */
	@Override
	public void windowDeiconified(WindowEvent e)
	{
		this.statusPanel.setTimerStart();
	}

	/**
	 * 窗口最小化时，停止计时器
	 */
	@Override
	public void windowIconified(WindowEvent e)
	{
		this.statusPanel.setTimerStop();
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

}
