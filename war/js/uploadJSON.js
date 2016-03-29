var validate = function(action) {
	obj = document.getElementById('json_file');
	fileUpload = document.getElementById('file-upload');
	uploadReset = document.getElementById('upload-reset');
	var fileName = obj.value;
	if (fileName.match(/.json$/) || fileName.match(/.txt$/)) {
		fileUpload.setAttribute("action", "/stationUpload?action=" + action);
		fileUpload.setAttribute("enctype", "multipart/form-data");
		fileUpload.setAttribute("method", "post");
		document.form.submit();
		return true;
	} 
	else if (fileUpload == undefined) {
		alert('No file selected. Please choose one');
		uploadReset.click();
		return false;
	} 
	else {
		alert('Please select a "json" or "txt" file');
		uploadReset.click();
		return false;
	}
}