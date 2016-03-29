<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-topbar.css" />
<link rel="stylesheet" type="text/css" href="../js/ddlevelsfiles/ddlevelsmenu-sidebar.css" />

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
	<li><a href="/admin/listAdmin.jsp"><%= printer.print("Administrators") %></a></li>
	<li><a href="#" rel="station"><%= printer.print("Station") %></a></li>
	<li><a href="#" rel="customers"><%= printer.print("Customers") %></a></li>
	<li><a href="#" rel="multimedia"><%= printer.print("Multimedia Content") %></a></li>
	<li><a href="#" rel="configuration"><%= printer.print("Configuration") %></a></li>
</ul>
</div>

<script type="text/javascript">
ddlevelsmenu.setup("ddtopmenubar", "topbar") //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
</script>

<ul id="station" class="ddsubmenustyle">
	<li><a href="/admin/listStation.jsp"><%= printer.print("Stations") %></a></li>
	<li><a href="/admin/listStationType.jsp"><%= printer.print("Station Types") %></a></li>
</ul>

<ul id="customers" class="ddsubmenustyle">
	<li><a href="/admin/listCustomer.jsp"><%= printer.print("Customers") %></a></li>
</ul>

<ul id="multimedia" class="ddsubmenustyle">
	<li><a href="/admin/listGenre.jsp"><%= printer.print("Genres") %></a></li>
	<li><a href="/admin/listMusicFile.jsp"><%= printer.print("Public Music Library") %></a></li>
</ul>

<ul id="configuration" class="ddsubmenustyle">
	<li><a href="/admin/listRegion.jsp"><%= printer.print("Regions") %></a></li>
	<li><a href="/admin/editSystem.jsp?readonly=true"><%= printer.print("System") %></a></li> 
</ul>