<%@ page import="javax.servlet.*,javax.servlet.http.*,javax.servlet.jsp.* "%>
<%@ page import="java.util.Set,java.util.Iterator,java.io.IOException"%>
<%!
  private void preCompileJsp(PageContext pageContext, JspWriter out,
   HttpServletRequest request, HttpServletResponse response, String uripath)
   throws IOException, ServletException {
 
     // getting jsp files list for pre compilation
     Set jspFilesSet = pageContext.getServletContext().getResourcePaths(uripath);
     for (Iterator jspFilesSetItr = jspFilesSet.iterator(); jspFilesSetItr.hasNext();) {
         String uri = (String) jspFilesSetItr.next();
         if (uri.endsWith(".jsp")) {
             RequestDispatcher rd = getServletContext().getRequestDispatcher(uri);
             if (rd == null) throw new Error(uri +" - not found");
             rd.include(request, response);
         }
         else if (uri.endsWith("/")) {
             preCompileJsp(pageContext, out, request, response, uri);
         }
     }
   }
%>
<html><head><title>Pre Compile JSP Files</title></head>
<body>
<%
  HttpServletRequest req = new HttpServletRequestWrapper(request) {
      public String getQueryString() {
          // setting the pre compile parameter
          return "jsp_precompile";
      };
  };
  preCompileJsp(pageContext, out, req, response, "/");
%>
JSP files Pre Compile Completed.
</body></html>