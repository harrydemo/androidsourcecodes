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
		 $('#note-password').text("����������ȷ!");
		 $('#note-password').css('visibility', 'visible');
		 */
     if ( $('#password').val().length < 3 )
    {
    	 $('#note-password').addClass('red');
		 $('#note-password').text("���벻��С��3λ!");
		 $('#note-password').css('visibility', 'visible');

    }
    else
    {
		 $('#note-password').addClass('blue');
		 $('#note-password').text("����������ȷ!");
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
		$('#note-verify').text("�����������벻һ��!");
        }
        else
        {	$('#note-verify').removeClass('red');
        	$('#note-verify').addClass('blue');
        	$('#note-verify').text("����������ȷ!");
		$('#note-' + $(this).attr('id')).css('visibility', 'visible');
        }
    });
    
   
$('#username').blur(function(){
	if($('#username').val().length > 3){
		$.post('username_check.php',{username:$('#username').val()},function(data){
			if(data == 'error')
			{
				$('#note-username').addClass('red');
				$('#note-username').text("������д���û����ѱ�ʹ��!");
				$('#note-username').css('visibility', 'visible');
			}
			else
			{	
				$('#note-username').removeClass('red');
				$('#note-username').addClass('blue');
				$('#note-username').text("��ϲ! �û�������!");
				$('#note-username').css('visibility', 'visible');
			}
			});}else{
				$('#note-username').addClass('red');
				$('#note-username').text("�û�������С��3λ!");
				$('#note-username').css('visibility', 'visible');
					}
	});
	
$('#email').blur(function(){
	if (chekemail(email.value)==false)

	  {
	  	 $('#note-email').addClass('red');
		 $('#note-email').text("�����ʽ����ȷ!");
		 $('#note-email').css('visibility', 'visible');
	 } 
	 
	else
	   {
	   	 $('#note-email').addClass('blue');
		 $('#note-email').text("����������ȷ");
		 $('#note-email').css('visibility', 'visible');
   }
		
		
	});
	
//--------------ע��Э�鸴ѡ��״̬���---------------------//
$('#checkagree').click(function(){
	
if (document.formUser.agreement.checked==false)
  {
  	 alert("���ѡ�޷��ύע��Ŷ��");
     document.forms['formUser'].elements['Submit1'].disabled = 'disabled';//�������ύ
}
  else
  {
	 document.forms['formUser'].elements['Submit1'].disabled = 'true';//�����ύ��
	}
	
});

});

//-------------�ύ����ע�����-------------------//
function register() {
if(document.formUser.username.value=="")
	{
      alert("�û�������Ϊ��");
	  document.formUser.username.focus();
	  return false;
	 }
 else if(document.formUser.email.value=="")
	{
	  alert("�������䲻��Ϊ��");
	  document.formUser.email.focus();
	  return false;
	 }	

 else if(document.formUser.password.value=="")
	{
      alert("�û����벻��Ϊ��");
      document.formUser.password.focus();
	  return false;
	 }
 else if(document.formUser.confirm_password.value=="")
	{
	  alert("ȷ�ϲ���Ϊ��");
      document.formUser.password.focus();
	  return false;
	 }
	}
	
//* *--------------------��������ʽ-----------------------------* *//
function chekemail(temail) {  
 var pattern = /^[\w]{1}[\w\.\-_]*@[\w]{1}[\w\-_\.]*\.[\w]{2,4}$/i;  
 if(pattern.test(temail)) {  
  return true;  
 }  
 else {  
  return false;  
 }  
} 
//* *--------------------�������ǿ��-----------------------------* *//

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
