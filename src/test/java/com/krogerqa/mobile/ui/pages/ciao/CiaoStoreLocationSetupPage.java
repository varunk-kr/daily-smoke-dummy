package com.krogerqa.mobile.ui.pages.ciao;

import com.krogerqa.mobile.ui.maps.ciao.CiaoStoreLocationSetupMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.MobileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;


public class CiaoStoreLocationSetupPage {

    static String DIVISION_NOT_FOUND = "Division not found";
    private static CiaoStoreLocationSetupPage instance;
    MobileCommands mobileCommands = new MobileCommands();
    CiaoStoreLocationSetupMap ciaoStoreSetupMap = CiaoStoreLocationSetupMap.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();

    private CiaoStoreLocationSetupPage() {
    }

    public synchronized static CiaoStoreLocationSetupPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoStoreLocationSetupPage.class) {
            if (instance == null) {
                instance = new CiaoStoreLocationSetupPage();
            }
        }
        return instance;
    }

    public void tapDivisionSpinner() {
        mobileCommands.waitForElementVisibility(ciaoStoreSetupMap.divisionSpinner());
        mobileCommands.tap(ciaoStoreSetupMap.divisionSpinner());
    }

    public void selectDivisionNumber(String division) {
        scrollToDivision(division);
        try {
            mobileCommands.tap(ciaoStoreSetupMap.divisionSpinnerOption(division));
        } catch (Exception | AssertionError e) {
            scrollToDivision(division);
            mobileCommands.tap(ciaoStoreSetupMap.divisionSpinnerOption(division));
        }
    }

    public void enterStoreNumber(String store) {
        mobileCommands.tap(ciaoStoreSetupMap.storeInput());
        mobileCommands.enterText(ciaoStoreSetupMap.storeInput(), store, true);
    }

    public void tapEnterButton() {
        mobileCommands.tap(ciaoStoreSetupMap.enterButton());
    }

    public void submitStoreLocation(String storeDivision, String storeId) {
        try {
            tapDivisionSpinner();
            selectDivisionNumber(storeDivision);
            enterStoreNumber(storeId);
            tapEnterButton();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void selectStoreForMultipleOrders(String storeDivision, String storeId) {
        mobileCommands.tap(ciaoStoreSetupMap.divisionSpinner());
        selectDivisionNumber(storeDivision);
        enterStoreNumber(storeId);
        tapEnterButton();
    }

    public void scrollToDivision(String division) {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        try {
            while (true) {
                String text = mobileCommands.getWebDriver().getPageSource();
                if (text.contains(division)) {
                    break;
                } else {
                    for (int j = 0; j < 2; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail(DIVISION_NOT_FOUND);
        }
    }
}
