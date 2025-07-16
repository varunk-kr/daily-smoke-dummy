package com.krogerqa.mobile.ui.maps.ciao;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class CiaoDeStagingMap {
    private static CiaoDeStagingMap instance;

    private CiaoDeStagingMap() {
    }

    public synchronized static CiaoDeStagingMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoDeStagingMap.class) {
            if (instance == null) {
                instance = new CiaoDeStagingMap();
            }
        }
        return instance;
    }

    public By containerIdText() {
        return AppiumBy.xpath("(//*[contains(@text,'Loc')]//preceding-sibling::android.widget.TextView)[1] | //android.widget.TextView[contains(@text,'In Progress')]//following-sibling::android.widget.TextView[3]");
    }

    public By oversizeUpcIdText() {
        return AppiumBy.xpath("(//*[contains(@text,'Loc')]//preceding-sibling::android.widget.TextView)[2] | //android.widget.TextView[contains(@text,'In Progress')]//following-sibling::android.widget.TextView[4]");
    }

    public By stagingZoneText() {
        return AppiumBy.xpath("//*[contains(@text,'Locations to Destage')]//following-sibling::android.view.View//following-sibling::android.widget.TextView | //android.widget.TextView[contains(@text,'In Progress')]//following-sibling::android.widget.TextView[2]");
    }

    public By stagingZoneTextForReStaging(int i) {
        return AppiumBy.xpath("(//android.widget.TextView[@text='Locations Destaged']/../following-sibling::android.view.View/android.view.View/android.widget.TextView)[" + i + "] | //android.widget.TextView[contains(@text,'In Progress')]//following-sibling::android.widget.TextView[2]");
    }

    public By containerKebabMenu() {
        return AppiumBy.xpath("(//*[contains(@text,'Loc')]//preceding-sibling::android.widget.TextView)[1]/..//android.view.View/android.view.View|(//*[contains(@text,'Loc')]//following-sibling::android.widget.Button)[1] | //android.widget.TextView[contains(@text,'In Progress')]//following-sibling::android.view.View[3]");
    }

    public By stagingZoneKebabMenu() {
        return AppiumBy.xpath("//android.widget.TextView[contains(@text,'In Progress')]/following-sibling::android.view.View/android.widget.Button/preceding-sibling::android.view.View[not(contains(@content-desc,'Expand or collapse'))]");
    }


    public By removeFloatingLabelButton() {
        return AppiumBy.xpath("//android.widget.Button");
    }

    public By addContainerManuallyOption() {
        return AppiumBy.xpath("//*[@text='Add container manually']|//*[@text='Destage container manually']|//*[contains(@text,'Destage Manually')]");
    }

    public By labelDamagedRadioButton() {
        return AppiumBy.xpath("(//android.widget.RadioButton)[1]");
    }

    public By dialogOkButton() {
        return AppiumBy.xpath("//*[@text='OK']");
    }

    public By removeManually() {
        return AppiumBy.xpath("//*[@text='Remove Manually']");
    }

    public By noBarcodeAvailable() {
        return AppiumBy.xpath("//android.widget.TextView[@text='No barcode available to scan']");
    }

    public By selectDropdown() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Select']");
    }

    public By completeButton() {
        return AppiumBy.xpath("//*[contains(@text,'Complete')]");
    }

    public By removeButton() {
        return AppiumBy.xpath("//*[contains(@text,'REMOVE')]");
    }

    public By deStageSuccessToastMessage() {
        return AppiumBy.xpath("//*[contains(@text,'Success')]");
    }

    public By getNumberOfContainersToDeStage() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Destaged']//following-sibling::android.widget.TextView|//android.widget.TextView[@text='Locations Destaged']//following-sibling::android.widget.TextView | //android.widget.TextView[contains(@text,'In Progress')]");
    }

    public By containerKebabMenuRejectedItems() {
        return AppiumBy.xpath("//*/android.view.View[4]/android.widget.Button");
    }

    public By scanPallyButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Pally Scan Ahead Screen Skip Button']");
    }

    public By pallyNotNeededOption() {
        return AppiumBy.xpath("//android.widget.RadioButton[@content-desc='EPosRadioButton Pally Not Needed']");
    }

    public By tapOkButton() {
        return AppiumBy.xpath("//android.widget.TextView[@text='OK']");
    }
}
