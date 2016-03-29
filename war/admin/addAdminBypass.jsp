<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Administrator" %>
<%@ page import="datastore.AdministratorManager" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = new Printer(Dictionary.Language.ENGLISH);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="../js/crypto.js">

</script>
<script language="JavaScript" type="text/javascript">
function hashAndSubmit()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  passwordInput.value = Sha256.hash(passwordInput.value);
  
  return true;
}

function checkPasswords()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  var passwordConfirmInput = document.getElementsByName("u_password_confirm")[0];
  
  if (passwordInput.value == passwordConfirmInput.value) {
  	return hashAndSubmit();
  }
  else {
  	alert("<%=printer.print("The password you entered doesn't match the confirmation password")%>.");
    return false;
  }
}
</script>

<%@  include file="../header/page-title.html" %>
</head>

<body>

<form method="post" id="form1" name="form1" 
      action="/manageUser?action=add" 
      onSubmit="return checkPasswords();"
      class="form-style">

	<input type="text" name="bypass" value="<%= request.getParameter("bypass") %>" style="display:none;" />

    <fieldset>
    <legend><%= printer.print("User Information") %></legend>
	
	<div>
	  <h2><%= printer.print("Add Administrator") %></h2>
	</div>
    
    <div>
		<label for="u_type"><span><%= printer.print("User Type") %> <span class="required_field">*</span></span></label>
		<input name="u_type" value="A" selected="selected" style="display:none;" />
		<input name="default-type" value="Administrator" class="input_extra_large" disabled="true" />
		<div id="u_type"></div>
	</div>
		
    <div>
        <label for="a_name"><span><%= printer.print("Administrator Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="a_name" class="input_extra_large" value="" /><br />
		<div id="a_name"></div>
	</div>

    <div>
        <label for="u_email"><span><%= printer.print("E-mail") %> <span class="required_field">*</span></span></label>
		<input type="text" name="u_email" class="input_extra_large" value="" /><br />
		<div id="u_email"></div>
	</div>

    <div>
       	<label for="u_password"><span><%= printer.print("Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password" class="input_extra_large" value="" /><br />
		<div id="u_password"></div>
	</div>
	
	<div>
       	<label for="u_password_confirm"><span><%= printer.print("Confirm Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password_confirm" class="input_extra_large" value="" /><br />
		<div id="u_password_confirm"></div>
	</div>
    
    <div>
       	<label for="a_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="a_comments" class="input_extra_large" value=""></textarea><br />
		<div id="a_comments"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	<br class="clearfloat" />
	<br class="clearfloat" />
	<br class="clearfloat" />

	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

</body>
</html>
