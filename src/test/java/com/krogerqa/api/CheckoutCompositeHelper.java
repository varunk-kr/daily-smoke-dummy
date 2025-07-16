package com.krogerqa.api;

import com.krogerqa.api.model.CancelCheckoutComposite.CancelCheckoutCompositePayload;
import com.krogerqa.api.model.activateEbtCheckoutComposite.ActivateEbtCheckoutCompositePayload;
import com.krogerqa.api.model.activateNonEbtCheckoutComposite.*;
import com.krogerqa.api.model.modifyCheckoutOrder.ModifyCheckoutPayload;
import com.krogerqa.api.model.modifyCheckoutOrder.PriceAdjustment;
import com.krogerqa.api.model.pickupCheckoutComposite.Properties;
import com.krogerqa.api.model.pickupCheckoutComposite.*;
import com.krogerqa.utils.Constants;
import com.krogerqa.utils.DateUtils;
import com.krogerqa.utils.ExcelUtils;
import com.krogerqa.utils.PropertyUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Helper class for creating payload and handling API call for ORDER CREATION using checkout composite
 */
public class CheckoutCompositeHelper {
    final static String slotId = Constants.CheckoutComposite.SLOT_ID;
    final static String nodeId = Constants.CheckoutComposite.NODE_ID;
    final static String reservationId = Constants.CheckoutComposite.RESERVATION_ID;
    private static final String ACTIVATE_HEADER = "ACTIVATE";
    private static final String MODIFY_HEADER = "MODIFY";
    private static final String CANCEL_HEADER = "CANCEL";
    private static final String FINALIZE_HEADER = "FINALIZE";
    public static String ORDER_ACTIVATION_FAILED = "Order Creation is not Successful";
    private static CheckoutCompositeHelper instance;

    public synchronized static CheckoutCompositeHelper getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (CheckoutCompositeHelper.class) {
            if (instance == null) {
                instance = new CheckoutCompositeHelper();
            }
        }
        return instance;
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private String getDateOfBirth(HashMap<String, String> userInfo) {
        return DateUtils.getDateFromDayMonthYear(Integer.parseInt(userInfo.get(ExcelUtils.AGE_RESTRICTED_DOB_YEAR)), Integer.parseInt(userInfo.get(ExcelUtils.AGE_RESTRICTED_DOB_MONTH)), Integer.parseInt(userInfo.get(ExcelUtils.AGE_RESTRICTED_DOB_DAY)));
    }

    private String calculateCostSummary(List<HashMap<String, String>> itemsList) {
        double subTotal = 0.0;
        for (HashMap<String, String> item : itemsList) {
            subTotal = subTotal + Double.parseDouble(item.get(ExcelUtils.ESTIMATED_SUBTOTAL).replace(Constants.KcpOrder.USD, ""));
        }
        subTotal = Double.parseDouble(new DecimalFormat("###.00").format(subTotal));
        double total = subTotal + Double.parseDouble(Constants.KcpOrder.TAX_TOTAL.replace(Constants.KcpOrder.USD, ""));
        double totalCost = Double.parseDouble(new DecimalFormat("#.00").format(total));
        return Constants.CheckoutComposite.USD + new DecimalFormat("#.00").format(totalCost);
    }

    private List<Network> getNetworkDetails(Vendor vendor, HashMap<String, String> userInfo) {
        Network network1 = new Network(nodeId, slotId, userInfo.get(ExcelUtils.STORE_ID), vendor, Constants.CheckoutComposite.PICK_CAPABILITY);
        Network network2 = new Network("309637b8-cad5-444e-84b2-a68d939cc0e7", slotId, userInfo.get(ExcelUtils.STORE_ID), vendor, Constants.CheckoutComposite.STAGE_CAPABILITY);
        Network network3 = new Network("e67a35a6-afc6-482b-8940-429e4b84953e", slotId, userInfo.get(ExcelUtils.STORE_ID), vendor, Constants.CheckoutComposite.HANDOFF_CAPABILITY);
        List<Network> networks = new ArrayList<>();
        networks.add(network1);
        networks.add(network2);
        networks.add(network3);
        return networks;
    }

    private List<CheckoutCompositeLineItems> getCheckOutLineItemDetails(List<HashMap<String, String>> itemsList, HashMap<String, String> userInfo) {
        List<CheckoutCompositeLineItems> checkoutCompositeLineItemsList = new ArrayList<>();
        for (HashMap<String, String> item : itemsList) {
            Department department = new Department(item.get(ExcelUtils.DEPARTMENT_CODE), item.get(ExcelUtils.DEPARTMENT_NAME));
            RecapDepartment recapDepartment = new RecapDepartment(department);
            PrimaryDepartment primaryDepartment = new PrimaryDepartment(recapDepartment);
            Commodity commodity = new Commodity(item.get(ExcelUtils.COMMODITY_CODE), item.get(ExcelUtils.COMMODITY_NAME));
            Subcommodity subcommodity = new Subcommodity(item.get(ExcelUtils.SUB_COMMODITY_CODE), item.get(ExcelUtils.SUB_COMMODITY_NAME));
            FamilyTree familyTree = new FamilyTree(primaryDepartment, commodity, subcommodity);
            Product product = new Product(Constants.CheckoutComposite.MISC_ACCOUNT, Constants.CheckoutComposite.ITEM_TYPE_CODE, Constants.CheckoutComposite.TARE_VALUE,
                    item.get(ExcelUtils.PRODUCT_ID), Boolean.parseBoolean(item.get(ExcelUtils.IS_ALCOHOL)), item.get(ExcelUtils.AVERAGE_WEIGHT_PER_UNIT),
                    item.get(ExcelUtils.CUSTOMER_FACING_SIZE), item.get(ExcelUtils.KROGER_OWNED_DESCRIPTION), Boolean.parseBoolean(item.get(ExcelUtils.IS_RANDOM_WEIGHT)),
                    Constants.CheckoutComposite.SNAP_ELIGIBLE, familyTree, ExcelUtils.SELL_BY);
            Regular regular = new Regular(item.get(ExcelUtils.ESTIMATED_SUBTOTAL));
            OriginalPrice originalPrice = new OriginalPrice(regular);
            Cart cart = new Cart(generateUuid(), item.get(ExcelUtils.ITEM_UPC), Integer.parseInt(item.get(ExcelUtils.ITEM_QUANTITY)), product, originalPrice);
            CheckoutCompositeLineItems checkoutCompositeLineItems = new CheckoutCompositeLineItems(cart, Constants.CheckoutComposite.REQUESTED_BY, userInfo.get(ExcelUtils.STORE_ID));
            checkoutCompositeLineItemsList.add(checkoutCompositeLineItems);
        }
        return checkoutCompositeLineItemsList;
    }

    public String returnCheckoutPayload(int daysFromToday, HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList) {
        String modalityType = Constants.CheckoutComposite.PICKUP_MODALITY_TYPE;
        if (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_DELIVERY_ORDER))) {
            modalityType = Constants.CheckoutComposite.DELIVERY_MODALITY_TYPE;
        }
        ArrayList<String> addressLines = new ArrayList<>();
        addressLines.add(userInfo.get(ExcelUtils.ADDRESS_LINE1));
        addressLines.add(userInfo.get(ExcelUtils.ADDRESS_LINE2));
        String cost = calculateCostSummary(itemsList);
        Address address = new Address(true, addressLines, userInfo.get(ExcelUtils.CITY_TOWN), userInfo.get(ExcelUtils.STATE_PROVINCE), userInfo.get(ExcelUtils.POSTAL_CODE),
                userInfo.get(ExcelUtils.COUNTRY_CODE), userInfo.get(ExcelUtils.PHONE_NUMBER));
        Contact contact = new Contact(userInfo.get(ExcelUtils.FIRST_NAME), userInfo.get(ExcelUtils.LAST_NAME),
                userInfo.get(ExcelUtils.PHONE_NUMBER), true, address);
        Vendor vendor = new Vendor(Constants.CheckoutComposite.ID, Constants.CheckoutComposite.NAME, reservationId);
        int hoursSpan;
        if ((userInfo.containsKey(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING)) && (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING))) || (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_MULTIPLE_SLOT_RUSH_ORDER)))) {
            if (userInfo.get(ExcelUtils.ORDER_SLOT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(2));
                } else if (userInfo.get(ExcelUtils.MULTIPLE_ORDER_COUNT).equalsIgnoreCase(ExcelUtils.SECOND_ORDER)) {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(3));
            } else {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(4));
            }
        } else if (userInfo.containsKey(ExcelUtils.MULTIPLE_ORDER_ROMB)) {
            hoursSpan = hourSpan(userInfo);
        } else if (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_RUSH_ORDER))  && (!Boolean.parseBoolean(userInfo.get(ExcelUtils.MULTIPLE_ORDER_ROMB)))) {
            hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(1));
        } else if (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_EXPRESS_ORDER))) {
            hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(1));
        } else {
            if (Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_MULTIPLE_ORDER_DYNAMIC_BATCHING)) && (userInfo.get(ExcelUtils.ORDER_SLOT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER))) {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(7));
            } else {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(5));
            }
            if (userInfo.containsKey(ExcelUtils.IS_PULL_AHEAD_FLOW) && Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_PULL_AHEAD_FLOW)) && userInfo.get(ExcelUtils.PULL_AHEAD_FLOW_ORDER_SLOT).equalsIgnoreCase(ExcelUtils.FIRST_ORDER)) {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(1));
            }
            if (userInfo.containsKey(ExcelUtils.IS_PULL_AHEAD_FLOW) && Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_PULL_AHEAD_FLOW)) && userInfo.get(ExcelUtils.PULL_AHEAD_FLOW_ORDER_SLOT).equalsIgnoreCase(ExcelUtils.LAST_ORDER)) {
                hoursSpan = Integer.parseInt(DateUtils.selectRequiredHours(6));
            }
        }
        if (!Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_RUSH_ORDER))) {
            if (!Boolean.parseBoolean(userInfo.get(ExcelUtils.IS_EXPRESS_ORDER))) {
                if (hoursSpan < 10) {
                    hoursSpan = 14;
                }
            }
        }
        String startTime = "T" + String.format("%02d", hoursSpan) + ":00:00Z/";
        String endTime = "T" + String.format("%02d", hoursSpan + 1) + ":00:00Z";
        Selections selections = new Selections(generateUuid(), vendor, userInfo.get(ExcelUtils.CODE), cost, userInfo.get(ExcelUtils.COST_ID),
                new Window(DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday) + startTime + DateUtils.getRequiredDateString(Constants.Date.ISO_DATE_FORMAT, daysFromToday) + endTime, userInfo.get(Constants.CheckoutComposite.TIMEZONE)),
                getNetworkDetails(vendor, userInfo));
        ArrayList<Selections> selectionsArrayList = new ArrayList<>();
        selectionsArrayList.add(selections);
        Buyer buyer = new Buyer(Constants.CheckoutComposite.PROFILE_ID, userInfo.get(ExcelUtils.LOYALTY_ID), Constants.CheckoutComposite.EXPERIENCE,
                userInfo.get(ExcelUtils.USER_EMAIL), getDateOfBirth(userInfo), address);
        Fulfillment fulfillment = new Fulfillment(contact, new PickupLocation(userInfo.get(ExcelUtils.STORE_ID)), new PricingLocation(userInfo.get(ExcelUtils.FACILITY_ID), userInfo.get(ExcelUtils.STORE_ID)),
                Constants.CheckoutComposite.INSTRUCTIONS, new Promise(Constants.CheckoutComposite.PROMISE_ID, selectionsArrayList));
        Properties properties = new Properties(userInfo.get(ExcelUtils.CHECKOUT_VERSION));
        PickCheckoutCompositePayload pickCheckoutCompositePayload = new PickCheckoutCompositePayload(modalityType, Constants.CheckoutComposite.CART_ID, buyer, fulfillment, getCheckOutLineItemDetails(itemsList, userInfo), properties);
        return new ApiUtils().convertObjectToString(pickCheckoutCompositePayload);
    }

    public int hourSpan(HashMap<String, String> testData) {
        int hour;
        switch (testData.get(ExcelUtils.MULTIPLE_ORDER_COUNT)) {
            case ExcelUtils.FIRST_ORDER:
                hour = Integer.parseInt(DateUtils.selectRequiredHours(1));
                break;
            case ExcelUtils.SECOND_ORDER:
                hour = Integer.parseInt(DateUtils.selectRequiredHours(2));
                break;
            case ExcelUtils.THIRD_ORDER:
                hour = Integer.parseInt(DateUtils.selectRequiredHours(3));
                break;
            default:
                hour = Integer.parseInt(DateUtils.selectRequiredHours(4));
        }
        return hour;
    }

    public Response createCheckoutCompositePickOrder(int daysFromToday, HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList) {
        return new ApiUtils().postPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), returnCheckoutPayload(daysFromToday, userInfo, itemsList),
                PropertyUtils.getCheckoutCompositePickupEndpoint(), compositePickUpHeader(ACTIVATE_HEADER, userInfo), new HashMap<>());
    }

    public void modifyCheckoutCompositeOrder(HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList, String orderNumber, String versionKey) {
        new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), activateModifyOrderPayload(userInfo, itemsList, versionKey),
                PropertyUtils.getCheckoutCompositeActivateEndpoint(orderNumber), compositePickUpHeader(MODIFY_HEADER, userInfo), new HashMap<>());
    }

    public String activateCompositeCheckoutNonEbtPayload(HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList, String versionKey) {
        String cost = calculateCostSummary(itemsList);
        Transactions transactions = new Transactions(cost, new PaymentMethod(userInfo.get(ExcelUtils.TYPE), userInfo.get(ExcelUtils.VAULT_ID),
                userInfo.get(ExcelUtils.SUB_TYPE), userInfo.get(ExcelUtils.LAST_FOUR_OF_CARD)));
        ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
        transactionsArrayList.add(transactions);
        PaymentDetails paymentDetails = new PaymentDetails(new Payment(generateUuid(), transactionsArrayList));
        ActivateNonEbtCheckoutCompositePayload activateNonEbtCheckoutCompositePayload = new ActivateNonEbtCheckoutCompositePayload(versionKey, paymentDetails);
        return new ApiUtils().convertObjectToString(activateNonEbtCheckoutCompositePayload);
    }

    public String activateCompositeCheckoutSnapEbtPayload(HashMap<String, String> userInfo, String versionKey, String nonEbtTotal, String ebtTotal) {
        ArrayList<String> addressLines = new ArrayList<>();
        addressLines.add(userInfo.get(ExcelUtils.ADDRESS_LINE1));
        addressLines.add(userInfo.get(ExcelUtils.ADDRESS_LINE2));
        Transactions creditCardtransactions = new Transactions(nonEbtTotal, new PaymentMethod(Constants.CheckoutComposite.CREDIT_CARD_PAY, userInfo.get(ExcelUtils.VAULT_ID),
                userInfo.get(ExcelUtils.SUB_TYPE), userInfo.get(ExcelUtils.LAST_FOUR_OF_CARD)));
        Transactions ebtCardTransactions = new Transactions(ebtTotal, new PaymentMethod(Constants.CheckoutComposite.EBT_CARD_PAY));
        ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
        transactionsArrayList.add(creditCardtransactions);
        transactionsArrayList.add(ebtCardTransactions);
        PaymentDetails paymentDetails = new PaymentDetails(new Payment(generateUuid(), transactionsArrayList));
        Address address = new Address(true, addressLines, userInfo.get(ExcelUtils.CITY_TOWN), userInfo.get(ExcelUtils.STATE_PROVINCE), userInfo.get(ExcelUtils.POSTAL_CODE),
                userInfo.get(ExcelUtils.COUNTRY_CODE), userInfo.get(ExcelUtils.PHONE_NUMBER));
        Contact contact = new Contact(userInfo.get(ExcelUtils.FIRST_NAME), userInfo.get(ExcelUtils.LAST_NAME),
                userInfo.get(ExcelUtils.PHONE_NUMBER), true, address);
        Buyer buyer = new Buyer(address);
        Fulfillment fulfillment = new Fulfillment(contact);
        ActivateSnapEbtCheckoutCompositePayload activateSnapEbtCheckoutCompositePayload = new ActivateSnapEbtCheckoutCompositePayload(versionKey, paymentDetails, buyer, fulfillment);
        return new ApiUtils().convertObjectToString(activateSnapEbtCheckoutCompositePayload);
    }

    public String activateCompositeCheckoutEbtPayload(HashMap<String, String> userInfo, String versionKey) {
        com.krogerqa.api.model.activateEbtCheckoutComposite.PaymentDetails paymentDetails = new com.krogerqa.api.model.activateEbtCheckoutComposite.PaymentDetails(true);
        com.krogerqa.api.model.activateEbtCheckoutComposite.Properties properties = new com.krogerqa.api.model.activateEbtCheckoutComposite.Properties(userInfo.get(ExcelUtils.CHECKOUT_VERSION));
        ActivateEbtCheckoutCompositePayload activateEbtCheckoutCompositePayload = new ActivateEbtCheckoutCompositePayload(paymentDetails, properties, versionKey);
        return new ApiUtils().convertObjectToString(activateEbtCheckoutCompositePayload);
    }

    public String cancelCompositeCheckoutPayload(String versionKey) {
        CancelCheckoutCompositePayload cancelCheckoutCompositePayload = new CancelCheckoutCompositePayload(versionKey, "Order was made accidentally!");
        return new ApiUtils().convertObjectToString(cancelCheckoutCompositePayload);
    }

    public String activateModifyOrderPayload(HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList, String modifyVersionKey) {
        String cost = calculateCostSummary(itemsList);
        Transactions transactions = new Transactions(cost, new PaymentMethod(userInfo.get(ExcelUtils.TYPE), userInfo.get(ExcelUtils.VAULT_ID),
                userInfo.get(ExcelUtils.SUB_TYPE), userInfo.get(ExcelUtils.LAST_FOUR_OF_CARD)));
        ArrayList<Transactions> transactionsArrayList = new ArrayList<>();
        transactionsArrayList.add(transactions);
        Buyer buyer = new Buyer(getDateOfBirth(userInfo));
        PriceAdjustment priceAdjustment = new PriceAdjustment();
        PaymentDetails paymentDetails = new PaymentDetails(new Payment(generateUuid(), transactionsArrayList));
        ModifyCheckoutPayload modifyCheckoutPayload = new ModifyCheckoutPayload(modifyVersionKey, buyer, getCheckOutLineItemDetails(itemsList, userInfo), priceAdjustment, paymentDetails);
        return new ApiUtils().convertObjectToString(modifyCheckoutPayload);
    }

    public Response activateCheckoutCompositeNonEbtOrder(HashMap<String, String> userInfo, List<HashMap<String, String>> itemsList, String orderNumber, String versionKey) {
        Response response = new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), activateCompositeCheckoutNonEbtPayload(userInfo, itemsList, versionKey),
                PropertyUtils.getCheckoutCompositeActivateEndpoint(orderNumber), compositePickUpHeader(ACTIVATE_HEADER, userInfo), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, ORDER_ACTIVATION_FAILED);
        return response;
    }

    public Response activateCheckoutCompositeSnapEbtOrder(HashMap<String, String> userInfo, String orderNumber, String versionKey, String nonEbtTotal, String ebtTotal) {
        Response response = new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), activateCompositeCheckoutSnapEbtPayload(userInfo, versionKey, nonEbtTotal, ebtTotal),
                PropertyUtils.getCheckoutCompositeActivateEndpoint(orderNumber), compositePickUpHeader(ACTIVATE_HEADER, userInfo), new HashMap<>());
        Assert.assertEquals(response.getStatusCode(), 200, ORDER_ACTIVATION_FAILED);
        return response;
    }

    public void cancelCheckoutCompositeOrder(String orderNumber, String versionKey, HashMap<String, String> userInfo) {
        new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), cancelCompositeCheckoutPayload(versionKey),
                PropertyUtils.getCheckoutCompositeActivateEndpoint(orderNumber), compositePickUpHeader(CANCEL_HEADER, userInfo), new HashMap<>());
    }

    public Response activateCheckoutCompositeEbtOrder(HashMap<String, String> userInfo, String orderNumber, String versionKey) {
        return new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), activateCompositeCheckoutEbtPayload(userInfo, versionKey),
                PropertyUtils.getCheckoutCompositeActivateEndpoint(orderNumber), compositePickUpHeader(ACTIVATE_HEADER, userInfo), new HashMap<>());
    }

    /**
     * @param orderNumber order Number created
     * @return versionKey after completing checkout
     */
    public String getCheckoutById(String orderNumber) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Correlation-ID", generateUuid());
        headers.put("X-Kroger-Tenant", "kroger");
        Response response = new ApiUtils().getApi(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), null,
                PropertyUtils.getCheckoutCompositeById(orderNumber), headers, new HashMap<>());
        return response.jsonPath().getString("data.checkouts.versionKey");
    }

    public String finalizeCheckoutCompositePayload(String versionKey) {
        FinalizeCheckoutCompositePayload finalizeCheckoutCompositePayload = new FinalizeCheckoutCompositePayload(versionKey);
        return new ApiUtils().convertObjectToString(finalizeCheckoutCompositePayload);
    }

    public void putFinalizeCheckout(String orderNumber, HashMap<String, String> userInfo) {
        new ApiUtils().putPayload(PropertyUtils.getCheckoutCompositeUsername(), PropertyUtils.getCheckoutCompositePassword(), finalizeCheckoutCompositePayload(getCheckoutById(orderNumber)),
                PropertyUtils.getCheckoutCompositeById(orderNumber), compositePickUpHeader(FINALIZE_HEADER, userInfo), new HashMap<>());
    }

    public HashMap<String, String> compositePickUpHeader(String action, HashMap<String, String> userInfo) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Correlation-ID", generateUuid());
        headers.put("X-Idempotency-Key", generateUuid());
        headers.put("X-Kroger-Tenant", userInfo.get(ExcelUtils.STORE_BANNER));
        headers.put("X-Kroger-Channel", "API");
        headers.put("X-Kroger-Checkouts-Use-Case", action);
        return headers;
    }
}
