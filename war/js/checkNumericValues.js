/**
 * Checks if this attribute is an integer
 * @param attribute
 * 			: the attribute to check
 * @return true if the attribute is an integer, false otherwise
 */
function isInteger(attribute, attributeName) {
   var ValidChars = "0123456789";
   var IsInteger=true;
   var Char;
 
   for (i = 0; i < attribute.length && IsInteger == true; i++) { 
      Char = attribute.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsInteger = false;
      }
   }
   
   if (!IsInteger) {
	   alert("The " + attributeName + " you entered is not valid.");
   }
   
   return IsInteger; 
}