package com.krogerqa.web.ui.maps.cue;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class CueFlexMap {

    private static CueFlexMap instance;
    BySizzle bySizzle = new BySizzle();

    private CueFlexMap() {
    }

    public synchronized static CueFlexMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueFlexMap.class) {
            if (instance == null) {
                instance = new CueFlexMap();
            }
        }
        return instance;
    }

    public By hamburgerMenu() {
        return bySizzle.css("button.kds-Button.kds-Button--primaryInverse.kds-Button--hasIconOnly");
    }

    public By columnCount() {
        return bySizzle.css("thead.table-head.trolleys-table-head >tr >th");
    }

    public By itemsSelectedColumnCount(int i) {
        return bySizzle.css("thead.table-head.trolleys-table-head >tr >th:nth-of-type(" + i + ")");
    }

    public By flexButton() {
        return bySizzle.css("a > span[data-id='components.nav.flex-link']");
    }

    public By trolleyButton() {
        return bySizzle.css("a > span[data-id='components.nav.trolleys-link']");
    }

    public By requestFlexLabourButton() {
        return bySizzle.css("kds-button[data-testid='request-flex-job']");
    }

    public By flexRequestPopupWindow() {
        return bySizzle.css("div.ant-modal-content");
    }

    public By enterNumberOfItems() {
        return bySizzle.css("input[data-testid='item-count-input']");
    }

    public By clickTimeSelectionOption() {
        return bySizzle.css("div[data-testid='select-start-time']");
    }

    public By selectStartTime() {
        return bySizzle.css("div.ant-select-item.ant-select-item-option:nth-of-type(4)");
    }

    public By datePickerIcon() {
        return bySizzle.css("button > kds-icon-calendar.neutral-most-prominent.hydrated");
    }

    public By selectTodaysDate(String todaysDate) {
        return bySizzle.css("td[title='" + todaysDate + "']");
    }

    public By flexLabourSubmitButton() {
        return bySizzle.css("button[type='submit']");
    }

    public By totalJobsAvailable() {
        return bySizzle.css("tr.table-row.flex_jobs-table-row");
    }

    public By selectRequiredJob(int i) {
        return bySizzle.css("tr[aria-label='table-body-row']:nth-of-type(" + i + ") td:nth-of-type(6)");
    }

    public By checkShopperId(int i) {
        return bySizzle.css("tr[aria-label='table-body-row']:nth-of-type(" + i + ") td:nth-of-type(2)");
    }

    public By flexJobStatus(int i) {
        return bySizzle.css("tr[aria-label='table-body-row']:nth-of-type(" + i + ") td:nth-of-type(3)");
    }

    public By selectviewInformationIcon(int i) {
        return bySizzle.css("tr.table-row.flex_jobs-table-row:nth-of-type(" + i + ") td span[role='button']");
    }

    public By viewInformationButton() {
        return bySizzle.css("li[aria-label='select option-View Information']");
    }

    public By flexJobId() {
        return bySizzle.css("div.wrapper-vji-text-body span");
    }

    public By expandTrolleySectionForFlexJob(int i) {
        return bySizzle.css("tr[aria-label='table-body-row']:nth-of-type(" + i + ") td:nth-of-type(1)");
    }

    public By totalTrolleysInJob() {
        return bySizzle.css("table.flex_job_trolleys-table > tbody >tr td:nth-of-type(1)");
    }

    public By getTrolleyText(int i) {
        return bySizzle.css("table.flex_job_trolleys-table > tbody >tr:nth-of-type(" + i + ") td:nth-of-type(1)");
    }

    public By itemProgressPercentage(int i) {
        return bySizzle.css("table.flex_job_trolleys-table > tbody >tr:nth-of-type(" + i + ") td:nth-of-type(3)");
    }

    public By searchTrolleyId() {
        return bySizzle.css("input[placeholder='Search']");
    }

    public By percentageFromTrolleypage(int i) {
        return bySizzle.css("tbody.table-body.trolleys-table-body tr.table-row td:nth-of-type(" + i + ")");
    }

    public By cancelTrolley() {
        return bySizzle.css("li[aria-label='select option-Cancel Trolley']");
    }

    public By moreOptionsMenu(int i) {
        return bySizzle.css("table.flex_job_trolleys-table > tbody >tr:nth-of-type(" + i + ")>td:nth-of-type(6)>div>div>kds-icon-more-options>kds-icon>svg");
    }

    public By cancelButton() {
        return bySizzle.css("kds-button[aria-label='continue-button']");
    }

}
