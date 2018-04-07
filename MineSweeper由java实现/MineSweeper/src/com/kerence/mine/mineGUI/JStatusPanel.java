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
 * ״̬�������Եķ���
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
	 * ���ñ���Ϊ����
	 */
	public void setFaceSurprised()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSurprised());
	}

	/**
	 * ���ñ���Ϊ����
	 */
	public void setFaceHappy()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceHappy());
	}

	/**
	 * ������������������
	 * 
	 * @param s
	 *            ����������
	 */
	public void addMineSweeperFrame(JMineSweeperFrame s)
	{
		this.mineSweeperFrame = s;
	}

	/**
	 * ���ñ��鰴�µ�Ц��ͼ��
	 */
	public void setFaceSmilePressed()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSmilePressed());
	}

	/**
	 * ��ʱ���ļ��������� ����ʱ��������Ϊ999ʱֹͣ��ʱ��
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{

		ledTimer.setNumber(ledTimer.getNumber() + 1);
		if (ledTimer.getNumber() == 999)
		{
			// ֹͣ������
			timer.stop();
		}
	}

	/**
	 * ��ʼ��״̬���
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
		this.add(Box.createHorizontalStrut(5));// ��䲻��Ŷ��
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
	 * �������������
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
				// ������Ϸ

				mineSweeperFrame.resetGame();// ��������������
			}
			expressionLabel.setIcon(ImageIconFactory.getFaceSmile());
		}

	}

	/**
	 * �����ӳ�ʱ��
	 * 
	 * @param delay
	 *            �ӳ�ʱ���Ժ���Ϊ��λ
	 */
	public void setDelay(int delay)
	{
		timer.setDelay(delay);
	}

	/**
	 * ������ʱ��
	 */
	public void setTimerStart()
	{
		timer.start();
	}

	/**
	 * ֹͣ��ʱ��
	 */
	public void setTimerStop()
	{
		timer.stop();
	}

	/**
	 * ������ʱ��
	 */
	public void startsTimer()
	{
		timer.start();
	}

	/**
	 * ���ñ���Ϊ��
	 */
	public void setFaceCry()
	{
		this.expressionLabel.setIcon(ImageIconFactory.getFaceCry());
	}

	/**
	 * �õ���ʱ���ĵ�ǰʱ��
	 */
	public int getTimerValue()
	{
		return ledTimer.getNumber();
	}

	/**
	 * ���ü�ʱ����ʱ��
	 */
	private void setTimerValue(int n)
	{
		ledTimer.setNumber(0);
	}

	/**
	 * ֹͣ��ʱ��
	 */
	public void stopTimer()
	{
		this.timer.stop();
	}

	/**
	 * ���ñ���Ϊ΢Ц
	 */
	public void setFaceSmile()
	{
		expressionLabel.setIcon(ImageIconFactory.getFaceSmile());
	}

	/**
	 * ���ü�ʱ��
	 */
	public void resetTimer()
	{
		timer.stop();
		setTimerValue(0);

	}

	/**
	 * ����ʣ����������ʾֵ
	 * 
	 * @param count
	 *            ʣ������
	 */
	public void setLEDMineCountLeft(int count)
	{
		ledMineCountLeft.setNumber(count);
	}

	/**
	 * �õ���ʱ������ʾֵ
	 * 
	 * @return ��ʱ����ֵ
	 */
	public int getLEDTime()
	{
		return ledTimer.getNumber();
	}
}
