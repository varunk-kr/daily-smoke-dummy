package utils;

public class OrderIdAndStoreIdHelper {

    /* Harris Teeter DyB smoke scenario */
    public static final String harrisTeeterDyB = "HARRIS_TEETER_DYB_14375";
    public static final String harrisTeeterDyBScenarioName = "HarrisTeeterDyBSingleThreadBau";

    /* 9 tote+ smoke scenario */
    public static final String dbNineTote = "DYNAMIC_BATCH_9TOTE_TC_15066";
    public static final String dbNineToteScenarioName = "DbNineToteHappyPath";

    /* Non PCL smoke scenarios */
    public static final String shiptDelivery = "SCENARIO_13";
    public static final String shiptDeliveryScenarioName = "ShiptDelivery";

    /*PCL smoke scenario */
    public static final String modifyCancelPcl = "PCL_SCENARIO_TC_4563";
    public static final String modifyCancelPclThroughUi = "PCL_SCENARIO_TC_4563_1";
    public static final String modifyCancelPclThroughApi = "PCL_SCENARIO_TC_4563_1_1";
    public static final String modifyCancelPclScenarioName = "ModifyCancelPcl";
    public static final String reusePcl = "PCL_SCENARIO_TC_4566";
    public static final String reusePclScenarioName = "ReusePcl";
    public static final String allFulfillmentTypeSmoke = "PCL_FULFILLMENT_TYPES_SMOKE";
    public static final String allFulfillmentTypeSmokeScenarioName = "AllFulfillmentTypeSmoke";
    public static final String takeOverCarryOverTrolleyPcl = "PCL_SCENARIO_TC_4539";
    public static final String takeOverCarryOverTrolleyPclScenarioName = "TakeOverCarryOverTrolleyPcl";

    /*Rush order Tc52 smoke scenario */
    public static final String rushOrderNonEBTPcl = "RUSH_ORDER_SCENARIO_TC_6706";
    public static final String tc52rushOrderNonEBTPcl = "RUSH_ORDER_TC52_TC_7114";
    public static final String rushOrderNonEBTPclScenarioName = "RushOrderNonEBTPcl";
    public static final String tc52rushOrderNonEBTPclScenarioName = "NonEBTPclOrderROMBTc52";

    /* PCL OverSize smoke scenario */
    public static final String containerLookupNewOSContainerMovement = "PCL_OVERSIZE_SCENARIO_TC_4568";
    public static final String containerLookupNewOSContainerMovementScenarioName = "InformationLookupOsMovement";

    /* MAFP smoke scenario */
    public static final String mafpCiaoChange = "MAFP_CIAO_SCENARIO_TC_10464";
    public static final String mafpCiaoChangeScenarioName = "WithSellByUnitAndSellByWeightItem";

    /* Enhanced batching BYOB smoke scenario */
    public static final String dbPclByobAcceptAnchorStaging = "EB_PCL_TC_10945";
    public static final String dbPclByobAcceptAnchorStagingScenarioName = "DbPclEnhancedBatchAMREByobAccept";

    /* DyB OS Pcl Single Thread Bulk staging smoke scenario */
    public static final String pclSingleThread = "DB_SINGLE_THREAD_12730";
    public static final String pclSingleThreadScenarioName = "AllFulfillmentTypeSingleThread";

    /* Instacart smoke scenario */
    public static final String instacart = "INSTACART_SCENARIO_TC_12864";
    public static final String instacartScenarioName = "InstacartBaseOrder";

    /* DyB Express Order OOOT smoke scenario */
    public static final String moveItemsPreWeighedScreen = "PCL_SCENARIO_TC_4504";
    public static final String moveItemsPreWeighedScreenScenarioName = "DbSingleThreadExpressOrderMoveItemsPreWeighedScreen";

    /* SC MT smoke scenario */
    public static final String scMtIntegration = "SC_INTEGRATION_TC_14431";
    public static final String scMtIntegrationScenarioName = "ScIntegrationBau";

    private OrderIdAndStoreIdHelper() {

    }

    /*getting the order number for each scenario*/
    public static void getAllScenarioOrderNumbers() {
        String value;
        String reusePclOrderNumber = "";
        boolean reusePclFlag = false;
        for (String key : E2eListeners.testCaseIdOrderNumbers.keySet()) {
            if (key.contains(harrisTeeterDyB)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(harrisTeeterDyBScenarioName, value);
            } else if (key.contains(dbNineTote)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(dbNineToteScenarioName, value);
            } else if (key.contains(shiptDelivery)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(shiptDeliveryScenarioName, value);
            } else if (key.contains(modifyCancelPcl)) {
                if (key.equals(modifyCancelPclThroughUi)) {
                    value = E2eListeners.testCaseIdOrderNumbers.get(key);
                    E2eListeners.scenarioOrderNumbers.put(modifyCancelPclScenarioName, value);
                } else if (key.equals(modifyCancelPclThroughApi)) {
                    value = E2eListeners.testCaseIdOrderNumbers.get(key);
                    E2eListeners.scenarioOrderNumbers.put(modifyCancelPclScenarioName, value);
                }
            } else if (key.contains(reusePcl)) {
                if (!reusePclFlag) {
                    reusePclOrderNumber = E2eListeners.testCaseIdOrderNumbers.get(key);
                    E2eListeners.scenarioOrderNumbers.put(reusePclScenarioName, reusePclOrderNumber);
                    reusePclFlag = true;
                } else {
                    value = reusePclOrderNumber + ", " + E2eListeners.testCaseIdOrderNumbers.get(key);
                    E2eListeners.scenarioOrderNumbers.put(reusePclScenarioName, value);
                    reusePclFlag = false;
                }
            } else if (key.contains(tc52rushOrderNonEBTPcl)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(tc52rushOrderNonEBTPclScenarioName, value);
            } else if (key.contains(rushOrderNonEBTPcl)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(rushOrderNonEBTPclScenarioName, value);
            } else if (key.contains(dbPclByobAcceptAnchorStaging)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(dbPclByobAcceptAnchorStagingScenarioName, value);
            } else if (key.contains(moveItemsPreWeighedScreen)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(moveItemsPreWeighedScreenScenarioName, value);
            } else if (key.contains(allFulfillmentTypeSmoke)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(allFulfillmentTypeSmokeScenarioName, value);
            } else if (key.contains(containerLookupNewOSContainerMovement)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(containerLookupNewOSContainerMovementScenarioName, value);
            } else if (key.contains(takeOverCarryOverTrolleyPcl)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(takeOverCarryOverTrolleyPclScenarioName, value);
            } else if (key.contains(instacart)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(instacartScenarioName, value);
            } else if (key.contains(mafpCiaoChange)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(mafpCiaoChangeScenarioName, value);
            } else if (key.contains(pclSingleThread)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(pclSingleThreadScenarioName, value);
            } else if (key.contains(scMtIntegration)) {
                value = E2eListeners.testCaseIdOrderNumbers.get(key);
                E2eListeners.scenarioOrderNumbers.put(scMtIntegrationScenarioName, value);
            } else {
                System.out.println("Invalid test case id");
                break;
            }
        }
    }

    /*getting the store number for each scenario*/
    public static void getAllScenarioStoreNumbers() {
        String value;
        for (String key : E2eListeners.testCaseIdStoreNumbers.keySet()) {
            if (key.contains(harrisTeeterDyB)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(harrisTeeterDyBScenarioName, value);
            } else if (key.contains(dbNineTote)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(dbNineToteScenarioName, value);
            } else if (key.contains(shiptDelivery)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(shiptDeliveryScenarioName, value);
            } else if (key.contains(modifyCancelPcl)) {
                if (key.equals(modifyCancelPclThroughUi)) {
                    value = E2eListeners.testCaseIdStoreNumbers.get(key);
                    E2eListeners.scenarioStoreNumbers.put(modifyCancelPclScenarioName, value);
                } else if (key.equals(modifyCancelPclThroughApi)) {
                    value = E2eListeners.testCaseIdStoreNumbers.get(key);
                    E2eListeners.scenarioStoreNumbers.put(modifyCancelPclScenarioName, value);
                }
            } else if (key.contains(reusePcl)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(reusePclScenarioName, value);
            } else if (key.contains(tc52rushOrderNonEBTPcl)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(tc52rushOrderNonEBTPclScenarioName, value);
            } else if (key.contains(rushOrderNonEBTPcl)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(rushOrderNonEBTPclScenarioName, value);
            } else if (key.contains(dbPclByobAcceptAnchorStaging)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(dbPclByobAcceptAnchorStagingScenarioName, value);
            } else if (key.contains(moveItemsPreWeighedScreen)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(moveItemsPreWeighedScreenScenarioName, value);
            } else if (key.contains(allFulfillmentTypeSmoke)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(allFulfillmentTypeSmokeScenarioName, value);
            } else if (key.contains(containerLookupNewOSContainerMovement)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(containerLookupNewOSContainerMovementScenarioName, value);
            } else if (key.contains(takeOverCarryOverTrolleyPcl)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(takeOverCarryOverTrolleyPclScenarioName, value);
            } else if (key.contains(instacart)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(instacartScenarioName, value);
            } else if (key.contains(mafpCiaoChange)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(mafpCiaoChangeScenarioName, value);
            } else if (key.contains(pclSingleThread)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(pclSingleThreadScenarioName, value);
            } else if (key.contains(scMtIntegration)) {
                value = E2eListeners.testCaseIdStoreNumbers.get(key);
                E2eListeners.scenarioStoreNumbers.put(scMtIntegrationScenarioName, value);
            } else {
                System.out.println("Invalid test case id");
                break;
            }
        }
    }
}
