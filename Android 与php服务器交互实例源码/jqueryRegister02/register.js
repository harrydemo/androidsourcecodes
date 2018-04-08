$(document).ready(function(){
$("input.register-input").click(function(){
	var id = $(this).attr('id');
	$('#note-'+id).removeClass();
	$('#note-'+id).text(info[id]);
	$('#note-' + $(this).attr('id')).css('visibility', 'visible');
});
$('input.register-input').blur(function() {
            $('#note-' + $(this).attr('id')).css('visibility', 'hidden');
	});

$('#password').blur(function() {
	/*
 	     $('#note-password').addClass('blue');
		 $('#note-password').text("密码输入正确!");
		 $('#note-password').css('visibility', 'visible');
		 */
     if ( $('#password').val().length < 3 )
    {
    	 $('#note-password').addClass('red');
		 $('#note-password').text("密码不能小于3位!");
		 $('#note-password').css('visibility', 'visible');

    }
    else
    {
		 $('#note-password').addClass('blue');
		 $('#note-password').text("密码输入正确!");
		 $('#note-password').css('visibility', 'visible');
    }
    
	});
	

$('#password').keyup(function() {
            checkIntensity(this.value)
	});

$('#verify').blur(function() {
	    
        if ($(this).val() != $('#password').val()||$(this).val().length<3)
        {
        $('#note-verify').css('visibility', 'visible');
        $('#note-verify').addClass('red');	
		$('#note-verify').text("两次输入密码不一致!");
        }
        else
        {	$('#note-verify').removeClass('red');
        	$('#note-verify').addClass('blue');
        	$('#note-verify').text("密码输入正确!");
		$('#note-' + $(this).attr('id')).css('visibility', 'visible');
        }
    });
    
   
$('#username').blur(function(){
	if($('#username').val().length > 3){
		$.post('username_check.php',{username:$('#username').val()},function(data){
			if(data == 'error')
			{
				$('#note-username').addClass('red');
				$('#note-username').text("您所填写的用户名已被使用!");
				$('#note-username').css('visibility', 'visible');
			}
			else
			{	
				$('#note-username').removeClass('red');
				$('#note-username').addClass('blue');
				$('#note-username').text("恭喜! 用户名可用!");
				$('#note-username').css('visibility', 'visible');
			}
			});}else{
				$('#note-username').addClass('red');
				$('#note-username').text("用户名不可小于3位!");
				$('#note-username').css('visibility', 'visible');
					}
	});
	
$('#email').blur(function(){
	if (chekemail(email.value)==false)

	  {
	  	 $('#note-email').addClass('red');
		 $('#note-email').text("邮箱格式不正确!");
		 $('#note-email').css('visibility', 'visible');
	 } 
	 
	else
	   {
	   	 $('#note-email').addClass('blue');
		 $('#note-email').text("邮箱输入正确");
		 $('#note-email').css('visibility', 'visible');
   }
		
		
	});
	
//--------------注册协议复选框状态检测---------------------//
$('#checkagree').click(function(){
	
if (document.formUser.agreement.checked==false)
  {
  	 alert("此项不选无法提交注册哦！");
     document.forms['formUser'].elements['Submit1'].disabled = 'disabled';//不允许提交
}
  else
  {
	 document.forms['formUser'].elements['Submit1'].disabled = 'true';//允许提交按
	}
	
});

});

//-------------提交处理注册程序-------------------//
function register() {
if(document.formUser.username.value=="")
	{
      alert("用户名不能为空");
	  document.formUser.username.focus();
	  return false;
	 }
 else if(document.formUser.email.value=="")
	{
	  alert("电子邮箱不能为空");
	  document.formUser.email.focus();
	  return false;
	 }	

 else if(document.formUser.password.value=="")
	{
      alert("用户密码不能为空");
      document.formUser.password.focus();
	  return false;
	 }
 else if(document.formUser.confirm_password.value=="")
	{
	  alert("确认不能为空");
      document.formUser.password.focus();
	  return false;
	 }
	}
	
//* *--------------------检测密码格式-----------------------------* *//
function chekemail(temail) {  
 var pattern = /^[\w]{1}[\w\.\-_]*@[\w]{1}[\w\-_\.]*\.[\w]{2,4}$/i;  
 if(pattern.test(temail)) {  
  return true;  
 }  
 else {  
  return false;  
 }  
} 
//* *--------------------检测密码强度-----------------------------* *//

function checkIntensity(pwd)
{
  var Mcolor = "#FFF",Lcolor = "#FFF",Hcolor = "#FFF";
  var m=0;

  var Modes = 0;
  for (i=0; i<pwd.length; i++)
  {
    var charType = 0;
    var t = pwd.charCodeAt(i);
    if (t>=48 && t <=57)
    {
      charType = 1;
    }
    else if (t>=65 && t <=90)
    {
      charType = 2;
    }
    else if (t>=97 && t <=122)
      charType = 4;
    else
      charType = 4;
    Modes |= charType;
  }

  for (i=0;i<4;i++)
  {
    if (Modes & 1) m++;
      Modes>>>=1;
  }

  if (pwd.length<=4)
  {
    m = 1;
  }

  switch(m)
  {
    case 1 :
      Lcolor = "2px solid red";
      Mcolor = Hcolor = "2px solid #DADADA";
    break;
    case 2 :
      Mcolor = "2px solid #f90";
      Lcolor = Hcolor = "2px solid #DADADA";
    break;
    case 3 :
      Hcolor = "2px solid #3c0";
      Lcolor = Mcolor = "2px solid #DADADA";
    break;
    case 4 :
      Hcolor = "2px solid #3c0";
      Lcolor = Mcolor = "2px solid #DADADA";
    break;
    default :
      Hcolor = Mcolor = Lcolor = "";
    break;
  }
  document.getElementById("pwd_lower").style.borderBottom  = Lcolor;
  document.getElementById("pwd_middle").style.borderBottom = Mcolor;
  document.getElementById("pwd_high").style.borderBottom   = Hcolor;

}
