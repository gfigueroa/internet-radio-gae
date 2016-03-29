/**
 *Print Page
 */

function printPage(id1,id2)
{
		str1=document.getElementById(id1).innerHTML;
		str2=document.getElementById(id2).innerHTML;
		//newwin=window.open('','printwin','left=100,top=100,width=800,height=800');
		newwin=window.open();
		newwin.document.write('<HTML>\n<HEAD>\n');
		newwin.document.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
		newwin.document.write('<link type="text/css" href="../stylesheets/printing.css" rel="stylesheet" />');
		newwin.document.write('<link type="text/css" href="../stylesheets/colorbox.css" rel="stylesheet" />');
		newwin.document.write('<script src="../js/jquery.min.js"></script>');
		newwin.document.write('<script src="../js/jquery.colorbox-min.js"></script>');
		newwin.document.write('<TITLE>Print Page - Order</TITLE>\n');
		newwin.document.write('<script>\n');
		newwin.document.write('function chkstate(){\n');
		newwin.document.write('if(document.readyState=="complete"){\n');
		newwin.document.write('window.close()\n');
		newwin.document.write('}\n');
		newwin.document.write('else{\n');
		newwin.document.write('setTimeout("chkstate()",2000)\n');
		newwin.document.write('}\n');
		newwin.document.write('}\n');
		newwin.document.write('function print_win(){\n');
		newwin.document.write('window.focus();\n');
		newwin.document.write('window.print();\n');
		newwin.document.write('chkstate();\n');
		newwin.document.write('}\n');
		newwin.document.write('<\/script>\n');
		newwin.document.write('</HEAD>\n');
		newwin.document.write('<BODY onload="print_win()">\n');
		newwin.document.write('</br></br></br>\n');
		newwin.document.write(str1);
		newwin.document.write(str2);
		newwin.document.write('</BODY>\n');
		newwin.document.write('</HTML>\n');
		newwin.document.write('</br></br></br></br>\n');
		newwin.document.close();
		newwin.focus();
		//newwin.print();
		//newwin.close();


}