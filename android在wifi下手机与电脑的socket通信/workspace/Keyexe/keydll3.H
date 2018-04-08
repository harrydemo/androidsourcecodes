#define KEYDLL3_API __declspec(dllexport)


KEYDLL3_API void installhook(HWND h);//This function installs the Keyboard hook.
KEYDLL3_API void removehook();//This function removes the previously installed hook.
KEYDLL3_API LRESULT CALLBACK hookproc(int ncode,WPARAM wparam,LPARAM lparam);//This function is called when the keyboard is operated.