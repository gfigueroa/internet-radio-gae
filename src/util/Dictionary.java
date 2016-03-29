/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.util.HashMap;
import java.io.Serializable;
/**
 * Utility class for storing terminologies in different languages 
 * for the web server pages.
 * 
 */

public class Dictionary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2389998361033740665L;

	public static enum Language {
		ENGLISH, CHINESE
	}
	
	public static String getLanguageString(Language language) {
		if (language == null) {
			return "";
		}
		
		switch (language) {
			case ENGLISH:
				return "EN";
			case CHINESE:
				return "CH";
			default:
				return "";
		}
	}
	
	private HashMap<String, Term> terms;
	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private Dictionary() {
		terms = new HashMap<String, Term>();
		loadTerms();
	}
	
	/**
	 * Get an instance of the Dictionary class.
	 * @return an instance of Dictionary
	 */
	public static Dictionary getInstance() {
		return DictionaryHolder.instance;
	}
	
	/**
	 * DictionaryHolder is loaded on the first execution of 
	 * Dictionary.getInstance() or the first access to 
	 * Dictionary.INSTANCE, not before.
	 */
	private static class DictionaryHolder {
		public static final Dictionary instance = new Dictionary();
	}
	
	/**
	 * Obtains the translation of this English term
	 * into the language given as parameter.
	 * @param englishTerm
	 * 			: the term in English to be translated
	 * @param language
	 * 			: the language desired for the translation
	 * @return the translation of this English term into the 
	 * 			desired language
	 */
	public String translate(String englishTerm, Language language) {
		
		Term term = terms.get(englishTerm);
		if (term == null) {
			return "";
		}
		
		switch (language) {
			case ENGLISH:
				return term.englishTerm;
			case CHINESE:
				return term.chineseTerm;
			default:
				return "";	
		}
	}
	
	/**
	 * Loads all the terms to be used by the dictionary.
	 */
	private void loadTerms() {
		terms.put("HOME", new Term("Home", "首頁"));
		terms.put("WELCOME", new Term("Welcome", "歡迎"));
		terms.put("SIGN IN", new Term("Sign in", "登入"));
		terms.put("USER E-MAIL", new Term("User e-mail", "使用者 e-mail"));
		terms.put("PASSWORD", new Term("Password", "密碼"));
		terms.put("LOG IN", new Term("Log in", "登入"));
		terms.put("TO ACCESS YOUR ACCOUNT", new Term("to access your account", "請輸入您的帳號"));
		terms.put("CREATE", new Term("Create", "建立"));
		terms.put("A NEW CUSTOMER ACCOUNT", new Term("a new customer account", "新的顧客帳戶"));
		terms.put("CREATE YOUR ACCOUNT TO GAIN ACCESS TO THE SYSTEM", new Term("Create your account to gain access to the system", "初次使用，請建立一個新帳號登入系統。"));
		terms.put("CREATE A NEW ACCOUNT", new Term("Create a new account", "建立新帳號"));
		terms.put("CHINESE", new Term("Chinese", "中文"));
		terms.put("ENGLISH", new Term("English", "英文"));

		terms.put("NAME AND CONTACT INFORMATION", new Term("Name and Contact Information", "姓名與連絡資訊"));
		terms.put("NEW CUSTOMER", new Term("New Customer", "新的顧客"));

		terms.put("LANGUAGE", new Term("Language", "語言"));


		terms.put("LOGOUT", new Term("Logout", "登出"));
		terms.put("DJ INTERFACE", new Term("DJ Interface", "廣播節目員界面"));
		terms.put("CHANNELS", new Term("Channels", "節目"));
		terms.put("PROGRAMS", new Term("Programs", "片段"));
		terms.put("MULTIMEDIA CONTENT", new Term("Multimedia Content", "多媒體內容"));
		terms.put("STATION AUDIO", new Term("Station Audio", "電台音效檔案"));
		terms.put("STATION IMAGE", new Term("Station Image", "電台影像檔案"));
		terms.put("PUBLIC MUSIC LIBRARY", new Term("Public Music Library", "公用音樂檔案庫"));
		terms.put("STATION PROFILE", new Term("Station Profile", "電台設定檔案"));

		terms.put("NAME", new Term("Name", "口白"));
		terms.put("POSITION", new Term("Position", "開始時間"));
		terms.put("FADE IN-OUT PERCENTAGE", new Term("Fade in-out percentage", "背景音樂淡入淡出百分比"));
		terms.put("FADE IN-OUT DURATION", new Term("Fade in-out duration", "背景音樂淡入淡出時間"));
		terms.put("LOCK FOR PLAYBACK", new Term("Lock for playback", "鎖定選取以進行播放"));
		terms.put("THE PROGRAM BANNER", new Term("The program banner", "片段字幕"));
		terms.put("PAUSE", new Term("Pause", "暫停"));
		terms.put("PLAY", new Term("Play", "播放"));
		terms.put("STOP", new Term("Stop", "停止"));
		terms.put("MUTE", new Term("Mute", "靜音"));
		terms.put("MAX VOLUME", new Term("Max Volume", "最大音量"));
		terms.put("MARK SLIDE", new Term("Mark Slide", "定位投影片位置"));
		terms.put("MARK DJ RECORDING", new Term("Mark DJ recording", "定位口白位置"));
		terms.put("SAVE", new Term("Save", "儲存"));
		terms.put("SEQUENCE NUMBER", new Term("Sequence number", "播放順序編號"));
		terms.put("OVERWRITE", new Term("Overwrite", "覆寫"));
		terms.put("CANCEL", new Term("Cancel", "取消"));
		terms.put("NEW PROGRAM", new Term("New program", "建立新的片段"));
		terms.put("INFORMATION", new Term("Information", "資訊"));
		terms.put("PLEASE SELECT A CHANNEL", new Term("Please select a channel", "請選請一個節目"));
		terms.put("OK", new Term("Ok", "確認"));
		terms.put("FILL IN THE PROGRAM INFORMATION", new Term("Fill in the program information", "填入片段資訊"));
		terms.put("FILL IN THE SLIDE INFORMATION", new Term("Fill in the slide information", "填入投影片資訊"));
		terms.put("FILL IN THE MAIN TRACK INFORMATION", new Term("Fill in the main track information", "填入主要音軌資訊"));
		terms.put("FILL IN THE DJ RECORDING INFORMATION", new Term("Fill in the dj recording information", "填入口白資訊"));
		terms.put("THE PROGRAM HAS BEEN SAVED", new Term("The program has been saved", "該片段已被儲存"));
		terms.put("THERE HAS BEEN A PROBLEM SAVING THE PROGRAM", new Term("There has been a problem saving the program", "無法儲存該片段"));
		terms.put("THERE ARE NO CHANNELS", new Term("There are no channels", "請先選擇節目"));
		terms.put("PLEASE SET THE MARK(S) FOR THE DJ RECORDING(S)", new Term("Please set the mark(s) for the dj recording(s)", "請設置口白的播放時間位置"));
		terms.put("PLEASE SET THE MARK(S) FOR THE SLIDE(S)", new Term("Please set the mark(s) for the slide(s)", "請設置投影片的播放時間位置"));
		terms.put("FILL IN THE PROGRAM NAME", new Term("Fill in the program name", "填入片段名稱"));
		terms.put("PLEASE CREATE A CHANNEL", new Term("Please create a channel", "請建立一個節目"));
		terms.put("SEARCH FOR MAIN TRACKS", new Term("Search for main tracks", "取得主音軌檔案"));
		terms.put("YOUR FILES", new Term("Your files", "你的檔案"));
		terms.put("LIBRARY", new Term("Library", "公用庫"));
		terms.put("DESCRIPTION", new Term("Description", "描述"));
		terms.put("SEARCH FOR SLIDES", new Term("Search for slides", "取得投影片檔案"));
		terms.put("SLIDES", new Term("Slides", "投影片檔案"));
		terms.put("UNLOCK TO EDIT WITHOUT PLAYBACK", new Term("Unlock to edit without playback", "解開選取鎖定以編輯播放時間位置"));
		terms.put("PROGRAM DURATION MARK", new Term("Program duration mark", "片段時間長度標記"));
		terms.put("SEARCH FOR SECONDARY TRACKS", new Term("Search for secondary tracks", "取得口白檔案"));
		terms.put("TRACKS", new Term("Tracks", "音軌檔案"));
		terms.put("SHARE ON", new Term("Share On", "分享至"));
		terms.put("SHARE ON FACEBOOK", new Term("Share On Facebook", "分享至臉書"));
		terms.put("STATION MUSIC", new Term("Station Music", "電台音樂檔案"));
		terms.put("STATION VOICE", new Term("Station Voice", "電台口白檔案"));
		terms.put("THE CURRENT CHANNEL WILL BE SHARED ON FACEBOOK", new Term("The current channel will be shared on Facebook", "目前的節目內容即將被分享至臉書"));
		terms.put("CLICK TO REPLAY", new Term("Click to replay", "點選重播"));

		terms.put("ADD NEW CHANNEL", new Term("Add new Channel", "加入新的節目"));
		terms.put("ADD A CHANNEL", new Term("Add a channel", "加入一個節目"));
		terms.put("CHANNEL NAME", new Term("Channel name", "節目名稱"));
		terms.put("ACTIONS", new Term("Actions", "執行動作"));
		terms.put("EDIT", new Term("Edit", "編輯"));
		terms.put("DELETE", new Term("Delete", "刪除"));
		terms.put("CHANNEL INFORMATION", new Term("Channel information", "節目資訊"));
		terms.put("VIEW A CHANNEL", new Term("View a channel", "檢視節目"));
		terms.put("STATION NAME", new Term("Station name", "電台名稱"));
		terms.put("CHANNEL NAME", new Term("Channel name", "節目名稱"));
		terms.put("CHANNEL NUMBER", new Term("Channel number", "節目編號"));
		terms.put("CLOSE", new Term("Close", "關閉"));
		terms.put("EDIT A CHANNEL", new Term("Edit a channel", "編輯節目"));
		terms.put("UPDATE", new Term("Update", "更新"));
		terms.put("CONTINUE ADDING CHANNELS", new Term("Continue adding channels", "繼續新增節目"));

		terms.put("ADD NEW PROGRAM (JSON UPLOAD)", new Term("Add new program (JSON upload)", "加入新的片段 (JSON 檔案上傳)"));
		terms.put("ADD PROGRAM", new Term("Add program", "加入片段"));
		terms.put("JSON FILE", new Term("JSON file", "JSON 檔案"));
		terms.put("PROGRAM NAME", new Term("Program name", "片段名稱"));
		terms.put("CHANNEL", new Term("Channel", "節目"));
		terms.put("PROGRAM INFORMATION", new Term("Program information", "片段資訊"));
		terms.put("VIEW A PROGRAM", new Term("View a program", "檢視片段"));
		terms.put("PROGRAM NAME", new Term("Program name", "片段名稱"));
		terms.put("PROGRAM DESCRIPTION", new Term("Program description", "片段描述"));
		terms.put("PROGRAM BANNER", new Term("Program banner", "片段的跑馬文字"));
		terms.put("PROGRAM SEQUENCE NUMBER", new Term("Program sequence number", "片段的順序編號"));
		terms.put("PROGRAM DURATION TIME", new Term("Program duration time", "片段時間長度"));
		terms.put("PROGRAM OVERLAP DURATION", new Term("Program overlap duration", "片段之間的重疊時間"));
		terms.put("MAIN TRACK", new Term("Main track", "主要音軌"));
		terms.put("MAIN TRACK TYPE", new Term("Main track type", "主要音軌檔案類型"));
		terms.put("MAIN TRACK MUSIC FILE", new Term("Main track music file", "主要音軌檔案"));
		terms.put("MAIN TRACK DURATION", new Term("Main track duration", "主要音軌時間長度"));
		terms.put("MAIN TRACK FADE-OUT STEPS", new Term("Main track fade-out steps", "主要音軌淡出的工作計次"));
		terms.put("MAIN TRACK FADE-OUT DURATION", new Term("Main track fade-out duration", "主要音軌淡出的時間"));
		terms.put("MAIN TRACK FADE-OUT PERCENTAGE", new Term("Main track fade-out percentage", "主要音軌淡出的百分比"));
		terms.put("MAIN TRACK FADE-IN STEPS", new Term("Main track fade-in steps", "主要音軌淡入的工作計次"));
		terms.put("MAIN TRACK FADE-IN DURATION", new Term("Main track fade-in duration", "主要音軌淡入的時間"));
		terms.put("MAIN TRACK FADE-IN PERCENTAGE", new Term("Main track fade-in percentage", "主要音軌淡入的百分比"));
		terms.put("SECONDARY TRACKS", new Term("Secondary tracks", "口白音軌"));
		terms.put("SECONDARY TRACK TYPE", new Term("Secondary track type", "口白音軌檔案類型"));
		terms.put("SECONDARY TRACK MULTIMEDIA CONTENT", new Term("Secondary track multimedia content", "口白音軌多媒體實體內容"));
		terms.put("SECONDARY TRACK MUSIC FILE", new Term("Secondary track music file", "口白音軌檔案"));
		terms.put("SECONDARY TRACK STARTING TIME", new Term("Secondary track starting time", "口白音軌開始時間"));
		terms.put("SECONDARY TRACK DURATION", new Term("Secondary track duration", "口白音軌時間長度"));
		terms.put("SECONDARY TRACK FADE-IN STEPS", new Term("Secondary track fade-in steps", "口白音軌淡入的工作計次"));
		terms.put("SECONDARY TRACK FADE-IN DURATION", new Term("Secondary track fade-in duration", "口白音軌淡入的時間"));
		terms.put("SECONDARY TRACK FADE-IN PERCENTAGE", new Term("Secondary track fade-in percentage", "口白音軌淡入的百分比"));
		terms.put("SECONDARY TRACK FADE-OUT STEPS", new Term("Secondary track fade-out steps", "口白音軌淡出的工作計次"));
		terms.put("SECONDARY TRACK FADE-OUT DURATION", new Term("Secondary track fade-out duration", "口白音軌淡出的時間"));
		terms.put("SECONDARY TRACK FADE-OUT PERCENTAGE", new Term("Secondary track fade-out percentage", "口白音軌淡出的百分比"));
		terms.put("SECONDARY TRACK OFFSET", new Term("Secondary track offset", "口白音軌的偏移量"));
		terms.put("SLIDES", new Term("Slides", "投影片"));
		terms.put("SLIDE MULTIMEDIA CONTENT", new Term("Slide multimedia content", "投影片的多媒體內容"));
		terms.put("SLIDE STARTING TIME", new Term("Slide starting time", "投影片開始時間"));
		terms.put("EDIT A PROGRAM", new Term("Edit a program", "編輯片段"));
		terms.put("NO.", new Term("No.", "順序編號"));

		terms.put("ADD NEW STATION AUDIO", new Term("Add new station audio", "加入新的電台音效檔案"));
		terms.put("ADD A STATION AUDIO FILE", new Term("Add a station audio file", "加入一個電台音效檔案"));
		terms.put("STATION AUDIO NAME", new Term("Station audio name", "電台音效檔案名稱"));
		terms.put("STATION AUDIO TYPE", new Term("Station audio type", "電台音效檔案類型"));
		terms.put("STATION AUDIO INFORMATION", new Term("Station audio information", "電台音效檔案資訊"));
		terms.put("VIEW A STATION AUDIO FILE", new Term("View a station audio file", "檢閱電台音效檔案"));
		terms.put("AUDIO FILE TYPE", new Term("Audio file type", "音效檔案類型"));
		terms.put("STATION AUDIO FILE", new Term("Station audio file", "電台音效檔案"));
		terms.put("STATION AUDIO DURATION", new Term("Station audio duration", "電台音效檔案時間"));
		terms.put("STATION AUDIO KEY", new Term("Station audio key", "電台音效檔案金鑰"));
		terms.put("EDIT A STATION AUDIO FILE", new Term("Edit a station audio file", "編輯電台音效檔案"));
		terms.put("CONTINUE ADDING STATION AUDIO FILES", new Term("Continue adding station audio files", "繼續新增電台音效檔案"));
		terms.put("STATION AUDIO FORMAT", new Term("Station audio format", "電台音效檔案格式"));
		terms.put("THERE IS ALREADY A STATION AUDIO WITH THIS NAME", new Term("There is already a station audio with this name", "已有相同的電台音效檔案名稱"));
		terms.put("THIS STATION AUDIO IS BEING USED IN ONE OF YOUR PROGRAM SEGMENTS AND CANNOT BE DELETED", new Term("This station audio is being used in one of your program segments and cannot be deleted", "電台音效檔案已被某節目的片段使用而無法刪除"));
		terms.put("MUSIC", new Term("Music", "音樂檔案"));
		terms.put("VOICE", new Term("Voice", "口白檔案"));

		terms.put("STATION IMAGE INFORMATION", new Term("Station image information", "電台影像檔案資訊"));
		terms.put("ADD A STATION IMAGE FILE", new Term("Add a station image file", "新增一個電台影像檔案"));
		terms.put("ADD NEW STATION IMAGE", new Term("Add new station image", "加入新的電台影像檔案"));
		terms.put("STATION IMAGE NAME", new Term("Station image name", "電台影像檔案"));
		terms.put("VIEW A STATION IMAGE FILE", new Term("View a station image file", "檢閱電台影像檔案"));
		terms.put("STATION IMAGE FILE", new Term("Station image file", "電台影像檔案"));
		terms.put("STATION IMAGE KEY", new Term("Station image key", "電台影像檔案金鑰"));
		terms.put("EDIT A STATION IMAGE FILE", new Term("Edit a station image file", "編輯電台影像檔案"));
		terms.put("STATION IMAGE FORMAT", new Term("Station image format", "電台影像檔案格式"));
		terms.put("CONTINUE ADDING STATION IMAGE FILES", new Term("Continue adding station image files", "繼續新增電台影像檔案"));
		terms.put("THERE IS ALREADY A STATION IMAGE WITH THIS NAME", new Term("There is already a station image with this name", "已有相同的電台影像檔案名稱"));
		terms.put("THIS STATION IMAGE IS BEING USED IN ONE OF YOUR PROGRAM SEGMENTS AND CANNOT BE DELETED", new Term("This station image is being used in one of your program segments and cannot be deleted", "電台影像檔案已被某節目的片段使用而無法刪除"));

		terms.put("MUSIC FILE", new Term("Music file", "音樂檔案"));
		terms.put("GENRE", new Term("Genre", "音樂類型"));
		terms.put("KEY", new Term("Key", "金鑰"));
		terms.put("BLOBKEY", new Term("Blobkey", "雲端儲存服務金鑰"));

		terms.put("EDIT STATION", new Term("Edit station", "編輯電台"));
		terms.put("STATION TYPE", new Term("Station type", "電台類型"));
		terms.put("STATION PRIVILEGE LEVEL", new Term("Station privilege level", "電台權限等級"));
		terms.put("STATION NAME", new Term("Station name", "電台名稱"));
		terms.put("STATION NUMBER", new Term("Station number", "電台編號"));
		terms.put("DESCRIPTION", new Term("Description", "描述"));
		terms.put("E-MAIL", new Term("E-mail", "E-mail"));
		terms.put("REGION", new Term("Region", "區域"));
		terms.put("ADDRESS", new Term("Address", "地址"));
		terms.put("WEBSITE", new Term("Website", "網站位置"));
		terms.put("LOGO", new Term("Logo", "圖像標誌"));
		terms.put("COMMENTS", new Term("Comments", "評論"));


		terms.put("ADMINISTRATORS", new Term("Administrators", "管理者"));
		terms.put("STATION", new Term("Station", "電台"));
		terms.put("STATIONS", new Term("Stations", "電台目錄"));
		terms.put("STATION TYPES", new Term("Station types", "電台類型"));
		terms.put("CUSTOMERS", new Term("Customers", "用戶"));
		terms.put("GENRES", new Term("Genres", "音樂類型"));
		terms.put("CONFIGURATION", new Term("Configuration", "設定"));
		terms.put("REGIONS", new Term("Regions", "地區"));
		terms.put("SYSTEM", new Term("System", "系統"));

		terms.put("ADMINISTRATOR LIST", new Term("Administrator List", "管理者名單"));
		terms.put("ADD NEW ADMINISTRATOR", new Term("Add New Administrator", "新增管理者"));
		terms.put("ADMINISTRATOR NAME", new Term("Administrator Name", "管理者名稱"));
		terms.put("USER INFORMATION", new Term("User Information", "會員資訊"));
		terms.put("ADD ADMINISTRATOR", new Term("Add Administrator", "新增管理者"));
		terms.put("USER TYPE", new Term("User Type", "會員類型"));
		terms.put("ADMINISTRATOR NAME", new Term("Administrator name", "會員名稱"));
		terms.put("E-MAIL", new Term("E-mail", "E-mail"));
		terms.put("PASSWORD", new Term("Password", "密碼"));
		terms.put("CONFIRM PASSWORD", new Term("Confirm Password", "密碼確認"));
		terms.put("VIEW ADMINISTRATOR", new Term("View Administrator", "檢閱管理者"));
		terms.put("EDIT ADMINISTRATOR", new Term("Edit administrator", "編輯管理者"));
		terms.put("CHANGE PASSWORD", new Term("Change password", "更改密碼"));
		terms.put("EDIT ADMINISTRATOR PASSWORD", new Term("Edit Administrator Password", "編輯管理者密碼"));
		terms.put("NEW PASSWORD", new Term("New Password", "設定新密碼"));
		terms.put("CONFIRM PASSWORD", new Term("Confirm Password", "密碼確認"));

		terms.put("STATION LIST", new Term("Station list", "電台列表"));
		terms.put("ADD NEW STATION", new Term("Add new station", "加入新的電台"));
		terms.put("STATION TYPE", new Term("Station type", "電台類型"));
		terms.put("SELECT STATION TYPE", new Term("Select station type", "選擇電台類型"));
		terms.put("BASIC", new Term("Basic", "基礎等級"));
		terms.put("BEGINNER", new Term("Beginner", "初學者等級"));
		terms.put("AMATEUR", new Term("Amateur", "業餘等級"));
		terms.put("PRO", new Term("Pro", "專業等級"));
		terms.put("STATION INFORMATION", new Term("Station information", "電台資訊"));
		terms.put("ADD STATION", new Term("Add station", "加入電台"));
		terms.put("EDIT STATION PASSWORD", new Term("Edit station password", "編輯電台密碼"));

		terms.put("CHANNEL KEY", new Term("Channel key", "節目金鑰"));
		terms.put("BACK TO STATION", new Term("Back to station", "回到電台"));

		terms.put("STATION TYPE LIST", new Term("Station type list", "電台類型列表"));
		terms.put("ADD NEW STATION TYPE", new Term("Add new station type", "加入新的電台類型"));
		terms.put("STATION TYPE", new Term("Station type", "電台類型"));
		terms.put("STATION TYPE INFORMATION", new Term("Station type information", "電台類型資訊"));
		terms.put("ADD STATION TYPE", new Term("Add station type", "加入電台類型"));
		terms.put("STATION TYPE NAME", new Term("Station type name", "電台類型名稱"));
		terms.put("STATION TYPE DESCRIPTION", new Term("Station type description", "電台類型描述"));
		terms.put("VIEW STATION TYPE", new Term("View station type", "檢閱電台類型"));
		terms.put("EDIT STATION TYPE", new Term("Edit station type", "編輯電台類型"));
		terms.put("VERSION", new Term("Version", "版本"));

		terms.put("CUSTOMER LIST", new Term("Customer List", "顧客名單"));
		terms.put("ADD NEW CUSTOMER", new Term("Add New Customer", "新增顧客"));
		terms.put("CUSTOMER NAME", new Term("Customer Name", "顧客名稱"));
		terms.put("NAME & CONTACT INFORMATION", new Term("Name & Contact Information", "名稱 & 連絡資訊"));
		terms.put("ADD CUSTOMER", new Term("Add Customer", "新增顧客"));
		terms.put("CUSTOMER NAME", new Term("Customer Name", "顧客名稱"));
		terms.put("GENDER", new Term("Gender", "性別"));
		terms.put("SELECT GENDER", new Term("Select Gender", "選擇性別"));
		terms.put("MALE", new Term("Male", "男性"));
		terms.put("FEMALE", new Term("Female", "女性"));
		terms.put("PHONE NUMBER", new Term("Phone Number", "電話"));
		terms.put("VIEW CUSTOMER", new Term("View Customer", "檢閱顧客資料"));
		terms.put("EDIT CUSTOMER", new Term("Edit Customer", "編輯顧客資料"));
		terms.put("EDIT CUSTOMER PASSWORD", new Term("Edit customer password", "編輯顧客密碼"));

		terms.put("ADD NEW GENRE", new Term("Add new genre", "加入新的作品"));
		terms.put("GENRE", new Term("Genre", "音樂類型"));
		terms.put("GENRE INFORMATION", new Term("Genre information", "作品資訊"));
		terms.put("ADD GENRE", new Term("Add genre", "加入作品"));
		terms.put("GENRE ENGLISH NAME", new Term("Genre English name", "作品英文名稱"));
		terms.put("GENRE CHINESE NAME", new Term("Genre Chinese name", "作品中文名稱"));
		terms.put("VIEW GENRE", new Term("View Genre", "檢閱作品"));
		terms.put("EDIT GENRE", new Term("Edit Genre", "編輯作品"));

		terms.put("ADD NEW MUSIC FILE", new Term("Add new music file", "加入新的音樂檔案"));
		terms.put("ADD MULTIPLE MUSIC FILES", new Term("Add multiple music files", "加入多個音樂檔案"));
		terms.put("MUSIC FILE INFORMATION", new Term("Music file information", "音樂檔案資訊"));
		terms.put("ADD MUSIC FILE", new Term("Add music file", "加入音樂檔案"));
		terms.put("FILE", new Term("File", "檔案"));
		terms.put("MUSIC FILE TITLE", new Term("Music file title", "音樂檔案標題"));
		terms.put("VIEW MUSIC FILE", new Term("View music file", "檢閱音樂檔案"));
		terms.put("GENRE", new Term("Genre", "音樂類型"));
		terms.put("MUSIC FILE KEY", new Term("Music file key", "音樂檔案金鑰"));
		terms.put("MUSIC FILE BLOBKEY", new Term("Music file blobkey", "音樂檔案屬性值金鑰"));
		terms.put("FILE(S)", new Term("File(s)", "(多個)檔案"));
		terms.put("THERE IS ALREADY A MUSIC FILE IN THE MUSIC LIBRARY WITH THIS TITLE", new Term("There is already a music file in the music library with this title", "在公用音樂庫已有相同的音樂檔名稱"));

		terms.put("REGION LIST", new Term("Region List", "地區列表"));
		terms.put("ADD NEW REGION", new Term("Add New Region", "新增地區"));
		terms.put("REGION INFORMATION", new Term("Region Information", "地區資訊"));
		terms.put("ADD REGION", new Term("Add Region", "新增地區"));
		terms.put("REGION NAME", new Term("Region Name", "地區名稱"));
		terms.put("VIEW REGION", new Term("View Region", "檢閱地區資訊"));
		terms.put("EDIT REGION", new Term("Edit Region", "編輯地區"));

		terms.put("SYSTEM INFORMATION", new Term("System Information", "系統資訊"));
		terms.put("VIEW SYSTEM", new Term("View System", "檢閱系統"));
		terms.put("EDIT SYSTEM", new Term("Edit System", "編輯系統"));
		terms.put("SYSTEM LAST MODIFIED ON", new Term("System last modified on", "上次修改"));
		terms.put("SYSTEM VERSIONS", new Term("System versions", "系統版本"));
		terms.put("STATION LIST VERSION", new Term("Station list version", "電台列表版本"));
		terms.put("STATION TYPE LIST VERSION", new Term("Station type list version", "電台類型列表版本"));
		terms.put("CHANNEL", new Term("Channel", "音樂檔案庫版本"));
		terms.put("OLDEST APP VERSION SUPPORTED", new Term("Oldest App version supported", "舊版APP支援"));
		terms.put("MUSIC LIBRARY VERSION", new Term("Music library version", "音樂檔案庫版本"));


		terms.put("SOME OR ALL OF THE INFORMATION PROVIDED DOES NOT MATCH ANY USER IN OUR DATABASE", new Term("Some or all of the information provided does not match any user in our database", "所提供資訊與會員資料庫沒有完全符合"));
		terms.put("USER REGISTERED SUCCESSFULLY", new Term("User registered successfully", "會員註冊成功"));
		terms.put("CUSTOMERS CANNOT LOG IN TO THE SYSTEM", new Term("Customers cannot log in to the system", "顧客不能登入此系統"));
		terms.put("REGISTER SUCCESSFUL", new Term("Register successful", "註冊成功"));

		terms.put("THE CHANNEL NUMBER YOU ENTERED IS NOT VALID", new Term("The channel number you entered is not valid", "輸入的節目編號是無效的"));
		terms.put("CHANNEL SUCCESSFULLY ADDED TO THE STATION", new Term("Channel successfully added to the station", "節目已成功加至電台"));
		terms.put("STATION AUDIO SUCCESSFULLY ADDED TO THE STATION", new Term("Station audio successfully added to the station", "電台音效已成功加至電台"));
		terms.put("STATION IMAGE SUCCESSFULLY ADDED TO THE STATION", new Term("Station image successfully added to the station", "電台影像已成功加至電台"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS CHANNEL", new Term("Are you sure you want to delete this channel", "請確認是否刪除該節目"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS PROGRAM", new Term("Are you sure you want to delete this program", "請確認是否刪除該片段"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS STATION AUDIO", new Term("Are you sure you want to delete this station audio", "請確認是否刪除該電台音效"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS STATION IMAGE", new Term("Are you sure you want to delete this station image", "請確認是否刪除該電台影像"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS SECONDARY TRACK", new Term("Are you sure you want to delete this secondary track", "請確認是否刪除該口白檔案"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS SLIDE", new Term("Are you sure you want to delete this slide", "請確認是否刪除該投影片檔案"));
		terms.put("PROGRAM UPDATED SUCCESSFULLY", new Term("Program updated successfully", "片段更新成功"));
		terms.put("CHANNEL UPDATED SUCCESSFULLY", new Term("Channel updated successfully", "節目更新成功"));
		terms.put("STATION AUDIO UPDATED SUCCESSFULLY", new Term("Station audio updated successfully", "電台音效更新成功"));
		terms.put("STATION IMAGE UPDATED SUCCESSFULLY", new Term("Station image updated successfully", "電台影像更新成功"));

		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS ADMINISTRATOR", new Term("Are you sure you want to delete this administrator", "請確認是否刪除該系統管理者"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS STATION", new Term("Are you sure you want to delete this station", "請確認是否刪除該電台"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS STATION TYPE", new Term("Are you sure you want to delete this station type", "請確認是否刪除該電台類型"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS CUSTOMER", new Term("Are you sure you want to delete this customer", "請確認是否刪除該用戶"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS GENRE", new Term("Are you sure you want to delete this genre", "請確認是否刪除該音樂類型"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS MUSIC FILE", new Term("Are you sure you want to delete this music file", "請確認是否刪除該音樂檔案"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS REGION", new Term("Are you sure you want to delete this region", "請確認是否刪除該區域分類"));

		terms.put("THE PASSWORD YOU ENTERED DOESN'T MATCH THE CONFIRMATION PASSWORD", new Term("The password you entered doesn't match the confirmation password", "您輸入的密碼不正確"));
		terms.put("THE EMAIL YOU PROVIDED IS ALREADY BEING USED IN THE SYSTEM", new Term("The email you provided is already being used in the system", "您輸入的email已存在"));
		terms.put("THE EMAIL YOU PROVIDED DOES NOT CONFORM TO THE STANDARD EMAIL FORMAT", new Term("The email you provided does not conform to the standard email format", "您輸入的email格式不符"));
		terms.put("YOU ARE MISSING SOME ESSENTIAL INFORMATION NEEDED BY THE SYSTEM", new Term("You are missing some essential information needed by the system", "您尚有必填資料未輸入"));
		terms.put("THE EMAIL YOU PROVIDED DOES NOT CONFORM TO THE STANDARD FORMATS (YOU CAN TRY SOMETHING LIKE USER@DOMAIN.COM)", new Term("The email you provided does not conform to the standard formats (you can try something like user@domain.com)", "您輸入的email格式不符,請確認是否輸入完整"));
		terms.put("ADMINISTRATOR PASSWORD CHANGED SUCCESSFULLY", new Term("Administrator password changed successfully", "管理者密碼修改成功"));
		terms.put("CUSTOMER PASSWORD CHANGED SUCCESSFULLY", new Term("Customer password changed successfully", "顧客密碼修改成功"));
		terms.put("STATION PASSWORD CHANGED SUCCESSFULLY", new Term("Station password changed successfully", "電台密碼修改成功"));
		terms.put("SYSTEM UPDATED SUCCESSFULLY", new Term("System updated successfully", "系統更新成功"));
	}

}
