<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="util.Printer"%>
<%@ page import="util.Dictionary"%>

<%
  User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.STATION) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String error = request.getParameter("etype");

  Key stationKey = sessionUser.getUserType() == User.Type.STATION ?
  		sessionUser.getKey().getParent() : KeyFactory.stringToKey(request.getParameter("s_key"));

  Station station = StationManager.getStation(stationKey);
%>

<%
	Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<form method="post" 
			id="form1" 
			name="form1"
			action="/stationUpload?action=add&type=program&redirectTo=listProgram"
			enctype="multipart/form-data"
			class="form-style">
	
		<fieldset>
			
			<legend><%=printer.print("Add program")%></legend>
	
			<div>
				<label for="json_file"><span><%=printer.print("JSON file")%></span></label>
				<input name="json_file" type="file" id="json_file"/><br />
				<div id="json_file"></div>
			</div>
			
		</fieldset>
		
		<br class="clearfloat" />
		
		<input type="button" value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listProgram.jsp'"
			class="button-close" /> 
		<input type="submit"
			value="<%=printer.print("Update")%>" class="button_style" />
	
	</form>

	<%@  include file="../header/page-footer.html" %>

</body>
</html>