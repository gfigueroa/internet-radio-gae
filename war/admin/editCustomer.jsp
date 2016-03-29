<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.Customer" %>
<%@ page import="datastore.CustomerManager" %>
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
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String message = request.getParameter("msg");
  String action = request.getParameter("action");
  String updateType = request.getParameter("update_type");
  boolean readOnly = request.getParameter("readonly") != null ? true : false;
  String error = request.getParameter("etype");
  
  Customer customer = CustomerManager.getCustomer(KeyFactory.stringToKey(request.getParameter("k")));
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

<form method="post" id="form1" name="form1" 
    action="/manageUser?action=update&u_type=C&update_type=I&k=<%= request.getParameter("k") %>" 
    class="form-style">

    <fieldset>
	<legend><%= printer.print("Name & Contact Information") %></legend>
	
	<% 
	if (message != null && message.equals("success") && action != null && action.equals("update")) { 
	%>
		<div class="success-div"><%= printer.print("Customer successfully updated") %>.</div>
	<% 
	} 
	%>
	
	<div>
		<h2><%= readOnly ? printer.print("View Customer") : printer.print("Edit Customer") %></h2>
	</div>
  	
	<% if (error != null && error.equals("Format")) { %>
		<div class="error-div"><%= printer.print("The email or phone you provided does not conform to the standard formats (you can try something like 0975384927 and user@domain.com)") %></div>
	<% } %>
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	
	<div>
    	<label  for="c_name"><span><%= printer.print("Customer Name") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_name" class="input_extra_large"  value="<%= customer.getCustomerName() %>" title="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="c_name"></div>
	</div>

	<div>
		<label for="c_gender"><span><%= printer.print("Gender") %></span></label>
				
        <select name="c_gender" <%= readOnly ? "disabled=\"true\"" : "" %>>
        	<option value="M">-<%= printer.print("Select Gender") %>-</option>
            <option value="F" 
            <% if (customer.getCustomerGender() == Customer.Gender.FEMALE) { %>
            	selected="true"
            <% } %>
            ><%= printer.print("Female") %></option>
            <option value="M"
            <% if (customer.getCustomerGender() == Customer.Gender.MALE) { %>
            	selected="true"
            <% } %>
            ><%= printer.print("Male") %></option>
        </select>
        <br />
		<div id="c_gender"></div>
	</div>
	
	<div>
		<label for="c_phone"><span><%= printer.print("Phone number") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_phone" class="input_large" value="<%= customer.getCustomerPhone().getNumber() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="c_phone"></div>
	</div>
    
    <div>
        <label for="u_email"><span><%= printer.print("E-mail") %> </span></label>
        <input type="text" name="u_email" class="input_extra_large" style="display:none;"
        	value="<%= customer.getUser().getUserEmail().getEmail() %>" />
		<input type="text" name="u_email_visible" class="input_extra_large" readonly="readonly"
			value="<%= customer.getUser().getUserEmail().getEmail() %>" /><br />
		<div id="u_email"></div>
	</div>
	
	<%
	if (!readOnly) {
	%>
		<div>
	        <label for="u_password"><span></span></label>
	        <a href="editCustomerPassword.jsp?k=<%= request.getParameter("k") %>"><%= printer.print("Change password") %></a>
		</div>
	<%
	}
	%>
	
	<div>
       	<label for="c_address1"><span><%= printer.print("Address") %> <span class="required_field">*</span></span></label>
		<input type="text" name="c_address1" class="input_extra_large" value="<%= customer.getCustomerAddress().getAddress() %>" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="c_address1"></div>
	</div>
	
	<div>
       	<label for="c_address2"><span></span></label>
		<input type="text" name="c_address2" class="input_extra_large" value="" <%= readOnly ? "readonly=\"readonly\"" : "" %> /><br />
		<div id="c_address2"></div>
	</div>
	
	<div>
       	<label for="c_comments"><span><%= printer.print("Comments") %></span></label>
		<textarea  name="c_comments" class="input_extra_large" value="" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= customer.getCustomerComments() %></textarea><br />
		<div id="c_comments"></div>
	</div>
	
	<div>
	<% if (message != null && message.equals("success")) { 
		if (action != null && action.equals("update")) {
			if (updateType != null && updateType.equals("P")) {
	%>
				<div class="success-div"><%= printer.print("Customer password changed successfully") %>.</div>
	<% 
			}
		}
	} 
	%>
	</div>
	
	</fieldset>

	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listCustomer.jsp'" class="button-close"/>
	
	<%
	if (!readOnly) {
	%>
		<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>
	<%
	}
	else {
	%>
		<input type="button" value="&nbsp;&nbsp;&nbsp;<%= printer.print("Edit") %>&nbsp;&nbsp;&nbsp;" onClick="location.href='/admin/editCustomer.jsp?k=<%= request.getParameter("k") %>'" class="button_style">
	<%
	}
	%>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
