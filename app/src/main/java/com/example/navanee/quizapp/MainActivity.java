package com.example.navanee.quizapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetQuestionsAsync.AccessQuestions {
    ImageView logoImage;
    Button exitButton;
    Button startButton;
    ArrayList<Question> quizQuestions = new ArrayList<Question>();
    ImageView imageview;
    ProgressBar logoProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        setAllViews();
        if(networkInfo == null || !networkInfo.isConnected())
        {
            Toast.makeText(this, getString(R.string.toast_has_no_internet), Toast.LENGTH_SHORT).show();
            imageview.setVisibility(View.VISIBLE);
            logoProgress.setVisibility(View.GONE);
            imageview.setImageResource(R.drawable.trivia);
            ((TextView) findViewById(R.id.loadingLabel)).setText(R.string.trivia_unAvailable);
            return;
        }
        new GetQuestionsAsync(this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
    }

    public void setAllViews() {
        logoImage = (ImageView) findViewById(R.id.logoImage);
        exitButton = (Button) findViewById(R.id.buttonExit);
        startButton = (Button) findViewById(R.id.buttonStart);
        exitButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        startButton.setEnabled(false);
        logoProgress = (ProgressBar) findViewById(R.id.loadingProgressBar);
        imageview = (ImageView)findViewById(R.id.logoImage);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonExit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if(v.getId() == R.id.buttonStart) {
            Intent intent = new Intent(this,ViewQuestion.class);
            intent.putExtra("quizQuesList", quizQuestions);
            startActivity(intent);
        }
    }

    @Override
    public void enableToMoveForward(ArrayList<Question> qList) {
        imageview.setVisibility(View.VISIBLE);
        logoProgress.setVisibility(View.GONE);
        imageview.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        ((TextView) findViewById(R.id.loadingLabel)).setText(R.string.trivia_ready);
        quizQuestions = qList;
        startButton.setEnabled(true);
    }
}
