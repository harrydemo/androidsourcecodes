========================================================================
       SAMPLE APPLICATION : Keyexe
========================================================================
	Disclaimer
	----------
	THIS SOFTWARE AND THE ACCOMPANYING FILES ARE DISTRIBUTED "AS IS" AND WITHOUT
	ANY WARRANTIES WHETHER EXPRESSED OR IMPLIED. NO REPONSIBILITIES FOR POSSIBLE
	DAMAGES OR EVEN FUNCTIONALITY CAN BE TAKEN. THE USER MUST ASSUME THE ENTIRE
	RISK OF USING THIS SOFTWARE.

	Terms of use
	------------
	THIS SOFTWARE IS FREE FOR PERSONAL USE OR FREEWARE APPLICATIONS.
	IF YOU USE THIS SOFTWARE IN COMMERCIAL OR SHAREWARE APPLICATIONS YOU
	ARE GENTLY ASKED TO SEND ONE LICENCED COPY OF YOUR APPLICATION(S)
	TO THE AUTHOR. IF YOU WANT TO PAY SOME MONEY INSTEAD, CONTACT ME BY
	EMAIL. YOU ARE REQUESTED TO CONTACT ME BEFORE USING THIS SOFTWARE
	IN YOUR SHAREWARE/COMMERCIAL APPLICATION.

	Contact info:
	Site: http:bizkerala.hypermart.net
	Email: anoopt@gmx.net
-------------------------------------------------------------------------------------------------------------------------------
	Usage :
-------------------------------------------------------------------------------------------------------------------------------

The DLL contains 3 exported functions:

1) void installhook(HWND h);
2) void removehook();
3) LRESULT CALLBACK hookproc(int ncode,WPARAM wparam,LPARAM lparam);

Keydll3.dll:
-----------
You should use installhook() to install the Keyboard hook. Pass a pointer of the main Application window as the
parameter. Always use removehook() to remove the hook before your app exits. Change the Classname and
window title in the call FindWindow(). The classname will be the classname of your application main window
and the title set to the title of your application main window. Then compile and link the Files to obtain the
.LIB and .DLL files.

Application:
-------------
When building your application, include the file 'Keydll3.h' in your project and also, do not forget to '#include'
wherever necessary. Specify 'Keydll3.lib' in the additional modules section in the Project settings. And, do not 
forget to add the path to keydll3.lib to the library paths in visual studio. Otherwise, you will get a linker error.

Put the .EXE and .DLL in the same folder and run the exe.

See the sample project 'Keyexe' source for more details.

The log of keystrokes is stored in a file 'Keylog.txt' in your temp folder.

