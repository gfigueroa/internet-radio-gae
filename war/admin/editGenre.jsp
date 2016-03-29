<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Genre" %>
<%@ page import="datastore.GenreManager" %>

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
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  
  Genre genre = GenreManager.getGenre(Long.parseLong(request.getParameter("k")));
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
      action="/manageObject?action=update&type=genre&k=<%= request.getParameter("k") %>" class="form-style">

    <fieldset>
	<legend><%= printer.print("Genre Information") %></legend>
	
	<% 
	if (message != null && message.equals("success") && action != null && action.equals("update")) { 
	%>
		<div class="success-div"><%= printer.print("Genre successfully updated") %>.</div>
	<% 
	} 
	%>
			
	<div>
	  <h2><%= readOnly ? printer.print("View Genre") : printer.print("Edit Genre") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	    	
    <div>
        <label for="g_e_name"><span><%= printer.print("Genre English Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="g_e_name" class="input_extra_large" value="<%= genre.getGenreEnglishName() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="g_e_name"></div>
	</div>
	
	<div>
        <label for="g_c_name"><span><%= printer.print("Genre Chinese Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="g_c_name" class="input_extra_large" value="<%= genre.getGenreChineseName() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="g_c_name"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listGenre.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editGenre.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
