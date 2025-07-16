package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeMap {

    private static HarvesterNativeMap instance;

    private HarvesterNativeMap() {
    }

    public synchronized static HarvesterNativeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeMap();
            }
        }
        return instance;
    }

    public By enterBarcodeButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Enter Container Barcode Manually']|//*[@text='Enter Barcode']|//*[@text='Enter barcode manually']|//android.widget.TextView[@content-desc='Enter Item Barcode Manually']|//*[@text='Enter Container Barcode']|//*[@text='Enter Item Barcode']");
    }

    public By orderType() {
        return AppiumBy.xpath("(//android.view.View[@content-desc='Tag'])[1]/android.widget.TextView");
    }

    public By orderStatus() {
        return AppiumBy.xpath("(//android.view.View[@content-desc='Tag'])[2]/android.widget.TextView");
    }

    public By backButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Back Icon']|//android.widget.ImageButton[@content-desc='Navigate up']");
    }

    public By inputField() {
        return AppiumBy.xpath("//*[contains(@text,'To enter text in this field, type')] | //*[@resource-id='com.kroger.harvester.stage:id/generic_input'] | //android.widget.EditText[@text='Barcode'] | //android.widget.EditText[@text='Item barcode'] | //android.widget.TextView[@text='Enter Item Barcode']//following-sibling::android.view.View/android.widget.EditText | //android.widget.TextView[@content-desc='Bottom Sheet Header Text']//following-sibling::android.view.View/android.widget.EditText | //android.widget.EditText");
    }

    public By enterButton() {
        return AppiumBy.xpath("//*[@text='Complete']|//android.view.View[@content-desc='Positive icon']");
    }

    public By toastPopup() {
        return AppiumBy.id("com.kroger.harvester.stage:id/toast_message");
    }

    public By toastMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/validation_text_form_field");
    }

    public By barcodeContinueButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Right Button in Bottom Sheet']|//*[@text='Continue']|//android.view.View[contains(@content-desc,'Enter barcode')]");
    }

    public By overRideWindow() {
        return AppiumBy.accessibilityId("Bottom Sheet Header Text");
    }

    public By startTrolleyButton(String trolleyId) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"Trolley #" + trolleyId + "\"]//following-sibling::android.widget.TextView[contains(@text,'Start')]");
    }

    public By containerAssignmentPage() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Container Assignment']");
    }

    public By trolleyIdText(String trolleyId) {
        return AppiumBy.xpath("//android.widget.TextView[@text='Trolley #' " + trolleyId + " ]");
    }

    public By startRunButton() {
        return AppiumBy.xpath("//android.view.ViewGroup[@content-desc='Start run button'] | //android.view.ViewGroup[@content-desc='Start Run button'] | //android.view.View[@content-desc='Start Run button']");
    }

    public By startRunForSingleThreadStore() {
        return AppiumBy.accessibilityId("Stage items button");
    }

    public By printTrolleyLabel() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Print Trolley Labels button']");
    }

    public By getTotalContainer() {
        return AppiumBy.xpath("//android.view.View[contains(@content-desc,'Container Assignment List Item at Index')]");
    }

    public By getTotalSkittleTemperatureCount() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Estimated Duration Text']//following-sibling::android.widget.TextView[not(contains(@content-desc,'Trolley'))]");
    }

    public By getSkittleTemperatureText(int i) {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Estimated Duration Text']//following-sibling::android.widget.TextView[not(contains(@content-desc,'Trolley'))][" + i + "]");
    }

    public By getContainerText(int i) {
        return AppiumBy.xpath("(//android.view.View[contains(@content-desc,'Container Assignment List Item at Index')])[" + i + "]//following-sibling::android.widget.TextView[1]");
    }

    public By inProgressTrolleyButton() {
        return AppiumBy.xpath("//*[contains(@text,'In Progress')]");
    }

    public By inProgressTrolleyMenuButton(String trolleyId) {
        return AppiumBy.xpath("//*[contains(@text,'Trolley #" + trolleyId + "')]/following-sibling::android.view.View[contains(@content-desc,'clickable')][1]/android.widget.ImageView");
    }

    public By clickTakeOverTrolley() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Harvester More Options Drop Down Menu Item At Index: 0']/following-sibling::android.widget.TextView");
    }

    public By takeOverRunTrolleyLabel() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Harvester More Options Drop Down Menu Item At Index: 0']/following-sibling::android.widget.TextView");
    }

    public By enterManuallyButtonInProgressSection() {
        return AppiumBy.xpath("//*[@text='Enter Manually']");
    }

    public By backToSelectingButton() {
        return AppiumBy.accessibilityId("Navigate up");
    }

    public By progressTagLabel(String trolleyId) {
        return AppiumBy.xpath("//*[contains(@text,'Trolley #" + trolleyId + "')]/following-sibling::android.view.View[1]/android.widget.TextView[@content-desc='Tag Text']");
    }

    public By orderId() {
        return AppiumBy.id("com.kroger.harvester.stage:id/containerDisplay");
    }

    public By viewOSOrderDetails() {
        return AppiumBy.xpath("//android.widget.TextView[@text='View']");
    }


    public By getSkittleTemperature(String trolleyId) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"Trolley #" + trolleyId + "\"]//preceding-sibling::android.widget.TextView");
    }
}