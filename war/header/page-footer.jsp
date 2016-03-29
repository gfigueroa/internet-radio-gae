<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
String getsetLanguage=printer.getLanguageString();
%>

<div class=language_bar>
		<%= printer.print("Language")%>:
		<form name="language_change" onsubmit="refreshpage();" method="POST" >
			<select name="language_selection" onchange="document.language_change.submit();">
					<%Printer text = new Printer(Dictionary.Language.CHINESE);%>
				  <option <%= getsetLanguage =="CHINESE"? "selected=\"true\"" : ""%> value="CH"><%= text.print("Chinese")%></option>
				  <option <%= getsetLanguage =="ENGLISH"? "selected=\"true\"" : ""%> value="EN">English</option>
			</select>
		</form>
</div>

<div id="page-footer">
<p>
	Smart Personalized Service Technology 2013 &reg; - All rights reserved
	<br/>
	System Version 1.0.6 (Alpha)
</p>
</div>