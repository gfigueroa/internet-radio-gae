<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.Region"%>
<%@ page import="datastore.RegionManager"%>
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
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.STATION) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String error = request.getParameter("etype");
  
  List<Region> regionList = RegionManager.getAllRegions();
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
		action="/manageStation?action=add" class="form-style"
		onsubmit="return checkNumericValues();" >

		<input type="text" name="type" value="channel" style="display: none;" /> 
		<input type="text" name="s_key" value="<%=request.getParameter("s_key")%>" style="display: none;" />

		<fieldset>
			<legend><%=printer.print("Channel Information")%></legend>

			<%
				if (message != null && message.equals("success") && action != null
						&& action.equals("add")) {
			%>
			<div class="success-div"><%=printer
						.print("Channel successfully added to the Station")%>.
			</div>
			<%
				}
			%>

			<div>
				<h2><%=printer.print("Add a channel")%></h2>
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
						<span class="required_field">*</span></span></label> <input type="text"
					name="c_name" class="input_extra_large" value="" title="" /><br />
				<div id="c_name"></div>
			</div>

			<div>
				<label for="c_number"><span><%=printer.print("Channel Number")%>
						<span class="required_field">*</span></span></label> <input type="text"
					name="c_number" class="input_extra_large" value="" title="" /><br />
				<div id="c_number"></div>
			</div>

		</fieldset>

		<br class="clearfloat" />

		<div>
			<input type="checkbox" name="keep_adding" checked="checked"
				value="true" />
			<%=printer.print("Continue adding channels")%>
			<div id="keep_adding"></div>
		</div>

		<input type="button" value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listChannel.jsp?s_key=<%=request.getParameter("s_key")%>'"
			class="button-close" /> <input type="submit"
			value="<%=printer.print("Update")%>" class="button_style" />

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
