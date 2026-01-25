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

    public CommunicationSatellite(String name, double batteryLevel, double bandWidth) {
        super(name, batteryLevel);
        this.bandWidth = bandWidth;
    }

    @Override
    protected void performMission() {
        if (isActive()) {
            energy.consume(0.05);
            if (isActive()) {
                sendData(this.bandWidth);
            }
        }
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{" +
                "name='" + name + '\'' +
                ", batteryLevel=" + energy.getBatteryLevel() +
                ", isActive=" + state.isActive() +
                ", bandWidth=" + bandWidth +
                '}';
    }
}
