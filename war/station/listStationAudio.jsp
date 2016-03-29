<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="datastore.StationAudio"%>
<%@ page import="datastore.StationAudioManager"%>
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
	List<StationAudio> stationAudioList = StationAudioManager.getAllStationAudiosFromStation(station.getKey(), false);
	
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
					+ printer.print("Station Audio")%></h1>

	<% if (error != null && error.equals("ReferentialIntegrity")) { %>
		<div class="error-div"><%= printer.print("This station audio is being used in one of your program segments and cannot be deleted") %>.</div>
	<% } %>

	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="list-table">

		<tr>
			<td width="30%">
				<a href="addStationAudio.jsp?s_key=<%=KeyFactory.keyToString(stationKey)%>">+
					<%=printer.print("Add New Station Audio")%>
				</a>
			</td>
			<td width="50%"></td>
			<td width="20%"></td>
		</tr>

		<tr>
			<td width="30%"></td>
			<td width="50%"></td>
			<td width="20%"></td>
		</tr>
		
		<tr>
			<td width="30%"><%= printer.print("Station Audio Name") %></td>
			<td width="50%"><%= printer.print("Station Audio Type") %></td>
			<td width="20%"><%= printer.print("Actions") %></td>
		</tr>

		<%
			for (StationAudio stationAudio : stationAudioList) {
		%>
		<tr>
			<td width="30%"><a
				id="<%=KeyFactory.keyToString(stationAudio.getKey())%>"
				href="editStationAudio.jsp?k=<%=KeyFactory.keyToString(stationAudio.getKey())
						+ "&readonly=true"%>"><%=stationAudio.getStationAudioName()%></a>
			</td>
			<td width="50%"><%= printer.print(stationAudio.getStationAudioTypeString()) %></td>
			<td width="20%">
				<a href="editStationAudio.jsp?k=<%= KeyFactory.keyToString(stationAudio.getKey()) %>"><%= printer.print("Edit") %></a>
				<br/>
				<a href="javascript:void(0);" onclick="confirmDelete('/manageStation?action=delete&type=audio&k=<%=KeyFactory.keyToString(stationAudio.getKey())%>', '<%=printer.print("Are you sure you want to delete this Station Audio")%>');"><%=printer.print("Delete")%></a>
			</td>
		</tr>
		<%
			}
		%>
	</table>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
