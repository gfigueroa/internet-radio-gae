<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.Station" %>
<%@ page import="datastore.StationManager" %>
<%@ page import="datastore.StationType" %>
<%@ page import="datastore.StationTypeManager" %>
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
<jsp:include page="../menu/main-menu.jsp" />

<h1><%=printer.print("Station List")%></h1>

<%
	List<Station> stationList = StationManager.getAllStations();
%>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">

  <tr>
    <td width="40%"><a href="/admin/addStation.jsp">+ <%=printer.print("Add New Station")%></a></td>
    <td width="25%"></td>
    <td width="15%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="40%"></td>
  	<td width="25%"></td>
  	<td width="15%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="40%"><%=printer.print("Station Name")%></td>
  	<td width="25%"><%=printer.print("Station type")%></td>
  	<td width="15%"><%=printer.print("E-mail")%></td>
    <td width="20%"><%=printer.print("Actions")%></td>
  </tr>

<%
	for (Station station : stationList) {
  	StationType type = StationTypeManager.getStationType(station.getStationType());
%>
  <tr>
    <td width="40%"><a href="editStation.jsp?k=<%= KeyFactory.keyToString(station.getKey()) + "&readonly=true" %>"><%= station.getStationName() %></a></td>
    <td width="25%"><%= type != null ? type.getStationTypeName() : printer.print("Not assigned yet") %></td>
    <td width="15%"><%= station.getUser().getUserEmail().getEmail() %></td>
    <td width="20%">
    	<a href="editStation.jsp?k=<%= KeyFactory.keyToString(station.getKey()) %>"><%= printer.print("Edit") %></a>
    	<br/>
		<a href="../station/listChannel.jsp?s_key=<%= KeyFactory.keyToString(station.getKey()) %>"><%= printer.print("Channels") %></a>
		<br/>
		<a href="javascript:void(0);" onclick="confirmDelete('/manageUser?action=delete&u_type=S&k=<%= KeyFactory.keyToString(station.getKey()) %>', '<%= printer.print("Are you sure you want to delete this Station")%>');"><%= printer.print("Delete") %></a>
    </td>
  </tr>
<%  
  }
%>
</table>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>