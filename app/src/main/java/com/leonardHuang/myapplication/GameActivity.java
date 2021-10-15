package com.leonardHuang.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final int MAX_TRIES = 9;
    public static final Random RANDOM = new Random();
    public String wordToGuess;
    private char[] wordGuessed;
    private int nbTries;
    private ArrayList<String> letters = new ArrayList<>();
    private TextView wordToGuessTextView;
    private TextView nbOfTries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        wordToGuessTextView = findViewById(R.id.wordToGuess);
        nbOfTries = findViewById(R.id.nbTriesTW);
        resetGameParameters();
    }

    public void resetGameParameters(){
        nbTries = 0;
        letters.clear();
        wordToGuess = chooseWordToGuess();

        wordGuessed = new char[wordToGuess.length()];

        for(int i = 0; i <wordToGuess.length();i++){
            wordGuessed[i] = '_';
        }

        wordToGuessTextView.setText(wordGuessedState());

    }

    private void gameRules(String letter, Button mButton){
        if(!letters.contains(letter)){

            if(wordToGuess.contains(letter)){
                mButton.setBackgroundColor(Color.GREEN);

                int index = wordToGuess.indexOf(letter);

                while(index>=0){
                    wordGuessed[index] = letter.charAt(0);
                    index = wordToGuess.indexOf(letter,index+1);
                }
            }
            else{
                mButton.setBackgroundColor(Color.RED);
                nbTries++;
                nbOfTries.setText("Number of tries :" + String.valueOf(MAX_TRIES-nbTries+1));
            }

            letters.add(letter);
        }
    }

    private String chooseWordToGuess(){
        String[] WORDS = getResources().getStringArray(R.array.WORDS); ;
        return WORDS[RANDOM.nextInt(WORDS.length)].toUpperCase();
    }

    private String wordGuessedState(){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i<wordGuessed.length;i++){
            builder.append(wordGuessed[i]);

            if(i<wordGuessed.length - 1){
                builder.append(" ");
            }
        }

        return builder.toString();
    }



    public void letterClicked(View view) {
        Button mButton = (Button) findViewById(view.getId());
        if (nbTries<MAX_TRIES) {
            String letter = ((Button) view).getText().toString();
            gameRules(letter,mButton);
            wordToGuessTextView.setText(wordGuessedState());

            if (wordGuessed()) {
                youWonAlert();
            }
        }
        else{
            youLostAlert();
        }
        mButton.setEnabled(false);

    }

    private boolean wordGuessed() {
        return wordToGuess.contentEquals(new String(wordGuessed));
    }

    private void youWonAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
        alert.setMessage("You won !");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        })
        .create();
        alert.show();
    }

    private void youLostAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
        alert.setMessage("You lost !");
        alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        })
                .create();
        alert.show();
    }
}