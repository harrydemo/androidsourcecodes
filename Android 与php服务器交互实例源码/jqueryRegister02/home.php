<?php session_start();
echo $_SESSION['msg']; 
?>
<html>
<head>
<script language="javascript">
	function ChkFields(){
	if(document.send.Username.value==""){
	   window.alert("请输入姓名")
	   return false
	}
	if(document.send.Pwd.value==""){
	   window.alert("请输入密码")
	   return false
	}
	return true
	}
	</script>
	<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="reg">
<h1>用户登录</h1>
<form method="POST" action="login.php" name="send" onsubmit="return ChkFields()">

<p align="center">用户名:&nbsp;<input type="text" name="Username"><a href=register.html>注册用户</a></p>
<p align="center">密&nbsp&nbsp码:&nbsp;<input type="password" name="Pwd"><a href=findpwd.php>忘记密码?</a></p>
<p align="center"><input type="submit" value="登陆&nbsp" name="a1" class = anniu><input type="reset" value="重写&nbsp" name="b2" class = anniu></p>

</form>
</div>
</body>
</html>