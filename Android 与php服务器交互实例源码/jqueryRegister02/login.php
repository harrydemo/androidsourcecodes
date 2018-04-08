<?php
session_start();
include("conn.php");

$username=str_replace(" ","",$_POST[Username]);

$conn = mysql_open();
if(empty($conn))
{
   die("数据库连接失败");
}
else
{   
   $str="select * from user_list where username='$username'";
   $result=mysql_query($str);
   $us=is_array($row=mysql_fetch_array($result));
   $ps= $us ? md5($_POST[Pwd].ALL_PS)== $row[password] : FALSE;
    if($ps){
    	$_SESSION["Username"]=$row[username];
    	$_SESSION["Passed"]=True;
    	$_SESSION[user_shell]=md5($row[username].$row[password].ALL_PS);
    	$msg="欢迎您回来,尊敬的".$username;
    	$_SESSION['msg']=$msg;
    	echo "<script>window.location.href='index.php'</script>";
    }else{
    	 $msg="用户名或密码错误";
         $_SESSION['msg']=$msg;
         //echo "密码或者用户名错误";
  		 //session_destroy();
  		 echo "<script>window.location.href='home.php'</script>";
    }
} 
?>