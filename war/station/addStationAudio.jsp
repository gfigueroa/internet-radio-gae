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

<script language="Javascript" type="text/javascript">
function isDouble(sText) {
   var ValidChars = "0123456789.";
   var IsDouble=true;
   var Char;

   for (i = 0; i < sText.length && IsDouble == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsDouble = false;
      }
   }
   return IsDouble; 
}

function checkNumericValues() {
  if(!isDouble(document.getElementsByName("a_duration")[0].value)){
    alert("<%=printer.print("The duration you entered is not valid")%>.");
    return false;
  }
  return true;
}
</script>

</head>

<body>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<form method="post" id="form1" name="form1"
		action="<%= blobstoreService.createUploadUrl("/manageStation?action=add") %>" 
		class="form-style"
		enctype="multipart/form-data"
		onsubmit="return checkNumericValues();">

		<input type="text" name="type" value="audio" style="display: none;" /> 

		<fieldset>
			<legend><%=printer.print("Station Audio Information")%></legend>

			<%
				if (message != null && message.equals("success") && action != null
						&& action.equals("add")) {
			%>
			<div class="success-div"><%=printer
						.print("Station Audio successfully added to the Station")%>.
			</div>
			<%
				}
			%>

			<div>
				<h2><%=printer.print("Add a Station Audio File")%></h2>
			</div>

			<% if (error != null && error.equals("MissingInfo")) { %>
				<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
			<% } %>
			<% if (error != null && error.equals("ObjectExists")) { %>
				<div class="error-div"><%= printer.print("There is already a station audio with this name") %></div>
			<% } %>

			<div>
				<label for="a_type"><span><%= printer.print("Audio File Type") %><span class="required_field">*</span></span></label>
				<select name="a_type">
		        	<option class="a_type" value="music"><%= printer.print("MUSIC") %></option>
		        	<option class="a_type" value="voice"><%= printer.print("VOICE") %></option>
		        </select>
				<div id="a_type"></div>
			</div>

			<div>
				<label for="a_name"><span><%=printer.print("Station Audio Name")%>
						<span class="required_field">*</span></span></label> <input type="text"
					name="a_name" class="input_extra_large" value="" title="" /><br />
				<div id="a_name"></div>
			</div>
			
			<div>
		       	<label for="file"><span><%= printer.print("Station Audio File") %><span class="required_field">*</span></span></label>
				<input type="file" name="file" class="input_extra_large" value="" /><br />
				<div id="file"></div>
			</div>
			
			<div>
				<label for="a_duration"><span><%=printer.print("Station Audio Duration")%>
						<span class="required_field">*</span></span></label> <input type="text"
					name="a_duration" class="input_extra_large" value="" title="" /><br />
				<div id="a_duration"></div>
			</div>

		</fieldset>

		<br class="clearfloat" />

		<div>
			<input type="checkbox" name="keep_adding" checked="checked"
				value="true" />
			<%=printer.print("Continue adding station audio files")%>
			<div id="keep_adding"></div>
		</div>

		<input type="button" value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listStationAudio.jsp'"
			class="button-close" /> <input type="submit"
			value="<%=printer.print("Update")%>" class="button_style" />

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
