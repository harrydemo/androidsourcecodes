# androidsourcecodes
echo "# androidsourcecodes" >> README.md

```
git init

git add README.md

git commit -m "first commit"

git remote add origin https://github.com/harrydemo/androidsourcecodes.git

git push -u origin master

git pull origin master --allow-unrelated-histories

git add .

git fetch -v --progress "origin"
```
when 
fatal: refusing to merge unrelated histories


#windows 控制台cmd乱码的解决办法

我本机的系统环境：

- OS Name: Microsoft Windows 10 企业版
- OS Version: 10.0.14393 N/A Build 14393


有时在cmd中输出的中文乱码




解决办法如下：

在cmd中输入 `CHCP 65001`




按Enter键

然后查看不再乱码





注：CHCP是一个计算机指令，能够显示或设置活动代码页编号。

代码页 | 描述
----|----
65001 |  UTF-8代码页

950 | 繁体中文

936 | 简体中文默认的GBK

437 | MS-DOS 美国英语




但是通过CHCP设置编码是治标不治本的

想永久的更改cmd编码值需要修改注册表


方法一：

在运行中通过regedit进入注册表

找到`HKEY_CURRENT_USER\Console\%SystemRoot%_system32_cmd.exe`

新建一个 DWORD（32位值）,命名为CodePage，值设为65001 

方法二：

我更喜欢这样：

新建一个cmd.reg

内容输入如下：
```
Windows Registry Editor Version 5.00



[HKEY_CURRENT_USER\Console\%SystemRoot%_system32_cmd.exe]

"CodePage"=dword:0000fde9

"FontFamily"=dword:00000036

"FontWeight"=dword:00000190

"FaceName"="Consolas"

"ScreenBufferSize"=dword:232900d2

"WindowSize"=dword:002b00d2
```
如图：




保存之后，双击cmd.reg即可。

cmd.reg我在csdn也放了一份:http://download.csdn.net/detail/taoshujian/9770251
