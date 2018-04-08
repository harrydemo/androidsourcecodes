package com.lequ.Blackjack;

public class CardPlayer {
	boolean isbanker = false;
	String playerName ;
	int playerScore;
	private int[] mPlayerCards;     //save player's cards
	int userId;//specification Players,now have two players
	public boolean isIsbanker() {
		return isbanker;
	}
	public void setIsbanker(boolean isbanker) {
		this.isbanker = isbanker;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getPlayerScore() {
		return playerScore;
	}
	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}
	public int[] getmPlayerCards() {
		return mPlayerCards;
	}
	public void setmPlayerCards(int[] mPlayerCards) {
		this.mPlayerCards = mPlayerCards;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
