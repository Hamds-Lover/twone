package com.ray.twone;

import com.badlogic.gdx.Game;
import com.ray.twone.screen.GameScreen;

public class Main extends Game{
    @Override
    public void create(){
        setScreen(new GameScreen());
    }
}