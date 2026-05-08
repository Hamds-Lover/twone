package com.ray.twone;

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

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends ApplicationAdapter {
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

    // Card display constants
    private static final float CARD_HEIGHT = 140f;
    private static final float CARD_SPACING = 10f;

    // Btn display constants
    private static final float BTN_WIDTH = 80f;
    private static final float BTN_HEIGHT = 40f;
    private static final float BTN_MARGIN = 10f;

    // game font
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2);

        // Load all card textures
        cardTextures = new HashMap<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                int num = Card.getFileNumber(suit, rank);
                String filename = "cardsTPerson/" + String.format("%02d_kerenel_Cards.png", num);
                cardTextures.put(suit.toString() + rank.toString(), new Texture(filename));
            }
        }
        cardBack = new Texture("cardsTPerson/28_kerenel_Cards.png");

        // Create the deck and the game (game starts automatically)
        deck = new Deck(cardTextures);
        game = new BlackjackGame(deck);

        // Create the buttons (should appear in the bottom right)
        float btnWidth = 80f, btnHeight = 40f;
        float margin = 10f;

        hitButton = new Rectangle(Gdx.graphics.getWidth() - 2 * (btnWidth + margin), margin, btnWidth, btnHeight);
        standButton = new Rectangle(Gdx.graphics.getWidth() - (btnWidth + margin), margin, btnWidth, btnHeight);
        newGameButton = new Rectangle(Gdx.graphics.getWidth() / 2f - 40f, Gdx.graphics.getHeight() / 2f - 70f, 80, 40);
        // Set initial projection
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        // clear the screen
        ScreenUtils.clear(0f, 0.5f, 0f, 1f);

        // btn touch detection

        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

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

        // Draw the cards
        batch.begin();

        // Dealer hand (at the top)
        float dealerY = Gdx.graphics.getHeight() - CARD_HEIGHT - 40f;
        drawHand(game.getDealerHand(), dealerY, true);

        // Player hand (bottom)
        float playerY = 40f;
        drawHand(game.getPlayerHand(), playerY, false);
        
        // Message result drawing
        if(game.isGameOver()){
            String msg = game.getResultMessage();
            // center the text
            //float textWidth = font.getRegion().getRegionWidth() * font.getData().scaleX;

            GlyphLayout layout = new GlyphLayout(font, msg);
            float textX = (Gdx.graphics.getWidth()- layout.width) / 2f;
            float textY = (Gdx.graphics.getHeight()- 2f+layout.height) / 2f;
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

    // Draw hand function - draws hand horizontally centered at given Y coord
    private void drawHand(Hand hand, float y, boolean isDealer){
        if(hand.getCards().isEmpty()) return;

        float totalWidth = 0f;
        // first loop - calc how much hor space is needed for the whole hand
        for(int i=0; i<hand.getCards().size(); i++){
            Texture tex = getCardTexture(hand.getCards().get(i), i, isDealer);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            totalWidth += CARD_HEIGHT * aspect; // this is to maintain aspect ratio of card display
        }
        totalWidth += (hand.getCards().size() - 1) * CARD_SPACING;

        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float currentX = startX;
        // second loop - draw the hand
        for(int i=0; i<hand.getCards().size(); i++){
            Texture tex = getCardTexture(hand.getCards().get(i), i, isDealer);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            float cardWidth = CARD_HEIGHT * aspect;
            batch.draw(tex, currentX, y, cardWidth, CARD_HEIGHT);
            currentX += cardWidth + CARD_SPACING;
        }
    }

    // Function to get the correct card texture
    private Texture getCardTexture(Card card, int index, boolean isDealer){

        // returns the card back to hide dealer's hole card
        if(isDealer && index == 0 && !game.isDealerTurn()){
            return cardBack;
        }

        //else return card texture
        return card.texture;
    }

    @Override
    public void resize(int width, int height){
        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height));
        shapeRenderer.setProjectionMatrix(shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height));
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