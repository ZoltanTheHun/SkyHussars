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
package com.codebetyars.skyhussars.engine.controls;

import com.jme3.input.KeyInput;
import java.util.HashMap;
import java.util.Map;


public class FlightKeyboardMap{
    
    public static enum Trigger{
        PITCH_DOWN,PITCH_UP,ROLL_LEFT,ROLL_RIGHT,
        THROTTLE_0,THROTTLE_20,THROTTLE_40,THROTTLE_60,THROTTLE_80,THROTTLE_100,
        FIRE
    }
      
    Map<Trigger,Integer> keys = new HashMap<>();
    {keys.put(Trigger.PITCH_DOWN,KeyInput.KEY_UP);} 
    {keys.put(Trigger.PITCH_UP,KeyInput.KEY_DOWN);}   
    {keys.put(Trigger.ROLL_LEFT,KeyInput.KEY_LEFT);}   
    {keys.put(Trigger.ROLL_RIGHT,KeyInput.KEY_RIGHT);}
    {keys.put(Trigger.THROTTLE_0,KeyInput.KEY_1);}
    {keys.put(Trigger.THROTTLE_20,KeyInput.KEY_2);}   
    {keys.put(Trigger.THROTTLE_40,KeyInput.KEY_3);}
    {keys.put(Trigger.THROTTLE_60,KeyInput.KEY_4);}
    {keys.put(Trigger.THROTTLE_80,KeyInput.KEY_5);}
    {keys.put(Trigger.THROTTLE_100,KeyInput.KEY_6);}
    {keys.put(Trigger.FIRE,KeyInput.KEY_SPACE);}

    public int getMapping(Trigger trigger){
       return keys.get(trigger);
    }   

}
