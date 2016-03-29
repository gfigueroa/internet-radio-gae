$(document).ready(function(){
	setMainTrackMenu();
	setDownloadMenu();
    setSecondaryTrackMenu();
    setUploader();
    setSliderUploader();
    setSecondaryUploader();
    setMainTrackPlayer();
    setSecondaryTrackPlayer();
    setHelperPlayer();
    setSlideShow();
    setSaveProgram();
	setProgramForm();
    setMessagesForm(); 
    setFacebookShare();
});
function setFacebookShare()
{
	var accordionChannel,
		active,
		activeProgram,
		facebookShare,
		urlToShare,
		options;
		
	accordionChannel = $('#accordion_channel');
	
	active = accordionChannel.accordion('option','active');
	
	facebookShare = $('#facebookShare');
	
	if(active === false || !accordionChannel.find('div:eq(' + active + ')').data().hasOwnProperty('active_program')){
		
		facebookShare.button({disabled: true});
		
		return;
	}
		
	
	facebookShare.button('enable').click(function(event){
		
		options = {
			bgColor 		: '#fff',
			duration		: 400,
			opacity			: 0.7,
			classOveride 	: false
		}
		
		accordionChannel = $('#accordion_channel');
	
		active = accordionChannel.accordion('option','active');
	
		facebookShare = $('#facebookShare');
	
		activeProgram = accordionChannel.find('div:eq(' + active + ')').data().active_program;
	
		urlToShare = window.location.origin + '/station/djPlayer.jsp?c=' + activeProgram.channelKey;

		urlToShare = urlToShare + '&lang=' + $('form[name="language_change"] select').val().toLowerCase();
	
		if(urlToShare === null || typeof urlToShare === 'undefined'){
			return;
		}		
		
		var newwindow = window.open(facebookShare.attr('url') + encodeURIComponent(urlToShare), 
			      'facebook-share-dialog', 
			      'width=626,height=436');
	
		
		$('#popupMessage').html($('#fbComfirmDialog').text().trim());
		$('#popupMessage').hide();
		
		var buttons = {};
		buttons[$('#ok').text().trim()] = function() { $(this).dialog('close'); };
		
		var wait_200_msec=false;
		var wait_3_sec=false;
		var waitingFB,waitingFB2;
		var ajaxLoadCircle3 = new ajaxLoader($("#accordion_channel").parent().parent(),options,'2','absolute');
	 	
		
 		waitingFB = setInterval(function(){
 										 										 										 										 	
 			if(wait_200_msec)
	 		{	
	 			clearInterval(waitingFB);
	 			
	 			newwindow.close();
	 		}

 			wait_200_msec=true;
			
 		},200); 		
 		
 		
 		waitingFB2 = setInterval(function(){
		 	
 			if(wait_3_sec)
	 		{	
	 			clearInterval(waitingFB2);
	 			
	 			if(ajaxLoadCircle3)
	 				ajaxLoadCircle3.remove();
	 				 			
	 			$('#popupMessage').dialog({
	 				autoOpen: true,
	 				height: 130,
	 				width: 350,
	 				buttons: buttons
	 			}).on('dialogclose', function(event, ui){
	 				
	 				$('#popupMessage').hide();
	 				
	 				 window.open(facebookShare.attr('url') + encodeURIComponent(urlToShare), 
	 					      'facebook-share-dialog', 
	 					      'width=626,height=436');	 				 	 				 		
	 			});
	 		}

 			wait_3_sec=true;
			
 		},3000);
 			

	});
	
}
function setProgramForm()
{
	$('#programForm').dialog({
		autoOpen: false,
		height: 320,
		width: 350,
		modal: true		
	});
}
function setMessagesForm()
{
	var buttons = {};
	
	buttons[$('#ok').text().trim()] = function() { $(this).dialog('close'); };
	
	$('#selectMessage').dialog({
		autoOpen: false,
		height: 130,
		width: 350,
		modal: true,
		buttons: buttons
	});
	
	$('#channelMessage').dialog({
		autoOpen: false,
		height: 130,
		width: 350,
		modal: true,
		buttons: buttons
	});
}
function setSaveProgram()
{
	$('#saveProgramBtn').button({icons: {primary: 'ui-icon-disk'}, text: false})
						.click(function(event){
							//uploadProgram();
							checkSaveState();
						});
}
function updateAfterUpload(state, result)
{
	var actions = {},
		buttons = {},
		accordionChannel,
		channel,
		active_program,
		active_channel_index,
		no_waiting;
	
	actions['success'] = function(state){
		
		$('#programForm').dialog('close');
		
		$('#popupMessage').html($('#programSavedSuccess').text().trim());
		
		buttons[$('#ok').text().trim()] = function() { $(this).dialog('close'); };
		
		
		$('#accordion_channel').trigger('click');
		 $('#accordion_channel').trigger('click');	
		 			 
		 active_channel_index = $("#accordion_channel").accordion('option', 'active');				
		 setProgramInfo(active_channel_index, $('#stationContainer').data().channelInfo,$('#stationContainer').data().waiting_options,no_waiting);
		 
		 var waitPrograms = setInterval(function(){
			 
			 accordionChannel = $('#accordion_channel');
			 
			 if(accordionChannel.find('div:eq(' + active_channel_index + ') p').is(':visible')){
					clearInterval(waitPrograms);
					
					
					if(accordionChannel.accordion('option', 'active') !== false){
						
						channel = accordionChannel.find('div:eq(' + accordionChannel.accordion('option','active') + ')');
						
						if(state === 'create' || state === 'createFromExisting'){
							//active_program = channel.data().program_info[channel.data().program_info.length - 1];
							console.log(channel.find('p:last').html());
							channel.find('p:last').click();
						}
						else if(state === 'overwrite'){
							
							if(channel.data().hasOwnProperty('active_program_index')){
							//active_program = channel.data().program_info[channel.data().active_program_index];
						    channel.find('p:eq('+ channel.data().active_program_index + ')').click();
							}
						}
					}
			 }
		 }, 200);
		 
		 
		$('#popupMessage').dialog({
			autoOpen: true,
			height: 130,
			width: 350,
			buttons: buttons
		}).on('dialogclose', function(event, ui){
			//refresh the program list
		/*	 $('#accordion_channel').trigger('click');
			 $('#accordion_channel').trigger('click');	
			 			 
			 active_channel_index = $("#accordion_channel").accordion('option', 'active');				
			 setProgramInfo(active_channel_index, $('#stationContainer').data().channelInfo,$('#stationContainer').data().waiting_options,no_waiting);
			 
			 var dialogW1 = this,
			 	 waitPrograms = setInterval(function(){
				 			 
			 accordionChannel = $('#accordion_channel');
				console.log(accordionChannel.find('div:eq(' + active_channel_index + ') p').is(':visible'));
			 if(accordionChannel.find('div:eq(' + active_channel_index + ') p').is(':visible')){
				clearInterval(waitPrograms);
				
				
				if(accordionChannel.accordion('option', 'active') !== false){
					
					channel = accordionChannel.find('div:eq(' + accordionChannel.accordion('option','active') + ')');
					
					if(state === 'create' || state === 'createFromExisting'){
						//active_program = channel.data().program_info[channel.data().program_info.length - 1];
						console.log(channel.find('p:last').html());
						channel.find('p:last').click();
					}
					else if(state === 'overwrite'){
						
						if(channel.data().hasOwnProperty('active_program_index')){
						//active_program = channel.data().program_info[channel.data().active_program_index];
					    channel.find('p:eq('+ channel.data().active_program_index + ')').click();
						}
					}
					/*$.data(channel[0],
							   'active_program',
							   active_program);
						
						setFacebookShare();*/
					//}
					
					$(this).dialog('destroy');
			/* }
			 }, 200);*/
			 
			});
				
	};
	
	actions['failure'] = function(state){
		
		$('#programForm').dialog('close');
				
		$('#popupMessage').html($('#programSavedFailure').text().trim());
		
		buttons[$('#ok').text().trim()] = function() { $(this).dialog('close'); };
		
		$('#popupMessage').dialog({
			autoOpen: true,
			height: 130,
			width: 350,
			buttons: buttons
		}).on('dialogclose', function(event, ui){
			//refresh the program list
			 $('#accordion_channel').trigger('click');
			 $('#accordion_channel').trigger('click');
			 
			 $(this).dialog('destroy');
		});
	};
	actions[result](state);
}
function handleUploadProgram(state)
{
	var message,
		i,
		str = '',
		tmp;
	
	/*if(state === 'createFromExisting'){
		$('#programSequenceNumber')
			.val($('#accordion_channel').find('div:eq(' + $('#accordion_channel').accordion('option', 'active') + ') p').length + 1);
	}*/
	
	message = uploadProgram(state);
	
	if(message.length > 0){
	
		if(message[0] === 'success' || message[0] === 'failure'){
			
			updateAfterUpload(state, message[0]);
						
		}
		else{
					
			for(i = 0; i < message.length; i++){
				str += message[i] + '<br/>';
			}
		
			$('#validationMessage').html(str);
			
			if(!$('#programForm').data().hasOwnProperty('resized')){
			
				$.data($('#programForm')[0], 'resized', $('#validationMessage').height());
			
				$('#programForm').dialog('option', 'height', $('#programForm').dialog('option', 'height') + $('#validationMessage').height());
			}
		}
	}
	else if($('#programForm').data().hasOwnProperty('resized')){
		$('#programForm').dialog('option', 'height', $('#programForm').dialog('option', 'height') - $('#programForm').data().resized);
		$.removeData($('#programForm')[0], 'resized');
	}
	
}
function openProgramForm(state, params)
{
	var action = {},
		programForm,
		buttons;
	
	programForm = $('#programForm');
	
	action['new'] = function(){
		programForm.dialog('option', 'title', $('#newProgram').text().trim());
		
		buttons = {};
		
		buttons[$('#createProgram').text().trim()] = function(){ handleUploadProgram('create'); };		
		buttons[$('#cancel').text().trim()] = function() { $(this).dialog('close'); };
		
		programForm.dialog('option', 'buttons', buttons);
		
		$('#programSequenceNumber').val(params.sequenceNumber);
		
		programForm.dialog('open');
	};
	

	action['select'] = function(){
		$('#selectMessage').dialog('open');
	};
	
	action['overwrite'] = function(){
		programForm.dialog('option', 'title', $('#newProgram').text().trim());
		
		buttons = {};
		
		buttons[$('#overwriteProgram').text().trim()] = function(){
													var activeProgram;
													
													handleUploadProgram('overwrite'); 
													
													activeProgram = $('#accordion_channel div:eq(' + $('#accordion_channel').accordion('option', 'active') + ')');													
													
													//$.removeData($(activeProgram[0], 'active_program'));
													};
		
		buttons[$('#createProgram').text().trim()] = function(){ 
												 var activeProgram;
												 
												 $('#programSequenceNumber').val(params.sequenceNumber);
												 
												 handleUploadProgram('createFromExisting');
												 
												 activeProgram = $('#accordion_channel div:eq(' + $('#accordion_channel').accordion('option', 'active') + ')');
													
												  $.removeData($(activeProgram[0], 'active_program'));
												 };
		
		buttons[$('#cancel').text().trim()] = function() { $(this).dialog('close'); };		
		
		programForm.dialog('option', 'buttons', buttons);
		
		$('#programName').val(params.activeProgram.programName);
		$('#programDescription').val(params.activeProgram.programDescription);
		$('#programSequenceNumber').val(params.activeProgram.programSequenceNumber);
		
		programForm.dialog('open');
	};
	
	action[state]();
	
}
function openChannelForm(state)
{
	$('#channelMessage').dialog('open');
}
function checkSaveState()
{
	var accordionChannel,
		activeProgram;
	
	accordionChannel = $('#accordion_channel');
	
	if(accordionChannel.find('div').length === 0){
		openChannelForm();
	}
	else if( accordionChannel.accordion('option', 'active') === false){
		openProgramForm('select');
	}
	else{
		activeProgram = $('#accordion_channel div:eq(' + accordionChannel.accordion('option', 'active') + ')').data().active_program;
		
		if(activeProgram === null || typeof activeProgram === 'undefined'){
			openProgramForm('new', {sequenceNumber: 
		         accordionChannel.find('div:eq(' + accordionChannel.accordion('option', 'active') + ') p').length + 1});
		}
		else{
			
			openProgramForm('overwrite', {activeProgram : activeProgram, 
										  sequenceNumber : accordionChannel.find('div:eq(' + accordionChannel.accordion('option', 'active') + ') p').length + 1});
		}
	}
}

function setDownloadSlideTimeMark2(Slide, mainTrackDuration)
{
	var slidesTimeMarks,
		slideInfo = {},
		chosenSlideIndex,
		mainPlayerStatus,
		i,
		length,
		newSlide = true,
		container,
		mark,
		positionInContainer;
	
	mainPlayerStatus = $('#jquery_jplayer_1').data().jPlayer.status;
	
	if(!$('#slidesTimeMarkContainer').data().hasOwnProperty('slidesTimeMarks')){
		$.data($('#slidesTimeMarkContainer')[0], 'slidesTimeMarks', []);
	}
	
	container = $('#slidesTimeMarkContainer');
	
	slideInfo.slide = Slide;
	slideInfo.time = Slide.startingTime;
	slideInfo.position = Slide.slideIndex;
	
	//if(!checkForSlidesCollisions(slideInfo, null)){
		
		container.data().slidesTimeMarks.push(slideInfo);
		
		container.data().slidesTimeMarks.sort(sortSlidesByTime);
		
		setSlideTimeMarkHelper(slideInfo, mainTrackDuration);
	//}
}
function setDownloadSlideTimeMark(SlideMarkIndex, SlidesMark)
{
	var slidesTimeMarks,
		slideInfo = {},
		chosenSlideIndex,
		mainPlayerStatus,
		i,
		length,
		newSlide = true,
		container,
		mark,
		positionInContainer;
	
	mainPlayerStatus = $('#jquery_jplayer_1').data().jPlayer.status;
	
	if(Galleria.get(0).getDataLength() === 0){
		return;
	}
	
	container = $('#slidesTimeMarkContainer');
	
	slideInfo.slide = Galleria.get(0).getData(SlidesMark[SlideMarkIndex].slideIndex).slide;
	slideInfo.time = SlidesMark[SlideMarkIndex].startingTime;
	slideInfo.position = SlidesMark[SlideMarkIndex].slideIndex;
	
	//if(!checkForSlidesCollisions(slideInfo, null)){
		
		container.data().slidesTimeMarks.push(slideInfo);
		
		container.data().slidesTimeMarks.sort(sortSlidesByTime);
		
		setSlideTimeMarkHelper(slideInfo);
	//}
}

function GetAudioNameFormat(type, Key){
	
	var result = {};
	var index=0;
	
	if(type=='FILE_UPLOAD'){
		
		$.ajax({
			url: '/rest/stationAudio/k=' + Key,
				data: {},
				async: false,
				cache: false,
				dataType: 'json',
				type: 'GET',
				success:function(json){
									
						index = json[0].stationAudioName.lastIndexOf('.');
						
						if(index === -1){
							result.fileNaturalName = json[0].stationAudioName;					
						}
						else{
							result.fileNaturalName = json[0].stationAudioName.substring(0, index);
						}
						
						result.fileExt = json[0].stationAudioFormat;				
				}
			});			
	}	
	else if(type=='MUSIC_FILE'){
		
		$.ajax({
			url: '/rest/musicLibrary/k=' + Key,
				data: {},
				async: false,
				cache: false,
				dataType: 'json',
				type: 'GET',
				success:function(json){
									
						index = json[0].musicFileTitle.lastIndexOf('.');
						
						if(index === -1){
							result.fileNaturalName = json[0].musicFileTitle;					
						}
						else{
							result.fileNaturalName = json[0].musicFileTitle.substring(0, index);
						}
						
						result.fileExt = json[0].musicFileFormat;				
				}
			});
	
	}
	
	return result;
}
	



function DownloadSecondTrackFile(downloadtype, fileIndex, secondaryTracks, selectedSong)
{
		
	 	var contentBody =  $('#secondaryTrackMenuContainer');
		var extFileName;
		var waiting;
	
			var template = $('#secondaryTrackTemplate').html(),
		    info,
		    song,
		    file = {},
		    positionSlider = {},
		    fadePercentageSlider = {},
		    fadeStepSlider = {},
		    fadeDurationSlider = {},
		    tmp;
			
			if(downloadtype == 'downloadProgram'){ 
				
			 		if(secondaryTracks[fileIndex].Type == 'FILE_UPLOAD'){
			 			
			 			file = GetAudioNameFormat(secondaryTracks[fileIndex].Type,secondaryTracks[fileIndex].StationAudio); 
			 			
			 			//file = getTrackInfo('dummy'+'.mp3');   
			 			file.secondaryTrackKey = secondaryTracks[fileIndex].SecondaryTrackKey;
			 			file.fileKey = secondaryTracks[fileIndex].StationAudioFileKey;
			 			file.datastoreObjectKey = secondaryTracks[fileIndex].StationAudio;
			 			file.secondaryTrackType = secondaryTracks[fileIndex].Type;
			 						 		
		 			}
			 		else if(secondaryTracks[fileIndex].Type == 'MUSIC_FILE'){
			 			
			 			file = GetAudioNameFormat(secondaryTracks[fileIndex].Type,secondaryTracks[fileIndex].MusicFileKey);
			 			
			 			//file = getTrackInfo('dummy'+'.mp3'); 
			 			file.secondaryTrackKey = secondaryTracks[fileIndex].SecondaryTrackKey;
			 			file.fileKey =secondaryTracks[fileIndex].MusicFileBlobKey;
			 			file.datastoreObjectKey = secondaryTracks[fileIndex].MusicFileKey;
			 			file.secondaryTrackType = secondaryTracks[fileIndex].Type;			 			
			 		}
			 		
			 		file.fadeInOutPct = secondaryTracks[fileIndex].FadeOutPct *100;
		 			file.fadeInOutDur = 2;
		 			file.fileDuration = secondaryTracks[fileIndex].Duration;
			}
			else if(downloadtype=='downloadOneAudio'){
										
						file = selectedSong;						
						file.fadeInOutPct = 20;
						file.fadeInOutDur = 2;
			}
				
		   
		//$.each(data.result, function (index, file) {
			
		   info = filler(template, 'Name', file.fileNaturalName);            
		   info = filler(info, 'Start', timeFormat(0));
		   info = filler(info, 'End', timeFormat(0));
		   info = filler(info, 'FadePercentage', '');
		   info = filler(info, 'FadeDuration', '');
		   
		   song = $(info);
		   
		   /*
		   file.fadeInOutPct = secondaryTracks[fileIndex].FadeOutPct *100;
		   file.fadeInOutDur = 2;
		   file.fileDuration = secondaryTracks[fileIndex].Duration;
		   */
		   
		   song.find('div:eq(0)').click(function(event){
        	   
        	   if($(this).parent().css('background-color') === 'rgba(0, 0, 0, 0)'){
        		   
        		   $('.secondaryTrackRow').css('background-color', 'rgba(0, 0, 0, 0)');
        		   
        		   $(this).parent().animate({backgroundColor: '#019be3'});
        		   
        		   $.data(contentBody[0], 'activeTrack', $(this).parent());
        	   }
        	   else {
        		   $(this).parent().css('background-color', 'rgba(0, 0, 0, 0)');
        		   
        		   $.data(contentBody[0], 'activeTrack', null);
        	   }
           });
		   	   	     
		   fadePercentageSlider.sliderHolder = song.find('div:eq(1) span');
		   fadePercentageSlider.tooltip1 = fadePercentageSlider.sliderHolder.prev();
		   fadePercentageSlider.percentage = file.fadeInOutPct;
		   
		   fadePercentageSlider.sliderHolder.slider({
		       min:1,
		       max:($('#jquery_jplayer_1').data().jPlayer.status.duration === 0 ? 1 : 100),
		       step:1,
		       value:file.fadeInOutPct,
		       slide:function(event, ui)
		       {
		           var songInfo;
		               
		           songInfo = $(this).parent().parent().data().songInfo;
		        
		           songInfo.fadeInOutPct = ui.value;
		           
		           fadePercentageSlider.tooltip1.tooltip('option', 'content', ui.value.toString() + '%');
		           
		           fadePercentageSlider.tooltip1.focusin();
		           
		           updateSecondaryTrackList(songInfo);
		           
		       },
		       stop:function(event, ui)
		       {
		           fadePercentageSlider.tooltip1.focusout();
		       }
		   });
		   
		   
		   fadeDurationSlider.sliderHolder = song.find('div:eq(2) span');
		   fadeDurationSlider.tooltip1 = fadeDurationSlider.sliderHolder.prev();
		   fadeDurationSlider.duration = file.fadeInOutDur;
		   
		   fadeDurationSlider.sliderHolder.slider({
		       min:1,
		       max:($('#jquery_jplayer_1').data().jPlayer.status.duration === 0 ? 1 :  5),
		       step:1,
		       value:4,
		       slide:function(event, ui){
		           
		           var songInfo;
		           
		           songInfo = $(this).parent().parent().data().songInfo;
		           
		           songInfo.fadeInOutDur = ui.value;
		
		           fadeDurationSlider.tooltip1.tooltip('option', 'content', ui.value.toString());
		           
		           fadeDurationSlider.tooltip1.focusin();
		           
		           updateSecondaryTrackList(songInfo);
		       },
		       stop:function(event, ui){
		           
		           fadeDurationSlider.tooltip1.focusout();
		       }
		   });
		   
		   contentBody.append(song);
		   
		   $.data(song[0], 'songInfo', file);
		   
		   if(downloadtype == 'downloadProgram'){ 		
			   
			   	//waiting = setInterval(function(){
	        	   
	        	  // if($('#jquery_jplayer_1').data().jPlayer.status.duration >0){
	        		   console.log(fileIndex);
	        		   //clearInterval(waiting);
	        		   setDownloadSecondaryTrackMark(fileIndex,secondaryTracks[fileIndex].StartingTime);
	        		   
	        	  // }
	           //}, 100);
			   			  
		   }
		   else if (downloadtype == 'downloadOneAudio'){ 
			
			   waiting = setInterval(function(){
	        	   
	        	   if(!$('#jquery_jplayer_3').data().isBusy){
	        		   clearInterval(waiting);
	        		   getSongMetadata(song);
	        	   }
	           }, 750);
		   }
		  	     	   
		   setSecondaryTooltips(fadePercentageSlider,  fadeDurationSlider);
		   
		  
		// });
		
		
}
function setDownloadSecondaryTrackMark(index,StartingTime)
{
	var mainPlayerStatus,
		secondaryTrackMenuContainer,
		activeTrack,
		proposedSong = {},
		positionInContainer,
		secondaryTrackTimeMark = {},
		mark;
	
	mainPlayerStatus = $('#jquery_jplayer_1').data().jPlayer.status;
	
	
	if(mainPlayerStatus.duration === 0){
		return;
	}
	
	
	secondaryTrackMenuContainer = $('#secondaryTrackMenuContainer');
	
	
	index +=1;
	activeTrack = $('.secondaryTrackRow:eq(' + index + ')');
	
	proposedSong.fileKey = activeTrack.data().songInfo.fileKey;
	proposedSong.start = StartingTime;
	proposedSong.end = proposedSong.start + activeTrack.data().songInfo.fileDuration;
	
	if(!checkForSecondaryTracksCollisions(proposedSong)){
		
		activeTrack.data().songInfo.start = proposedSong.start;
    	activeTrack.data().songInfo.end = proposedSong.end;
  	   
  	   	if(!updateSecondaryTrackList(activeTrack.data().songInfo)){
  	   		
  	   	   $("#jquery_jplayer_2").data().list.push(activeTrack.data().songInfo);
  		   
  		   mark = setSecondaryTrackMarkHelper(activeTrack);
  		   
  		   secondaryTrackTimeMark.fileKey = activeTrack.data().songInfo.fileKey;
  		   secondaryTrackTimeMark.start = activeTrack.data().songInfo.start;
  		   secondaryTrackTimeMark.end = activeTrack.data().songInfo.end;
  		   secondaryTrackTimeMark.mark = mark;
  		   
  		   $('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks.push(secondaryTrackTimeMark);
        }
  	   else{
  		   
  		   secondaryTrackTimeMark = getSecondaryTrackMark(activeTrack.data().songInfo.fileKey);
  		   
  		   if(secondaryTrackTimeMark !== null){
  		   
  			   positionInContainer = (activeTrack.data().songInfo.start * (secondaryTrackTimeMark.mark.parent().width() /*- secondaryTrackTimeMark.mark.width()*/))/mainPlayerStatus.duration;
  			   
  			   secondaryTrackTimeMark.mark.css('left', positionInContainer + 'px');
  		   }
  		   
  	   }
  	   
  	   $("#jquery_jplayer_2").data().list.sort(sortSecondaryTrackByStart);
    }
}
function setSlideShow()
{
	Galleria.loadTheme('/js/djInterface/UI/galleria/themes/classic/galleria.classic.min.js');
	Galleria.configure({transition:'fade', carousel:true});
    Galleria.run('#galleria');
    Galleria.ready(function(){
    	
    });
    
    $('#slidesSearcherDialog').dialog({
	      autoOpen: false,
	      show: {
	        effect: "slide",
	        duration: 1000
	      },
	      hide: {
	        effect: "drop",
	        duration: 1000
	      },
	      width:250,
	      height: 'auto',
	      minHeight: 100
	    });

    $('#slideSearcherBtn').button({icons:{primary:'ui-icon-circle-zoomin'}, text: false}).click(
		function(event){
			event.preventDefault();
			$('#slidesSearcherDialog').dialog('open');
		});
    $('#slideMarker').button({icons:{primary:'ui-icon-pin-s'}, text: false})
    				 .click(function(event){
    					 setSlideTimeMark(Galleria.get(0).getIndex());
    				 });
    
    $.data($('#slidesEditBtn')[0], 'edit', true);
    $('#slideMarker').button('enable');
    
    $('#slidesEditBtn').button({icons: {primary: 'ui-icon-unlocked'}, text : false})
    				   .click(function(event){
    					   
    					   if($(this).button('option','icons').primary === 'ui-icon-unlocked'){
    						   $(this).button('option', {icons: {primary: 'ui-icon-locked'}});
    						   $(this).tooltip('option', 'content', $(this).attr('unlock'));
    						   $.data(this, 'edit', false);
    						   $('#slideMarker').button('disable');
    					   }
    					   else {
    						   $(this).button('option', {icons: {primary: 'ui-icon-unlocked'}});
    						   $(this).tooltip('option', 'content', $(this).attr('lock'));
    						   $.data(this, 'edit', true);
    						   $('#slideMarker').button('enable');
    					   }
    					   
    					   checkSlideEditAction();
    				   })
    				   .attr('title', $('#slidesEditBtn').attr('lock'))
    				   .tooltip();
}

function buildSlideInfo(SlideMarks)
{
	var slide_num;
	var bFirstTime=true;
	
	 $.each(SlideMarks, function(index,n){
		 
		 if(SlideMarks[index].slideIndex === undefined){
			 
			 if(bFirstTime){
				 slide_num=0;
				 bFirstTime = false;
			 }
			 else
				 ++slide_num;
		 }			 
		  
		 $.each(SlideMarks, function(i,n){
			 
			 if(SlideMarks[i].slideIndex === undefined){
				 if(SlideMarks[i].fileKey === SlideMarks[index].fileKey)
					 SlideMarks[i].slideIndex = slide_num;
			 }		 
		 });
		 
	 });
	 		
}
function checkRepDownload(slideMarkIndex, SlideMarks){
	
	var Rep= false;
	
	$.each(SlideMarks, function(i,n){
		 
		if(slideMarkIndex > i){
					
			 if(SlideMarks[slideMarkIndex].slideIndex === SlideMarks[i].slideIndex){				 
					 Rep = true;				 
					 return;				 		
			 }
		}
	 });
	
	if(Rep)		
		return true;
	else
		return false;
}

function setPublicMusicInfo(){
	
	var public_music_info=[];
	
	$( "#puclic_music_lib" ).accordion({ heightStyle: "content", collapsible: true, active: false });
	
		$.ajax({
		url: '/rest/musicLibrary',
			data: {},
			async: false,
			cache: false,
			dataType: 'json',
			type: 'GET',
			success:function(json){
						
				$('.public_music_file').remove();
				
				$.each(json, function(i,n){	 									
					
					var item = json[i];
					$( "#puclic_music_lib_l1").append('<p><a href="#" class=public_music_file>' + removeExt(item.musicFileTitle) + '</a></p>');
									
					public_music_info[i] = {};
					public_music_info[i].musicKey = item.key;
					public_music_info[i].fileName = item.musicFileTitle;
					public_music_info[i].fileKey = item.musicFileFile.keyString;					
					public_music_info[i].fileFormat = item.musicFileFormat;
					
				});
				
				$("#puclic_music_lib" ).accordion("refresh" );
			}
		});
		
		$('.public_music_file').click(function(){
			
			var active = $('.public_music_file').index(this),
				selectedSong={};
			
			if(active === false){
				return;
			}
									
			selectedSong = GetAudioNameFormat('MUSIC_FILE', public_music_info[active].musicKey);								
			selectedSong.fileKey = public_music_info[active].fileKey;
			selectedSong.mainTrackType =  'MUSIC_FILE';
 			selectedSong.datastoreObjectKey = public_music_info[active].musicKey;
		
			$.data($('#jquery_jplayer_1')[0], 'songInfo', selectedSong);
			controlMainPlayer(selectedSong);
						
		});
}	
function setStationSlideInfo()
{
	var image_info=[];
	
	$( "#accordion_slide" ).accordion({ heightStyle: "content", collapsible: true, active: false });
	
	$.ajax({
		url: '/rest/stationImages/s=' + $('#header').attr('stationkey'),
			data: {},
			async: false,
			cache: false,
			dataType: 'json',
			type: 'GET',
			success:function(json){
				
				$('.station_slide_file').remove();
				$.data($('#stationContainer')[0], 'stationSlideInfo', []);
						
				$.each(json, function(i,n){	 
					
					var item = json[i];
					$( "#accordion_slide_l1").append('<p><a href="#" class=station_slide_file>' + removeExt (item.stationImageName) + '</a></p>');		
					
					image_info[i] = {};
					image_info[i].datastoreObjectKey = item.stationImageKey;
					image_info[i].fileName = item.stationImageName;
					image_info[i].fileKey = item.stationImageMultimediaContent.keyString;
					
					$('#stationContainer').data().stationSlideInfo.push(image_info[i]);
			  });
				
				$("#accordion_slide" ).accordion("refresh" );
			}
		});
	
	
	$('.station_slide_file').click(function(){
		
		var active = $('.station_slide_file').index(this),
			selectedImage;			
					
		if(Galleria.get(0).getDataLength() === 0){
			Galleria.get(0).load({image: '/img?blobkey=' + image_info[active].fileKey, slide: image_info[active]});
		}
		else {
			Galleria.get(0).push({image: '/img?blobkey=' + image_info[active].fileKey, slide: image_info[active]});
		}				

	});
	
}
function setStationMusicInfo()
{	
	var audio_music_info=[];
		
	$( "#accordion_station_music" ).accordion({ heightStyle: "content", collapsible: true, active: false });
	var audio_music_index=0;
	
	$.ajax({
		url: '/rest/stationAudio/s=' + $('#header').attr('stationkey'),
			data: {},
			async: false,
			cache: false,
			dataType: 'json',
			type: 'GET',
			success:function(json){
				
				$('.station_music_file').remove();
				$.data($('#stationContainer')[0], 'stationMusicInfo', []);
						
				$.each(json, function(i,n){	 
					
					var item = json[i];
											
					if(item.stationAudioType === 'Music'){						
													
					$( "#accordion_station_music_l1").append('<p><a href="#" class=station_music_file>' +  removeExt(item.stationAudioName) + '</a></p>');													
											
					audio_music_info[audio_music_index] = {};
					audio_music_info[audio_music_index].stationAudioKey = item.stationAudioKey;
					audio_music_info[audio_music_index].fileName = item.stationAudioName;
					audio_music_info[audio_music_index].fileKey = item.stationAudioMultimediaContent.keyString;
					audio_music_info[audio_music_index].fileType = item.stationAudioType;
					audio_music_info[audio_music_index].fileFormat = item.stationAudioFormat;
					
					$('#stationContainer').data().stationMusicInfo.push(audio_music_info[audio_music_index]);
					
					++audio_music_index;
					}	
				});
				
				$("#accordion_station_music" ).accordion("refresh" );
			}
		});
	
	
	$('.station_music_file').click(function(){
		
		var active = $('.station_music_file').index(this),
			selectedSong;
		
		if(active === false){
			return;
		}				
										
			selectedSong = GetAudioNameFormat('FILE_UPLOAD', audio_music_info[active].stationAudioKey);
			selectedSong.fileKey = audio_music_info[active].fileKey;
			selectedSong.mainTrackType =  'FILE_UPLOAD';
 			selectedSong.datastoreObjectKey = audio_music_info[active].stationAudioKey;
		
			$.data($('#jquery_jplayer_1')[0], 'songInfo', selectedSong);
													
			controlMainPlayer(selectedSong);								
	});
			
}
function setStationVoiceInfo()
{
	var audio_voice_info=[];
	
	$( "#accordion_station_voice" ).accordion({ heightStyle: "content", collapsible: true, active: false });
	var audio_voice_index=0;
	
	$.ajax({
		url: '/rest/stationAudio/s=' + $('#header').attr('stationkey'),
			data: {},
			async: false,
			cache: false,
			dataType: 'json',
			type: 'GET',
			success:function(json){
				
				$('.station_voice_file').remove();
				$.data($('#stationContainer')[0], 'stationVoiceInfo', []);
						
				$.each(json, function(i,n){	 
					
					var item = json[i];
					
					//temporarily fulfill the request, need to be changed
					if(item.stationAudioType === 'Voice'){						
						
						$( "#accordion_station_voice_l1").append('<p><a href="#" class=station_voice_file>'  + removeExt(item.stationAudioName) + '</a></p>');																		
						audio_voice_info[audio_voice_index] = {};
						audio_voice_info[audio_voice_index].stationAudioKey = item.stationAudioKey;
						audio_voice_info[audio_voice_index].fileName = item.stationAudioName;
						audio_voice_info[audio_voice_index].fileKey = item.stationAudioMultimediaContent.keyString;
						audio_voice_info[audio_voice_index].fileType = item.stationAudioType;
						audio_voice_info[audio_voice_index].fileFormat = item.stationAudioFormat;
						
						$('#stationContainer').data().stationVoiceInfo.push(audio_voice_info[audio_voice_index]);
						
						++audio_voice_index;
					}

			  });
				
				$("#accordion_station_voice" ).accordion("refresh" );
			}
		});
	
	
	$('.station_voice_file').click(function(event){
		
		var active = $('.station_voice_file').index(this),
			selectedSong;
		
		if(active === false){
			return;
		}
						
		var dummy_array =[];			
							
		selectedSong = GetAudioNameFormat('FILE_UPLOAD', audio_voice_info[active].stationAudioKey);
		selectedSong.fileKey = audio_voice_info[active].fileKey;
		selectedSong.secondaryTrackType =  'FILE_UPLOAD';
 		selectedSong.datastoreObjectKey = audio_voice_info[active].stationAudioKey;					
								
		DownloadSecondTrackFile('downloadOneAudio',0,dummy_array,selectedSong);
		enableSecondaryTrackControls(selectedSong);						
	});	
		    
}
function setStationChannelInfo(channel_info)
{
	$( "#accordion_channel" ).accordion({ heightStyle: "content", collapsible: true,  active: false});
	
	$.ajax({
		url: '/rest/stationProfile/s=' + $('#header').attr('stationkey'),
			data: {},
			async:false,
			cache: false,
			dataType: 'json',
			type: 'GET',
			success:function(json){
				$.data($('#stationContainer')[0], 'channelInfo', []);
				
				$('.channel_menu').remove();
				
				$.each(json.channels, function(i,n){	 
					
					var item = json.channels[i];
					$( "#accordion_channel").append('<h3 class=channel_menu>' + item.channelName + '</h3>');
					$( "#accordion_channel").append('<div id=channel' + item.channelNumber + ' class=channel_menu>'+'</div>');
					
					channel_info[i] = {};
					channel_info[i].id = 'channel' + item.channelNumber;
					channel_info[i].key = item.key;
					
					$('#stationContainer').data().channelInfo.push(channel_info[i]);

			  });

			    $( "#accordion_channel" ).accordion( "refresh"); 
			    		
			}
		});

}
function setProgramInfo(active,channel_info,options,ajaxLoadCircle1)
{
	var prog_item;	
	var program_obj_info=[];
	
			   						      			      			     			      																																											   																														
			 $.ajax({
				    url: '/rest/programs/c=' + channel_info[active].key,				    
				    async: false,
					data: {},
					cache: true,
					dataType: 'json',
					type: 'GET',
					
					success:function(json){								
						
					$('#'+channel_info[active].id + ' p').remove();
													
					json.sort(sortProgramkBySequenceNumber);						//sort by program sequence number
					
					$.data($('#'+channel_info[active].id)[0], 'program_info', []);			//for Dago's upload
					$.data($('#'+channel_info[active].id)[0], 'program_object_info', []);										
					
					$.each(json, function(i,n){	 
						  prog_item = json[i];
						  
						  $('#'+channel_info[active].id).append('<p><a href="#">' + prog_item.programName + '</a></p>');						  
						  $('#'+channel_info[active].id + ' p').hide();
						  
						  program_obj_info[i] = {};									
						  program_obj_info[i].programName = prog_item.programName;
						  program_obj_info[i].programBanner = prog_item.programBanner;
						  program_obj_info[i].programTotalDurationTime = prog_item.programTotalDurationTime;
						  program_obj_info[i].programKey = prog_item.programKey;
						  
						  //fetch the maintrack info
						  program_obj_info[i].mainTrackType = prog_item.mainTrack.mainTrackType;						  
						  program_obj_info[i].mainTrackMusicFileKey =prog_item.mainTrack.mainTrackMusicFileKey;
						  program_obj_info[i].mainTrackMusicFileBlobKey =prog_item.mainTrack.mainTrackMusicFileBlobKey.keyString;
						  program_obj_info[i].mainTrackStationAudio = prog_item.mainTrack.stationAudio;
						  program_obj_info[i].mainTrackStationAudioFileKey = prog_item.mainTrack.stationAudioBlobKey.keyString;								  
						  program_obj_info[i].mainTrackDuration = prog_item.mainTrack.mainTrackDuration;
						  
						  //fetch the slides mark info
						  var program_info_index = i;
						  									 									  
						  program_obj_info[i].slides = [];								  
						  $.each(prog_item.slides, function(i,n){
							  
							  program_obj_info[program_info_index].slides[i] = {};
							  program_obj_info[program_info_index].slides[i].slideKey = prog_item.slides[i].slideKey;
							  program_obj_info[program_info_index].slides[i].datastoreObjectKey = prog_item.slides[i].stationImage;
							  program_obj_info[program_info_index].slides[i].fileKey = prog_item.slides[i].stationImageBlobKey.keyString;
							  program_obj_info[program_info_index].slides[i].startingTime = prog_item.slides[i].slideStartingTime;										  
						  });
						  
						  //build the real slide info
						  buildSlideInfo(program_obj_info[program_info_index].slides);
						  		
						//fetch the 2nd track mark info
						  program_obj_info[i].secondaryTracks = [];
						  $.each(prog_item.secondaryTracks, function(i,n){
							  
							  program_obj_info[program_info_index].secondaryTracks[i] = {};									 									  
							  program_obj_info[program_info_index].secondaryTracks[i].Type = 					prog_item.secondaryTracks[i].secondaryTrackType;	
							  program_obj_info[program_info_index].secondaryTracks[i].SecondaryTrackKey =		prog_item.secondaryTracks[i].secondaryTrackKey;
							  program_obj_info[program_info_index].secondaryTracks[i].MusicFileKey =			prog_item.secondaryTracks[i].secondaryTrackMusicFileKey;
							  program_obj_info[program_info_index].secondaryTracks[i].MusicFileBlobKey =		prog_item.secondaryTracks[i].secondaryTracMusicFileBlobKey.keyString;								  													   
							  program_obj_info[program_info_index].secondaryTracks[i].StationAudio = 			prog_item.secondaryTracks[i].stationAudio;
							  program_obj_info[program_info_index].secondaryTracks[i].StationAudioFileKey = 	prog_item.secondaryTracks[i].stationAudioBlobKey.keyString;
							  program_obj_info[program_info_index].secondaryTracks[i].StartingTime = 			prog_item.secondaryTracks[i].secondaryTrackStartingTime;					  
							  program_obj_info[program_info_index].secondaryTracks[i].FadeOutPct = 				prog_item.secondaryTracks[i].secondaryTrackFadeOutPercentage;
							  program_obj_info[program_info_index].secondaryTracks[i].Duration =				prog_item.secondaryTracks[i].secondaryTrackDuration;
						  });										  									 
						  
						  $('#'+channel_info[active].id).data().program_info.push(json[program_info_index]); 				//for Dago's upload
						  $('#'+channel_info[active].id).data().program_object_info.push(program_obj_info[program_info_index]);
						  						  						 
					  });
													
				    $("#accordion_channel").accordion("refresh");	
				    							  
					}	
													
				}).always(function(){
				
					if (ajaxLoadCircle1) 
						ajaxLoadCircle1.remove();
					
						$('#'+channel_info[active].id).css('height',40);
						
						$.data($('#accordion_channel')[0], 'loaderRemoved', true);
											
					});
			 
}
function setDownloadMenu()
{
	var public_music_info  = [];		
	var audio_music_info  = [];
	var audio_voice_info  = [];
	var image_info  = [];
	var channel_info= [];
	var program_obj_info= [];
	
	var waiting;
	var waiting_for_clear_secondtrack_mark;		
	var waiting_clear_slides;
	var waiting_for_secondtrack_mark;
	var waiting_for_all_done;
	
	
	var finish_load_images=false;
	var finish_load_secondtrack=false;
	var finish_load_slidetimemark=false;
	var finish_load_secondtimemark=false;
	
	var slides_first_download=true;
	var finish_clear_slide_mark=false;
	var second_track_first_download=true;
	var finish_clear_secondtrack_mark=false;
	
	var ajaxLoadCircle1,ajaxLoadCircle2;
	
	var options = {
			bgColor 		: '#fff',
			duration		: 400,
			opacity			: 0.7,
			classOveride 	: false
		}
	
	$.data($('#stationContainer')[0], 'waiting_options', options);
			
		setPublicMusicInfo();
		
		setStationSlideInfo();
	
		setStationMusicInfo();		
		
		setStationVoiceInfo();			
				
		
		setStationChannelInfo(channel_info);	    
		
																		    	
			$("#accordion_channel").click( function(){  
										
				var active = $("#accordion_channel").accordion('option', 'active');					
				var prog_item;				
			   	
				if(active === false){
					return;
				}
								
				// Show the waiting animation in the accordion
				$('#'+channel_info[active].id).css('height',40);	//the width and height of the icon are both 31
				$('#'+channel_info[active].id + ' p').hide();
				ajaxLoadCircle1 = new ajaxLoader($('#'+channel_info[active].id),options,'1','relative');
				$("#accordion_channel").accordion("refresh");
												
				var waiting_animation;
				var wait_200_msec=false;
				$.data($('#accordion_channel')[0], 'loaderRemoved', false);	
					
				waiting_animation = setInterval(function(){
					
					if(wait_200_msec)
			 		{
						
						clearInterval(waiting_animation);
						
						setProgramInfo(active,channel_info,options,ajaxLoadCircle1);											 	
						 
						//**** Click one program 
						 $('#'+channel_info[active].id + ' p').click(function(event){
							 
							 var finish_secondtrack_mark=false;
							 var finish_slides_mark=false;
							 var total_num_slides=-1;
							 var slide_count=0;
							 
							 //$.data($('#slidesTimeMarkContainer')[0], 'finishSet', false);
							 $.data($('#slidesTimeMarkContainer')[0], 'finishSet', true);
							 $.data($('#secondaryTrackTimeMarkContainer')[0], 'finishSet', false);							 
							 
							 finish_clear_slide_mark=false;
							 finish_clear_secondtrack_mark=false;
							 
							 finish_load_images=false;
							 finish_load_secondtrack=false;
							 finish_load_slidetimemark=false;
							 finish_load_secondtimemark=false;
							 							 							 							 														 						
							 							 								   							 	
							 var active_program_index = $('#'+channel_info[active].id + ' p').index(this);							 							
							 var selectedSong;
							 var extFileName;
							 
							if(active_program_index === false){
								return;
							}		
							
							ajaxLoadCircle2 = new ajaxLoader($("#accordion_channel").parent().parent(),options,'2','absolute');
							
							
							if($(this).css('background-color') === 'rgba(0, 0, 0, 0)'){
				        		   
				        		   $(this).parent().children().css('background-color', 'rgba(0, 0, 0, 0)');				        		  
				        		   $(this).animate({backgroundColor: '#019be3'});									
				        	   }				        	   
							
							
							//set the program_obj_info, for it can be created by program download and changed by the save function, updateAfterUpload()
							program_obj_info = $('#'+channel_info[active].id).data().program_object_info;
							
							//fetch the banner info							
							$('#programBanner').val(program_obj_info[active_program_index].programBanner);
							
							//fetch the main track file
							$.data($('#'+channel_info[active].id)[0], 'active_program', $('#'+channel_info[active].id).data().program_info[active_program_index]);
							$.data($('#'+channel_info[active].id)[0], 'active_program_index', active_program_index);
							setFacebookShare();
							
							$('#secondaryTrackTimeMarkContainer span').remove();
						 	$('#secondaryTrackMenuHeader').siblings().remove();
						 	$.data($('#secondaryTrackTimeMarkContainer')[0], 'secondaryTrackTimeMarks', []);
							$.data($('#jquery_jplayer_2')[0], 'list', []);
							$.data($('#jquery_jplayer_2')[0],'timeout', false);
							
						 	finish_clear_secondtrack_mark=true;
							
							 		if(program_obj_info[active_program_index].mainTrackType == 'FILE_UPLOAD'){
							 			
							 			selectedSong = GetAudioNameFormat(program_obj_info[active_program_index].mainTrackType,program_obj_info[active_program_index].mainTrackStationAudio);							 			
							 			selectedSong.fileKey = program_obj_info[active_program_index].mainTrackStationAudioFileKey;
							 			selectedSong.mainTrackType = program_obj_info[active_program_index].mainTrackType;
							 			selectedSong.datastoreObjectKey = program_obj_info[active_program_index].mainTrackStationAudio;
							 			
						 			}
							 		else if(program_obj_info[active_program_index].mainTrackType == 'MUSIC_FILE'){
							 			
							 			selectedSong = GetAudioNameFormat(program_obj_info[active_program_index].mainTrackType,program_obj_info[active_program_index].mainTrackMusicFileKey);
							 			//selectedSong = getTrackInfo('dummy'+'.mp3');   
							 			selectedSong.fileKey = program_obj_info[active_program_index].mainTrackMusicFileBlobKey;
							 			
							 			selectedSong.mainTrackType = program_obj_info[active_program_index].mainTrackType;
							 			selectedSong.datastoreObjectKey = program_obj_info[active_program_index].mainTrackMusicFileKey;
							 		}
							 		$.data($('#jquery_jplayer_1')[0], 'songInfo', selectedSong);	
									controlMainPlayer(selectedSong);
																								 						
								//fetch the slides
								if(Galleria.get(0).getDataLength()!== 0){
									//Galleria.get(0).splice(0,Galleria.get(0).getDataLength());
									Galleria.get(0).destroy();
									Galleria.run('#galleria');
								}	
																																																											
								/*																		
								$('#slidesTimeMarkContainer').load(location.href  + " #refreshSlidesTimeMarkContainer", function() {											
										
									if(slides_first_download)
										finish_clear_slide_mark=true;	
									
																			
									if($('#slidesTimeMarkContainer').data().slidesTimeMarks!=null)
									{
										
										$.data($('#slidesTimeMarkContainer')[0], 'slidesTimeMarks', []);
										
										slides_first_download = false;
										finish_clear_slide_mark=true;																						
									}
								});*/
								
								$('#slidesTimeMarkContainer span').remove();
								$.data($('#slidesTimeMarkContainer')[0], 'slidesTimeMarks', []);
								
								waiting_clear_slides = setInterval(function(){
									
									if(Galleria.get(0).getDataLength()===0){
											
										clearInterval(waiting_clear_slides);	
																																								
										$.each(program_obj_info[active_program_index].slides, function(i,n){	
	 								 		
										 	if(Galleria.get(0).getDataLength() === 0){
										 		slide_count=1;
												Galleria.get(0).load({image: '/img?blobkey=' + program_obj_info[active_program_index].slides[i].fileKey, slide: program_obj_info[active_program_index].slides[i]});											
											}
											else {
												
												var Rep = checkRepDownload(i ,program_obj_info[active_program_index].slides);
												
												if(!Rep){
													slide_count +=1;
													Galleria.get(0).push({image: '/img?blobkey=' + program_obj_info[active_program_index].slides[i].fileKey, slide: program_obj_info[active_program_index].slides[i]});
												}
											}
										 	/*
										 	setDownloadSlideTimeMark2(program_obj_info[active_program_index].slides[i],
										 					          program_obj_info[active_program_index].programTotalDurationTime);
										 					          */
										 	setDownloadSlideTimeMark2(program_obj_info[active_program_index].slides[i],
										 			program_obj_info[active_program_index].mainTrackDuration);
										 	
										});
										
										
										$('#slidesEditBtn').button('option', {icons: {primary: 'ui-icon-locked'}});
			    						 $('#slidesEditBtn').tooltip('option', 'content', $('#slidesEditBtn[0]').attr('unlock'));					    						   
			    						 $.data($('#slidesEditBtn')[0], 'edit', false);
			    						 checkSlideEditAction();
										//total_num_slides = slide_count;
									}
								}, 100);	
								
								 
	    						 
								/*
								var wait_1_sec=false;
								
								Galleria.get(0).bind('image', function(e) {									
									if(Galleria.get(0).getDataLength() === total_num_slides)
										finish_load_images = true;											 	
								});
							 								 								 
							 															 								 	
							 	waiting = setInterval(function(){	
							 		
							 		if(program_obj_info[active_program_index].slides.length===0){
							 			clearInterval(waiting);
							 			$.data($('#slidesTimeMarkContainer')[0], 'finishSet', true);
							 		}							 									 	
									
									if(wait_1_sec & finish_clear_slide_mark)
									{									
										clearInterval(waiting);										
																										
										$.each(program_obj_info[active_program_index].slides, function(i,n){
									 		setDownloadSlideTimeMark(i, program_obj_info[active_program_index].slides);
									 											 		 											 
									 	});																				
													
												   $.data($('#slidesTimeMarkContainer')[0], 'finishSet', true);
										
					    						   $('#slidesEditBtn').button('option', {icons: {primary: 'ui-icon-locked'}});
					    						   $('#slidesEditBtn').tooltip('option', 'content', $('#slidesEditBtn[0]').attr('unlock'));					    						   
					    						   $.data($('#slidesEditBtn')[0], 'edit', false);
					    						   checkSlideEditAction();
									}
									
									if(finish_load_images)
										wait_1_sec=true;
										
								}, 1000);	
							 	
								*/							 								 
							 	//fetch the 2nd-track files	
							 	stopSecondaryPlayer();
							 								 									 								 							 	
							 	/*$('#secondaryTrackTimeMarkContainer').load(location.href  + " #refreshSecondaryTrackTimeMarkContainer", function() {																											 	
							 		
							 		if(second_track_first_download)
										finish_clear_secondtrack_mark=true;																											
									
									if($('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks!=null)
									{
										
										$.data($('#secondaryTrackTimeMarkContainer')[0], 'secondaryTrackTimeMarks', []);
										$.data($('#jquery_jplayer_2')[0], 'list', []);
										
										second_track_first_download = false;
										finish_clear_secondtrack_mark=true;																			
									}
							 	});*/
							 	
							 	/*$('#secondaryTrackTimeMarkContainer span').remove();
							 	$('#secondaryTrackMenuHeader').siblings().remove();
							 	$.data($('#secondaryTrackTimeMarkContainer')[0], 'secondaryTrackTimeMarks', []);
								$.data($('#jquery_jplayer_2')[0], 'list', []);
								
							 	finish_clear_secondtrack_mark=true;*/
							 
							 	
							 	
							 	//waiting_for_clear_secondtrack_mark= setInterval(function(){
							 			
							 		if(program_obj_info[active_program_index].secondaryTracks.length===0){
							 			$.data($('#secondaryTrackTimeMarkContainer')[0], 'finishSet', true);
							 		}
																		
							 		/*if(finish_clear_secondtrack_mark)
									{									
										clearInterval(waiting_for_clear_secondtrack_mark);
																				   
							 	*/
											 	//$('#secondaryTrackMenuContainer').load(location.href  + " #secondaryTrackMenuHeader", function() {							 									 	
											 		
											 	var waitForDurationOfMainTrack = setInterval(function(){
											 			
											 			if($('#jquery_jplayer_1').data().jPlayer.status.duration > 0){
											 				
											 				clearInterval(waitForDurationOfMainTrack);
											 				
											 				$.each(program_obj_info[active_program_index].secondaryTracks, function(i,n){	
												 														 														 		
											 					var selectSong={};	//no use						 		
											 					DownloadSecondTrackFile('downloadProgram',i,program_obj_info[active_program_index].secondaryTracks,selectSong);
											 					enableSecondaryTrackControls(selectSong);									
											 				});	
											 		}
											 
											 	}, 100);
									//}
							 //	}, 100);	
							 		
							 		var wait_2_sec=false;
							 									 		
							 		waiting_for_secondtrack_mark = setInterval(function(){
							 										 										 										 										 	
							 			if(wait_2_sec)
								 		{	
								 			clearInterval(waiting_for_secondtrack_mark);
								 			
									 		$.data($('#secondaryTrackTimeMarkContainer')[0], 'finishSet', true);
											
											$('#secondaryTrackEditBtn').button('option', {icons: {primary: 'ui-icon-locked'}});
											$('#secondaryTrackEditBtn').tooltip('option', 'content', $('#secondaryTrackEditBtn').attr('unlock'));					    						   
											$.data($('#secondaryTrackEditBtn')[0], 'edit', false);											
											checkSecondaryTrackEditAction();	
								 		}
							 			
							 			//Two seconds after marking the second-track, we make it uneditable 
							 			if(typeof  $('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks !== 'undefined'){
						 					console.log('length->', $('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks.length);
							 				if($('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks.length === program_obj_info[active_program_index].secondaryTracks.length)
						 						wait_2_sec=true;
							 			}
						 			
							 		},2000);
							 		
							 		var sec=60;
							 									 								 									 								 	
							 		waiting_for_all_done= setInterval(function(){
							 										 			
							 			--sec;										
								 		if(($('#slidesTimeMarkContainer').data().finishSet && $('#secondaryTrackTimeMarkContainer').data().finishSet) || sec === -1)
										{									
											clearInterval(waiting_for_all_done);
																							
											if (ajaxLoadCircle2) 
												ajaxLoadCircle2.remove();																						
										}								 										 		
								 										 		
								 	}, 1000);								 								 								 								 								 																	 							 								 						 							 				 							 								 					 								 							 							 											 
							 		
							 	event.stopPropagation();
							 	
						 });	 	//the end of clicking one program
						 
						 			 	
				 	} // if wait_200_msec					
					wait_200_msec = true;
							
				},200);	 //show the waiting animation first	
				
			
			//remove the waiting animation in the accordion
			var waiting_removed;
			var wait_1000_msec=false;
			 		
			waiting_removed = setInterval(function(){
			 										 										 										 										 	
				if($('#accordion_channel').data().loaderRemoved & wait_1000_msec)
				{	
					 			clearInterval(waiting_removed);
					 						 						 						 						 
					 			$('#'+channel_info[active].id).css('height','auto');
					 			$('#'+channel_info[active].id + ' p').show();
					 			
					 			$("#accordion_channel").accordion("refresh");
				}			 					 		
			wait_1000_msec=true;	
			 			
			},1000);
				
			
		});						//accordion click

}

function setMainTrackMenu()
{
	$('#songSearcherOptions input').change(function(){
		if($(this).is(':checked'))
		{
			songSearcherOptionsHandler($(this).attr('data'));
		}
	});
	
	$('#songSearcherOptions').buttonset();
	
	$('#songSearcherDialog').dialog({
      autoOpen: false,
      show: {
        effect: "slide",
        duration: 1000
      },
      hide: {
        effect: "drop",
        duration: 1000
      },
	  width:350
    });
	
	$('#songSearcherBtn').button({icons:{primary:'ui-icon-circle-zoomin'}, text: false}).click(
	function(event){
		event.preventDefault();
		$('#songSearcherDialog').dialog('open');
	});
	
	$('#jp_container_1 li:gt(0)').tooltip();
}
function setSecondaryTrackMenu()
{
    $('#secondarySearcherDialog').dialog({
      autoOpen: false,
      show: {
        effect: "slide",
        duration: 1000
      },
      hide: {
        effect: "drop",
        duration: 1000
      },
      width:270,
      height: 'auto',
      minHeight: 100
    });
    
    $('#secondarySearcherBtn').button({icons:{primary:'ui-icon-circle-zoomin'}, text: false}).click(
    function(event){
		event.preventDefault();
		$('#secondarySearcherDialog').dialog('open');
	});
    
    $('#secondaryTrackMarker').button({icons:{primary:'ui-icon-lightbulb'}, text: false})
    						  .click(function(event){
    							  setSecondaryTrackMark();
    						  });
    
    $.data($('#secondaryTrackEditBtn')[0], 'edit', true);
    $('#secondaryTrackMarker').button('enable');
    
    $('#secondaryTrackEditBtn').button({icons: {primary: 'ui-icon-unlocked'}, text : false})
	   .click(function(event){
		   
		   if($(this).button('option','icons').primary === 'ui-icon-unlocked'){
			   $(this).button('option', {icons: {primary: 'ui-icon-locked'}});
			   $(this).tooltip('option', 'content', $(this).attr('unlock'));
			   $.data(this, 'edit', false);
			   $('#secondaryTrackMarker').button('disable');
		   }
		   else {
			   $(this).button('option', {icons: {primary: 'ui-icon-unlocked'}});
			   $(this).tooltip('option', 'content', $(this).attr('lock'));
			   $.data(this, 'edit', true);
			   $('#secondaryTrackMarker').button('enable');
		   }
		   
		   checkSecondaryTrackEditAction();
		   
	   }).attr('title', $('#secondaryTrackEditBtn').attr('lock'))
	     .tooltip();
}
function checkSlideEditAction()
{
	if($('#slidesEditBtn').data().edit === false){
		$('#slidesTimeMarkContainer .slideTimeMark').draggable('disable');
		$('#slideMarker').button('disable');
	}
	else{
		$('#slidesTimeMarkContainer .slideTimeMark').draggable('enable');
		$('#slideMarker').button('enable');
	}
}
function checkSecondaryTrackEditAction()
{
	if($('#secondaryTrackEditBtn').data().edit === false){
		$('#secondaryTrackTimeMarkContainer .secondaryTrackTimeMark').draggable('disable');
		$('#secondaryTrackMarker').button('disable');
	}
	else{
		$('#secondaryTrackTimeMarkContainer .secondaryTrackTimeMark').draggable('enable');
		$('#secondaryTrackMarker').button('enable');
	}
}
function setUploader()
{
    //$('#personalSongBtn').button({icons:{primary:'ui-icon-circle-arrow-n'}});
    $('#selectFilesBtn').button({icons:{primary:'ui-icon-circle-plus'}});
    
    setUploaderHelper();
        
}
function setUploaderHelper()
{
    var progressbar = $('#mainTrackProgressbar'),
        fileCount = 0,
        mainPlayer,
        fileType = /(\.|\/)(mp3)$/i;
    
    $('#personalSongBrowser').fileupload({
        dataType: 'json',
        sequentialUploads: true,
        progressall: function (e, data) {
             var progress = parseInt(data.loaded / data.total * 100, 10);
             
             progressbar.progressbar({
                value: progress
             });
             
             
             if(progress === 100)
             {
                 progressbar.progressbar('destroy');
             }
        },
        add: function(e, data){
        	
        	if(!fileType.test(data.files[0].name)){
				
				$('#mainTrackFileTypeError').show();
				return false;
			}
			else{
				$('#mainTrackFileTypeError').hide();
			}
        	
            	$.ajax({
    				url: '/station/createUploadUrl.jsp?url='+
    					 encodeURIComponent('/binaryFileUpload?action=upload&type=audio_music'),
    				cache: false,
    				dataType: 'json',
    				type: 'GET',
    				success:	function(result){
    					$('#personalSongBrowser').data().blueimpFileupload.options.url = result.uploadUrl; 
    					data.submit();
    				}});
        },
        done: function (e, data) {
            
            var template = $('#mainTrackTemplate ul').html(),
                song,
                file,
                tmp;
                      
            file = data.result;
            file.mainTrackType = 'FILE_UPLOAD';
               
            loadMainTrack(file);
               
            fileCount++;
           
            if(fileCount === data.originalFiles.length)
            {
                $('#personalSongBrowser').fileupload('destroy');
                $('#personalSongBrowser').unbind();
                setUploaderHelper();
                
                setStationMusicInfo();
            }
        }
    });
    
}
function setSliderUploader()
{
    $('#selectSlidesFilesBtn').button({icons:{primary:'ui-icon-circle-plus'}});
    
    setSliderUploaderHelper();
    
}
function setSliderUploaderHelper()
{
	var progressbar = $('#slidesProgressbar'),
    fileCount = 0,
    fileType = /(\.|\/)(gif|jpe?g|png)$/i;
	
	$('#slidesBrowser').fileupload({
		dataType:'json',		
		sequentialUploads: true,
		progressall: function(e, data){
			var progress = parseInt(data.loaded / data.total * 100, 10);
			
			progressbar.progressbar({
				value: progress
			});
			
			if(progress === 100){
				progressbar.progressbar('destroy');
			}
		},
		add: function(e, data){
			//$('#slidesBtn').click(function(){
			
				if(!fileType.test(data.files[0].name)){
					
					$('#slideFileTypeError').show();
					return false;
				}
				else{
					$('#slideFileTypeError').hide();
				}
				
				$.ajax({
				url: '/station/createUploadUrl.jsp?url='+
					 encodeURIComponent('/binaryFileUpload?action=upload&type=image'),
				cache: false,
				dataType: 'json',
				type: 'GET',
				success:	function(result){
					$('#slidesBrowser').data().blueimpFileupload.options.url = result.uploadUrl; 
					data.submit();
				}});
				
			//});
		},
		done: function(e, data){
			
			if(Galleria.get(0).getDataLength() === 0){
				Galleria.get(0).load({image: '/img?blobkey=' + data.result.fileKey, slide: data.result});
			}
			else {
				Galleria.get(0).push({image: '/img?blobkey=' + data.result.fileKey, slide: data.result});
			}
			
			fileCount++;
			
			if(fileCount === data.originalFiles.length){
				$('#slidesBrowser').fileupload('destroy');
				//$('#slidesBtn').unbind();
				$('#slidesBrowser').unbind();
				
				setSliderUploaderHelper();
				
				setStationSlideInfo();
			}
		}
	});
	
}
function setSecondaryUploader()
{
    $('#selectSecondaryFilesBtn').button({icons:{primary:'ui-icon-circle-plus'}});
    
    setSecondaryUploaderHelper();
    
}
function setSecondaryUploaderHelper()
{
    var contentBody =  $('#secondaryTrackMenuContainer'),
        progressbar = $('#secondaryTrackProgressbar'),
        fileCount = 0,
        fileType = /(\.|\/)(mp3)$/i;
        
     $('#secondaryTrackBrowser').fileupload({
        dataType: 'json',
        sequentialUploads: true,
        progressall: function (e, data) {
             var progress = parseInt(data.loaded / data.total * 100, 10);
             
             progressbar.progressbar({
                value: progress
             });
             
             
             if(progress === 100)
             {
                 progressbar.progressbar('destroy');
             }
        },
        add: function(e, data){ 
        	
        	if(!fileType.test(data.files[0].name)){
				
				$('#secondaryTrackFileTypeError').show();
				return false;
			}
			else{
				$('#secondaryTrackFileTypeError').hide();
			}
        	
            	$.ajax({
    				url: '/station/createUploadUrl.jsp?url='+
    					 encodeURIComponent('/binaryFileUpload?action=upload&type=audio_voice')+'&ts=' + new Date().getTime(),
    				cache: false,
    				dataType: 'json',
    				type: 'GET',
    				success:	function(result){
    					$('#secondaryTrackBrowser').data().blueimpFileupload.options.url = result.uploadUrl; 
    					data.submit();
    				}});
          
        },
        done: function (e, data) {
            
            var template = $('#secondaryTrackTemplate').html(),
                info,
                song,
                file = {},
                positionSlider = {},
                fadePercentageSlider = {},
                fadeStepSlider = {},
                fadeDurationSlider = {},
                tmp,
                waiting;
            
               file = data.result;
               
               tmp = getTrackInfo(file.fileName);
               file.fileNaturalName = tmp.fileNaturalName;
               file.fileExt = tmp.fileExt;
               
               info = filler(template, 'Name', file.fileNaturalName);            
               info = filler(info, 'Start', timeFormat(0));
               info = filler(info, 'End', timeFormat(0));
               info = filler(info, 'FadePercentage', '');
               info = filler(info, 'FadeDuration', '');
               
               song = $(info);
               
               file.fadeInOutPct = 40;
               
               file.fadeInOutDur = 2;
               
               song.find('div:eq(0)').click(function(event){
            	   
            	   if($(this).parent().css('background-color') === 'rgba(0, 0, 0, 0)'){
            		   
            		   $('.secondaryTrackRow').css('background-color', 'rgba(0, 0, 0, 0)');
            		   
            		   $(this).parent().animate({backgroundColor: '#019be3'});
            		   
            		   $.data(contentBody[0], 'activeTrack', $(this).parent());
            	   }
            	   else {
            		   $(this).parent().css('background-color', 'rgba(0, 0, 0, 0)');
            		   
            		   $.data(contentBody[0], 'activeTrack', null);
            	   }
               });
               
               fadePercentageSlider.sliderHolder = song.find('div:eq(1) span');
               fadePercentageSlider.tooltip1 = fadePercentageSlider.sliderHolder.prev();
               fadePercentageSlider.percentage = file.fadeInOutPct;
               
               fadePercentageSlider.sliderHolder.slider({
                   min:1,
                   max:($('#jquery_jplayer_1').data().jPlayer.status.duration === 0 ? 1 : 100),
                   step:1,
                   value:40,
                   slide:function(event, ui)
                   {
                       var songInfo;
                           
                       songInfo = $(this).parent().parent().data().songInfo;
                    
                       songInfo.fadeInOutPct = ui.value;
                       
                       fadePercentageSlider.tooltip1.tooltip('option', 'content', ui.value.toString() + '%');
                       
                       fadePercentageSlider.tooltip1.focusin();
                       
                       updateSecondaryTrackList(songInfo);
                       
                   },
                   stop:function(event, ui)
                   {
                       fadePercentageSlider.tooltip1.focusout();
                   }
               });
              
               fadeDurationSlider.sliderHolder = song.find('div:eq(2) span');
               fadeDurationSlider.tooltip1 = fadeDurationSlider.sliderHolder.prev();
               fadeDurationSlider.duration = file.fadeInOutDur;
               
               fadeDurationSlider.sliderHolder.slider({
                   min:1,
                   max:($('#jquery_jplayer_1').data().jPlayer.status.duration === 0 ? 1 :  5),
                   step:1,
                   value:4,
                   slide:function(event, ui){
                       
                       var songInfo;
                       
                       songInfo = $(this).parent().parent().data().songInfo;
                       
                       songInfo.fadeInOutDur = ui.value;

                       fadeDurationSlider.tooltip1.tooltip('option', 'content', ui.value.toString());
                       
                       fadeDurationSlider.tooltip1.focusin();
                       
                       updateSecondaryTrackList(songInfo);
                   },
                   stop:function(event, ui){
                       
                       fadeDurationSlider.tooltip1.focusout();
                   }
               });
               
               contentBody.append(song);
               
               file.secondaryTrackType = 'FILE_UPLOAD';
               
               $.data(song[0], 'songInfo', file);
              
               waiting = setInterval(function(){
            	   
            	   if(!$('#jquery_jplayer_3').data().isBusy){
            		   clearInterval(waiting);
            		   getSongMetadata(song);
            	   }
               }, 750);
                              
               
               setSecondaryTooltips(fadePercentageSlider, fadeDurationSlider);
               
               fileCount++;
          
            
            if(!contentBody.is(':visible'))
            {
                contentBody.toggle();
            }
            
           
            if(fileCount === data.originalFiles.length)
            {
                $('#secondaryTrackBrowser').fileupload('destroy');
                $('#secondaryTrackBrowser').unbind();
                setSecondaryUploaderHelper();
                
                setStationVoiceInfo();
            }
                        
        }
    });    
}
function setSecondaryTooltips(fadePercentageSlider, fadeDurationSlider)
{
   /* positionSlider.tooltip1.tooltip({
        show: null,
        position: {
            my: "left top",
            at: "left bottom",
            of: positionSlider.sliderHolder.parent()
        },
        content: '00:00:00',
        open: function( event, ui ) {
                ui.tooltip.animate({ top: ui.tooltip.position().top - 20}, "fast" );
        }
    });
    
    positionSlider.tooltip2.tooltip({
        show: null,
        position: {
            my: "right top",
            at: "right bottom",
            of: positionSlider.sliderHolder.parent()
        },
        content: '00:00:00',
        open: function( event, ui ) {
                ui.tooltip.animate({ top: ui.tooltip.position().top - 20}, "fast" );
        }
    });
    */
    fadePercentageSlider.tooltip1.tooltip({
        show: {
        	effect: 'slideDown',
        	duration: 100
        },
        position: {
            my: "center top",
            at: "center bottom",
            of: fadePercentageSlider.sliderHolder
        },
        content: fadePercentageSlider.percentage.toString()
    });
    
    
    fadeDurationSlider.tooltip1.tooltip({
    	show: {
        	effect: 'slideDown',
        	duration: 100
        },
        position: {
            my: "center top",
            at: "center bottom",
            of: fadeDurationSlider.sliderHolder
        },
        content: fadeDurationSlider.duration.toString()
    });
}
function setSecondaryTrackMark()
{
	var mainPlayerStatus,
		secondaryTrackMenuContainer,
		activeTrack,
		proposedSong = {},
		positionInContainer,
		secondaryTrackTimeMark = {},
		mark;
	
	if($('#secondaryTrackEditBtn').data().edit === false){
		return;
	}
	
	mainPlayerStatus = $('#jquery_jplayer_1').data().jPlayer.status;
	
	if(mainPlayerStatus.duration === 0){
		return;
	}
	
	secondaryTrackMenuContainer = $('#secondaryTrackMenuContainer');
	
	if(typeof secondaryTrackMenuContainer.data().activeTrack === 'undefined' || secondaryTrackMenuContainer.data().activeTrack === null){
		return;
	}
	
	activeTrack = secondaryTrackMenuContainer.data().activeTrack;
	
	proposedSong.fileKey = activeTrack.data().songInfo.fileKey;
	proposedSong.start = mainPlayerStatus.currentTime;
	proposedSong.end = proposedSong.start + activeTrack.data().songInfo.fileDuration;
	
	if(!checkForSecondaryTracksCollisions(proposedSong)){
		
		activeTrack.data().songInfo.start = proposedSong.start;
    	activeTrack.data().songInfo.end = proposedSong.end;
  	   
  	   	if(!updateSecondaryTrackList(activeTrack.data().songInfo)){
  	   		
  	   	   $("#jquery_jplayer_2").data().list.push(activeTrack.data().songInfo);
  		   
  		   mark = setSecondaryTrackMarkHelper(activeTrack);
  		   
  		   secondaryTrackTimeMark.fileKey = activeTrack.data().songInfo.fileKey;
  		   secondaryTrackTimeMark.start = activeTrack.data().songInfo.start;
  		   secondaryTrackTimeMark.end = activeTrack.data().songInfo.end;
  		   secondaryTrackTimeMark.mark = mark;
  		   
  		   $('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks.push(secondaryTrackTimeMark);
        }
  	   else{
  		   
  		   secondaryTrackTimeMark = getSecondaryTrackMark(activeTrack.data().songInfo.fileKey);
  		   
  		   if(secondaryTrackTimeMark !== null){
  		   
  			   positionInContainer = (activeTrack.data().songInfo.start * (secondaryTrackTimeMark.mark.parent().width() /*- secondaryTrackTimeMark.mark.width()*/))/mainPlayerStatus.duration;
  			   
  			   secondaryTrackTimeMark.mark.css('left', positionInContainer + 'px');
  		   }
  		   
  	   }
  	   
  	   $("#jquery_jplayer_2").data().list.sort(sortSecondaryTrackByStart);
    }
}
function getSecondaryTrackMark(secondaryTrackFileKey)
{
	var secondaryTrackTimeMarks,
		i;
	
	secondaryTrackTimeMarks = $('#secondaryTrackTimeMarkContainer').data().secondaryTrackTimeMarks;
	
	for(i= 0; i < secondaryTrackTimeMarks.length; i++){
		
		if(secondaryTrackTimeMarks[i].fileKey === secondaryTrackFileKey){
			return secondaryTrackTimeMarks[i];
		}
	}
	
	return null;
}
function setSecondaryTrackMarkHelper(secondaryTrack)
{
	var template,
		mark,
		position,
		container,
		markContainer,
		positionInContainer,
		width,
		draggedWidth,
		time,
		proposedSong = {};
	
	template = $('#secondaryTrackTimeMarkTemplate').html();
	
	mark = $(template);
	
	$.data(mark[0], 'secondaryTrack', secondaryTrack);
	
	container = $('#secondaryTrackTimeMarkContainer');
	
	positionInContainer = (secondaryTrack.data().songInfo.start * (container.width() /*- mark.width()*/))/$('#jquery_jplayer_1').data().jPlayer.status.duration;
	
	width = (secondaryTrack.data().songInfo.end * (container.width() - mark.width()))/$('#jquery_jplayer_1').data().jPlayer.status.duration;
	
	width = width - positionInContainer;
	
	$.data(mark[0], 'originalWidth', width);
	
	width = (container.width() >= width + positionInContainer ? width : (container.width() - positionInContainer));
	
	container.append(mark
			         .draggable({
			        	 containment: 'parent', 
			     		 axis: 'x',
			     		 start: function(event, ui){			     		
			     			 
			     			 $('.secondaryTrackRow:gt(0)').css('background-color', 'rgba(0, 0, 0, 0)');
			     			 
			     			 $(this).data().secondaryTrack.animate({backgroundColor: '#019be3'});
			     			 $.data(this, 'left', ui.position.left);
			     		 },
			     		 stop: function(event, ui){			     			
			     			 $(this).data().secondaryTrack.css('background-color', 'rgba(0, 0, 0, 0)');
			     			 
			     			 time = (ui.position.left * $('#jquery_jplayer_1').data().jPlayer.status.duration) / 
			     			        ($(this).parent().width() /*- $(this).width()*/);
			     			 
			     			 proposedSong.fileKey = $(this).data().secondaryTrack.data().songInfo.fileKey;
			     			 proposedSong.start = time;
			     			 proposedSong.end = proposedSong.start + $(this).data().secondaryTrack.data().songInfo.fileDuration;
			     			 
			     			 if(!checkForSecondaryTracksCollisions(proposedSong)){
			     				 $(this).data().secondaryTrack.data().songInfo.start = proposedSong.start;
			     				 $(this).data().secondaryTrack.data().songInfo.end = proposedSong.end;
			     				 
			     				 /*draggedWidth = (proposedSong.end * ($(this).parent().width() - $(this).width())) / $('#jquery_jplayer_1').data().jPlayer.status.duration;
			     				 
			     				 draggedWidth = draggedWidth - ui.position.left;
			     				 
			     				 draggedWidth = ($(this).parent().width() >= draggedWidth ? draggedWidth : ($(this).parent().width() - ui.position.left));
			     				 
			     				 $(this).css('width', draggedWidth + 'px');*/
			     				 
			     				 if($(this).parent().width() < $(this).data().originalWidth){
			     					 
			     					 draggedWidth = (($(this).parent().width() - ui.position.left > $(this).data().originalWidth)
			     							 		  ? $(this).data().originalWidth : $(this).parent().width() - ui.position.left);
			     					 
			     					 $(this).css('width', draggedWidth + 'px');
			     				 }
			     				 
			     				$("#jquery_jplayer_2").data().list.sort(sortSecondaryTrackByStart);
			     			 }
			     			 else{
			     				 $(this).css('left', $(this).data().left + 'px');
			     			 }
			     		 }
			         })
			         .css('left', positionInContainer + 'px')
			         .css('width', width + 'px')
			         .click(function(event){
			        	 $('.secondaryTrackRow:gt(0)').css('background-color', 'rgba(0, 0, 0, 0)');
			        	 $(this).data().secondaryTrack.animate({backgroundColor: '#019be3'});
			         }));
	
	return mark;
}
function setSlideTimeMark(chosenSlideIndex)
{
	var slidesTimeMarks,
		slideInfo = {},
		mainPlayerStatus,
		i,
		length,
		newSlide = true,
		container,
		mark,
		positionInContainer;
	
	if($('#slidesEditBtn').data().edit === false){
		return;
	}
	
	mainPlayerStatus = $('#jquery_jplayer_1').data().jPlayer.status;
	
	if(mainPlayerStatus.duration === 0 || Galleria.get(0).getDataLength() === 0){
		return;
	}
	
	//chosenSlideIndex = Galleria.get(0).getIndex();
	
	container = $('#slidesTimeMarkContainer');
	
	
	slideInfo.slide = Galleria.get(0).getData(chosenSlideIndex).slide;
	slideInfo.time = mainPlayerStatus.currentTime;
	slideInfo.position = chosenSlideIndex;
	
	if(!checkForSlidesCollisions(slideInfo, null)){
		
		if(slideInfo.slide.hasOwnProperty('slideKey')){
			slideInfo.isNewMark = true;
		}
		
		container.data().slidesTimeMarks.push(slideInfo);
		
		container.data().slidesTimeMarks.sort(sortSlidesByTime);
		
		setSlideTimeMarkHelper(slideInfo);
	}
	
	/*if(!container.data().hasOwnProperty('slidesTimeMarks')){
		
		slideInfo.slide = Galleria.get(0).getData(chosenSlideIndex).slide;
		slideInfo.time = mainPlayerStatus.currentTime;
		slideInfo.position = chosenSlideIndex;
		
		$.data(container[0], 'slidesTimeMarks', [slideInfo]);
		
		setSlideTimeMarkHelper(slideInfo);
		
		return;
	}
	*/
	//slidesTimeMarks = container.data().slidesTimeMarks;
	
	/*length = slidesTimeMarks.length;
	
	for(i = 0; i < length; i++){
		
		if(slidesTimeMarks[i].slide === Galleria.get(0).getData(chosenSlideIndex).slide){
			chosenSlideIndex = i;
			newSlide = false;
			break;
		}
	}*/
	
	//if(newSlide){		
	
		/*slideInfo.slide = Galleria.get(0).getData(chosenSlideIndex).slide;
		slideInfo.time = mainPlayerStatus.currentTime;
		slideInfo.position = chosenSlideIndex;
	
		slidesTimeMarks.push(slideInfo);
		slidesTimeMarks.sort(sortSlidesByTime);
		
		setSlideTimeMarkHelper(slideInfo);
/*	}
	else {
		slidesTimeMarks[chosenSlideIndex].time.push(mainPlayerStatus.currentTime);
		
		mark = container.find('span:eq(' + chosenSlideIndex + ')');
		
		positionInContainer = (slidesTimeMarks[chosenSlideIndex].time * (container.width() - mark.width()))/mainPlayerStatus.duration;
		console.log('position->' + positionInContainer);
		mark.css('left', positionInContainer + 'px');
	}*/
	
}
function setSlideTimeMarkHelper(slideInfo, mainTrackDuration)
{
	var template,
		mark,
		position,
		container,
		markContainer,
		positionInContainer,
		time,
		proposedSlide = {},
		duration;
	
	template = $('#slideTimeMarkTemplate').html();
	
	mark = $(template);
	
	$.data(mark[0], 'slide', slideInfo);
	
	container = $('#slidesTimeMarkContainer');
	
	duration = mainTrackDuration === null || typeof mainTrackDuration === 'undefined' ? $('#jquery_jplayer_1').data().jPlayer.status.duration : mainTrackDuration;
	
	positionInContainer = (slideInfo.time * (container.width() /*- mark.width()*/))/ duration;
	
	positionInContainer = ((positionInContainer + container.width() * 0.01) >= container.width() ? positionInContainer - container.width() * 0.01 : positionInContainer);
	
	container.append(mark
		.draggable({
		containment: 'parent', 
		axis: 'x',
		create:function(e,u){
			console.log(u);
		},
		start: function(event, ui){
			position = $(this).data().slide.position;
			Galleria.get(0).show(position);
			$.data(this, 'left', ui.position.left);
			
		},
		drag: function(event, ui){
			
			/*duration = 300;//$('#jquery_jplayer_1').data().jPlayer.status.duration;
				
			if(duration > 0){		
				
				time = (ui.position.left * duration) / (container.width() - mark.width());
			}
			else{				
			
				time = -1;
			}*/
		},
		stop: function(event, ui){
			
			time = (ui.position.left * $('#jquery_jplayer_1').data().jPlayer.status.duration) / ($(this).parent().width() /*- $(this).width()*/);
			
			proposedSlide.time = time;
			
			if(!checkForSlidesCollisions(proposedSlide, $(this).data().slide)){
				
				$(this).data().slide.time = time;
				$(this).parent().data().slidesTimeMarks.sort(sortSlidesByTime);
			}
			else{
				
				$(this).css('left', $(this).data().left + 'px');
			}
			//sortSlidesByTime(container.data().slidesTimeMarks);
		}
		})
		.css('left', positionInContainer + 'px')
		.click(function(event){
			Galleria.get(0).show($(this).data().slide.position);
		}));
}
function sortSlidesByTime(slide1, slide2)
{
	if(slide1.time < slide2.time){
		return 1;
	}
	if(slide1.time >  slide2.time){
		return -1;
	}
	return 0;
}
function setMainTrackPlayer()
{
     $("#jquery_jplayer_1").jPlayer({
        swfPath: "/js/djInterface/Player/",
        cssSelectorAncestor: '#jp_container_1',
        solution: 'html,flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:1,
      }).bind($.jPlayer.event.ready, function(event){
    	  $.data(event.currentTarget, 'originalVolume', $(event.currentTarget).data().jPlayer.options.volume);
      }).bind($.jPlayer.event.seeking, function(event){
          controlSeekingtimePlayer(event);          
      }).bind($.jPlayer.event.timeupdate, function(event){
          controlPlaytime(event);
      }).bind($.jPlayer.event.pause, function(event){
    	  pauseSecondaryPlayer(event);
      }).bind($.jPlayer.event.volumechange, function(event){
    	  //$(event.currentTarget).data().originalVolume = $(event.currentTarget).data().jPlayer.options.volume;
      });
     
     $('#jp_container_1 .jp-stop').bind('click', function(){    	  
    	  $.data($('#jquery_jplayer_2')[0],'timeout', false);
    	  
    	  console.log('setting timeout to ', $('#jquery_jplayer_2').data().timeout);
      });
}
function setProgramDurationMark()
{
	var programDurationMark,
		active,
		active_program,
		time,
		initialPosition = -1;
	
	programDurationMark = $('.programDurationMark');
	
	active = $('#accordion_channel').accordion('option', 'active');
	
	if(active !== false){
		active_program = $('#accordion_channel div:eq(' + active + ')').data();
		
		if(active_program.hasOwnProperty('active_program')){
			active_program = active_program.active_program;
			
			initialPosition = ((active_program.programTotalDurationTime * $('#jp_container_1 .jp-progress').width()) / 
					$('#jquery_jplayer_1').data().jPlayer.status.duration/*active_program.mainTrack.mainTrackDuration*/) - parseFloat(programDurationMark.css('width'), 10);
			
			$.data($('#jquery_jplayer_1')[0], 'programTotalDurationTime', active_program.programTotalDurationTime);
			
			programDurationMark.css('left', initialPosition + 'px');
		}
		else {
			$.data($('#jquery_jplayer_1')[0], 'programTotalDurationTime', -1);
			programDurationMark.css("left",$('#jp_container_1 .jp-progress').width() - programDurationMark.width());
		}
	}
	else {
		$.data($('#jquery_jplayer_1')[0], 'programTotalDurationTime', -1);
		programDurationMark.css("left",$('#jp_container_1 .jp-progress').width() -programDurationMark.width());
	}
	
	programDurationMark
	.css('display', 'block')
	.draggable({
   	 containment: 'parent', 
		 axis: 'x',
		 stop: function(event, ui){
			 time = (ui.position.left * $('#jquery_jplayer_1').data().jPlayer.status.duration) / 
		        	($(this).parent().width());
			 
			 $.data($('#jquery_jplayer_1')[0], 'programTotalDurationTime', time);
			 
		 }
		 })
     .tooltip();
}
function setSecondaryTrackPlayer()
{
     $("#jquery_jplayer_2").jPlayer({
        swfPath: "JS/Player/",
        cssSelectorAncestor: '#jp_container_2',
        solution: 'html, flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:1
      }).bind($.jPlayer.event.pause, function(event){
    	  $.data(event.currentTarget, 'playing', false);
      });
}
function setHelperPlayer()
{
	$("#jquery_jplayer_3").jPlayer({
        swfPath: "JS/Player/",
        cssSelectorAncestor: '#jp_container_3',
        solution: 'html, flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:0
      });
	
	$.data($("#jquery_jplayer_3")[0], 'waitingList', []);
}
function pauseSecondaryPlayer(event)
{	console.log('paused at ' + event.jPlayer.status.currentTime);
	$('#jquery_jplayer_2').jPlayer('pause');
}
function stopMainPlayer()
{
	$('#jquery_jplayer_1').jPlayer('stop');
}
function stopSecondaryPlayer()
{	
	$('#jquery_jplayer_2').jPlayer('stop');
}
function controlSeekingtimePlayer(event)
{
	/*if(!$('#jquery_jplayer_2').data().jPlayer.status.paused){
		$.data($(event.currentTarget)[0], 'seekedTime', event.jPlayer.status.currentTime);
		$($(event.currentTarget)[0]).data().fadeOut = true;
		console.log('control seeked time->' + event.jPlayer.status.currentTime);
	}*/
	/*console.log('removing prop fadeOutOcurred');
	if($(event.currentTarget).data().jPlayer.options.volume >= $(event.currentTarget).data().originalVol){
	
		$(event.currentTarget).data().fadeOutOcurred = false;
	}*/
	/*var mainPlayer;
	
	mainPlayer = $(event.currentTarget);
	*/
	/*if(mainPlayer.data().jPlayer.options.volume !== mainPlayer.data().originalVolume /*&&
	   (mainPlayer.data().jPlayer.status.currentTime > (mainPlayer.data().fadeOutBy.end + 
	   mainPlayer.data().fadeOutBy.fadeInOutDur * 4)) ||
	   (mainPlayer.data().jPlayer.status.currentTime < mainPlayer.data().fadeOutBy.start)*///){
	/*	$(event.currentTarget).jPlayer('volume', $(event.currentTarget).data().originalVolume);
	}*/
	$.data(event.currentTarget, 'hasSeeked', true);
}
function controlPlaytime(event)
{
	var list,
		currentTime,
		i,
		mainPlayer,
		secondaryPlayer,
		flagStart,
		flagEnd,
		newVolume,
		waitingTimeForFadeIn = 3,
		programTotalDurationTime;
	
	currentTime = event.jPlayer.status.currentTime;
	
	programTotalDurationTime = $('#jquery_jplayer_1').data().hasOwnProperty('programTotalDurationTime') ? 
			                   $('#jquery_jplayer_1').data().programTotalDurationTime : Number.MAX_VALUE;
			                   
	if(programTotalDurationTime !== -1 && currentTime > programTotalDurationTime){
		stopMainPlayer();
		return;
	}
	playSlide(currentTime);
	
	if(!$('#jquery_jplayer_2').data().hasOwnProperty('list')){
		return;
	}
	
	if($('#secondaryTrackEditBtn').data().edit){
		return;
	}
	
	secondaryPlayer = $('#jquery_jplayer_2');
	
	list = secondaryPlayer.data().list;	
	
	mainPlayer = $(event.currentTarget);
	
	
	for(i = 0; i < list.length; i++){
		
		flagStart = currentTime >= list[i].start;
		flagEnd = currentTime <= list[i].end;
		
		if(secondaryPlayer.data().hasOwnProperty('songInfo') && list[i].fileKey === secondaryPlayer.data().songInfo.fileKey && flagStart && !flagEnd){
			console.log(list[i].fileKey,'===', secondaryPlayer.data().songInfo.fileKey);
			secondaryPlayer.data().timeout = false;
		}
		
		if(flagEnd && (secondaryPlayer.data().jPlayer.status.paused || mainPlayer.data().hasSeeked) && !secondaryPlayer.data().timeout){
			console.log('before preload', ' timeout->', secondaryPlayer.data().timeout);
			preloadSecondaryTrack(mainPlayer, secondaryPlayer, list[i]);
		}
		
		if((secondaryPlayer.data().jPlayer.status.paused && !secondaryPlayer.data().playing/*&& !secondaryPlayer.data().timeout*/) &&
			flagStart && flagEnd && !mainPlayer.data().jPlayer.status.paused){
			console.log(list[i].fileKey, ' play at->', getAproxTime2(currentTime) - getAproxTime2(list[i].start));
			controlSecondaryPlayer(secondaryPlayer, getAproxTime2(currentTime) - getAproxTime2(list[i].start));
			
			break;
		}
		else if(!secondaryPlayer.data().jPlayer.status.paused &&
				flagStart && flagEnd &&
				mainPlayer.data().jPlayer.options.volume !== (mainPlayer.data().originalVolume * (list[i].fadeInOutPct / 100))){
			console.log('before fadeout');
			newVolume = getFadeOutVolume2(list[i], mainPlayer);
			
			if(newVolume >= (mainPlayer.data().originalVolume * (list[i].fadeInOutPct / 100))){
				console.log('fade OUT sec->' + currentTime +  ' newVolume->' + newVolume);
				mainPlayer.jPlayer('volume', newVolume);
				
				$.data(mainPlayer[0], 'fadeOutBy', list[i]);
				
				break;
			}
			else{
				mainPlayer.jPlayer('volume', (mainPlayer.data().originalVolume * (list[i].fadeInOutPct / 100)));
				
				$.data(mainPlayer[0], 'fadeOutBy', list[i]);
				
				break;
			}
		}
		else if(!flagEnd && 
			     Math.floor(mainPlayer.data().jPlayer.status.currentTime) <= (list[i].end + list[i].fadeInOutDur) &&
			     (i === list.length - 1 || list[i+1].start - currentTime >= waitingTimeForFadeIn)){
			console.log('before fadein');
			newVolume = getFadeInVolume2(list[i], mainPlayer);
			
			if(newVolume <= mainPlayer.data().originalVolume){
				$.data(secondaryPlayer[0], 'playing', false);
				console.log('fade IN sec->' + currentTime +  ' newVolume->' + newVolume);
				mainPlayer.jPlayer('volume', newVolume);
				break;
			}
			
		}
		else if(mainPlayer.data().hasSeeked){
			
			if(flagStart && flagEnd){
			
				//preloadSecondaryTrack(mainPlayer, secondaryPlayer, list[i]);
				if(!mainPlayer.data().jPlayer.status.paused){
					
				
				mainPlayer.jPlayer('volume', mainPlayer.data().originalVolume * (list[i].fadeInOutPct / 100));
				
				preloadSecondaryTrack(mainPlayer, secondaryPlayer, list[i]);
				//secondaryPlayer.jPlayer('play', Math.floor(currentTime) - list[i].start);
				
				console.log(list[i].fileKey,' seeked vol inside->' + mainPlayer.data().jPlayer.options.volume);
				}
				
				mainPlayer.data().hasSeeked = false;
			}
			else if(isOutsideAnySecondaryTrack(i, list, currentTime)){
				
				mainPlayer.jPlayer('volume', mainPlayer.data().originalVolume);
				
				secondaryPlayer.jPlayer('stop');
				$.data(secondaryPlayer[0], 'playing', false);
				
				console.log('seeked vol outside->' + mainPlayer.data().jPlayer.options.volume);
				
				mainPlayer.data().hasSeeked = false;
				
				//$.data(secondaryPlayer[0], 'timeout', false);
			}
			
		}
	}
}
function preloadSecondaryTrack(mainPlayer, secondaryPlayer, songInfo)
{
	var media = {},
		songInfoTmp,
		flagStart,
		flagEnd;
	
	media[songInfo.fileExt] = '/audioStreaming?file_id=' + songInfo.fileKey;
	console.log('enter preload');
	
	if(!secondaryPlayer.data().hasOwnProperty('timeout')){
		$.data(secondaryPlayer[0], 'timeout', true);
	}
	else{
		secondaryPlayer.data().timeout = true;
	}
	console.log(media[songInfo.fileExt] === secondaryPlayer.data().jPlayer.status.src, ' ',
			secondaryPlayer.data().jPlayer.status.duration > 0);
	
	if(media[songInfo.fileExt] === secondaryPlayer.data().jPlayer.status.src && secondaryPlayer.data().jPlayer.status.duration > 0){
		return;
	}
	
	$.data(secondaryPlayer[0], 'songInfo', songInfo);
	console.log('preloading...' + songInfo.fileKey, ' timeout->', secondaryPlayer.data().timeout);
	secondaryPlayer.jPlayer('setMedia', media)
				   .bind($.jPlayer.event.loadstart, function(event){					   
					   
					   songInfoTmp = $(event.currentTarget).data().songInfo;
					   
					   flagStart = mainPlayer.data().jPlayer.status.currentTime >= songInfoTmp.start;
					   flagEnd = mainPlayer.data().jPlayer.status.currentTime <= songInfoTmp.end;
					   
					   if(!mainPlayer.data().jPlayer.status.paused && flagStart && flagEnd){
						   
						   $(event.currentTarget).jPlayer('play', 
			                          getAproxTime(mainPlayer.data().jPlayer.status.currentTime 
			                        		       - songInfoTmp.start));
					   }
					   
					   $(event.currentTarget).unbind($.jPlayer.event.loadstart);
				   });
}
function playSlide(currentTime){
	var container,
		slidesTimeMarks,
		i;
	
	container = $('#slidesTimeMarkContainer');
	
	if(!container.data().hasOwnProperty('slidesTimeMarks')){
		return;
	}
	
	slidesTimeMarks = container.data().slidesTimeMarks;	
	
	if($('#slidesEditBtn').data().edit){
		return;
	}
	
	for(i = 0; i < slidesTimeMarks.length; i++){
		
		if(currentTime >= slidesTimeMarks[i].time){
			if(slidesTimeMarks[i].position !== container.data().activeSlide){
			Galleria.get(0).show(slidesTimeMarks[i].position);
			$.data(container[0], 'activeSlide', slidesTimeMarks[i].position);
			}
			break;
		}
	}
}
function isOutsideAnySecondaryTrack(position, list, currentTime)
{
	var i;
	
	for(i = position + 1; i < list.length; i++){
		
		if(currentTime >= list[i].start && currentTime <= list[i].end){
			return false;
		}
	}
	
	return true;
}
function getFadeOutVolume2(secondaryTrack, mainPlayer){
	var aproxTime,
		currentVolume,
		tick,
		newVolume;
	
	aproxTime = getAproxTime2(mainPlayer.data().jPlayer.status.currentTime);
	
	currentVolume = mainPlayer.data().jPlayer.options.volume;
	
	tick = parseInt((secondaryTrack.fadeInOutDur * 4) - (aproxTime - secondaryTrack.start) / 0.250, 10);
	
	if(tick > 0){
	
		newVolume = currentVolume - ((currentVolume - 
			    	mainPlayer.data().originalVolume * (secondaryTrack.fadeInOutPct / 100)) / tick);
	
		newVolume = Math.round(newVolume * 100) / 100;
	}
	else{
		newVolume = -1;
	}
	return newVolume;
}
function getFadeInVolume2(secondaryTrack, mainPlayer)
{
	var aproxTime,
	currentVolume,
	tick,
	newVolume;

	aproxTime = getAproxTime2(mainPlayer.data().jPlayer.status.currentTime);

	currentVolume = mainPlayer.data().jPlayer.options.volume;

	tick = parseInt((secondaryTrack.fadeInOutDur * 4) - (aproxTime - secondaryTrack.end) / 0.250, 10);
	
	if(tick > 0){
		newVolume = currentVolume - ((currentVolume - 
		    		mainPlayer.data().originalVolume * mainPlayer.data().originalVolume) / tick);

		newVolume = Math.round(newVolume * 100) / 100;
	}
	else{
		newVolume = 2;
	}

	return newVolume;
}
function getFadeOutVolume(secondaryTrack, mainPlayer)
{
	var percentagePerStep,
		aproxTime,
		newVolume;
	
	percentagePerStep = (mainPlayer.data().originalVolume * (1 - secondaryTrack.fadeInOutPct / 100))
						/ (secondaryTrack.fadeInOutDur * 4);
	
	aproxTime = getAproxTime(mainPlayer.data().jPlayer.status.currentTime);
	
	newVolume = mainPlayer.data().originalVolume - percentagePerStep * (
			   aproxTime - secondaryTrack.start) / 0.250;
	
	newVolume = Math.round(newVolume * 10000) / 10000;
	
	return newVolume;
}
function getFadeInVolume(secondaryTrack, mainPlayer)
{
	var percentagePerStep,
		aproxTime,
		newVolume;
	
	percentagePerStep = (mainPlayer.data().originalVolume * (1 - secondaryTrack.fadeInOutPct / 100))
						/ (secondaryTrack.fadeInOutDur * 4);
	
	aproxTime = getAproxTime(mainPlayer.data().jPlayer.status.currentTime);
	
	newVolume = (mainPlayer.data().originalVolume * (secondaryTrack.fadeInOutPct / 100)) + percentagePerStep * (
			   aproxTime - secondaryTrack.end) / 0.250;
	
	newVolume = Math.round(newVolume * 10000) / 10000;
	
	newVolume = parseInt(newVolume,10) === 1 ? 1 : newVolume;
	
	return newVolume;
}
function getAproxTime2(currentTime)
{
	var aproxTime,
	intTime,
	ticks;

	intTime = parseInt(currentTime, 10);

	ticks = currentTime - intTime;

	ticks = Math.floor(ticks / 0.250) * 0.250;

	aproxTime = intTime + ticks;

	return aproxTime;
}
function getAproxTime(currentTime)
{
	var aproxTime,
		intTime,
		ticks;
	
	intTime = parseInt(currentTime, 10);
	
	ticks = currentTime - intTime;
	
	ticks = ticks >= 0.90 ? 1 : ticks;
	
	ticks = Math.floor(ticks / 0.250) * 0.250;
	
	aproxTime = intTime + ticks;
	
	return aproxTime;
}
function controlPlaytimePlayer2(event)
{
    var list,
        currentTime,
        i,
        mainPlayer,
        secondaryPlayer,
        chosen = null,
        waitingTimeFadeIn = 5;
    
    if(!$('#jquery_jplayer_2').data().hasOwnProperty('list'))
    {
        return;
    }
    
    list = $('#jquery_jplayer_2').data().list;
    
    currentTime = event.jPlayer.status.currentTime;
    
    mainPlayer = $(event.currentTarget);
    
    secondaryPlayer = $('#jquery_jplayer_2').data();
    
    for(i = 0; i < list.length; i++)
    {
        if(secondaryPlayer.jPlayer.status.paused && !secondaryPlayer.timeout &&
           currentTime >= list[i].start && 
           currentTime <= list[i].end)
        {
        	console.log('song->' + list[i].fileNaturalName + ' play at ->' + (Math.floor(currentTime) - list[i].start) + ' current time: ' + currentTime);
        	
        	if(!mainPlayer.data().hasSeeked && !mainPlayer.data().fadeOutOcurred){
        	
        		$.data(mainPlayer[0], 'originalVol', mainPlayer.data().jPlayer.options.volume);
        	}
        	else{
        		mainPlayer.data().hasSeeked = false;
        	}
        		
            controlSecondaryPlayer(list[i], Math.floor(currentTime) - list[i].start);
            
            /*steps = 0;
            
            //interval = setInterval(function(){
                
            	manageSecondaryPlayerFadeOut(list[i], mainPlayer);
                
                steps++;
                
                if(steps === list[i].fadeInOutStep)
                {
                    clearInterval(interval);
                    
                    interval = setInterval(function(){
                    	manageSecondaryPlayerFadeIn(list[i], mainPlayer);
                    	
                    	steps--;
                    	
                    	if(steps === 0)
                        {
                    		console.log('interval cleared')
                    		clearInterval(interval);
                        }
                    }, list[i].fadeInOutDur * 1000);
                }
               /* }, 
                list[i].fadeInOutDur * 1000);*/
            break;
        }
        else if(!secondaryPlayer.jPlayer.status.paused){
        	
        	/*if(!mainPlayer.data().hasOwnProperty('fadeOut')){
            	$.data(mainPlayer[0], 'fadeOut', true);
            }
        	
        	if(mainPlayer.data().fadeOut){
        		manageSecondaryPlayerFadeOut(list[i], mainPlayer);
        	}*/
        	
        	if(!mainPlayer.data().fadeOutOcurred && Math.round(currentTime) < (list[i].start + list[i].fadeInOutDur * list[i].fadeInOutStep) &&
        		mainPlayer.data().jPlayer.options.volume > (mainPlayer.data().originalVol*(1-list[i].fadeInOutPct)) ){
        		manageSecondaryPlayerFadeOut(list[i], mainPlayer);
        		break;
        	}
        	else if(currentTime <= list[i].end){
        		console.log('fade out finished!');
        		$.data(mainPlayer[0], 'fadeOutOcurred', true);
        		$.data(mainPlayer[0], 'lastFadeOutVolume', mainPlayer.data().jPlayer.options.volume);
        	}
        	
        	/*else if(Math.round(currentTime) > (secondaryPlayer.jPlayer.status.duration 
        			- (list[i].fadeInOutStep * list[i].fadeInOutDur))){
        		
        		/*if(!mainPlayer.data().hasOwnProperty('fadeIn')){
                	$.data(mainPlayer[0], 'fadeIn', true);
                }
        		
        		if(mainPlayer.data().fadeIn && manageSecondaryPlayerFadeIn(list[i], mainPlayer) === 0){
        			mainPlayer.data().fadeIn = false;
        		}*/
        	/*	
        		manageSecondaryPlayerFadeIn(list[i], mainPlayer);
        	}*/
        }
        else if(mainPlayer.data().fadeOutOcurred){ //&& (list[i].start - currentTime) >= waitingTimeFadeIn || i === list.length - 1){
        	//remove the fadeOutOcurred property
        	
        	if((list[i].start - Math.floor(currentTime)) >= 0 || (list[i].start - currentTime) >= waitingTimeFadeIn){        		
        		chosen = list[i-1];
        	}
        	else if(i === list.length - 1){
        		chosen = list[i];
        	}
        	
        	if(chosen !== null){
        	
	        	if(mainPlayer.data().jPlayer.options.volume < mainPlayer.data().originalVol){
	        	
	        		manageSecondaryPlayerFadeIn(chosen, mainPlayer);
	        	}
	        	else{
	        		
	        		$(mainPlayer.data()).removeProp('fadeOutOcurred');
	        		
	        		console.log('paused 1->' + secondaryPlayer.jPlayer.status.paused + ' paused2->' + $('#jquery_jplayer_2').data().jPlayer.status.paused + ' current time->' + currentTime + ' fade in finished! ' + !mainPlayer.data().fadeOutOcurred);
	        	}
	        	
	        	break;
        	}
        	
        }
    }
}
function manageSecondaryPlayerFadeIn(secondaryTrack, mainPlayer)
{
    var percentagePerStep,
    	newVolume,
    	step;
    
    percentagePerStep = secondaryTrack.fadeInOutPct/(secondaryTrack.fadeInOutStep * secondaryTrack.fadeInOutDur * 4 * 100);
    
    newVolume = mainPlayer.data().lastFadeOutVolume +
    		    (((mainPlayer.data().jPlayer.status.currentTime - secondaryTrack.end)/0.250) + 1) * percentagePerStep;
    
    
    /*percentagePerStep = 1 + (secondaryTrack.fadeInOutPct / secondaryTrack.fadeInOutStep / secondaryTrack.fadeInOutDur)/100;
    
    step = secondaryTrack.fadeInOutStep - (Math.floor((Math.round(mainPlayer.data().jPlayer.status.currentTime) - secondaryTrack.start)
            / secondaryTrack.fadeInOutDur) % secondaryTrack.fadeInOutStep);
    
    //mainPlayer.jPlayer('volume', mainPlayer.data().jPlayer.options.volume * percentagePerStep);
    mainPlayer.jPlayer('volume', mainPlayer.data().jPlayer.options.volume * Math.pow(percentagePerStep, step));
    
    console.log('after fade in->'+mainPlayer.data().jPlayer.options.volume);*/
    
    console.log('song->' + secondaryTrack.fileNaturalName + ' fade in -> time: ' + mainPlayer.data().jPlayer.status.currentTime + ' lastFadeOutVol: ' + mainPlayer.data().lastFadeOutVolume + ' old vol: ' + mainPlayer.data().jPlayer.options.volume + ' new vol: ' + newVolume);
    
    mainPlayer.jPlayer('volume', newVolume);
}
function manageSecondaryPlayerFadeOut(secondaryTrack, mainPlayer)
{
    var percentagePerStep,
    	step,
    	newVolume;
    
    
    percentagePerStep = secondaryTrack.fadeInOutPct/(secondaryTrack.fadeInOutStep * secondaryTrack.fadeInOutDur * 4 * 100);
    
    newVolume = mainPlayer.data().originalVol - 
    		    (((mainPlayer.data().jPlayer.status.currentTime - secondaryTrack.start)/0.250) + 1) * percentagePerStep;
    
    console.log('song->' +  secondaryTrack.fileNaturalName +' fade out -> time: ' + mainPlayer.data().jPlayer.status.currentTime + ' original vol: ' +  mainPlayer.data().originalVol +' old vol: ' + mainPlayer.data().jPlayer.options.volume + ' new vol: ' + newVolume);
    
    mainPlayer.jPlayer('volume', newVolume);
    
    
    /*
    percentagePerStep = 1 - (secondaryTrack.fadeInOutPct / (secondaryTrack.fadeInOutStep * secondaryTrack.fadeInOutDur * 4 * 100));//1 - (secondaryTrack.fadeInOutPct / secondaryTrack.fadeInOutStep / secondaryTrack.fadeInOutDur)/4/100;
    percentagePerStep = Math.round(percentagePerStep * 10000) / 10000;
    
    step = Math.round(((mainPlayer.data().jPlayer.status.currentTime) - secondaryTrack.start)
           / secondaryTrack.fadeInOutDur) % secondaryTrack.fadeInOutStep;
    
    /*stepLookAhead = Math.round(((mainPlayer.data().jPlayer.status.currentTime + 0.250) - secondaryTrack.start)
            / secondaryTrack.fadeInOutDur) % secondaryTrack.fadeInOutStep;*/
    
   //if(((secondaryTrack.fadeInOutStep - 1) - step) >= ((secondaryTrack.fadeInOutStep - 1) - stepLookAhead)){
    //if(step < secondaryTrack.fadeInOutDur){
    /* if(!mainPlayer.data().hasOwnProperty('previousStep')){
    	 $.data(mainPlayer[0], 'previousStep',  -1);
     }
     
	 if(mainPlayer.data().previousStep <= step){  
	   	newVolume = mainPlayer.data().jPlayer.options.volume * Math.pow(percentagePerStep, step);
	   	newVolume = Math.round(newVolume * 10000) / 10000;
	   	
	   	if(!mainPlayer.data().hasOwnProperty('newVolume')){
	   		$.data(mainPlayer[0], 'newVolume', [{sec : mainPlayer.data().jPlayer.status.currentTime, vol : newVolume}]);
	   	}
	   	else if(mainPlayer.data().hasOwnProperty('seekedTime')){
	   		newVolume = checkSeekedTime(mainPlayer.data().newVolume,mainPlayer.data().seekedTime);
	   		mainPlayer.data().newVolume.push({sec : mainPlayer.data().jPlayer.status.currentTime, vol : newVolume});
	   		$(mainPlayer.data()).removeProp('seekedTime');
	   	}
	   	else{
	   		mainPlayer.data().newVolume.push({sec: mainPlayer.data().jPlayer.status.currentTime, vol: newVolume});
	   	}
	   	
    	mainPlayer.jPlayer('volume', newVolume);
    	console.log('sec-> ' +  (mainPlayer.data().jPlayer.status.currentTime) +' step -> ' + step + ',' + secondaryTrack.fadeInOutDur +' after fade out->'+mainPlayer.data().jPlayer.options.volume);
    }
    else{
    	mainPlayer.data().fadeOut =  false;
    }
	 
    mainPlayer.data().previousStep = step;*/
    
    //mainPlayer.jPlayer('volume', mainPlayer.data().jPlayer.options.volume * percentagePerStep);
    
}
function checkSeekedTime(newVolume, seekedTime)
{
	var i,
		length,
		vol;
	
	for(i = 0, length = newVolume.length; i < length; i++ ){
		if(seekedTime <= newVolume[i].sec){
			vol = newVolume[i].vol;
			newVolume.splice(i);
			return vol;
		}
	}
}
function checkForSlidesCollisions(slide, draggedSlide)
{
	var slidesTimeMarks = [],
		collision = false,
		i,
		timeWindow = 5;
	
	if(!$('#slidesTimeMarkContainer').data().hasOwnProperty('slidesTimeMarks')){
		$.data($('#slidesTimeMarkContainer')[0], 'slidesTimeMarks', slidesTimeMarks);
	}
	else{
		
		slidesTimeMarks = $('#slidesTimeMarkContainer').data().slidesTimeMarks;
		
		for(i = 0; i < slidesTimeMarks.length; i++){
			
			if((draggedSlide === null || draggedSlide.time !== slidesTimeMarks[i].time)
				&& Math.abs(slide.time - slidesTimeMarks[i].time) <= timeWindow){
				collision = true;
				break;
			}
		}
	}
	
	return collision;
	
}
function checkForSecondaryTracksCollisions(secondaryTrack)
{
    var list = [],
        i,
        collision = false;
    
    if(!$("#jquery_jplayer_2").data().hasOwnProperty('list'))
    {
        $.data($("#jquery_jplayer_2")[0], 'list', list); 
        $.data($('#secondaryTrackTimeMarkContainer')[0], 'secondaryTrackTimeMarks', []);
    
    }
    else
    {
        list = $("#jquery_jplayer_2").data().list;
        
        for(i = 0; i < list.length; i++)
        {
            if(secondaryTrack.fileKey !== list[i].fileKey &&
               ((secondaryTrack.start >= list[i].start && secondaryTrack.end <= list[i].end) ||
                (secondaryTrack.start <= list[i].start && secondaryTrack.end >= list[i].end) ||
                (secondaryTrack.start >= list[i].start && secondaryTrack.end >= list[i].end && secondaryTrack.start <= list[i].end) ||
                (secondaryTrack.start <= list[i].start && secondaryTrack.end <= list[i].end && secondaryTrack.end >= list[i].start)))
            {
                collision = true;                
                break;
            }
        }
        
    }
    
    return collision;
}
function sortSecondaryTrackByStart(track1, track2)
{
	if(track1.start < track2.start){
		return -1;
	}
	if(track1.start > track2.start){
		return 1;
	}
	
	return 0;
}

function sortProgramkBySequenceNumber(program1, program2)
{
	if(program1.programSequenceNumber < program2.programSequenceNumber){
		return -1;
	}
	if(program1.programSequenceNumber > program2.programSequenceNumber){
		return 1;
	}
	
	return 0;
}
function updateSecondaryTrackList(secondaryTrack)
{
    var list,
        i;
    
    if(!$("#jquery_jplayer_2").data().hasOwnProperty('list'))
    {
        return false;
    }
    
    list = $("#jquery_jplayer_2").data().list;
    
    for(i = 0; i < list.length; i++)
    {
        if(secondaryTrack.fileKey === list[i].fileKey)
        {
            return true;
        }
    }
    
    return false;
}
function controlMainPlayer(songInfo)
{
	var media = {},
		waiting;
	
	media[songInfo.fileExt] = '/fileDownload?file_id=' + songInfo.fileKey;
	
	$('#mainTrackTitleTextContainer').text(songInfo.fileNaturalName);
	
    $('#jquery_jplayer_1').jPlayer({'errorAlerts': true, 'warningAlerts': true}).jPlayer('setMedia', media)
    	.jPlayer('pause')
    	.bind($.jPlayer.event.loadstart, function(event){
    		enableSecondaryTrackControls(songInfo);
    		
    		waiting = setInterval(function() {
    			
    			if($('#jquery_jplayer_1').data().jPlayer.status.duration > 0){
    				clearInterval(waiting);
    				setProgramDurationMark();
    			}
    		
    		}, 750);
    	}).bind($.jPlayer.event.error,function(event){
    		console.log('Error->');
    		console.log(event);
    	}).bind($.jPlayer.event.warning,function(event){
    		console.log('Warning->');
    		console.log(event);
    	});
    
    $.data($('#jquery_jplayer_1')[0], 'songInfo', songInfo);
    
}
function enableSecondaryTrackControls(songInfo)
{
	var i,
		length,
		fadePct,
		fadeDur;
		
	fadePct = $('.secondaryTrackFadePercentage span');
	fadeDur = $('.secondaryTrackFadeDuration span');
	
	length = fadePct.length - 1;
	
	for(i = 0; i < length; i++)
	{
		$(fadePct[i]).slider('option', 'max', 100);
		$(fadeDur[i]).slider('option', 'max', 5).slider('option', 'value', 4);
	}
}
function controlSecondaryPlayer(secondaryPlayer, advanceTo)
{
	
	$.data(secondaryPlayer[0], 'playing', true);
	
	secondaryPlayer.jPlayer('play', advanceTo);
	/*var media = {},
		waiting;
	
	media[songInfo.fileExt] = '/fileDownload?file_id=' + songInfo.fileKey;
	console.log("advance to->" + advanceTo);
	
	if(!$('#jquery_jplayer_2').data().hasOwnProperty('timeout')){
		$.data($('#jquery_jplayer_2')[0], 'timeout', true);
	}
	else{
		$('#jquery_jplayer_2').data().timeout = true;
	}*/
	
	//$('#jquery_jplayer_1').jPlayer('pause');
		
   // $('#jquery_jplayer_2').jPlayer('setMedia', media)
    					  /*.bind($.jPlayer.event.loadstart, function(event){
    						  
    						  if($('#jquery_jplayer_2').data().jPlayer.status.duration === 0){
	    						  waiting =  setInterval(function(){
	    								
	    								if($('#jquery_jplayer_2').data().jPlayer.status.duration > 0){
	    									clearInterval(waiting);
	    									
	    									$('#jquery_jplayer_2').data().timeout = false;
	    									$('#jquery_jplayer_2').jPlayer('play', advanceTo);
	    									$('#jquery_jplayer_1').jPlayer('play');
	    									
	    								}
	    								else{
	    									$('#jquery_jplayer_1').jPlayer('pause');
	    								}
	    							}, 100);
    						  }else{
    							  
    							  	$('#jquery_jplayer_2').data().timeout = false;
									$('#jquery_jplayer_2').jPlayer('play', advanceTo);
									$('#jquery_jplayer_1').jPlayer('play');
									
    						  }
    						  
    						  $(this).unbind($.jPlayer.event.loadstart);
    					  });*/
    	/*.bind($.jPlayer.event.playing, function(event){    		    	
    		
    		$('#jquery_jplayer_2').data().timeout = false;
    		
    		$(this).unbind($.jPlayer.event.playing);
    	})
    	.jPlayer('play', advanceTo);*/
}
function getSongMetadata(song)
{
	var media = {},
		songInfo,
		waiting,
		interval;
	
	$.data($('#jquery_jplayer_3')[0], 'isBusy', true);
	
	songInfo = song.data().songInfo;
	
	media[songInfo.fileExt] = '/fileDownload?file_id=' + songInfo.fileKey;
	

	$('#jquery_jplayer_3').jPlayer('setMedia', media)
	.bind($.jPlayer.event.loadstart, function(event){
		
		interval = setInterval(function(){
			var secondaryTracks,
				i;
			
			if($('#jquery_jplayer_3').data().jPlayer.status.duration > 0)
			{
				clearInterval(interval);
				$('#jquery_jplayer_3').unbind();							
				
				songInfo.fileDuration = $('#jquery_jplayer_3').data().jPlayer.status.duration;
				
				$('#jquery_jplayer_3').data().isBusy = false;			
			}
		}, 250);
	});
}
function loadMainTrack(songFile)
{
	var tmp,
		songInfo;
	
	tmp = getTrackInfo(songFile.fileName);
	
	songInfo = songFile;
	
	songInfo.fileNaturalName = tmp.fileNaturalName;
	songInfo.fileExt = tmp.fileExt;
	
	$.data($('#jquery_jplayer_1')[0], 'songInfo', songInfo);
	
	controlMainPlayer(songInfo);
	
}
function uploadProgram(state)
{
	var result,
		program,
		mainTrack,
		secondaryTracks,
		slides,
		message = [];
	
	result = prepareProgramUpload(state);
	
	if(result.message !== null){
		
		message.push(result.message);
	}
	
	program = result.program;
	
	result = prepareMainTrackUpload();
	
	if(result.message !== null){
		
		message.push(result.message);
	}
	
	mainTrack = result.mainTrack;
	
	result = prepareSecondaryTracksUpload();
	
	if(result.message !== null){
		
		message.push(result.message);
	}
	
	secondaryTracks = result.secondaryTracks;
	
	result = prepareSlidesUpload();
	
	if(result.message !== null){
		
		message.push(result.message);
	}
	
	slides = result.slides;
	
	if(message.length === 0){
		
		program.mainTrack = mainTrack;
		program.secondaryTracks = secondaryTracks;
		program.slides = slides;
	
		message.push(uploadProgramHelper(program, state));
	
	}
	
	return message;
}
function uploadProgramHelper(program, state)
{
	var message,
	    url;
	
	if(state === 'overwrite'){
		url = '/stationUpload?action=edit&type=program';
	}
	else{
		url = '/stationUpload?action=addJson&type=program';
	}
	
	$.ajax({
		url: url,
	cache: false,
	async: false,
	data: 'jsonobject='+ JSON.stringify(program),
	type: 'POST',
	success: function(result){
		message = result.trim();
	}
	});
	
	return message;
}
function prepareProgramUpload(state)
{
	var channelInfo,
		program = {},
		active,
		result = { program : null, message : null},
		activeProgram;
	
	channelInfo = $('#stationContainer').data().channelInfo;
	
	if(typeof channelInfo === 'undefined' || channelInfo === null){
		
		result.message = $('#noChannels').text().trim();
		
		return result;
	}
	
	if(channelInfo.length === 0){
		
		result.message = $('#fillProgram').text().trim();
		
		return result;
	}
	active = $("#accordion_channel").accordion('option', 'active');
	
	if(active === false){
		
		result.message = $('#selectChannel').text().trim();
		
		return result;
	}
	
	if( $('#programName').val() === null ||  $('#programName').val().trim() === ''){
		
		result.message = $('#fillProgramName').text().trim();
		
		return result;
	}
	
	if(state === 'overwrite'){
		
		activeProgram = $('#accordion_channel div:eq(' + active + ')').data().active_program;
		
		if(activeProgram === null || typeof activeProgram === 'undefined' || !activeProgram.hasOwnProperty('programKey')){
			
			result.message = $('#fillProgram').text().trim();
			
			return result;
		}
			
		program.programKey = activeProgram.programKey; 
	}
	
	program.channelKey = channelInfo[active].key;
	program.programName = $('#programName').val().trim();
	program.programDescription = $('#programDescription').val().trim();
	program.programBanner = $('#programBanner').val().trim();
	program.programSequenceNumber = parseInt($('#programSequenceNumber').val(), 10);
	program.programTotalDurationTime = $('#jquery_jplayer_1').data().programTotalDurationTime === -1 ? $('#jquery_jplayer_1').data().jPlayer.status.duration : $('#jquery_jplayer_1').data().programTotalDurationTime;
	program.programOverlapDuration = 4.0;
	
	result.program = program;
	
	return result;
}
function prepareMainTrackUpload()
{
	var mainTrack = {},
		mainTrackInfo,
		result = { mainTrack : null, message : null};
	
	mainTrackInfo = $('#jquery_jplayer_1').data();
	
	if(typeof mainTrackInfo === 'undefined' || mainTrackInfo === null || typeof mainTrackInfo.songInfo === "undefined"){
		
		result.message = $('#fillMainTrack').text().trim();
		
		return result;
	}
	
	mainTrack.mainTrackType = mainTrackInfo.songInfo.mainTrackType;
	
	if(mainTrack.mainTrackType === 'FILE_UPLOAD'){
		
		mainTrack.stationAudio = mainTrackInfo.songInfo.datastoreObjectKey;
		mainTrack.mainTrackMusicFileKey = 0;
	}
	else if(mainTrack.mainTrackType === 'MUSIC_FILE'){
		
		mainTrack.stationAudio = '';
		
		mainTrack.mainTrackMusicFileKey = mainTrackInfo.songInfo.datastoreObjectKey;
	}
	
	mainTrack.mainTrackPlaylistKey = '';
	mainTrack.mainTrackDuration = mainTrackInfo.jPlayer.status.duration;
	mainTrack.mainTrackFadeInSteps = 4;
	mainTrack.mainTrackFadeInDuration = 4;
	mainTrack.mainTrackFadeInPercentage = 1.0;
	mainTrack.mainTrackFadeOutSteps = 4;
	mainTrack.mainTrackFadeOutDuration = 4;
	mainTrack.mainTrackFadeOutPercentage = 0.25;
	
	result.mainTrack = mainTrack;
	
	return result;
}
function prepareSecondaryTracksUpload()
{
	var secondaryTracks = [],
		secondaryTrackInfo,
		i,
		result = { secondaryTracks : null, message : null};
	
	if($('#secondaryTrackMenuContainer .secondaryTrackRow').length === 1){
		
		//result.message = $('#fillSecondaryTrack').text().trim();
		result.secondaryTracks = [];
		
		return result;
	}
	secondaryTrackInfo = $('#jquery_jplayer_2').data().list;
	
	if(typeof secondaryTrackInfo === 'undefined' || secondaryTrackInfo === null || secondaryTrackInfo.length === 0){
		
		result.message = $('#setSecondaryTrackMark').text().trim();
		
		return result;
	}
	
	for(i = 0; i < secondaryTrackInfo.length; i++){
		
		secondaryTrack = {};
		
		secondaryTrack.secondaryTrackType = secondaryTrackInfo[i].secondaryTrackType;
		
		if(secondaryTrack.secondaryTrackType === 'FILE_UPLOAD'){
			
			secondaryTrack.stationAudio = secondaryTrackInfo[i].datastoreObjectKey;
			secondaryTrack.secondaryTrackMusicFileKey = 0;
			
		}
		else if(secondaryTrack.secondaryTrackType === 'MUSIC_FILE'){
			
			secondaryTrack.stationAudio = '';
			secondaryTrack.secondaryTrackMusicFileKey = secondaryTrackInfo[i].datastoreObjectKey;
		}
		
		secondaryTrack.secondaryTrackStartingTime = secondaryTrackInfo[i].start;
		secondaryTrack.secondaryTrackDuration = secondaryTrackInfo[i].fileDuration;
		secondaryTrack.secondaryTrackFadeInSteps = 4;
		secondaryTrack.secondaryTrackFadeInDuration = secondaryTrackInfo[i].fadeInOutDur;
		secondaryTrack.secondaryTrackFadeInPercentage = 1.0;
		secondaryTrack.secondaryTrackFadeOutSteps = 4;
		secondaryTrack.secondaryTrackFadeOutDuration = secondaryTrackInfo[i].fadeInOutDur;
		secondaryTrack.secondaryTrackFadeOutPercentage = (secondaryTrackInfo[i].fadeInOutPct / 100);
		secondaryTrack.secondaryTrackOffset = 0.0;
		secondaryTrack.secondaryTrackKey = secondaryTrackInfo[i].hasOwnProperty('secondaryTrackKey')? 
				                           secondaryTrackInfo[i].secondaryTrackKey : '';
		
		secondaryTracks.push(secondaryTrack);
	}
	
	result.secondaryTracks = secondaryTracks;
	
	return result;
}
function prepareSlidesUpload()
{
	var slides = [],
		slide,
		slideInfo,
		i,
		result = { slides : null, message : null};
	
	if(Galleria.get(0).getDataLength() === 0){
		
		//result.message = $('#fillSlide').text().trim();
		
		result.slides = [];
		
		return result;
	}
	slideInfo = $('#slidesTimeMarkContainer').data().slidesTimeMarks;
	
	if(typeof slideInfo === 'undefined' || slideInfo === null || slideInfo.length === 0){
		
		result.message = $('#setSlideMark').text().trim();
		
		return result;
	}
	
	for(i = 0; i < slideInfo.length; i++){
		
		slide = {};
		
		slide.stationImage = slideInfo[i].slide.datastoreObjectKey;
		slide.slideStartingTime = slideInfo[i].time;
		slide.slideKey = slideInfo[i].slide.hasOwnProperty('slideKey') && !slideInfo[i].hasOwnProperty('isNewMark') ? slideInfo[i].slide.slideKey : '';
		
		slides.push(slide);
	}
	
	result.slides = slides;
	
	return result;
}
function timeFormat(value)
{
    $.jPlayer.timeFormat.showHour = true;
    $.jPlayer.timeFormat.padHour = true;
    
    return $.jPlayer.convertTime(value);  
}
function filler(template, key, value)
{
    return template.replace('{' + key + '}', value);
}
function removeExt(songFileName)
{
	var index,
		result;
	
	index = songFileName.lastIndexOf('.');	
	
	if(index === -1){
		result = songFileName;					
	}
	else{
		result = songFileName.substring(0, index);
	}
	
	return result;
}
function getTrackInfo(songFileName)
{
	var index,
		result = {};
	
	index = songFileName.lastIndexOf('.');
	
	result.fileNaturalName = songFileName.substring(0, index);
	
	result.fileExt = songFileName.substring(index + 1 , songFileName.length);
	
	return result;
}
function songSearcherOptionsHandler(option)
{
	var handler = {
		'personal' : function(){
			$('#providedLibraryFormContainer').hide();
			$('#personalLibraryFormContainer').toggle();
		},
		'provided' : function(){
			$('#personalLibraryFormContainer').hide();
			$('#providedLibraryFormContainer').toggle();
		}
	};
	
	return handler[option]();
}


function ajaxLoader (el, options, text, position_ref) {
	// Becomes this.options
	var defaults = {
		bgColor 		: '#fff',
		duration		: 400,
		opacity			: 0.7,
		classOveride 	: false
	}
	this.options 	= jQuery.extend(defaults, options);
	this.container 	= $(el);
	
	this.init = function() {
		var container = this.container;
		// Delete any other loaders
		this.remove(); 
		// Create the overlay 
		var overlay = $('<div></div>').css({
				'background-color': this.options.bgColor,
				'opacity':this.options.opacity,
				'width':container.width(),
				'height':container.height(),
				'position':position_ref,
				'top':'0px',
				'left':'0px',
				'z-index':99999
		}).addClass('ajax_overlay'+ text);
		// add an overiding class name to set new loader style 
		if (this.options.classOveride) {
			overlay.addClass(this.options.classOveride);
		}
		// insert overlay and loader into DOM 
		container.append(
			overlay.append(
				$('<div></div>').addClass('ajax_loader'+ text)
			).fadeIn(this.options.duration)
		);
    };
	
	this.remove = function(){
		var overlay = this.container.children(".ajax_overlay" + text);
		if (overlay.length) {
			overlay.fadeOut(this.options.classOveride, function() {
				overlay.remove();
			});
		}	
	}

    this.init();
}
