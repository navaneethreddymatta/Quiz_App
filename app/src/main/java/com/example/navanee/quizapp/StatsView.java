package com.example.navanee.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsView extends AppCompatActivity implements View.OnClickListener{
    TextView percentageDisplay;
    ProgressBar percentageProgress;
    Button quitButton, tryAgainButton;
    int numCorrectAns, numTotalQues, percentageCorrectAns;
    Intent intent;
    ArrayList<Question> queList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_view);
        setAllViews();
        numCorrectAns = getIntent().getExtras().getInt("numCorrectAnswers");
        queList = (ArrayList<Question>) getIntent().getExtras().get("quizQuesList");
        numTotalQues = queList.size();
        percentageCorrectAns = (numCorrectAns * 100) / numTotalQues;
        System.out.println("numCorrectAns: " + numCorrectAns);
        System.out.println("numTotalQues: " + numTotalQues);
        System.out.println("percentageCorrectAns: " + percentageCorrectAns);
        percentageDisplay.setText(percentageCorrectAns + "%");
        percentageProgress.setProgress(percentageCorrectAns);
        quitButton.setOnClickListener(this);
        tryAgainButton.setOnClickListener(this);
    }

    public void setAllViews(){
        percentageDisplay = (TextView) findViewById(R.id.correctAnsPerc);
        percentageProgress = (ProgressBar) findViewById(R.id.statsPerc);
        quitButton = (Button) findViewById(R.id.quitQuiz);
        tryAgainButton = (Button) findViewById(R.id.quizTryAgain);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.quitQuiz) {
            intent = new Intent(this, MainActivity.class);
        } else if (v.getId() == R.id.quizTryAgain) {
            intent = new Intent(this, ViewQuestion.class);
            intent.putExtra("quizQuesList",queList);
        }
        startActivity(intent);
    }
}
