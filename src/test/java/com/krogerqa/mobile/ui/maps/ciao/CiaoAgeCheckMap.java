package com.krogerqa.mobile.ui.maps.ciao;


import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoAgeCheckMap {

    private static CiaoAgeCheckMap instance;
    private CiaoAgeCheckMap (){}
    public synchronized static CiaoAgeCheckMap getInstance() {
        if (instance == null) {
            synchronized (CiaoAgeCheckMap.class)
            {
                if (instance == null) {
                    instance = new CiaoAgeCheckMap();
                }
            }
        }
        return instance;
    }

    public By ageRestrictionIcon() {
        return AppiumBy.accessibilityId("age restriction icon");
    }

    public By dateOfBirthInput() {
        return AppiumBy.className("android.widget.EditText");
    }

    public By hamburgerMenuIcon() {
        return AppiumBy.accessibilityId("scan license menu icon");
    }

    public By verifyIdManually() {
        return AppiumBy.xpath("//*[@text='Verify ID Manually']");
    }

    public By verifyIdButton() {
        return AppiumBy.xpath("//*[@text='Verify ID']");
    }
}
