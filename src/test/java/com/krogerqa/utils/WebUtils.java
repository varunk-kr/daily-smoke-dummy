package com.krogerqa.utils;

import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

public class WebUtils {

    public static final Logger LOGGER = LogManager.getLogger(WebUtils.class);
    private static WebUtils instance;
    private static final String RP_SCREENSHOT = "RP_MESSAGE#FILE#{}#{}";
    BaseCommands baseCommands = new BaseCommands();

    private WebUtils() {
    }

    public synchronized static WebUtils getInstance() {
        if (instance == null) {
            synchronized (WebUtils.class) {
                if (instance == null) {
                    instance = new WebUtils();
                }
            }
        }
        return instance;
    }

    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) baseCommands.getWebDriver();
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
    }

    public void captureScreenshot(String applicationName, String exception) {
        LOGGER.info(RP_SCREENSHOT, ((TakesScreenshot) baseCommands.getWebDriver()).getScreenshotAs(OutputType.FILE).getAbsoluteFile(), applicationName);
        Assert.fail(exception);
    }
}
