import os
import sys
import xml.etree.ElementTree as ET
import csv
import json
import logging
import requests
from datetime import datetime

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def read_capability_csv(csv_file_path):
    logger.info(f"csv file path {csv_file_path}")
    capability_mapping = {}
    try:
        with open(csv_file_path, mode='r') as file:
            reader = csv.reader(file)
            next(reader)  # Skip header row
            for row in reader:
                if len(row) == 3:  # Ensure there are 3 columns
                    test_name, capability, test_domain = row
                    capability_mapping[test_name] = (capability, test_domain)
    except Exception as e:
        logger.info(f"Error reading CSV file: {e}")
    return capability_mapping

def count_passing_tests(file_path, capability_mapping, test_environment, test_type):
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()

        # Handle 'testsuite' or multiple 'testsuite' elements
        testsuites = [root] if root.tag == 'testsuite' else root.findall('testsuite')

        test_results = []
        current_timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        for suite in testsuites:
            for testcase in suite.findall('testcase'):
                test_name = testcase.attrib.get('name', 'Unknown')

                # Determine test result based on presence of failure, error, or skipped
                if testcase.find('failure') is not None:
                    result = "Failed"
                elif testcase.find('error') is not None:
                    result = "Failed"
                elif testcase.find('skipped') is not None:
                    result = "Skipped"
                else:
                    result = "Success"  # Mark as Passed

                # Print the result for the test case (including Passed tests)
                print(f"Test Case: {test_name} - {result}")

                # Match capability from the CSV for mapping
                if test_name in capability_mapping:
                    capability, test_domain = capability_mapping[test_name]
                    capabilities = capability.split('|')  # Split multiple capabilities
                    for cap in capabilities:
                        test_results.append({
                            "EnvironmentVariable": test_name,
                            "TestStatus": result,
                            "TestExecutionTimestamp": current_timestamp,
                            "TestType": test_type,
                            "TestDomain": test_domain,
                            "TestCapabilities": cap.strip(),
                            "TestEvent": "null",
                            "TestDOM": "null",
                            "TestContract": "null",
                            "TestName": test_name,
                            "TestLocation": "null",
                            "TestEnvironment": test_environment
                        })
                else:
                    print(f"Ignoring test case {test_name}: No matching capability found")

        logger.info(f"Test Results {test_results}")
        # Send results to the API
        if test_results:
            logger.info(f"Sending data to azure...")
            url = "https://fn-lmlm-automation-report-app.azurewebsites.net/api/SaveTestResults"
            headers = {"Content-Type": "application/json"}
            response = requests.post(url, headers=headers, json=test_results)
            if response.status_code != 200:
                print(f"Error sending test results: {response.status_code} - {response.text}")

    except ET.ParseError as e:
        print(f"Error parsing XML file: {e}")
    except FileNotFoundError:
        print(f"File not found: {file_path}")
    except Exception as e:
        print(f"An error occurred: {e}")

def process_directory(directory_path, capability_mapping, test_environment, test_type):
    logger.info(f"Directory path: {directory_path}")
    try:
        for root, dirs, files in os.walk(directory_path):  # Traverse subdirectories
            for file_name in files:
                if file_name.endswith('.xml'):
                    file_path = os.path.join(root, file_name)
                    logger.info(f"\nProcessing file: {file_path}")
                    count_passing_tests(file_path, capability_mapping, test_environment, test_type)
    except FileNotFoundError:
        print(f"Directory not found: {directory_path}")
    except Exception as e:
        print(f"An error occurred while processing the directory: {e}")

if __name__ == "__main__":
    logger.info("Script invoked")
    # Get the directory path and CSV file from command line argument
    if len(sys.argv) != 3:
        print("Usage: python script_name.py <path_to_directory_with_xml_reports> <path_to_capability_csv>")
        sys.exit(1)

    testng_directory = sys.argv[1]
    capability_csv = sys.argv[2]
    logger.info(f" testng_directory {testng_directory}")
    logger.info(f" capability_csv {capability_csv}")

    # Read capabilities from CSV
    logger.info("Reading capability csv")
    capability_mapping = read_capability_csv(capability_csv)

    # Hardcoded values
    test_environment = "Stage"
    test_type = "End2End"

    # Process the XML files in the provided directory
    logger.info("Processing directory")
    process_directory(testng_directory, capability_mapping, test_environment, test_type)
