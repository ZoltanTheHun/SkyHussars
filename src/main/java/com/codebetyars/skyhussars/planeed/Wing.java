package com.codebetyars.skyhussars.planeed;

import javafx.beans.property.SimpleStringProperty;

public class Wing {
    
    private SimpleStringProperty name;
    private SimpleStringProperty direction;
    private SimpleStringProperty wingArea;
    private SimpleStringProperty incidence;
    private SimpleStringProperty cog;
    private SimpleStringProperty aspectRatio;
    private SimpleStringProperty damper;
    private SimpleStringProperty dehidralDegree;
            
            
    public Wing(String name, String direction, String wingArea,
            String incidence,String cog,String aspectRatio,String damper,
    String dehidralDegree) {
        this.name = new SimpleStringProperty(name);
        this.direction = new SimpleStringProperty(direction);
        this.wingArea = new SimpleStringProperty(wingArea);
        this.incidence = new SimpleStringProperty(incidence);
        this.cog = new SimpleStringProperty(cog);
        this.aspectRatio = new SimpleStringProperty(aspectRatio);    
        this.damper = new SimpleStringProperty(damper);
        this.dehidralDegree = new SimpleStringProperty(dehidralDegree);
    }
    
    public String getName(){return name.get();}
    public String getDirection(){return direction.get();}
    public String getWingArea(){return wingArea.get();}
    public String getIncidence(){return incidence.get();}
    public String getCog(){return cog.get();}
    public String getAspectRatio(){return aspectRatio.get();}
    public String getDamper(){return damper.get();}
    public String getDehidralDegree(){return dehidralDegree.get();}
    
    public Wing setName(String name){this.name.set(name); return this;}
    public Wing setDirection(String direction){this.direction.set(direction);return this;}
    public Wing setWingArea(String wingArea){this.wingArea.set(wingArea);return this;}
    public Wing setIncidence(String incidence){this.incidence.set(incidence);return this;}
    public Wing setCog(String cog){this.cog.set(cog);return this;}
    public Wing setAspectRatio(String aspectRatio){this.aspectRatio.set(aspectRatio);return this;}
    public Wing setDamper(String damper){this.damper.set(damper);return this;}
    public Wing setDehidralDegree(String dehidralDegree){this.dehidralDegree.set(dehidralDegree);return this;}

    
        
}
