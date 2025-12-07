package org.example;

public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;

    public double getResolution() {
        return this.resolution;
    }

    public int getPhotosTaken() {
        return this.photosTaken;
    }


    public ImagingSatellite(double resolution, int photosTaken, String name, double batteryLevel, boolean isActive){
        this.resolution = resolution;
        this.photosTaken = photosTaken;
        this.name = name;
        this.batteryLevel = batteryLevel;
        this.isActive = isActive;
    }
    @Override
    protected void performMission() {
        if (this.isActive) {
            consumeBattery(0.08);
        }
        if (this.isActive) {
            takePhoto();
            System.out.println( this.name + ":" +" Съемка территории с разрешением "+ this.resolution +" м/пиксель");
            System.out.println(this.name + ": " + "Снимок #" +this.getPhotosTaken()+ " сделан!");
        }else{
            System.out.println("\uD83D\uDED1 "+this.name + ":"+" Не может выполнить съемку - не активен");
        }
    }

    private void takePhoto() {
        if (this.isActive) {
            this.photosTaken++;
        }
    }

    @Override
    public String toString() {
        return "ImagingSatellite{name='" + name +
                "', battery=" + batteryLevel +
                ", active=" + isActive +
                ", resolution=" + resolution +
                ", photosTaken=" + photosTaken +
                "}";
    }
}
