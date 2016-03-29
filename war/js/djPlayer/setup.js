$(document).ready(function(){
	getInformation();
});
function preSetup(player, selector){
	
	//$("#jquery_jplayer_1").jPlayer('destroy');
	
	
	/*$("#jquery_jplayer_1").jPlayer({
        swfPath: "/js/djPlayer/Player/",
        cssSelectorAncestor: '#jp_container_1',
        solution: 'html,flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:1,
      });
	*/
	player.jPlayer({
        swfPath: "/js/djPlayer/Player/",
        cssSelectorAncestor: selector,
        solution: 'html,flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:1,
      });
}
function getInformation(){
	
	var c,
		information;
	
	c = getParameterByName('c');
	
	if(c === null || c === ''){
		
		$('#container').html($('#missingChannelKey').text().trim());
		
		return;
	}
	
	$.ajax({
		url:'/rest/programs/c=' + c,
		async: true,
		cache: true,
		dataType: 'json',
		type: 'GET',
		success: function(info){
				initialSetup(info);
		},
		error: function(){
			$('#container').html($('#loadProblem').text().trim());
		}});
}
function initialSetup(info)
{
	var container;
	
	if(info.length === 0){
		
		return;
	}
	$('#playAgain div').tooltip();
	
	info.sort(sortByProperty('programSequenceNumber', 'ASC'));

	container = $('#container');
	
	$.data(container[0], 'programs', info);
	
	$.data(container[0], 'activeProgram', info[0]);	
	
	Galleria.loadTheme('/js/djInterface/UI/galleria/themes/classic/galleria.classic.min.js');
	
	buildReadModePlayer();
	
	setFacebookShare();
}
function restartChannel()
{
	var playAgain,
		container,
		mainTrackContainer;
	
	playAgain = $('#playAgain');
	
	playAgain.toggle();
	
	playAgain.bind('click',function(event){
		$(this).unbind();
		container = $('#container');
		mainTrackContainer = $('#mainTrackContainer');
		
		$.data(container[0], 'activeProgram', container.data().programs[0]);
		
		mainTrackContainer.data().activePlayer.jPlayer('destroy');
		mainTrackContainer.data().activePlayer.removeData();
		mainTrackContainer.data().activePlayer.unbind();
		mainTrackContainer.data().waitingPlayer.jPlayer('destroy');
		mainTrackContainer.data().waitingPlayer.removeData();
		mainTrackContainer.data().waitingPlayer.unbind();
		
		$(mainTrackContainer.data()).removeProp('activePlayer');
		$(mainTrackContainer.data()).removeProp('waitingPlayer');
		
		buildReadModePlayer();
		
		$(this).toggle();		
	});
	
}
function setFacebookShare()
{
	var activeProgram,
		facebookShare,
		urlToShare,
		lang;
	
	
	facebookShare = $('#facebookShare1,#facebookShare2');
	
	if(!$('#container').data().hasOwnProperty('activeProgram')){
		
		return;
	}
	
	activeProgram =$('#container').data().activeProgram;
	
	urlToShare = window.location.origin + '/station/djPlayer.jsp?c=' + activeProgram.channelKey;
	
	lang = getParameterByName('lang');
	
	urlToShare = urlToShare + '&lang=' + (lang === '' ? 'EN' : lang);
	
	facebookShare.button().click(function(){
		
		if(urlToShare === null || typeof urlToShare === 'undefined'){
			return;
		}
		
		window.open(facebookShare.attr('url') + encodeURIComponent(urlToShare), 
			      'facebook-share-dialog', 
			      'width=626,height=436');
	});
		
}
function buildReadModePlayer()
{
	setGalleria();
	setBanner();
	setSecondaryTrackExtraInfo();
	setMainPlayer();
	setSecondaryTrackPlayer();
}
function setGalleria()
{
	var activeProgram,
		images = [],
		image,
		position;
	
	activeProgram = $('#container').data().activeProgram;
	
	if(activeProgram.slides.length === 0){
		
		if(activeProgram.stationLogo.keyString === ''){
			image = '/images/smasrvLogo.png';
		}
		else{
			image = '/img?blobkey=' + activeProgram.stationLogo.keyString;
		}
		
		images.push({image : image});
		
		Galleria.run('#slideShow', {
			thumbnails:false,
			dataSource:images
		});
		return;
	}
	
	activeProgram.slides.sort(sortByProperty('slideStartingTime', 'ASC'));
	
	$.each(activeProgram.slides,function(index, value){
		
		image = '/img?blobkey=' + value.stationImageBlobKey.keyString;
		
		position = checkRepetition(images, image, 'image');
		
		if(position === -1){
			images.push({ image : image, stationImageBlobKey : value.stationImageBlobKey });
			activeProgram.slides[index].position = images.length - 1;
		}
		else{
			activeProgram.slides[index].position = position;
		}
	});
	
	
	$.data($('#slideShow')[0], 'activeSlide', activeProgram.slides[0].position);
	
	activeProgram.slides.sort(sortByProperty('slideStartingTime', 'DES'));
	
	Galleria.configure({
		debug: false,
		showInfo: false
	});
	
	Galleria.run('#slideShow',{
		extend:function(){
			
		},
		thumbnails:false,
		dataSource: images
	});
		
}
function setBanner()
{
	var activeProgram;
	
	activeProgram = $('#container').data().activeProgram;
	
	$('#banner span').text(activeProgram.programBanner);
}
function setMainPlayer()
{
	var container,
		activePlayer,
		waitingPlayer,
		activeProgram,
		song = {},
		media = {},
		nextProgram,
		tmp,
		selector;
	
	container = $('#mainTrackContainer');
	
	activeProgram = $('#container').data().activeProgram;
	
	if(container.data().hasOwnProperty('waitingPlayer')){
		
		activePlayer = container.data().waitingPlayer;
					
		if(activePlayer[0].id === $('#jquery_jplayer_1')[0].id){
			waitingPlayer = $('#jquery_jplayer_1_2');
			selector = '#jp_container_1_2';
			$('#jp_container_1').show();
		}
		else{
			waitingPlayer = $('#jquery_jplayer_1');
			selector = '#jp_container_1';
			$('#jp_container_1_2').show();
		}
		
		waitingPlayer.jPlayer('destroy');
		waitingPlayer.removeData();
		waitingPlayer.unbind();
		
		preSetup(waitingPlayer, selector);
		
		$(selector).hide();
		
		$(selector +' li:gt(0)').tooltip();
		
		$(selector + ' .jp-duration').text(timeFormat($('#container').data().activeProgram.programTotalDurationTime));
		
		//activePlayer.jPlayer('volume', 0);
		
		activePlayer.jPlayer('play');
		//console.log('play->' + activePlayer[0].id);
		$.data(container[0], 'activePlayer', activePlayer);
		$.data(container[0], 'waitingPlayer', waitingPlayer);
		
		
		nextProgram = getNextProgram();
		
		if(nextProgram !== null){
			
			song = GetAudioNameFormat(nextProgram.mainTrack.mainTrackType, nextProgram.mainTrack, 'main');
			
			media = {};
			
			media[song.fileExt] = '/audioStreaming?file_id=' + song.fileKey;
			
			waitingPlayer.jPlayer('setMedia', media)
			  .bind($.jPlayer.event.loadstart, function(event){
				  $.data(event.currentTarget, 'originalVolume', 1);
			  })
			  .bind($.jPlayer.event.seeking, function(event){
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
		          controlSeekingtimePlayer(event);          
		      })
			  .bind($.jPlayer.event.timeupdate, function(event){
				  
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
				  
				  controlPlaytime(event);
			  })
			  .bind($.jPlayer.event.pause, function(event){
		    	  
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
				  pauseSecondaryPlayer(event);
		      });
		}
		
	}
	else{
		activePlayer = $('#jquery_jplayer_1');
		waitingPlayer = $('#jquery_jplayer_1_2');
		
		preSetup(activePlayer, '#jp_container_1');
		preSetup(waitingPlayer, '#jp_container_1_2');
		
		$.data(container[0], 'activePlayer', activePlayer);
		$.data(container[0], 'waitingPlayer', waitingPlayer);
		
		$('#jp_container_1').show();
		$('#jp_container_1_2').hide();
		
		song = GetAudioNameFormat(activeProgram.mainTrack.mainTrackType, activeProgram.mainTrack, 'main');
		
		media[song.fileExt] = '/audioStreaming?file_id=' + song.fileKey;
			
		nextProgram = getNextProgram();

		activePlayer.jPlayer('setMedia', media)
		  .bind($.jPlayer.event.loadstart, function(event){		
				  $.data(event.currentTarget, 'originalVolume', 1);
			  })
		  .jPlayer('play')
		  .bind($.jPlayer.event.seeking, function(event){
			  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
				  return;
			  }
	          controlSeekingtimePlayer(event);          
	      })
		  .bind($.jPlayer.event.timeupdate, function(event){
			  
			  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
				  return;
			  }
			  
			  controlPlaytime(event);
		  })
		  .bind($.jPlayer.event.pause, function(event){
			  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
				  return;
			  }
			  pauseSecondaryPlayer(event);
	      });
		
		if(nextProgram !== null){
			
			song = GetAudioNameFormat(nextProgram.mainTrack.mainTrackType, nextProgram.mainTrack, 'main');
			
			media = {};
			
			media[song.fileExt] = '/audioStreaming?file_id=' + song.fileKey;
			
			waitingPlayer.jPlayer('setMedia', media)
			  .bind($.jPlayer.event.loadstart, function(event){				
				  $.data(event.currentTarget, 'originalVolume', 1);
			  })
			  .bind($.jPlayer.event.seeking, function(event){
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
				  controlSeekingtimePlayer(event);          
		      })
			  .bind($.jPlayer.event.timeupdate, function(event){
				  
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
				  
				  controlPlaytime(event);
			  })
			  .bind($.jPlayer.event.pause, function(event){
				  if(event.currentTarget.id !== $('#mainTrackContainer').data().activePlayer[0].id){
					  return;
				  }
				  pauseSecondaryPlayer(event);
		      });
		}
		
		$('#jp_container_1 li:gt(0)').tooltip();
		
		$('#jp_container_1 .jp-duration').text(timeFormat($('#container').data().activeProgram.programTotalDurationTime));
	}
		
	/*	
	song = GetAudioNameFormat(activeProgram.mainTrack.mainTrackType, activeProgram.mainTrack, 'main');
	
	media[song.fileExt] = '/fileDownload?file_id=' + song.fileKey;
		
	nextProgram = getNextProgram();
		
	preSetup(activePlayer, waitingPlayer);
	*/
	/*
	$('#jquery_jplayer_1').jPlayer('setMedia', media)
						  .jPlayer('play')
						  .bind($.jPlayer.event.seeking, function(event){
					          controlSeekingtimePlayer(event);          
					      })
						  .bind($.jPlayer.event.timeupdate, function(event){
							  controlPlaytime(event);
						  })
						  .bind($.jPlayer.event.pause, function(event){
					    	  pauseSecondaryPlayer(event);
					      });
	*/
	
	
	/*$('#jp_container_1 li:gt(0)').tooltip();
	
	$('#jp_container_1 .jp-duration').text(timeFormat($('#container').data().activeProgram.programTotalDurationTime));*/
	
}
function setSecondaryTrackPlayer()
{
	$('#jquery_jplayer_2').jPlayer('destroy');
	
     $("#jquery_jplayer_2").jPlayer({
        swfPath: "JS/Player/",
        cssSelectorAncestor: '#jp_container_2',
        solution: 'html, flash',
        supplied: 'mp3, m4a, oga, webm',
        volume:1
      });
     
     //$.data($('#jquery_jplayer_1')[0], 'originalVolume', 1);
}
function setSecondaryTrackExtraInfo()
{
	var activeProgram,
		i,
		extraInfo;
	
	activeProgram = $('#container').data().activeProgram;
	
	if(activeProgram === null || typeof activeProgram === 'undefined'){
		return;
	}
	
	for(i = 0; i < activeProgram.secondaryTracks.length; i++){
		
		extraInfo = GetAudioNameFormat(activeProgram.secondaryTracks[i].secondaryTrackType, activeProgram.secondaryTracks[i], 'secondary');
		
		activeProgram.secondaryTracks[i].fileKey = extraInfo.fileKey;
		activeProgram.secondaryTracks[i].fileExt = extraInfo.fileExt;
		activeProgram.secondaryTracks[i].fileNaturalName = extraInfo.fileNaturalName;
	}
}
function getNextProgram()
{
	var container,
		index;
	
	container = $('#container');
	
	if(!container.data().hasOwnProperty('programs')){
		return null;
	}
	
	index = container.data().programs.indexOf(container.data().activeProgram);
	
	if(index + 1 < container.data().programs.length){
		return container.data().programs[index + 1];
	}
	else{
		return null;
	}
}
function controlPlaytime(event){
	var mainPlayer,
		message,
		nextProgram,
		container;
	
	
	mainPlayer = $(event.currentTarget);
	
	playSlide(mainPlayer);
	
	message = playSecondaryTrack(mainPlayer);
	
	/*if(message === 'fading'){
		controlMainTrackFading(mainPlayer);
	}
	else */
	
	/*container = $('#mainTrackContainer');
	
	if(message === 'mainFadeStarted'){
		
		nextProgram = getNextProgram();
		
		if(nextProgram !== null){
			$.data($('#container')[0], 'activeProgram', nextProgram);
			buildReadModePlayer();
			
			controlMainTrackFading(container.data().waitingPlayer, container.data().activePlayer);
		}
	}
	else if(message === 'mainFadeContinued'){
		controlMainTrackFading(container.data().waitingPlayer, container.data().activePlayer);
	}
	else */if(message === 'endProgram'){
		
		nextProgram = getNextProgram();
		
		if(nextProgram !== null){
			$.data($('#container')[0], 'activeProgram', nextProgram);
			buildReadModePlayer();
		}
		else{
			mainPlayer.jPlayer('stop');
			
			if(event.jPlayer.status.currentTime > 0){
				restartChannel();
			}
		}
		
	}
}
function stopMainPlayer()
{
	$('#jquery_jplayer_1').jPlayer('stop');
}
function controlMainTrackFading(activePlayer, waitingPlayer)
{
	var newVolume;
	
	newVolume = getFadeOutVolumeForMainTrack(activePlayer);
	
	activePlayer.jPlayer('volume', newVolume);
	
	newVolume = getFadeInVolumeForMainTrack(waitingPlayer);
	
	waitingPlayer.jPlayer('volume', newVolume);
	
	/*if(waitingPlayer.data().jPlayer.status.paused){
		waitingPlayer.jPlayer('play');
	}*/
}
function playSecondaryTrack(mainPlayer)
{
	var secondaryPlayer,
		secondaryTracks,
		i,
		currentTime,
		flagStart,
		flagEnd,
		newVolume,
		waitingTimeForFadeIn = 3,
		programTotalDurationTime;
	
	if(!$('#container').data().hasOwnProperty('activeProgram') || !$('#container').data().activeProgram.hasOwnProperty('secondaryTracks')){
		
		return 'noInfo';
	}
	
	programTotalDurationTime = Math.floor($('#container').data().activeProgram.programTotalDurationTime);
		
	secondaryPlayer = $('#jquery_jplayer_2');
	
	secondaryTracks = $('#container').data().activeProgram.secondaryTracks;
	
	currentTime = mainPlayer.data().jPlayer.status.currentTime;
	
	/*if(programTotalDurationTime - currentTime <= $('#container').data().activeProgram.programOverlapDuration &&
	   programTotalDurationTime - currentTime > 0){
		
		if(!$('#mainTrackContainer').data().mainFadeStarted){
			$.data($('#mainTrackContainer')[0], 'mainFadeStarted', true);
			
			return 'mainFadeStarted';
		}
		else{
			$.data($('#mainTrackContainer')[0], 'mainFadeStarted', false);
			
			return 'mainFadeContinued';
		}
		
	}
	else if(programTotalDurationTime - currentTime <= 0){
	else */if(currentTime > programTotalDurationTime){
		
		return 'endProgram';
	}
	
	for(i = 0; i < secondaryTracks.length; i++){
		
		secondaryTracks[i].secondaryTrackEndingTime = secondaryTracks[i].secondaryTrackStartingTime + secondaryTracks[i].secondaryTrackDuration;
		
		flagStart = currentTime >= secondaryTracks[i].secondaryTrackStartingTime;
		flagEnd = currentTime <= secondaryTracks[i].secondaryTrackEndingTime;
		
		if(secondaryPlayer.data().hasOwnProperty('songInfo') && secondaryTracks[i].fileKey === secondaryPlayer.data().songInfo.fileKey && flagStart && !flagEnd){
			secondaryPlayer.data().timeout = false;
		}
		
		if(flagEnd && secondaryPlayer.data().jPlayer.status.paused && !secondaryPlayer.data().timeout){
			console.log('preloading, currentTime->', currentTime, ' secondaryEnd->', secondaryTracks[i].secondaryTrackEndingTime);
			preloadSecondaryTrack(mainPlayer, secondaryPlayer,secondaryTracks[i], flagStart && flagEnd);
		}
		else
		//if((secondaryPlayer.data().jPlayer.status.paused && !secondaryPlayer.data().timeout) &&
		if(secondaryPlayer.data().jPlayer.status.paused && flagStart && flagEnd && !mainPlayer.data().jPlayer.status.paused){
			console.log('playing, currentTime->', currentTime, ' secondaryEnd->', secondaryTracks[i].secondaryTrackEndingTime,
					    ' at->', getAproxTime(currentTime) - getAproxTime(secondaryTracks[i].secondaryTrackStartingTime));
			controlSecondaryPlayer(mainPlayer, secondaryPlayer, 
					               getAproxTime(currentTime) - getAproxTime(secondaryTracks[i].secondaryTrackStartingTime)
					              );
			
			break;
		}
		else if(!secondaryPlayer.data().jPlayer.status.paused &&
				flagStart && flagEnd &&
				mainPlayer.data().jPlayer.options.volume !== (mainPlayer.data().originalVolume * secondaryTracks[i].secondaryTrackFadeOutPercentage)){
			
			newVolume = getFadeOutVolume(secondaryTracks[i], mainPlayer);
			console.log('fade out new volume->', newVolume, ' time->', currentTime);
			if(newVolume >= (mainPlayer.data().originalVolume * secondaryTracks[i].secondaryTrackFadeOutPercentage)){
				
				mainPlayer.jPlayer('volume', newVolume);
				
				$.data(mainPlayer[0], 'fadeOutBy', secondaryTracks[i]);
								
			}
			break;
		}
		else if(!flagEnd && 
			     Math.floor(mainPlayer.data().jPlayer.status.currentTime) <= (secondaryTracks[i].secondaryTrackEndingTime + secondaryTracks[i].secondaryTrackFadeOutDuration) &&
			     (i === secondaryTracks.length - 1 || secondaryTracks[i+1].secondaryTrackStartingTime - Math.floor(currentTime) >= waitingTimeForFadeIn)){
			
			newVolume = getFadeInVolume(secondaryTracks[i], mainPlayer);
			console.log('fade in new volume->' + newVolume + ' time->'+currentTime);
			secondaryPlayer.data().timeout = false;
			if(newVolume <= mainPlayer.data().originalVolume){
				
				mainPlayer.jPlayer('volume', newVolume);
			}
			break;
		}
		else if(mainPlayer.data().hasSeeked){
			
			if(flagStart && flagEnd){
			
				mainPlayer.jPlayer('volume', mainPlayer.data().originalVolume * secondaryTracks[i].secondaryTrackFadeOutPercentage);
				
				secondaryPlayer.jPlayer('play', getAproxTime(currentTime) - getAproxTime(secondaryTracks[i].secondaryTrackStartingTime));
				console.log('has seeked inside in an already loaded song->' + (secondaryPlayer.data().jPlayer.status.src === '/audioStreaming?file_id=' + secondaryTracks[i].fileKey));
				//controlSecondaryPlayer(mainPlayer, secondaryTracks[i], getAproxTime(currentTime) - getAproxTime(secondaryTracks[i].secondaryTrackStartingTime));
								
			}
			else if(isOutsideAnySecondaryTrack(i, secondaryTracks, currentTime)){
				
				mainPlayer.jPlayer('volume', mainPlayer.data().originalVolume);
				
				secondaryPlayer.jPlayer('stop');
			}
			
			mainPlayer.data().hasSeeked = false;
		}
	}
	
	return 'continueProgram';
}
function preloadSecondaryTrack(mainPlayer, secondaryPlayer, songInfo, insideSecondaryTrack)
{
	var media = {},
		waiting,
		songInfoTmp,
		flagStart,
		flagEnd;
	
	media[songInfo.fileExt] = '/audioStreaming?file_id=' + songInfo.fileKey;
	console.log('preload');
	if(media[songInfo.fileExt] === secondaryPlayer.data().jPlayer.status.src && secondaryPlayer.data().jPlayer.status.duration > 0){
		return;
	}
	console.log('preload, and is inside secondary track playtime:', insideSecondaryTrack);
	if(!secondaryPlayer.data().hasOwnProperty('timeout')){
		$.data(secondaryPlayer[0], 'timeout', true);
	}
	else{
		secondaryPlayer.data().timeout = true;
	}
	
	$.data(secondaryPlayer[0], 'songInfo', songInfo);
	
	/*if(insideSecondaryTrack){
		mainPlayer.jPlayer('pause');
	}*/
	
	secondaryPlayer.jPlayer('setMedia', media)
				   .bind($.jPlayer.event.loadstart, function(event){
					   
					   songInfoTmp = $(event.currentTarget).data().songInfo;
					   
					   flagStart = $('#mainTrackContainer').data().activePlayer.data().jPlayer.status.currentTime >= songInfoTmp.secondaryTrackStartingTime;
					   flagEnd = $('#mainTrackContainer').data().activePlayer.data().jPlayer.status.currentTime <= songInfoTmp.secondaryTrackEndingTime;
						
					   //if(flagStart && flagEnd){
						   
						   /*if(event.jPlayer.status.duration === 0){
							   
							   waiting = setInterval(function(){
								   
								   if($(event.currentTarget).data().jPlayer.status.duration > 0){
									   clearInterval(waiting);
									   
									   //$(event.currentTarget).data().timeout = false;
									   console.log('timeout->' + $('#jquery_jplayer_2').data().timeout, ' inside=', flagStart && flagEnd);
									   if(flagStart && flagEnd){
										   									   
									   $(event.currentTarget).jPlayer('play', 
											                          getAproxTime($('#mainTrackContainer').data().activePlayer.data().jPlayer.status.currentTime
											                        		       - songInfoTmp.secondaryTrackStartingTime));
									   
									   $('#mainTrackContainer').data().activePlayer.jPlayer('play');
									   
									   }
								   }
							   },100);
						   }
						   else */{
							   
							   //$(event.currentTarget).data().timeout = false;
							   
							   if(flagStart && flagEnd){
								   
							   $(event.currentTarget).jPlayer('play', 
				                          getAproxTime($('#mainTrackContainer').data().activePlayer.data().jPlayer.status.currentTime 
				                        		       - songInfoTmp.secondaryTrackStartingTime));
		   
							   $('#mainTrackContainer').data().activePlayer.jPlayer('play');
							   }
						   }
					   					   
					   
					   $(event.currentTarget).unbind($.jPlayer.event.loadstart);
				   });
}
function controlSecondaryPlayer(mainPlayer, secondaryPlayer, advanceTo)
{
	
	var waiting;
	
	/*if(secondaryPlayer.data().jPlayer.status.duration === 0){
		mainPlayer.jPlayer('pause');
		
		waiting = setInterval(function(){
			
			if(secondaryPlayer.data().jPlayer.status.duration > 0){
				clearInterval(waiting);
				
				secondaryPlayer.jPlayer('play', advanceTo);
				mainPlayer.jPlayer('play');
			}
		},100);
	}
	else*/ {
		secondaryPlayer.jPlayer('play', advanceTo);
	}
	/*var media = {},
		waiting;
		
	media[songInfo.fileExt] = '/fileDownload?file_id=' + songInfo.fileKey;
	
	if(media[songInfo.fileExt] === $('#jquery_jplayer_2').data().jPlayer.status.src && $('#jquery_jplayer_2').data().jPlayer.status.duration > 0){
		return;
	}
	
	if(!$('#jquery_jplayer_2').data().hasOwnProperty('timeout')){
		$.data($('#jquery_jplayer_2')[0], 'timeout', true);
	}
	else{
		$('#jquery_jplayer_2').data().timeout = true;
	}
	*/
	//console.log('jump to->',advanceTo);
	//$('#jquery_jplayer_1').jPlayer('pause');
	//console.log(mainPlayer[0].id);
	/*mainPlayer.jPlayer('pause');*/
	
// $('#jquery_jplayer_2').jPlayer('setMedia', media)
    					  /*.bind($.jPlayer.event.playing, function(event){    		    	
    		
    		$('#jquery_jplayer_2').data().timeout = false;
    		
    		$(this).unbind($.jPlayer.event.playing);
    	})
    	.jPlayer('play', advanceTo)*/
    		/*			  .bind($.jPlayer.event.loadstart, function(event){    						  
    						  if($('#jquery_jplayer_2').data().jPlayer.status.duration === 0){
	    						  waiting =  setInterval(function(){
	    								
	    								if($('#jquery_jplayer_2').data().jPlayer.status.duration > 0){
	    									clearInterval(waiting);
	    									
	    									$('#jquery_jplayer_2').data().timeout = false;
	    									$('#jquery_jplayer_2').jPlayer('play', advanceTo);
	    									//$('#jquery_jplayer_1').jPlayer('play');
	    									mainPlayer.jPlayer('play');
	    									
	    								}
	    								else{
	    									//$('#jquery_jplayer_1').jPlayer('pause');
	    									mainPlayer.jPlayer('pause');
	    								}
	    							}, 100);
    						  }else{
    							  
    							  	$('#jquery_jplayer_2').data().timeout = false;
									$('#jquery_jplayer_2').jPlayer('play', advanceTo);
									//$('#jquery_jplayer_1').jPlayer('play');
									mainPlayer.jPlayer('play');
									
    						  }
    						  
    						  $(this).unbind($.jPlayer.event.loadstart);
    					  });*/
}
function pauseSecondaryPlayer(event)
{	
	$('#jquery_jplayer_2').jPlayer('pause');
}
function controlSeekingtimePlayer(event)
{
	$.data(event.currentTarget, 'hasSeeked', true);
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
function getFadeOutVolume(secondaryTrack, mainPlayer)
{
	var aproxTime,
		currentVolume,
		tick,
		newVolume;
	
	aproxTime = getAproxTime(mainPlayer.data().jPlayer.status.currentTime);
	
	currentVolume = mainPlayer.data().jPlayer.options.volume;
	
	tick = parseInt((secondaryTrack.secondaryTrackFadeOutDuration * 4) - (aproxTime - secondaryTrack.secondaryTrackStartingTime) / 0.250, 10);
	
	if(tick > 0){
	
		newVolume = currentVolume - ((currentVolume - 
			    	mainPlayer.data().originalVolume * secondaryTrack.secondaryTrackFadeOutPercentage) / tick);
	
		newVolume = Math.round(newVolume * 100) / 100;
	}
	else if(currentVolume > secondaryTrack.secondaryTrackFadeOutPercentage){
		
		newVolume = secondaryTrack.secondaryTrackFadeOutPercentage;
	}
	else{
		newVolume = -1;
	}
	return newVolume;
}
function getFadeOutVolumeForMainTrack(activePlayer)
{
	var activeProgram,
	aproxTime,
	currentVolume,
	tick,
	newVolume;

	activeProgram = $('#container').data().activeProgram;
	
	aproxTime = getAproxTime(activePlayer.data().jPlayer.status.currentTime);
	
	currentVolume = activePlayer.data().jPlayer.options.volume;
	
	tick = parseInt((activeProgram.programOverlapDuration * 4) - (aproxTime) / 0.250, 10);
	
	if(tick > 0){
	
		newVolume = currentVolume - ((currentVolume) / tick);
	
		newVolume = Math.round(newVolume * 100) / 100;
	}
	else{
		newVolume = -1;
	}
	return newVolume;
}
function getFadeInVolumeForMainTrack(waitingPlayer)
{
	var nextProgram,
	aproxTime,
	currentVolume,
	tick,
	newVolume;

	nextProgram = getNextProgram();
	
	if(nextProgram === null){
		return 2;
	}
	
	aproxTime = getAproxTime(waitingPlayer.data().jPlayer.status.currentTime);

	currentVolume = waitingPlayer.data().jPlayer.options.volume;

	tick = parseInt((nextProgram.programOverlapDuration * 4) - (aproxTime) / 0.250, 10);
	
	if(tick > 0){
		newVolume = currentVolume - ((currentVolume) / tick);

		newVolume = Math.round(newVolume * 100) / 100;
	}
	else{
		newVolume = 2;
	}

	return newVolume;
}
function getFadeInVolume(secondaryTrack, mainPlayer)
{
	var aproxTime,
	currentVolume,
	tick,
	newVolume;

	aproxTime = getAproxTime(mainPlayer.data().jPlayer.status.currentTime);

	currentVolume = mainPlayer.data().jPlayer.options.volume;

	tick = parseInt((secondaryTrack.secondaryTrackFadeInDuration * 4) - (aproxTime - secondaryTrack.secondaryTrackEndingTime) / 0.250, 10);
	
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
function getAproxTime(currentTime)
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
function playSlide(mainPlayer){
	var container,
		slidesTimeMarks,
		i,
		galleria;
	
	container = $('#container').data();
	
	if(!container.hasOwnProperty('activeProgram') || !container.activeProgram.hasOwnProperty('slides')){
		return;
	}
	if(!$('#slideShow').data().hasOwnProperty('activeSlide')){
		
		$.data($('#slideShow')[0], 'activeSlide', 0);
	}
	slidesTimeMarks = container.activeProgram.slides;	
	
	galleria = Galleria.get(0);
	
	for(i = 0; i < slidesTimeMarks.length; i++){
		
		if(mainPlayer.data().jPlayer.status.currentTime >= slidesTimeMarks[i].slideStartingTime){
			if(slidesTimeMarks[i].position !== $('#slideShow').data().activeSlide){
				
				$.data($('#slideShow')[0], 'activeSlide', slidesTimeMarks[i].position);
				
				Galleria.get(0).show(slidesTimeMarks[i].position);			
			}
			
			break;
		}
	}
}
function GetAudioNameFormat(uploadType, track, trackType){
	
	var handler = {},
		result = {},
		kParam,
		fileKey;
	
	
	handler['FILE_UPLOAD'] = function(){
		
		if(trackType === 'main'){
			kParam = track.stationAudio;
			fileKey = track.stationAudioBlobKey.keyString;
		}
		else if(trackType === 'secondary'){
			kParam = track.stationAudio;
			fileKey = track.stationAudioBlobKey.keyString;
		}
		
		$.ajax({
			url: '/rest/stationAudio/k=' + kParam,
			async: false,
			cache: true,
			dataType:  'json',
			type: 'GET',
			success: function(json){
				
				var index;
				
				index = json[0].stationAudioName.lastIndexOf('.');
				
				if(index === -1){
					result.fileNaturalName = json[0].stationAudioName;
					result.fileExt = json[0].stationAudioFormat;
					result.fileKey = fileKey;
				}
				else {
					result.fileNaturalName = json[0].stationAudioName.substring(0, index);
					result.fileExt = json[0].stationAudioFormat;
					result.fileKey = fileKey;
				}

			}
		});
	};
	
	handler['MUSIC_FILE'] = function(){
		
		if(trackType === 'main'){
			kParam = track.mainTrackMusicFileKey;
			fileKey = track.mainTrackMusicFileBlobKey.keyString;
		}
		else if(trackType === 'secondary'){
			kParam = track.secondaryTrackMusicFileKey;
			fileKey = track.secondaryTracMusicFileBlobKey.keyString;
		}
		
		$.ajax({
			url: '/rest/musicLibrary/k=' + kParam,
			async: false,
			cache: true,
			dataType:  'json',
			type: 'GET',
			success: function(json){
				
				var index;
				
				index = json[0].musicFileTitle.lastIndexOf('.');
				
				if(index === -1){
					result.fileNaturalName = json[0].musicFileTitle;
					result.fileExt = json[0].musicFileFormat;
					result.fileKey = fileKey;
				}
				else {
					result.fileNaturalName = json[0].musicFileTitle.substring(0, index);
					result.fileExt = json[0].musicFileFormat;
					result.fileKey = fileKey;
				}
			}
		});
	};

	handler[uploadType]();
	
	return result;
}
function checkRepetition(array, value, property){
	
	return array.map(checkRepetitionHelper(property)).indexOf(value);
}
function checkRepetitionHelper(property)
{
	
	return function(value)
	{ 
		return value[property];
	}
}
function sortByProperty(property, order)
{
	return function(obj1, obj2){
		
		if(obj1[property] < obj2[property]){
			return order === 'ASC' ? -1 : 1;
		}
		else
		if(obj1[property] > obj2[property]){
			return order === 'ASC' ? 1 : -1;
		}
	
		return 0;
	
	}
}
function timeFormat(value)
{
    $.jPlayer.timeFormat.showHour = false;
    $.jPlayer.timeFormat.padHour = true;
    
    return $.jPlayer.convertTime(value);  
}
function getParameterByName(name) {
    var regex,
    	results;
    
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    
	regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
	
    results = regex.exec(location.search);
    
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}