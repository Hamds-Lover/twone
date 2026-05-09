package com.ray.twone.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.ray.twone.game.*;

public class CardRenderer {

	// Card display constants
    private static final float CARD_HEIGHT = 140f;
    private static final float CARD_SPACING = 10f;

    // extra atts
    float worldWidth;
    Texture cardBack;

    private SpriteBatch batch;

    public CardRenderer(SpriteBatch batch, float worldWidth, Texture cardBack) {
        this.batch = batch;
        this.worldWidth = worldWidth;
        this.cardBack = cardBack;
    }

        // Draw hand function - draws hand horizontally centered at given Y coord
    public void drawHand(Hand hand, float y, boolean isDealer, boolean isDealerTurn){
        if(hand.getCards().isEmpty()) return;

        float totalWidth = 0f;
        // first loop - calc how much hor space is needed for the whole hand
        for(int i=0; i<hand.getCards().size(); i++){
            Texture tex = getCardTexture(hand.getCards().get(i), i, isDealer, isDealerTurn);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            totalWidth += CARD_HEIGHT * aspect; // this is to maintain aspect ratio of card display
        }
        totalWidth += (hand.getCards().size() - 1) * CARD_SPACING;

        float startX = (worldWidth - totalWidth) / 2f;
        float currentX = startX;
        // second loop - draw the hand
        for(int i=0; i<hand.getCards().size(); i++){
            Texture tex = getCardTexture(hand.getCards().get(i), i, isDealer, isDealerTurn);
            float aspect = (float) tex.getWidth() / tex.getHeight();
            float cardWidth = CARD_HEIGHT * aspect;
            batch.draw(tex, currentX, y, cardWidth, CARD_HEIGHT);
            currentX += cardWidth + CARD_SPACING;
        }
    }


    // Function to get the correct card texture
    public Texture getCardTexture(Card card, int index, boolean isDealer, boolean isDealerTurn){

        // returns the card back to hide dealer's hole card
        if(isDealer && index == 0 && !isDealerTurn){
            return cardBack;
        }

        //else return card texture
        return card.texture;
    }
}