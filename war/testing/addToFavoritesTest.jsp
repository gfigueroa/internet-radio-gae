<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css" rel="stylesheet" />

<%@  include file="../header/page-title.html" %>
</head>

<body>

<h1>Add to My Favorites Test</h1>

<form method="post" id="form1" name="form1"
		action="/cloudSync?action=addToFavorites"
		class="form-style">
	
	<input type="text" name="u_email" value="customer@smasrv.com" style="display:none;" />
	<input type="text" name="u_password" value="b6c45863875e34487ca3c155ed145efe12a74581e27befec5aa661b8ee8ca6dd" style="display:none;" />
	<input type="text" name="r_type" value="STORE" style="display:none;" />
	<input type="text" name="r_comment" value="Comment!" style="display:none;" />
	<input type="text" name="i_key" value="agpzbWFzcnYtY29zciMLEgpSZXN0YXVyYW50IhNjaGFubmVsMUBzbWFzcnYuY29tDA" style="display:none;" />
	
	<button type="submit" value="Update" class="button"/>
		<img src="../images/update.jpg" class="img-button"/>
	</button>

</form>

<%@  include file="../header/page-footer.html" %>

</body>
</html>
