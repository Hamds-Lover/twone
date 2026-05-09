package com.ray.twone.game;

public class BlackjackGame{
	private Deck deck;
	private Hand playerHand;
	private Hand dealerHand;
	private CpuDecisionMaker cpu;
	private GameEvaluator evaluator;

	private boolean playerTurn;
	private boolean gameOver;
	private String resultMessage;

	public BlackjackGame(Deck deck){
		this.deck = deck;
		playerHand = new Hand();
		dealerHand = new Hand();
		cpu = new CpuDecisionMaker();
		evaluator = new GameEvaluator();
		startNewGame();
	}

	// Resets the game, deals two cards to each player

	public void startNewGame(){
		// Make sure the deck is reset (full/fresh) and clears both player's hands
		deck.resetDeck();
		playerHand.clear();
		dealerHand.clear();

		// Deal the initial cards to both players
		playerHand.addCard(deck.deal());
		dealerHand.addCard(deck.deal());
		playerHand.addCard(deck.deal());
		dealerHand.addCard(deck.deal());

		playerTurn = true;
		gameOver = false;
		resultMessage = null;
	}

	// Player takes a card (hits)
	public void playerHit(){
		if(!playerTurn || gameOver) return;
		playerHand.addCard(deck.deal());
		// quick bust check I guess
		if(playerHand.getValue() > 21){
			gameOver = true;
			playerTurn = false;
			resultMessage = evaluator.evaluate(playerHand, dealerHand).toString();
		}
	}

	// Player stands
	public void playerStand(){
		if(!playerTurn || gameOver) return;
		playerTurn = false;

		// Cpu turn (which for somereason only happens when the player stands??)
		while(cpu.shouldHit(dealerHand.getValue(), playerHand.getVisibleTotal())){
			dealerHand.addCard(deck.deal());
		}

		gameOver = true;
		resultMessage = evaluator.evaluate(playerHand, dealerHand).toString();
	}

	// Convert resultMessage to friendly text
    private String formatResult(GameResult result) {
        switch (result) {
            case PLAYER_WIN:  return "You win!";
            case CPU_WIN:     return "Dealer wins.";
            case TIE:         return "Push (tie).";
            case PLAYER_BUST: return "Player busts! Dealer wins.";
            case CPU_BUST:    return "Dealer busts! You win.";
            default:          return "Unknown";
        }
    }

    // Getters needed by Main for drawing and state checks
    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public boolean isPlayerTurn() { return playerTurn; }
    public boolean isGameOver() { return gameOver; }
    public String getResultMessage() { return resultMessage; }

    /** Exposes whether the dealer's turn has started (for revealing the hole card). */
    public boolean isDealerTurn() { return !playerTurn || gameOver; }
}