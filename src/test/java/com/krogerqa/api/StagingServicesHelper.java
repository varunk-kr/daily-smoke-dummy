package com.krogerqa.api;

import com.krogerqa.api.model.StageContainer.Containers;
import com.krogerqa.api.model.StageContainer.StagingApi;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;

/**
 * Helper class for creating payloads for STAGING AND DE-STAGING services and Handling API calls for various Staging and DeStaging services
 */
public class StagingServicesHelper {
    private static final String STAGE_NOT_SUCCESS = "Staging Tote is not successful";
    private static final String PICK_UP = "pickup";
    private static final String KROGER = "kroger";
    private static final String API = "API";
    private static StagingServicesHelper instance;

    private StagingServicesHelper() {
    }

    public synchronized static StagingServicesHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (StagingServicesHelper.class) {
            if (instance == null) {
                instance = new StagingServicesHelper();
            }
        }
        return instance;
    }

    private static String stageEventPayload(String containerId) {
        Containers containers=new Containers(containerId);
        StagingApi stagingApi=new StagingApi(containers);
        return new ApiUtils().convertObjectToString(stagingApi);
    }

    public void startStageRun(String storeId, String stagingZoneBarcode, String containerId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.PickCreation.X_KROGER_REC_LOCATION_ID, storeId);
        headers.put(Constants.PickCreation.X_KROGER_STAGE_LOCATION_ID, storeId);
        headers.put(Constants.PickCreation.X_KROGER_EUID, PropertyUtils.getStagingZonesUsername());
        headers.put(Constants.PickCreation.X_KROGER_VERTICAL,PICK_UP);
        headers.put(Constants.PickCreation.X_KROGER_TENANT,KROGER);
        headers.put(Constants.PickCreation.X_KROGER_CHANNEL,API);
        headers.put(Constants.PickCreation.X_AUTHENTICATED_USERID, PropertyUtils.getStagingZonesUsername());
        headers.put(Constants.PickCreation.CONTENT_TYPE, Constants.PickCreation.CONTENT_TYPE_VALUE);
        headers.put(Constants.PickCreation.CONTENT_LANGUAGE, Constants.PickCreation.CONTENT_LANGUAGE_VALUE);
        Response response = new ApiUtils().postPayload(PropertyUtils.getStagingZonesUsername(), PropertyUtils.getStagingZonesPassword(), stageEventPayload(containerId), PropertyUtils.getStagingEndpoint(storeId, stagingZoneBarcode), headers, new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, STAGE_NOT_SUCCESS);
    }
}
