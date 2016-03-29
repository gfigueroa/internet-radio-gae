<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Region" %>
<%@ page import="datastore.RegionManager" %>

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
  
  Region region = RegionManager.getRegion(Long.parseLong(request.getParameter("k")));
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
      action="/manageObject?action=update&type=region&k=<%= request.getParameter("k") %>" class="form-style">

    <fieldset>
	<legend><%= printer.print("Region Information") %></legend>
	
	<% 
	if (message != null && message.equals("success") && action != null && action.equals("update")) { 
	%>
		<div class="success-div"><%= printer.print("Region successfully updated") %>.</div>
	<% 
	} 
	%>
			
	<div>
	  <h2><%= readOnly ? printer.print("View Region") : printer.print("Edit Region") %></h2>
	</div>

	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	    	
    <div>
        <label for="r_name"><span><%= printer.print("Region Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="r_name" class="input_extra_large" value="<%= region.getRegionName() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="r_name"></div>
	</div>
    
    <div>
       	<label for="r_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="r_comments" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= region.getRegionComments() %></textarea><br />
		<div id="r_comments"></div>
	</div>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listRegion.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editRegion.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
