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
import javax.swing.JTextField;

import com.Model.EmpModel;

import com.Tools.*;

public class Help extends JDialog
{

	JPanel jp1, jp2, jp3;
	JLabel jl1, jl2, jl3, jl4;
	EmpModel em;
	boolean changpass_b;

	public Help(JFrame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		this.setBounds(350, 230, 780, 300);
		this.setLayout(null);// ���޸�����ĶԻ�������Ϊ�ղ���
		jp1 = new JPanel();

		jl4 = new JLabel("��������¹������ɶԾƵ�Ա���Ļ�����Ϣ���й���");
		jl4.setFont(MyTools.f1);
		jl4.setBounds(40, 0, 1000, 40);
		this.add(jl4);
		jl1 = new JLabel("�������¼�����������޸�����");
		jl1.setFont(MyTools.f1);
		jl1.setBounds(40, 60, 1000, 40);
		this.add(jl1);
		jl2 = new JLabel("������˿���Ϣ���������Թ�������ס�Ĺ˿͵Ļ�����Ϣ����������ŵȣ��������ڴ˽��н��� ");
		jl2.setFont(MyTools.f1);
		jl2.setBounds(40, 120, 1000, 40);
		this.add(jl2);
		jl3 = new JLabel("�����������Ϣ���������Բ鿴�͹���Ƶ귿���״̬���۸�");
		jl3.setFont(MyTools.f1);
		jl3.setBounds(40, 180, 1000, 40);
		this.add(jl3);
		this.setVisible(true);
	}
}
