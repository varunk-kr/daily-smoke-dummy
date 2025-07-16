package com.krogerqa.web.ui.maps.deMeter;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class DeMeterPickingMap {
    private static DeMeterPickingMap instance;

    BySizzle bySizzle = new BySizzle();

    public synchronized static DeMeterPickingMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DeMeterPickingMap.class) {
            if (instance == null) {
                instance = new DeMeterPickingMap();
            }
        }
        return instance;
    }


    public By clickBeginButton() {
        return bySizzle.css("div:nth-child(2)> div>div>button");
    }

    public By hiddenInputField() {
        return bySizzle.css("[data-testid=scanner]");
    }

    public By getUpc() {
        return bySizzle.css("p.MuiTypography-body1:nth-of-type(4)");
    }

    public By selectOptions() {
        return bySizzle.css("div[data-testid='footer']>div>div>button.MuiButton-outlined");
    }

    public By optionsAvailable() {
        return bySizzle.css("li.MuiButtonBase-root.MuiListItem-gutters");
    }

    public By selectEnterBarcodeManually(int i) {
        return bySizzle.css("li.MuiButtonBase-root.MuiListItem-gutters:nth-of-type(" + i + ")");
    }

    public By enterBarcodeField() {
        return bySizzle.css("div.MuiInputBase-root > input");
    }

    public By enterButton() {
        return bySizzle.css("div.MuiDialogActions-root >div>button.MuiButton-contained");
    }

    public By reviewSelected() {
        return bySizzle.css("button.MuiButtonBase-root.MuiButton-contained span.MuiButton-label");
    }

    public By endSelecting() {
        return bySizzle.css("button.MuiButton-contained span.MuiButton-label");
    }

    public By confirmSelectingPopup() {
        return bySizzle.css("div.MuiGrid-root.MuiGrid-item button.MuiButton-contained ");
    }

    public By takeOthertrolley() {
        return bySizzle.css("button.MuiButton-contained > span");
    }

    public By endAssignmentEarly() {
        return bySizzle.css("div[data-testid='footer'] >div >div>button.MuiButton-outlined>span");
    }

    public By totalTrolleysDisplayed() {
        return bySizzle.css("div.MuiPaper-root.MuiPaper-elevation1");
    }

    public By requiredTrolley(int i) {
        return bySizzle.css("div.MuiPaper-root.MuiPaper-elevation1:nth-of-type(" + i + ")");
    }

    public By clickBeginTrolleyButton(int i) {
        return bySizzle.css("div.MuiPaper-root.MuiPaper-elevation1:nth-of-type(" + i + ") >div button");
    }

    public By confirmFinishButton() {
        return bySizzle.css("div.MuiPaper-root.MuiDialog-paperScrollPaper>div>div> div>button.MuiButton-contained");
    }

    public By firstLocation(int i) {
        return bySizzle.css("div.MuiGrid-root >div.MuiGrid-root div.false:nth-child(" + i + ")");
    }

    public By totalLocations() {
        return bySizzle.css("div.MuiGrid-root >div.MuiGrid-root div.false");
    }

    public By tapSelectingButton() {
        return bySizzle.css("div:nth-child(2) > button");
    }

    public By acceptPopup() {
        return bySizzle.css("div.MuiPaper-root.MuiDialog-paper button.MuiButton-contained");
    }
}
