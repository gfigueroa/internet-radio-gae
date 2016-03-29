<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Genre" %>
<%@ page import="datastore.GenreManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MusicFile" %>
<%@ page import="datastore.MusicFileManager" %>
<%@ page import="com.google.appengine.api.blobstore.BlobInfoFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
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
  
  MusicFile musicFile = MusicFileManager.getMusicFile(Long.parseLong(request.getParameter("k")));
  Genre genre = GenreManager.getGenre(musicFile.getGenre());
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
      action=""
      class="form-style">

    <fieldset>
	<legend><%= printer.print("Music File Information") %></legend>
			
	<div>
	  <h2><%= printer.print("View Music File") %></h2>
	</div>
	
	<div>
        <label for="m_genre"><span><%= printer.print("Genre") %> </span></label>
		<input type="text" name="m_genre" class="input_extra_large" value="<%= genre != null ? genre.getGenreEnglishName() : printer.print("Not assigned yet") %>" readonly="readonly" /><br />
		<div id="m_genre"></div>
	</div>
	
    <div>
        <label for="m_title"><span><%= printer.print("Music File Title") %> </span></label>
		<input type="text" name="m_title" class="input_extra_large" value="<%= musicFile.getMusicFileTitle() %>" readonly="readonly" /><br />
		<div id="m_title"></div>
	</div>
	
	<div>
        <label for="m_key"><span><%= printer.print("Music File Key") %> </span></label>
		<input type="text" name="m_key" class="input_extra_large" value="<%= musicFile.getKey() %>" readonly="readonly" /><br />
		<div id="m_key"></div>
	</div>
	
	<div>
        <label for="m_blobkey"><span><%= printer.print("Music File BlobKey") %> </span></label>
		<input type="text" name="m_blobkey" class="input_extra_large" value="<%= musicFile.getMusicFileFile().getKeyString() %>" readonly="readonly" /><br />
		<div id="m_blobkey"></div>
	</div>
	
	<%
    if (musicFile.getMusicFileFile() != null) {
    %>
    <div>
       	<label for="m_file"><span><%= printer.print("File") %></span></label>
       	<%
			BlobInfoFactory bif = new BlobInfoFactory();
			BlobKey blobKey = new BlobKey(musicFile.getMusicFileFile().getKeyString());
	        String fileName = bif.loadBlobInfo(blobKey).getFilename();
		%>
		<a href="/fileDownload?file_id=<%= musicFile.getMusicFileFile().getKeyString() %>">
			<%= fileName %>
		</a>
		<br />
		<div id="m_file"></div>
	</div>
	<%
	}
	%>
	
	</fieldset>
  
	<br class="clearfloat" />
	
	<input type="button" value="<%= printer.print("Close") %>" onclick="location.href='/admin/listMusicFile.jsp'" class="button-close"/>

</form>

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
