<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Administrator" %>
<%@ page import="datastore.AdministratorManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

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
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String updateType = request.getParameter("update_type");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  
  Administrator admin = AdministratorManager.getAdministrator(KeyFactory.stringToKey(request.getParameter("k")));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>
</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1" 
      action="/manageUser?action=update&u_type=A&update_type=I&k=<%= request.getParameter("k") %>" class="form-style">

    <fieldset>
	<legend><%= printer.print("User Information") %></legend>
	
	<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
		<div class="success-div"><%= printer.print("Administrator successfully updated") %>.</div>
	<% } %>
	
	<div>
	  <h2><%= readOnly ? printer.print("View Administrator") : printer.print("Edit Administrator") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	    
	<div>
		<label for="u_type"><span><%= printer.print("User Type") %></span></label>
		<input name="u_type" value="A" selected="selected" style="display:none;" />
		<input name="default-type" value="Administrator" class="input_extra_large" readonly="readonly" />
		<div id="u_type"></div>
	</div>
		
    <div>
        <label for="a_name"><span><%= printer.print("Administrator Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="a_name" class="input_extra_large" value="<%= admin.getAdministratorName() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="a_name"></div>
	</div>

    <div>
        <label for="u_email"><span><%= printer.print("E-mail") %></span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;" 
				value="<%= admin.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" readonly="readonly"
				value="<%= admin.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>
	
	<%
	if (!readOnly) {
	%>
		<div>
	        <label for="u_password"><span></span></label>
	        <a href="editAdminPassword.jsp?k=<%= request.getParameter("k") %>"><%= printer.print("Change password") %></a>
		</div>
	<%
	}
	%>
    
    <div>
       	<label for="a_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="a_comments" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= admin.getAdministratorComments() %></textarea><br />
		<div id="a_comments"></div>
	</div>
	
	<div>
	<% if (message != null && message.equals("success")) { 
		if (action != null && action.equals("update")) {
			if (updateType != null && updateType.equals("P")) {
	%>
				<div class="success-div"><%= printer.print("Administrator password changed successfully") %>.</div>
	<% 
			}
		}
	} 
	%>
	</div>
	
	</fieldset>
  	
  	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listAdmin.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editAdmin.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
