package com.example.samples.logindialog;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 9
 * Time: 오후 5:32
 * To change this template use File | Settings | File Templates.
 */
public interface LoginInfo {

    /**
     * values are for the test.
     */
    public static final String LOGIN_HOST = "http://m.imbc.com";
    public static final String LOGIN_URL = LOGIN_HOST + "/m/User/mLogin.aspx?TemplateID=popup";
    public static final String LOGIN_FAIL_URL = LOGIN_HOST + "/m/User/mLoginError.aspx";

    public static final String COOKIE_URL = "http://m.imbc.com/m/";
    public static final String COOKIE_KEY_IMBCSESSION = "IMBCSession";

}
