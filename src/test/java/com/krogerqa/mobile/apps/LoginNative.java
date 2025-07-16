package com.krogerqa.mobile.apps;

import com.krogerqa.mobile.ui.pages.login.LoginNativePage;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.MobileUtils;

public class LoginNative {
    private static LoginNative instance;
    LoginNativePage loginNativePage = LoginNativePage.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();

    private LoginNative() {
    }

    public synchronized static LoginNative getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (LoginNative.class) {
            if (instance == null) {
                instance = new LoginNative();
            }
        }
        return instance;
    }

    public void loginNativeApp(String spmUser, String spmPassword) {
        try {
            loginNativePage.loginNativeApp(spmUser, spmPassword);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.LOGIN, String.valueOf(e));
        }
    }

    public void LoginOktaSignIn(String spmUser, String spmPassword) {
        try {
            loginNativePage.loginNativeApp(spmUser, spmPassword);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.LOGIN, String.valueOf(e));
        }
    }

    public void LoginOktaSignInForTC52(String spmUser, String spmPassword) {
        try {
            loginNativePage.loginForTc52(spmUser, spmPassword);
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.LOGIN, String.valueOf(e));
        }
    }
}
