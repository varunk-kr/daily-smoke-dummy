package com.krogerqa.api;

import com.krogerqa.utils.Constants;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;

public class HarvesterServicesHelper {
    public static final String AMBIENT_TEMPERATURE = "AMBIENT";
    public static final String REFRIGERATED_TEMPERATURE = "REFRIGERATED";
    public static final String FROZEN_TEMPERATURE = "FROZEN";
    private static final String STAGING_ZONES_UNAVAILABLE = "Unable to retrieve staging zones from the API";
    private static final String LOCATION_ID = "locationId";
    private static final String STAGING_ZONES_DATA = "data.stagingZones[";
    private static final String BARCODE = "].barcode";
    private static final String STAGING_ZONES_NOT_FOUND = "Valid staging zones not found for ";
    private static final String HOT_TEMPERATURE = "HOT";
    public static String ambientStagingZone;
    public static String refrigeratedStagingZone;
    public static String frozenStagingZone;
    public static String hotStagingZone;
    private static HarvesterServicesHelper instance;


    private HarvesterServicesHelper() {
    }

    public synchronized static HarvesterServicesHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (HarvesterServicesHelper.class) {
            if (instance == null) {
                instance = new HarvesterServicesHelper();
            }
        }
        return instance;
    }


    /*** GET API for fetching staging zones based on store id
     * @param storeId store location Id
     * @return response for the GET API */
    private Response triggerStagingZonesApi(String storeId, HashMap<String, String> testOutputData) {
        String stagingEndpoint;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_ZONE))) {
            stagingEndpoint = PropertyUtils.getHarvesterAnchorStagingZonesEndpoint().replace(LOCATION_ID, storeId);
        } else {
            stagingEndpoint = PropertyUtils.getHarvesterStagingZonesEndpoint().replace(LOCATION_ID, storeId);
        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.PickCreation.X_KROGER_EUID, PropertyUtils.getStagingZonesUsername());
        headers.put(Constants.PickCreation.X_KROGER_CHANNEL, "API");
        headers.put(Constants.PickCreation.X_KROGER_TENANT, "kroger");
        headers.put(Constants.PickCreation.X_KROGER_VERTICAL, "pickup");
        headers.put(Constants.PickCreation.X_AUTHENTICATED_USERID, PropertyUtils.getStagingZonesUsername());
        Response response = new ApiUtils().getApi(PropertyUtils.getStagingZonesUsername(), PropertyUtils.getStagingZonesPassword(), null, stagingEndpoint, headers, new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, STAGING_ZONES_UNAVAILABLE);
        return response;
    }

    /**
     * Verifies valid staging zone barcode format: 1 uppercase alphabet, 5 digits, followed by 1 uppercase alphabet
     *
     * @param barcode staging zone barcode fetched from GET staging zones API
     * @return true if staging zone is in the valid format, else false
     */
    private boolean verifyValidBarcodeFormat(String barcode, HashMap<String, String> testOutputData) {
        String pattern;
        if (Boolean.parseBoolean(testOutputData.get(ExcelUtils.IS_ANCHOR_STAGING_ZONE))) {
            pattern = "([A-Za-z0-9]+(-[A-Za-z0-9]+)+)";
        } else {
            pattern = "[A-Z]\\d{5}[A-Z]";
        }
        return barcode.matches(pattern);
    }


    /**
     * Gets staging zones for each temperature type
     *
     * @param storeId location id
     * @return Map containing temperature type and staging zone pairs
     */
    public HashMap<String, String> getStagingZones(String storeId, HashMap<String, String> testOutputData) {
        String scenario = testOutputData.get(ExcelUtils.SCENARIO);
        JsonPath jsonPath = new JsonPath(triggerStagingZonesApi(storeId, testOutputData).asString());
        jsonPath.get("data.stagingZones");
        HashMap<String, String> stagingZones = new HashMap<>();

        for (int i = 0; i < jsonPath.getInt("data.stagingZones.size()"); i++) {
            if (verifyValidBarcodeFormat(jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE), testOutputData)) {
                String temperatureType = jsonPath.getString(STAGING_ZONES_DATA + i + "].temperatureType");
                switch (temperatureType) {
                    case "AM":
                        stagingZones.put(AMBIENT_TEMPERATURE, jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE));
                        ambientStagingZone = jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE);
                        break;
                    case "RE":
                        stagingZones.put(REFRIGERATED_TEMPERATURE, jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE));
                        refrigeratedStagingZone = jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE);
                        break;
                    case "FR":
                        stagingZones.put(FROZEN_TEMPERATURE, jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE));
                        frozenStagingZone = jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE);
                        break;
                    case "HOT":
                        stagingZones.put(HOT_TEMPERATURE, jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE));
                        hotStagingZone = jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE);
                        break;
                }
            }
            if (stagingZones.containsKey(AMBIENT_TEMPERATURE) && stagingZones.containsKey(REFRIGERATED_TEMPERATURE) && stagingZones.containsKey(FROZEN_TEMPERATURE) && stagingZones.containsKey(HOT_TEMPERATURE)) {
                break;
            }
        }
        if (stagingZones.size() < 3) {
            Assert.fail(STAGING_ZONES_NOT_FOUND + storeId);
        }
        testOutputData.put(ExcelUtils.STAGING_ZONES, String.valueOf(stagingZones));
        ExcelUtils.writeToExcel(scenario, testOutputData);
        return stagingZones;
    }

    public String getReStagingZoneForOverSize(String storeId, HashMap<String, String> testOutputData, String overSizeStagingZone) {
        JsonPath jsonPath = new JsonPath(triggerStagingZonesApi(storeId, testOutputData).asString());
        jsonPath.get("data.stagingZones");
        String reStagingZone = "";
        for (int i = 0; i < jsonPath.getInt("data.stagingZones.size()"); i++) {
            if (verifyValidBarcodeFormat(jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE), testOutputData)) {
                if (jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE).startsWith(String.valueOf(overSizeStagingZone.charAt(0)))) {
                    if (!jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE).equals(overSizeStagingZone)) {
                        reStagingZone = jsonPath.getString(STAGING_ZONES_DATA + i + BARCODE);
                        break;
                    }
                }
            }
        }
        return reStagingZone;
    }
}
