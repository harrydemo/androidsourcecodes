package com.kerence.mine.mineGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.kerence.mine.res.image.ImageIconFactory;

/**
 * 状态面板的属性的方法
 * 
 * @author Kerence
 * 
 */
public class JStatusPanel extends JPanel implements ActionListener
{
	private int leftMineCount;
	private JLED ledTimer = new JLED();
	private JLED ledMineCountLeft = new JLED();
	private JLabel expressionLabel = new JLabel(ImageIconFactory.getFaceSmile());
	private Timer timer = new Timer(1000, this);
	private MouseListener expressionLabelListener = new ExpressionListener();
	private JMineSweeperFrame mineSweeperFrame;

	/**
	 * 设置表情为惊讶
	 */
	public void setFaceSurprised()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSurprised());
	}

	/**
	 * 设置表情为快乐
	 */
	public void setFaceHappy()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceHappy());
	}

	/**
	 * 设置它的主窗口引用
	 * 
	 * @param s
	 *            主窗口引用
	 */
	public void addMineSweeperFrame(JMineSweeperFrame s)
	{
		this.mineSweeperFrame = s;
	}

	/**
	 * 设置表情按下的笑脸图标
	 */
	public void setFaceSmilePressed()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSmilePressed());
	}

	/**
	 * 计时器的监听处理方法 当计时器的数字为999时停止计时器
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{

		ledTimer.setNumber(ledTimer.getNumber() + 1);
		if (ledTimer.getNumber() == 999)
		{
			// 停止计数器
			timer.stop();
		}
	}

	/**
	 * 初始化状态面板
	 */
	public JStatusPanel()
	{
		// ledTimerPanel.setBackground(Color.green);
		// expressionPanel.setBackground(Color.blue);
		// ledMineCountPanel.setBackground(Color.pink);

		Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 2, 5), bevelBorder));
		this.setPreferredSize(new Dimension(100, 47));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(Box.createHorizontalStrut(5));// 填充不了哦。
		this.add(ledMineCountLeft);
		this.add(Box.createHorizontalGlue());
		this.add(expressionLabel);
		this.add(Box.createHorizontalGlue());
		this.add(ledTimer);
		this.add(Box.createHorizontalStrut(5));
		this.setDelay(1000);
		this.expressionLabel.addMouseListener(new ExpressionListener());
	}

	private boolean isPressed;

	/**
	 * 表情的鼠标监听器
	 * 
	 * @author Kerence
	 * 
	 */
	private class ExpressionListener extends MouseAdapter
	{

		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (expressionLabel == e.getSource() && isPressed)
			{
				expressionLabel.setIcon(ImageIconFactory.getFaceSmilePressed());
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			if (isPressed && e.getSource() == expressionLabel)
			{
				expressionLabel.setIcon(ImageIconFactory.getFaceSmile());
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (e.getButton() != MouseEvent.BUTTON1)
			{
				return;
			}
			if (e.getSource() == expressionLabel)
			{
				isPressed = true;
				expressionLabel.setIcon(ImageIconFactory.getFaceSmilePressed());
			} else
			{
				mineSweeperFrame.leftButtonPressedOnMineBlock();
			}

		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (isPressed && e.getSource() == expressionLabel)
			{
				isPressed = false;
				// 重置游戏

				mineSweeperFrame.resetGame();// 重置雷数和雷阵
			}
			expressionLabel.setIcon(ImageIconFactory.getFaceSmile());
		}

	}

	/**
	 * 设置延迟时间
	 * 
	 * @param delay
	 *            延迟时间以毫秒为单位
	 */
	public void setDelay(int delay)
	{
		timer.setDelay(delay);
	}

	/**
	 * 启动计时器
	 */
	public void setTimerStart()
	{
		timer.start();
	}

	/**
	 * 停止计时器
	 */
	public void setTimerStop()
	{
		timer.stop();
	}

	/**
	 * 启动计时器
	 */
	public void startsTimer()
	{
		timer.start();
	}

	/**
	 * 设置表情为哭
	 */
	public void setFaceCry()
	{
		this.expressionLabel.setIcon(ImageIconFactory.getFaceCry());
	}

	/**
	 * 得到计时器的当前时间
	 */
	public int getTimerValue()
	{
		return ledTimer.getNumber();
	}

	/**
	 * 设置计时器的时间
	 */
	private void setTimerValue(int n)
	{
		ledTimer.setNumber(0);
	}

	/**
	 * 停止计时器
	 */
	public void stopTimer()
	{
		this.timer.stop();
	}

	/**
	 * 设置表情为微笑
	 */
	public void setFaceSmile()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSmile());
	}

	/**
	 * 重置计时器
	 */
	public void resetTimer()
	{
		timer.stop();
		setTimerValue(0);

	}

	/**
	 * 设置剩余雷数的显示值
	 * 
	 * @param count
	 *            剩余雷数
	 */
	public void setLEDMineCountLeft(int count)
	{
		ledMineCountLeft.setNumber(count);
	}

	/**
	 * 得到计时器的显示值
	 * 
	 * @return 计时器的值
	 */
	public int getLEDTime()
	{
		return ledTimer.getNumber();
	}
}
