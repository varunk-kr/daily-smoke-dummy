package com.krogerqa.mobile.ui.maps.toggle;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class NativeToggleMap {
    private static NativeToggleMap instance;

    private NativeToggleMap() {
    }

    public synchronized static NativeToggleMap getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (NativeToggleMap.class) {
            if (instance == null) {
                instance = new NativeToggleMap();
            }
        }
        return instance;
    }

    public By applicationName() {
        return AppiumBy.xpath("//android.widget.ImageButton[@content-desc='Open navigation drawer']/following-sibling::android.widget.TextView | //android.view.View[@content-desc='Hamburger Menu Button']/../../android.widget.TextView | //android.view.View[@content-desc='Scaffold Navigation Icon']/../following-sibling::android.widget.TextView");
    }

    public By hamburgerMenuButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Hamburger Menu Button']|//android.view.View[@content-desc='Scaffold Navigation Icon']");
    }

    public By devTogglesOption() {
        return AppiumBy.xpath("//*[@text='Dev Toggles']");
    }

    public By harvesterSettingsOption() {
        return AppiumBy.xpath("//*[@text='Settings']");
    }

    public By harvesterConfigEditorOption() {
        return AppiumBy.xpath("//*[@content-desc='Configuration Editor']");
    }

    public By searchButton() {
        return AppiumBy.xpath("//android.view.View[@content-desc='Search Icon']|//android.widget.ImageView[@content-desc='Search']");
    }

    public By searchBox() {
        return AppiumBy.xpath("//android.widget.EditText|//android.widget.ImageView[@content-desc='Search']");
    }

    public By selectToggle(String toggleName) {
        return AppiumBy.xpath("//android.widget.TextView[@text='" + toggleName + "']|//android.widget.TextView[@resource-id='com.kroger.ecommpos.ciao:id/name']");
    }

    public By applyOverrideValueToggle() {
        return AppiumBy.xpath("//android.widget.TextView[@text='Apply Override Values']/following-sibling::android.view.View|//android.widget.Switch[@resource-id='com.kroger.ecommpos.ciao:id/apply_override_switch']");
    }

    public By isEnabledToggle() {
        return AppiumBy.xpath("//android.widget.TextView[@text='isEnabled']/following-sibling::android.view.View|//android.widget.Switch[@resource-id='com.kroger.ecommpos.ciao:id/enabled_switch']");
    }
}