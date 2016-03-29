<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.Region"%>
<%@ page import="datastore.RegionManager"%>
<%@ page import="datastore.Channel"%>
<%@ page import="datastore.ChannelManager"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="util.GeoCoder"%>
<%@ page import="util.Location"%>
<%@ page import="util.GeocodeResponse"%>
<%@ page import="util.Printer"%>
<%@ page import="util.Dictionary"%>

<%
	User sessionUser = (User)session.getAttribute("user");
  	if (sessionUser == null)
    	response.sendRedirect("../login.jsp");
  	else {
	  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.STATION) {
	  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	  	}
  	}
  
  	boolean readOnly = request.getParameter("readonly") != null ? true : false;
  	String error = request.getParameter("etype");
  	String message = request.getParameter("msg");
  	String action = request.getParameter("action");

  	String channelKeyString = request.getParameter("k");
  	Key channelKey = KeyFactory.stringToKey(channelKeyString);
  	Channel channel = ChannelManager.getChannel(channelKey);
  	Station station = StationManager.getStation(channelKey.getParent());
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
function isInteger(sText) {
   var ValidChars = "0123456789";
   var IsInteger=true;
   var Char;

 
   for (i = 0; i < sText.length && IsInteger == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsInteger = false;
      }
   }
   return IsInteger; 
}

function checkNumericValues() {
  if(!isInteger(document.getElementsByName("c_number")[0].value)){
    alert("<%=printer.print("The channel number you entered is not valid")%>.");
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
		action="/manageStation?action=update" class="form-style"
		onsubmit="return checkNumericValues();" >

		<input type="text" name="type" value="channel" style="display: none;" /> 
		<input type="text" name="k" value="<%=request.getParameter("k")%>" style="display: none;" />

		<fieldset>
			<legend><%=printer.print("Channel Information")%></legend>

			<% if (message != null && message.equals("success") && action != null && action.equals("update")) { %>
				<div class="success-div"><%=printer.print("Channel updated successfully")%>.</div>
			<% } %>

			<div>
				<h2><%=readOnly ? printer.print("View a channel") : printer
					.print("Edit a Channel")%></h2>
			</div>

			<%
				if (error != null && error.equals("MissingInfo")) {
			%>
			<div class="error-div"><%=printer
						.print("You are missing some essential information needed by the system")%></div>
			<%
				}
			%>

			<div>
				<label for="s_name"><span><%=printer.print("Station Name")%></span></label>
				<input type="text" name="s_name" class="input_extra_large"
					value="<%=station.getStationName()%>" title="" readonly="readonly" /><br />
				<div id="s_name"></div>
			</div>

			<div>
				<label for="c_name"><span><%=printer.print("Channel Name")%>
					<span class="required_field">*</span></span></label> 
					<input type="text" name="c_name" class="input_extra_large" value="<%=channel.getChannelName()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="c_name"></div>
			</div>
			
			<div>
				<label for="c_number"><span><%=printer.print("Channel Number")%>
					<span class="required_field">*</span></span></label> 
					<input type="text" name="c_number" class="input_extra_large" value="<%=channel.getChannelNumber()%>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> /><br />
				<div id="c_number"></div>
			</div>

		</fieldset>

		<br class="clearfloat" /> <input type="button"
			value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listChannel.jsp?s_key=<%=KeyFactory.keyToString(station.getKey())%>'"
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
			onclick="location.href='/station/editChannel.jsp?k=<%=request.getParameter("k")%>'"
			class="button_style"/>
		<%
			}
		%>

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
