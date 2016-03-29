<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>
<%@ page import="datastore.Channel" %>
<%@ page import="datastore.ChannelManager" %>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>

<%

Printer printer = new Printer(Dictionary.Language.CHINESE);
String language = request.getParameter("lang") != null ? request.getParameter("lang") : "CH";
if(language.equalsIgnoreCase("EN")) {
		printer.setLanguage(Dictionary.Language.ENGLISH);
}
else{
		printer.setLanguage(Dictionary.Language.CHINESE);
}

String channelKeyString = request.getParameter("c");
Channel channel = null;
String firstSlideBlobKeyString = null;
String firstSlideUrl = null;

if(!channelKeyString.isEmpty()){
	
	Key channelKey = KeyFactory.stringToKey(channelKeyString);
	
	channel = ChannelManager.getChannel(channelKey);
	
	firstSlideBlobKeyString = ChannelManager.getFirstSlideBlobKey(channel).getKeyString();
	
	firstSlideUrl = request.getRequestURL().substring(0,request.getRequestURL().indexOf(request.getServletPath()));
	
	if(firstSlideBlobKeyString.isEmpty()){
		firstSlideUrl = firstSlideUrl + "/images/smasrvLogo.png";
	}
	else{				
		firstSlideUrl = firstSlideUrl + "/fileDownload?file_id=" + firstSlideBlobKeyString;
	}
}
else{
	
	response.sendRedirect("/login.jsp");
}

%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="/stylesheets/djPlayer.css" />
		<link rel="stylesheet" href="/js/djPlayer/UI/redmond/jquery-ui-1.10.3.custom.css" />
		<link rel="stylesheet" href="/js/djPlayer/Player/skin/blue.monday/jplayer.blue.monday.css" />
		<link rel="stylesheet" href="/js/djPlayer/UI/galleria/themes/classic/galleria.classic.css" />
		<script type="text/javascript" src="/js/djPlayer/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/js/djInterface/UI/js/jquery-ui-1.10.3.custom.js"></script>
		<script type="text/javascript" src="/js/djPlayer/UI/galleria/galleria-1.2.9.min.js"></script>
		<script type="text/javascript" src="/js/djPlayer/Player/jquery.jplayer.min.js"></script>
		<script src="/js/djPlayer/setup.js"></script>
		<meta property="og:title" content="<%= channel.getChannelName() %>" />
		<meta property="og:type" content="music.radio_station" />
		<meta property="og:url" content="<%= request.getRequestURL() + "?" + request.getQueryString() %>" />
		<meta property="og:image" content="<%= firstSlideUrl  %>" />
	</head>
	<body>
		<div id="container">
			<div id="playAgain">
				<div title="<%= printer.print("Click to replay") %>"></div>
				<div id="clickToReplay"><%= printer.print("Click to replay") %></div>
			</div>
			<div id="slideShow">
			</div>
			<div id="banner" class="marquee">
				<div>
					<span></span>
					<span></span>
				</div>			
			</div>
			<div id="mainTrackContainer">
				<div id="jquery_jplayer_1" class="jp-jplayer"></div>				
		          <div id="jp_container_1" class="jp-audio">
		            <div class="jp-type-single">
		              <div class="jp-gui jp-interface">
		                <ul class="jp-controls">
		                  <li>
		                    <a href="javascript:;" class="jp-play" tabindex="1" title="<%= printer.print("play") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-pause" tabindex="1" title="<%= printer.print("pause") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-stop" tabindex="1" title="<%= printer.print("stop") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-mute" tabindex="1" title="<%= printer.print("mute")%>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-unmute" tabindex="1" title="<%= printer.print("unmute") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-volume-max" tabindex="1" title="<%= printer.print("max volume") %>"></a>
		                  </li>	
		                  <li>
                  			<a href="https://www.facebook.com/sharer/sharer.php?u=" url="https://www.facebook.com/sharer/sharer.php?u=" tabindex="1" id="facebookShare1" title="<%= printer.print("Share on Facebook") %>">
                  			<%= printer.print("Share on") %> <img src="/images/fb.png"></a>
                 		 </li>
                 		 <li id="clientLogoContainer1">
                 		 	<a href="javascript:;" id="clientLogo1"><img src="/images/clientLogo.png"></a>
                 		 </li>	                                               
		                </ul>
		                <div class="jp-progress">
		                  <div class="jp-seek-bar">
		                    <div class="jp-play-bar"></div>                 
		                  </div>
		                </div>
		                <div class="jp-volume-bar">
		                  <div class="jp-volume-bar-value"></div>
		                </div>
		                <div class="jp-time-holder">
		                  <div class="jp-current-time"></div>
		                  <div class="jp-duration"></div>
		                  <ul class="jp-toggles">
		                  </ul>
		                </div>
		              </div>
		              <div class="jp-title">
		              </div>
		              <div class="jp-no-solution">
		              <span>Update Required</span> To play the media you will need to either update your browser to a recent version or
		              update your 
		              <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.</div>
		            </div>
		          </div>
		          <div id="jquery_jplayer_1_2" class="jp-jplayer"></div>
		          <div id="jp_container_1_2" class="jp-audio">
		            <div class="jp-type-single">
		              <div class="jp-gui jp-interface">
		                <ul class="jp-controls">
		                  <li>
		                    <a href="javascript:;" class="jp-play" tabindex="1" title="<%= printer.print("play") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-pause" tabindex="1" title="<%= printer.print("pause") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-stop" tabindex="1" title="<%= printer.print("stop") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-mute" tabindex="1" title="<%= printer.print("mute")%>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-unmute" tabindex="1" title="<%= printer.print("unmute") %>"></a>
		                  </li>
		                  <li>
		                    <a href="javascript:;" class="jp-volume-max" tabindex="1" title="<%= printer.print("max volume") %>"></a>
		                  </li>	
		                  <li>
                  			<a href="https://www.facebook.com/sharer/sharer.php?u=" url="https://www.facebook.com/sharer/sharer.php?u=" tabindex="1" id="facebookShare2" title="<%= printer.print("Share on Facebook") %>">
                  			<%= printer.print("Share on") %> <img src="/images/fb.png"></a>
                 		 </li>
                 		 <li id="clientLogoContainer2">
                 		 	<a href="javascript:;" id="clientLogo2"><img src="/images/clientLogo.png"></a>
                 		 </li>		                                               
		                </ul>
		                <div class="jp-progress">
		                  <div class="jp-seek-bar">
		                    <div class="jp-play-bar"></div>                 
		                  </div>
		                </div>
		                <div class="jp-volume-bar">
		                  <div class="jp-volume-bar-value"></div>
		                </div>
		                <div class="jp-time-holder">
		                  <div class="jp-current-time"></div>
		                  <div class="jp-duration"></div>
		                  <ul class="jp-toggles">
		                  </ul>
		                </div>
		              </div>
		              <div class="jp-title">
		              </div>
		              <div class="jp-no-solution">
		              <span>Update Required</span> To play the media you will need to either update your browser to a recent version or
		              update your 
		              <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.</div>
		            </div>
		          </div>
				</div>
				<div id="secondaryTrackContainer">
					<div id="jquery_jplayer_2" class="jp-jplayer"></div>
			          <div id="jp_container_2" class="jp-audio">
			            <div class="jp-type-single">
			              <div class="jp-gui jp-interface">
			                <ul class="jp-controls">
			                  <li>
			                    <a href="javascript:;" class="jp-play" tabindex="1">play</a>
			                  </li>
			                  <li>
			                    <a href="javascript:;" class="jp-pause" tabindex="1">pause</a>
			                  </li>
			                  <li>
			                    <a href="javascript:;" class="jp-stop" tabindex="1">stop</a>
			                  </li>
			                  <li>
			                    <a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a>
			                  </li>
			                  <li>
			                    <a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a>
			                  </li>
			                  <li>
			                    <a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a>
			                  </li>
			                </ul>
			                <div class="jp-progress">
			                  <div class="jp-seek-bar">
			                    <div class="jp-play-bar"></div>
			                  </div>
			                </div>
			                <div class="jp-volume-bar">
			                  <div class="jp-volume-bar-value"></div>
			                </div>
			                <div class="jp-time-holder">
			                  <div class="jp-current-time"></div>
			                  <div class="jp-duration"></div>
			                  <ul class="jp-toggles">
			                    <li>
			                      <a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a>
			                    </li>
			                    <li>
			                      <a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a>
			                    </li>
			                  </ul>
			                </div>
			              </div>
			              <div class="jp-title">
			                <ul>
			                  
			                </ul>
			              </div>
			              <div class="jp-no-solution">
			              <span>Update Required</span> To play the media you will need to either update your browser to a recent version or
			              update your 
			              <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.</div>
			            </div>
          </div>
				</div>
		</div>
		<div id="templates">
			<span id="missingChannelKey"><%= printer.print("There is no channel key defined") %></span>
			<span id="loadProblem"><%= printer.print("Error loading channel") %></span>
		</div>
	</body>
</html>