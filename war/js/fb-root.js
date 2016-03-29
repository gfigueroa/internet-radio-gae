  /**
 *Login using facebook
 */


window.fbAsyncInit = function() {
          FB.init({
            appId      : '344628262258401',
            status     : true, 
            cookie     : true,
            xfbml      : true,
            oauth      : true,
          });
        };
        (function(d){
           var js, id = 'facebook-jssdk'; if (d.getElementById(id)) {return;}
           js = d.createElement('script'); js.id = id; js.async = true;
           js.src = "//connect.facebook.net/en_US/all.js";
           d.getElementsByTagName('head')[0].appendChild(js);
         }(document));
        
        
        FB.api(
        		  {
        		    method: 'fql.query',
        		    query: 'SELECT name FROM user WHERE uid=me()'
        		  },
        		  function(response) {
        		    alert('Your name is ' + response[0].name);
        		  }
        		);
        
        FB.getLoginStatus(function(response) {
        	  if (response.status === 'connected') {
        	    // the user is logged in and has authenticated your
        	    // app, and response.authResponse supplies
        	    // the user's ID, a valid access token, a signed
        	    // request, and the time the access token 
        	    // and signed request each expire
        	    var uid = response.authResponse.userID;
        	    var accessToken = response.authResponse.accessToken;
        	  } else if (response.status === 'not_authorized') {
        	    // the user is logged in to Facebook, 
        	    // but has not authenticated your app
        	  } else {
        	    // the user isn't logged in to Facebook.
        	  }
        	 });