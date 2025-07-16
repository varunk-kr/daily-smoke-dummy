package com.krogerqa.utils;

public final class Constants {
    public static final String PARTNER_ID = "data.deliveries.partnerId";
    public static final String DELIVERY_ORDER_ID = "data.deliveries.orderId";
    public static final String DELIVERY_PARTNER_TRACKING_ID = "data.deliveries.updates.deliveryPartnerTracking.id";
    public static final String DELIVERY_WINDOW_STARTS_AT = "data.deliveries.updates.deliveryPartnerTracking.additionalAttributes.delivery_window_starts_at";
    public static final String DELIVERY_WINDOW_ENDS_AT = "data.deliveries.updates.deliveryPartnerTracking.additionalAttributes.delivery_window_ends_at";
    public static final String DELIVERY_PARTNER_TRACKING_STATUS = "data.deliveries.updates.deliveryPartnerTracking.status";
    public static final String PARTNER_SHIPT_ID = "SHIPT";
    public static final String DELIVERY_SHIPT_ORDER_ID = "data.delivery.orderId";
    public static final String DELIVERY_SHIPT_PARTNER_TRACKING_ID = "data.delivery.updates.deliveryPartnerTracking.id";
    public static final String DELIVERY_SHIPT_WINDOW_STARTS_AT = "data.delivery.updates.deliveryPartnerTracking.additionalAttributes.delivery_window_starts_at";
    public static final String DELIVERY_SHIPT_WINDOW_ENDS_AT = "data.delivery.updates.deliveryPartnerTracking.additionalAttributes.delivery_window_ends_at";
    public static final String DELIVERY_SHIPT_PARTNER_TRACKING_STATUS = "data.delivery.updates.deliveryPartnerTracking.status";
    public static final String TIMESLOT_AVAILABLE = "item unavailable";
    public static final int MaxFrozenOversizeTrolleyCapacity = 18;
    public static final String EXPRESS_ORDER = "Express";
    public static final String SMALL_OS_ITEM = "SMALL_OS_ITEM";
    public static final String AM_TROLLEY = "AM_TROLLEY";
    public static final String RE_TROLLEY = "RE_TROLLEY";
    public static final String FR_TROLLEY = "FR_TROLLEY";
    public static final String OS_TROLLEY = "OS_TROLLEY";
    public static final String SMOS_TROLLEY = "SMOS_TROLLEY";
    public static final String SC_STATUS_NEW = "New";
    public static final String SC_STATUS_SENT_TO_SCALE = "Sent to Scale";
    public static final String SC_STATUS_IN_PROGRESS = "In progress";
    public static final String SC_STATUS_READY_AT_COUNTER = "Ready at Counter";
    public static final String INCORRECT_SC_NEW_STATUS = "Status is not showing new for the SC item.";
    public static final String INCORRECT_SC_SENT_TO_SCALE_STATUS = "Status is not showing sent to scale for the SC item.";
    public static final String INCORRECT_SC_IN_PROGRESS_STATUS = "Status is not showing in progress for the SC item.";
    public static final String INCORRECT_SC_READY_AT_COUNTER_STATUS = "Status is not showing ready at counter for the SC item.";
    public static final String INCORRECT_SC_ITEMS_DISPLAY = "Service Counter Item is not displayed in Cue service counter page.";

    public static final class Date {
        public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
        public static final String CUE_DATE_FORMAT = "MM/dd/yyyy";
        public static final int PREVIOUS_DAY = -1;
        public static final int TODAY = 0;
    }

    public static final class Shipt {
        public static final String DELIVERY_CREATED_AT = "data.deliveries.createdAt.value";
        public static final String DELIVERY_UPDATED_AT = "data.deliveries.updates.deliveryPartnerTracking.updatedAt.value";
        public static final String DELIVERY_STATUS_CHANGED = "delivery.status_changed";
        public static final String VEHICLE_TYPE = "Small";
        public static final String SHIPT_STATUS_CLAIMED = "claimed";
        public static final String SHIPT_STATUS_CANCELLED = "cancelled";
        public static final String SHIPT_FIF_STATUS_OPEN = "open";
        public static final String DELIVERY_SHIPT_CREATED_AT = "data.delivery.createdAt.value";
        public static final String DELIVERY_SHIPT_UPDATED_AT = "data.delivery.updates.deliveryPartnerTracking.updatedAt.value";
        public static final String SHIPT_STATUS_SHOPPER_ARRIVED = "shopper arrived";
        public static final String SHIPT_STATUS_PICKED_UP = "picked up";
        public static final String SHIPT_STATUS_DELIVERED = "delivered";
        public static final String SHIPT_DELIVERY = "Shipt Delivery";
    }


    public static final class PickCreation {
        public static final String PICK_LISTS_ITEM_UPC = "itemUpc";
        public static final String PICK_LISTS_CONTAINER_NUMBER = "containerId";
        public static final String PICK_LISTS_TROLLEY_ID = "trolleyName";
        public static final String PICK_LISTS_CONTAINER_TEMPERATURE = "tempType";
        public static final String PICK_LISTS_CONTAINER_TYPE = "type";
        public static final String PICK_LISTS_ITEM_ID = "itemId";
        public static final String NOT_READY_OSS = "Not Ready OOS";
        public static final String PICK_LISTS_CONTAINER_UUID = "id";
        public static final String ITEM_ORDERED_QUANTITY = "orderedQuantity";
        public static final String ITEM_PICKED_AMOUNT = "pickedAmount";
        public static final String ITEM_FULFILLMENT_TYPE = "fulfillmentType";
        public static final String NEED_BY_TIME = "needBy";
        public static final String FULFILLMENT_OUT_OF_STOCK = "outOfStock";
        public static final String FULFILLMENT_AS_ORDERED = "asOrdered";
        public static final String ITEM_STATUS = "itemStatus";
        public static final String PICKED_STATUS = "Picked";
        public static final String OUT_OF_STOCK = "Out of Stock";
        public static final String SUBSTITUTE = "Substitute";
        public static final String SUBSTITUTE_ACCEPT = "Substitute Accept";
        public static final String SUBSTITUTE_REJECT = "Substitute Reject";
        public static final String SUBSTITUTE_SHORT = "Substitute Short";
        public static final String SELL_BY_WEIGHT = "Sell By Weight";
        public static final String CARRYOVER = "Carryover";
        public static final String SHORTED = "Shorted";
        public static final String NOT_READY = "Not Ready";
        public static final String START_TROLLEY_EVENT = "START_TROLLEY_RUN";
        public static final String END_TROLLEY_EVENT = "END_TROLLEY_RUN";
        public static final String ITEM_PICK_EVENT = "ITEM_PICK";
        public static final String ORDER_BATCHING_TIME = "T18:00:00.000Z";
        public static final String PERMANENT_LABEL = "PL";
        public static final String ASSIGNING = "Assigning";
        public static final String FLOATING_LABEL = "FL";
        public static final String NORMAL_CONTAINER = "T";
        public static final String OVERSIZE_ITEM_MOVEMENT_TOAST_MESSAGE = "You cannot move items to an oversize container. Please scan a different container.";
        public static final String ITEM_MOVEMENT_TOAST_MESSAGE = "Items successfully moved. Please make sure to move all items to the new container.";
        public static final String TROLLEY_NOT_FOUND = "Trolley not found";
        public static final String TROLLEY_NOT_GENERATED = "Trolleys not getting generated";
        public static final String ORDER_NOT_FOUND = "Order not found";
        public static final String INCORRECT_TEMP_TYPE_BULK_STAGING = "Temperature type must be AM for bulk staging";
        public static final String AMBIENT = "AMBIENT";
        public static final String REFRIGERATED = "REFRIGERATED";
        public static final String FROZEN_TEMP = "FROZEN";
        public static final String STAGED = "Staged";
        public static final String NOT_STAGED = "Not Staged";
        public static final String NOT_READY_SUBS = "NOT_READY_SUBS";
        public static String AMBIENT_BULK_DE_STAGING = "AM";
        public static String FROZEN = "FR";
        public static String OVERSIZE = "OS";
        public static String X_KROGER_EUID = "X-Kroger-Associate-Euid";
        public static String X_KROGER_REC_LOCATION_ID = "X-Kroger-Receiving-Location-Id";
        public static String X_KROGER_STAGE_LOCATION_ID = "X-Kroger-Staging-Location-Id";
        public static String X_KROGER_CHANNEL = "X-Kroger-Channel";
        public static String X_KROGER_TENANT = "X-Kroger-Tenant";
        public static String X_KROGER_VERTICAL = "X-Kroger-Vertical";
        public static String X_AUTHENTICATED_USERID = "X-Authenticated-UserId";
        public static String CONTENT_TYPE = "Content-Type";
        public static String CONTENT_LANGUAGE = "Content-Language";
        public static String CONTENT_LANGUAGE_VALUE = "en-US";
        public static String CONTENT_TYPE_VALUE = "application/json";
        public static String DB_ASSOCIATE_TEXT = "system";
        public static String TEXT_FROM_VISUAL_ID = "Order ";
        public static String ALL_ITEMS_FOR_PICKUP = "All Items for Pickup";
        public static String QTY = "qty";
        public static String SISTER_STORE = "SisterStore";
        public static String KROGER_BANNER = "kroger";
        public static String HT_BANNER = "Harris Teeter";
        public static String ENTER_BARCODE_WITH_TOOL_TIP = "ENTER_BARCODE_WITH_TOOL_TIP";
        public static String ENTER_BARCODE_WITHOUT_TOOL_TIP = "ENTER_BARCODE_WITHOUT_TOOL_TIP";
    }

    public static final class KcpOrder {
        public static String TAX_TOTAL = "USD 1.48";
        public static String USD = "USD ";
    }

    public static final class Instacart {
        public static String PICKED = "PICKED";
        public static String STAGED = "STAGED";
        public static String PICKED_UP = "PICKED_UP";
        public static String MAKE = "toyoto";
        public static String MODEL = "camry";
        public static String VEHICLE_TYPE = "sedan";
        public static String VEHICLE_COLOR = "blue";
        public static String CONTAINER_ID = "777283971-1-";
        public static String REFRIGERATED_TEMPERATURE = "REFRIGERATOR";
        public static String UTC = "UTC";
        public static String PICKING_COMPLETED = "PICKING_COMPLETED";
        public static String BAG = "BAG";
        public static String FULFILLED = "FULFILLED";
        public static String ITEM_ID = "408366763229007-";
        public static String CREATE_ORDER = "CREATE_ORDER-";
    }

    public static final class CheckoutComposite {
        public static String PICKUP_MODALITY_TYPE = "PICKUP";
        public static String DELIVERY_MODALITY_TYPE = "DELIVERY";
        public static String EXPERIENCE = "FIRST_TIME";
        public static String INSTRUCTIONS = "Please deliver this to the side door, not the front door.";
        public static String ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
        public static String SPECIAL_INSTRUCTION = "SPECIAL_INSTRUCTION";
        public static String USD = "USD ";
        public static String ID = "KR001";
        public static String NAME = "KROGER";
        public static String MARIANOS = "MARIANOS";
        public static String KINGSOOPERS = "KingSoopers";
        public static String TIMEZONE = "TIMEZONE";
        public static String PICK_CAPABILITY = "PICK";
        public static String STAGE_CAPABILITY = "STAGE";
        public static String HANDOFF_CAPABILITY = "LAST_MILE_HANDOFF";
        public static String REQUESTED_BY = "CUSTOMER";
        public static float MISC_ACCOUNT = 0.5f;
        public static String ITEM_TYPE_CODE = "abc";
        public static float TARE_VALUE = 1.23f;
        public static String SNAP_ELIGIBLE = "FoodStampEligible";
        public static String PROFILE_ID = "75fab540-1709-4ba2-838f-18f6f0a9e4f3";
        public static String CART_ID = "5a93c64c-0adc-4c75-9c37-bdf0f3fb571f";
        public static String PROMISE_ID = "777ef527-4a27-4057-ac0c-256c13982013";
        public static String NODE_ID = "ab419183-1a23-448f-9c9e-d47d5737db59";
        public static String SLOT_ID = "cea30a4b-9294-4593-80c0-856b01fbf65f";
        public static String RESERVATION_ID = "46a90c1c-9411-4762-9891-f7bdf734363f";
        public static String EBT_CARD_PAY = "EBT_CARD_PAY";
        public static String CREDIT_CARD_PAY = "CREDIT_CARD_PAY";
    }

    public static final class ApplicationName {
        public static String KSP = "Kroger Seamless Portal";
        public static String CUE = "Cue";
        public static String HARVESTER = "Harvester";
        public static String DASH = "Dash";
        public static String CIAO = "Ciao";
        public static String LOGIN = "Login";
        public static String TC52_NOTIFICATIONS = "TC52 Notifications";
    }

    public static final class Toggles {
        public static String NEW_CUSTOMER_TOGGLE = "NewCustomerToggle";
        public static String BAG_OPTIONALITY = "EnableBagOptionalityFeatures";
        public static String REQUEST_TROLLEY = "EnableRequestTrolley";
    }

    public static final class Klog {
        public static String STORE_ID_VALIDATION_MSG = "store id validation is not successful";
        public static String ORDER_ID_VALIDATION_MSG = "order id validation is not successful";
        public static String GROSS_VALUE_VALIDATION_MSG = "gross positive validation is not successful";
        public static String CARD_VALIDATION_MSG = "customer plus card validation is not successful";
        public static final String KLOG_NOT_RECEIVED_MSG = "Xml is not generated for the order";
        public static final String KLOG_RESPONSE_CODE_FAILED_MSG = "200 response code for klog not received";
    }

    public static final class LoginContexts {
        public static String WEB_CONTEXT = "WEBVIEW_chrome";
        public static String NATIVE_CONTEXT = "NATIVE_APP";
    }

    public static final class ScDataDog {
        public static final String SCHEMA_LOCATION = "http://www.mt.com/ComOne/namespace ComOneTransactionLog.xsd";
        public static final String REQUEST = "Request";
        public static final String READ = "Read";
        public static final String UPDATE = "Update";
        public static final String SALE = "<Sale>";
        public static final String IN_PROCESS = "<Sale Status=\"InProcess\">";
        public static final String FINISHED = "<Sale Status=\"Finished\">";
        public static final String MESSAGE_ID = "1744210478955";
        public static final String DATE_TIME = "2025-04-09T10:54:38-04:00";
        public static final String MESSAGE_TAG_DATA_DOG_XML = "<Message xmlns:freshpro=\"http://www.mt.com/ComOne/namespace/freshpro\"\n" +
                "         xmlns=\"http://www.mt.com/ComOne/namespace\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://www.mt.com/ComOne/namespace ComOneTransactionLog.xsd\">";
    }
}