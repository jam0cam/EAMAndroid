package com.eam.android.control;

import android.content.Context;
import android.net.MailTo;
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
        } else if(url.startsWith("mailto:")){
            MailTo mt = MailTo.parse(url);
            Intent i = newEmailIntent(parent, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
            parent.startActivity(i);
            view.reload();
            return true;
        } else if(url.startsWith("tel:")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            parent.startActivity(intent);
            view.reload();
            return true;
        }
        else {
            view.loadUrl(url);
        }

        return true;
    }

    public static Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
          Intent intent = new Intent(Intent.ACTION_SEND);
          intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
          intent.putExtra(Intent.EXTRA_TEXT, body);
          intent.putExtra(Intent.EXTRA_SUBJECT, subject);
          intent.putExtra(Intent.EXTRA_CC, cc);
          intent.setType("message/rfc822");
          return intent;
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
