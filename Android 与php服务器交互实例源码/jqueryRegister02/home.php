<?php session_start();
echo $_SESSION['msg']; 
?>
<html>
<head>
<script language="javascript">
	function ChkFields(){
	if(document.send.Username.value==""){
	   window.alert("����������")
	   return false
	}
	if(document.send.Pwd.value==""){
	   window.alert("����������")
	   return false
	}
	return true
	}
	</script>
	<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="reg">
<h1>�û���¼</h1>
<form method="POST" action="login.php" name="send" onsubmit="return ChkFields()">

<p align="center">�û���:&nbsp;<input type="text" name="Username"><a href=register.html>ע���û�</a></p>
<p align="center">��&nbsp&nbsp��:&nbsp;<input type="password" name="Pwd"><a href=findpwd.php>��������?</a></p>
<p align="center"><input type="submit" value="��½&nbsp" name="a1" class = anniu><input type="reset" value="��д&nbsp" name="b2" class = anniu></p>

</form>
</div>
</body>
</html>