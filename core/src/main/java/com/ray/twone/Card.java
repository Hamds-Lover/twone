package com.ray.twone;

import com.badlogic.gdx.graphics.Texture;

public class Card {
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Rank { ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }

    public final Suit suit;
    public final Rank rank;
    public Texture texture;   // we'll set this after loading the image

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /** Returns the Blackjack value: Ace returns 11 (we'll adjust soft hands later) */
    public int value() {
        switch (rank) {
            case ACE:   return 11;
            case TWO:   return 2;
            case THREE: return 3;
            case FOUR:  return 4;
            case FIVE:  return 5;
            case SIX:   return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE:  return 9;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:  return 10;
            default: return 0; // never happens
        }
    }

    public String toString() {
        return rank + " of " + suit;
    }

    
    public static int getFileNumber(Suit suit, Rank rank) {
    int suitOffset;
    switch (suit) {
        case HEARTS:   suitOffset = 0; break;
        case SPADES:   suitOffset = 14; break;
        case DIAMONDS: suitOffset = 28; break;
        case CLUBS:    suitOffset = 42; break;
        default: suitOffset = 0;
    }
    // rank ordinal: ACE=0, TWO=1, ..., KING=12. So we add 1
    int rankNumber = rank.ordinal() + 1;
    return suitOffset + rankNumber;
}
}