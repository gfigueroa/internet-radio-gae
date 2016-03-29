<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.StationImage"%>
<%@ page import="datastore.StationImageManager"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="com.google.appengine.api.blobstore.BlobInfoFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
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
  
  	boolean readOnly = request.getParameter("readonly") != null ? true : false;
  	String error = request.getParameter("etype");
  	String message = request.getParameter("msg");
  	String action = request.getParameter("action");

  	String stationImageKeyString = request.getParameter("k");
  	Key stationImageKey = KeyFactory.stringToKey(stationImageKeyString);
  	StationImage stationImage = StationImageManager.getStationImage(stationImageKey);
  	Station station = StationManager.getStation(stationImageKey.getParent());
  	
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
		action="<%= blobstoreService.createUploadUrl("/manageStation?action=update") %>" 
		class="form-style"
		enctype="multipart/form-data">

		<input type="text" name="type" value="image" style="display: none;" /> 
		<input type="text" name="k" value="<%=request.getParameter("k")%>" style="display: none;" />

		<fieldset>
			<legend><%=printer.print("Station Image Information")%></legend>

			<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
				<div class="success-div"><%=printer.print("Station image updated successfully")%>.</div>
			<% } %>

			<div>
				<h2><%=readOnly ? printer.print("View a Station Image File") : printer
					.print("Edit a Station Image File")%></h2>
			</div>

			<% if (error != null && error.equals("MissingInfo")) { %>
				<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
			<% } %>
			<% if (error != null && error.equals("ObjectExists")) { %>
				<div class="error-div"><%= printer.print("There is already a station image with this name") %></div>
			<% } %>

			<div>
				<label for="i_name"><span><%=printer.print("Station Image Name")%>
					<span class="required_field">*</span></span></label> 
					<input type="text" name="i_name" class="input_extra_large" value="<%=stationImage.getStationImageName()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="i_name"></div>
			</div>
			
			<%
		    if (!(stationImage.getStationImageMultimediaContent() == null && readOnly)) {
		    %>
		    <div>
		       	<label for="file"><span><%= printer.print("Station Image File") %><span class="required_field">*</span></span></label>
		       	<%
				if (stationImage.getStationImageMultimediaContent() != null) {
					BlobInfoFactory bif = new BlobInfoFactory();
					BlobKey blobKey = new BlobKey(stationImage.getStationImageMultimediaContent().getKeyString());
		        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
				%>
					<a href="/fileDownload?file_id=<%= stationImage.getStationImageMultimediaContent().getKeyString() %>" target="_new">
						<%= fileName %>
					</a>
					<br />
				<%
				}
				%>
				<%
				if (!readOnly) {
				%>		
					<label for="file"><span></span></label><input type="file" name="file" class="input_extra_large" value="" /><br />
				<%
				}
				%>
				<div id="file"></div>
			</div>
			<%
			}
			%>
			
			<div>
				<label for="i_key"><span><%=printer.print("Station Image Key")%>
					</span></label> 
					<input type="text" name="i_key" class="input_extra_large" value="<%= KeyFactory.keyToString(stationImage.getKey()) %>" title="" readonly="readonly" /><br />
				<div id="i_key"></div>
			</div>
			
			<div>
				<label for="i_format"><span><%=printer.print("Station Image Format")%>
					</span></label> 
					<input type="text" name="i_format" class="input_extra_large" value="<%= stationImage.getStationImageFormat() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="i_format"></div>
			</div>

		</fieldset>

		<br class="clearfloat" /> <input type="button"
			value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listStationImage.jsp'"
			class="button-close" />

		<%
			if (!readOnly) {
		%>
		<input type="submit" value="<%=printer.print("Update")%>"
			class="button_style" />
		<%
			} else {
		%>
		<input type="button"
			value="&nbsp;&nbsp;<%=printer.print("Edit")%>&nbsp;&nbsp;"
			onclick="location.href='/station/editStationImage.jsp?k=<%=request.getParameter("k")%>'"
			class="button_style"/>
		<%
			}
		%>

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
