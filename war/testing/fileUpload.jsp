<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>


<%@ page language="java"%>
<HTml>
<%--<link rel="stylesheet" type="text/css" href="css/displaytagex.css">--%>


<script language="javascript" src='../js/uploadFile.js'></script>

<!-- *************************************** Upload tab****************************************** -->
<div class="g-unit" id="upload-tab">
	<div class="message" id="upload-show-message" style="display: none"></div>
	<h2>Upload Retail Stores</h2>
	<div class="results" style="border: 0;" id="upload-list-ctr">
		<form id="file-upload">
			<table>
				<tr>
					<td><b>Choose the file To Upload:</b></td>
					<td><input name="file" type="file" id="file"></td>
				</tr>
				<tr>
					<td><input type="reset" id="upload-reset" value="reset"
						style="visibility: hidden"></td>
					<td><input type="submit" value="Upload Orders"
						onclick="return validate()"></td>
				</tr>
			</table>
		</form>
	</div>
</div>

</BODY>
</HTML>