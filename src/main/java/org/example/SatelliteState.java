package org.example;

import lombok.Data;

@Data
public class SatelliteState {

    private boolean active;
    private String statusMessage = "Не активирован";

    public void activate() {
        active = true;
        statusMessage = "Активен";
    }

    public void deactivate() {
        active = false;
        statusMessage = "Не активирован";
    }

}