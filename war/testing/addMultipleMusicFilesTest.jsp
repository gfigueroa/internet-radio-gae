<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<%
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<body>

<h1>Add Multiple Music Files Test</h1>

<form method="post" 
		id="form1" 
		name="form1"
		action="<%= blobstoreService.createUploadUrl("/manageObject?action=add&type=multipleMusic") %>"
		enctype="multipart/form-data"
		class="form-style">

	<br/><br/><br/>
	<b>Choose the file to upload:</b>
	<input name="file" type="file" id="file" multiple />
	
	<input type="text" name="genre" value="123" style="display:none;" />
	
	<input type="submit" value="Update" class="button_style"/>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>