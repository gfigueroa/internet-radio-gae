<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>

<%@ page language="java"%>
<% 
	String audioBlobKeyString = request.getParameter("file_id");
%>
<html>
<%--<link rel="stylesheet" type="text/css" href="css/displaytagex.css">--%>

<script language="javascript" src='../js/uploadFile.js'></script>

<!-- *************************************** Upload tab****************************************** -->
<div class="g-unit" id="upload-tab">
	<div class="message" id="upload-show-message" style="display: none"></div>
	<h2>MP3 Streaming</h2>
	<audio controls> 
		<source src="/audioStreaming?file_id=<%= audioBlobKeyString %>" type="audio/mpeg"> 
			Your browser does not support the audio element. 
		</source>
	</audio>
</div>

</body>
</html>