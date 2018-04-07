// Keyexe.h : main header file for the KEYEXE application
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
#if !defined(AFX_KEYEXE_H__BBACFDD1_07F3_46EE_A2CA_1D3E6D61A28A__INCLUDED_)
#define AFX_KEYEXE_H__BBACFDD1_07F3_46EE_A2CA_1D3E6D61A28A__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CKeyexeApp:
// See Keyexe.cpp for the implementation of this class
//

class CKeyexeApp : public CWinApp
{
public:
	CKeyexeApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CKeyexeApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CKeyexeApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_KEYEXE_H__BBACFDD1_07F3_46EE_A2CA_1D3E6D61A28A__INCLUDED_)
