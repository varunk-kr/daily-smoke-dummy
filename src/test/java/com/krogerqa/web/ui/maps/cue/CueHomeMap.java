package com.krogerqa.web.ui.maps.cue;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class CueHomeMap {

    private static CueHomeMap instance;
    BySizzle bySizzle = new BySizzle();

    private CueHomeMap() {
    }

    public synchronized static CueHomeMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueHomeMap.class) {
            if (instance == null) {
                instance = new CueHomeMap();
            }
        }
        return instance;
    }

    public By searchOrderInputField() {
        return bySizzle.css("input[placeholder='Search']");
    }

    public By orderRow() {
        return bySizzle.css("tr[aria-label='table-body-row']");
    }

    public By orderStatusText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(3)");
    }

    public By paymentStatusText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(9)");
    }

    public By homePageOrderSourceText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(8)");
    }

    public By customerNameText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(6)>span");
    }

    public By stagePercentText() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(4)");
    }

    public By kebabMenu() {
        return bySizzle.css("[aria-label*='Open menu']");
    }

    public By rushOrderCheckBox() {
        return bySizzle.css("[data-test-id*='rushCheckBox'] input");
    }

    public By isRombCheckboxEnabled() {
        return bySizzle.css("tbody.orders-table-body tr[aria-label='table-body-row'] td:nth-of-type(11) input");
    }

    public By clickBatchOrderButton() {
        return bySizzle.css("button[class='kds-Button kds-Button--primary kds-Button--compact']");
    }

    public By rushOrderPopupText() {
        return bySizzle.css("div.flex.flex-col.content-center div >span >span");
    }

    public By clickBatchOrderButtonPopup() {
        return bySizzle.css("button[class='kds-Button kds-Button--primary']");
    }

    public By cancelOrderOption() {
        return bySizzle.css("[data-menu-id*='Cancel Order']");
    }

    public By getTrolleyText() {
        return bySizzle.css("[aria-label='Trolleys page']");
    }

    public By selectCancelOption() {
        return bySizzle.css("kds-radio[data-testid='components.orders-table.order-options.modal.cancel.customer']");
    }

    public By selectCancelOptionAsOos() {
        return bySizzle.css("label[data-testid='out-of-stock']");
    }

    public By cancelButton() {
        return bySizzle.css("*[data-testid='submit-button']");
    }

    public By customerCheckInOption() {
        return bySizzle.css("[data-menu-id*='Customer Check-In']");
    }

    public By parkingSpotInput() {
        return bySizzle.css("input[id='parking-spot']");
    }

    public By vehicleTypeButton() {
        return bySizzle.css("#vehicle-type button:first-of-type");
    }

    public By checkInButton() {
        return bySizzle.css("button[type='submit']");
    }

    public By closeButton() {
        return bySizzle.css("button[aria-label='Close']");
    }

    public By vehicleColor() {
        return bySizzle.css("div[data-color='WHITE']>label>span");
    }

    public By carryoverOrdersSection() {
        return bySizzle.css(".section-title");
    }

    public By alertText() {
        return bySizzle.css("div.ant-modal-body");
    }

    public By datePicker() {
        return bySizzle.css(".ant-picker input");
    }

    public By clickRequestTrolley() {
        return bySizzle.css("kds-button[data-testid='trolley-on-demand'][disabled='false']");
    }

    public By orderDetailsHeader() {
        return bySizzle.css(".order-details-header");
    }

    public By trolleyConfirmButton() {
        return bySizzle.css("kds-button[class='batchOrdersContinueButton hydrated']");
    }

    public By trolleyRequestNotification() {
        return bySizzle.css("div.ant-notification-notice-message");
    }

    public By rushOrderLabelWithLightningIcon() {
        return bySizzle.css("span[data-testid='inline-order-error-button']");
    }

    public By rushOrderDescriptionInAlerts() {
        return bySizzle.css("span[class='flex items-center'] span[class='pl-4 theme-call-out-default font-bold'], div[class='express-order express-message']");
    }

    public By expressOrderDescriptionInAlerts() {
        return bySizzle.css("div[class='ant-modal-body'] div[class='expressOrder-message']");
    }

    public By rushOrderCount() {
        return bySizzle.css("div[class='flex justify-between items-center'] div[class='widget-content'], article[class='expres-order-count']");
    }

    public By expressOrderCount() {
        return bySizzle.css("article[class='express-order-count']");
    }

    public By okButtonPopup() {
        return bySizzle.css("button[class='kds-Button kds-Button--primary']");
    }

    public By totalItemCount() {
        return bySizzle.css(".table.orders-table tr:nth-of-type(2) td:nth-of-type(5)");
    }

    public By rushOrderCheckBox(String orderNum) {
        return bySizzle.css("[data-test-id*='rushCheckBox-" + orderNum + "']");
    }

    public By closeCircle() {
        return bySizzle.css("[data-icon=close-circle]");
    }

    public By serviceCounterTab() {
        return bySizzle.css("a[href*='service-counter-items']");
    }

    public By getStatusForServiceCounter(int i) {
        return bySizzle.css("tr:nth-of-type(" + i + ") td:nth-child(6)");
    }

    public By getItemNameForServiceCounter(int i) {
        return bySizzle.css("tr:nth-of-type(" + i + ") td:nth-child(3)");
    }

    public By serviceCounterItemRowList(String order) {
        return bySizzle.css("td a[data-id='" + order + "']");
    }

}
