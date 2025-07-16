package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.*;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EmailSender {

    static final Map<String, String> packageSuiteName = new HashMap<>() {{
        put("MODFS E2E PCL FLOWS", "harvesterWithPCL");
        put("MODFS E2E MAFP CIAO CHANGES", "mafpCiaoChanges");
        put("MODFS E2E ENHANCED BATCHING BYOB FLOWS WITH PCL", "enhancedDynamicBatching");
        put("MODFS E2E TC52 & RUSH ORDER FLOWS WITH PCL", "tc52NotificationsWithPCL");
        put("MODFS E2E PCL OVERSIZE FLOWS", "pclOversize");
        put("MODFS E2E DB OS PCL SINGLE THREAD BULK STAGING FLOWS", "dynamicBatchingSingleThreadPCL");
        put("MODFS E2E INSTACART FLOWS", "instacart");
        put("MODFS E2E EXPRESS ORDER OOOT FLOWS", "dynamicBatchingExpressOrder");
        put("MODFS E2E HT DYB SINGLE THREAD FLOWS", "harrisTeeterDynamicBatching");
        put("MODFS E2E SC MT INTEGRATION FLOWS", "serviceCounterIntegration");
        put("MODFS E2E 9 TOTE+ FLOWS", "dynamicBatchingWithNineTote");
    }};
    static String recipientMails = "varun.kumar@kroger.com,vaishak.menon@kroger.com,rashmi.nair@kroger.com,kiranmai.aluru@kroger.com,raghvendra.kulkarni@kroger.com,nimish.wadhawan@kroger.com,shivani.lnu@kroger.com,meghana.pakala@kroger.com,varun.mohan@kroger.com,alekya.krishna@kroger.com,devika.bandi@kroger.com";
    static String pieChartTitle = "MODFS E2E SMOKE FLOWS";
    static List<String> artifactsNames = Arrays.asList("HT DyB Artifacts", "9Tote+ Artifacts", "Shipt Artifacts", "ScMT Artifacts", "PclSmoke Artifacts", "ModifyCancel Artifacts", "PreWeighed Artifacts", "CarryOverTakeOver Artifacts", "ReusePcl Artifacts", "PclOversize Artifacts", "Tc52 Artifacts", "Mafp Artifacts", "ByobDyB Artifacts", "DbPclSmoke Artifacts", "Instacart Artifacts");
    static List<String> suiteNames = Arrays.asList("MODFS E2E PCL FLOWS", "MODFS E2E MAFP CIAO CHANGES", "MODFS E2E ENHANCED BATCHING BYOB FLOWS WITH PCL", "MODFS E2E TC52 & RUSH ORDER FLOWS WITH PCL", "MODFS E2E PCL OVERSIZE FLOWS", "MODFS E2E DB OS PCL SINGLE THREAD BULK STAGING FLOWS", "MODFS E2E INSTACART FLOWS", "MODFS E2E EXPRESS ORDER OOOT FLOWS", "MODFS E2E HT DYB SINGLE THREAD FLOWS", "MODFS E2E SC MT INTEGRATION FLOWS", "MODFS E2E 9 TOTE+ FLOWS");
    static String dailyExecutionStatus = "Hi Team, Please find the today's daily execution status below:";
    static List<String> additionalInfoColumns = Arrays.asList("Git Repo Link", "QMetry Smoke Scenarios", "Confluence Link", "Github Action", "Report Portal", "QMetry Test Cycle", "Harvester version", "Ciao version");
    static String gitRepoLink = "https://github.com/krogertechnology/fst-FulfillmentETEAutomation";
    static String qmetryLink = "https://jira.kroger.com/jira/secure/QTMAction.jspa#/Manage/TestCase?folderId=69038&projectId=32480";
    static String confluenceLink = "https://confluence.kroger.com/confluence/display/FST/Automation+Execution+Status?src=contextnavpagetreemode";
    static String reportPortalLaunchesLink = "http://reportportal.kroger.com:8080/ui/login#fst-fullfilment-e2e/launches/all";
    static int totalPass = 0;
    static int totalFail = 0;
    static String harvesterVersion = "harvester-stage-5.27.0-release.apk";
    static String ciaoVersion = "CiaoAndroid-debug-3.4-debug.510.apk";
    static String testCycle = "https://jira.kroger.com/jira/secure/QTMAction.jspa#/TestCycleDetail/b4JmfXGzMFZ?projectId=32480";

    /*Generating detailed table for each package*/
    private static String generateTableBasedOnScenarioForEachPackage(String suiteName, String htmlContent) {
        htmlContent = htmlContent + "<br><h3>" + suiteName + "</h3>" + "<table border=2 cell-spacing=0 cell-padding=2 style=\"text-align:center;width:100%;border-collapse: collapse;border:3px solid black\">";
        htmlContent = htmlContent + "<tr style=\"background-color:#DCDCDC;border:3px solid black\"><td style=\"width:5%\"><b>Sr. No.</b></td><td style=\"width:60%\"><b>Scenario Name</b></td><td style=\"width:15%\"><b>Order Number</b></td><td style=\"width:10%\"><b>Store ID</b></td><td style=\"width:10%\"><b>Result</b></td></tr>";
        int i = 0;
        OrderIdAndStoreIdHelper.getAllScenarioOrderNumbers();
        OrderIdAndStoreIdHelper.getAllScenarioStoreNumbers();
        String packageName = packageSuiteName.get(suiteName);
        StringBuilder htmlContentBuilder = new StringBuilder(htmlContent);
        for (String key : E2eListeners.scenarioPackageMap.keySet()) {
            String value = E2eListeners.scenarioPackageMap.get(key);
            String description = E2eListeners.scenarioDescriptionMap.get(key);
            String result = String.valueOf(E2eListeners.totalScenarioResults.get(key));
            String orderNumber = String.valueOf(E2eListeners.scenarioOrderNumbers.get(key));
            String storeNumber = String.valueOf(E2eListeners.scenarioStoreNumbers.get(key));
            if (orderNumber == null || orderNumber.equals("null") || orderNumber.isEmpty() || orderNumber.equals(" ")) {
                orderNumber = "--";
            }
            if (storeNumber == null || storeNumber.equals("null") || storeNumber.isEmpty() || storeNumber.equals(" ")) {
                storeNumber = "--";
            }
            if (result.equals("true")) {
                result = "PASS";
            } else {
                result = "FAIL";
            }
            if (value.equals(packageName)) {
                i++;
                htmlContentBuilder.append("<tr>");
                htmlContentBuilder.append("<td>").append(i).append("</td>");
                htmlContentBuilder.append("<td>").append(description).append("</td>");
                htmlContentBuilder.append("<td>").append(orderNumber).append("</td>");
                htmlContentBuilder.append("<td>").append(storeNumber).append("</td>");
                if (result.equals("PASS")) {
                    htmlContentBuilder.append("<td>").append("<b style=\"color:green\">").append(result).append("</b>").append("</td>");
                } else {
                    htmlContentBuilder.append("<td>").append("<b style=\"color:red\">").append(result).append("</b>").append("</td>");
                }
                htmlContentBuilder.append("</tr>");
            }
        }
        htmlContent = htmlContentBuilder.toString();
        htmlContent = htmlContent + "</table>";
        return htmlContent;
    }


    /*Generating the suite data - how many pass/fail in each suite
    public static String getAllRequiredSuiteResultsData(String suiteName, String htmlContent) {
        int totalCount = 0;
        int pass = 0;
        int fail = 0;
        String packageName = packageSuiteName.get(suiteName);
        for (String key : E2eListeners.scenarioPackageMap.keySet()) {
            String value = E2eListeners.scenarioPackageMap.get(key);
            String result = String.valueOf(E2eListeners.totalScenarioResults.get(key));
            if (value.equals(packageName)) {
                totalCount++;
                if (result.equals("true")) {
                    pass++;
                } else {
                    fail++;
                }
            }
        }
        if (totalCount != 0) {
            if (pass == totalCount) {
                htmlContent += "<td style=\"background-color:#90EE90;color:black;border:2px solid black\"'>" + "<b>PASS</b>" + "</td>";
            } else if (fail == totalCount) {
                htmlContent += "<td style=\"background-color:#FA8072;color:black;border:2px solid black\"'>" + "<b>FAIL</b>" + "</td>";
            } else {
                htmlContent += "<td style=\"background-color:#FFBF00;color:black;border:2px solid black\"'>" + "<b>PARTIALLY PASS</b>" + "</td>";
            }
        } else {
            htmlContent += "<td style=\"background-color:black;color:white;border:2px solid black\"'>" + "<b>NOT EXECUTED</b>" + "</td>";
        }
        return htmlContent;
    }*/

    /*  generating additionalInfoTable data*/
    public static String generateAdditionalInfoTable(String htmlContent, String url) {
        htmlContent += "<br><br><h3>" + "Additional Information:" + "</h3>" + "<table cell-spacing=5 cell-padding=5 style=\"text-align:center;width:100%;border-collapse: collapse;border:3px solid black\">";
        for (String additionalInfoColumn : additionalInfoColumns) {
            htmlContent += "<tr style=\"border:3px solid black;height:35px\"><td style=\"background-color:#DCDCDC;\"><b>" + additionalInfoColumn + "</b></td>";
            switch (additionalInfoColumn) {
                case "Git Repo Link":
                    htmlContent += "<td style=\"border:2px solid black\"><a href=\"" + gitRepoLink + "\">Fulfillment E2E Repo</a></td>";
                    break;
                case "QMetry Smoke Scenarios":
                    htmlContent += "<td style=\"border:2px solid black\"><a href=\"" + qmetryLink + "\">" + "Smoke Scenarios" + "</a></td>";
                    break;
                case "Confluence Link":
                    htmlContent += "<td style=\"border:2px solid black\"><a href=\"" + confluenceLink + "\">Fulfillment E2E Status Confluence</a></td>";
                    break;
                case "Github Action":
                    htmlContent += "<td style=\"border:2px solid black\"><a href=\"" + "https://github.com/krogertechnology/fst-FulfillmentETEAutomation/actions/runs/" + url + "\">Github Action CI/CD Link</a></td>";
                    break;
                case "Report Portal":
                    htmlContent += "<td style=\"border:2px solid black\" colspan=9><a href=\"" + reportPortalLaunchesLink + "\">Report Portal Launches</a></td>";
                    break;
                case "QMetry Test Cycle":
                    htmlContent += "<td style=\"border:2px solid black\" colspan=9><a href=\"" + testCycle + "\">QMetry Test Cycle</a></td>";
                    break;
                case "Harvester version":
                    htmlContent += "<td style=\"border:2px solid black\">" + harvesterVersion + "</td>";
                    break;
                case "Ciao version":
                    htmlContent += "<td style=\"border:2px solid black\">" + ciaoVersion + "</td>";
                    break;
            }
            htmlContent += "</tr>";
        }
        htmlContent += "</table><br>";
        return htmlContent;
    }

    public static void main(String[] args) {
        getAllDataFromListeners();
        String from = "noreply@kroger.com";
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.kroger.com");
        props.setProperty("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "false");
        String recipientEmail = recipientMails;
        String url = args[3];
        Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
            }
        });
        InternetAddress fromAddress = null;
        try {
            fromAddress = new InternetAddress(from);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        try {
            String htmlContent = "";
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.setFrom(fromAddress);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Daily Automation Execution Status: " + java.time.LocalDate.now());

            /*adding header*/
            htmlContent = htmlContent + dailyExecutionStatus + "<br><br>";


            byte[] chartImageBytes = generateImage();
            htmlContent = htmlContent + "<div style=\"display:flex;flex-wrap:wrap\">";
            htmlContent = htmlContent + "<img style=\"margin:auto\" src=\"data:image/png;base64," + Base64.getEncoder().encodeToString(chartImageBytes) + "\" alt=\"Pie Chart " + " " + "\">";
            htmlContent = htmlContent + "<br>";

            /*SuiteDetailTable*/
            htmlContent = htmlContent + "</div><br><br>Please find below the detailed report:<br>";
            for (String suiteName : suiteNames) {
                htmlContent = generateTableBasedOnScenarioForEachPackage(suiteName, htmlContent);
            }

            /*AdditionalInfoTable*/
            htmlContent = generateAdditionalInfoTable(htmlContent, url);

            /*adding footer*/
            htmlContent += "Please let us know if you need any additional info.<br><br><b>Thanks</b><br>Fulfillment ETE Team";

            /*Zip file creation code*/
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html");

            String filePath3 = "src/test/resources/output-data/job";
            Multipart multipart = new MimeMultipart();

            int j = 0;
            /*Creating the attachment*/
            for (int i = 0; i < 15; i++) {
                j++;
                MimeBodyPart attachmentPart = new MimeBodyPart();
                String filePath1 = filePath3 + j + "/test-output/Reports";
                String filePath2 = filePath3 + j + "/target/Logs";
                String destFile = filePath3 + j + "/" + artifactsNames.get(i) + ".zip";
                try (FileOutputStream fos = new FileOutputStream(destFile);
                     ZipOutputStream zos = new ZipOutputStream(fos)) {
                    addFolderToZip(filePath1, filePath1, zos);
                    addFolderToZip(filePath2, filePath2, zos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                attachmentPart.attachFile(destFile);
                multipart.addBodyPart(attachmentPart);
            }

            /*Attaching the zip file and html content*/
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            /*Sending the email*/
            Transport.send(message);
            System.out.println("Mail Sent successfully");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Exception" + e);
        }
    }

    /*generation of pie charts*/
    private static byte[] generateImage() throws IOException {
        byte[] chartImageBytes;
        int totalCount = 0;
        double passPercentage = 0;
        double failPercentage = 0;
        for (String key : E2eListeners.totalScenarioResults.keySet()) {
            Boolean value = E2eListeners.totalScenarioResults.get(key);
            totalCount++;
            if (value) {
                totalPass++;
            } else {
                totalFail++;
            }
        }
        if (totalCount != 0) {
            passPercentage = (totalPass * 100.0) / totalCount;
            failPercentage = (totalFail * 100.0) / totalCount;
        }
        /*Round pass and fail percentages to 1 decimal place and format them*/
        String passPercentageFormatted = (passPercentage % 1 == 0) ?
                String.format("%.0f", passPercentage) :
                String.format("%.1f", passPercentage);
        String failPercentageFormatted = (failPercentage % 1 == 0) ?
                String.format("%.0f", failPercentage) :
                String.format("%.1f", failPercentage);
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Pass", Double.parseDouble(passPercentageFormatted));
        dataset.setValue("Fail", Double.parseDouble(failPercentageFormatted));
//        dataset.setValue("Blocked", 0);
        JFreeChart chart = ChartFactory.createPieChart(
                pieChartTitle, dataset, true, true, false
        );

        /*Setting the legends and background color of the pie chart*/
        PiePlot plot = (PiePlot) chart.getPlot();
        int finalPass = totalPass;
        int finalFail = totalFail;
        double finalPassPercentage = passPercentage;
        double finalFailPercentage = failPercentage;
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator() {
            @Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                Number value = dataset.getValue(key);
                double percentage = 0;
                int count = 0;
                if (value != null) {
                    if (key.equals("Pass")) {
                        count = finalPass;
                        percentage = finalPassPercentage;
                    } else if (key.equals("Fail")) {
                        count = finalFail;
                        percentage = finalFailPercentage;
                    }
                }
                String formattedPercentage;
                if (percentage % 1 != 0) {
                    formattedPercentage = String.format("%.1f", percentage);
                } else {
                    formattedPercentage = String.format("%.0f", percentage);
                }
                return key.toString() + " " + count + ", " + (formattedPercentage) + "%";
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
                return null;
            }
        });
        plot.setBackgroundPaint(Color.WHITE);

        /*Setting the color of the fields*/
        plot.setSectionPaint("Pass", new Color(58, 255, 117));
        plot.setSectionPaint("Fail", new Color(255, 100, 95));
//        plot.setSectionPaint("Blocked", Color.GRAY);

        /*Generating the image of the pie chart*/
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 500, 300);
        chartImageBytes = out.toByteArray();
        return chartImageBytes;
    }

    /*Adding the folder to Zip file*/
    private static void addFolderToZip(String rootFolder, String folderPath, ZipOutputStream zos) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addFolderToZip(rootFolder, file.getPath(), zos); // Recursively add subfolder
                } else {
                    addFileToZip(rootFolder, file.getPath(), zos); // Add file to zip
                }
            }
        }
    }

    /*Adding the file to Zip*/
    private static void addFileToZip(String rootFolder, String filePath, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            /*Calculate relative path within zip file*/
            String entryName = filePath.substring(rootFolder.length() + 1);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    /*Getting all the required data from the Listeners variable*/
    public static void getAllDataFromListeners() {
        String projectRoot = System.getProperty("user.dir");
        for (int i = 1; i <= 15; i++) {
            try (FileReader reader = new FileReader(projectRoot + "/src/test/resources/test-results/job" + i + "/results.json")) {

                /*Parse JSON file into JsonObject*/
                JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);

                /*Extract scenarioPackageMap*/
                JsonObject packageMap = jsonObject.getAsJsonObject("scenarioPackageMap");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : packageMap.entrySet()) {
                    E2eListeners.scenarioPackageMap.put(entry.getKey(), entry.getValue().getAsString());
                }

                /*Extract finalScenarioResults*/
                JsonObject results = jsonObject.getAsJsonObject("finalScenarioResults");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : results.entrySet()) {
                    E2eListeners.totalScenarioResults.put(entry.getKey(), entry.getValue().getAsBoolean());
                }

                /*Extract scenarioDescriptionMap*/
                JsonObject descriptionMap = jsonObject.getAsJsonObject("scenarioDescriptionMap");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : descriptionMap.entrySet()) {
                    E2eListeners.scenarioDescriptionMap.put(entry.getKey(), entry.getValue().getAsString());
                }

                /*Extract testCaseIdOrderNumbersMap*/
                JsonObject testCaseIdOrderNumbersMap = jsonObject.getAsJsonObject("testCaseIdOrderNumbers");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : testCaseIdOrderNumbersMap.entrySet()) {
                    E2eListeners.testCaseIdOrderNumbers.put(entry.getKey(), entry.getValue().getAsString());
                }

                /*Extract testCaseIdStoreNumbersMap*/
                JsonObject testCaseIdStoreNumbersMap = jsonObject.getAsJsonObject("testCaseIdStoreNumbers");
                for (Map.Entry<String, com.google.gson.JsonElement> entry : testCaseIdStoreNumbersMap.entrySet()) {
                    E2eListeners.testCaseIdStoreNumbers.put(entry.getKey(), entry.getValue().getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
