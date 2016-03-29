/**
 * Treeview language
 */

function doMenu(item) {
 obj=document.getElementById(item);
 col=document.getElementById("x" + item);
 if (obj.style.display=="block") {
	 obj.style.display="none";
	 col.innerHTML="[+]";
 }
 else {
	 obj.style.display="block";
	 col.innerHTML="[-]";
  
 }
}