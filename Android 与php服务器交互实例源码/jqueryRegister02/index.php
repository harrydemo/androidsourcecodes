<?php 
session_start();
if(isset($_SESSION["Passed"]))
{
echo "<script>alert('���ѵ�¼�����ڽ�ת���½ǰҳ��');</script>";
echo $_SESSION["Username"].";��ӭ����"."\n";
echo "<a href=Pwdchange.php?username=" . $_SESSION["username"] . "onclick='return newwin(this.href)'>�޸�����</a>&nbsp<a href=logout.php>�˳���½</a>";
//echo "<script>alert(document.getElementById('menu').style);document.getElementById('menu').style.display='none';</script>"; //û��Ӧ
//echo "<script>$('#menu_item').hide();</script>"; //û��Ӧ
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="gbk">
<head>
<title>jquery ˫���༭</title>
<style type="text/css">
table	{ border:0;border-collapse:collapse;}
td		{ font:normal 12px/17px Arial;padding:2px;width:100px;}
th		{ font:bold 12px/17px Arial;text-align:left;padding:4px;border-bottom:1px solid #333;}
.dan	{ background:#FC0}  
.ed	{ background:#669;color:#fff;}
</style>
<script src="jquery.js" type="text/javascript"></script>
<script type="text/javascript">
  $(function(){
		$("tbody>tr:even").addClass("dan");
     
	    $("tbody>tr>td").dblclick(function(){
		  var inval = $(this).html();
		  var infd = $(this).attr("fd");
		  var inid =  $(this).parents().attr("id");
			$(this).html("<input id='edit"+infd+inid+"' value='"+inval+"'> ");

			$("#edit"+infd+inid).focus().live("blur",function(){
			    var editval = $(this).val();
				$(this).parents("td").html(editval);
				$.post("post.php",{id:inid,fd:infd,val:editval});
			})
		});
		
  });
</script>

<script type="text/javascript">
$(document).ready(function(){
    var infd = $(this).attr("fd");
    $.ajax({

              type: "POST",
              
              dataType:'json',

              url: "action.php",

              data: {name:"123456789"},

              success: function(response,st){
                  
                   for(i=0;i<eval(response).length;i++){
                   $("tbody>tr").eq(i).children("td").eq(0).html(eval(response)[i].title);
                   $("tbody>tr").eq(i).children("td").eq(1).html(eval(response)[i].date);
                   $("tbody>tr").eq(i).children("td").eq(2).html(eval(response)[i].address);
                   }
             }
      });
});
</script>

<script language="javascript">
	function ChkFields(){
		/*
    if(document.form1.Username.value==""){
	   window.alert("����������");
	   return false;
	}
	if(document.form1.Pwd.value==""){
	   window.alert("����������");
	   return false;
	};
	return true;
	*/
	
	window.alert("��������");
	return false;

    };
</script>

</head>
<body>
<div id=menu <?php if(isset($_SESSION["Passed"])){echo "style='display:none'";};?>>
<form method="POST" action="login.php" name="form1" onsubmit="return ChkFields()">
<p align="left">�û���:&nbsp;<input type="text" name="Username">&nbsp��&nbsp��:&nbsp;<input type="password" name="Pwd"><input type="submit" value="��½&nbsp" name="a1" class = anniu><a href=register.html>ע��</a></p>
</form>
</div>
	
	<table>
		<thead>
			<tr><th>����</th><th>ʱ��</th><th>�ص�</th></tr>
		</thead>
		<tbody>
			<tr id="1"> 
				<td fd="title"></td><td fd="date"></td><td fd="address"></td></tr>
			<tr  id="2"> 
				<td fd="title"></td><td fd="date"></td><td fd="address"></td></tr>
			<tr  id="3"> 
				<td fd="title"></td><td fd="date"></td><td fd="address"></td></tr>
			<tr  id="4"> 
				<td fd="title"></td><td fd="date"></td><td fd="address"></td></tr>
		</tbody>
	</table>
    
    
</body>
</html>