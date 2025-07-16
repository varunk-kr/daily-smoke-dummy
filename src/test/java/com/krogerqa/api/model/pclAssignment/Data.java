package com.krogerqa.api.model.pclAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private long timeToAssign;
    private ArrayList<Assignments> assignments = new ArrayList<>();

    public Data() {
    }

    public Data(long timeToAssign, ArrayList<Assignments> assignments) {
        this.timeToAssign = timeToAssign;
        this.assignments = assignments;
    }

    public long getTimeToAssign() {
        return timeToAssign;
    }

    public ArrayList<Assignments> getAssignments() {
        return assignments;
    }
}
