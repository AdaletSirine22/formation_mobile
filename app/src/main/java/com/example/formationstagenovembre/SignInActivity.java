package com.example.formationstagenovembre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private Button signInButton;
    private TextView forgot_psw, goToRegister;
    private EditText email, password;
    private CheckBox rememberMe;
    private String emailString, passwordString;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        forgot_psw = findViewById(R.id.forgot_psw);
        goToRegister = findViewById(R.id.go_to_register);
        signInButton = findViewById(R.id.sign_in_btn);
        email = findViewById(R.id.email_sign_in);
        password = findViewById(R.id.password_sign_in);
        rememberMe = findViewById(R.id.checkbox_remember_me);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        forgot_psw.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
            startActivity(intent);

        });

        goToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(v -> {
            emailString = email.getText().toString().trim();
            passwordString = password.getText().toString().trim();
            progressDialog.setMessage("Please wait... ");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    checkEmailVerification();

                } else {
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        });
    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show();
                user.sendEmailVerification();
                firebaseAuth.signOut();
                progressDialog.dismiss();
            }
        }
    }
}
