package com.ray.twone.screen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.ray.twone.game.*;
import com.badlogic.gdx.ScreenAdapter;
import com.ray.twone.render.CardRenderer;
import com.ray.twone.render.CardLoader;


import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    // Card assests
    private HashMap<String, Texture> cardTextures;
    private Texture cardBack;

    // Game logic
    private Deck deck;
    private BlackjackGame game;

    // UI buttons
    private Rectangle hitButton;
    private Rectangle standButton;
    private Rectangle newGameButton;

    // Btn display constants
    private static final float BTN_WIDTH = 80f;
    private static final float BTN_HEIGHT = 40f;
    private static final float BTN_MARGIN = 10f;

    // game font
    private BitmapFont font;

    // Viewport and camera
    private Viewport viewport;
    private OrthographicCamera camera;

    private static final float WORLD_WIDTH = 1280f;
    private static final float WORLD_HEIGHT = 720f;

    // Card renderer and loader
    private CardRenderer cardRenderer;
    private CardLoader cardLoader;

    // Card display constants
    private static final float CARD_HEIGHT = 140f;
    private static final float CARD_SPACING = 10f;

    @Override
    public void show() {
        //cam and viewport stuff
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(WORLD_WIDTH/2f, WORLD_HEIGHT/2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2);

        // Projection matrix?
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Card asset loading
        cardLoader = new CardLoader();
        cardBack = cardLoader.getCardBack();
        cardTextures = cardLoader.loadAssets();
        // init card renderer
        cardRenderer = new CardRenderer(batch, WORLD_WIDTH, cardBack);

        // Create the deck and the game (game starts automatically)
        deck = new Deck(cardTextures);
        game = new BlackjackGame(deck);

        // Create the buttons (should appear in the bottom right)
        hitButton = new Rectangle(WORLD_WIDTH - 2 * (BTN_WIDTH + BTN_MARGIN), BTN_MARGIN, BTN_WIDTH, BTN_HEIGHT);
        standButton = new Rectangle(WORLD_WIDTH - (BTN_WIDTH + BTN_MARGIN), BTN_MARGIN, BTN_WIDTH, BTN_HEIGHT);
        newGameButton = new Rectangle(WORLD_WIDTH/2f - BTN_WIDTH / 2f, WORLD_HEIGHT/2f - 70f, BTN_WIDTH, BTN_HEIGHT);
    }

    @Override
    public void render(float delta) {
        // clear the screen
        ScreenUtils.clear(0f, 0.5f, 0f, 1f);

        // btn touch detection
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            float touchX = touchPos.x;
            float touchY = touchPos.y;

            if (!game.isGameOver() && game.isPlayerTurn()) {
                if (hitButton.contains(touchX, touchY)) {
                    game.playerHit();
                    if (game.isGameOver()) {
                        System.out.println(game.getResultMessage());
                    }
                } else if (standButton.contains(touchX, touchY)) {
                    game.playerStand();
                    System.out.println(game.getResultMessage());
                }
            } else if (game.isGameOver() && newGameButton.contains(touchX, touchY)) {
                game.startNewGame();
            }
        }

        camera.update();

        // Draw the cards
        batch.begin();
        // Dealer hand (at the top)
        float dealerY = WORLD_HEIGHT - CARD_HEIGHT - 40f;
        cardRenderer.drawHand(game.getDealerHand(), dealerY, true, game.isDealerTurn());

        // Player hand (bottom)
        float playerY = 40f;
        cardRenderer.drawHand(game.getPlayerHand(), playerY, false, game.isDealerTurn());
        
        // Message result drawing
        if(game.isGameOver()){
            String msg = game.getResultMessage();
            // center the text
            //float textWidth = font.getRegion().getRegionWidth() * font.getData().scaleX;

            GlyphLayout layout = new GlyphLayout(font, msg);
            float textX = (WORLD_WIDTH- layout.width) / 2f;
            float textY = (WORLD_HEIGHT +layout.height) / 2f;
            font.draw(batch, msg, textX, textY);

        }

        // End of texture batch
        batch.end();

        // Draw the btns
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Hit btn - light blue
        shapeRenderer.setColor(0.2f, 0.6f, 1f, 1f);
        shapeRenderer.rect(hitButton.x, hitButton.y, hitButton.width, hitButton.height);
            // Stand btn - orange
        shapeRenderer.setColor(1f, 0.6f, 0.2f, 1f);
        shapeRenderer.rect(standButton.x, standButton.y, standButton.width, standButton.height);
            // New game btn
        if(game.isGameOver()){
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 1f);
            shapeRenderer.rect(newGameButton.x, newGameButton.y, newGameButton.width, newGameButton.height);
        }
        // End of shape rendering
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        for (Texture t : cardTextures.values()) t.dispose();
        cardBack.dispose();
        font.dispose();
    }
}