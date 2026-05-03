package com.ray.twone;

import java.util.Collections;
import java.util.ArrayList;

public class Hand{
	private ArrayList<Card> cards = new ArrayList<>(); // card storing obviously.


	// method to add card to hand
	public void addCard(Card card){
		cards.add(card);
	}


	// method for hand val and ace bust handling
	public int getValue(){
		int sum = 0;
		int aces = 0;

		for(Card c : cards){
			sum += c.value();
			if(c.rank == Card.Rank.ACE){
				aces++;
			}
		}

		// to avoid aces making u bust
		while(sum>21 && aces>0){
			sum-=10;
			aces--;
		}

		return sum;
	}

	// dealer hand sum (sum without the hole card)
	public int getVisibleTotal(){
		if(cards.size() <= 1) return 0;

		int sum = 0;
		int aces = 0;

		for(int i = 1; i < cards.size(); i++){
			Card c = cards.get(i);
			sum += c.value();

			// Ace bust handling: ace incrementation
			if(c.rank == Card.Rank.ACE){
				aces++;
			}
			// ace conversion from 11 to 1
			while(sum > 21 && aces > 0){
				sum -=10;
				aces--;
			}
		}
		return sum;
	}

	// Natural blackjac check (when hand has only 2 cards and equal to 21)

	public boolean isBlackjack(){
		return cards.size() == 2 && getValue() == 21;
	}

	// Empty the hand

	public void clear(){
		cards.clear();
	}

	/** Needed for drawing: access each card. */
    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}