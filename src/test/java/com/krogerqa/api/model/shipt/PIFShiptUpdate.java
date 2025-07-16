package com.krogerqa.api.model.shipt;

public class PIFShiptUpdate {
    public PIFShiptUpdate(String event, PIFShiptUpdateData data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public PIFShiptUpdateData getData() {
        return data;
    }

    public void setData(PIFShiptUpdateData data) {
        this.data = data;
    }

    public PIFShiptUpdate() {
    }

    private String event;
    private PIFShiptUpdateData data;
}
