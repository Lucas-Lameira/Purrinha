package com.example.stickzinho;

public class Hand {

    private int stick;
    private int stickInHand;
    private int guess;

    public Hand () {
        this.stick = 3;
        this.stickInHand = 0;
        this.guess = 0;
    }

    public int getStick() {
        return stick;
    }

    public void setStick(int stick) {
        this.stick = stick;
    }

    public int getStickInHand() {
        return stickInHand;
    }

    public void setStickInHand(int stickInHand) {
        this.stickInHand = stickInHand;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }
}
