package com.eam.android;


import java.util.List;

import com.eam.android.exceptions.ServerException;
import com.eam.android.model.ServerResponse;
import com.eam.android.presenter.LoginPresenter;
import com.eam.android.utils.Config;
import com.eam.android.utils.NavigationHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

/**
 * This is the login screen.
 * @author Jia
 *
 */
public class EAMAndroid extends Activity implements OnClickListener, LocationListener, OnKeyListener {
	LoginPresenter presenter = new LoginPresenter(this);
	private double longitude = 0;
	private double latitude = 0; 
	private int requestCode;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates("gps", 0, 0, this);
        setContentView(R.layout.main);
        
        //Set up the listener for the button
        Button button = (Button)findViewById(R.id.btnLogin);
        button.setOnClickListener(this);
        
        View txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtPassword.setOnKeyListener(this);
        
        setGPS();
    }	
    
    private void setGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;
        
        for (int i=providers.size()-1; i>=0; i--) {
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) break;
        }
        
        makeUseOfNewLocation(l);
        
    }
    
    
    /**
     * This handles all the clicks coming from the android phone itself.  i.e. Back Button, etc.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	//this intercepts the back button when clicked.
    	//from the login page, back doesn't do anything
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }    
  
    // LogIn Button On click
	@Override
	public void onClick(View v) {		
		EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
		String password = txtPassword.getText().toString();
		txtPassword.setText("");
		
		EditText txtUsername = (EditText)findViewById(R.id.txtUserName);
		String username = txtUsername.getText().toString();
		txtUsername.setText("");
		try {
			ServerResponse response = presenter.submitLogin(username, password, latitude, longitude);

			if (response.token == null)	{//failed login
				TextView view = (TextView)findViewById(R.id.lblErrorMsg);
				view.setVisibility(TextView.VISIBLE);
				view.setText(R.string.invalid_login);
				return;
			}
			
			// Put the username and token into the bundle and pass it to the Home screen
			Intent intent = new Intent(this, com.eam.android.EamHome.class);
			Bundle bundle = new Bundle();
			bundle.putString("token", response.token);
			bundle.putString("username", username);
			intent.putExtras(bundle);
			startActivityForResult(intent, requestCode);
			
		} catch (ServerException e) {
			//error connecting to server.
			TextView view = (TextView)findViewById(R.id.lblErrorMsg);
			view.setVisibility(TextView.VISIBLE);
			view.setText("Server Error");
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Config.KILL_APP) {
    		NavigationHandler.exitApp(this);
    	}
    	//If this is the result code, then we want to stay in the current Activity
    	else if (resultCode == Config.RETURN_TO_LOGIN) {
    		//do nothing
    	}
    	
		super.onActivityResult(requestCode, resultCode, data);
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
            return false;
        case R.id.mnuChangeURL:
        	changeUrl();
        default:
            return false;
        }
    }	
    
    private void changeUrl() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);    
    	builder.setMessage(Config.URL_CHANGE_MSG)
    	       .setCancelable(false)
    	       .setTitle("Alert")
    	       .setPositiveButton("Configure...", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {    	        	   
    	        	   //end this activity and go back to the welcome screen.
    	        	   navigateChangeUrl();
    	           }
    	       })
    	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    private void navigateChangeUrl() {
    	NavigationHandler.changeUrl(this);
    }
    

	public void makeUseOfNewLocation(Location location) {
		if (location == null)
			return;
		
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
		EditText txtLat = (EditText)findViewById(R.id.txtLat);
		txtLat.setText(Double.toString(latitude));
		
		EditText txtLong = (EditText)findViewById(R.id.txtLong);
		txtLong.setText(Double.toString(longitude));		
	}

	@Override
	public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        makeUseOfNewLocation(location);		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// Auto-generated method stub
		
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
}