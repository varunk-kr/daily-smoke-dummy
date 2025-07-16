package com.krogerqa.mobile.ui.pages.harvester;

import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeMap;
import com.krogerqa.mobile.ui.maps.harvester.HarvesterNativeSelectingMap;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import com.krogerqa.utils.Constants;
import org.openqa.selenium.Keys;
import org.testng.Assert;

public class HarvesterNativePage {
    private static HarvesterNativePage instance;
    MobileCommands mobileCommands = new MobileCommands();
    HarvesterNativeMap harvesterNativeMap = HarvesterNativeMap.getInstance();
    HarvesterNativeSelectingMap harvesterNativeSelectingMap = HarvesterNativeSelectingMap.getInstance();

    private HarvesterNativePage() {
    }

    public synchronized static HarvesterNativePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativePage.class) {
            if (instance == null) {
                instance = new HarvesterNativePage();
            }
        }
        return instance;
    }

    /**
     * To enter the barcode manually for item, container or staging zone
     *
     * @param barcode could be containerId, itemUpc, stagingZone barcode
     */
    public void enterBarcode(String barcode, String screen) {
        if (screen.equalsIgnoreCase(Constants.PickCreation.ENTER_BARCODE_WITH_TOOL_TIP)) {
            mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.toolTipForSelecting());
            mobileCommands.tap(harvesterNativeSelectingMap.toolTipForSelecting());
        }
        mobileCommands.waitForElementVisibility(harvesterNativeMap.enterBarcodeButton());
        try {
            clickOnBarcode();
        } catch (Exception e) {
            clickOnBarcode();
        }
        enterValueInInputField(barcode);
        mobileCommands.waitForElementVisibility(harvesterNativeMap.barcodeContinueButton());
        try {
            mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
        } catch (Exception e) {
            mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
        }
    }

    private void clickOnBarcode() {
        if (mobileCommands.numberOfElements(harvesterNativeMap.enterBarcodeButton()) > 1) {
            mobileCommands.tap(harvesterNativeSelectingMap.enterContainerBarcodeForSubs());
        } else {
            mobileCommands.tap(harvesterNativeMap.enterBarcodeButton());
        }
    }

    public void enterValueInInputField(String barcode) {
        for (int i = 0; i < 5; i++) {
            try {
                mobileCommands.waitForElementVisibility(harvesterNativeMap.inputField());
                mobileCommands.enterText(harvesterNativeMap.inputField(), barcode, true);
                break;
            } catch (Exception e) {
                if (!mobileCommands.elementDisplayed(harvesterNativeMap.inputField())) {
                    mobileCommands.waitForElementVisibility(harvesterNativeMap.enterBarcodeButton());
                    mobileCommands.tap(harvesterNativeMap.enterBarcodeButton());
                }
            }
        }
    }

    public void confirmOverRideStagingZone() {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.overRideWindow());
        mobileCommands.waitForElementClickability(harvesterNativeMap.barcodeContinueButton());
        mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
        waitForToastInvisibility();
    }

    public void enterBarcodeInProgressTrolley(String barcode) {
        mobileCommands.waitForElementVisibility(harvesterNativeSelectingMap.toolTipIcon());
        mobileCommands.tap(harvesterNativeSelectingMap.toolTipIcon());
        try {
            mobileCommands.waitForElementClickability(harvesterNativeMap.enterManuallyButtonInProgressSection());
            mobileCommands.tap(harvesterNativeMap.enterManuallyButtonInProgressSection());
        } catch (Exception | AssertionError e) {
            mobileCommands.tap(harvesterNativeSelectingMap.toolTipIcon());
            mobileCommands.waitForElementClickability(harvesterNativeMap.enterManuallyButtonInProgressSection());
            mobileCommands.tap(harvesterNativeMap.enterManuallyButtonInProgressSection());
        }
        mobileCommands.waitForElementVisibility(harvesterNativeMap.inputField());
        mobileCommands.tap(harvesterNativeMap.inputField());
        mobileCommands.enterText(harvesterNativeMap.inputField(), barcode, true);
        try {
            mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
        } catch (Exception | AssertionError e) {
            mobileCommands.tap(harvesterNativeMap.barcodeContinueButton());
        }
    }

    public void startRunButton() {
        waitForToastInvisibility();
        mobileCommands.waitForElementClickability(harvesterNativeMap.startRunButton());
        mobileCommands.tap(harvesterNativeMap.startRunButton());
    }

    public void clickStartButton(String trolleyId) {
        mobileCommands.waitForElementClickability(harvesterNativeMap.startTrolleyButton(trolleyId));
        mobileCommands.tap(harvesterNativeMap.startTrolleyButton(trolleyId));
        if (!mobileCommands.elementDisplayed(harvesterNativeMap.containerAssignmentPage())) {
            mobileCommands.tap(harvesterNativeMap.startTrolleyButton(trolleyId));
        }
    }

    public void waitForToastInvisibility() {
        mobileCommands.keyPress(String.valueOf(Keys.ENTER));
        mobileCommands.waitForElementInvisible(harvesterNativeMap.toastPopup());
    }

    public void verifyToastMessage(String message) {
        try {
            Assert.assertEquals(message, mobileCommands.getElementText(harvesterNativeMap.toastMessage()));
        } catch (Exception | AssertionError e) {
            waitForToastInvisibility();
        }
    }

    public void printOversizeTrolleyLabels() {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.printTrolleyLabel());
        mobileCommands.tap(harvesterNativeMap.printTrolleyLabel());
    }

    public void clickInProgressTrolleysButton() {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.inProgressTrolleyButton());
        mobileCommands.tap(harvesterNativeMap.inProgressTrolleyButton());
    }

    public void clickInProgressTrolleyMenuButton(String trolleyId) {
        mobileCommands.tap(harvesterNativeMap.inProgressTrolleyMenuButton(trolleyId));
    }

    public void clickTakeOverRunButton() {
        mobileCommands.waitForElementVisibility(harvesterNativeMap.clickTakeOverTrolley());
        mobileCommands.tap(harvesterNativeMap.clickTakeOverTrolley());
    }

    public void backToSelectingScreen() {
        mobileCommands.waitForElement(harvesterNativeMap.backToSelectingButton());
        mobileCommands.tap(harvesterNativeMap.backToSelectingButton());
    }

    public String getProgressTagText(String trolleyId) {
        return mobileCommands.getElementText(harvesterNativeMap.progressTagLabel(trolleyId));
    }

    public String getTakeOverRunLabelText() {
        return mobileCommands.getElementText(harvesterNativeMap.takeOverRunTrolleyLabel());
    }
}
