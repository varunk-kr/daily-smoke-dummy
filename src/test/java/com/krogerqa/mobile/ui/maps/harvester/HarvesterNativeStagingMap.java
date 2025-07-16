package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeStagingMap {

    private static HarvesterNativeStagingMap instance;

    private HarvesterNativeStagingMap() {
    }

    public synchronized static HarvesterNativeStagingMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeStagingMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeStagingMap();
            }
        }
        return instance;
    }

    public By selectStaging() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Staging Nav Card']");
    }

    public By selectInformationLookup() {
        return AppiumBy.xpath("//*[@text='Use Information Lookup']");
    }

    public By LocationDetails() {
        return AppiumBy.xpath("//*[@text='Location Details']");
    }

    public By textIdentifier(String text) {
        return AppiumBy.xpath("//*[contains(@text,'" + text + "')]");
    }


    public By expandAllButton() {
        return AppiumBy.xpath("//*[@text='Expand All']");
    }

    public By stagingArea() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/area']|//android.widget.TextView[@content-desc='Recommended area']");
    }

    public By selectFirstOsContainer() {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='Item UPC'])[1]");
    }

    public By selectFirstSubItem() {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='Sub Item UPC'])[1]");
    }

    public By performOsSubstitution(int i) {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='Item UPC'])[" + i + "]");
    }

    public By osItemsRemaining() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='Item Image']|//*[contains(@text,'UPC:')]");
    }

    public By substituteItemsRemaining() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Item Name']|(//android.widget.TextView[@content-desc='Item UPC'])[1]");
    }

    public By unStagedItems() {
        return AppiumBy.xpath("//*[contains(@text,'Scan to Confirm')]");
    }

    public By locationOverrideContinueButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Confirm Action Bottom Sheet Continue Button']|//android.view.ViewGroup[@content-desc='Continue Button']|//android.view.View[@content-desc='Right Button in Bottom Sheet']");
    }

    public By doneButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Done Staging Button']|//android.view.ViewGroup[@content-desc='Done Button']");
    }

    public By backButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Back Icon']|//android.widget.ImageButton[@content-desc='Navigate up']");
    }

    public By kebabMenu() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='More Options Icon']");
    }

    public By moveItemsOptions() {
        return AppiumBy.id("android:id/content");
    }

    public By moveAllItemsCheckbox() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Select All Checkbox']");
    }

    public By someItemsCheckbox(String Upc) {
        return AppiumBy.xpath("(//android.widget.TextView[@text='UPC: " + Upc + "']/preceding-sibling::android.view.View)");
    }

    public By getUpcOnItemMovement() {
        return AppiumBy.xpath("(//*[contains(@text,'UPC')])[2]");
    }

    public By toastPopup() {
        return AppiumBy.id("com.kroger.harvester.stage:id/toast_message");
    }

    public By nextButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Next Button']");
    }

    public By continueButton() {
        return AppiumBy.accessibilityId("Continue button");
    }

    public By continueBtn() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Right Button in Bottom Sheet']|//android.view.View[@content-desc='Continue']");
    }

    public By scanContainerToMoveText() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Scan Container to Move Banner Text']");
    }

    public By moveItems() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Move Items Button']");
    }

    public By toastMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/validation_text_form_field");
    }

    public By anchorLastStagedContainerInfo() {
        return AppiumBy.id("com.kroger.harvester.stage:id/anchorLastStagedContainerInfo");
    }

    public By stagingTemperatureForBulkStaging() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Temperature type']");
    }

    public By anchorLastStagedContainerLocation() {
        return AppiumBy.id("com.kroger.harvester.stage:id/anchorLastStagedContainerLocation");
    }

    public By reStageContainerContinueButton() {
        return AppiumBy.id("com.kroger.harvester.stage:id/restageContainerContinueButton");
    }

    public By reStageContainerMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/message");
    }

    public By viewButton() {
        return AppiumBy.xpath("//*[@text='View']");
    }

    public By selectSubstitutedOsContainer() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Sub Item UPC']");
    }
}
