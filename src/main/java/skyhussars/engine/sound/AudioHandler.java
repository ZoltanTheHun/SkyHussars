/*
 * Copyright (c) 2016, ZoltanTheHun
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
package skyhussars.engine.sound;

import com.jme3.audio.AudioNode;

public class AudioHandler {

    private enum Command {

        PLAY, STOP, PAUSE;
    }

    private Command command = Command.STOP;
    private Command prevCommand = Command.STOP;

    private AudioNode audioNode;

    private float pitch;

    public AudioHandler(AudioNode audioNode) {
        this.audioNode = audioNode;
        pitch = audioNode.getPitch();
    }

    public AudioNode audioNode() {
        return audioNode;
    }

    synchronized public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    synchronized public void update() {
        if (command != prevCommand) {
            switch (command) {
                case PLAY:
                    audioNode.play();
                    break;
                case STOP:
                    audioNode.stop();
                    break;
                case PAUSE:
                    audioNode.pause();
                    break;
            }
            prevCommand = command;
        }
        if (Math.abs(pitch - audioNode.getPitch()) > 0.01) {
            audioNode.setPitch(pitch);
        }
    }

    synchronized public void play() {
        command = Command.PLAY;
    }

    synchronized public void stop() {
        command = Command.STOP;
    }

    synchronized public void pause() {
        command = Command.PAUSE;
    }
}
