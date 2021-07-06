package com.example.stickzinho;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int totalSticksInHands;
    int gameControl = 0;
    EditText input;

    Hand player;
    Hand bot;

    Random random;

    TextView playerInfo;
    TextView botInfo;

    TextView mainQuestion;
    TextView playerSticksInfo;
    TextView botSticksInfo;

    Button guessBtn;
    Button addInHandBtn;
    Button startGame;

    ImageView playerOpenHandImg;
    ImageView playerCloseHandImg;
    ImageView stick01;
    ImageView stick02;
    ImageView stick03;

    ImageView botOpenHandImg;
    ImageView botCloseHandImg;
    ImageView botStick01;
    ImageView botStick02;
    ImageView botStick03;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        player = new Hand();
        bot = new Hand();

        //texts
        input = findViewById(R.id.putSticks);
        playerInfo = findViewById(R.id.playerInfo);

        botInfo = findViewById(R.id.botInfo);
        mainQuestion = findViewById(R.id.description);

        playerSticksInfo = findViewById(R.id.playerSticksInfo);
        botSticksInfo = findViewById(R.id.botSticksInfo);
        playerSticksInfo.setText("Palitos: " + player.getStick());
        botSticksInfo.setText("Palitos: " + bot.getStick());

        //buttons
        guessBtn = findViewById(R.id.guess);
        addInHandBtn = findViewById(R.id.addInHand);
        startGame = findViewById(R.id.startGame);

        //images
        playerOpenHandImg = findViewById(R.id.playerOpenHand);
        playerCloseHandImg = findViewById(R.id.playerCloseHand);
        stick01 = findViewById(R.id.stick01);
        stick02 = findViewById(R.id.stick02);
        stick03 = findViewById(R.id.stick03);

        botOpenHandImg = findViewById(R.id.botOpenHand);
        botCloseHandImg = findViewById(R.id.botCloseHand);
        botStick01 = findViewById(R.id.botStick01);
        botStick02 = findViewById(R.id.botStick02);
        botStick03 = findViewById(R.id.botStick03);
    }

    public void roundControl(View view) {
        switch (gameControl){
            case 0:
                startGame.setVisibility(View.GONE);
                mainQuestion.setText(R.string.question);
                input.setVisibility(View.VISIBLE);
                addInHandBtn.setVisibility(View.VISIBLE);
                gameControl = 1;
                break;
            case 1:
                resetRound();
                break;
        }
    }

    public void nextRound(){
        input.setVisibility(View.INVISIBLE);
        guessBtn.setVisibility(View.INVISIBLE);
        mainQuestion.setText(R.string.question4);
        startGame.setText(R.string.next);
        startGame.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void addSticksInHand(View view) {
        player.setStickInHand(Integer.parseInt(input.getText().toString()));
        bot.setStickInHand(random.nextInt(bot.getStick() + 1));

        addInHandBtn.setVisibility(View.INVISIBLE);
        guessBtn.setVisibility(View.VISIBLE);
        mainQuestion.setText(R.string.question2);

        int maxRangeInput = bot.getStick() + player.getStickInHand();

        input.setHint("De " + player.getStickInHand() + " a " + maxRangeInput);
        input.setText("");

        input.setKeyListener(
                DigitsKeyListener.getInstance(
                        inputRange(player.getStickInHand(), maxRangeInput)
                )
        );

    }

    public void makeGuess(View view) {
        player.setGuess(Integer.parseInt(input.getText().toString()));

        int min = bot.getStickInHand();
        int max = bot.getStickInHand() + player.getStick();
        bot.setGuess((int) ((Math.random() * (max - min)) + min));

        while(bot.getGuess() == player.getGuess()){
            bot.setGuess((int) ((Math.random() * (max - min)) + min));
        }

        openHand();
        checkRoundWinner();
    }

    @SuppressLint("SetTextI18n")
    public void checkRoundWinner() {
        setTotalSticksInBothHands();

        if (player.getGuess() == totalSticksInHands){
            player.setStick(player.getStick() - 1);
            playerSticksInfo.setText("Palitos: " + player.getStick());
            playerInfo.setText(R.string.winner);
            botInfo.setText(R.string.loser);
        }else if (bot.getGuess() == totalSticksInHands){
            bot.setStick(bot.getStick() -1);
            botSticksInfo.setText("Palitos: " + bot.getStick());
            botInfo.setText(R.string.winner);
            playerInfo.setText(R.string.loser);
        }else {
            botInfo.setText(R.string.even);
            playerInfo.setText(R.string.even);
        }

        checkGameWinner();
    }

    @SuppressLint("SetTextI18n")
    public void checkGameWinner () {
        if(player.getStick() == 0){
            playerInfo.setText(R.string.gameWinner);
            botInfo.setText(R.string.gameLoser);
            resetGame(1);
        }else if (bot.getStick() == 0){
            botInfo.setText(R.string.gameWinner);
            playerInfo.setText(R.string.gameLoser);
            resetGame(2);
        }else {
            nextRound();
        }
    }

    public void setTotalSticksInBothHands(){
        totalSticksInHands = player.getStickInHand() + bot.getStickInHand();
    }

    public void resetRound(){
        startGame.setVisibility(View.GONE);
        addInHandBtn.setVisibility(View.VISIBLE);
        mainQuestion.setText(R.string.question);
        input.setHint("De 0 a " + player.getStick());
        input.setText("");
        input.setVisibility(View.VISIBLE);

        input.setKeyListener(
                DigitsKeyListener.getInstance(
                        inputRange(0, player.getStick())
                )
        );
        closeHand();
    }


    @SuppressLint("SetTextI18n")
    public void resetGame (int x) {
        player.setStick(3);
        player.setGuess(0);
        player.setStickInHand(0);

        bot.setStick(3);
        bot.setGuess(0);
        bot.setStickInHand(0);

        closeHand();

        playerSticksInfo.setText("Palitos: " + player.getStick());
        botSticksInfo.setText("Palitos: " + bot.getStick());

        addInHandBtn.setVisibility(View.INVISIBLE);
        guessBtn.setVisibility(View.INVISIBLE);

        input.setVisibility(View.INVISIBLE);
        input.setText("");
        input.setHint(R.string.quantity);
        input.setKeyListener(
                DigitsKeyListener.getInstance(
                        inputRange(0, player.getStick())
                )
        );

        botInfo.setText("");
        playerInfo.setText("");

        if (x == 1){
            mainQuestion.setText(R.string.gameWinner);

        }else{
            mainQuestion.setText(R.string.gameLoser);
        }

        startGame.setText(R.string.restart);
        startGame.setVisibility(View.VISIBLE);
        gameControl = 0;
    }

    public String inputRange(int start, int end){
        StringBuilder range = new StringBuilder();

        for (int i = start; i <= end; i++){
            range.append(i);
        }

        return range.toString();
    }

    public void openHand() {
        playerCloseHandImg.setVisibility(View.INVISIBLE);
        playerOpenHandImg.setVisibility(View.VISIBLE);

        switch (player.getStickInHand()){
            case 0:
                stick01.setVisibility(View.INVISIBLE);
                stick02.setVisibility(View.INVISIBLE);
                stick03.setVisibility(View.INVISIBLE);
                break;
            case 1:
                stick01.setVisibility(View.VISIBLE);
                stick02.setVisibility(View.INVISIBLE);
                stick03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                stick01.setVisibility(View.VISIBLE);
                stick02.setVisibility(View.VISIBLE);
                stick03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                stick01.setVisibility(View.VISIBLE);
                stick02.setVisibility(View.VISIBLE);
                stick03.setVisibility(View.VISIBLE);
                break;
        }

        botCloseHandImg.setVisibility(View.INVISIBLE);
        botOpenHandImg.setVisibility(View.VISIBLE);

        switch (bot.getStickInHand()){
            case 0:
                botStick01.setVisibility(View.INVISIBLE);
                botStick02.setVisibility(View.INVISIBLE);
                botStick03.setVisibility(View.INVISIBLE);
                break;
            case 1:
                botStick01.setVisibility(View.VISIBLE);
                botStick02.setVisibility(View.INVISIBLE);
                botStick03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                botStick01.setVisibility(View.VISIBLE);
                botStick02.setVisibility(View.VISIBLE);
                botStick03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                botStick01.setVisibility(View.VISIBLE);
                botStick02.setVisibility(View.VISIBLE);
                botStick03.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void closeHand() {
        stick01.setVisibility(View.INVISIBLE);
        stick02.setVisibility(View.INVISIBLE);
        stick03.setVisibility(View.INVISIBLE);
        playerOpenHandImg.setVisibility(View.INVISIBLE);
        playerCloseHandImg.setVisibility(View.VISIBLE);

        botStick01.setVisibility(View.INVISIBLE);
        botStick02.setVisibility(View.INVISIBLE);
        botStick03.setVisibility(View.INVISIBLE);
        botOpenHandImg.setVisibility(View.INVISIBLE);
        botCloseHandImg.setVisibility(View.VISIBLE);
    }
}