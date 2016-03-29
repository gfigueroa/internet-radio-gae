<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<% 
Printer printer = (Printer)session.getAttribute("printer");
String language_selected = request.getParameter("language_selection");
if(language_selected !=null && language_selected.equals("EN")){
		printer.setLanguage(Dictionary.Language.ENGLISH);
	}
if(language_selected !=null && language_selected.equals("CH")){
		printer.setLanguage(Dictionary.Language.CHINESE);
}
session.setAttribute("printer", printer);
%>