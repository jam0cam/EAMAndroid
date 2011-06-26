package com.eam.android.control;

import com.eam.android.utils.NavigationHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EamWebviewClient extends WebViewClient {

	private Activity parent;
	ProgressDialog dialog;
	
	public EamWebviewClient(Activity parent) {
		super();
		this.parent = parent;		
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.contains("Downloader.aspx")) {
			//this is so the download can hapen
			parent.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} else {
			view.loadUrl(url);	
		}
		
		return true;
	}
	
	


	@Override
	public void onPageFinished(WebView view, String url) {
		try {
			if (dialog != null && !parent.isFinishing())
				dialog.dismiss();			
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "An error occurred when trying to dismiss *loading* dialog");
		}


		view.clearCache(true);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if (dialog == null || !dialog.isShowing()) {
			dialog = ProgressDialog.show(parent, "", "Loading. Please wait...", true);			
		}
		
		if (url.toLowerCase().contains("login.aspx") && !url.toLowerCase().contains("widget")) {
			NavigationHandler.goToLogin(parent);
		} 
	}

}
