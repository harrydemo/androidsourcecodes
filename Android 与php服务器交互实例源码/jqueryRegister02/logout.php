<?php
	 session_start(); 
	 session_unset();
	 session_destroy();
     
     echo "<script>alert('您已退出登陆！现在以游客身份转入前面页面');parent.location.href='index.php';</script>";
     
?>
