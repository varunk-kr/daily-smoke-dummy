package com.krogerqa.mobile.apps;

import com.krogerqa.api.CheckoutCompositeHelper;
import com.krogerqa.mobile.ui.maps.ciao.CiaoCouponsMap;
import com.krogerqa.mobile.ui.maps.ciao.CiaoHomeMap;
import com.krogerqa.mobile.ui.maps.ciao.CiaoSummaryMap;
import com.krogerqa.mobile.ui.pages.ciao.*;
import com.krogerqa.mobile.ui.pages.toggle.NativeTogglePage;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.MobileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.util.HashMap;

import static com.krogerqa.mobile.apps.PermanentContainerLabelHelper.mobileCommands;

public class CiaoDeStagingAndCheckout {
    private static CiaoDeStagingAndCheckout instance;
    CheckoutCompositeHelper checkoutCompositeHelper = CheckoutCompositeHelper.getInstance();
    CiaoStoreLocationSetupPage ciaoStoreSetupPage = CiaoStoreLocationSetupPage.getInstance();
    CiaoHomePage ciaoHomePage = CiaoHomePage.getInstance();
    CiaoDeStagingPage ciaoDestagingPage = CiaoDeStagingPage.getInstance();
    CiaoDeStageCompletePage ciaoDestageCompletePage = CiaoDeStageCompletePage.getInstance();
    CiaoAgeCheckPage ciaoAgeCheckPage = CiaoAgeCheckPage.getInstance();
    CiaoCouponsPage ciaoCouponsPage = CiaoCouponsPage.getInstance();
    CiaoSummaryPage ciaoSummaryPage = CiaoSummaryPage.getInstance();
    CiaoCheckoutCompletePage ciaoCheckoutCompletePage = CiaoCheckoutCompletePage.getInstance();
    CiaoCancelOrderPage ciaoCancelOrderPage = CiaoCancelOrderPage.getInstance();
    CiaoCouponsMap ciaoCouponsMap = CiaoCouponsMap.getInstance();
    CiaoSummaryMap ciaoSummaryMap = CiaoSummaryMap.getInstance();
    CiaoHomeMap ciaoHomeMap = CiaoHomeMap.getInstance();
    NativeTogglePage nativeTogglePage = NativeTogglePage.getInstance();
    MobileUtils mobileUtils = MobileUtils.getInstance();
    static String itemsNotFound = "Items not found";

    private CiaoDeStagingAndCheckout() {
    }

    public static synchronized CiaoDeStagingAndCheckout getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CiaoDeStagingAndCheckout.class) {
            if (instance == null) {
                instance = new CiaoDeStagingAndCheckout();
            }
        }
        return instance;
    }

    public void verifyRemoveFloatingLabels(String scenario) {
        HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
        HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
        if (updatedTestOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP).contains(Constants.PickCreation.FLOATING_LABEL)) {
            ciaoDestagingPage.removeFloatingLabels();
        }
    }

    public void verifyOrderCheckoutInCiao(String scenario, HashMap<String, String> testOutputData) {
        try {
            boolean isEbt = Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT));
            verifyOrderInCiao(testOutputData, isEbt);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                ciaoHomePage.tapStartButton(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID));
            } else {
                ciaoHomePage.tapStartButton(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
            }
            verifyCiaoDeStaging(scenario, testOutputData, isEbt);
            if (!isEbt) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
                    if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                        verifyRemoveFloatingLabels(scenario);
                    }
                }
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                    verifyCiaoCheckoutForInstacartOrder(testOutputData);
                } else {
                    verifyCiaoCheckout(scenario, testOutputData);
                }
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void verifyCancelOrderInCiao(HashMap<String, String> testOutputData) {
        try {
            testOutputData.put(ExcelUtils.CANCEL_FROM_CIAO, String.valueOf(true));
            ExcelUtils.writeToExcel(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
            boolean isEbt = Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT));
            verifyOrderInCiao(testOutputData, Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT)));
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                ciaoCancelOrderPage.cancelOrder(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID));
                ciaoHomePage.tapStartButton(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID));
            } else {
                ciaoCancelOrderPage.cancelOrder(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
                ciaoHomePage.tapStartButton(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
            }
            verifyCiaoDeStaging(testOutputData.get(ExcelUtils.SCENARIO), testOutputData, isEbt);
            if (!isEbt) {
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER)) && !(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER)))) {
                    verifyRemoveFloatingLabels(testOutputData.get(ExcelUtils.SCENARIO));
                }
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                    verifyCiaoCheckoutForInstacartOrder(testOutputData);
                } else {
                    verifyCiaoCheckout(testOutputData.get(ExcelUtils.SCENARIO), testOutputData);
                }
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void verifyOrderInCiao(HashMap<String, String> testOutputData, boolean isEbt) {
        if (!testOutputData.containsKey(ExcelUtils.MULTIPLE_ORDER_COUNT) || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).isEmpty() || testOutputData.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
            ciaoStoreSetupPage.submitStoreLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
        }
        if (testOutputData.containsKey(ExcelUtils.CANCEL_TROLLEY_PCL_LABEL_TEMPERATURE_MAP)) {
            ciaoStoreSetupPage.submitStoreLocation(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
            nativeTogglePage.handleToggle(testOutputData);
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
            ciaoHomePage.verifyOrderDetails(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID), Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK)), isEbt);
        } else {
            ciaoHomePage.verifyOrderDetails(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK)), isEbt);
        }
    }

    public void changeStoreSetup() {
        try {
            ciaoHomePage.tapHamburgerMenuIcon();
            ciaoHomePage.tapChangeButton();
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void setUpStoreForMultipleOrders(HashMap<String, String> testOutputData) {
        try {
            ciaoHomePage.tapHamburgerMenuIcon();
            ciaoHomePage.tapChangeButton();
            ciaoStoreSetupPage.selectStoreForMultipleOrders(testOutputData.get(ExcelUtils.STORE_DIVISION_ID), testOutputData.get(ExcelUtils.STORE_LOCATION_ID));
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void verifyCiaoDeStaging(String scenario, HashMap<String, String> testOutputData, boolean isEbt) {
        HashMap<String, String> updatedTestOutputData;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            if (testOutputData.containsKey(ExcelUtils.TAKE_OVER_WITH_ITEM_MOVEMENT)) {
                updatedTestOutputData = new HashMap<>(testOutputData);
            } else {
                updatedTestOutputData = new HashMap<>(testData);
            }
            HashMap<String, String> map;
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                map = ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.INSTACART_CONTAINER_MAP));
            } else {
                map = ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                ciaoDestagingPage.deStagePclContainer(testOutputData, scenario, ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.BULK_STAGING_CONTAINER_MAP)));
            } else {
                ciaoDestagingPage.scanPally();
                ciaoDestagingPage.deStagePclContainer(testOutputData, scenario, map);
            }
        } else {
            if (testOutputData.containsKey(ExcelUtils.MULTI_CONTAINER_MAP_WITH_NR_ITEM) && (Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_TEMP_COLLAPSE)))) {
                ciaoDestagingPage.scanPally();
                ciaoDestagingPage.deStageContainer(scenario, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.MULTI_CONTAINER_MAP_WITH_NR_ITEM)));
            } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                ciaoDestagingPage.deStagePclContainer(testOutputData, scenario, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.BULK_STAGING_CONTAINER_MAP)));
            } else {
                ciaoDestagingPage.scanPally();
                ciaoDestagingPage.deStageContainer(scenario, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)));
            }
        }
        if (isEbt) {
            ciaoDestageCompletePage.verifyEbtDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
            ciaoDestageCompletePage.removeOrderFromTasks();
        } else if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_INSTACART_ORDER))) {
                ciaoDestageCompletePage.verifyDeStageComplete(testOutputData.get(ExcelUtils.INSTACART_CUE_VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.SPOT), testOutputData);
            } else {
                ciaoDestageCompletePage.verifyDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.SPOT), testOutputData);
            }
        } else {
            ciaoDestageCompletePage.verifyDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.SPOT), testOutputData);
        }
    }

    public void removeAllItemsFromOrder() {
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.kebabMenu());
        mobileCommands.tap(ciaoSummaryMap.kebabMenu());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.removeItemOption());
        mobileCommands.tap(ciaoSummaryMap.removeItemOption());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.removeItemDropdown());
        mobileCommands.tap(ciaoSummaryMap.removeItemDropdown());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.selectOptionForRemove());
        mobileCommands.tap(ciaoSummaryMap.selectOptionForRemove());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.removeButton());
        mobileCommands.tap(ciaoSummaryMap.removeButton());
    }

    public void cancelOrder() {
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.kebabMenu());
        mobileCommands.tap(ciaoSummaryMap.kebabMenu());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.removeItemOption());
        mobileCommands.tap(ciaoSummaryMap.removeItemOption());
        mobileCommands.waitForElementVisibility(ciaoSummaryMap.cancelOrderButton());
        mobileCommands.tap(ciaoSummaryMap.cancelOrderButton());
        mobileCommands.waitForElementVisibility(ciaoHomeMap.hamburgerMenuIcon());
    }

    public void scrollToItemsAndCancelOrder() {
        Actions actions = new Actions(mobileCommands.getWebDriver());
        try {
            for (int i = 0; i < 20; i++) {
                String text = mobileCommands.getWebDriver().getPageSource();
                if (text != null && text.contains(Constants.PickCreation.ALL_ITEMS_FOR_PICKUP)) {
                    break;
                } else {
                    for (int j = 0; j < 4; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail(itemsNotFound);
        }
        mobileCommands.tap(ciaoSummaryMap.getAllItemsCount());
        mobileCommands.scrollDownToFindVisibleText(Constants.PickCreation.QTY);
        int totalItems = Integer.parseInt(((mobileCommands.getElementText(ciaoSummaryMap.getAllItemsCount()).split("\\(")[1]).split("\\)"))[0]);
        for (int i = 0; i < totalItems; i++) {
            if (Integer.parseInt(mobileCommands.getElementText(ciaoSummaryMap.quantity()).split(" ")[1]) == totalQuantity()) {
                break;
            } else {
                removeAllItemsFromOrder();
            }
        }
        cancelOrder();
    }

    public int totalQuantity() {
        return Integer.parseInt(((mobileCommands.getElementText(ciaoSummaryMap.getAllItemsCount()).split("\\(")[1]).split("\\)"))[0]);
    }

    public void verifyCiaoCheckoutForInstacartOrder(HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK)) && ciaoAgeCheckPage.ageRestrictedLabelPresent()) {
            ciaoAgeCheckPage.completeAgeCheck(testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CANCEL_FROM_CIAO))) {
            mobileCommands.waitForElementVisibility(ciaoSummaryMap.completeButton());
            scrollToItemsAndCancelOrder();
        } else {
            mobileCommands.waitForElementVisibility(ciaoSummaryMap.completeButton());
            mobileCommands.tap(ciaoSummaryMap.completeButton());
            ciaoCheckoutCompletePage.tapDoneButton();
        }
    }

    public void verifyCiaoCheckout(String scenario, HashMap<String, String> testOutputData) {
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK)) && ciaoAgeCheckPage.ageRestrictedLabelPresent()) {
            ciaoAgeCheckPage.completeAgeCheck(testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR));
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_OOS_SHORT)) || Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PARTIAL_FULFILL))) {
            ciaoCheckoutCompletePage.tapNextButton();
        }
        try {
            ciaoCheckoutCompletePage.pallyConfirmationCheckout();
        } catch (Exception | AssertionError e) {
            ciaoCheckoutCompletePage.pallyConfirmationCheckout();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_AGE_CHECK)) && ciaoAgeCheckPage.ageRestrictedLabelPresent()) {
            ciaoAgeCheckPage.completeAgeCheck(testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY), testOutputData.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR));
        }
        if (!mobileCommands.elementDisplayed(ciaoCouponsMap.addCouponManuallyLink())) {
            try {
                ciaoCouponsPage.closeBagFeeOption();
            } catch (Exception | AssertionError e) {
                ciaoCouponsPage.closeBagFeeOption();
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_SUBSTITUTE_ITEM_ACCEPT))) {
            selectSubstituteAccept(testOutputData);
            if (!mobileCommands.elementDisplayed(ciaoCouponsMap.addCouponManuallyLink())) {
                try {
                    ciaoCouponsPage.closeBagFeeOption();
                } catch (Exception | AssertionError e) {
                    ciaoCouponsPage.closeBagFeeOption();
                }
            }
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CIAO_SUBSTITUTE_ITEM_REJECT))) {
            selectSubstituteReject();
        }
        if (Double.parseDouble(testOutputData.get(ExcelUtils.CIAO_COUPON_AMOUNT)) > 0) {
            ciaoCouponsPage.addCoupon(testOutputData.get(ExcelUtils.CIAO_COUPON_AMOUNT));
        } else {
            ciaoCouponsPage.tapNextButton();
        }
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.CANCEL_FROM_CIAO))) {
            mobileCommands.waitForElementVisibility(ciaoSummaryMap.completeButton());
            scrollToItemsAndCancelOrder();
        } else {
            ciaoSummaryPage.tapCompleteButton();
            testOutputData.put(ExcelUtils.AMOUNT_PROCESSED, ciaoCheckoutCompletePage.getAmountProcessed());
            ExcelUtils.writeToExcel(scenario, testOutputData);
            if (!Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_MODIFY_ORDER))) {
                ciaoCheckoutCompletePage.completeCheckout(testOutputData, testOutputData.get(ExcelUtils.COUPON_TOTAL), testOutputData.get(ExcelUtils.KROGER_SAVINGS), testOutputData.get(ExcelUtils.CIAO_COUPON_AMOUNT),
                        testOutputData.get(ExcelUtils.ORDER_TOTAL), testOutputData.get(ExcelUtils.CUSTOMER_NAME), Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_CARRYOVER)), Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER)), Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_DELIVERY_ORDER)));
            }
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_COMPOSITE_PICKUP_ORDER))) {
                if (testOutputData.get(ExcelUtils.STORE_BANNER).equalsIgnoreCase(ExcelUtils.BANNER_HARRIS_TEETER)) {
                    testOutputData.put(ExcelUtils.STORE_BANNER, Constants.PickCreation.KROGER_BANNER);
                }
                checkoutCompositeHelper.putFinalizeCheckout(testOutputData.get(ExcelUtils.ORDER_NUMBER), testOutputData);
            }
        }
    }

    private void selectSubstituteAccept(HashMap<String, String> testOutputData) {
        boolean multipleSubstitution = testOutputData.containsKey(ExcelUtils.MULTIPLE_SUBS) && Boolean.parseBoolean(testOutputData.get(ExcelUtils.MULTIPLE_SUBS));
        Actions actions = new Actions(mobileCommands.getWebDriver());
        if (multipleSubstitution) {
            int multipleSubsCount = ExcelUtils.convertStringToList(testOutputData.get(ExcelUtils.MULTIPLE_SUBS)).size();
            int acceptedCount = 0;
            for (int i = 1; i <= multipleSubsCount; i++) {
                ciaoCheckoutCompletePage.selectAcceptMultipleSubstitute(i);
                if (acceptedCount >= multipleSubsCount) {
                    break;
                }
                acceptedCount = acceptedCount + 1;

                while (!ciaoCheckoutCompletePage.isSubsAcceptElementDisplayed(i + 1)) {
                    for (int j = 0; j < 8; j++) {
                        actions.sendKeys(Keys.DOWN).build().perform();
                    }
                    ciaoCheckoutCompletePage.selectAcceptMultipleSubstitute(i + 1);
                }
            }
        } else {
            ciaoCheckoutCompletePage.selectAcceptSubstitute();
        }
        ciaoCouponsPage.tapNextButton();
    }

    private void selectSubstituteReject() {
        ciaoCheckoutCompletePage.selectRejectSubstitute();
        ciaoCouponsPage.tapNextButton();
    }

    public void verifyRejectedItemInCiao(String scenario, HashMap<String, String> testOutputData) {
        try {
            boolean isEbt = Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT));
            verifyOrderInCiao(testOutputData, isEbt);
            ciaoHomePage.selectRejectedSubsOrder(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), isEbt);
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            ciaoDestagingPage.scanPally();
            ciaoDestagingPage.deStageRejectedSubsContainer(testOutputData, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.CONTAINER_MAP)), updatedTestOutputData.get(ExcelUtils.REJECTED_ITEM_CONTAINER));
            if (isEbt) {
                ciaoDestageCompletePage.verifyEbtDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
                ciaoDestageCompletePage.removeOrderFromTasks();
            } else {
                ciaoDestageCompletePage.verifyDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.SPOT), testOutputData);
                verifyCiaoCheckout(scenario, testOutputData);
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }

    public void verifyRejectedItemPclInCiao(String scenario, HashMap<String, String> testOutputData) {
        try {
            boolean isEbt = Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_EBT));
            verifyOrderInCiao(testOutputData, isEbt);
            ciaoHomePage.selectRejectedSubsOrder(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), isEbt);
            HashMap<String, String> testData = ExcelUtils.getTestDataMap(scenario, scenario);
            HashMap<String, String> updatedTestOutputData = new HashMap<>(testData);
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_BULK_STAGING))) {
                ciaoDestagingPage.deStageRejectedSubsContainer(testOutputData, ExcelUtils.convertStringToMap(updatedTestOutputData.get(ExcelUtils.BULK_STAGING_CONTAINER_MAP)), updatedTestOutputData.get(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER));
            } else {
                ciaoDestagingPage.scanPally();
                ciaoDestagingPage.deStageRejectedSubsContainer(testOutputData, ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.PCL_ID_TEMPERATURE_MAP)), updatedTestOutputData.get(ExcelUtils.REJECTED_ITEM_PCL_CONTAINER));
            }
            if (isEbt) {
                ciaoDestageCompletePage.verifyEbtDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID));
                ciaoDestageCompletePage.removeOrderFromTasks();
            } else {
                ciaoDestageCompletePage.verifyDeStageComplete(testOutputData.get(ExcelUtils.VISUAL_ORDER_ID), testOutputData.get(ExcelUtils.SPOT), testOutputData);
                if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PCL_ORDER))) {
                    verifyRemoveFloatingLabels(scenario);
                }
                verifyCiaoCheckout(scenario, testOutputData);
            }
        } catch (Exception | AssertionError e) {
            mobileUtils.captureScreenshot(Constants.ApplicationName.CIAO, String.valueOf(e));
        }
    }
}
