package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

public class ImportResultsQmetry {
    private static String SUITE_NAME;
    private static String ENVIRONMENT;
    private static String BUILD;
    private static String COMPONENT;
    private static String TESTCASE_FOLDERPATH;
    private static String TESTCYCLE_FOLDERPATH;
    private static String AUTHORIZATION_TOKEN;
    private static String TEST_RESULT_FILE_FULLPATH;
    private static String API_KEY;
    private static String PROJECT_KEY = "FFILLSVCS";
    private static String RESULT_FORMAT = "TESTNG";
    private static String STORY_ID;
    private APIHelper apiHelper;

    public ImportResultsQmetry() {
    }

    public static void generateTestNgXmlFile() {
        EmailSender.getAllDataFromListeners();
        String xmlFilePath = "src/main/java/utils/testng-results.xml";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));
            doc.getDocumentElement().normalize();
            NodeList methodList = doc.getElementsByTagName("test-method");
            for (int i = 0; i < methodList.getLength(); i++) {
                Element methodElement = (Element) methodList.item(i);
                String methodId = methodElement.getAttribute("id");
                if (E2eListeners.totalScenarioResults.containsKey(methodId)) {
                    boolean passed = E2eListeners.totalScenarioResults.get(methodId);
                    methodElement.setAttribute("status", passed ? "PASS" : "FAIL");
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlFilePath));
            transformer.transform(source, result);
            System.out.println("TestNG results updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            generateTestNgXmlFile();
            customParameters(args);
            APIHelper apiHelper = new APIHelper(TEST_RESULT_FILE_FULLPATH);
            apiHelper.frameworkConverter(STORY_ID);
            TEST_RESULT_FILE_FULLPATH = apiHelper.getTestResultFileFullpath();
            apiHelper.readingXML(TEST_RESULT_FILE_FULLPATH, SUITE_NAME, ENVIRONMENT, BUILD, COMPONENT, TESTCASE_FOLDERPATH, TESTCYCLE_FOLDERPATH, STORY_ID);
            if (TEST_RESULT_FILE_FULLPATH == "" || PROJECT_KEY == "" || RESULT_FORMAT == "") {
                throw new RuntimeException("ERROR : Not selected proper inputs for Username/Password/Result file path/Format/Project Key");
            }

            switch (apiHelper.getENV().toLowerCase()) {
                case "stage":
                    if (!apiHelper.getJiraApiUrl().contains("stage")) {
                        System.out.println("ALERT: Please Check proper Environment and API URL...");
                        System.exit(0);
                    }
                    break;
                case "production":
                    if (apiHelper.getJiraApiUrl().contains("stage")) {
                        System.out.println("ALERT: Please Check proper Environment and API URL...");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("ALERT: Please Check proper Environment and API URL...");
                    System.exit(0);
            }

            AUTHORIZATION_TOKEN = apiHelper.getAuthToken();
            API_KEY = apiHelper.getProjectAPIKey();
            if (API_KEY.equals("") || API_KEY.equals("INVALID")) {
                StringBuilder userProjectErrorMessage = new StringBuilder();
                userProjectErrorMessage.append("ALERT:: The user does not have access for selected environment/project. \n").append("Check if the PROJECT_KEY value is correct in qmetry.properties file. \n");
                throw new RuntimeException(userProjectErrorMessage.toString());
            }

            Map<String, String> trackInfoUrl = apiHelper.getTrackInfoUrl();
            if (!((String) trackInfoUrl.get("STATUS")).equals("1")) {
                throw new RuntimeException("ERROR : Fail to run utility and error while import results into Jira. :: Import Level");
            }

            String uploadResponse = apiHelper.uploadResultFileFromImportUrl((String) trackInfoUrl.get("TRACKING_URL"));
            System.out.println("\u001b[32;1;2m+-------------------------+\u001b[0m");
            if (!uploadResponse.equals("204")) {
                throw new RuntimeException("ERROR : Fail to run utility and error to import results into Jira. :: Upload Level");
            }

            int fetchAttempts = 0;
            int maximumAttempts = 5;
            boolean hasTestCycleKey = false;

            Map progressResponse;
            do {
                Thread.sleep(5000L);
                progressResponse = apiHelper.getProgressStatus((String) trackInfoUrl.get("TRACKING_ID"));
                System.out.println(progressResponse);
                ++fetchAttempts;

                try {
                    hasTestCycleKey = ((String) progressResponse.get("TEST_CYCLE_HAS_KEY")).isEmpty();
                } catch (NullPointerException var9) {
                    System.out.println("Test Cycle Key has not yet been assigned...");
                }
            } while (!hasTestCycleKey && fetchAttempts < maximumAttempts);

            if (!progressResponse.containsKey("TEST_CYCLE_ISSUE_KEY")) {
                throw new RuntimeException("Alert: Jira is taking longer than expected to return the Test Cycle Issue Key");
            }

            System.out.println("✅ Progress Status: " + (String) progressResponse.get("PROGRESS_STATUS"));
            System.out.println("✅ Import Status: " + (String) progressResponse.get("IMPORT_STATUS"));
            System.out.println("\ud83c\udd94 Test Cycle Issue Key:" + (String) progressResponse.get("TEST_CYCLE_ISSUE_KEY"));
            System.out.println("\u001b[32;1;2m+-------------------------+\u001b[0m");
            if (!((String) progressResponse.get("PROGRESS_STATUS")).equals("SUCCESS")) {
                throw new RuntimeException("Alert :It seems the Results file imported, but the server is not getting proper response.Please check Qmetry Test Management :: Progress Level");
            }

            System.out.println("\ud83d\ude80 SUCCESS : Results imported into Jira. \ud83d\ude80");
        } catch (Exception var10) {
            Exception mainEx = var10;
            mainEx.printStackTrace();
        }

    }

    private static void customParameters(String[] args) {
        TEST_RESULT_FILE_FULLPATH = "src/main/java/utils/testng-results.xml";

        try {
            SUITE_NAME = "Automated TestSuite";
            if (SUITE_NAME == null) {
                SUITE_NAME = "empty";
            }
        } catch (Exception var8) {
            SUITE_NAME = "empty";
        }

        try {
            ENVIRONMENT = "Stage";
            if (ENVIRONMENT == null) {
                ENVIRONMENT = "empty";
            }
        } catch (Exception var7) {
            ENVIRONMENT = "empty";
        }

        try {
            BUILD = EmailSender.harvesterVersion;
            if (BUILD == null) {
                BUILD = "empty";
            }
        } catch (Exception var6) {
            BUILD = "empty";
        }

        try {
            COMPONENT = "E2E";
            if (COMPONENT == null) {
                COMPONENT = "empty";
            }
        } catch (Exception var5) {
            COMPONENT = "empty";
        }

        try {
            TESTCASE_FOLDERPATH = "ModFS E2E Daily Smoke Scenarios";
            if (TESTCASE_FOLDERPATH == null) {
                TESTCASE_FOLDERPATH = "empty";
            }
        } catch (Exception var4) {
            TESTCASE_FOLDERPATH = "empty";
        }

        try {
            TESTCYCLE_FOLDERPATH = "QEE End to End Testing";
            if (TESTCYCLE_FOLDERPATH == null) {
                TESTCYCLE_FOLDERPATH = "empty";
            }
        } catch (Exception var3) {
            TESTCYCLE_FOLDERPATH = "empty";
        }

        try {
            STORY_ID = "FFILLSVCS-TR-4735";
            if (STORY_ID == null) {
                STORY_ID = "empty";
            }
        } catch (Exception var2) {
            STORY_ID = "empty";
        }

    }
}
