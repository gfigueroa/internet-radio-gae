/**
 * Confirms the delete action
 * @param url:
 * 			the incoming URL
 * @param itemName:
 * 			the name of the item to delete
 * @return true if action is confirmed, false otherwise
 */


function confirmDelete(url, itemName) {
	//var message="Are you sure you want to delete this " + itemName + "?";
	var message=itemName + "?";
	var answer = confirm (message);
	if (answer){
		window.location = url;
		return true;
	}
	else {
		return false;
	}
}