package com.codebetyars.skyhussars.engine;

public abstract class GameState {
    public abstract GameState update(float tpf);
    public abstract void close();
    public abstract void initialize();
}
