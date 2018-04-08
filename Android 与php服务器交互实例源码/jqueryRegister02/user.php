<?php
/*
 * Created on 2011-8-25
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */ 
    session_start();
    include("conn.php");

    $username= str_replace(" ","",$_POST["username"]);
    $emailaddr= str_replace(" ","",$_POST["email"]);
    $conn=mysql_open();
    $code=md5($_POST[password].ALL_PS);
    $sql="insert into user_list(uid,m_id,username,password,email) values(1,2,'$username','$code','$emailaddr')";
    $query=mysql_query($sql);
    mysql_close($conn);

if($query){
	
//echo "×¢²á³É¹¦";
$_SESSION["Username"]=$username;
$_SESSION["Passed"]=True;
echo "<script>window.location.href='index.php'</script>";

//header("Location:index.php");
}


?>
