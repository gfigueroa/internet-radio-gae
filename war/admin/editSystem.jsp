<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.StationType" %>
<%@ page import="datastore.System" %>
<%@ page import="datastore.SystemManager" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="util.DateManager" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<%
	User sessionUser = (User)session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("/login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  
  System system = SystemManager.getSystem();
%>

<%
	Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<%@  include file="../header/page-title.html" %>

<script src="../js/checkNumericValues.js" language="javascript" type="text/javascript"></script>

</head>

<body>

<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<br/>

<form method="post" id="form1" name="form1" 
	  onsubmit="return isInteger(oavs1.value, 'oldest app version supported (digit 1)') && isInteger(oavs2.value, 'oldest app version supported (digit 2)') && isInteger(oavs3.value, 'oldest app version supported (digit 3)');"
      action="/manageSystem?action=update&type=S"
      class="form-style">

    <fieldset>
    
    <legend><%=printer.print("System Information")%></legend>
	
	<div>
	  <h2><%=readOnly ? printer.print("View System") : printer.print("Edit System")%></h2>
	</div>
	
	<%
			if (error != null && error.equals("MissingInfo")) {
		%>
		<div class="error-div"><%=printer.print("You are missing some essential information needed by the system")%></div>
	<%
		}
	%>
    			
    <div>
    	<label for="s_time"><span><%=printer.print("System last modified on")%></span></label>
		<input type="text" name="s_time" class="input_large" value="<%=DateManager.printDateAsString(system.getSystemTime())%>" readonly="readonly" /><br />
		<div id="s_time"></div>
	</div>
	
	<hr/>
	
	<h3><%=printer.print("System Versions")%></h3>
		
	<div>
        <label for="sl_version"><span><%=printer.print("Station list version")%></span></label>
		<input type="text" name="sl_version" class="input_large" value="<%=system.getStationListVersion()%>" readonly="readonly" /><br />
		<div id="sl_version"></div>
	</div>
	
	<div>
        <label for="stl_version"><span><%=printer.print("Station type list version")%></span></label>
		<input type="text" name="stl_version" class="input_large" value="<%=system.getStationTypeListVersion()%>" readonly="readonly" /><br />
		<div id="stl_version"></div>
	</div>

	<div>
        <label for="ml_version"><span><%=printer.print("Music library version")%></span></label>
		<input type="text" name="ml_version" class="input_large" value="<%=system.getMusicLibraryVersion()%>" readonly="readonly" /><br />
		<div id="ml_version"></div>
	</div>
	
	<br/>
	
	<div>
        <label for="oavs"><span><%= printer.print("Oldest App version supported") %></span></label>
		<input type="text" name="oavs1" class="input_extra_small" maxlength="3" value="<%= system.getOldestAppVersionSupported1() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> />.
		<input type="text" name="oavs2" class="input_extra_small" maxlength="3" value="<%= system.getOldestAppVersionSupported2() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> />.
		<input type="text" name="oavs3" class="input_extra_small" maxlength="3" value="<%= system.getOldestAppVersionSupported3() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="oavs"></div>
	</div>
	
	<hr/>
	
	<div>
		<% if (message != null && message.equals("success")) { 
			if (action != null && action.equals("update")) {
		%>
				<div class="success-div"><%= printer.print("System updated successfully") %>.</div>
		<% 
			}
		} 
		%>
	</div>
		
	</fieldset>

	<br class="clearfloat" />
	
	<%
	if (!readOnly) {
	%>
		<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='editSystem.jsp?readonly=true'" class="button-close"/>
	<%
	}
	%>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	%>

	<%
	if (readOnly) {
	%>
		<br class="clearfloat" />
		<br class="clearfloat" />
		<br class="clearfloat" />
		<input type="button" value="<%= printer.print("Edit") %>" onclick="location.href='editSystem.jsp'" class="button_style"/>	
	<%
	}
	%>
	
</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
