<?php

  include("conn.php");
  
  $conn = mysql_open();
  
  $sql="select * from schedule";

  $result=mysql_query($sql);

    
	$arr = array();

	while($row = mysql_fetch_array($result,MYSQL_ASSOC)){   //��ѯ����sql
	$arr[] = $row;                                   //����ѯ�����Ľ����������$arr
	}

	$str = json_encode($arr);                           //������ת��Ϊjson��ʽ���ַ���  
	      
	echo json_encode($str);
      
          
?>