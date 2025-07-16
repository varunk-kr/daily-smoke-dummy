package utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kroger.qmetry.converters.*;
import com.kroger.qmetry.junitmerger.Merger;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.*;

public class APIHelper {
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static String ENV = "PRODUCTION";
    private static String JIRA_API_URL = "https://jira.kroger.com/jira/rest/";
    private static String IMPORT_URL = "qtm4j/automation/latest/importresult";
    private static String IMPORT_BODY_PARAMETER = "";
    private static String AUTHORIZATION_TOKEN;
    private static String TEST_RESULT_FILE_FULLPATH = "target/surefire-reports/testng-results.xml";
    private static String API_KEY;
    private static String PROJECT_KEY = "FFILLSVCS";
    private static String PROGRESS_TRACK = "/track?trackingId";
    private static String RESULT_FORMAT = "TESTNG";
    private static String TESTING_TYPE;
    private static String DATA_PROVIDER;
    private static String FILEPATH = "target/surefire-reports/testng-results.xml";
    private InputStream inputStreamQmetry;

    public String getENV() {
        return ENV;
    }

    public String getTestResultFileFullpath() {
        return TEST_RESULT_FILE_FULLPATH;
    }

    public String getJiraApiUrl() {
        return JIRA_API_URL;
    }

    public APIHelper(String filePath) {
        TEST_RESULT_FILE_FULLPATH = filePath;
        FILEPATH = filePath;
        AUTHORIZATION_TOKEN = this.getAuthToken();
        String CHECK_FLG = this.getPropValues();
        if (!CHECK_FLG.equals("SET")) {
            System.out.println("ALERT:: Config File parameters not set properly..");
            System.exit(0);
        }

    }

    public static String setImportBodyParameter(String tcSummary, String resultType, String environment, String build, String component, String testCaseFolderPath, String testCycleFolderPath, String storyId) {
        IMPORT_BODY_PARAMETER = "";
        StringBuilder requestBody = new StringBuilder();
        requestBody.append("{ \"format\":\"");
        requestBody.append(resultType).append("\",");
        requestBody.append("\"testCycleToReuse\":\"FFILLSVCS-TR-4735\",");
        if (!environment.equals("empty")) {
            requestBody.append("\"environment\":\"").append(environment).append("\",");
        }

        requestBody.append("\"automationHierarchy\":\"1\",");
        if (!build.equals("empty")) {
            requestBody.append("\"build\":\"").append(build).append("\",");
        }

        requestBody.append("\"fields\":{\"testCycle\":{},");

        requestBody.append("\"testCase\":{");
        if (!testCaseFolderPath.equals("empty")) {
            requestBody.append("\"folderPath\":\"").append(testCaseFolderPath).append("\",");
        }

        if (!component.equals("empty") && !environment.equals("empty")) {
            requestBody.append("\"components\":[\"").append(component).append("\"],");
        }

        requestBody.append("\"description\": \"Automated Test Case\",");
        requestBody.append("\"status\":\"Done\"");
        requestBody.append("}}}");
        IMPORT_BODY_PARAMETER = IMPORT_BODY_PARAMETER.concat(requestBody.toString());
        return IMPORT_BODY_PARAMETER;
    }

    public Map<String, String> getTrackInfoUrl() {
        Map<String, String> trackMap = new HashMap<>();

        try {
            HttpResponse<String> responseImport = Unirest.post(JIRA_API_URL + IMPORT_URL)
                    .header("Content-Type", "application/json")
                    .header("apiKey", API_KEY)
                    .header("Authorization", AUTHORIZATION_TOKEN)
                    .body(IMPORT_BODY_PARAMETER)
                    .asString();  // Use string first

            int status = responseImport.getStatus();
            String body = responseImport.getBody();

            System.out.println("DEBUG: HTTP Status = " + status);
            System.out.println("DEBUG: Response Body = \n" + body);

            if ((status == 200 || status == 201) && body.trim().startsWith("{")) {
                JSONObject json = new JSONObject(body);
                trackMap.put("TRACKING_URL", json.getString("url"));
                trackMap.put("TRACKING_ID", json.getString("trackingId"));
                trackMap.put("STATUS", "1");
            } else {
                throw new RuntimeException("Import failed. Server response not in expected JSON format:\n" + body);
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Import request failed.");
            System.out.println("IMPORT_BODY_PARAMETER: " + IMPORT_BODY_PARAMETER);
            e.printStackTrace();
            throw new RuntimeException("Failed to import test results. See logs for details.");
        }

        return trackMap;
    }

    public String uploadResultFileFromImportUrl(String uploadUrl) {
        File result_file = new File(TEST_RESULT_FILE_FULLPATH);
        if (!result_file.isFile()) {
            System.out.println("Alter (File not found): Please check Test Results file path...");
            System.exit(0);
        }

        try {
            HttpResponse<JsonNode> response_upload = Unirest.post(uploadUrl).header("apiKey", API_KEY).header("Authorization", AUTHORIZATION_TOKEN).field("file", result_file).asJson();
            return response_upload.getStatusText();
        } catch (Exception var4) {
            System.out.println("Alert : Please check proper parameters in the Test Results file with proper path.");
            System.exit(0);
            return "";
        }
    }

    public Map<String, String> getProgressStatus(String trackId) {
        Map<String, String> progressMap = new HashMap();
        Boolean importStatus = false;

        try {
            HttpResponse<JsonNode> response_progress = Unirest.get(JIRA_API_URL + IMPORT_URL + PROGRESS_TRACK + "=" + trackId).header("Content-Type", "application/json").header("apiKey", API_KEY).header("Authorization", AUTHORIZATION_TOKEN).asJson();
            String jsonSummary = ((JsonNode) response_progress.getBody()).getObject().get("summary").toString();
            JsonElement jsonElement = JsonParser.parseString(((JsonNode) response_progress.getBody()).toString());
            Gson gson = new Gson();
            Map<String, Object> map = (Map) gson.fromJson(jsonElement, Map.class);
            if (map.get("importStatus").toString().equals("FAILED")) {
                importStatus = true;
            }

            List<String> listSummary = List.of(map.get("summary").toString().split(","));
            Map<String, String> mapSummary = new HashMap();
            Iterator var11 = listSummary.iterator();

            while (var11.hasNext()) {
                String keyValue = (String) var11.next();
                String[] parts = keyValue.split("=");
                mapSummary.put(parts[0].replaceAll("\\s+", ""), parts[1].replaceAll("\\s+", ""));
            }

            String testCycleId = "";
            Iterator var16 = mapSummary.keySet().iterator();

            while (var16.hasNext()) {
                String key = (String) var16.next();
                if (key.equals("testCycleIssueKey")) {
                    testCycleId = (String) mapSummary.get(key);
                }
            }

            progressMap.put("PROGRESS_STATUS", ((JsonNode) response_progress.getBody()).getObject().getString("processStatus"));
            progressMap.put("IMPORT_STATUS", ((JsonNode) response_progress.getBody()).getObject().getString("processStatus"));
            progressMap.put("TEST_CYCLE_HAS_KEY", testCycleId);
            progressMap.put("TEST_CYCLE_ISSUE_KEY", testCycleId);
            return progressMap;
        } catch (Exception var14) {
            if (importStatus) {
                throw new RuntimeException("The supplied file is invalid, please review if the file was generated correctly during the Test Suite Execution");
            } else {
                return progressMap;
            }
        }
    }

    public String getAuthToken() {
        String authOrgin = "c3ZjcW1ldHJ5cDp4WVVjd1lPcVVwb29pV2NCU3A3Q2llVEtUSnI=";
        return "Basic " + authOrgin;
    }

    public String getProjectAPIKey() {
        try {
            AUTHORIZATION_TOKEN = this.getAuthToken();
            SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial((KeyStore) null, new TrustSelfSignedStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
            HttpClient customHttpClient = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            Unirest.setHttpClient(customHttpClient);
            JSONParser parse = new JSONParser();
            String get_project_key = "";
            HttpResponse<String> ID_response = Unirest.get(JIRA_API_URL + "api/2/project/" + PROJECT_KEY).header("Authorization", AUTHORIZATION_TOKEN).header("Content-Type", "application/json").asString();
            System.out.println("Jira connection successful: " + ID_response.getStatusText());
            if (ID_response.getStatusText().equals("200")) {
                org.json.simple.JSONObject jobj = (org.json.simple.JSONObject) parse.parse((String) ID_response.getBody());
                get_project_key = jobj.get("id").toString();
                String request_AK = "{\"locale\":\"en_US\",\"timezone\":\"America/New_York\",\"label\":\"\",\"apiKeyPurpose\":\"AUTOMATION\",\"projectId\":\"PROJECT_ID\", \"label\":\"xxxx\"}".replaceAll("PROJECT_ID", get_project_key);
                HttpResponse<JsonNode> key_response = Unirest.post(JIRA_API_URL + "qtm4j/ui/latest/apikey").header("Authorization", AUTHORIZATION_TOKEN).header("Content-Type", "application/json").body(request_AK).asJson();
                if (key_response.getStatusText().equals("200")) {
                    API_KEY = ((JsonNode) key_response.getBody()).getObject().getString("key");
                    return ((JsonNode) key_response.getBody()).getObject().getString("key");
                } else {
                    return "";
                }
            } else {
                return "INVALID";
            }
        } catch (Exception var8) {
            throw new RuntimeException("The connection to Jira was not established correctly.");
        }
    }

    public static String separatorsToSystem(String res) {
        String fileNewPath = "";
        if (res == null) {
            return null;
        } else {
            if (File.separatorChar == '\\') {
                fileNewPath = res.replace('/', File.separatorChar);
            } else {
                fileNewPath = res.replace('\\', File.separatorChar);
            }

            return fileNewPath;
        }
    }

    public String getPropValues() {
        String RETURN_FLAG = "NOTSET";

        try {
            Properties propQmetry = new Properties();
            String qmetryFileName = "qmetry.properties";
            this.inputStreamQmetry = new FileInputStream("./" + qmetryFileName);
            if (this.inputStreamQmetry == null) {
                throw new FileNotFoundException("property file '" + qmetryFileName + "' not found in the classpath");
            }

            propQmetry.load(this.inputStreamQmetry);
            ENV = "PRODUCTION";
            JIRA_API_URL = "https://jira.kroger.com/jira/rest/";
            IMPORT_URL = "qtm4j/automation/latest/importresult";
            PROGRESS_TRACK = "/track?trackingId";
            PROJECT_KEY = propQmetry.getProperty("PROJECT_KEY");
            System.out.println("PROJECT_KEY: " + PROJECT_KEY);
            RESULT_FORMAT = propQmetry.getProperty("RESULT_FORMAT");
            TESTING_TYPE = propQmetry.getProperty("TESTING_TYPE");
            DATA_PROVIDER = propQmetry.getProperty("DATA_PROVIDER");
            if (RESULT_FORMAT == null) {
                RESULT_FORMAT = "TESTNG";
            }

            if (TESTING_TYPE == null) {
                TESTING_TYPE = "UI";
            }

            RETURN_FLAG = "SET";
        } catch (Exception var12) {
            Exception e = var12;
            System.out.println("Exception: " + e);
        } finally {
            try {
                this.inputStreamQmetry.close();
            } catch (IOException var11) {
                IOException e = var11;
                e.printStackTrace();
            }

        }

        return RETURN_FLAG;
    }

    public String readingXML(String xmlFile, String SUITE_NAME, String ENVIRONMENT, String BUILD, String COMPONENT, String TESTCASE_FOLDERPATH, String TESTCYCLE_FOLDERPATH, String STORY_ID) {
        try {
            File file = new File(xmlFile);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("suite");

            for (int itr = 0; itr < nodeList.getLength(); ++itr) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == 1) {
                    Element eElement = (Element) node;
                    String suiteName = eElement.getAttribute("name");
                    String environment = "empty";
                    String build = "empty";
                    String component = "empty";
                    String testCaseFolderPath = "empty";
                    String testCycleFolderPath = "empty";
                    String storyId = "empty";
                    if (SUITE_NAME != null && !SUITE_NAME.equals("empty")) {
                        suiteName = suiteName + " - " + SUITE_NAME;
                    }

                    if (ENVIRONMENT != null && !ENVIRONMENT.equals("empty")) {
                        environment = ENVIRONMENT;
                    }

                    if (BUILD != null && !BUILD.equals("empty")) {
                        build = BUILD;
                    }

                    if (!COMPONENT.equals("empty")) {
                        component = COMPONENT;
                    }

                    if (TESTCASE_FOLDERPATH != null && !TESTCASE_FOLDERPATH.equals("empty")) {
                        testCaseFolderPath = TESTCASE_FOLDERPATH;
                    }

                    if (TESTCYCLE_FOLDERPATH != null && !TESTCYCLE_FOLDERPATH.equals("empty")) {
                        testCycleFolderPath = TESTCYCLE_FOLDERPATH;
                    }

                    if (STORY_ID != null && !STORY_ID.equals("empty")) {
                        storyId = STORY_ID;
                    }

                    return setImportBodyParameter(suiteName, RESULT_FORMAT, environment, build, component, testCaseFolderPath, testCycleFolderPath, storyId);
                }
            }
        } catch (Exception var24) {
            Exception e = var24;
            e.printStackTrace();
        }

        return null;
    }

    public void frameworkConverter(String STORY_ID) {
        switch (TESTING_TYPE) {
            case "PYVALIDATA":
                ConvertPyValidataTestNGResults convertPyValidataTestNGResults = new ConvertPyValidataTestNGResults(TEST_RESULT_FILE_FULLPATH);

                try {
                    TEST_RESULT_FILE_FULLPATH = convertPyValidataTestNGResults.convertTestNG();
                    break;
                } catch (Throwable var14) {
                    Throwable e = var14;
                    throw new RuntimeException(e);
                }
            case "WDIO":
                ConvertWDIOTestNGResults convertWDIOTestNGResults = new ConvertWDIOTestNGResults(TEST_RESULT_FILE_FULLPATH);
                TEST_RESULT_FILE_FULLPATH = convertWDIOTestNGResults.convertTestNG();
                break;
            case "ESPRESSO":
                ConvertEspressoTestNGResults convertEspressoTestNGResults = new ConvertEspressoTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH);
                convertEspressoTestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = convertEspressoTestNGResults.qmetryFile;
                break;
            case "PLAYWRIGHT":
                ConvertJUnitTestNGResults convertPWTestNGResults = new ConvertJUnitTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH, STORY_ID);
                convertPWTestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = convertPWTestNGResults.qmetryFile;
                break;
            case "KARATE":
                try {
                    Merger.converter(TEST_RESULT_FILE_FULLPATH, "testng-results.xml", "Karate Suite");
                } catch (Exception var13) {
                    Exception e = var13;
                    throw new RuntimeException(e);
                }
                TEST_RESULT_FILE_FULLPATH = TEST_RESULT_FILE_FULLPATH + "/qmetry/testng-results.xml";
                ConvertJUnitTestNGResults convertKarateTestNGResults = new ConvertJUnitTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH, STORY_ID);
                convertKarateTestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = convertKarateTestNGResults.qmetryFile;
                break;
            case "TOSCA":
                ConvertToscaTestNGResults convertToscaTestNGResults = new ConvertToscaTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH, STORY_ID);
                convertToscaTestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = convertToscaTestNGResults.qmetryFile;
                break;
            case "POSTMAN":
                ConvertJUnitTestNGResults convertPostmanTestNGResults = new ConvertJUnitTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH, STORY_ID);
                convertPostmanTestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = convertPostmanTestNGResults.qmetryFile;
                break;
            case "XCUITest":
                ConvertXCUITestNGResults convertXCUITestNGResults = new ConvertXCUITestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH);
                convertXCUITestNGResults.convertTestNG();
                TEST_RESULT_FILE_FULLPATH = ConvertXCUITestNGResults.qmetryFile;
                break;
            default:
                ConvertNativeTestNGResults convertNativeTestNGResults = new ConvertNativeTestNGResults(TESTING_TYPE, TEST_RESULT_FILE_FULLPATH, DATA_PROVIDER, STORY_ID);
                TEST_RESULT_FILE_FULLPATH = convertNativeTestNGResults.convertTestNG();
        }

    }
}
