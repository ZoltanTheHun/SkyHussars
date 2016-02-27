package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.*;
import com.codebetyars.skyhussars.engine.controls.ControlsMapper;
import com.codebetyars.skyhussars.engine.mission.MissionFactory;
import com.jme3.app.Application;
import com.jme3.input.FlyByCamera;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.system.AppSettings;
import jme3utilities.sky.SkyControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration
@ComponentScan
public class SkyHussarsContext {

    @Autowired
    private Application application;

    @Autowired
    private SkyControl skyControl;

    @Bean
    public SkyHussars application() {
        return new SkyHussars();
    }

    @Bean
    public AppSettings settings() {
        AppSettings settings = new AppSettings(false);
        settings.setTitle("SkyHussars");
        settings.setSettingsDialogImage("Textures/settings_image.jpg");
        return settings;
    }

    @Bean
    public SkyControl skyControl() {
        Calendar now = new GregorianCalendar();
        SkyControl skyControl = new SkyControl(application.getAssetManager(), application.getCamera(), 0.9f, true, true);
        skyControl.getSunAndStars().setHour(now.get(Calendar.HOUR_OF_DAY));
        skyControl.getSunAndStars().setSolarLongitude(now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        skyControl.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        skyControl.setCloudiness(0f);
        skyControl.setEnabled(true);
        return skyControl;
    }

}
