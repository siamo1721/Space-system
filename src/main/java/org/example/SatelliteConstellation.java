package org.example;

import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private  String constellationName;
    private List<Satellite> satelliteList = new ArrayList<>();

    public void addSatellite(Satellite satellite){
        satelliteList.add(satellite);
        System.out.println(satellite.name +" добавлен в группировку " + "'" + this.constellationName+ "'");
    }
    public List<Satellite> getSatellite(){
        return satelliteList;
    }

    public String getConstellationName(){
        return this.constellationName;
    }
    public void executeAllMission(){
        for(Satellite el : satelliteList){
            el.performMission();
        }
    }
    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
        this.satelliteList = new ArrayList<>();
    }
}
