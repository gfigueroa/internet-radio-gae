<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
%>

<div class="logout-bar">
  			<% if(printer.getLanguageString().equals("ENGLISH")){%>
				<a href="/handleSession?action=destroy"><img class="logout-bar-image" src="/images/logout.png" width="70px"></img></a>
			<% } else { %>
				<a href="/handleSession?action=destroy"><img class="logout-bar-image" src="/images/logout_ch.png" width="60px"></img></a>
			<% }%>
</div>
