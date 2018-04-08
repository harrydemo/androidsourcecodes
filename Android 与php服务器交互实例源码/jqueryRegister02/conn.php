<?php
function mysql_open()
{
define(ALL_PS,"PHP100");

$mysql_servername="localhost";
$mysql_username="root";
$mysql_password="123";
$mysql_dbname="member";

$conn=mysql_connect($mysql_servername ,$mysql_username ,$mysql_password);
      mysql_query("set names UTF8");
      mysql_select_db($mysql_dbname , $conn);
	  return $conn;
}
?>