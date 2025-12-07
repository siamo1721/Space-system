package org.example;

public class CommunicationSatellite extends Satellite {

    private double bandWidth;

    public double getBandWidth() {
        return this.bandWidth;
    }

    private void sendData(double amount) {
        System.out.println(this.name + ": Передача данных со скоростью " + amount + " Мбит/с");
        System.out.println(this.name + ": Отправил " + amount + " Мбит данных!");
    }
    public CommunicationSatellite(String name, double batteryLevel, boolean isActive,double bandWidth  ){
        this.name = name;
        this.batteryLevel = batteryLevel;
        this.isActive = isActive;
        this.bandWidth = bandWidth;
    }
    @Override
    protected void performMission() {
        if (this.isActive) {
            consumeBattery(0.05);
            if (this.isActive) {
                sendData(this.bandWidth);
            }
        }
    }
    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "name='" + name + '\'' +
                ", batteryLevel=" + batteryLevel +
                ", isActive=" + isActive +
                ", bandWidth=" + bandWidth +
                '}';
    }
}
