/*
 * Copyright (c) 2019, ZoltanTheHun
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
package skyhussars;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import skyhussars.engine.SettingsManager;
import skyhussars.engine.terrain.TheatreLoader;
import skyhussars.terrained.OpenTheatrePopup;
import skyhussars.terrained.TerrainProperties;

@Configuration
@ComponentScan(basePackages = {"skyhussars.terrained"})
public class TerrainEdConfig {
    
    @Bean
    public SettingsManager settingsManager() {
        return new SettingsManager(System.getProperty("user.dir"));
    }
    
    @Bean
    public TheatreLoader theatreLoader() {
        return new TheatreLoader(settingsManager().assetDirectory());
    }
    
    @Bean
    public TerrainProperties terrainProperties() {
        return new TerrainProperties();
    }

    @Bean
    public OpenTheatrePopup theatreSelectorPopup() {
        return new OpenTheatrePopup(theatreLoader(), terrainProperties());
    }

}
