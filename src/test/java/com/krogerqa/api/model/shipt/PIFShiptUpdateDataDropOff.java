package com.krogerqa.api.model.shipt;

public class PIFShiptUpdateDataDropOff {
    public String getDelivery_window_starts_at() {
        return delivery_window_starts_at;
    }

    public void setDelivery_window_starts_at(String delivery_window_starts_at) {
        this.delivery_window_starts_at = delivery_window_starts_at;
    }

    public String getDelivery_window_ends_at() {
        return delivery_window_ends_at;
    }

    public void setDelivery_window_ends_at(String delivery_window_ends_at) {
        this.delivery_window_ends_at = delivery_window_ends_at;
    }

    public PIFShiptUpdateDataDropOff() {
    }

    private String delivery_window_starts_at;
    private String delivery_window_ends_at;

    public PIFShiptUpdateDataDropOff(String delivery_window_starts_at, String delivery_window_ends_at) {
        this.delivery_window_starts_at = delivery_window_starts_at;
        this.delivery_window_ends_at = delivery_window_ends_at;
    }
}
