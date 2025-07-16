package com.krogerqa.utils;

import com.google.common.collect.ImmutableMap;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import com.krogerqa.seleniumcentral.framework.main.MobileCommands;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

public class MobileUtils {

    public static final Logger LOGGER = LogManager.getLogger(MobileUtils.class);
    private static final String RP_SCREENSHOT = "RP_MESSAGE#FILE#{}#{}";
    private static MobileUtils instance;
    MobileCommands mobileCommands = new MobileCommands();

    private MobileUtils() {
    }

    public synchronized static MobileUtils getInstance() {
        if (instance == null) {
            synchronized (MobileUtils.class) {
                if (instance == null) {
                    instance = new MobileUtils();
                }
            }
        }
        return instance;
    }

    public AndroidDriver getAndroidDriver() {
        return ((AndroidDriver) (new BaseTest().getDriver()));
    }

    /**
     * Clears Chrome data.
     */
    public void resetChrome() {
        List<String> resetChromeArgs = Arrays.asList("clear", "com.android.chrome");
        getAndroidDriver().executeScript("mobile:shell", ImmutableMap.of("command", "pm", "args", resetChromeArgs));
    }

    /**
     * Performs scroll by given percent in the given direction.
     *
     * @param left      left coordinate of the scroll bounding area.
     * @param top       top coordinate of the scroll bounding area.
     * @param direction scrolling direction - up, down, left or right
     * @param percent   size of the scroll as a percentage of the scrolling area, which is 100x100
     */
    public void scrollByPercent(int left, int top, String direction, double percent) {
        getAndroidDriver().executeScript("mobile:scrollGesture", ImmutableMap.of(
                "left", left, "top", top, "width", 100, "height", 100,
                "direction", direction, "percent", percent));
    }

    public void captureScreenshot(String applicationName, String exception) {
        LOGGER.info(RP_SCREENSHOT, ((TakesScreenshot) mobileCommands.getWebDriver()).getScreenshotAs(OutputType.FILE).getAbsoluteFile(), applicationName);
        Assert.fail(exception);
    }
}
