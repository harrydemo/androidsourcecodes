/**
 * ���������޸�login���е��û�������
 */
package com.face;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Model.EmpModel;

import com.Tools.*;

public class ChangePass extends JDialog implements ActionListener
{

	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2, jl3, jl4;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JButton jb1, jb2;
	EmpModel em;
	boolean changpass_b;

	public ChangePass(JFrame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		this.setBounds(400, 250, 400, 400);
		this.setLayout(null);// ���޸�����ĶԻ�������Ϊ�ղ���
		jp1 = new JPanel();

		jl4 = new JLabel("�������û���:");
		jl4.setFont(MyTools.f1);
		jl4.setBounds(40, 0, 120, 40);
		this.add(jl4);
		jl1 = new JLabel("������ԭ����:");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(40, 60, 120, 40);
		this.add(jl1);
		jl2 = new JLabel("������������: ");
		jl2.setFont(MyTools.f1);
		jl2.setBounds(40, 120, 120, 40);
		this.add(jl2);
		jl3 = new JLabel("���ٴ����������룺");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(40, 180, 150, 40);
		this.add(jl3);

		jtf4 = new JTextField();
		jtf4.setBounds(180, 6, 170, 25);
		this.add(jtf4);
		/*
		 * jtf1 = new JTextField(); jtf1.setBounds(180, 65, 170, 25);
		 * this.add(jtf1); jtf2 = new JTextField(); jtf2.setBounds(180, 125,
		 * 170, 25); this.add(jtf2); jtf3 = new JTextField();
		 * jtf3.setBounds(180, 185, 170, 25); this.add(jtf3);
		 */
		jtf1 = new JPasswordField();
		jtf1.setBounds(180, 65, 170, 25);
		this.add(jtf1);
		jtf2 = new JPasswordField();
		jtf2.setBounds(180, 125, 170, 25);
		this.add(jtf2);
		jtf3 = new JPasswordField();
		jtf3.setBounds(180, 185, 170, 25);
		this.add(jtf3);

		jb1 = new JButton("ȷ��");
		jb1.addActionListener(this);
		jb1.setBounds(120, 280, 60, 30);
		this.add(jb1);
		jb2 = new JButton("ȡ��");
		jb2.addActionListener(this);
		jb2.setBounds(240, 280, 60, 30);
		this.add(jb2);

		// jtf1.setBounds(4)

		/*
		 * jp1.setLayout(new GridLayout(3,1));
		 * 
		 * jl1 = new JLabel("������ԭ���룺          "); jp1.add(jl1); jl2 = new
		 * JLabel("�����������룺          "); jp1.add(jl2); jl3 = new
		 * JLabel("���ٴ����������룺"); jp1.add(jl3);
		 * 
		 * jp2 = new JPanel();
		 * 
		 * jp2.setLayout(new GridLayout(3,1)); jtf1 = new JTextField(20);
		 * jp2.add(jtf1); jtf2 = new JTextField(20); jp2.add(jtf2); jtf3 = new
		 * JTextField(20); jp2.add(jtf3);
		 * 
		 * jp3 = new JPanel(); jb1 = new JButton("ȷ��");
		 * jb1.addActionListener(this); jp3.add(jb1); jb2 = new JButton("ȡ��");
		 * jb2.addActionListener(this); jp3.add(jb2);
		 * 
		 * 
		 * this.add(jp1,BorderLayout.WEST); this.add(jp2,BorderLayout.CENTER);
		 * this.add(jp3,BorderLayout.SOUTH);
		 */
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{

		/*
		 * //�����޸�����ķ�ʽ��Ҫ�������ԭ���� if(arg0.getSource()==jb1) {
		 * 
		 * this.remove(jtf1); //�Ƴ����???????
		 * 
		 * this.getContentPane().remove(jtf1); this.getContentPane().repaint();
		 * 
		 * 
		 * 
		 * em = new EmpModel(); String get_jtf4 = jtf4.getText();
		 * System.out.print("     ���ı���1�������������Ϊ:"+get_jtf4); String paras1 [] =
		 * {get_jtf4}; em.query("select * from login where userid = ?", paras1);
		 * changpass_b = em.b; if(!changpass_b) {
		 * JOptionPane.showMessageDialog(this, "��������û��������ڣ����������룡"); } else
		 * if(!jtf2.getText().equals(jtf3.getText())) // else if(2!=2) {
		 * System.out
		 * .println("jtf2�������ֵΪ:"+jtf2.getText()+"jtf3�������ֵΪ:"+jtf3.getText());
		 * JOptionPane.showMessageDialog(this, "��������������벻һ�£����������룡"); } else
		 * if(changpass_b &&jtf2.getText().equals(jtf3.getText())) {
		 * System.out.println("jtf2.getText()==jtf3.getText()"); em = new
		 * EmpModel(); String [] paras_changepass = {jtf2.getText()}; String sql
		 * = "update login set password = ?"; em.updEmp(sql, paras_changepass);
		 * if(jtf2.getText().equals("")) { JOptionPane.showMessageDialog(this,
		 * "�����޸ĳɹ���������Ϊ����ֵ"); }else if(!jtf2.getText().equals("")) {
		 * JOptionPane.showMessageDialog(this, "�����޸ĳɹ���������Ϊ��"+jtf2.getText()); }
		 * 
		 * this.dispose(); } }
		 */

		// �����޸�����ķ�ʽҪ�������ԭ���������²����޸�
		if (arg0.getSource() == jb1)
		{
			em = new EmpModel();
			String get_jtf1 = jtf1.getText();
			System.out.print("     ���ı���1�������������Ϊ:" + get_jtf1);
			String paras1[] =
			{ get_jtf1 };
			em.query("select * from login where password = ?", paras1);
			changpass_b = em.b;
			if (!changpass_b)
			{
				JOptionPane.showMessageDialog(this, "ԭ��������������������룡");
			}

			// else if(!jtf2.getText()==jtf3.getText()) //����д����cc++��д��������java��д��
			else if (!jtf2.getText().equals(jtf3.getText()))
			// else if(2!=2)
			{
				System.out.println("jtf2�������ֵΪ:" + jtf2.getText() + "jtf3�������ֵΪ:" + jtf3.getText());
				JOptionPane.showMessageDialog(this, "��������������벻һ�£����������룡");
			}

			// else if(changpass_b &&jtf2.getText()==jtf3.getText())

			else if (changpass_b && jtf2.getText().equals(jtf3.getText()))
			{
				System.out.println("jtf2.getText()==jtf3.getText()");
				em = new EmpModel();
				String[] paras_changepass =
				{ jtf2.getText() };
				String sql = "update login set password = ?";
				em.updEmp(sql, paras_changepass);
				if (jtf2.getText().equals(""))
				{
					JOptionPane.showMessageDialog(this, "�����޸ĳɹ���������Ϊ����ֵ");
				} else if (!jtf2.getText().equals(""))
				{
					// JOptionPane.showMessageDialog(this,
					// "�����޸ĳɹ���������Ϊ��"+jtf2.getText());
					JOptionPane.showMessageDialog(this, "�����޸ĳɹ�");
				}

				this.dispose();
			}
		}

		else if (arg0.getSource() == jb2)
		{
			this.dispose();
		}

	}
}
