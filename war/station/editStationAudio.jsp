<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.StationAudio"%>
<%@ page import="datastore.StationAudioManager"%>
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

  	String stationAudioKeyString = request.getParameter("k");
  	Key stationAudioKey = KeyFactory.stringToKey(stationAudioKeyString);
  	StationAudio stationAudio = StationAudioManager.getStationAudio(stationAudioKey);
  	Station station = StationManager.getStation(stationAudioKey.getParent());
  	
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
		action="<%= blobstoreService.createUploadUrl("/manageStation?action=update") %>" 
		class="form-style"
		enctype="multipart/form-data"
		onsubmit="return checkNumericValues();">

		<input type="text" name="type" value="audio" style="display: none;" /> 
		<input type="text" name="k" value="<%=request.getParameter("k")%>" style="display: none;" />

		<fieldset>
			<legend><%=printer.print("Station Audio Information")%></legend>

			<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
				<div class="success-div"><%=printer.print("Station audio updated successfully")%>.</div>
			<% } %>

			<div>
				<h2><%=readOnly ? printer.print("View a Station Audio File") : printer
					.print("Edit a Station Audio File")%></h2>
			</div>

			<% if (error != null && error.equals("MissingInfo")) { %>
				<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
			<% } %>
			<% if (error != null && error.equals("ObjectExists")) { %>
				<div class="error-div"><%= printer.print("There is already a station audio with this name") %></div>
			<% } %>

			<div>
				<label for="a_type"><span><%= printer.print("Audio File Type") %><span class="required_field">*</span></span></label>
				<select name="a_type" <%=readOnly ? "disabled=\"disabled\"" : ""%>>
		        	<option class="a_type" value="music" <%= stationAudio.getStationAudioType() == StationAudio.StationAudioType.MUSIC ? "selected=\"true\"" : "" %>><%= printer.print("MUSIC") %></option>
		        	<option class="a_type" value="voice" <%= stationAudio.getStationAudioType() == StationAudio.StationAudioType.VOICE ? "selected=\"true\"" : "" %>><%= printer.print("VOICE") %></option>
		        </select>
				<div id="a_type"></div>
			</div>

			<div>
				<label for="a_name"><span><%=printer.print("Station Audio Name")%>
					<span class="required_field">*</span></span></label> 
					<input type="text" name="a_name" class="input_extra_large" value="<%=stationAudio.getStationAudioName()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="a_name"></div>
			</div>
			
			<%
		    if (!(stationAudio.getStationAudioMultimediaContent() == null && readOnly)) {
		    %>
		    <div>
		       	<label for="file"><span><%= printer.print("Station Audio File") %><span class="required_field">*</span></span></label>
		       	<%
				if (stationAudio.getStationAudioMultimediaContent() != null) {
					BlobInfoFactory bif = new BlobInfoFactory();
					BlobKey blobKey = new BlobKey(stationAudio.getStationAudioMultimediaContent().getKeyString());
		        	String fileName = bif.loadBlobInfo(blobKey).getFilename();
				%>
					<a href="/fileDownload?file_id=<%= stationAudio.getStationAudioMultimediaContent().getKeyString() %>" target="_new">
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
				<label for="a_duration"><span><%=printer.print("Station Audio Duration")%>
					<span class="required_field">*</span></span></label> 
					<input type="text" name="a_duration" class="input_extra_large" value="<%=stationAudio.getStationAudioDuration()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="a_duration"></div>
			</div>
			
			<div>
				<label for="a_key"><span><%=printer.print("Station Audio Key")%>
					</span></label> 
					<input type="text" name="a_key" class="input_extra_large" value="<%= KeyFactory.keyToString(stationAudio.getKey()) %>" title="" readonly="readonly" /><br />
				<div id="a_key"></div>
			</div>
			
			<div>
				<label for="a_format"><span><%=printer.print("Station Audio Format")%>
					</span></label> 
					<input type="text" name="a_format" class="input_extra_large" value="<%= stationAudio.getStationAudioFormat() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="a_format"></div>
			</div>

		</fieldset>

		<br class="clearfloat" /> <input type="button"
			value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listStationAudio.jsp'"
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
			onclick="location.href='/station/editStationAudio.jsp?k=<%=request.getParameter("k")%>'"
			class="button_style"/>
		<%
			}
		%>

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
