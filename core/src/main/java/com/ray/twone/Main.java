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
    private BlackjackGame game;
    private boolean testDone = false;

    @Override
    public void create() {
        batch = new SpriteBatch();

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
        game = new BlackjackGame(deck);
    }

    @Override
    public void render() {
        if (!testDone) {
            testDone = true;
            // Simulate a few actions to test
            System.out.println("Player hand: " + game.getPlayerHand());
            System.out.println("Dealer visible hand (after hidden): " + game.getDealerHand().getVisibleTotal());
            game.playerHit();
            System.out.println("After hit, player: " + game.getPlayerHand() + " value=" + game.getPlayerHand().getValue());
            game.playerStand();
            System.out.println("After stand, result: " + game.getResultMessage());
        }

        ScreenUtils.clear(0f, 0.5f, 0f, 1f);
        batch.begin();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture t : cardTextures.values()) t.dispose();
        cardBack.dispose();
    }
}