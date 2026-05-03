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
    private HashMap<String, Texture> cardTextures;
    private Texture cardBack;
    private Deck deck;
    private boolean testDone = false;   // so we print only once

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load textures (same as before)
        cardTextures = new HashMap<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                int num = Card.getFileNumber(suit, rank);
                String filename = "cardsTPerson/" + String.format("%02d_kerenel_Cards.png", num);
                cardTextures.put(suit.toString() + rank.toString(), new Texture(filename));
            }
        }
        cardBack = new Texture("cardsTPerson/28_kerenel_Cards.png");

        deck = new Deck(cardTextures);
    }

    @Override
    public void render() {
        // Run the test exactly once
        if (!testDone) {
            testDone = true;
            Hand hand = new Hand();
            hand.addCard(deck.deal());
            hand.addCard(deck.deal());
            System.out.println("Hand: " + hand);
            System.out.println("Value: " + hand.getValue());
            System.out.println("Blackjack: " + hand.isBlackjack());
        }

        // Green table background
        ScreenUtils.clear(0f, 0.5f, 0f, 1f);
        batch.begin();
        // (optional: draw the cards to see them)
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture tex : cardTextures.values()) tex.dispose();
        cardBack.dispose();
    }
}