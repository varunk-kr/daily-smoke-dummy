package com.krogerqa.web.ui.maps.seamlessportal;

import com.krogerqa.seleniumcentral.framework.sizzlecss.BySizzle;
import org.openqa.selenium.By;

public class SeamlessUpcSearchMap {

    private static SeamlessUpcSearchMap instance;
    BySizzle bySizzle = new BySizzle();

    private SeamlessUpcSearchMap() {
    }

    public synchronized static SeamlessUpcSearchMap getInstance() {
        if (instance == null) {
            synchronized (SeamlessUpcSearchMap.class) {
                if (instance == null) {
                    instance = new SeamlessUpcSearchMap();
                }
            }
        }
        return instance;
    }

    public By searchFieldDefaultState() {
        return bySizzle.css("#SearchBar-input");
    }

    public By searchFieldOpenedState() {
        return bySizzle.css("#SearchBar-input-open");
    }

    public By addToCartButton() {
        return bySizzle.css("button[data-testid='kds-QuantityStepper-ctaButton']");
    }

    public By clearSearchTextButton() {
        return bySizzle.css("button[aria-label='Clear Search Term']");
    }

    public By itemIncrementButton() {
        return bySizzle.css("button[aria-label*='Increment']");
    }

    public By reviewButton() {
        return bySizzle.css("button[data-testid='ModifyModeHeader-review-button']");
    }
}
