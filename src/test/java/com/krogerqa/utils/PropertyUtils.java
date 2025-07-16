package com.krogerqa.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class PropertyUtils {
    private static PropertyUtils instance;

    private PropertyUtils() {
    }

    public synchronized static PropertyUtils getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (PropertyUtils.class) {
            if (instance == null) {
                instance = new PropertyUtils();
            }
        }
        return instance;
    }

    /**
     * This method will load all properties from info.properties file
     *
     * @return all properties part of info.properties
     */
    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader("./src/test/resources/info.properties");
            properties.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static String getCourierCoreToPifUsername() {
        return loadProperties().getProperty("courier-core-to-pif-user-name");
    }

    public static String getCourierCoreToPifPassword() {
        return getDecodedPassword(loadProperties().getProperty("courier-core-to-pif-password"));
    }

    public static String getCourierDeliveriesEndpoint() {
        return loadProperties().getProperty("courier-deliveries-endpoint");
    }

    public static String getCourierServicePostEndpoint() {
        return loadProperties().getProperty("courier-service-post-endpoint");

    }

    public static String getShiptPifAzureUsername() {
        return loadProperties().getProperty("courier-core-to-pif-azure-username");
    }

    public static String getShiptPifAzurePassword() {
        return getDecodedPassword(loadProperties().getProperty("courier-core-to-pif-azure-password"));
    }

    public static String getCourierDeliveriesAzureEndpoint() {
        return loadProperties().getProperty("courier-deliveries-azure-endpoint");
    }

    public static String getCourierServicePostPIFEndpoint() {
        return loadProperties().getProperty("courier-service-post-pif_endpoint");
    }


    public static String getOrderBatchingEndpoint() {
        return loadProperties().getProperty("order-batching-endpoint");
    }

    public static String getPickingEventsEndpoint() {
        return loadProperties().getProperty("picking-events-endpoint");
    }

    public static String getFulfillItemEndpoint() {
        return loadProperties().getProperty("fulfill-item-endpoint");
    }

    public static String getPickListsDataEndpoint() {
        return loadProperties().getProperty("pick-lists-data-endpoint");
    }

    public static String getPickRunsHeaderEndpoint() {
        return loadProperties().getProperty("pick-runs-header-endpoint");
    }

    public static String getCarryoverJobRunnerEndpoint() {
        return loadProperties().getProperty("carryover-job-runner-endpoint");
    }

    public static String getPermanentContainerLabelEndpoint() {
        return loadProperties().getProperty("permanent-container-label-endpoint");
    }

    public static String getTriggerSubstituteCommunicationEndpoint(String orderId) {
        return loadProperties().getProperty("trigger-substitution-communication-endpoint").replace("orderId", orderId);
    }

    public static String getSubstituteCustomerResponseEndpoint(String orderId) {
        return loadProperties().getProperty("substitute-customer-response-endpoint").replace("orderId", orderId);
    }

    public static String getSubstituteCustomerResponseApiUsername() {
        return loadProperties().getProperty("substitute-customer-response-api-username");
    }

    public static String getSubstituteCustomerResponseApiPassword() {
        return getDecodedPassword(loadProperties().getProperty("substitute-customer-response-api-password"));
    }

    public static String getHarrisTeeterUrl() {
        return loadProperties().getProperty("harris-teeter-url");
    }

    public static String getSpmUserId() {
        return loadProperties().getProperty("spm-user-id");
    }

    public static String getSpmPassword() {
        return getDecodedPassword(loadProperties().getProperty("spm-password"));
    }

    public static String getControlTowerEventEndpoint() {
        return loadProperties().getProperty("control-tower-event-endpoint");
    }

    public static String getKlogEndpoint() {
        return loadProperties().getProperty("klog-endpoint");
    }

    public static String getKrogerSeamlessUrl() {
        return loadProperties().getProperty("kroger-seamless-url");
    }

    public static String getKrogerSeamlessUrlEbt() {
        return loadProperties().getProperty("kroger-seamless-url-ebt");
    }

    public static String getCueBaseUrl() {
        return loadProperties().getProperty("cue-base-url");
    }

    public static String getCueUrl(String store) {
        return loadProperties().getProperty("cue-url").replace("storeNo", store);
    }

    public static String getDashUrl(String store) {
        return loadProperties().getProperty("dash-url").replace("storeNo", store);
    }

    public static String getHarvesterStagingZonesEndpoint() {
        return loadProperties().getProperty("harvester-staging-zones-endpoint");
    }

    public static String getHarvesterAnchorStagingZonesEndpoint() {
        return loadProperties().getProperty("harvester-anchor-staging-zones-endpoint");
    }

    public static String getCheckoutCompositePickupEndpoint() {
        return loadProperties().getProperty("checkout-composite-pickup");
    }

    public static String getCheckoutCompositeActivateEndpoint(String orderNumber) {
        return loadProperties().getProperty("checkout-composite-activate").replace("orderNumber", orderNumber);
    }

    public static String getCheckoutCompositeById(String orderNumber) {
        return loadProperties().getProperty("get-checkout-composite-byId").replace("orderNumber", orderNumber);
    }

    public static String getStagingZonesUsername() {
        return loadProperties().getProperty("staging-zones-api-username");
    }

    public static String getStagingZonesPassword() {
        return getDecodedPassword(loadProperties().getProperty("staging-zones-api-password"));
    }

    public static String getKlogDecryptionStageKey() {
        return loadProperties().getProperty("klog-decryption-stage-key");
    }

    public static String getKrogerPurchaseDetailsUrl() {
        return loadProperties().getProperty("kroger-purchase-details-url");
    }

    public static String getCheckoutCompositeUsername() {
        return loadProperties().getProperty("checkout-composite-username");
    }

    public static String getCheckoutCompositePassword() {
        return getDecodedPassword(loadProperties().getProperty("checkout-composite-password"));
    }

    public static String getCustomerCheckInUsername() {
        return loadProperties().getProperty("customer-check-in-api-username");
    }

    public static String getCustomerCheckInPassword() {
        return loadProperties().getProperty("customer-check-in-api-password");
    }

    public static String getCustomerCheckInEndpoint() {
        return loadProperties().getProperty("customer-check-in-api-endpoint");
    }

    public static String getHarvesterNativeUsername() {
        return loadProperties().getProperty("harvester-native-username");
    }

    public static String getHarvesterNativePassword() {
        return getDecodedPassword(loadProperties().getProperty("harvester-native-password"));
    }

    public static String getHarvesterNativeUsername1() {
        return loadProperties().getProperty("harvester-native-username1");
    }

    public static String getHarvesterNativePassword1() {
        return getDecodedPassword(loadProperties().getProperty("harvester-native-password1"));
    }

    public static String getCiaoUsername() {
        return loadProperties().getProperty("ciao-username");
    }

    public static String getCiaoPassword() {
        return getDecodedPassword(loadProperties().getProperty("ciao-password"));
    }

    public static String getCueUsername() {
        return loadProperties().getProperty("cue-username");
    }

    public static String getCuePassword() {
        return getDecodedPassword(loadProperties().getProperty("cue-password"));
    }

    public static String getDemeterUsername() {
        return loadProperties().getProperty("demeter-username");
    }

    public static String getDemeterPassword() {
        return loadProperties().getProperty("demeter-password");
    }

    public static String getStagingEndpoint(String storeId, String stagingZone) {
        return loadProperties().getProperty("harvester-staging-barcodes-endpoint").replace("receivingLocationId", storeId).replace("stagingZoneBarcode", stagingZone);
    }

    public static String getDeMeterUrl() {
        return loadProperties().getProperty("deMeter-url");
    }

    public static String getFlexAuthenticationTokenEndpoint() {
        return loadProperties().getProperty("flex-oauthToken-endPoint");
    }

    public static String getInstacartAuthenticationTokenEndpoint() {
        return loadProperties().getProperty("instacart-oauthToken-endPoint");
    }

    public static String getInstacartOrderIngestionEndpoint() {
        return loadProperties().getProperty("instacart-order-ingestion-endPoint");
    }

    public static String getInstacartPickCompletedEndpoint() {
        return loadProperties().getProperty("instacart-pick-completedEvent-endPoint");
    }

    public static String getInstacartOrderDetailsEndpoint() {
        return loadProperties().getProperty("instacart-order-details-endPoint");
    }

    public static String getFlexAcceptJobEndpoint() {
        return loadProperties().getProperty("flex-acceptJob-endPoint");
    }

    public static String getFlexDetails(String jobId) {
        return loadProperties().getProperty("flex-details-endpoint").replace("id", jobId);
    }

    /**
     * @return the Encoded Password for the String passed
     * Get the password from propertyFile and pass in below method to encode it
     */
    public static String getEncodedPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    /**
     * @return the decoded password to pass in the application.
     * Get the encoded password from property file before passing in application
     */
    public static String getDecodedPassword(String encodedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }

    public static String getKrogerFederationSeamlessUrl() {
        return loadProperties().getProperty("kroger-federation-seamless-url");
    }

    public static String getByobUrl(String orderNumber) {
        return loadProperties().getProperty("byob-url").replace("orderNumber", orderNumber);
    }

    public static String getKingSoopersByobUrl(String orderNumber) {
        return loadProperties().getProperty("byob-url-kingsooper").replace("orderNumber", orderNumber);
    }

    public static String getMarianosByobUrl(String orderNumber) {
        return loadProperties().getProperty("byob-url-marianos").replace("orderNumber", orderNumber);
    }

    public static String getKlogAuthenticationTokenEndpoint() {
        return loadProperties().getProperty("klog-oauthToken-endPoint");
    }

    public static String getKlogByOrderNumber(String orderNumber) {
        return loadProperties().getProperty("retrieve-klog-endPoint").replace("orderNumber", orderNumber);
    }
    public static String getKingSoopersSeamlessUrl() {
        return loadProperties().getProperty("kingsoopers-seamless-url");
    }


    public static String getDataDogXmlEndpoint() {
        return loadProperties().getProperty("datadog-xml-endPoint");
    }
}
