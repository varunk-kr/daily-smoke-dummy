package com.krogerqa.web.ui.pages.cue;

import com.krogerqa.api.ApiUtils;
import com.krogerqa.api.model.flexJob.AcceptFlexJobPayload;
import com.krogerqa.api.model.flexJob.Agent;
import com.krogerqa.api.model.flexJob.DateTime;
import com.krogerqa.seleniumcentral.framework.main.BaseCommands;
import com.krogerqa.utils.DateUtils;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import com.krogerqa.web.ui.maps.cue.CueFlexMap;
import com.krogerqa.web.ui.pages.deMeter.DeMeterValidationsPage;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CueFlexPage {
    public static String agecyJobId;
    public static String flexNumberOfitems;
    static String completedStatus = "COMPLETED";
    static String hundredPercentage = "100%";
    static String zeroPercentage = "0%";
    static String percentageSelected = "% Selected";
    static String bearertoken;
    static String acceptFlexJob = "JOB_ACCEPTED";
    static String acceptedStatus = "ACCEPTED";
    static String utcTimeFormat = "UTC";
    static String flexJobId;
    static String acceptingFlexJobIsNotSuccessfull = "Accepting flex job is not successfull";
    private static CueFlexPage instance;
    BaseCommands baseCommands = new BaseCommands();
    CueFlexMap cueFlexMap = CueFlexMap.getInstance();

    private CueFlexPage() {
    }

    public synchronized static CueFlexPage getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CueFlexPage.class) {
            if (instance == null) {
                instance = new CueFlexPage();
            }
        }
        return instance;
    }

    public static void acceptFlexJob() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-partner-id", "PPUP");
        Agent agent = new Agent(PropertyUtils.getDemeterUsername());
        DateTime dateTime = new DateTime(DateUtils.getCurrentTimeInUTCTimeFormat(), utcTimeFormat);
        Response response = new ApiUtils().postPayloadWithBearerToken(returnAcceptFlexJobPayload(getFlexAgencyId(), flexJobId, acceptFlexJob, agent, dateTime), PropertyUtils.getFlexAcceptJobEndpoint(),
                headers, new HashMap<>(), bearertoken);
        Assert.assertEquals(response.getStatusCode(), 201, acceptingFlexJobIsNotSuccessfull);
    }

    public static void getFlexJobAccessToken() {
        HashMap<String, String> formParam = new HashMap<>();
        formParam.put("grant_type", "client_credentials");
        formParam.put("client_id", "flexinternaltesting-db08c817154eadf609a2f7c474afb6f11220244124347436920");
        formParam.put("client_secret", "psbcCItHQdomA_BkaaE1e-AiflNSBuwCvBzIUN1x");
        Response res = new ApiUtils().postPayloadForFormUrlencoded(null, null, formParam, PropertyUtils.getFlexAuthenticationTokenEndpoint());
        bearertoken = res.jsonPath().getString("access_token");
    }

    public static String getFlexAgencyId() {
        Response response = new ApiUtils().getApiWithBearerToken(PropertyUtils.getFlexDetails(flexJobId), new HashMap<>(), new HashMap<>(), bearertoken);
        agecyJobId = response.jsonPath().getString("data.agencyJobId");
        return agecyJobId;
    }

    public static String returnAcceptFlexJobPayload(String agencyJobId, String jobId, String status, Agent agent, DateTime dateTime) {
        AcceptFlexJobPayload acceptFlexJobPayload = new AcceptFlexJobPayload(agencyJobId, jobId, status, agent, dateTime);
        return new ApiUtils().convertObjectToString(acceptFlexJobPayload);
    }

    public void verifyFlexJobAccepted() {
        getFlexJobAccessToken();
        baseCommands.webpageRefresh();
        acceptFlexJob();
        baseCommands.webpageRefresh();
        verifyFlexJobAcceptedStatus();
    }

    public void verifyFlexJobAcceptedStatus() {
        baseCommands.webpageRefresh();
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            if ((baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(flexNumberOfitems) &&
                    baseCommands.getElementText(cueFlexMap.checkShopperId(i)).equalsIgnoreCase(PropertyUtils.getDemeterUsername()))) {
                Assert.assertTrue(baseCommands.getElementText(cueFlexMap.flexJobStatus(i)).equalsIgnoreCase(acceptedStatus));
                break;
            }
        }
    }

    public void verifyFlexJobCreated() {
        openFlexOperationManagementPage();
        requestFlexLabour();
        enterFlexLabourDetails();
        getFlexJobId();
    }

    public void verifyFlexJobAndPerformValidations() {
        verifyFlexJobCreated();
        verifyFlexJobAccepted();
    }

    public void openFlexOperationManagementPage() {
        baseCommands.waitForElementVisibility(cueFlexMap.hamburgerMenu());
        baseCommands.click(cueFlexMap.hamburgerMenu());
        baseCommands.click(cueFlexMap.flexButton());
    }

    public void requestFlexLabour() {
        baseCommands.waitForElementVisibility(cueFlexMap.requestFlexLabourButton());
        baseCommands.click(cueFlexMap.requestFlexLabourButton());
        baseCommands.waitForElementVisibility(cueFlexMap.flexRequestPopupWindow());
    }

    public void enterFlexLabourDetails() {
        baseCommands.enterText(cueFlexMap.enterNumberOfItems(), getNumberOfItems(), true);
        selectStartTime();
        selectCurrentDate();
        baseCommands.click(cueFlexMap.flexLabourSubmitButton());
    }

    private String getNumberOfItems() {
        Random random = new Random();
        return flexNumberOfitems = String.valueOf(random.nextInt(400 - 170) + 170);
    }

    private void selectStartTime() {
        baseCommands.click(cueFlexMap.clickTimeSelectionOption());
        baseCommands.waitForElementVisibility(cueFlexMap.selectStartTime());
        baseCommands.click(cueFlexMap.selectStartTime());
    }

    private void selectCurrentDate() {
        baseCommands.click(cueFlexMap.datePickerIcon());
        baseCommands.click(cueFlexMap.selectTodaysDate(DateUtils.getTodaysDate()));
    }

    public void getFlexJobId() {
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            if (baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(flexNumberOfitems)) {
                baseCommands.click(cueFlexMap.selectviewInformationIcon(i));
                break;
            }
        }
        baseCommands.click(cueFlexMap.viewInformationButton());
        baseCommands.waitForElementVisibility(cueFlexMap.flexJobId());
        flexJobId = baseCommands.getElementText(cueFlexMap.flexJobId());
        baseCommands.webpageRefresh();
    }

    public void validateFlexJobCompletedStatus() {
        openFlexOperationManagementPage();
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            if ((baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(CueFlexPage.flexNumberOfitems) &&
                    baseCommands.getElementText(cueFlexMap.checkShopperId(i)).equalsIgnoreCase(PropertyUtils.getDemeterUsername()))) {
                Assert.assertTrue(baseCommands.getElementText(cueFlexMap.flexJobStatus(i)).equalsIgnoreCase(completedStatus));
                break;
            }
        }
    }

    public void validateTrolleyStatusCompletionInFlexPage(HashMap<String, String> trolleyTemperatureMap) {
        List<String> trolleyList = new ArrayList<>(trolleyTemperatureMap.keySet());
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            if ((baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(CueFlexPage.flexNumberOfitems) &&
                    baseCommands.getElementText(cueFlexMap.checkShopperId(i)).equalsIgnoreCase(PropertyUtils.getDemeterUsername()))) {
                baseCommands.click(cueFlexMap.expandTrolleySectionForFlexJob(i));
                break;
            }
        }
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalTrolleysInJob()); i++) {
            Assert.assertEquals(hundredPercentage, baseCommands.getElementText(cueFlexMap.itemProgressPercentage(i)));
            Assert.assertTrue(trolleyList.contains(baseCommands.getElementText(cueFlexMap.getTrolleyText(i))));
        }
    }


    public void validateTrolleyStatusFromTrolleyPage(HashMap<String, String> testOutputData) {
        baseCommands.getWebDriver().switchTo().window(DeMeterValidationsPage.flexWindowHandle);
        openTrolleysSection();
        searchTrolleysAndValidateStatus(testOutputData);
    }

    private void openTrolleysSection() {
        baseCommands.waitForElementVisibility(cueFlexMap.hamburgerMenu());
        baseCommands.click(cueFlexMap.hamburgerMenu());
        baseCommands.click(cueFlexMap.trolleyButton());
    }

    private void searchTrolleysAndValidateStatus(HashMap<String, String> testOutputData) {
        HashMap<String, String> trolleyMap = ExcelUtils.convertStringToMap(testOutputData.get(ExcelUtils.FLEX_TROLLEY_TEMPERATURE_MAP));
        String takeOverTrolley = testOutputData.get(ExcelUtils.TAKE_OVER_TROLLEY);
        for (String trolley : trolleyMap.keySet()) {
            if (!trolley.equals(takeOverTrolley)) {
                baseCommands.enterText(cueFlexMap.searchTrolleyId(), trolley, true);
                Assert.assertEquals(baseCommands.getElementText(cueFlexMap.percentageFromTrolleypage(getItemsStatusColumn())), hundredPercentage);
                baseCommands.webpageRefresh();
            }
        }
    }

    private int getItemsStatusColumn() {
        int selectedPercentageInTrolley = 0;
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.columnCount()); i++) {
            if (baseCommands.getElementText(cueFlexMap.itemsSelectedColumnCount(i)).equalsIgnoreCase(percentageSelected)) {
                selectedPercentageInTrolley = i;
                break;
            }
        }
        return selectedPercentageInTrolley;
    }

    // Cancel the In-progress trolley from Flex Operations Management screen
    public void cancelInProgressTrolley(String flexWindowHandle) {
        openFlexOperationManagementPage();
        baseCommands.getWebDriver().switchTo().window(flexWindowHandle);
        baseCommands.webpageRefresh();
        for (int i = 1; i <= baseCommands.numberOfElements(cueFlexMap.totalJobsAvailable()); i++) {
            baseCommands.webpageRefresh();
            if ((baseCommands.getElementText(cueFlexMap.selectRequiredJob(i)).contains(CueFlexPage.flexNumberOfitems) && baseCommands.getElementText(cueFlexMap.checkShopperId(i)).equalsIgnoreCase(PropertyUtils.getDemeterUsername()))) {
                baseCommands.click(cueFlexMap.expandTrolleySectionForFlexJob(i));
                break;
            }
        }
        for (int j = 1; j <= baseCommands.numberOfElements(cueFlexMap.totalTrolleysInJob()); j++) {
            if ((baseCommands.getElementText(cueFlexMap.itemProgressPercentage(j)).equalsIgnoreCase(zeroPercentage))) {
                baseCommands.click(cueFlexMap.moreOptionsMenu(j));
                baseCommands.click(cueFlexMap.cancelTrolley());
                baseCommands.click(cueFlexMap.cancelButton());
                break;
            }
        }
    }
}
