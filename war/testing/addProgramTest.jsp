<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Add Program Test</h1>

<form method="post" 
		id="form1" 
		name="form1"
		action="/stationUpload?action=add&type=program"
		enctype="multipart/form-data"
		class="form-style">

	<br/><br/><br/>
	<b>Choose the file to upload:</b>
	<input name="json_file" type="file" id="json_file"/>
	
	<input type="submit" value="Update" class="button_style"/>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>