package com.eoe.andy;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;   
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheServer {

	/**
	 * @param args
	 */

	static boolean running=false;
	private static JFrame theFrame;
	private static final int  SERVERPORT = 12111; // 服务器端口
	private static List<Socket>    _clientList = new ArrayList<Socket>();  // 客户端连接
	private static ExecutorService _executorService;  // 线程池
	private static ServerSocket    _serverSocket;  // ServerSocket对象
	private static Statement statement;
	static DefaultListModel model;
	static InputStream in; 
	static Thread t;

	static boolean stop=true;
	
	static class StartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			
			
			stop=false;
			// TODO Auto-generated method stub
			t=new Thread(new Runnable() {
				@Override
				public void run() 
				{
					try
					{   // 设置服务器端口
						_serverSocket = new ServerSocket(SERVERPORT);
						// 创建一个线程池
						_executorService = Executors.newCachedThreadPool();
						System.out.println("等待Android客户端程序的连接...");

						// 用来临时保存客户端连接的Socket对象
						Socket client = null;
						while (!stop)
						{
							// 接收客户连接并添加到list中
						    try {
						    	client = _serverSocket.accept(); 
								_clientList.add(client);
								System.out.println(client.getInetAddress()+"Android客户端程序连接");
								// 开启一个客户端线程
								_executorService.execute(new ThreadServer(client)); 
						       } catch(IOException e ) {
						    	   System.out.println( "服务停止！" );
						       }	
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

				}
			});
			t.start();
		}

	}
	
	static class StopListener implements ActionListener{

		@SuppressWarnings("deprecation")
		@Override
		
		public void actionPerformed(ActionEvent arg0) {
			stop=true;
			// TODO Auto-generated method stub
			try {
				_serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	static private int checkLog(String uid,String pwd)
	{
		String sql = "select * from usertb where uid="+"'"+uid+"'";
		ResultSet rs ;
		try {
			rs = statement.executeQuery(sql);

			if(!rs.next())
			{
				return -1;
			}
			else 
			{
				if (rs.getString("pwd").equals(pwd)) 
				{
					return 0;

				} else 
				{
					return -2;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		} 

	}
	static private String getdairyall(String uid)
	{
		String sql = "select * from dairytb where userid="+"'"+uid+"'";
		ResultSet rs ;
		try {
			rs = statement.executeQuery(sql);
			int i=0;
			String allString="";
			while (rs.next()) 
			{
				i++;
				allString=allString+""+rs.getInt("dairynum")+"$"+rs.getString("content")+"$"+rs.getString("title")+"|";
			}
			allString=i+"|"+allString;
			return allString;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERR";
		} 

	}
	static private int NewDairy(String uid,String dairyTitle,String dairyText)
	{
		String sql = "insert into dairytb(dairynum,content,title,userid) values(null,'"+dairyText+"','"+dairyTitle+"','"+uid+"')";
		try {
			int rs = statement.executeUpdate(sql);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} 

	}
	static private int ChangeDairy(String dairyid,String dairyTitle,String dairyText)
	{
		String sql = "UPDATE dairytb SET content='"+dairyText+"',title='"+dairyTitle+"' where dairynum='"+dairyid+"'";
		try {
			int rs = statement.executeUpdate(sql);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} 

	}
	static private int newuser(String uid,String pwd)
	{
		String sqlnew = "insert into usertb (uid,pwd) values('"+uid+"','"+pwd+"')";

		try {

			if ((checkLog(uid, pwd)==-2)||checkLog(uid, pwd)==0) 
			{
				return -1;
			}
			else{
				int row=statement.executeUpdate(sqlnew);
				return row;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("sqlerro");
			return -2;
		} 

	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		theFrame=new JFrame("日记服务器");

		Button startButton=new Button("开始");
		Button stopButton=new Button("停止");

		JList lst = new JList(new DefaultListModel()); 
		model = (DefaultListModel) lst.getModel(); 

		JScrollPane scrlpane=new JScrollPane(lst);

		theFrame.setSize(600, 300);

		startButton.addActionListener(new StartListener());
		stopButton.addActionListener(new StopListener());
		
		JPanel thePanel=new JPanel();


		thePanel.setLayout(new FlowLayout());
		thePanel.add(startButton);
		thePanel.add(stopButton);


		theFrame.setLayout(new BorderLayout());
		theFrame.add(thePanel,BorderLayout.NORTH);
		theFrame.add(scrlpane,BorderLayout.SOUTH);


		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);


		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名

		String url = "jdbc:mysql://127.0.0.1:3306/dairydb";

		// MySQL配置时的用户名

		String user = "root";

		// Java连接MySQL配置时的密码

		String password = "wen200415";

		try {

			// 加载驱动程序

			Class.forName(driver);

			// 连续数据库

			Connection conn = DriverManager.getConnection(url, user, password);

			if(!conn.isClosed())

				System.out.println("Succeeded connecting to the Database!");

			statement=conn.createStatement();


		} catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} catch(SQLException e) {   
			e.printStackTrace();   
		} catch(Exception e) {   
			e.printStackTrace();   
		}   
	}  
	static class ThreadServer implements Runnable
	{
		private Socket                        _socket;
		private BufferedReader        _bufferedReader;
		private PrintWriter                _printWriter;
		private String                        _message;

		public ThreadServer(Socket socket) throws IOException
		{
			this._socket    = socket;
			_bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			in=socket.getInputStream();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			model.addElement("收到连接请求！----------------"+df.format(new java.util.Date()));

		}
		public void run()
		{
			try
			{
				while(true)
				{
					
				_message = _bufferedReader.readLine();	
		
					
					if (_message!=null) 
					{
						System.out.println(_message);
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
						model.addElement("收到信息！："+_message+ "--------------"+df.format(new java.util.Date()));
						if(_message.startsWith("#<LOGIN>#"))
						{  
							String contentString=_message.substring(9);
							String []saStrings=contentString.split("\\|");
							int res=checkLog(saStrings[0], saStrings[1]);
							if (res==0) 
							{
								_message = "#<LOGYES>#";
								_message=_message+getdairyall(saStrings[0]); 
							}
							if (res==-1) 
							{
								_message = "#<NOUSER>#";
							}
							if (res==-2) 
							{
								_message = "#<WPWD>#";
							}
							sendMessage(_message);  
						}
						else
						{
							if(_message.startsWith("#<REG>#"))
							{
								String contentString=_message.substring(7);
								String []saStrings=contentString.split("\\|");
								int res=newuser(saStrings[0], saStrings[1]);
								if (res>=0) 
								{
									_message = "#<REGYES>#";
								}
								if (res==-1) 
								{
									_message = "#<REPUID>#";
								}
								if (res==-2) 
								{
									_message = "#<SEREE>#";
								}
								sendMessage(_message);   
							}
							else
							{
								if(_message.startsWith("#<NEWDAIRY>#"))
								{
									System.out.println(_message);
									String contentString=_message.substring(12);
									String []saStrings=contentString.split("\\|");
									
									if (saStrings[2].endsWith("#<end>#")) 
									{
										int res=NewDairy(saStrings[0], saStrings[1],saStrings[2].substring(0, saStrings[2].length()-7));
										System.out.println(res);
										if (res>=0) 
										{
											_message = "#<SAVEYES>#";
										}
										if (res<0) 
										{
											_message = "#<SEERR>#";
										}
									} 
									else 
									{
										String tp,tpall = "";
										while((tp=_bufferedReader.readLine())!=null)
										{
											tpall=tpall+tp;
											if(tp.endsWith("#<end>#"))
											{
												break;
											}
										}
										System.out.println(tpall);
										int res=NewDairy(saStrings[0], saStrings[1],saStrings[2]+tpall.substring(0, tpall.length()-7));
										System.out.println(res);
										if (res>=0) 
										{
											_message = "#<SAVEYES>#";
										}
										if (res<0) 
										{
											_message = "#<SEERR>#";
										}
									};
									

									

									sendMessage(_message);   
								}
								else
								{
									if(_message.startsWith("#<GETDAIRY>#"))
									{
										String contentString=_message.substring(12);
										if (getdairyall(contentString)=="ERR") 
										{
											_message="#<GETERR>#";
										} 
										else 
										{
											_message="#<GETOK>#"+getdairyall(contentString);
										}
										sendMessage(_message);   
									}
									else 
									{
										if(_message.startsWith("#<CHADAIRY>#"))
										{
											System.out.println(_message);
											String contentString=_message.substring(12);
											String []saStrings=contentString.split("\\|");


											int res=ChangeDairy(saStrings[0], saStrings[1], saStrings[2]);
											if (res>=0) 
											{
												_message="#<CHOK>#";

											} 
											else 
											{
												_message="#<SEREE>#";

											}
											sendMessage(_message);   
										}
									}
								}
							}
						}


					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		} 
		private void sendMessage(String message) throws IOException
		{

			for (Socket client : _clientList)
			{
				_printWriter = new PrintWriter(client.getOutputStream(), true);
				_printWriter.println(message);
			}
		}
	}
}

