<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="datastore.Station" %>
<%@ page import="datastore.StationManager" %>
<%@ page import="datastore.User" %>
<%@ page import="util.Printer" %>
<%@ page import="util.Dictionary" %>

<jsp:include page="../header/language-header.jsp" />

<%
	Printer printer = (Printer)session.getAttribute("printer");
	
	User sessionUser = (User)session.getAttribute("user");
	if (sessionUser == null)
	{
		response.sendRedirect("../login.jsp");
		return;
	}
	else {
		if (sessionUser.getUserType() != User.Type.STATION) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	Station station = StationManager.getStation(sessionUser);
	Key stationKey = station.getKey();
	String stationKeyString = KeyFactory.keyToString(stationKey);
	
	String message = request.getParameter("msg");
	String action = request.getParameter("action");
	String updateType = request.getParameter("update_type");
	String error = request.getParameter("etype");

	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="/stylesheets/djInterface.css" />
    <link rel="stylesheet" href="/js/djInterface/UI/redmond/jquery-ui-1.10.3.custom.css" />
    <link rel="stylesheet" href="/js/djInterface/Player/skin/blue.monday/jplayer.blue.monday.css" />
    <script type="text/javascript" src="/js/djInterface/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/js/djInterface/UI/js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="/js/djInterface/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/js/djInterface/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/js/djInterface/Player/jquery.jplayer.min.js"></script>
    <script type="text/javascript" src="/js/djInterface/UI/galleria/galleria-1.2.9.min.js"></script>    
    <script src="/js/djInterface/setup.js"></script>
    <%@  include file="../header/page-title.html"%>
  </head>
  <body>
    <jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />
  
    <div id="container">
      <div id="header" stationKey="<%=stationKeyString%>">
        <h1><%= station.getStationName() %></h1>
      </div>
      <div id="stationContainer">      
        <!--<div id="songSearcherBtnContainer">
          <button id="songSearcherBtn">Search</button>
        </div>-->               
        <div id="puclic_music_lib">
        <h3><%= printer.print("Public Music Library") %></h3>
        <div id="puclic_music_lib_l1"></div>
        </div>      
        
        <div id="accordion_slide">
		<h3><%= printer.print("Station Image") %></h3>
		<div id="accordion_slide_l1"></div>
		</div>       
		
        <div id="accordion_station_music">
        <h3><%= printer.print("Station Music") %></h3>
        <div id="accordion_station_music_l1"></div>
        </div>
        
        <div id="accordion_station_voice">
        <h3><%= printer.print("Station Voice") %></h3>
        <div id="accordion_station_voice_l1"></div>
        </div>				
    	
		<div id="accordion_channel">		
		</div>			
		 
        <div id="songSearcherDialog" title="<%= printer.print("Search For Main Tracks") %>">
          <div id="songSearcherOptions">
          <input type="radio" id="songSearcherOptions1" name="radio" data="personal" checked="checked" />
          <label for="songSearcherOptions1"><%= printer.print("Your files") %></label> 
          <!-- <input type="radio" id="songSearcherOptions2" name="radio" data="provided" />
          <label for="songSearcherOptions2"><%= printer.print("Library") %></label> --></div>
          <div id="personalLibraryFormContainer">            
            <div>
              <span class="btn btn-success fileinput-button">
                <!-- <button id="selectFilesBtn">-->
                <span id="selectFilesBtn" class="btn btn-success fileinput-button">
                <!-- <span  class="ui-icon ui-icon-plusthick"></span>-->
                 Tracks</span> 
                <input id="personalSongBrowser" type="file" name="file" />
                <!-- </button>-->
              </span>
              <span id="mainTrackFileTypeError">
              	<%= printer.print("Invalid file type") %>
              </span>
              </div>
              <!-- <div id="personalSongBtnContainer">
                <button id="personalSongBtn">Upload</button>
              </div>-->
              <div id="mainTrackProgressbar"></div>            
          </div>
          <div id="providedLibraryFormContainer">
            <p>A provided library</p>
          </div>
        </div>
        <div>
        <!-- <ul id="mainTrackMenuContainer"></ul> -->
        </div>
      </div>
      <div id="controlBox">
      	<div id="channelMessage" title="<%= printer.print("Information") %>">
      		<span class="ui-icon ui-icon-alert" style="float: left; margin-top: 10px;margin-right: 10px;"></span><p><%= printer.print("Please create a channel") %></p>
      	</div>
      	<div id="popupMessage" title="<%= printer.print("Information") %>">
      	</div>
		<div id="programForm">
		<div id="validationMessage"></div>
      	<form>
      		<fieldset>
      			<div>
      				<label for="programName"><%= printer.print("Program name") %></label>
      			</div>
      			<div>
      				<input type="text" name="programName" id="programName" tabindex="1" placeholder="<%= printer.print("Program name") %>" class="text ui-widget-content ui-corner-all" />
      			</div>
      			<div>
      				<label for="programDescription"><%= printer.print("Description") %></label>
      			</div>
      			<div>
      				<textarea type="text" name="programDescription" id="programDescription" tabindex="2" placeholder="<%= printer.print("Description") %>" class="text ui-widget-content ui-corner-all" ></textarea>
      			</div>
      			<div>
      				<label for="programSequenceNumber"><%= printer.print("Sequence Number") %></label>
      			</div>
      			<div>
      				<input type="text" name="programSequenceNumber" id="programSequenceNumber" class="text ui-widget-content ui-corner-all" /> 
      			</div>
      		</fieldset>
      	</form>
      </div>
      <div id="selectMessage" title="<%= printer.print("Information") %>">
      	 <span class="ui-icon ui-icon-alert"></span><p><%= printer.print("Please select a channel") %></p>
      </div>
        <div id=refreshControlContainer>  
        <div id="slideContainer">        	
        	<div id='galleria'>        	
				 <!-- <img src='/images/img/1.jpg'/>-->
			</div>			
			<div id="slidesSearcherDialog" title="<%= printer.print("Search For Slides") %>">
            <div id="slidesFormContainer">			
			<div>
              <span class="btn btn-success fileinput-button">                
                <span id="selectSlidesFilesBtn" class="btn btn-success fileinput-button">                
                 <%= printer.print("Slides") %></span> 
                <input id="slidesBrowser" type="file" name="file" multiple="multiple" />                
              </span>
              <span id="slideFileTypeError">
              	<%= printer.print("Invalid file type") %>
              </span>
            </div>
            <!-- <div id="slidesBtnContainer">
                <button id="slidesBtn">Upload</button>
              </div>-->
              <div id="slidesProgressbar"></div>
            </div>
            </div>
            <div id="slideSearcherBtnContainer">
    		<button id="slideSearcherBtn"></button>
    		</div>
            <div id="slidesTimeMarkContainer">
	            <div id="refreshSlidesTimeMarkContainer">
	            </div>
            </div>
            <div id="slidesSubContainer">
            	<div>
            		<button id="slidesEditBtn" title="" unlock= "<%= printer.print("Unlock to edit without playback") %>"
            	 	lock= "<%= printer.print("Lock for playback") %>"></button>
            	 </div>
            	 <div id="programBannerContainer">
            	 	<input type="text" id="programBanner" placeholder="The program banner">
            	 </div>
            </div>
        </div>
        <div id="audioContainer">
          <div id="jquery_jplayer_1" class="jp-jplayer"></div>
          <div id="jp_container_1" class="jp-audio">
            <div class="jp-type-single">
              <div class="jp-gui jp-interface">
                <ul class="jp-controls">
                  <li>
                  	<a href="javascript:;" tabindex="1" id="songSearcherBtn"></a>
                  </li>
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
                  	<a href="javascript:;" tabindex="1" id="slideMarker" title="<%= printer.print("mark slide") %>"></a>
                  </li>
                  <li>
                  	<a href="javascript:;" tabindex="1" id="secondaryTrackMarker" title="<%= printer.print("mark dj recording") %>"></a>
                  </li>
                  <li>
                  	<a href="javascript:;" tabindex="1" id="saveProgramBtn" title="<%= printer.print("Save") %>"></a>
                  </li>
                  <li>
                  	<a href="https://www.facebook.com/sharer/sharer.php?u=" url="https://www.facebook.com/sharer/sharer.php?u=" tabindex="1" id="facebookShare" title="<%= printer.print("Share on Facebook") %>">
                  	<%= printer.print("Share on") %><img src="/images/fb.png"></a>
                  </li>
                  <li id="clientLogoContainer">
                 		 	<a href="javascript:;" id="clientLogo"><img src="/images/clientLogo.png"></a>
                  </li>	
                </ul>
                <div class="jp-progress">
                  <div class="jp-seek-bar">
                    <div class="jp-play-bar"></div>
                    <div class="programDurationMark" title="<%= printer.print("Program Duration Mark") %>"></div>
                  </div>
                </div>
                <div class="jp-volume-bar">
                  <div class="jp-volume-bar-value"></div>
                </div>
                <div class="jp-time-holder">
                  <div class="jp-current-time"></div>
                  <div class="jp-duration"></div>
                  <ul class="jp-toggles">
                    <!-- <li>
                      <a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a>
                    </li>
                    <li>
                      <a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a>
                    </li>
                    -->
                    <li>
                    	<a href="javascript:;" id="mainTrackTitleContainer"></a>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="jp-title">
                <ul>
                  <li id="mainTrackTitleTextContainer"></li>
                </ul>
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
          <div id="jquery_jplayer_3" class="jp-jplayer"></div>
          <div id="jp_container_3" class="jp-audio">
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
            <div id="secondarySearcherBtnContainer">
            <button id="secondarySearcherBtn"></button>
            </div>
            <div id="secondaryTrackTimeMarkContainer">
	            <div id="refreshSecondaryTrackTimeMarkContainer">
	            </div>
            </div>
            <div>
            	<button id="secondaryTrackEditBtn" unlock= "<%= printer.print("Unlock to edit without playback") %>"
            	 lock= "<%= printer.print("Lock for playback") %>"></button>
            </div>
            <div id="trackCollisionMessage" title="Warning"><p>There is a collision detected.</p></div>
            <div id="secondarySearcherDialog" title="<%= printer.print("Search For Secondary Tracks") %>">
            <div id="secondaryFormContainer">     
            <div>
              <span class="btn btn-success fileinput-button">
                <!-- <button id="selectSecondaryFilesBtn">-->
                <span id="selectSecondaryFilesBtn" class="btn btn-success fileinput-button">
                <!-- <span class="ui-icon ui-icon-plusthick"></span>-->
                 <%= printer.print("Tracks") %></span> 
                <input id="secondaryTrackBrowser" type="file" name="file" multiple="multiple" />
                <!-- </button> -->
              </span>
              <span id="secondaryTrackFileTypeError">
              	<%= printer.print("Invalid file type") %>
              </span>
            </div>
              <!-- <div id="secondarySongBtnContainer">
                <button id="secondarySongBtn">Upload</button>
              </div>-->
              <div id="secondaryTrackProgressbar"></div>           
              </div>
              </div>
                <div id="secondaryTrackMenuContainer">
                    <div id=secondaryTrackMenuHeader class="secondaryTrackRow header">
                        <div class="secondaryTrackName"><%= printer.print("Name") %></div>
                        <!--<div class="secondaryTrackDuration">Duration</div>-->
                        <!-- <div class="secondaryTrackPosition">Position</div> -->
                        <div class="secondaryTrackFadePercentage"><%= printer.print("Fade in-out percentage") %></div>
                        <!-- <div class="secondaryTrackFadeStep">Fade in-out step</div>-->
                        <div class="secondaryTrackFadeDuration"><%= printer.print("Fade in-out duration") %></div>
                    </div>
                </div>            
        </div>
        </div>   
      </div>
      <div id="footer">
        <jsp:include page="../header/page-footer.jsp" />
      </div>
    </div>
    <div id="templates">
      <span id="mainTrackTemplate">
       <ul>
        <li>
            <a href="javascript:;">{Track}</a>
        </li>
       </ul>
      </span>
      <span id="secondaryTrackTemplate">
       <div class="secondaryTrackRow">
        <div class="secondaryTrackName"><span>{Name}</span></div>    
        <!--<div class="secondaryTrackPosition"><p class="secondaryTrackStart" title="00:00:00"></p><p class="secondaryTrackEnd" title="00:00:00"></p><span>{Position}</span></div>-->
        <div class="secondaryTrackFadePercentage"><p class="secondaryTrackFadePct" title="1"></p><span>{FadePercentage}</span></div>
        <!--<div class="secondaryTrackFadeStep"><p class="secondaryTrackFadeStep" title="1"></p><span>{FadeStep}</span></div> -->
        <div class="secondaryTrackFadeDuration"><p class="secondaryTrackFadeDuration" title="1"></p><span>{FadeDuration}</span></div>
       </div>
      </span>
      <span id="slideTimeMarkTemplate">
      	<span class="slideTimeMark"></span>
      </span>
      <span id="secondaryTrackTimeMarkTemplate">
      	<span class="secondaryTrackTimeMark"></span>
      </span>
	  <span id="specialMessages">
      	<span id="cancel"><%= printer.print("Cancel") %></span>
      	<span id="newProgram"><%= printer.print("New Program") %></span>
      	<span id="ok"><%= printer.print("Ok") %></span>
      	<span id="createProgram"><%= printer.print("Create") %></span>
      	<span id="overwriteProgram"><%= printer.print("Overwrite") %></span>
      	<span id="fillProgram"><%= printer.print("Fill in the program information") %></span>
      	<span id="fillSlide"><%= printer.print("Fill in the slide information") %></span>
      	<span id="fillMainTrack"><%= printer.print("Fill in the main track information") %></span>
      	<span id="fillSecondaryTrack"><%= printer.print("Fill in the dj recording information") %></span>
      	<span id="programSavedSuccess"><%= printer.print("The program has been saved") %></span>
      	<span id="programSaveFailure"><%= printer.print("There has been a problem saving the program") %></span>
      	<span id="noChannels"><%= printer.print("There are no channels") %></span>
      	<span id="selectChannel"><%= printer.print("Please select a channel") %></span>
      	<span id="setSecondaryTrackMark"><%= printer.print("Please set the mark(s) for the dj recording(s)") %></span>
      	<span id="setSlideMark"><%= printer.print("Please set the mark(s) for the slide(s)") %></span>
      	<span id="fillProgramName"><%= printer.print("Fill in the program name") %></span>
      	<span id="fbComfirmDialog"><%= printer.print("The current channel will be shared on Facebook") %></span>
      </span>
    </div>
  </body>
</html>