package com.krogerqa.mobile.ui.pages.toggle;

import com.krogerqa.mobile.ui.maps.toggle.NativeToggleMap;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;

import java.util.HashMap;
import java.util.Map;

import static com.krogerqa.mobile.apps.PermanentContainerLabelHelper.mobileCommands;

public class NativeTogglePage {
    private static NativeTogglePage instance;
    NativeToggleMap nativeToggleMap = NativeToggleMap.getInstance();

    private NativeTogglePage() {
    }

    public static synchronized NativeTogglePage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (NativeTogglePage.class) {
            if (instance == null) {
                instance = new NativeTogglePage();
            }
        }
        return instance;
    }

    public void handleToggle(HashMap<String, String> testOutputData) {
        HashMap<String, String> toggleOptionsMap = new HashMap<>();
        boolean isByobFlow = Boolean.parseBoolean(testOutputData.get(ExcelUtils.ACCEPT_BAG_FEES));
        boolean isRequestTrolleyFromHarvester = Boolean.parseBoolean(testOutputData.get(ExcelUtils.REQUEST_TROLLEY_FROM_HARVESTER));
        String applicationName;
        if (mobileCommands.getElementText(nativeToggleMap.applicationName()).contains("Harvester 2.0")) {
            applicationName = "Harvester";
            toggleOptionsMap.put(Constants.Toggles.BAG_OPTIONALITY, String.valueOf(isByobFlow));
            toggleOptionsMap.put(Constants.Toggles.BAG_OPTIONALITY, String.valueOf(isByobFlow));
            toggleOptionsMap.put(Constants.Toggles.REQUEST_TROLLEY, String.valueOf(isRequestTrolleyFromHarvester));
            mobileCommands.waitForElementVisibility(nativeToggleMap.hamburgerMenuButton());
            mobileCommands.tap(nativeToggleMap.hamburgerMenuButton());
            mobileCommands.waitForElementVisibility(nativeToggleMap.harvesterSettingsOption());
            mobileCommands.tap(nativeToggleMap.harvesterSettingsOption());
            mobileCommands.waitForElementVisibility(nativeToggleMap.harvesterConfigEditorOption());
            mobileCommands.tap(nativeToggleMap.harvesterConfigEditorOption());
        } else {
            applicationName = "Ciao";
            toggleOptionsMap.put(Constants.Toggles.NEW_CUSTOMER_TOGGLE, "false");
            mobileCommands.waitForElementVisibility(nativeToggleMap.hamburgerMenuButton());
            mobileCommands.tap(nativeToggleMap.hamburgerMenuButton());
            mobileCommands.waitForElementVisibility(nativeToggleMap.devTogglesOption());
            mobileCommands.tap(nativeToggleMap.devTogglesOption());
        }
        mobileCommands.tap(nativeToggleMap.searchButton());
        for (Map.Entry<String, String> toggleValue : toggleOptionsMap.entrySet()) {
            if (toggleValue.getValue().equalsIgnoreCase("true")) {
                mobileCommands.enterText(nativeToggleMap.searchBox(), toggleValue.getKey(), true);
                mobileCommands.waitForElementVisibility(nativeToggleMap.selectToggle(toggleValue.getKey()));
                mobileCommands.tap(nativeToggleMap.selectToggle(toggleValue.getKey()));
                if (mobileCommands.getElementAttribute(nativeToggleMap.isEnabledToggle(), "checked").equals("false")) {
                    mobileCommands.tap(nativeToggleMap.applyOverrideValueToggle());
                    mobileCommands.tap(nativeToggleMap.isEnabledToggle());
                }
                backToSearchToggle();
            } else {
                mobileCommands.enterText(nativeToggleMap.searchBox(), toggleValue.getKey(), true);
                mobileCommands.waitForElementVisibility(nativeToggleMap.selectToggle(toggleValue.getKey()));
                mobileCommands.tap(nativeToggleMap.selectToggle(toggleValue.getKey()));
                if (mobileCommands.getElementAttribute(nativeToggleMap.isEnabledToggle(), "checked").equals("true")) {
                    mobileCommands.tap(nativeToggleMap.applyOverrideValueToggle());
                    mobileCommands.tap(nativeToggleMap.isEnabledToggle());
                }
                backToSearchToggle();
            }
        }
        navigateToHomeScreen(applicationName);
    }

    public void navigateToHomeScreen(String applicationName) {
        int browserBackCount = 2;
        if (applicationName.equals("Ciao")) {
            browserBackCount = 1;
        }
        for (int i = 0; i < browserBackCount; i++) {
            mobileCommands.browserBack();
        }
    }

    public void backToSearchToggle() {
        int i = 0;
        mobileCommands.browserBack();
        while (!mobileCommands.elementDisplayed(nativeToggleMap.searchButton())) {
            mobileCommands.browserBack();
            i++;
            if (i > 5) {
                break;
            }
        }
    }
}