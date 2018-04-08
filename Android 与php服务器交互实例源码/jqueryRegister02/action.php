<?php

  include("conn.php");
  
  $conn = mysql_open();
  
  $sql="select * from schedule";

  $result=mysql_query($sql);

    
	$arr = array();

	while($row = mysql_fetch_array($result,MYSQL_ASSOC)){   //查询出来sql
	$arr[] = $row;                                   //将查询出来的结果赋给数组$arr
	}

	$str = json_encode($arr);                           //将数组转化为json格式的字符串  
	      
	echo json_encode($str);
      
          
?>