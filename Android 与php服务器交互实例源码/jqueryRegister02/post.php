<?php
	include("conn.php");

if(!empty($_POST['id'])){
	$id=$_POST['id'];
	$fd=$_POST['fd'];
	$val=$_POST['val'];
	$conn=mysql_open();
    $sql = "update schedule set `$fd`='$val' where `id`='$id'";
    echo $sql;
    $query=mysql_query($sql);
    mysql_close($conn);
}

?>