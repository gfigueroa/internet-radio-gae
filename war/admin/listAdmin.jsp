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
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/admin-menu.jsp" />

<h1><%= printer.print("Administrator List") %></h1>

<%
	List<Administrator> adminList = AdministratorManager.getAllAdministrators();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="40%"><a href="/admin/addAdmin.jsp">+ <%= printer.print("Add New Administrator") %></a></td>
    <td width="40%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="40%"></td>
    <td width="40%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="40%"><%= printer.print("Administrator Name") %></td>
    <td width="40%"><%= printer.print("E-mail") %></td>
    <td width="20%"><%= printer.print("Actions") %></td>
  </tr>

<% 
  for (Administrator admin : adminList) {
%>
  <tr>
    <td width="40%"><a href="/admin/editAdmin.jsp?k=<%= KeyFactory.keyToString(admin.getKey()) + "&readonly=true" %>"><%= admin.getAdministratorName() %></a></td>
    <td width="40%"><%= admin.getUser().getUserEmail().getEmail() %></td>
    <td width="20%">
    <a href="/admin/editAdmin.jsp?k=<%= KeyFactory.keyToString(admin.getKey()) %>"><%= printer.print("Edit") %></a>
    <br/>
    <a href="javascript:void(0);" onclick="confirmDelete('/manageUser?action=delete&u_type=A&k=<%= KeyFactory.keyToString(admin.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Administrator")%>');"><%= printer.print("Delete") %></a>
    </td>
  </tr>
<%  
  }
%>
</table>


<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
