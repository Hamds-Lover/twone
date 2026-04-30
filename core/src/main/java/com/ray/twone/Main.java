package com.ray.twone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    // Coord vars
    public float x;
    public float y;
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        x = 140;
        y = 210;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= 4;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 4;
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) y += 4;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= 4;
        batch.begin();
        batch.draw(image, x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
