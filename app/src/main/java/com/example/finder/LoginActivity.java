package com.example.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private Button buttonLogin;


    private String SETERROR = "Field can't be empty";
    private String TAG = "Login Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();

        textInputEmail = findViewById(R.id.textInput_Email);
        textInputPassword = findViewById(R.id.textInput_Password);

        buttonLogin = findViewById(R.id.btn_login);




        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
            }
        });



    }

    public void viewRegisterClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
        startActivity(intent);
    }


    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError(SETERROR);
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError(SETERROR);
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void confirmInput() {
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        String email = textInputEmail.getEditText().getText().toString();

        String password = textInputPassword.getEditText().getText().toString();

        alreadyUserAccountLogin(email, password);


    }

    private void alreadyUserAccountLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // updateUI(user) ;

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish(); // dont go back no needed.
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();

                            textInputPassword.setError("Wrong Password");


                        }

                        // ...
                    }
                });


    }

}