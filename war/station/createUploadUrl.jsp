<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="java.net.URLDecoder"%>

<%
BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
String url = URLDecoder.decode(request.getParameter("url"), "UTF-8");
String result;

result = blobstoreService.createUploadUrl(url);

response.setContentType("application/json");

response.getWriter().print("{\"uploadUrl\": \""+ result + "\"}");
%>