<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>

<%@ page import="util.Printer"%>
<%@ page import="util.Dictionary"%>

<%
	Printer printer = (Printer) session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

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

	Key stationKey = sessionUser.getUserType() == User.Type.STATION
			? sessionUser.getKey().getParent()
			: KeyFactory.stringToKey(request.getParameter("s_key"));

	Station station = StationManager.getStation(stationKey);

	String error = request.getParameter("etype");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css"
	rel="stylesheet" />

<script language="JavaScript" type="text/javascript"
	src="../js/crypto.js">
</script>

<script language="JavaScript" type="text/javascript">
function hashAndSubmit()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  passwordInput.value = Sha256.hash(passwordInput.value);
  
  return true;
}

function checkPasswords()
{
  var passwordInput = document.getElementsByName("u_password")[0];
  var passwordConfirmInput = document.getElementsByName("u_password_confirm")[0];
  
  if (passwordInput.value == passwordConfirmInput.value) {
  	return hashAndSubmit();
  }
  else {
  	alert("<%=printer
					.print("The password you entered doesn't match the confirmation password")%>.");
    return false;
  }
}
</script>

<%@  include file="../header/page-title.html"%>
</head>

<body>
	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<form method="post" id="form1" name="form1"
		action="/manageUser?action=update&u_type=S&update_type=P&k=<%=request.getParameter("s_key")%>"
		onsubmit="return checkPasswords();" class="form-style">

		<fieldset>
			<legend><%=printer.print("Edit Station Password")%></legend>

			<%
				if (error != null && error.equals("MissingInfo")) {
			%>
			<div class="error-div"><%=printer
						.print("You are missing some essential information needed by the system")%></div>
			<%
				}
			%>

			<div>
				<label for="u_email"><span><%=printer.print("E-mail")%>
				</span></label> <input type="text" name="u_email" class="input_extra_large"
					style="display: none;"
					value="<%=station.getUser().getUserEmail().getEmail()%>" /> <input
					type="text" name="u_email_visible" class="input_extra_large"
					disabled="disabled"
					value="<%=station.getUser().getUserEmail().getEmail()%>" /><br />
				<div id="u_email"></div>
			</div>

			<div>
				<label for="u_password"><span><%=printer.print("New Password")%>
						<span class="required_field">*</span></span></label> <input type="password"
					name="u_password" class="input_extra_large" value="" /><br />
				<div id="u_password"></div>
			</div>

			<div>
				<label for="u_password_confirm"><span><%=printer.print("Confirm Password")%>
						<span class="required_field">*</span></span></label> <input type="password"
					name="u_password_confirm" class="input_extra_large" value="" /><br />
				<div id="u_password_confirm"></div>
			</div>

		</fieldset>

		<br class="clearfloat" /> <input type="button"
			value="<%=printer.print("Close")%>"
			onclick="location.href='<%=sessionUser.getUserType() == User.Type.ADMINISTRATOR ? "/admin/editStation.jsp?k="
					+ request.getParameter("s_key")
					: "/station/editStation.jsp?k="
							+ KeyFactory.keyToString(stationKey)%>'"
			class="button-close" /> <input type="submit"
			value="<%=printer.print("Update")%>" class="button_style" />

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>