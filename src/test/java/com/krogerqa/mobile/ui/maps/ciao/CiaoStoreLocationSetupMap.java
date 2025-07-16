package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoStoreLocationSetupMap {

    private static CiaoStoreLocationSetupMap instance;
    private CiaoStoreLocationSetupMap (){}
    public synchronized static CiaoStoreLocationSetupMap getInstance() {
        if (instance == null) {
            synchronized (CiaoStoreLocationSetupMap.class)
            {
                if (instance == null) {
                    instance = new CiaoStoreLocationSetupMap();
                }
            }
        }
        return instance;
    }

    public By divisionSpinner() {
        return AppiumBy.xpath("//*[@text='Division']/following-sibling::android.view.View/android.widget.TextView[@text='Enter Store']|//android.widget.TextView[@text='Select division']");
    }

    public By divisionSpinnerOption(String division) {
        return AppiumBy.xpath("//*[@text='" + division + "']");
    }

    public By storeInput() {
      return AppiumBy.xpath("//*[@text='Store Number']/following-sibling::android.widget.EditText|//android.widget.TextView[@text='Enter Store']");
    }

    public By enterButton() {
       return AppiumBy.xpath("//*[@text='ENTER']");
    }
}