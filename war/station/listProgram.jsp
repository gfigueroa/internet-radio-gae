<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.Channel"%>
<%@ page import="datastore.ChannelManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="datastore.Program"%>
<%@ page import="datastore.ProgramManager"%>
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
	List<Program> programs = ProgramManager.getAllProgramsFromStation(station.getKey(), true);
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
					+ printer.print("Programs")%></h1>

	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="list-table">

		<tr>
			<td width="40%"><a href="addProgram.jsp">+<%=printer.print("Add New Program (JSON Upload)")%></a></td>
			<td width="10%"></td>
			<td width="40%"></td>
			<td width="10%"></td>
		</tr>

		<tr>
			<td width="40%"></td>
			<td width="10%"></td>
			<td width="40%"></td>
			<td width="10%"></td>
		</tr>
		
		<tr>
			<td width="40%"><%= printer.print("Program Name") %></td>
			<td width="10%"><%= printer.print("No.") %></td>
			<td width="40%"><%= printer.print("Channel") %></td>
			<td width="10%"><%= printer.print("Actions") %></td>
		</tr>

		<%
			for (Program program : programs) {
				Channel channel = ChannelManager.getChannel(program.getKey().getParent());
		%>
		<tr>
			<td width="40%">
				<a
					id="<%=KeyFactory.keyToString(program.getKey())%>"
					href="editProgram.jsp?k=<%= KeyFactory.keyToString(program.getKey()) 
					+ "&readonly=true"%>">
					<%= program.getProgramName() %>
				</a>
			</td>
			<td width="10%">
				<%= program.getProgramSequenceNumber() %>
			</td>
			<td width="40%">
				<%= channel.getChannelName() %>
			</td>
			<td width="10%">
				<a href="editProgram.jsp?k=<%=KeyFactory.keyToString(program.getKey())%>"><%=printer.print("Edit")%></a>
				<br/>
				<a href="javascript:void(0);" onclick="confirmDelete('/manageStation?action=delete&type=program&k=<%= KeyFactory.keyToString(program.getKey()) %>', '<%= printer.print("Are you sure you want to delete this program") %>');"><%=printer.print("Delete")%></a>
			</td>
		</tr>
		<%
			}
		%>
	</table>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
