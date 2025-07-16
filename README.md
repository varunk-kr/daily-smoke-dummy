[![Sonar Scanner](https://github.com/krogertechnology/fulfillment-ete-qa/actions/workflows/sonarScanner.yml/badge.svg)](https://github.com/krogertechnology/fulfillment-ete-qa/actions/workflows/sonarScanner.yml)


# Fulfillment End to End Automation Test Suite
The Fulfillment End-to-End Automation Test Suite represents an end-to-end solution for testing Fulfillment Services applications, as well as uphill and downhill dependencies. Orders are placed on the stage environment Kroger front end site, managed in Cue, picked and staged in Cue, then destaged and tendered in Ciao.

___________________

# How to Contribute
All new branches should be created according to the following structure:

- `feature/FFILLSVCS-XXXXX-name-of-feature`
- `hotfix/FFILLSVCS-XXXXX-purpose-of-hotfix`

If changes being made don't correlate to a Jira ticket, the branch should be named as such:
- `feature/NOJIRA-name-of-feature`
- `hotfix/NOJIRA-purpose-of-hotfix`

All code should be merged from the `feature` branch to the `development` branch. All PRs must have at least **two** approving Code Reviews and pass the Sonar Scan and Sonar Quality Gate to successfully merge.

#Executing the QMetry task

To execute the QMetry task, open the terminal in your IDE and execute the following command.

`mvn exec:java`

IMPORTANT: YOU WOULD NEED TO EXECUTE THIS COMMAND EVERY TIME YOU WANT TO UPLOAD THE RESULTS TO QMETRY. IT IS NOT A ONE TIME STEP, AND NEEDS TO BE EXECUTED AFTER EACH TEST RUN IS CONCLUDED.
