package com.example.samples.logindialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import com.example.samples.R;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 9
 * Time: 오후 2:11
 * To change this template use File | Settings | File Templates.
 */
public class CloseDialogAfterLoginActivity extends Activity
    implements DialogInterface.OnDismissListener, View.OnClickListener {

    private static final String TAG = CloseDialogAfterLoginActivity.class.getSimpleName();

    private static final boolean DEBUG = true;



    private LoginDialog mLoginDialog;
    private String cookie;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindialog_main);

        Button btn = (Button)findViewById(R.id.button_write);
        btn.setOnClickListener(this);

        initDialog();

    }

    private void initDialog() {

        mLoginDialog = new LoginDialog(this);
        mLoginDialog.setOnDismissListener(this);

    }


    /////////////////////////////////////////////////////////////////////////////////
    /// OnDismissListener
    ///
    ///
    @Override
    public void onDismiss(DialogInterface dialog) {

        String cookieString = CookieManager.getInstance().getCookie(LoginInfo.COOKIE_URL);
        if (isSuccessfulLogin(cookieString)) {
            setCookie(cookieString);
            doSomethingAfterLogin();
        }
    }

    private boolean isSuccessfulLogin(String cookieString) {
        return (cookieString != null && cookieString.indexOf(LoginInfo.COOKIE_KEY_IMBCSESSION) != -1);
    }

    private void doSomethingAfterLogin() {
        // To oo Something
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }


    /////////////////////////////////////////////////////////////////////////////////
    /// View.OnClickListener
    ///
    ///
    @Override
    public void onClick(View v) {
       mLoginDialog.show();
    }
}
