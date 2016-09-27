package com.example.navanee.quizapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewQuestion extends AppCompatActivity implements View.OnClickListener, GetImageAsync.ImageInterface {
    TextView questionNumber;
    TextView quizTimerView;
    TextView questionDesc;
    RadioGroup questionChoices;
    Button quitBtn;
    Button nextBtn;
    int qIndex = 0;
    ArrayList<Question> queList = new ArrayList<Question>();
    Question ques;
    int numCorrectAnswers = 0;
    MyCountDownTimer myTestTimer;
    private final long startTime = 120 * 1000;
    private final long interval = 1 * 1000;

    ProgressBar imageLoadingProgBar;
    TextView imageLoadingProgText;
    ImageView questionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        setAllViews();
        loadQuestionDetails(qIndex);
        myTestTimer = new MyCountDownTimer(startTime,interval);
        myTestTimer.start();
    }

    public void setAllViews() {
        questionNumber = (TextView) findViewById(R.id.questionNumber);
        quizTimerView = (TextView) findViewById(R.id.countDownTimer);
        questionDesc = (TextView) findViewById(R.id.questionDescription);
        questionChoices = (RadioGroup) findViewById(R.id.choicesRadioGroup);
        quitBtn = (Button) findViewById(R.id.quitButton);
        nextBtn = (Button) findViewById(R.id.nextButton);
        quitBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        queList = (ArrayList<Question>) getIntent().getExtras().get("quizQuesList");

        imageLoadingProgBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        imageLoadingProgText = (TextView) findViewById(R.id.loadingProgressMessage);
        questionImage = (ImageView) findViewById(R.id.questionImage);
    }

    public void loadQuestionDetails(int questionNum) {
        ques = queList.get(questionNum);
        int qNumber = questionNum + 1;
        questionNumber.setText("Q" + qNumber);
        questionDesc.setText(ques.getText());
        questionChoices.setOrientation(RadioGroup.VERTICAL);
        String[] choices = ques.getChoices();
        RadioButton[] rb = new RadioButton[choices.length];
        questionChoices.removeAllViews();
        for (int i = 0; i < choices.length; i++) {
            rb[i] = new RadioButton(this);
            questionChoices.addView(rb[i]);
            rb[i].setText(choices[i]);
        }
        String imageString = ques.getImage();
        if(imageString != "" && imageString != null ) {
            new GetImageAsync(this).execute(imageString,ques.getId());
        } else {
            imageLoadingProgBar.setVisibility(View.GONE);
            imageLoadingProgText.setVisibility(View.GONE);
            questionImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int correctAnswer = Integer.parseInt(ques.getAnswer()) - 1;
        int index = questionChoices.indexOfChild(findViewById(questionChoices.getCheckedRadioButtonId()));
        if (correctAnswer == index)
            numCorrectAnswers++;
        if (v.getId() == R.id.quitButton) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.nextButton) {
            if (qIndex == queList.size() - 1) {
                myTestTimer.cancel();
                endMyTest();
            } else {
                qIndex++;
                imageLoadingProgBar.setVisibility(View.VISIBLE);
                imageLoadingProgText.setVisibility(View.VISIBLE);
                questionImage.setVisibility(View.GONE);
                loadQuestionDetails(qIndex);
            }
        }
    }

    public void endMyTest() {
        Intent intent = new Intent(this, StatsView.class);
        intent.putExtra("numCorrectAnswers", numCorrectAnswers);
        intent.putExtra("quizQuesList", queList);
        startActivity(intent);
    }

    @Override
    public void setImage(Bitmap bitmap, int qID) {
        if(qID == Integer.parseInt(ques.getId())) {
            imageLoadingProgBar.setVisibility(View.GONE);
            imageLoadingProgText.setVisibility(View.GONE);
            questionImage.setVisibility(View.VISIBLE);
            questionImage.setImageBitmap(bitmap);
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            quizTimerView.setText("Time Left: " + millisUntilFinished / 1000 + " Seconds");
        }

        @Override
        public void onFinish() {
            endMyTest();
        }
    }
}
