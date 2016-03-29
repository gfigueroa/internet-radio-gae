<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-topbar.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-sidebar.css" />
<link rel="stylesheet" type="text/css" href="../stylesheets/station-menu.css" />

<script type="text/javascript" src="../js/ddlevelsfiles/ddlevelsmenu.js">

/***********************************************
* All Levels Navigational Menu- (c) Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

</script>	

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>

<div id="ddtopmenubar" class="mattblackmenu">
<ul>
  <li><a href="/station/djInterface.jsp" ><%= printer.print("DJ Interface") %></a></li>
  <li><a href="/station/listChannel.jsp" ><%= printer.print("Channels") %></a></li>
  <li><a href="/station/listProgram.jsp" ><%= printer.print("Programs") %></a></li>
  <li><a href="#" rel="multimedia"><%= printer.print("Multimedia Content") %></a></li>
  <li><a href="/station/editStation.jsp" rel="station"><%= printer.print("Station Profile") %></a></li>
</ul>
</div>

<script type="text/javascript">
ddlevelsmenu.setup("ddtopmenubar", "topbar") //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
</script>

<ul id="multimedia" class="ddsubmenustyle">
  <li><a href="/station/listStationAudio.jsp" ><%= printer.print("Station Audio") %></a></li>
  <li><a href="/station/listStationImage.jsp" ><%= printer.print("Station Image") %></a></li>
  <li><a href="/station/listMusicFile.jsp" ><%= printer.print("Public Music Library") %></a></li>
</ul>