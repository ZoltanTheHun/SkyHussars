/*
 * Copyright (c) 2017, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
