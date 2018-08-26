package com.intimetec.firestoredemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreActivity extends AppCompatActivity {

    private static final String TAG = FirestoreActivity.class.getName();
    private FirebaseFirestore db;

    private Button addQuestionsBtn;

    private Button getDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        addQuestionsBtn = findViewById(R.id.addDataBtn);

        getDataBtn = findViewById(R.id.getDataBtn);

        addQuestionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionToFirestore();
            }
        });

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }


    private Map<String, Question> getQuestions() {
        Map<String, Question> questions = new HashMap<>();

        Question question = new Question();
        question.setQuestion("Who is the Prime Minister of India?");
        question.setOption1("Atal Bihari Vajpayee");
        question.setOption2("Manmohan Singh");
        question.setOption3("Narendra Modi");
        question.setOption4("I K Gujral");
        question.setCorrectAns("Narendra Modi");

        questions.put("1", question);

        question = new Question();
        question.setQuestion("Who is the President of India?");
        question.setOption1("Abdul Kalam");
        question.setOption2("Ramnath Kovind");
        question.setOption3("Pranav Mukherjee");
        question.setOption4("Rajendra Prasad");
        question.setCorrectAns("Ramnath Kovind");

        questions.put("2", question);

        question = new Question();
        question.setQuestion("Who is the CEO of Google?");
        question.setOption1("Larry Page");
        question.setOption2("Satya Nadela");
        question.setOption3("Sundar Pichai");
        question.setOption4("Mukesh Ambani");
        question.setCorrectAns("Sundar Pichai");

        questions.put("3", question);

        return questions;
    }


    private void addQuestionToFirestore() {

        Map<String, Question> questions = getQuestions();

        for (int i = 1; i <= questions.size(); i++) {
            String key = String.valueOf(i);
            Question question = questions.get(key);
            db.collection("questions").document(i + "")
                    .set(question, SetOptions.merge());
        }
    }

    private void getData() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Question question = document.toObject(Question.class);
                                if (question != null) {
                                    System.out.println(question.toString());
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
