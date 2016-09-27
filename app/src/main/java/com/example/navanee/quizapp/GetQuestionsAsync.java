package com.example.navanee.quizapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by navanee on 25-09-2016.
 */
public class GetQuestionsAsync extends AsyncTask<String,Void,ArrayList<Question>> {
    AccessQuestions activity;
    public GetQuestionsAsync(AccessQuestions activity) {
        this.activity = activity;
    }
    ArrayList<Question> questionsList = new ArrayList<Question>();

    @Override
    protected void onPostExecute(ArrayList<Question> questions) {
        activity.enableToMoveForward(questionsList);
        super.onPostExecute(questions);
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\n");
                }
                JSONObject root = new JSONObject(sb.toString());
                JSONArray questionsJSONArray = root.getJSONArray("questions");
                for(int i=0; i < questionsJSONArray.length(); i++) {
                    JSONObject questionJSONObject = questionsJSONArray.getJSONObject(i);
                    Question que = new Question();
                    que.setId(questionJSONObject.getString("id"));
                    que.setText(questionJSONObject.getString("text"));
                    if(questionJSONObject.has("image"))
                        que.setImage(questionJSONObject.getString("image"));
                    JSONObject choices = questionJSONObject.getJSONObject("choices");
                    JSONArray choice = choices.getJSONArray("choice");
                    String[] choicesArray = choice.toString().substring(1,(choice.toString()).length()-1).replace("},{", ",").split(",");
                    for(int j = 0; j < choicesArray.length; j++) {
                        choicesArray[j] = choicesArray[j].substring(1,choicesArray[j].length()-1);
                    }
                    que.setChoices(choicesArray);
                    que.setAnswer(choices.getString("answer"));
                    questionsList.add(que);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questionsList;
    }
    static public interface AccessQuestions {
        void enableToMoveForward(ArrayList<Question> qList);
    }
}


