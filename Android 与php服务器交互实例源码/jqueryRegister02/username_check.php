<?php
include("conn.php");
	//ษ่ึราณรๆฑเย๋
//header("Content-type:text/html;charset=UTF-8");

$username=trim($_POST["username"]);
$conn=mysql_open();
$sql="select * from user_list where username='$username'";
$query=mysql_query($sql);
$rst=mysql_fetch_object($query);
mysql_close($conn);
if ($rst==false)
{
echo 'ok';
}
else
{
echo 'error';
}

?>