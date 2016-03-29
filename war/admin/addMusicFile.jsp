<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Genre" %>
<%@ page import="datastore.GenreManager" %>
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
  User sessionUser = (User) session.getAttribute("user");
  if (sessionUser == null)
    response.sendRedirect("../login.jsp");
  else {
  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
  
  String error = request.getParameter("etype");
  BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  List<Genre> genres = GenreManager.getAllGenres();
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
      action="<%= blobstoreService.createUploadUrl("/manageObject?action=add&type=musicFile") %>"
      class="form-style"
      enctype="multipart/form-data">

    <fieldset>
    <legend><%= printer.print("Music File Information") %></legend>
	
	<div>
	  <h2><%= printer.print("Add Music File") %></h2>
	</div>
	
	<% if (error != null && error.equals("MissingInfo")) { %>
		<div class="error-div"><%= printer.print("You are missing some essential information needed by the system") %></div>
	<% } %>
	<% if (error != null && error.equals("ObjectExists")) { %>
		<div class="error-div"><%= printer.print("There is already a music file in the music library with this title") %></div>
	<% } %>
		
	<div>
       	<label for="file"><span><%= printer.print("File") %></span></label>
		<input type="file" name="file" class="input_extra_large" value="" /><br />
		<div id="file"></div>
	</div>
	
	<div>
		<label for="genre"><span><%= printer.print("Genre") %> <span class="required_field">*</span></span></label>
		<select name="genre">
		<%
		for (Genre genre : genres) {
		%>
        	<option class="genre" value="<%= genre.getKey() %>"><%= genre.getGenreEnglishName() %></option>
        <%
		}
		%>
        </select>
		<div id="genre"></div>
	</div>
	
    <div>
        <label for="m_title"><span><%= printer.print("Music File Title") %> <span class="required_field">*</span></span></label>
		<input type="text" name="m_title" class="input_extra_large" value="" /><br />
		<div id="m_title"></div>
	</div>

	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onClick="location.href='/admin/listMusicFile.jsp'" class="button-close"/>

	<input type="submit" value="<%= printer.print("Update") %>" class="button_style"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
