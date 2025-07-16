package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeOrderLookUpMap {

    private static HarvesterNativeOrderLookUpMap instance;

    private HarvesterNativeOrderLookUpMap() {
    }

    public synchronized static HarvesterNativeOrderLookUpMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeOrderLookUpMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeOrderLookUpMap();
            }
        }
        return instance;
    }

    public By selectOrderLookup() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Order Lookup']");
    }

    public By clickOnInformationLookupButton() {
        return AppiumBy.xpath("//*[@text='Information Lookup']");
    }

    public By orderLookUpEnterBarcodeLink() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Information Lookup Home Screen Content']/android.view.View");
    }

    public By orderLookUpEnterBarcodeToContainerLink() {
        return AppiumBy.accessibilityId("Enter barcode manually button");
    }

    public By orderLookUpBarcodeInput() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Enter manually input']/android.widget.EditText|//android.view.View[@content-desc='Enter manually input']/../android.widget.EditText");
    }

    public By InfoLookUpEnterBarcodeLink() {
        return AppiumBy.xpath("//*[@text='Enter barcode manually']|//*[@text='Barcode']");
    }

    public By continueButton() {
        return AppiumBy.accessibilityId("Right Button in Bottom Sheet");
    }

    public By orderLookUpMoveItems() {
        return AppiumBy.accessibilityId("Move Items Button Order Details");
    }

    public By orderLookUpMoveAllItemsCheckbox() {
        return AppiumBy.accessibilityId("Select All Checkbox");
    }

    public By someItemsCheckbox(String Upc) {
        return AppiumBy.xpath("(//android.widget.TextView[@text='UPC: " + Upc + "']/preceding-sibling::android.view.View)");
    }

    public By getUpcOnItemMovement() {
        return AppiumBy.xpath("(//*[contains(@text,'UPC')])[2]");
    }

    public By getUpcOnItemMovementForPickedItems() {
        return AppiumBy.xpath("(//*[contains(@text,'UPC')])[1]");
    }

    public By nextButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Next Button']");
    }

    public By hamburgerMenuIcon() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Scaffold Navigation Icon']");
    }

    public By toastMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/validation_text_form_field");
    }

    public By moveItems() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Move Items Button']");
    }

    public By continueBtn() {
        return AppiumBy.xpath("//*[@content-desc='Right Button in Bottom Sheet']|//android.view.View[@content-desc='Continue']");
    }

    public By orderDetailsHeader() {
        return AppiumBy.xpath("//*[contains(@text,'Order Details')]");
    }

    public By itemMovementContainer(String container) {
        return AppiumBy.xpath("//android.view.View[@content-desc='Accordion for Container: " + container + "']/android.view.View/android.view.View/android.widget.Button");
    }

    public By noOfContainers() {
        return AppiumBy.xpath("//android.widget.TextView[@text='# of Containers']/following-sibling::android.widget.TextView");
    }

    public By viewButton(String orderId) {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='" + orderId + "']/following-sibling::android.view.View/android.widget.TextView[@text='View'])[1]");
    }

    public By expandContainer(String pclId) {
        return AppiumBy.xpath("//android.view.View[@content-desc='Accordion for Container: " + pclId + "']/android.view.View/android.view.View");
    }

    public By collapseContainer(String pclId) {
        return AppiumBy.xpath("//android.view.View[@content-desc='Accordion for Container: " + pclId + "']/android.view.View/android.view.View");
    }

    public By kebabMenu(String orderId) {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='" + orderId + "']/following-sibling::android.view.View/android.widget.ImageView[@content-desc='More Options Icon'])[1]");
    }

    public By reStageButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Re-Stage']");
    }

    public By SearchVisualOrderID() {
        return AppiumBy.xpath("//android.widget.TextView[contains(@text,'Order')]");
    }

    public By clickOnView() {
        return AppiumBy.xpath("//android.widget.TextView[@text='View']");
    }

    public By locationOverrideContinueButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Right Button in Bottom Sheet']");
    }
}
