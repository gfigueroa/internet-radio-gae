<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Region" %>
<%@ page import="datastore.RegionManager" %>
<%@ page import="datastore.StationType" %>
<%@ page import="datastore.StationTypeManager" %>
<%@ page import="datastore.Station" %>
<%@ page import="datastore.StationManager" %>
<%@ page import="util.DateManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
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
  		if (sessionUser.getUserType() != User.Type.STATION) {
  			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  		}
  	}
    
  	String message = request.getParameter("msg");
  	String action = request.getParameter("action");
  	String updateType = request.getParameter("update_type");
  	String error = request.getParameter("etype");
  	boolean readOnly = request.getParameter("readonly") != null ? true : false;
  
  	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  
  	Station station = StationManager.getStation(sessionUser);
	List<StationType> stationTypes = StationTypeManager.getAllStationTypes();
	List<Region> regions = RegionManager.getAllRegions();
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

<br/>

<form method="post" id="form1" name="form1" 
		action="<%=blobstoreService.createUploadUrl("/manageUser?action=update&u_type=S&update_type=I&k=" + request.getParameter("k"))%>"
		class="form-style" enctype="multipart/form-data">

    <fieldset>
	<legend><%=printer.print("Station Profile")%></legend>
	
	<%
			if (message != null && message.equals("success") && action != null && action.equals("update")) {
		%>
		<div class="success-div"><%=printer.print("Channel updated successfully")%>.</div>
	<%
		}
	%>
	
	<div>
		<h2><%=readOnly ? printer.print("View Station") : printer.print("Edit Station")%></h2>
	</div>

	<%
		if (error != null && error.equals("MissingInfo")) {
	%>
		<div class="error-div"><%=printer.print("You are missing some essential information needed by the system")%></div>
	<%
		}
	%>
	<%
		if (error != null && error.equals("Format")) {
	%>
		<div class="error-div"><%=printer.print("The email or phone you provided does not conform to the standard formats (you can try something like 0975384927 and user@domain.com)")%></div>
	<%
		}
	%>
	
	<input type="text" name="k" value="<%=KeyFactory.keyToString(station.getKey())%>" style="display:none;" id="url_location" />
	
	<div>
		<label for="s_type"><span><%=printer.print("Station Type")%></span></label>
        <select name="s_type" disabled="disabled">
        <%
        	for (StationType type : stationTypes) {
        %>
        	<option value="<%=type.getKey()%>" <%=station.getStationType().longValue() == type.getKey().longValue() ? "selected=\"true\"" : ""%>>
        		<%=type.getStationTypeName()%>
        	</option>
        <%
        	}
        %>
        </select>
		<div id="s_type"></div>
	</div>

    <div>
    	<label  for="s_privilege"><span><%=printer.print("Station Privilege Level")%></span></label>
		<input type="text" name="s_privilege" class="input_extra_large" value="<%=station.getStationPrivilegeLevel() == 0 ? "BASIC" : "PRO"%>" title="" readonly="readonly" /><br />
		<div id="s_privilege"></div>
	</div>

    <div>
    	<label  for="s_name"><span><%=printer.print("Station Name")%> <span class="required_field">*</span></span></label>
		<input type="text" name="s_name" class="input_extra_large" value="<%=station.getStationName()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
		<div id="s_name"></div>
	</div>
	
	<div>
       	<label  for="s_number"><span><%= printer.print("Station Number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="s_number" class="input_extra_large" value="<%=station.getStationNumber()%>"  title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
		<div id="s_number"></div>
	</div>
	
	<div>
       	<label for="s_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="s_description" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= station.getStationDescription() %></textarea><br />
		<div id="s_description"></div>
	</div>
	
	<div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= station.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" readonly="readonly"
			value="<%= station.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>	
	
	<%
	if (!readOnly) {
	%>
		<div>
	        <label for="u_password"><span></span></label>
	        <a href="../station/editStationPassword.jsp?r_key=<%= request.getParameter("k") %>"><%= printer.print("Change password") %></a>
		</div>
	<%
	}
	%>
	
	<div>
		<label for="s_region"><span><%= printer.print("Region") %> <span class="required_field">*</span></span></label>
		<select name="s_region" <%=readOnly ? "disabled=\"true\"" : ""%>>
		<%
		for (Region region : regions) {
		%>
        	<option class="s_region" value="<%= region.getKey() %>" <%= station.getRegion() == region.getKey() ? "selected=\"true\"" : "" %>><%= region.getRegionName() %></option>
        <%
		}
		%>
        </select>
		<div id="s_region"></div>
	</div>
	
	<div>
       	<label for="s_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="s_address1" class="input_extra_large" value="<%=station.getStationAddress().getAddress()%>" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
		<div id="s_address1"></div>
	</div>
    <div>
       	<label for="s_address2"><span></span></label>
		<input type="text" name="s_address2" class="input_extra_large" value="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
		<div id="s_address2"></div>
	</div>
	
    <div>
       	<label for="s_website"><span><%= printer.print("Website") %> </span></label>
		<input type="text" name="s_website" class="input_extra_large" value="<%= station.getStationWebsite().getValue() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="s_website"></div>
	</div>
	
	<%
    if (!(station.getStationLogo() == null && readOnly)) {
    %>
	    <div>
	       	<label for="s_logo"><span><%= printer.print("Logo") %> </span></label>
	       	<%
			if (station.getStationLogo() != null) {
			%>
				<a target="_new" href="/img?blobkey=<%= station.getStationLogo().getKeyString() %>">
				<img src="/img?blobkey=<%= station.getStationLogo().getKeyString() %>&s=300"/>
				</a>
				<br />
			<%
			}
			%>
			<%
			if (!readOnly) {
			%>	
				<label><span> </span></label><input type="file" name="s_logo" class="input_extra_large" value="" /><br />
			<%
			}
			%>
			<div id="s_logo"></div>
		</div>
	<%
	}
	%>
    
    <div>
       	<label for="s_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea name="s_comments" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= station.getStationComments() %></textarea><br />
		<div id="s_comments"></div>
	</div>
	
	<hr/>

	<div>
	<% 
	if (message != null && message.equals("success")) {
		if (action != null && action.equals("update")) {
			if (updateType != null && updateType.equals("P")) {
	%>
				<div class="success-div"><%= printer.print("Station password changed successfully") %>.</div>
	<% 
			}
			else if (updateType != null && updateType.equals("I")) {
	%>
				<div class="success-div"><%= printer.print("Station successfully updated") %></div>
	<%
			}
		}
	} 
	%>
	</div>
	
	</fieldset>

	<br class="clearfloat" />

	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onclick="location.href='/admin/editStation.jsp?k=<%= request.getParameter("k") %>'" class="button_style"/>
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
