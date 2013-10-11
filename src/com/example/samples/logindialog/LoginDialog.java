package com.example.samples.logindialog;

import android.app.Dialog;
import android.content.Context;
import android.net.http.SslError;
import android.util.Log;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.example.samples.R;


/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 9
 * Time: 오후 5:32
 * To change this template use File | Settings | File Templates.
 */

/**
 * LoginDialog has the WebView which shows the login page
 *
 * After login has done successfully, dialog is dismissed by
 * invoking the {@link android.app.Dialog#dismiss()} on WebViewClient
 *
 */
public class LoginDialog extends Dialog{



    public LoginDialog(Context context) {
        super(context);
        WebView webView = initWebView(context);

        init(context, webView);
    }

    public LoginDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private WebView initWebView(Context context) {
        WebView webView = new WebView(context);
        webView.setWebViewClient(
                new LoginWebViewClient(context, this));
        webView.loadUrl(LoginInfo.LOGIN_URL);
        return webView;
    }

    private void init(Context context, WebView webView) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(webView);
        setCancelable(true);
    }


    ////
    ////    inner class
    ////

    private static class LoginWebViewClient extends WebViewClient {
        private static final String TAG = "LoginWebViewClient";
        private static final boolean DEBUG = true;

        private final Context mContext;
        private final Dialog mDialog;


        public LoginWebViewClient(Context context, Dialog dialog) {
            mContext = context;
            mDialog = dialog;
        }


        /**
         * Adjust this method to dismiss the dialog after login.
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {

            if (DEBUG) Log.d(TAG, "finish url = " + url);


            if (url.startsWith(LoginInfo.LOGIN_FAIL_URL)) {
                // Fail to Log-in

                // showErrorDialog
                String loginFailed = mContext.getResources().getString(R.string.login_failed);
                Toast.makeText(mContext, loginFailed, Toast.LENGTH_LONG).show();

                // return to the LOGIN_URL

            } else if (!url.equals(LoginInfo.LOGIN_URL)) {
                // Success to Log-in

                // @note
                // url may be : https://member.imbc.com/Login/MobileLoginProcessNew.aspx

                // TODO    : how to deal with the problem that mDialog has null.
                mDialog.dismiss();

            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            // this will ignore the Ssl error and will go forward to your site
            handler.proceed();
        }

    }// end of LoginWebViewClient



}
