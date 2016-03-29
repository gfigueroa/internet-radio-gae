<%@page import="datastore.MainTrack.MainTrackType"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	errorPage=""%>
<%@ page import="java.util.List"%>
<%@ page import="datastore.MusicFile"%>
<%@ page import="datastore.MusicFileManager"%>
<%@ page import="datastore.MainTrack"%>
<%@ page import="datastore.Playlist"%>
<%@ page import="datastore.PlaylistManager"%>
<%@ page import="datastore.SecondaryTrack"%>
<%@ page import="datastore.SecondaryTrackManager"%>
<%@ page import="datastore.Slide"%>
<%@ page import="datastore.SlideManager"%>
<%@ page import="datastore.StationAudio"%>
<%@ page import="datastore.StationAudioManager"%>
<%@ page import="datastore.StationImage"%>
<%@ page import="datastore.StationImageManager"%>
<%@ page import="datastore.Region"%>
<%@ page import="datastore.RegionManager"%>
<%@ page import="datastore.Program"%>
<%@ page import="datastore.ProgramManager"%>
<%@ page import="datastore.Station"%>
<%@ page import="datastore.StationManager"%>
<%@ page import="datastore.User"%>
<%@ page import="datastore.UserManager"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobKey"%>
<%@ page import="com.google.appengine.api.blobstore.BlobInfoFactory"%>
<%@ page import="util.GeoCoder"%>
<%@ page import="util.Location"%>
<%@ page import="util.GeocodeResponse"%>
<%@ page import="util.Printer"%>
<%@ page import="util.Dictionary"%>

<%
	User sessionUser = (User)session.getAttribute("user");
  	if (sessionUser == null)
    	response.sendRedirect("../login.jsp");
  	else {
	  	if (sessionUser.getUserType() != User.Type.ADMINISTRATOR && sessionUser.getUserType() != User.Type.STATION) {
	  		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	  	}
  	}
  
  	BlobInfoFactory bif = new BlobInfoFactory();
  	
  	boolean readOnly = request.getParameter("readonly") != null ? true : false;
  	String error = request.getParameter("etype");
  	String message = request.getParameter("msg");
  	String action = request.getParameter("action");

  	String programKeyString = request.getParameter("k");
  	Key programKey = KeyFactory.stringToKey(programKeyString);
  	Program program = ProgramManager.getProgram(programKey);
  	
  	MainTrack mainTrack = program.getMainTrack();
  	List<SecondaryTrack> secondaryTracks = SecondaryTrackManager.getAllSecondaryTracksFromProgram(programKey, true);
  	List<Slide> slides = SlideManager.getAllSlidesFromProgram(programKey, true);
  	
  	Station station = StationManager.getStation(programKey.getParent().getParent());
%>

<%
	Printer printer = (Printer)session.getAttribute("printer");
%>
<jsp:include page="../header/language-header.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="../stylesheets/default-layout.css"
	rel="stylesheet" />

<%@  include file="../header/page-title.html"%>

<script language="Javascript" type="text/javascript">
function isInteger(sText) {
   var ValidChars = "0123456789";
   var IsInteger=true;
   var Char;

   for (i = 0; i < sText.length && IsInteger == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsInteger = false;
      }
   }
   return IsInteger; 
}

function isDouble(sText) {
   var ValidChars = "0123456789.";
   var IsDouble=true;
   var Char;

   for (i = 0; i < sText.length && IsDouble == true; i++) { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) {
         IsDouble = false;
      }
   }
   return IsDouble; 
}

function checkNumericValues() {
  if (!isInteger(document.getElementsByName("p_sequence_n")[0].value)) {
    	alert("<%=printer.print("The program sequence number you entered is not valid")%>.");
    	return false;
  }
  if (!isDouble(document.getElementsByName("p_duration")[0].value)) {
  		alert("<%=printer.print("The program duration time you entered is not valid")%>.");
  		return false;
  }
  if (!isDouble(document.getElementsByName("p_o_duration")[0].value)) {
	  	alert("<%=printer.print("The program overlap duration you entered is not valid")%>.");
	  	return false;
  }
  if (!isDouble(document.getElementsByName("mt_duration")[0].value)) {
	  	alert("<%=printer.print("The main track duration you entered is not valid")%>.");
	  	return false;
  }
  if (!isInteger(document.getElementsByName("mt_fadeinsteps")[0].value)) {
	    alert("<%=printer.print("The main track fade-in steps you entered is not valid")%>.");
	    return false;
  }
  if (!isDouble(document.getElementsByName("mt_fadeinduration")[0].value)) {
	  	alert("<%=printer.print("The main track fade-in duration you entered is not valid")%>.");
	  	return false;
  }
  if (!isDouble(document.getElementsByName("mt_fadeinpercentage")[0].value)) {
	  	alert("<%=printer.print("The main track fade-in percentage you entered is not valid")%>.");
	  	return false;
  }
  if (!isInteger(document.getElementsByName("mt_fadeoutsteps")[0].value)) {
	    alert("<%=printer.print("The main track fade-out steps you entered is not valid")%>.");
	    return false;
  }
  if (!isDouble(document.getElementsByName("mt_fadeoutduration")[0].value)) {
	  	alert("<%=printer.print("The main track fade-out duration you entered is not valid")%>.");
	  	return false;
  }
  if (!isDouble(document.getElementsByName("mt_fadeoutpercentage")[0].value)) {
	  	alert("<%=printer.print("The main track fade-out percentage you entered is not valid")%>.");
	  	return false;
  }

  for (var i = 0; i < parseInt(document.getElementsByName("st_count")[0].value); i++) {
	  if (!isDouble(document.getElementsByName("st_stime" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track starting time you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isDouble(document.getElementsByName("st_duration" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track duration you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isInteger(document.getElementsByName("st_fadeinsteps" + i)[0].value)){
		    alert("<%=printer.print("The secondary track fade-in steps you entered is not valid")%>.");
		    return false;
	  }
	  if (!isDouble(document.getElementsByName("st_fadeinduration" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track fade-in duration you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isDouble(document.getElementsByName("st_fadeinpercentage" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track fade-in percentage you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isInteger(document.getElementsByName("st_fadeoutsteps" + i)[0].value)) {
		    alert("<%=printer.print("The secondary track fade-out steps you entered is not valid")%>.");
		    return false;
	  }
	  if (!isDouble(document.getElementsByName("st_fadeoutduration" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track fade-out duration you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isDouble(document.getElementsByName("st_fadeoutpercentage" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track fade-out percentage you entered is not valid")%>.");
		  	return false;
	  }
	  if (!isDouble(document.getElementsByName("st_offset" + i)[0].value)) {
		  	alert("<%=printer.print("The secondary track offset you entered is not valid")%>.");
		  	return false;
	  }
  }
  
  for (var i = 0; i < parseInt(document.getElementsByName("slide_count")[0].value); i++) {
	  if (!isDouble(document.getElementsByName("slide_stime" + i)[0].value)) {
		  	alert("<%=printer.print("The slide starting time you entered is not valid")%>.");
		  	return false;
	  }
  }
  
  return true;
}
</script>

<script src="../js/confirmAction.js"></script>

</head>

<body>

	<jsp:include page="../header/logout-bar.jsp" />
	<%@  include file="../header/page-banner.html"%>
	<jsp:include page="../menu/main-menu.jsp" />

	<form id="form1" name="form1" class="form-style" 
		method="post"
		action="/manageStation?action=update"
		onsubmit="return checkNumericValues();">
		
		<input type="text" name="type" value="program" style="display: none;" /> 
		<input type="text" name="k" value="<%=request.getParameter("k")%>" style="display: none;" />

		<fieldset>
			<legend><%=printer.print("Program Information")%></legend>

			<% if (message != null && message.equals("success") && action != null && (action.equals("update") || action.equals("delete"))) { %>
				<div class="success-div"><%=printer.print("Program updated successfully")%>.
				</div>
			<% } %>

			<div>
				<h2><%=readOnly ? printer.print("View a Program") : printer
					.print("Edit a Program")%></h2>
			</div>

			<%
				if (error != null && error.equals("MissingInfo")) {
			%>
			<div class="error-div"><%=printer
						.print("You are missing some essential information needed by the system")%></div>
			<%
				}
			%>

			<div>
				<label for="p_name"><span><%= printer.print("Program Name") %>
					</span></label> 
					<input type="text" name="p_name" class="input_extra_large" value="<%= program.getProgramName() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="p_name"></div>
			</div>

			<div>
		       	<label for="p_description"><span><%= printer.print("Program Description") %> <span class="required_field">*</span></span></label>
				<textarea name="p_description" class="input_extra_large" <%= readOnly ? "readonly=\"readonly\"" : "" %>><%= program.getProgramDescription() %></textarea><br/>
				<div id="p_description"></div>
			</div>
			
			<div>
				<label for="p_banner"><span><%= printer.print("Program Banner") %>
					</span></label> 
					<input type="text" name="p_banner" class="input_extra_large" value="<%= program.getProgramBanner() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="p_banner"></div>
			</div>
			
			<div>
				<label for="p_sequence_n"><span><%= printer.print("Program Sequence Number") %>
					</span></label> 
					<input type="text" name="p_sequence_n" class="input_extra_large" value="<%= program.getProgramSequenceNumber() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="p_sequence_n"></div>
			</div>
			
			<div>
				<label for="p_duration"><span><%= printer.print("Program Duration Time") %>
					</span></label> 
					<input type="text" name="p_duration" class="input_extra_large" value="<%= program.getProgramTotalDurationTime() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="p_duration"></div>
			</div>
			
			<div>
				<label for="p_o_duration"><span><%= printer.print("Program Overlap Duration") %>
					</span></label> 
					<input type="text" name="p_o_duration" class="input_extra_large" value="<%= program.getProgramOverlapDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="p_o_duration"></div>
			</div>
			
			<hr/>
			<br/>
			
			<h3><%= printer.print("Main Track") %></h3>
			
			<input type="text" name="mt_key" value="<%= KeyFactory.keyToString(mainTrack.getKey()) %>" style="display: none;" />

			<div>
				<label for="mt_type"><span><%= printer.print("Main Track Type") %>
					</span></label> 
					<input type="text" name="mt_type" class="input_extra_large" value="<%= mainTrack.getMainTrackTypeString() %>" title="" readonly="readonly" />
					<br/>
				<div id="mt_type"></div>
			</div>
			
			<% 
				if (mainTrack.getMainTrackType() == MainTrack.MainTrackType.FILE_UPLOAD) {
					Key stationAudioKey = mainTrack.getStationAudio();
					StationAudio stationAudio = StationAudioManager.getStationAudio(stationAudioKey);
			%>
			<div>
				<label for="mt_mmcontent"><span><%= printer.print("Main Track Multimedia Content") %>
					</span></label> 
					<input type="text" name="mt_mmcontent" class="input_extra_large" value="<%= stationAudio.getStationAudioName() %>" title="" readonly="readonly" />
					<br/>
					<label><span></span></label> 
					<a href="/fileDownload?file_id=<%= stationAudio.getStationAudioMultimediaContent().getKeyString() %>">
						<%= stationAudio.getStationAudioMultimediaContent().getKeyString() %>
					</a>
					<br/>
				<div id="mt_mmcontent"></div>
			</div>
			<%
				}
			%>
			
			<% 
				if (mainTrack.getMainTrackType() == MainTrack.MainTrackType.MUSIC_FILE) {
					MusicFile musicFile = MusicFileManager.getMusicFile(mainTrack.getMusicFile());
			%>
			<div>
				<label for="mt_music"><span><%= printer.print("Main Track Music File") %>
					</span></label> 
					<input type="text" name="mt_music" class="input_extra_large" value="<%= musicFile.getMusicFileTitle() %>" title="" readonly="readonly" />
					<br/>
				<div id="mt_music"></div>
			</div>
			<%
				}
			%>
			
			<% 
				if (mainTrack.getMainTrackType() == MainTrack.MainTrackType.PLAYLIST) {
					Playlist playlist = PlaylistManager.getPlaylist(mainTrack.getPlaylist());
			%>
			<div>
				<label for="mt_playlist"><span><%= printer.print("Main Track Playlist") %>
					</span></label> 
					<input type="text" name="mt_playlist" class="input_extra_large" value="<%= playlist.getPlaylistName() %>" title="" readonly="readonly" />
					<br/>
				<div id="mt_playlist"></div>
			</div>
			<%
				}
			%>
			<input type="text" name="k" value="<%=request.getParameter("k")%>" style="display: none;" />
			<div>
				<label for="mt_duration"><span><%= printer.print("Main Track Duration") %>
					</span></label> 
					<input type="text" name="mt_duration" class="input_extra_large" value="<%= mainTrack.getMainTrackDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_duration"></div>
			</div>
			
			<div>
				<label for="mt_fadeinsteps"><span><%= printer.print("Main Track Fade-In Steps") %>
					</span></label> 
					<input type="text" name="mt_fadeinsteps" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeInSteps() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeinsteps"></div>
			</div>
			
			<div>
				<label for="mt_fadeinduration"><span><%= printer.print("Main Track Fade-In Duration") %>
					</span></label> 
					<input type="text" name="mt_fadeinduration" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeInDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeinduration"></div>
			</div>
			
			<div>
				<label for="mt_fadeinpercentage"><span><%= printer.print("Main Track Fade-In Percentage") %>
					</span></label> 
					<input type="text" name="mt_fadeinpercentage" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeInPercentage() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeinpercentage"></div>
			</div>
			
			<div>
				<label for="mt_fadeoutsteps"><span><%= printer.print("Main Track Fade-Out Steps") %>
					</span></label> 
					<input type="text" name="mt_fadeoutsteps" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeOutSteps() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeoutsteps"></div>
			</div>
			
			<div>
				<label for="mt_fadeoutduration"><span><%= printer.print("Main Track Fade-Out Duration") %>
					</span></label> 
					<input type="text" name="mt_fadeoutduration" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeOutDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeoutduration"></div>
			</div>
			
			<div>
				<label for="mt_fadeoutpercentage"><span><%= printer.print("Main Track Fade-Out Percentage") %>
					</span></label> 
					<input type="text" name="mt_fadeoutpercentage" class="input_extra_large" value="<%= mainTrack.getMainTrackFadeOutPercentage() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="mt_fadeoutpercentage"></div>
			</div>
			
			<hr/>
			<br/>
			
			<h3><%= printer.print("Secondary Tracks") %></h3>

			<input type="text" name="st_count" value="<%= secondaryTracks.size() %>" style="display: none;" />

			<%
			int secondaryTrackCount = 0;
			for (SecondaryTrack secondaryTrack : secondaryTracks) {
			%>

			<input type="text" name="st_key<%= secondaryTrackCount %>" value="<%= KeyFactory.keyToString(secondaryTrack.getKey()) %>" style="display: none;" />
			
			<% if (!readOnly){ %>
			<div>
				<label for="st_no<%= secondaryTrackCount %>"><span><%= secondaryTrackCount + 1%>
					</span></label> 
					<a href="javascript:void(0);" onclick="confirmDelete('/manageProgram?action=delete&type=st&k=<%= KeyFactory.keyToString(secondaryTrack.getKey()) %>', '<%= printer.print("Are you sure you want to delete this secondary track") %>');"><%=printer.print("Delete")%></a>
					<br/>
				<div id="st_no<%= secondaryTrackCount %>"></div>
			</div>
			<% } %>
			
			<div>
				<label for="st_type<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Type") %>
					</span></label> 
					<input type="text" name="st_type<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackTypeString() %>" title="" readonly="readonly" />
					<br/>
				<div id="st_type<%= secondaryTrackCount %>"></div>
			</div>
			
			<% 
				if (secondaryTrack.getSecondaryTrackType() == SecondaryTrack.SecondaryTrackType.FILE_UPLOAD) {
					Key stationAudioKey = secondaryTrack.getStationAudio();
					StationAudio stationAudio = StationAudioManager.getStationAudio(stationAudioKey);
			%>
			<div>
				<label for="st_mmcontent<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Multimedia Content") %>
					</span></label> 
					<input type="text" name="st_mmcontent<%= secondaryTrackCount %>" class="input_extra_large" value="<%= stationAudio.getStationAudioName() %>" title="" readonly="readonly" />
					<br/>
					<label><span></span></label> 
					<a href="/fileDownload?file_id=<%= stationAudio.getStationAudioMultimediaContent().getKeyString() %>">
						<%= stationAudio.getStationAudioMultimediaContent().getKeyString() %>
					</a>
					<br/>
				<div id="st_mmcontent<%= secondaryTrackCount %>"></div>
			</div>
			<%
				}
			%>
			
			<% 
				if (secondaryTrack.getSecondaryTrackType() == SecondaryTrack.SecondaryTrackType.MUSIC_FILE) {
					MusicFile musicFile = MusicFileManager.getMusicFile(secondaryTrack.getMusicFile());
			%>
			<div>
				<label for="st_music<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Music File") %>
					</span></label> 
					<input type="text" name="st_music<%= secondaryTrackCount %>" class="input_extra_large" value="<%= musicFile.getMusicFileTitle() %>" title="" readonly="readonly" />
					<br/>
				<div id="st_music<%= secondaryTrackCount %>"></div>
			</div>
			<%
				}
			%>
			
			<div>
				<label for="st_stime<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Starting Time") %>
					</span></label> 
					<input type="text" name="st_stime<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackStartingTime() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_stime<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_duration<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Duration") %>
					</span></label> 
					<input type="text" name="st_duration<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_duration<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeinsteps<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-In Steps") %>
					</span></label> 
					<input type="text" name="st_fadeinsteps<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeInSteps() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeinsteps<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeinduration<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-In Duration") %>
					</span></label> 
					<input type="text" name="st_fadeinduration<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeInDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeinduration<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeinpercentage<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-In Percentage") %>
					</span></label> 
					<input type="text" name="st_fadeinpercentage<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeInPercentage() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeinpercentage<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeoutsteps<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-Out Steps") %>
					</span></label> 
					<input type="text" name="st_fadeoutsteps<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeOutSteps() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeoutsteps<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeoutduration<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-Out Duration") %>
					</span></label> 
					<input type="text" name="st_fadeoutduration<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeOutDuration() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeoutduration<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_fadeoutpercentage<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Fade-Out Percentage") %>
					</span></label> 
					<input type="text" name="st_fadeoutpercentage<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackFadeOutPercentage() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_fadeoutpercentage<%= secondaryTrackCount %>"></div>
			</div>
			
			<div>
				<label for="st_offset<%= secondaryTrackCount %>"><span><%= printer.print("Secondary Track Offset") %>
					</span></label> 
					<input type="text" name="st_offset<%= secondaryTrackCount %>" class="input_extra_large" value="<%= secondaryTrack.getSecondaryTrackOffset() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="st_offset<%= secondaryTrackCount %>"></div>
			</div>

			<hr/>
			<%
				secondaryTrackCount++;
			}
			%>
			
			<br/>

			<h3><%= printer.print("Slides") %></h3>
			
			<input type="text" name="slide_count" value="<%= slides.size() %>" style="display: none;" />
			
			<%
			int slideCount = 0;
			for (Slide slide : slides) {
				Key stationImageKey = slide.getStationImage();
				StationImage stationImage = StationImageManager.getStationImage(stationImageKey);
			%>
			
			<input type="text" name="slide_key<%= slideCount %>" value="<%= KeyFactory.keyToString(slide.getKey()) %>" style="display: none;" />
			
			<% if (!readOnly){ %>
			<div>
				<label for="slide_no<%= slideCount %>"><span><%= slideCount + 1%>
					</span></label> 
					<a href="javascript:void(0);" onclick="confirmDelete('/manageProgram?action=delete&type=slide&k=<%= KeyFactory.keyToString(slide.getKey()) %>', '<%= printer.print("Are you sure you want to delete this slide") %>');"><%=printer.print("Delete")%></a>
					<br/>
				<div id="slide_no<%= slideCount %>"></div>
			</div>
			<%} %>
			
			<div>
				<label for="slide_mmcontent<%= slideCount %>"><span><%= printer.print("Slide Multimedia Content") %>
					</span></label> 
					<input type="text" name="slide_mmcontent<%= slideCount %>" class="input_extra_large" value="<%= stationImage.getStationImageName() %>" title="" readonly="readonly" />
					<br/>
					<label><span></span></label> 
					<a href="/fileDownload?file_id=<%= stationImage.getStationImageMultimediaContent().getKeyString() %>">
						<%= stationImage.getStationImageMultimediaContent().getKeyString() %>
					</a>
					<br/>
				<div id="slide_mmcontent<%= slideCount %>"></div>
			</div>
			
			<div>
				<label for="slide_stime<%= slideCount %>"><span><%= printer.print("Slide Starting Time") %>
					</span></label> 
					<input type="text" name="slide_stime<%= slideCount %>" class="input_extra_large" value="<%= slide.getSlideStartingTime() %>" title="" <%=readOnly ? "readonly=\"readonly\"" : ""%> />
					<br/>
				<div id="slide_stime<%= slideCount %>"></div>
			</div>
			
			<hr/>
			<% 
				slideCount++;
			}
			%>

		</fieldset>

		<br class="clearfloat" /> <input type="button"
			value="<%=printer.print("Close")%>"
			onclick="location.href='/station/listProgram.jsp'"
			class="button-close" />
			 
		<%
			if (!readOnly) {
		%>
		<input type="submit" value="<%=printer.print("Update")%>"
			class="button_style" />
		<%
			} else {
		%>
		<input type="button"
			value="&nbsp;&nbsp;<%=printer.print("Edit")%>&nbsp;&nbsp;"
			onclick="location.href='/station/editProgram.jsp?k=<%=request.getParameter("k")%>'"
			class="button_style"/>
		<%
			}
		%>

	</form>

	<jsp:include page="../header/page-footer.jsp" />

</body>
</html>
