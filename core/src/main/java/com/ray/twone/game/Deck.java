package com.ray.twone.game;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Texture;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private HashMap<String, Texture> textureMap;

    public Deck() {
        resetDeck();
    }

    public Deck(HashMap<String, Texture> textureMap) {
        this.textureMap = textureMap;
        resetDeck();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            System.out.println("Deck empty! Resetting...");
            resetDeck();
        }
        return cards.remove(0);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void resetDeck() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                Card card = new Card(suit, rank);
                if (textureMap != null) {
                    String key = suit.toString() + rank.toString();
                    card.texture = textureMap.get(key);
                }
                cards.add(card);
            }
        }
        shuffle();
    }
}