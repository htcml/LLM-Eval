package com.example.data.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class ComprehensiveDataTransformer {

    private static final Logger logger = LoggerFactory.getLogger(ComprehensiveDataTransformer.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public ComprehensiveDataTransformer() {
        logger.info("Comprehensive Data Transformer initialized.");
    }

    // Reads the input JSON and parses it into a map representation
    public Map<String, Object> readInputJson(String jsonString) throws IOException {
        logger.info("Reading input JSON");
        return objectMapper.readValue(jsonString, Map.class);
    }

    // Processes the input data and transforms it into an output JSON structure
    public ObjectNode processData(Map<String, Object> inputData) {
        logger.info("Processing input data to generate output JSON");
        
        ObjectNode outputJson = objectMapper.createObjectNode();
        outputJson.put("requestId", generateRequestId());
        outputJson.put("generatedAt", System.currentTimeMillis());

        // User profile section
        ObjectNode userProfile = processUserProfile(inputData);
        outputJson.set("user_profile", userProfile);

        // Settings section
        ObjectNode settings = processUserSettings(inputData);
        outputJson.set("settings", settings);

        // Transaction history
        ArrayNode transactions = processTransactionHistory(inputData);
        outputJson.set("transactions", transactions);

        // Analytics section
        ObjectNode analytics = processAnalyticsData(inputData);
        outputJson.set("analytics", analytics);

        // Metadata section
        ObjectNode metadata = processMetadata(inputData);
        outputJson.set("metadata", metadata);

        logger.info("Final output JSON: {}", outputJson);
        return outputJson;
    }

    // Process user's profile data
    private ObjectNode processUserProfile(Map<String, Object> inputData) {
        logger.info("Processing user profile data");
        ObjectNode userProfile = objectMapper.createObjectNode();
        userProfile.put("userId", fetchUserId(inputData));
        setIfExists("userName", String.class, inputData, userProfile, "$.user_profile.name");
        setIfExists("email", String.class, inputData, userProfile, "$.user_profile.email");
        setIfExists("address", String.class, inputData, userProfile, "$.user_profile.address");

        processContacts(inputData, userProfile);
        processPreferences(inputData, userProfile);
        processBillingDetails(inputData, userProfile);
        processSubscriptionInfo(inputData, userProfile);

        // Custom attributes
        processCustomAttributes(inputData, userProfile);

        return userProfile;
    }

    // Processes and adds the user's contact information
    private void processContacts(Map<String, Object> inputData, ObjectNode userProfile) {
        logger.info("Processing contacts");
        ArrayNode contactsArray = objectMapper.createArrayNode();
        List<Map<String, Object>> contacts = getContacts(inputData);
        if (contacts != null && !contacts.isEmpty()) {
            for (int i = 0; i < contacts.size(); i++) {
                Map<String, Object> contact = contacts.get(i);
                ObjectNode contactNode = objectMapper.createObjectNode();
                setIfExists("type", String.class, contact, contactNode, "$.contact.type");
                setIfExists("number", String.class, contact, contactNode, "$.contact.number");
                contactsArray.add(contactNode);
            }
            userProfile.set("contacts", contactsArray);
        }
    }

    // Process user preferences like language, timezone, etc.
    private void processPreferences(Map<String, Object> inputData, ObjectNode userProfile) {
        logger.info("Processing preferences");
        ObjectNode preferences = objectMapper.createObjectNode();
        setIfExists("language", String.class, inputData, preferences, "$.preferences.language");
        setIfExists("timezone", String.class, inputData, preferences, "$.preferences.timezone");
        setIfExists("currency", String.class, inputData, preferences, "$.preferences.currency");
        userProfile.set("preferences", preferences);
    }

    // Process billing details from inputData
    private void processBillingDetails(Map<String, Object> inputData, ObjectNode userProfile) {
        logger.info("Processing billing details");
        ObjectNode billingDetails = objectMapper.createObjectNode();
        setIfExists("cardType", String.class, inputData, billingDetails, "$.billing.cardType");
        setIfExists("cardNumber", String.class, inputData, billingDetails, "$.billing.cardNumber");
        setIfExists("expiryDate", String.class, inputData, billingDetails, "$.billing.expiryDate");
        setIfExists("billingAddress", String.class, inputData, billingDetails, "$.billing.billingAddress");
        userProfile.set("billingDetails", billingDetails);
    }

    // Process subscription-related information
    private void processSubscriptionInfo(Map<String, Object> inputData, ObjectNode userProfile) {
        logger.info("Processing subscription info");
        ObjectNode subscriptionInfo = objectMapper.createObjectNode();
        setIfExists("plan", String.class, inputData, subscriptionInfo, "$.subscription.plan");
        setIfExists("subscriptionStart", String.class, inputData, subscriptionInfo, "$.subscription.start");
        setIfExists("subscriptionEnd", String.class, inputData, subscriptionInfo, "$.subscription.end");
        userProfile.set("subscriptionInfo", subscriptionInfo);
    }

    // Process custom user attributes
    private void processCustomAttributes(Map<String, Object> inputData, ObjectNode userProfile) {
        logger.info("Processing custom attributes");
        ObjectNode customAttributes = objectMapper.createObjectNode();
        setIfExists("loyaltyTier", String.class, inputData, customAttributes, "$.customAttributes.loyaltyTier");
        setIfExists("accountStatus", String.class, inputData, customAttributes, "$.customAttributes.accountStatus");
        setIfExists("preferencesUpdatedAt", String.class, inputData, customAttributes, "$.customAttributes.preferencesUpdatedAt");
        userProfile.set("customAttributes", customAttributes);
    }

    // Processes user's system settings
    private ObjectNode processUserSettings(Map<String, Object> inputData) {
        logger.info("Processing user settings");
        ObjectNode settings = objectMapper.createObjectNode();
        setIfExists("notificationsEnabled", Boolean.class, inputData, settings, "$.settings.notificationsEnabled");
        setIfExists("theme", String.class, inputData, settings, "$.settings.theme");
        setIfExists("darkMode", Boolean.class, inputData, settings, "$.settings.darkMode");
        return settings;
    }

    // Processes transaction history
    private ArrayNode processTransactionHistory(Map<String, Object> inputData) {
        logger.info("Processing transaction history");
        ArrayNode transactions = objectMapper.createArrayNode();
        List<Map<String, Object>> history = getTransactionHistory(inputData);
        if (history != null && !history.isEmpty()) {
            for (Map<String, Object> transaction : history) {
                ObjectNode transactionNode = objectMapper.createObjectNode();
                setIfExists("transactionId", String.class, transaction, transactionNode, "$.transactions.transactionId");
                setIfExists("amount", Double.class, transaction, transactionNode, "$.transactions.amount");
                setIfExists("date", String.class, transaction, transactionNode, "$.transactions.date");
                transactions.add(transactionNode);
            }
        }
        return transactions;
    }

    // Processes analytics data
    private ObjectNode processAnalyticsData(Map<String, Object> inputData) {
        logger.info("Processing analytics data");
        ObjectNode analytics = objectMapper.createObjectNode();
        setIfExists("loginCount", Integer.class, inputData, analytics, "$.analytics.loginCount");
        setIfExists("lastLogin", String.class, inputData, analytics, "$.analytics.lastLogin");
        setIfExists("totalPurchases", Integer.class, inputData, analytics, "$.analytics.totalPurchases");
        setIfExists("timeSpent", Double.class, inputData, analytics, "$.analytics.timeSpent");
        return analytics;
    }

    // Processes metadata about the request
    private ObjectNode processMetadata(Map<String, Object> inputData) {
        logger.info("Processing metadata");
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("version", "1.0");
        metadata.put("processedBy", "ComprehensiveDataTransformer");
        metadata.put("timestamp", System.currentTimeMillis());
        setIfExists("sourceSystem", String.class, inputData, metadata, "$.metadata.sourceSystem");
        return metadata;
    }

    // Set a value in JSON if it exists
    private <T> void setIfExists(String fieldName, Class<T> valueType, Map<String, Object> inputData, ObjectNode jsonNode, String jsonPath) {
        T value = (T) inputData.get(fieldName);
        if (value != null) {
            String[] pathSegments = jsonPath.split("\\.");
            ObjectNode currentNode = jsonNode;
            for (int i = 1; i < pathSegments.length - 1; i++) {
                String segment = pathSegments[i].replaceAll("[\\[\\]$]", "");
                currentNode = currentNode.with(segment);
            }
            String finalField = pathSegments[pathSegments.length - 1].replaceAll("[\\[\\]$]", "");
            if (valueType == String.class) {
                currentNode.put(finalField, (String) value);
            } else if (valueType == Integer.class) {
                currentNode.put(finalField, (Integer) value);
            } else if (valueType == Double.class) {
                currentNode.put(finalField, (Double) value);
            } else if (valueType == Boolean.class) {
                currentNode.put(finalField, (Boolean) value);
            }
        }
    }

    // Retrieves user ID (example of ID generation)
    private String fetchUserId(Map<String, Object> inputData) {
        logger.info("Fetching userId");
        if (inputData.containsKey("userId")) {
            return (String) inputData.get("userId");
        }
        return "USER-" + System.nanoTime();
    }

    // Fetches contact details from inputData
    private List<Map<String, Object>> getContacts(Map<String, Object> inputData) {
        logger.info("Fetching contacts");
        if (inputData.containsKey("contacts")) {
            return (List<Map<String, Object>>) inputData.get("contacts");
        }
        return null;
    }

    // Fetches transaction history from inputData
    private List<Map<String, Object>> getTransactionHistory(Map<String, Object> inputData) {
        logger.info("Fetching transaction history");
        if (inputData.containsKey("transactions")) {
            return (List<Map<String, Object>>) inputData.get("transactions");
        }
        return null;
    }

    // Generates a unique request ID
    private String generateRequestId() {
        logger.info("Generating requestId");
        return "REQ-" + System.nanoTime();
    }

    // Main method for testing the processor logic
    public static void main(String[] args) {
        ComprehensiveDataTransformer processor = new ComprehensiveDataTransformer();
        String inputJson = "{"
                + "\"userId\": \"XXX\","
                + "\"userName\": \"XXX\","
                + "\"email\": \"XXX@example.com\","
                + "\"address\": \"XXX XXX St\","
                + "\"contacts\": ["
                + "{\"type\": \"home\", \"number\": \"XXX-XXX\"},"
                + "{\"type\": \"work\", \"number\": \"XXX-XXX\"}"
                + "],"
                + "\"preferences\": {"
                + "\"language\": \"XXX\","
                + "\"timezone\": \"XXX\","
                + "\"currency\": \"XXX\""
                + "},"
                + "\"billing\": {"
                + "\"cardType\": \"XXX\","
                + "\"cardNumber\": \"XXX-XXX-XXX-XXX\","
                + "\"expiryDate\": \"XX/XX\","
                + "\"billingAddress\": \"XXX XXX Rd\""
                + "},"
                + "\"subscription\": {"
                + "\"plan\": \"XXX\","
                + "\"subscriptionStart\": \"XX/XX/XXXX\","
                + "\"subscriptionEnd\": \"XX/XX/XXXX\""
                + "},"
                + "\"transactions\": ["
                + "{\"transactionId\": \"XXX\", \"amount\": 100.0, \"date\": \"XX/XX/XXXX\"},"
                + "{\"transactionId\": \"XXX\", \"amount\": 200.0, \"date\": \"XX/XX/XXXX\"}"
                + "]"
                + "}";
        try {
            // Step 1: Parse input JSON
            Map<String, Object> inputData = processor.readInputJson(inputJson);

            // Step 2: Process data and create output JSON
            ObjectNode outputJson = processor.processData(inputData);

            // Step 3: Convert final output to string and display
            String outputString = processor.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputJson);
            System.out.println(outputString);
        } catch (IOException e) {
            logger.error("Error processing JSON: {}", e.getMessage());
        }
    }
}

