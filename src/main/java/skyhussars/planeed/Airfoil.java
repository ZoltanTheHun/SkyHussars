package skyhussars.planeed;

import javafx.beans.property.SimpleStringProperty;

public class Airfoil {
    
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty direction  = new SimpleStringProperty();;
    private SimpleStringProperty wingArea  = new SimpleStringProperty();;
    private SimpleStringProperty incidence  = new SimpleStringProperty();;
    private SimpleStringProperty cog  = new SimpleStringProperty();;
    private SimpleStringProperty aspectRatio  = new SimpleStringProperty();;
    private SimpleStringProperty damper = new SimpleStringProperty();;
    private SimpleStringProperty dehidralDegree = new SimpleStringProperty();;
           
    public Airfoil(){};
            
    public Airfoil(Airfoil af){
        this.name.set(af.name.get());
        this.direction.set(af.direction.get());
        this.cog.set(af.direction.get());
        this.damper.set(af.direction.get());
        this.wingArea.set(af.wingArea.get());
        this.incidence.set(af.incidence.get());
        this.aspectRatio.set(af.aspectRatio.get());
        this.dehidralDegree.set(af.dehidralDegree.get());
    }
        
    public String getName(){return name.get();}
    public String getDirection(){return direction.get();}
    public String getWingArea(){return wingArea.get();}
    public String getIncidence(){return incidence.get();}
    public String getCog(){return cog.get();}
    public String getAspectRatio(){return aspectRatio.get();}
    public String getDamper(){return damper.get();}
    public String getDehidralDegree(){return dehidralDegree.get();}
    
    public Airfoil setName(String name){this.name.set(name); return this;}
    public Airfoil setDirection(String direction){this.direction.set(direction);return this;}
    public Airfoil setWingArea(String wingArea){this.wingArea.set(wingArea);return this;}
    public Airfoil setIncidence(String incidence){this.incidence.set(incidence);return this;}
    public Airfoil setCog(String cog){this.cog.set(cog);return this;}
    public Airfoil setAspectRatio(String aspectRatio){this.aspectRatio.set(aspectRatio);return this;}
    public Airfoil setDamper(String damper){this.damper.set(damper);return this;}
    public Airfoil setDehidralDegree(String dehidralDegree){this.dehidralDegree.set(dehidralDegree);return this;}

    
        
}
