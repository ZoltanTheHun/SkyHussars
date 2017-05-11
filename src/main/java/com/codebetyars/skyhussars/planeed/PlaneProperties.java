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
package com.codebetyars.skyhussars.planeed;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlaneProperties {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty modelName = new SimpleStringProperty();
    private FloatProperty massEmpty = new SimpleFloatProperty();
    private FloatProperty massTakeOffMax = new SimpleFloatProperty();
    private FloatProperty massGross = new SimpleFloatProperty();

    public StringProperty getName() {
        return name;
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public StringProperty getModelName() {
        return modelName;
    }

    public void setModelName(StringProperty modelName) {
        this.modelName = modelName;
    }

    public FloatProperty getMassEmpty() {
        return massEmpty;
    }

    public void setMassEmpty(FloatProperty massEmpty) {
        this.massEmpty = massEmpty;
    }

    public FloatProperty getMassTakeOffMax() {
        return massTakeOffMax;
    }

    public void setMassTakeOffMax(FloatProperty massTakeOffMax) {
        this.massTakeOffMax = massTakeOffMax;
    }

    public FloatProperty getMassGross() {
        return massGross;
    }

    public void setMassGross(FloatProperty massGross) {
        this.massGross = massGross;
    }
}
