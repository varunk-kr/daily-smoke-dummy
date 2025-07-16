package com.krogerqa.mobile.ui.maps.harvester;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HarvesterNativeSelectingMap {

    private static HarvesterNativeSelectingMap instance;

    private HarvesterNativeSelectingMap() {
    }

    public synchronized static HarvesterNativeSelectingMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterNativeSelectingMap.class) {
            if (instance == null) {
                instance = new HarvesterNativeSelectingMap();
            }
        }
        return instance;
    }

    public By qtyMinusButton() {
        return AppiumBy.xpath("//android.widget.Button[@content-desc='Minus button in the stepper']");
    }

    public By doneSubstitutionButton() {
        return AppiumBy.accessibilityId("Done Substituting Button");
    }

    public By selecting() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Selecting Nav Card']");
    }

    public By toastPopup() {
        return AppiumBy.id("com.kroger.harvester.stage:id/toast_message");
    }

    public By itemUpcText() {
        return AppiumBy.xpath("(//android.widget.TextView[@content-desc='Item UPC'])[1]|(//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/upcTextView'])[1]");
    }

    public By containerNumber() {
        return AppiumBy.xpath("//*[@resource-id='com.kroger.harvester.stage:id/containerValue']");
    }

    public By newSectionTrolleysText() {
        return AppiumBy.xpath("//*[contains(@text,'New')]");
    }

    public By hamburgerMenuIcon() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Scaffold Navigation Icon']");
    }

    public By selectStore() {
        return AppiumBy.xpath("//*[contains(@text,'Store')]");
    }

    public By substituteOoSButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Item Unavailable button']");
    }

    public By confirmButton() {
        return AppiumBy.accessibilityId("Right Button in Bottom Sheet");
    }

    public By itemNotReadyOOS() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Substitute/OoS']");
    }

    public By itemUnavailableButton() {
        return AppiumBy.accessibilityId("Item Unavailable button");
    }

    public By itemNotReadyLayout() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Item Not Ready']");
    }

    public By markOutOfStockButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Mark Out of Stock Button']");
    }

    public By quantityDialogEnterButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc=\"Kds Spinner\"]/android.view.View");
    }

    public By quantityDialogToFill() {
        return AppiumBy.xpath("//android.widget.ScrollView/android.view.View[1]");
    }

    public By shortRemainingItemsMenuOption() {
        return AppiumBy.accessibilityId("Partially Fulfill Button");
    }

    public By addNewButton() {
        return AppiumBy.accessibilityId("+ Add New button");
    }

    public By continueButton() {
        return AppiumBy.accessibilityId("Continue Button");
    }

    public By continueButtonForBagsConfirmation() {
        return AppiumBy.accessibilityId("Continue");
    }


    public By completeRunButton() {
        return AppiumBy.xpath("//android.view.ViewGroup[@content-desc='Finish run button']|//android.view.View[@content-desc='Finish run button']");
    }

    public By screenTitle() {
        return AppiumBy.xpath("//*[@resource-id='com.kroger.harvester.stage:id/action_bar']//android.widget.TextView|//android.view.View[@content-desc='Back Icon']/../following-sibling::android.widget.TextView");
    }

    public By goToSelectingButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='View Trolley List'] | //android.view.View[@content-desc='View Trolley List button'] | //android.view.ViewGroup[@content-desc='Go To Selecting']|//android.view.View[@content-desc='Go To Selecting']");
    }

    public By completeButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Complete button']");
    }

    public By toolTipIcon() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='More Options Icon']");
    }

    public By toolTipForSelecting() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Item Unavailable']/following-sibling::android.widget.ImageView|//android.widget.TextView[@text='Selecting']/following-sibling::android.widget.ImageView[@content-desc='More Options Icon']|//android.widget.TextView[@text='Fulfill Item']/following-sibling::android.widget.ImageView");
    }

    public By enterWeightManuallyOption() {
        return AppiumBy.xpath("//*[@text='Enter weight manually for all items']");
    }

    public By weighScreenUpcText() {
        return AppiumBy.id("com.kroger.harvester.stage:id/itemUPCTextView");
    }

    public By weightItemsText() {
        return AppiumBy.id("com.kroger.harvester.stage:id/selecting_bottom_nav_weigh");
    }

    public By submitWeightButton() {
        return AppiumBy.accessibilityId("Submit Weight Button");
    }

    public By containerNumberText() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Container Display Text']");
    }

    public By upNextText() {
        return AppiumBy.xpath("//*[contains(@text,'Up Next')]");
    }

    public By kebabMenuIcon() {
        return AppiumBy.xpath("//android.widget.ImageView[@content-desc='Scan Container Card More Options']");
    }

    public By moveItemsToAnotherContainerOption() {
        return AppiumBy.id("android:id/content");
    }

    public By toastMessage() {
        return AppiumBy.id("com.kroger.harvester.stage:id/validation_text_form_field");
    }

    public By getContainerCountInTrolley(String trolleyId) {
        return AppiumBy.xpath("//android.widget.TextView[@text=\"Trolley #" + trolleyId + "\"]//following-sibling::android.widget.TextView[contains(@text,'containers')]");
    }

    public By preWeighPclLabel() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc=\"Container Display Text\" and contains(@text,'-')]");
    }

    public By getPclLabelFromUi() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/containerDisplay'] | //android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/containerValue'] | //android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/containerDisplayText']| //android.widget.TextView[@content-desc='Container Display Text']");
    }

    public By kebabIconSubstitution() {
        return AppiumBy.id("com.kroger.harvester.stage:id/scanContainerMenu");
    }

    public By moveToContainerSubstitution() {
        return AppiumBy.xpath("//*[contains(@text,'Move to')]");
    }

    public By existingContainer() {
        return AppiumBy.xpath("//android.view.ViewGroup[contains(@content-desc,'01')]//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/kds_button_text']|//android.view.ViewGroup[contains(@content-desc,'02')]//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/kds_button_text']");
    }

    public By yesExitButton() {
        return AppiumBy.xpath("//android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/kds_button_text' and @text='Yes, Cancel']");
    }

    public By itemsQtyText() {
        return AppiumBy.id("com.kroger.harvester.stage:id/quantityText");
    }


    public By mafpItemQuantity() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Item Quantity'] | //android.widget.TextView[@resource-id='com.kroger.harvester.stage:id/itemBadge']");
    }

    public By tempSkittleText() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Container Display Text']/preceding-sibling::android.widget.TextView[not(contains(@text,'Container'))]");
    }

    public By storeChangeButton() {
        return AppiumBy.id("com.kroger.harvester.stage:id/drawer_secondary_headline");
    }

    public By getBagCountInTrolley(String trolleyId) {
        return AppiumBy.xpath("//*[contains(@text,'" + trolleyId + "')]/following-sibling::android.view.View[@content-desc='Tag']/android.widget.TextView[@content-desc='Tag Text']");
    }

    public By bagsConfirmation() {
        return AppiumBy.xpath("//*[@text='Bag Confirmation']");
    }

    public By bagsConfirmationCheckbox() {
        return AppiumBy.accessibilityId("Not Checked Checkbox. ");
    }

    public By bagsIcon() {
        return AppiumBy.id("com.kroger.harvester.stage:id/bagIcon");
    }

    public By bagCountContainerAssignment(int bagCount) {
        return AppiumBy.xpath("//(android.widget.ImageView[@content-desc='Bag icon'])+[" + bagCount + "]");
    }

    public By containerWithBags(String container) {
        return AppiumBy.xpath("//*[contains(@text,'containers have bags')]/following-sibling::android.view.View[1]/android.widget.TextView[contains(@text,'" + container + "')]");
    }

    public By itemsToPick() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Item Image']|//*[@text='Aisle']");
    }

    public By scannedBarcodesButton() {
        return AppiumBy.accessibilityId("I've Scanned all Barcodes Button");
    }

    public By markAsFullySelected() {
        return AppiumBy.accessibilityId("Mark As Fully Selected Button");
    }

    public By partialFulfillButton() {
        return AppiumBy.accessibilityId("Partially Fulfill Button");
    }

    public By requestTrolleyButton() {
        return AppiumBy.accessibilityId("Request Trolley");
    }

    public By gotItButton() {
        return AppiumBy.accessibilityId("Got It Button");
    }

    public By itemNotReadySubstitution() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Substitute/OoS']");
    }

    public By noSuggestionAvailableText() {
        return AppiumBy.xpath("//android.widget.TextView[@text='No Suggested Subs']");
    }

    public By orderIdText() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Order ID']");
    }

    public By itemUpcTextList() {
        return AppiumBy.xpath("//android.widget.TextView[@content-desc='Item UPC']");
    }

    public By enterContainerBarcodeForSubs() {
        return AppiumBy.xpath("//*[@text='Enter Container Barcode']");
    }
}
