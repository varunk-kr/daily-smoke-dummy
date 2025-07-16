package com.krogerqa.api.model.flexJob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class AcceptFlexJobPayload {
    private final String agencyJobId;
    private final String jobId;
    private final String status;
    private final Agent agent;
    private final DateTime datetime;


    public AcceptFlexJobPayload(String agencyJobId, String jobId, String status, Agent agent, DateTime datetime) {
        this.agencyJobId = agencyJobId;
        this.jobId = jobId;
        this.status = status;
        this.agent = agent;
        this.datetime = datetime;
    }

    public String getAgencyJobId() {
        return agencyJobId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public Agent getAgent() {
        return agent;
    }

    public DateTime getDatetime() {
        return datetime;
    }
}
