// KeyexeDlg.cpp : implementation file
//
//	Disclaimer
//	----------
//	THIS SOFTWARE AND THE ACCOMPANYING FILES ARE DISTRIBUTED "AS IS" AND WITHOUT
//	ANY WARRANTIES WHETHER EXPRESSED OR IMPLIED. NO REPONSIBILITIES FOR POSSIBLE
//	DAMAGES OR EVEN FUNCTIONALITY CAN BE TAKEN. THE USER MUST ASSUME THE ENTIRE
//	RISK OF USING THIS SOFTWARE.
//
//	Terms of use
//	------------
//	THIS SOFTWARE IS FREE FOR PERSONAL USE OR FREEWARE APPLICATIONS.
//	IF YOU USE THIS SOFTWARE IN COMMERCIAL OR SHAREWARE APPLICATIONS YOU
//	ARE GENTLY ASKED TO SEND ONE LICENCED COPY OF YOUR APPLICATION(S)
//	TO THE AUTHOR. IF YOU WANT TO PAY SOME MONEY INSTEAD, CONTACT ME BY
//	EMAIL. YOU ARE REQUESTED TO CONTACT ME BEFORE USING THIS SOFTWARE
//	IN YOUR SHAREWARE/COMMERCIAL APPLICATION.
//
//	Contact info:
//	Site: http://bizkerala.hypermart.net
//	Email: anoopt@gmx.net
//----------------------------------------------------------------------------------
//
//	Usage Instructions: See Readme.txt for Both 'Keyexe' & 'Keydll3' projects.
//
//----------------------------------------------------------------------------------
#include "stdafx.h"
#include "Keyexe.h"
#include "KeyexeDlg.h"
#include "Keydll3.h"//Include this for functions in the DLL.
//#include "..\Keydll3\Keydll3.h"//Include this for functions in the DLL.

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

#define WM_KEYSTROKE (WM_USER + 755)//This message is recieved when key is down/up
#define WM_NC (WM_USER+1001)

#define SOCKET_PORT htons(0xE917);//7016
//#define SOCKET_PORT 59670

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog used for App About

SOCKET ServerSock;
SOCKET connectSock;
struct sockaddr_in dstclient_addr;
char recv_message_server[256] = {0};
char      send_message[256] = {0};

HANDLE hThread;
DWORD ThreadID;
volatile BOOL ThreadRun = false;

const char* WINDOWNHANDLERNAME = "gjz.control.pc";
void socketMoveMouse(int direction,int step);
void analyseCommand(char *message);

//server
BOOL InItServerSock()
{
	//Variable Define
	int Status;
	WORD wMajorVersion,wMinorVersion;
	WORD wVersionReqd;
	WSADATA lpmyWSAData;
	
	//InI Winsock
	wMajorVersion = 1;
	wMinorVersion = 2;
	wVersionReqd = MAKEWORD(wMajorVersion,wMinorVersion);

	//Startup WinSock
	Status = WSAStartup(wVersionReqd,&lpmyWSAData);	
	if (Status != 0)
		return FALSE;	

	//Socket
	ServerSock = socket(AF_INET,SOCK_STREAM,0);
	if (ServerSock == INVALID_SOCKET)
		return FALSE;

	dstclient_addr.sin_family = PF_INET;
	dstclient_addr.sin_port = SOCKET_PORT;
	dstclient_addr.sin_addr.s_addr = INADDR_ANY;
	
	//BIND
	Status = bind(ServerSock,(struct sockaddr far *)&dstclient_addr,sizeof(dstclient_addr));
	if (Status != 0)
		return FALSE;
	
	//LISTEN
	Status = listen(ServerSock,1);
	if (Status != 0)
		return FALSE;

	char name[255]; 
	PHOSTENT hostinfo; 
	if( gethostname (name, sizeof(name)) == 0) 
	{ 
		if((hostinfo = gethostbyname(name)) != NULL) 
		{ 
			CString local_IP_address; 
			local_IP_address = inet_ntoa (*(struct in_addr *)*hostinfo->h_addr_list); 
			u_short port = ((dstclient_addr.sin_port & 0xFF00)>>8) | ((dstclient_addr.sin_port & 0x00FF)<<8);

			CString temp;
			temp.Format("请连接这个IP地址：\n%s:%d",local_IP_address, port);
			SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);
		} 
	}

    return TRUE;
}

BOOL waiteAccept()
{
	//ACCEPT
	int len = sizeof(dstclient_addr);
	connectSock = accept(ServerSock,(struct sockaddr far *)&dstclient_addr,&len);
	if (connectSock < 0)
	{
		closesocket(connectSock);
		return FALSE;
	}

	//GetSCREEN
	int SysWidth  = GetSystemMetrics(SM_CXSCREEN);
	int SysHeight = GetSystemMetrics(SM_CYSCREEN);

	return TRUE;
}

DWORD WINAPI threadFunc(LPVOID threadNum)
{
	int length;	
	CString temp;

	if(!InItServerSock())
		return 0;
	if(!waiteAccept())
		return 0;
	
	//等待连接的时候，连接可能被取消。
	if(ThreadRun)
	{
		//int   nPort_Client; 
		//sockaddr   *remoteSockAddr = (struct sockaddr far *)&dstclient_addr ; 
        //getpeername(connectSock, &sockaddr, &nPort_Client); 

		CString temp;
		temp.Format("%s:%d\n已经连接上!", inet_ntoa(dstclient_addr.sin_addr), dstclient_addr.sin_port);//remoteSockAddr->sa_data
		SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);
	}
	else
	{
		SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, "请启动服务！");
		return 0;
	}

	//send(NewSock,(char*)&FALG,sizeof(FALG)+1,MSG_OOB);		
	while(ThreadRun)
	{
		//length = recv(connectSock,(char*)recv_message,sizeof(recv_message)+1,0);
		length = recv(connectSock,(char*)recv_message_server,sizeof(recv_message_server),0);
		if( length>0)
		{
			//MessageBox(0, recv_message, "接收到的信息", MB_OK);		
			temp.Format("接收到的信息：\n%s",recv_message_server);
			SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);

			analyseCommand(recv_message_server);
			memset(recv_message_server, 0, sizeof(recv_message_server));
		}
	}

	return 0;
}

DWORD clientThreadID;
SOCKET    clientSock;
char      server_address[50] = {0};
char      recv_message_client[256] = {0};
struct    sockaddr_in server_ip;
BOOL clientThreadRun = false;
//client
BOOL InItClientSock()
{
	//Define Variable
	WORD wVersionrequested;
	WSADATA wsaData;

	wVersionrequested = MAKEWORD(2,0);
	
	//Start Sock
	int err = WSAStartup(wVersionrequested,&wsaData);
	if (err == -1)
	{
		MessageBox(0,"WSAStartup err", "error",MB_OK);
		return FALSE;
	}
	return TRUE;
}
BOOL ConnectSock()
{
	SOCKET clientSockConnect;
	u_short port;

	//Ini Sock
	clientSock = socket(AF_INET,SOCK_STREAM,0);
	if (ServerSock < 0)
	{
			MessageBox(0,"scoker err",
				 "err",MB_OK);
			return FALSE;
	}

	char *strPort = strstr(server_address, ":");

	if( (strPort!=NULL) && (strlen(strPort)>2) )
	{
		port = atoi(strPort+1);
		if(port==0)
		{
			MessageBox(0, "IP端口不对, 参考例子：192.168.12.134:8981", "提示", MB_OK);
			return FALSE;
		}
		memset(strPort, 0, sizeof(strPort));
	}
	else
	{
		MessageBox(0, "IP地址不对, 参考例子：192.168.12.134:8981", "提示", MB_OK);
		return FALSE;
	}

	//Connect
	server_ip.sin_family = AF_INET;
	server_ip.sin_port = ((port&0xff00)>>8)|((port&0x00ff)<<8);
	server_ip.sin_addr.S_un.S_addr = inet_addr(server_address);

	clientSockConnect = connect(clientSock,(struct sockaddr*)&server_ip,sizeof(server_ip));
	if (clientSockConnect!=0)
	{
		CString temp;
		temp.Format("系统拒绝连接，可能是输入IP地址或端口不对：\n%s:%d", server_address, server_ip.sin_port);
		SetDlgItemText( FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);
		MessageBox(0, "系统拒绝连接，可能是输入IP地址或端口不对 ", "提示", MB_OK);
		return FALSE;
	}
	return TRUE;
}

DWORD WINAPI clientThreadFunc(LPVOID threadNum)
{
	int length;	
	CString temp;

	if(!InItClientSock())
		return 0;
	if(!ConnectSock())
		return 0;
	
	//等待连接的时候，连接可能被取消。
	if(clientThreadRun)
		SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, "已经连接上！");
	else
	{
		SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, "请启动服务！");
		return 0;
	}

	//send(NewSock,(char*)&FALG,sizeof(FALG)+1,MSG_OOB);		
	while(clientThreadRun)
	{
		if( (length = recv(clientSock,(char*)recv_message_client,sizeof(recv_message_client),0))>0)
		{
			//MessageBox(0, recv_message, "接收到的信息", MB_OK);		
			temp.Format("接收到的信息：\n%s",recv_message_client);
			SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);

			analyseCommand(recv_message_client);
			memset(recv_message_client, 0, sizeof(recv_message_client));
		}
		temp.Format("接收到的信息：\n%s",recv_message_client);
	}

	return 0;
}

void mouseMove(char *message)
{
	char *strPoint = strstr(message, ";");

	if( strPoint!=NULL )
	{
		int pointY = atoi(strPoint+1);		
		memset(strPoint, 0, sizeof(strPoint));
		int pointX = atoi(message);

		POINT lpPoint ;
		GetCursorPos(&lpPoint);
		SetCursorPos(lpPoint.x + pointX, lpPoint.y + pointY);
	}
	else
	{
		MessageBox(0, "接受到的数据有误", "提示", MB_OK);
	}
}

void analyseCommand(char *message)
{
	char command = message[0];

	switch(command)
	{
	case '0'://取消鼠标动作
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		break;
	case '1'://移动鼠标
		mouseMove(message+2);
		break;
	case '2'://鼠标左键down
		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
		break;
	case '3'://鼠标左键up
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		break;
	case '4':		
		break;
	case '5'://右击
		mouse_event(MOUSEEVENTF_RIGHTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_RIGHTUP,0,0,0,0);
		break;
	case '6'://单击
		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		break;
	case '7'://双击
		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		break;
	case '8'://向上拖动滚动
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, 100, 0);
		break;
	case '9'://向下拖动滚动
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, -100, 0);
		break;
	}
}

void socketMoveMouse(int direction,int step) 
{
	// TODO: Add your control notification handler code here
	POINT lpPoint ;
	GetCursorPos(&lpPoint);
	switch(direction)
	{
		case 0:
			SetCursorPos(lpPoint.x , lpPoint.y - step);
			break;
		case 1:
			SetCursorPos(lpPoint.x + step, lpPoint.y);
			break;
		case 2:
			SetCursorPos(lpPoint.x , lpPoint.y + step);
			break;
		case 3:
			SetCursorPos(lpPoint.x - step , lpPoint.y);
			break;
		default :
			break;
	}
}

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	//{{AFX_DATA(CAboutDlg)
	enum { IDD = IDD_ABOUTBOX };
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CAboutDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	//{{AFX_MSG(CAboutDlg)
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
	//{{AFX_DATA_INIT(CAboutDlg)
	//}}AFX_DATA_INIT
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
		// No message handlers
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CKeyexeDlg dialog

CKeyexeDlg::CKeyexeDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CKeyexeDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CKeyexeDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	hooked = false;// Initialize all members here.
}

void CKeyexeDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CKeyexeDlg)
	DDX_Control(pDX, IDOK, m_hook);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CKeyexeDlg, CDialog)
	//{{AFX_MSG_MAP(CKeyexeDlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDOK, OnHook)
	ON_BN_CLICKED(IDC_BUTTON_START, OnButtonStart)
	ON_BN_CLICKED(IDC_BUTTON_NOTIFY, OnButtonNotify)
	ON_BN_CLICKED(IDC_BUTTON_CONNECT, OnButtonConnect)
	ON_BN_CLICKED(IDC_BUTTON_SEND, OnButtonSend)
	ON_MESSAGE(WM_KEYSTROKE, processkey)
	ON_MESSAGE(WM_NC,OnNotifyIcon)
	ON_BN_CLICKED(IDC_BUTTON_SERVER, OnButtonServer)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CKeyexeDlg message handlers

BOOL CKeyexeDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	SetDlgItemText(IDC_EDIT_IP,"192.168.1.102:1122");//预设ip地址
	SetDlgItemText(IDC_EDIT_SEND,"client,sya no...");//预设client发送内容
	SetDlgItemText(IDC_EDIT_SERVER,"server,douy,why?");//预设Server发送内容
	
	
	// TODO: Add extra initialization here
	
	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CKeyexeDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CKeyexeDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CKeyexeDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}

void CKeyexeDlg::OnHook()//Install or Remove keyboard Hook.
{
	// TODO: Add your control notification handler code here
	if(hooked==true)
	{
		removehook();
		hooked = false;
		m_hook.SetWindowText("开始监控键盘");	
	}
	else
	{
		installhook(this->GetSafeHwnd());
		hooked = true;
		m_hook.SetWindowText("停止监控键盘");
	}
	
}

void CKeyexeDlg::OnCancel() //On exit, do cleanup(Close files, remove hook.). 
{
	// TODO: Add extra cleanup here
	if(hooked==true)
	{
		removehook();
	}

	closesocket(connectSock);
	closesocket(ServerSock);

	closesocket(clientSock);

	CDialog::OnCancel();
}


LRESULT CKeyexeDlg::processkey(WPARAM wParam, LPARAM lParam)//This block processes the keystroke info.
{
	if(!((DWORD)lParam&0x40000000))
	{
		return 0L;
	}

	CString temp;
	char buffer[20];
	GetKeyNameText(lParam,buffer,20);
	temp.Format("监控到键盘事件：\n%s",buffer);
	//SetDlgItemText(FindWindow(NULL, WINDOWNHANDLERNAME), IDC_STATIC_IP, temp);
	SetDlgItemText(IDC_STATIC_KEY,temp);
	/*return 0L;*/
	

	switch(wParam)
	{
	case VK_UP:
		MoveMouse(0,1);
		break;
	case VK_RIGHT:
		MoveMouse(1,1);
		break;
	case VK_DOWN:
		MoveMouse(2,1);
		break;
	case VK_LEFT:
		MoveMouse(3,1);
		break;

	case VK_NUMPAD1://1 单击
	case 49:
		MouseClick();
		break;
	case VK_NUMPAD2://2 双击
	case 50:
		MouseDoubleClick();
		break;
	case VK_NUMPAD3://3 右击
	case 51:
		MouseRightClick();
		break;
	case VK_NUMPAD4:
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, 100, 0);
		break;
	case VK_NUMPAD5:
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, -100, 0);
		break;
	default:
		break;
	}

	return 0L;
}

//移动鼠标,direction表示方向(上0，右1，下2，左3)，step表示移动步长
void CKeyexeDlg::MoveMouse(int direction,int step) 
{
	// TODO: Add your control notification handler code here
	POINT lpPoint ;
	GetCursorPos(&lpPoint);
	switch(direction)
	{
		case 0:
			SetCursorPos(lpPoint.x , lpPoint.y - 1);
			break;
		case 1:
			SetCursorPos(lpPoint.x + 1, lpPoint.y);
			break;
		case 2:
			SetCursorPos(lpPoint.x , lpPoint.y + 1);
			break;
		case 3:
			SetCursorPos(lpPoint.x - 1 , lpPoint.y);
			break;
		default :
			break;
	}
}
void CKeyexeDlg::MouseClick()
{
	mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
	mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
}
void CKeyexeDlg::MouseDoubleClick()
{
	mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
	mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
	mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
	mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
}
void CKeyexeDlg::MouseRightClick()
{
	mouse_event(MOUSEEVENTF_RIGHTDOWN,0,0,0,0);
	mouse_event(MOUSEEVENTF_RIGHTUP,0,0,0,0);
}

/*
void CKeyexeDlg::saveKeyCodeToText()
{
	//char buffer[20];
	//GetKeyNameText(lParam,buffer,20);

	ofstream ofs("key.txt",ios::app);
	  char ch=0;

      if( ((DWORD)lParam&0x40000000) && (HC_ACTION==nCode) ) //有键按下
      {
            if( (wParam==VK_SPACE)||(wParam==VK_RETURN)||(wParam>=0x2f ) &&(wParam<=0x100) )
            {
                  //fl=fopen("key.txt","a＋");    //输出到key.txt文件
                  if (wParam==VK_RETURN)
                  {
                        ch='\n';
                  }
                  else
                  {
                        BYTE ks[256];
                        GetKeyboardState(ks);
                        WORD w;
                        UINT scan=0;
                        ToAscii(wParam,scan,ks,&w,0);
                        //ch=MapVirtualKey(wParam,2); //把虚键代码变为字符
                        ch =char(w); 
                  }
					char c[2];
					c[0]=ch;
					c[1]='\0';
					ofs<<c;
					MessageBox(NULL,c,"Dll",MB_OK);
            }
      }
	ofs.flush();
	ofs.close();
}*/

void CKeyexeDlg::OnButtonStart() 
{
	// TODO: Add your control notification handler code here
	if(ThreadRun == false)
	{
		ThreadRun = true;
		hThread=CreateThread(NULL,
			0,
			threadFunc,
			NULL,//传递的参数
			0,
			&ThreadID);
		
		SetDlgItemText(IDC_BUTTON_START,"停止服务");
	}
	else
	{	
		ThreadRun = false;
		closesocket(connectSock);
		closesocket(ServerSock);	
		
		SetDlgItemText(IDC_BUTTON_START,"开启服务");
	}

	//WaitForSingleObject(hThread,INFINITE);
}


void CKeyexeDlg::OnButtonNotify() 
{
	// TODO: Add your control notification handler code here
	
	NotifyIcon.cbSize=sizeof(NOTIFYICONDATA);
    NotifyIcon.hIcon=AfxGetApp()->LoadIcon(IDI_ICON_BIRD);//IDR_MAINFRAME
    NotifyIcon.hWnd=m_hWnd;
    lstrcpy(NotifyIcon.szTip,"NotifyIcon Test");
    NotifyIcon.uCallbackMessage = WM_NC;
    NotifyIcon.uFlags=NIF_ICON | NIF_MESSAGE | NIF_TIP;
    Shell_NotifyIcon(NIM_ADD,&NotifyIcon);
	ShowWindow(SW_HIDE);
}
void CKeyexeDlg::OnNotifyIcon(WPARAM wParam,LPARAM IParam)
{
         if ((IParam == WM_LBUTTONDOWN) || (IParam == WM_RBUTTONDOWN))
         { 
              ModifyStyleEx(0,WS_EX_TOPMOST);
              ShowWindow(SW_SHOW);
              Shell_NotifyIcon(NIM_DELETE, &NotifyIcon);
         }
}

void CKeyexeDlg::OnButtonConnect() 
{
	// TODO: Add your control notification handler code here
	if(clientThreadRun == false)
	{
		clientThreadRun = true;
		GetDlgItemText(IDC_EDIT_IP,server_address,sizeof(server_address));

		hThread=CreateThread(NULL,
			0,
			clientThreadFunc,
			NULL,//传递的参数
			0,
			&clientThreadID);
		
		SetDlgItemText(IDC_BUTTON_CONNECT,"断开连接");
	}
	else
	{		
		clientThreadRun = false;
		closesocket(connectSock);
		closesocket(ServerSock);	
		
		SetDlgItemText(IDC_BUTTON_CONNECT,"连接");
	}

	//WaitForSingleObject(hThread,INFINITE);
}

void CKeyexeDlg::OnButtonSend() 
{
	// TODO: Add your control notification handler code here
	GetDlgItemText(IDC_EDIT_SEND,send_message, sizeof(send_message));
	
	send(clientSock,(char*)send_message,strlen(send_message), MSG_OOB);
}

void CKeyexeDlg::OnButtonServer() 
{
	// TODO: Add your control notification handler code here
	
	// TODO: Add your control notification handler code here
	GetDlgItemText(IDC_EDIT_SERVER,send_message,sizeof(send_message));
	
	send(connectSock,(char*)send_message,strlen(send_message), MSG_OOB);
}
