package com.ray.twone;

public class GameEvaluator{
	public GameResult evaluate(Hand playerHand, Hand dealerHand){
		int playerVal = playerHand.getValue();
		int dealerVal = dealerHand.getValue();

		// Bust checks
		if(playerVal > 21) return GameResult.PLAYER_BUST;
		if(dealerVal > 21) return GameResult.CPU_BUST;

		// Natural blackjack check
		boolean playerBJ = playerHand.isBlackjack();
		boolean dealerBJ = dealerHand.isBlackjack();

		if(playerBJ && dealerBJ) return GameResult.TIE;
		if(playerBJ) return GameResult.PLAYER_WIN;
		if(dealerBJ) return GameResult.CPU_WIN;

		// Regular checks
		if(playerVal > dealerVal) return GameResult.PLAYER_WIN;
		if(dealerVal > playerVal) return GameResult.CPU_WIN;
		return GameResult.TIE; // if no other checks trigger then surely it is a tie.
	}
}