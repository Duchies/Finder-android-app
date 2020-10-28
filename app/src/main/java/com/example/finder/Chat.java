package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Chat extends AppCompatActivity {

    Constants constants;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        initCometChat();
        initViews();
    }

    private void initViews() {
        EditText userText = findViewById(R.id.UserIdText);
        Button SubmitBtn = findViewById(R.id.submit_button);

        SubmitBtn.setOnClickListener(view -> {

            String UID = "SUPERHERO1"; // Replace with the UID of the user to login
            String authKey = constants.appAuthKey; // Replace with your App Auth Key

//
                CometChat.login(UID, authKey, new CometChat.CallbackListener<User>() {

                    @Override
                    public void onSuccess(User user) {
                        String msg = userText.getText().toString();
                        redirectToGroupChatClass(msg);
                     //   Log.d(TAG, "Login Successful : " + user.toString());
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(Chat.this, "somthing wrong !", Toast.LENGTH_SHORT).show();
                     //   Log.d(TAG, "Login failed with exception: " + e.getMessage());
                    }
                });

        });


    }

    private void redirectToGroupChatClass(String msg) {

     Intent i = new Intent(Chat.this,GroupChat.class);
     i.putExtra("msg",msg);
     startActivity(i);
     finish();

    }

    private void initCometChat() {
        String appID = constants.appID; // Replace with your App ID
        String region = "us"; // Replace with your App Region ("eu" or "us")

        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();

        CometChat.init(this, appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {


                Log.d("Chat class", "Initialization completed successfully");
            }
            @Override
            public void onError(CometChatException e) {

                Log.d("Chat Class", "Initialization failed with exception: " + e.getMessage());
            }
        });

    }
}