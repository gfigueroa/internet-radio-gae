<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Administrator" %>
<%@ page import="datastore.AdministratorManager" %>

<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String error = request.getParameter("etype");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
      action="/manageUser?action=add" 
      onSubmit="return checkPasswords();"
      class="form-style">
  	
  	<input name="u_type" value="C" style="display:none;" />
  
    <fieldset>
	<legend><%= printer.print("Name & Contact Information") %></legend>
	
	<div>
	  <h2><%= printer.print("Add Customer") %></h2></div>
	</div>
	
	<% if (error != null && error.equals("AlreadyExists")) { %>
		<div class="error-div"><%= printer.print("The email you provided is already being used in the system") %></div>
	<% } %>
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The email or phone you provided does not conform to the standard formats (you can try something like 0975384927 and user@domain.com)") %></div>
	<% } %>
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
    
    <div>
       	<label  for="c_name"><span><%= printer.print("Customer Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_name" class="input_extra_large" value="" title="" /><br />
		<div id="c_name"></div>
	</div>

	<div>
		<label for="c_gender"><span><%= printer.print("Gender") %> <span class="required_field">*</span></span></label>
        <select name="c_gender">
        	<option value="M">-<%= printer.print("Select Gender") %>-</option>
        	<option value="F"><%= printer.print("Female") %></option>
        	<option value="M"><%= printer.print("Male") %></option>
        </select>
        <br />
	<div id="c_gender"></div>
	
	</div>
		<div>
		<label for="c_phone"><span><%= printer.print("Phone Number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_phone" class="input_large" value="" /><br />
		<div id="c_phone"></div>
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
       	<label for="c_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_address1" class="input_extra_large" value="" /><br />
		<div id="c_address1"></div>
	</div>
	
    <div>
       	<label for="c_address2"><span></span></label>
		<input type="text" name="c_address2" class="input_extra_large" value="" /><br />
		<div id="c_address2"></div>
	</div>
	
    <div>
       	<label for="c_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="c_comments" class="input_extra_large" value=""></textarea><br />
		<div id="c_comments"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listCustomer.jsp'" class="button-close"/>

	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
