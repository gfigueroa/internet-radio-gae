<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="datastore.User" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
                     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="stylesheets/login.css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="js/crypto.js">
</script>

<script type="text/javascript" src="../js/fb-root.js"></script>

<script language="JavaScript" type="text/javascript">
function hashAndSubmit()
{
  var passwordInput = document.getElementsByName("password")[0];
  passwordInput.value = Sha256.hash(passwordInput.value);
  
  return true;
}
</script>

<% 
	Printer printer = new Printer(Dictionary.Language.CHINESE);
	String language = request.getParameter("lang") != null ? request.getParameter("lang") : "CH";
	if(language.equals("EN")) {
			printer.setLanguage(Dictionary.Language.ENGLISH);
	}
	if(language.equals("CH")) {
			printer.setLanguage(Dictionary.Language.CHINESE);
	}

	// If user is logged in, don't allow this page to open
	User sessionUser = (User) session.getAttribute("user");
	if (sessionUser != null) {
		// we check the user type to send him to his/her own main page
    	switch(sessionUser.getUserType()) {
        	case ADMINISTRATOR:
                response.sendRedirect("/admin/listAdmin.jsp");
                break;
            case STATION:
                response.sendRedirect("/station/editStation.jsp");
                break;
            default:
                // there should be no other
                // type of user
                assert(false);
        }
	}
%>

<% String noHeader = request.getParameter( "header" );%>

<title>Social Internet Radio</title>
<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width" />
</head>

<body>

<% 
if (noHeader!=null && noHeader.equals("false")) { 
%>
<% 
} 
else {
%>
	<%@  include file="header/homepage-banner.html" %>
	<%@  include file="menu/blank-menu.jsp" %>
<%
}
%>		

<div class="welcome-banner">
  <h1><%= printer.print("Welcome") %>!</h1>
</div>

<%
  String error = request.getParameter("etype");
  String message = request.getParameter("msg") != null ? request.getParameter("msg") : "";
  String action = request.getParameter("action") != null ? request.getParameter("action") : "";
%>

<div class="login-box">
	<div class="login-left">
	  	<form name="login-form" action="/attemptlogin" 
	    	method="post" onsubmit="hashAndSubmit()">
			
			<input type="text" name="url_location" value="" style="display:none;" id="url_location" />
			
			<div class="login-box-title">
		    <span><%= printer.print("Sign in") %></span> <br/> <%= printer.print("to access your account") %>.
		    </div>
		    
		    <%
		    if (error != null && error.equals("InvalidInfo")) {
		    %>
		        <div class="error-div">
		        <%= printer.print("Some or all of the information provided does not match any user in our database") %>. 
		        </div>
		    <%
		    }
		    else if (error != null && error.equals("CustomerLogin")) {
		    %>
		    	<div class="error-div">
		        <%= printer.print("Customers cannot log in to the system") %>. 
		        </div>
		    <%
		    }
		    %>
		    
		    <div class="login-box-user">
		    	<label><%= printer.print("User e-mail") %></label><br/>
		      	<input type="text" name="username" />
		    </div>
		    
		    <label><%= printer.print("Password") %></label><br/>
		    <div class="login-box-password">
		    	<input type="password" name="password" />
		    </div>
		    
		    <div class="login-box-button">
		      	<input type="submit" value="<%= printer.print("Log in") %>" />
		    </div>
		    
		    <div class="fb-login-button" align="right">Login with Facebook</div>
		    <hr>
		    <%
		    Printer text = new Printer(Dictionary.Language.CHINESE);
		    if(language.equals("EN")) { 
		    %>
				<input type="hidden" name="language" value="EN"/>
			<%
			}
			if(language.equals("CH")) {
			%>
				<input type="hidden" name="language" value="CH"/>
			<%
			}
			%>
		    
	      	<a href="login.jsp?lang=CH"><%= text.print("Chinese") %></a> <a href="login.jsp?lang=EN">English</a>
		</form>
	</div>

	<div class="login-right">
		<div class="login-box-title">
		    <span><%= printer.print("Create") %></span> <br/><%= printer.print("a new customer account") %>.
		</div>
		<p><%= printer.print("Create your account to gain access to the system") %>.</p>
		<br/>
		<hr />
		<br/>
		<div class="login-box-button">
			<a href="/customer/autoCustomer.jsp?lang=<%= language %>"><%= printer.print("Create a new account") %></a>
		</div>
		
		<%
		if (message.equals("success") && action.equals("add")) {
		%>
		<br/>
		<div class="success-div">
		<%= printer.print("User registered successfully") %>!
		</div>
		<%
		}
		%>
	</div>
	<br style="clear:both;"/>  
</div>

<hr style="width:98%;text-align:center;"/>
</br>

<% 
if (noHeader!=null && noHeader.equals("false")) { 
%>
<% 
}
else {
%>
	<iframe src="https://www.facebook.com/plugins/like.php?href=www.smasrv.com"
	        scrolling="no" frameborder="0"
	        style="border:none; align:middle; width:450px; height:80px">
	</iframe>
	<%@  include file="./header/page-footer.html" %>
<% 
}
%>		

</body>
</html>