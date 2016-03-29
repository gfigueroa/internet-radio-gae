<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Region" %>
<%@ page import="datastore.RegionManager" %>
<%@ page import="datastore.StationType" %>
<%@ page import="datastore.StationTypeManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

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
  		if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  		}
  	}
  
  	String error = request.getParameter("etype");
  	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  	List<StationType> stationTypes = StationTypeManager.getAllStationTypes();
  	List<Region> regions = RegionManager.getAllRegions();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="../js/crypto.js"></script>

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
  	alert("<%=printer.print("The password you entered doesn't match the confirmation password")%>.");
    return false;
  }
}

</script>

<%@  include file="../header/page-title.html" %>
</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<form method="post" id="form1" name="form1"
		action="<%=blobstoreService.createUploadUrl("/manageUser?action=add")%>"
		onsubmit="return checkPasswords();"
		class="form-style" enctype="multipart/form-data">
		
	<input type="text" name="u_type" value="S" style="display:none;" />

    <fieldset>
	<legend><%=printer.print("Station Information")%></legend>
	
	<div>
	  <h2><%=printer.print("Add Station")%></h2>
	</div>
	
	<%
			if (error != null && error.equals("AlreadyExists")) {
		%>
		<div class="error-div"><%=printer.print("The email you provided is already being used in the system")%></div>
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
	<%
		if (error != null && error.equals("MissingInfo")) {
	%>
		<div class="error-div"><%=printer.print("You are missing some essential information needed by the system")%></div>
	<%
		}
	%>
	
	<div>
		<label for="s_type"><span><%=printer.print("Station type")%> <span class="required_field">*</span></span></label>
		<select name="s_type">
        <option value="">-<%=printer.print("Select Station Type")%>-</option>
        <%
        	for (StationType stationType : stationTypes) {
        %>
        	<option class="s_type" value="<%= stationType.getKey()%>"><%=stationType.getStationTypeName() %></option>
        <%
        	}
        %>
        </select>
		<div id="s_type"></div>
	</div>
	
	<div>
		<label for="s_privilege"><span><%=printer.print("Station privilege level")%> <span class="required_field">*</span></span></label>
		<select name="s_privilege">
        	<option class="s_privilege" value="0"><%= printer.print("Basic") %></option>
        	<option class="s_privilege" value="1"><%= printer.print("Pro") %></option>
        </select>
		<div id="s_privilege"></div>
	</div>
	
	<div>
       	<label  for="s_name"><span><%= printer.print("Station Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="s_name" class="input_extra_large"  value=""  title="" /><br />
		<div id="s_name"></div>
	</div>
	
	<div>
       	<label  for="s_number"><span><%= printer.print("Station Number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="s_number" class="input_extra_large" value=""  title="" /><br />
		<div id="s_number"></div>
	</div>
	
	<div>
       	<label for="s_description"><span><%= printer.print("Description") %> <span class="required_field">*</span></span></label>
		<textarea name="s_description" class="input_extra_large"></textarea><br />
		<div id="s_description"></div>
	</div>
	
	<div>
       	<label for="u_email"><span><%= printer.print("E-mail") %> <span class="required_field">*</span></span></label>
		<input type="text" name="u_email" class="input_extra_large" value="" /><br />
		<div id="u_email"></div>
	</div>
	
    <div>
       	<label for="u_password"><span><%= printer.print("Password") %><span class="required_field">*</span></span></label>
		<input type="password" name="u_password" class="input_extra_large" value="" /><br />
		<div id="u_password"></div>
	</div>
	
	<div>
       	<label for="u_password_confirm"><span><%= printer.print("Confirm Password") %> <span class="required_field">*</span></span></label>
		<input type="password" name="u_password_confirm" class="input_extra_large" value="" /><br />
		<div id="u_password_confirm"></div>
	</div>
	
	<div>
		<label for="s_region"><span><%= printer.print("Region") %> <span class="required_field">*</span></span></label>
		<select name="s_region">
		<%
		for (Region region : regions) {
		%>
        	<option class="s_region" value="<%= region.getKey() %>"><%= region.getRegionName() %></option>
        <%
		}
		%>
        </select>
		<div id="s_region"></div>
	</div>
	
	<div>
       	<label for="s_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="s_address1" class="input_extra_large" value="" /><br />
		<div id="s_address1"></div>
	</div>
    <div>
       	<label for="s_address2"><span></span></label>
		<input type="text" name="s_address2" class="input_extra_large" value="" /><br />
		<div id="s_address2"></div>
	</div>
	
    <div>
       	<label for="s_website"><span><%= printer.print("Website") %> </span></label>
		<input type="text" name="s_website" class="input_extra_large" value="" /><br />
		<div id="s_website"></div>
	</div>
	
    <div>
       	<label for="s_logo"><span><%= printer.print("Logo") %></span></label>
		<input type="file" name="s_logo" class="input_extra_large" value="" /><br />
		<div id="s_logo"></div>
	</div>
    
    <div>
       	<label for="s_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="s_comments" class="input_extra_large"></textarea><br />
		<div id="s_comments"></div>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onclick="location.href='/admin/listStation.jsp'" class="button-close"/>
	
	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
