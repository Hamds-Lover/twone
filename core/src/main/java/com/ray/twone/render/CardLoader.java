package com.ray.twone.render;

import com.ray.twone.game.*;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class CardLoader{
    // Card assests
    private HashMap<String, Texture> cardTextures;
    private Texture cardBack = new Texture("cardsTPerson/28_kerenel_Cards.png");

    public HashMap<String, Texture> loadAssets(){
    	// Load all card textures
        cardTextures = new HashMap<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                int num = Card.getFileNumber(suit, rank);
                String filename = "cardsTPerson/" + String.format("%02d_kerenel_Cards.png", num);
                cardTextures.put(suit.toString() + rank.toString(), new Texture(filename));
            }
        }
        return cardTextures;
    }

    public Texture getCardBack(){
    	return cardBack;
    }
}