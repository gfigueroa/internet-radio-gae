<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="datastore.Channel"%>
<%@ page import="datastore.ChannelManager"%>
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
		if (sessionUser.getUserType() != User.Type.ADMINISTRATOR
				&& sessionUser.getUserType() != User.Type.STATION) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	Key stationKey = sessionUser.getUserType() == User.Type.STATION ? sessionUser
			.getKey().getParent() : KeyFactory.stringToKey(request
			.getParameter("s_key"));

	Station station = StationManager.getStation(stationKey);
	List<Channel> channelList = ChannelManager
			.getStationChannels(station.getKey());
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
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
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

	<%
		if (sessionUser.getUserType() != User.Type.STATION) {
	%>
	<a href="../admin/listStation.jsp" class="back-link-RS"><%=printer.print("Back to Station")%></a>
	<%
		}
	%>

	<h1><%=station.getStationName() + " - "
					+ printer.print("Channels")%></h1>

	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="list-table">

		<tr>
			<td width="20%">
				<a href="addChannel.jsp?s_key=<%=KeyFactory.keyToString(stationKey)%>">+
					<%=printer.print("Add New Channel")%>
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
			<td width="20%"><%= printer.print("Channel Name") %></td>
			<td width="60%"><%= sessionUser.getUserType() == User.Type.ADMINISTRATOR ? printer.print("Channel Key") : "" %></td>
			<td width="20%"><%= printer.print("Actions") %></td>
		</tr>

		<%
			for (Channel channel : channelList) {
		%>
		<tr>
			<td width="20%"><a
				id="<%=KeyFactory.keyToString(channel.getKey())%>"
				href="editChannel.jsp?k=<%=KeyFactory.keyToString(channel.getKey())
						+ "&readonly=true"%>"><%=channel.getChannelName()%></a>
			</td>
			<td width="60%">
				<%= sessionUser.getUserType() == User.Type.ADMINISTRATOR ? KeyFactory.keyToString(channel.getKey()) : "" %>
			</td>
			<td width="20%">
				<a href="editChannel.jsp?k=<%=KeyFactory.keyToString(channel.getKey())%>"><%=printer.print("Edit")%></a>
				<br/>
				<a href="javascript:void(0);" onclick="confirmDelete('/manageStation?action=delete&type=channel&k=<%=KeyFactory.keyToString(channel.getKey())%>', '<%=printer.print("Are you sure you want to delete this Channel")%>');"><%=printer.print("Delete")%></a>
			</td>
		</tr>
		<%
			}
		%>
	</table>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
