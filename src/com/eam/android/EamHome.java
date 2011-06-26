package com.eam.android;

import java.net.URLEncoder;

import com.eam.android.control.EamWebviewClient;
import com.eam.android.utils.Config;
import com.eam.android.utils.MenuHandler;
import com.eam.android.utils.NavigationHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

/**
 * This is the home page activity that shows the EAMs for the current user in a webview
 * @author Jia
 *
 */
public class EamHome extends Activity {
	
	private WebView webView;
	
	 public WebView getWebView() {
		return webView;
	}


	public void setWebView(WebView webView) {
		this.webView = webView;
	}


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.home);
        
        if (savedInstanceState != null) {
        	//((WebView)findViewById(R.id.webview1)).restoreState(savedInstanceState);
        }        
        else {	       
	        //getting the user name which was saved from the previous activity (login page)
	        Bundle previousBundle = this.getIntent().getExtras();
	        String token = previousBundle.getString("token");
	        String username = previousBundle.getString("username");
	    	Config.token = token;
	    	Config.username = username;
	    	
	        //loads the webview in the center of the panel        
	       loadWebView(username, token);
        }
    }
    
    
    /**
     * This is automatically called when the orientation changes.  This saves the webview state so it 
     * can be reloaded after orientation change
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) { 
    	((WebView)findViewById(R.id.webview1)).saveState(outState); 
    	super.onSaveInstanceState(outState);
    }
     
    
    /**
     * This restores the previously saved state... or NOT!  Shouldn't have to manually restore the
     * settings, but I guess for now that'll have to be.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	webView = ((WebView)findViewById(R.id.webview1));
    	webView.restoreState(savedInstanceState);
    	webView.getSettings().setJavaScriptEnabled(true);
    	webView.getSettings().setBuiltInZoomControls(true);
    	webView.getSettings().setAllowFileAccess(true);
    	
    	webView.setWebViewClient(new EamWebviewClient(this)); 
    }
   
    /**
     * Loads the webview for the first time.  Loads the home page
     * @param token
     */
    private void loadWebView(String username, String token) {
		webView = (WebView) findViewById(R.id.webview1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setAllowFileAccess(true);
		 //by default, loads the home page for this user
		webView.loadUrl(Config.getWidgetLoginUrl() + "?username=" + username + "&token=" + URLEncoder.encode(token));
		
		EamWebviewClient webViewClient = new EamWebviewClient(this);
		webView.setWebViewClient(webViewClient);
		
		}
    
    /**
     * This handles all the clicks coming from the android phone itself.  i.e. Back Button, etc.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	//this intercepts the back button when clicked.
    	
    	//2011.05.28 - decided to just not do anything when the back button is clicked because the user
    	//really should navigate within the web control
        //if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
        	//((WebView)findViewById(R.id.webview1)).goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    
    /**
     * This creates the items that pops up when the menu button is clicked on the device.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
		return true;    	
    }

	/**
     * This handles the action that happens afterwards when a menu item is clicked 
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
        switch (item.getItemId()) {
        case R.id.mnuExit:        	
    		NavigationHandler.exitApp(this);
            return true;
        case R.id.mnuHome:
        	MenuHandler.goToHome(this, Config.token);
            return true;
        case R.id.mnuOptions:
        	MenuHandler.goToOptions(this);
        	return true;
        default:
            return false;
        }
    }	
}