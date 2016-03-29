var validate=function(type){
	obj=document.getElementById('file');
	fileUpload=document.getElementById('file-upload');
	uploadReset=document.getElementById('upload-reset');
	var fileName = obj.value;
	if(fileName.match(/.csv$/)||fileName.match(/.txt$/)){
		fileUpload.setAttribute("action", "/uploadfile?type=" + type);
		fileUpload.setAttribute("enctype", "multipart/form-data");
		fileUpload.setAttribute("method","post");
		 document.form.submit();
		return true;
	} else if(fileUpload==undefined){
		alert('No file selected. Please choose one');
		uploadReset.click();
		return false;
	}else{
		alert('Please select a "csv" or "txt" file');
		uploadReset.click();
		return false;
	}
}