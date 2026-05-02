package com.ray.twone;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    // Card assets
    private HashMap<String, Texture> cardTextures;
    private Texture cardBack;

    // Game objects
    private Deck deck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private boolean dealerTurn;       // true after player stands
    private boolean gameOver;

    // UI buttons
    private Rectangle hitButton;
    private Rectangle standButton;

    // Card display constants
    private static final float CARD_HEIGHT = 140f;
    private static final float CARD_SPACING = 10f;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Load all card textures
        cardTextures = new HashMap<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                int num = Card.getFileNumber(suit, rank);
                String filename = "cardsTPerson/" + String.format("%02d_kerenel_Cards.png", num);
                cardTextures.put(suit.toString() + rank.toString(), new Texture(filename));
            }
        }
        cardBack = new Texture("cardsTPerson/28_kerenel_Cards.png"); // red back

        // Create shuffled deck and hands
        deck = new Deck(cardTextures);
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        dealerTurn = false;
        gameOver = false;

        // Deal initial two cards each
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());

        // Create buttons (bottom-right corner)
        float buttonWidth = 80f, buttonHeight = 40f;
        float margin = 10f;
        hitButton = new Rectangle(
            Gdx.graphics.getWidth() - 2 * (buttonWidth + margin),
            margin,
            buttonWidth,
            buttonHeight
        );
        standButton = new Rectangle(
            Gdx.graphics.getWidth() - (buttonWidth + margin),
            margin,
            buttonWidth,
            buttonHeight
        );
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        // Input handling
        if (!gameOver && !dealerTurn) {
            if (Gdx.input.justTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // flip Y

                if (hitButton.contains(touchX, touchY)) {
                    playerHand.add(deck.deal());
                    if (handValue(playerHand) > 21) {
                        gameOver = true;
                        System.out.println("Player busts! Dealer wins.");
                    }
                }
                if (standButton.contains(touchX, touchY)) {
                    dealerTurn = true;
                }
            }
        }

        // Dealer's automatic play after player stands
        if (dealerTurn && !gameOver) {
            while (handValue(dealerHand) < 17) {
                dealerHand.add(deck.deal());
            }
            gameOver = true;

            int playerScore = handValue(playerHand);
            int dealerScore = handValue(dealerHand);
            if (dealerScore > 21 || playerScore > dealerScore) {
                System.out.println("Player wins!");
            } else if (dealerScore == playerScore) {
                System.out.println("Push (tie).");
            } else {
                System.out.println("Dealer wins.");
            }
        }

        // Clear screen with green background
        ScreenUtils.clear(0f, 0.5f, 0f, 1f);

        batch.begin();

        // Draw dealer hand
        float dealerY = Gdx.graphics.getHeight() - CARD_HEIGHT - 40f;
        drawHand(dealerHand, dealerY, true); // dealer's second card hidden until dealerTurn

        // Draw player hand
        float playerY = 40f;
        drawHand(playerHand, playerY, false); // all cards face up

        batch.end();
    }

    /** Draws a hand of cards horizontally centered. */
    private void drawHand(ArrayList<Card> hand, float y, boolean isDealer) {
        if (hand.isEmpty()) return;

        // Calculate total width
        float totalWidth = 0f;
        for (int i = 0; i < hand.size(); i++) {
            Texture tex = getCardTexture(hand.get(i), i, isDealer);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            float cardWidth = CARD_HEIGHT * aspect;
            totalWidth += cardWidth;
        }
        totalWidth += (hand.size() - 1) * CARD_SPACING;

        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float currentX = startX;

        for (int i = 0; i < hand.size(); i++) {
            Texture tex = getCardTexture(hand.get(i), i, isDealer);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            float cardWidth = CARD_HEIGHT * aspect;
            batch.draw(tex, currentX, y, cardWidth, CARD_HEIGHT);
            currentX += cardWidth + CARD_SPACING;
        }
    }

    /** Returns the correct texture for a card in a hand.
     *  Dealer's second card (index 1) is hidden until dealerTurn is true. */
    private Texture getCardTexture(Card card, int index, boolean isDealer) {
        if (isDealer && index == 1 && !dealerTurn && !gameOver) {
            return cardBack; // hole card
        }
        return card.texture;
    }

    /** Calculates the Blackjack value of a hand (adjusting aces). */
    private int handValue(ArrayList<Card> hand) {
        int sum = 0;
        int aces = 0;
        for (Card c : hand) {
            sum += c.value();
            if (c.rank == Card.Rank.ACE) aces++;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10; // change an ace from 11 to 1
            aces--;
        }
        return sum;
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture tex : cardTextures.values()) {
            tex.dispose();
        }
        shapeRenderer.dispose();
        cardBack.dispose();
    }
    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height));
        // If you use ShapeRenderer, update its projection too
        shapeRenderer.setProjectionMatrix(shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height));
    }
}