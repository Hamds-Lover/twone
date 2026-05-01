package com.ray.twone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Texture cardFrst;
    // Coord vars
    public float x;
    public float y;
    // Draw size vars
    private float drawWidth;
    private float drawHeight;
    @Override
    public void create() {
            //this block is for fps limiting/testing.
        //Gdx.graphics.setVSync(true);                // sync with monitor refresh
        //Gdx.graphics.setForegroundFPS(30);          // limit to 60 if you want to test
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        cardFrst = new Texture("./spanish_deck/1.PNG");
        x = 140;
        y = 210;
        drawWidth = 32;
        drawHeight = 32;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 200;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  x -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    y += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  y -= speed * delta;

        x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - cardFrst.getWidth());
        y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - cardFrst.getHeight());

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        batch.draw(cardFrst, x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        cardFrst.dispose();
    }
}
