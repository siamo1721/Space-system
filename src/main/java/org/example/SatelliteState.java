package org.example;

public class SatelliteState {
    private boolean active;

    public boolean isActive(){
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }
}
