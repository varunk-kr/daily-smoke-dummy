package com.krogerqa.api;

import com.krogerqa.api.model.pclAssignment.AssigningPcl;
import com.krogerqa.api.model.pclAssignment.Assignments;
import com.krogerqa.api.model.pclAssignment.Data;
import com.krogerqa.mobile.apps.PermanentContainerLabelHelper;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssignPclHelper {

    private static final String PCL_ERROR_MSG = "Assigning PCL is not successful";
    private static final String PCL_NO_CONTAINERS_MSG = "No containers available to assign PCL";
    private static final String FL_LABEL = Constants.PickCreation.FLOATING_LABEL;
    private static final String PL_LABEL = Constants.PickCreation.PERMANENT_LABEL;
    private static AssignPclHelper instance;
    ApiUtils apiUtils = new ApiUtils();
    PickingServicesHelper pickingServicesHelper = PickingServicesHelper.getInstance();

    private AssignPclHelper() {
    }

    public synchronized static AssignPclHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (AssignPclHelper.class) {
            if (instance == null) {
                instance = new AssignPclHelper();
            }
        }
        return instance;
    }

    public HashMap<String, String> assigningPcl(String scenario, HashMap<String, String> testOutputData, HashMap<String, String> containerMapPcl, String orderId, List<HashMap<String, String>> itemsList) {
        boolean isReusePclScenario = Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_REUSE_PCL_SCENARIO));
        Response response;
        String pcl;
        HashMap<String, String> pclLabelTemperatureMap = new HashMap<>();
        HashMap<String, String> pclIdTemperatureMap = new HashMap<>();
        int containerCountAM = 0;
        if (isReusePclScenario) {
            containerMapPcl.clear();
            testOutputData.remove(ExcelUtils.PCL_ID_TEMPERATURE_MAP);
            testOutputData.remove(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP);
        }
        for (HashMap<String, String> pickRun : pickingServicesHelper.getContainerDetails(orderId, itemsList)) {
            if (pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE).equals(ExcelUtils.AMBIENT))
                containerCountAM++;
            if (pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE).equals(ExcelUtils.AMBIENT) && containerCountAM > 1)
                continue;
            if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_PICK_TO_BAG)) || pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE).equals(ExcelUtils.FROZEN)) {
                pcl = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + FL_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
            } else {
                pcl = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + PL_LABEL + PermanentContainerLabelHelper.generateRandomPclNumber();
            }
            if (pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER).contains(Constants.PickCreation.OVERSIZE)) {
                if(Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_TAKEOVER_WITHOUT_CARRYOVER))){
                    continue;
                }
                   pcl = testOutputData.get(ExcelUtils.STORE_DIVISION_ID) + testOutputData.get(ExcelUtils.STORE_LOCATION_ID) + Constants.PickCreation.OVERSIZE + PermanentContainerLabelHelper.generateRandomPclNumber();
                   pclLabelTemperatureMap.put(pcl, pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE));
                   pclIdTemperatureMap.put(pcl.substring(6), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE));
            }
            pclLabelTemperatureMap.put(pcl, pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE));
            pclIdTemperatureMap.put(pcl.substring(6), pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_TEMPERATURE));
            long seconds = System.currentTimeMillis();
            ArrayList<Assignments> assignmentsArrayList = new ArrayList<>();
            Assignments assignments = new Assignments(pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_UUID), pcl, pickRun.get(Constants.PickCreation.PICK_LISTS_CONTAINER_NUMBER), testOutputData.get(ExcelUtils.STORE_ID), orderId, Integer.parseInt(pickRun.get(Constants.PickCreation.ITEM_PICKED_AMOUNT)), pickRun.get(Constants.PickCreation.NEED_BY_TIME), pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID), (pickRun.get(Constants.PickCreation.NEED_BY_TIME).split("T"))[0], seconds);
            assignmentsArrayList.add(assignments);
            Data data = new Data(12000, assignmentsArrayList);
            AssigningPcl assigningPcl = new AssigningPcl(data);
            response = apiUtils.putPayload(null, null, apiUtils.convertObjectToString(assigningPcl), PropertyUtils.getPermanentContainerLabelEndpoint(), new HashMap<>(), new HashMap<>());
            Assert.assertEquals(response.getStatusCode(), 200, PCL_ERROR_MSG);
            containerMapPcl.put(pcl, pickRun.get(Constants.PickCreation.PICK_LISTS_TROLLEY_ID));
        }
        if (isReusePclScenario) {
            testOutputData.put(ExcelUtils.PCL_ID_TEMPERATURE_MAP, String.valueOf(pclIdTemperatureMap));
            testOutputData.put(ExcelUtils.PCL_LABEL_TEMPERATURE_MAP, String.valueOf(pclLabelTemperatureMap));
            ExcelUtils.writeToExcel(scenario, testOutputData);
        }
        return containerMapPcl;
    }


    public void verifyPclAssignment(String response) {
        JsonPath jsonPath = new JsonPath(response);
        int numberOfContainers = jsonPath.getInt("data.pick-lists.item-containers.size()");
        for (int i = 0; i < numberOfContainers; i++) {
            if (jsonPath.get("data.pick-lists.item-containers[" + i + "].type").equals("forceOverSize")) {
                numberOfContainers = numberOfContainers - 1;
            }
        }
        Assert.assertTrue(numberOfContainers > 0, PCL_NO_CONTAINERS_MSG);
    }

    public HashMap<String, String> assignPclToContainers(String scenario, HashMap<String, String> testOutputData, HashMap<String, String> containerMapPcl, String orderId, List<HashMap<String, String>> itemsList) {
        verifyPclAssignment(pickingServicesHelper.queryItemContainers(orderId));
        return assigningPcl(scenario, testOutputData, containerMapPcl, orderId, itemsList);
    }
}
