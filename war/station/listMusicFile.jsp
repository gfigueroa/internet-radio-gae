<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="datastore.Genre" %>
<%@ page import="datastore.GenreManager" %>
<%@ page import="datastore.User" %>
<%@ page import="datastore.UserManager" %>
<%@ page import="datastore.MusicFile" %>
<%@ page import="datastore.MusicFileManager" %>
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
  	if (sessionUser.getUserType() != User.Type.STATION) {
  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  	}
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />
<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.colorbox-min.js"></script>
<script src="../js/popup.js"></script>
<script src="../js/confirmAction.js"></script>

<%@  include file="../header/page-title.html" %>

</head>

<body>
<jsp:include page="../header/logout-bar.jsp" />
<%@  include file="../header/page-banner.html" %>
<jsp:include page="../menu/main-menu.jsp" />

<h1><%= printer.print("Public Music Library") %></h1>

<%
	List<MusicFile> musicFileList = MusicFileManager.getAllMusicFiles();
%>

<div>
	<h3></h3>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="list-table">
  
  <tr>
    <td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="40%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="20%"></td>
    <td width="10%"></td>
    <td width="10%"></td>
    <td width="40%"></td>
    <td width="20%"></td>
  </tr>
  
  <tr>
  	<td width="20%"><%= printer.print("Music File") %></td>
  	<td width="10%"><%= printer.print("Genre") %></td>
  	<td width="10%"><%= printer.print("Key") %></td>
  	<td width="40%"><%= printer.print("BlobKey") %></td>
    <td width="20%"></td>
  </tr>
  
<% 
  for (MusicFile musicFile : musicFileList) {
  	Genre genre = GenreManager.getGenre(musicFile.getGenre());
%>
  <tr>
    <td width="20%"><%= musicFile.getMusicFileTitle() %></td>
    <td width="10%"><%= genre != null ? genre.getGenreEnglishName() : printer.print("Not assigned yet") %></td>
    <td width="10%"><%= musicFile.getKey() %></td>
    <td width="40%"><%= musicFile.getMusicFileFile().getKeyString() %></td>
    <td width="20%"></td>
  </tr>
<%  
  }
%>
</table>                                                                                                        

<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
