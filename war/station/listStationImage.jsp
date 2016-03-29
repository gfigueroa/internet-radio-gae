<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="datastore.StationImage"%>
<%@ page import="datastore.StationImageManager"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="util.Printer"%>
<%@ page import="util.Dictionary"%>

<%
	User sessionUser = (User) session.getAttribute("user");
	if (sessionUser == null)
		response.sendRedirect("../login.jsp");
	else {
		if (sessionUser.getUserType() != User.Type.STATION) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	Key stationKey = sessionUser.getUserType() == User.Type.STATION ? sessionUser
			.getKey().getParent() : KeyFactory.stringToKey(request
			.getParameter("s_key"));

	Station station = StationManager.getStation(stationKey);
	List<StationImage> stationImageList = StationImageManager.getAllStationImagesFromStation(station.getKey(), true);
	
  	String error = request.getParameter("etype");
  	String message = request.getParameter("msg");
  	String action = request.getParameter("action");
%>

<%
	Printer printer = (Printer) session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css"
	rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css"
	rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<%--<script src="../js/popup.js"></script>--%>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html"%>
</head>

<body>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<h1><%=station.getStationName() + " - "
					+ printer.print("Station Image")%></h1>
					
	<% if (error != null && error.equals("ReferentialIntegrity")) { %>
		<div class="error-div"><%= printer.print("This station image is being used in one of your program segments and cannot be deleted") %>.</div>
	<% } %>

	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="list-table">

		<tr>
			<td width="20%">
				<a href="addStationImage.jsp?s_key=<%=KeyFactory.keyToString(stationKey)%>">+
					<%=printer.print("Add New Station Image")%>
				</a>
			</td>
			<td width="60%"></td>
			<td width="20%"></td>
		</tr>

		<tr>
			<td width="20%"></td>
			<td width="60%"></td>
			<td width="20%"></td>
		</tr>
		
		<tr>
			<td width="20%"><%= printer.print("Station Image Name") %></td>
			<td width="60%"><%= sessionUser.getUserType() == User.Type.ADMINISTRATOR ? printer.print("Station Image Key") : "" %></td>
			<td width="20%"><%= printer.print("Actions") %></td>
		</tr>

		<%
			for (StationImage stationImage : stationImageList) {
		%>
		<tr>
			<td width="20%"><a
				id="<%=KeyFactory.keyToString(stationImage.getKey())%>"
				href="editStationImage.jsp?k=<%=KeyFactory.keyToString(stationImage.getKey())
						+ "&readonly=true"%>"><%=stationImage.getStationImageName()%></a>
			</td>
			<td width="60%"></td>
			<td width="20%">
				<a href="editStationImage.jsp?k=<%= KeyFactory.keyToString(stationImage.getKey()) %>"><%= printer.print("Edit") %></a>
				<br/>
				<a href="javascript:void(0);" onclick="confirmDelete('/manageStation?action=delete&type=image&k=<%=KeyFactory.keyToString(stationImage.getKey())%>', '<%=printer.print("Are you sure you want to delete this Station Image")%>');"><%=printer.print("Delete")%></a>
			</td>
		</tr>
		<%
			}
		%>
	</table>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
