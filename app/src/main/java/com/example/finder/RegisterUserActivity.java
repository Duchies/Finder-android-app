package com.example.finder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputphoneNumber;
    private Button buttonResgiter;

    private String SETERROR = "Field can't be empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();


        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_name);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputphoneNumber = findViewById(R.id.text_input_phoneNumber);
        buttonResgiter = findViewById(R.id.btn_register);
        buttonResgiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput();
            }
        });


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

    private boolean validatepPhoneNumber() {
        String emailInput = textInputphoneNumber.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputphoneNumber.setError(SETERROR);
            return false;
        } else {
            textInputphoneNumber.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputUsername.setError(SETERROR);
            return false;
        } else {
            textInputUsername.setError(null);
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
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validatepPhoneNumber()) {
            return;
        }
        String email = textInputEmail.getEditText().getText().toString();

        String username = textInputUsername.getEditText().getText().toString();

        String password = textInputPassword.getEditText().getText().toString();

        String phoneNumber = textInputphoneNumber.getEditText().getText().toString();


        createUserAccount(email, username, password);


    }

    private void createUserAccount(String email, String username, String password) {

        buttonResgiter.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                            showMessage("Account created");
                            buttonResgiter.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish(); // because no reson to go on register page

                        } else {

                            // account creation failed
                            showMessage("account creation failed " + task.getException().getMessage());

                            buttonResgiter.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void viewRegistedClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }


}