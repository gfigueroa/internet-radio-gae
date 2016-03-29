<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.Region"%>
<%@ page import="datastore.RegionManager"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
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

  Key stationKey = sessionUser.getKey().getParent();
  Station station = StationManager.getStation(stationKey);
  
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<%
	Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css"
	rel="stylesheet" />

<%@  include file="../header/page-title.html"%>

</head>

<body>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<form method="post" id="form1" name="form1"
		action="<%= blobstoreService.createUploadUrl("/manageStation?action=add") %>" 
		class="form-style"
		enctype="multipart/form-data">

		<input type="text" name="type" value="image" style="display: none;" /> 

		<fieldset>
			<legend><%=printer.print("Station Image Information")%></legend>

			<%
				if (message != null && message.equals("success") && action != null
						&& action.equals("add")) {
			%>
			<div class="success-div"><%=printer
						.print("Station Image successfully added to the Station")%>.
			</div>
			<%
				}
			%>

			<div>
				<h2><%=printer.print("Add a Station Image File")%></h2>
			</div>

			<% if (error != null && error.equals("MissingInfo")) { %>
				<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
			<% } %>
			<% if (error != null && error.equals("ObjectExists")) { %>
				<div class="error-div"><%= printer.print("There is already a station image with this name") %></div>
			<% } %>

			<div>
				<label for="i_name"><span><%=printer.print("Station Image Name")%>
						<span class="required_field">*</span></span></label> <input type="text"
					name="i_name" class="input_extra_large" value="" title="" /><br />
				<div id="i_name"></div>
			</div>
			
			<div>
		       	<label for="file"><span><%= printer.print("Station Image File") %><span class="required_field">*</span></span></label>
				<input type="file" name="file" class="input_extra_large" value="" /><br />
				<div id="file"></div>
			</div>

		</fieldset>

		<br class="clearfloat" />

		<div>
			<input type="checkbox" name="keep_adding" checked="checked"
				value="true" />
			<%=printer.print("Continue adding station image files")%>
			<div id="keep_adding"></div>
		</div>

		<input type="button" value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listStationImage.jsp'"
			class="button-close" /> <input type="submit"
			value="<%=printer.print("Update")%>" class="button_style" />

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
