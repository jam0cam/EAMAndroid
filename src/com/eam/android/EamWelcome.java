package com.eam.android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.eam.android.presenter.WelcomePresenter;
import com.eam.android.utils.Config;
import com.eam.android.utils.NavigationHandler;
import com.eam.android.view.IWelcomeView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EamWelcome extends Activity implements OnClickListener, OnKeyListener, IWelcomeView {

	int requestCode;
	private WelcomePresenter presenter = new WelcomePresenter(this);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //since it's not null, that means it's being returned back to this screen (most likely because we're trying to exit)
        if (savedInstanceState != null)
        	return;
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        		
        setContentView(R.layout.welcome);
        
        //Set up the listener for the button
        Button button = (Button)findViewById(R.id.btnSubmit);
        button.setOnClickListener(this);
        
        View txt = (EditText)findViewById(R.id.txtUrl);
        txt.setOnKeyListener(this);
    	initWebUrl();
    }
	
    
	/**
	 * If this is the first time user, then it will initalize the target-domain to whatever it's supposed to be
	 * @throws IOException 
	 */
	public void initWebUrl() {
		//tries to load the target_domain from a file.  
		try{
			FileInputStream fis = openFileInput(Config.FILE_NAME);
			DataInputStream in = new DataInputStream(fis);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    Config.TARGET_DOMAIN = br.readLine();
		   
		    showLoginPage();
		} catch (FileNotFoundException ex) {
			//if there are exceptions reading the file, then a new file needed.
			//continue to show the current page
		} catch (IOException e) {
			//continue to show the current page
		}
	}
	
	public void showLoginPage() {
		Intent intent = new Intent(this, com.eam.android.EAMAndroid.class);
		startActivityForResult(intent, requestCode);
	}
	
	/**
	 * Since this is the root Activity, all decisions are handled here.  Cannot use the Navigation handler.
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Config.KILL_APP) {    		
    		finish();
    	}
    	else if (resultCode == Config.CHANGE_URL) {
    		//User wants to change URL.  Make sure the file is deleted.
    		try {
    			deleteFile(Config.FILE_NAME);	
    		} catch (Exception e) {
    			//if there is an exception deleting the file, do nothing
    		}    		
    	}    	
    }    

	@Override
	public void onClick(View view) {
		submit();
	}	
	
	public void showErrorMsg() {
        TextView txt = (TextView)findViewById(R.id.lblErrorMsg);
        txt.setVisibility(TextView.VISIBLE);
	}
	
	public void saveUrl(String url) {		
		try {
			FileOutputStream fos;
			fos = openFileOutput(Config.FILE_NAME, Context.MODE_PRIVATE);
			fos.write(url.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			//This will never happen because we made sure there's a file there.  Exit application if this happens
			System.err.println(e.getMessage());
			System.exit(0);
		} catch (IOException e) {
			//This will never happen because we made sure there's a file there.  Exit application if this happens
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
	  if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_ENTER:
                    onClick(null);
            }
        }
		return false;
	}

	@Override
	public void setUrl(String url) {
		EditText txtUrl = (EditText)findViewById(R.id.txtUrl);
		txtUrl.setText(url);
	}

	@Override
	public String getUrl() {
		EditText txtUrl = (EditText)findViewById(R.id.txtUrl);
		String url = txtUrl.getText().toString();		
		return url;
	}

	@Override
	public void submit() {

		String url = getUrl();
		
		//validates that the URL is legit
		if (presenter.submit()) {
			saveUrl(url);		
			showLoginPage();			
		} else {
			showErrorMsg();
		}		
	}
	
    /**
     * This creates the items that pops up when the menu button is clicked on the device.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.login_menu, menu);
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
        default:
            return false;
        }
    }	    
	
}
