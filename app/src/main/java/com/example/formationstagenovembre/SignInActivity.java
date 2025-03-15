package com.example.formationstagenovembre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
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

        //remember me checkbox
        SharedPreferences preferences = getSharedPreferences("checkBoxRemember", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean isRememberMeChecked = preferences.getBoolean("isChecked", false);

        if (isRememberMeChecked) {
            startActivity(new Intent(this, HomeActivity.class));
        }

        rememberMe.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                editor.putBoolean("isChecked", true);
                editor.apply();
            } else {
                editor.putBoolean("isChecked", false);
                editor.apply();
            }
        }));

        forgot_psw.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        goToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(v -> {
            if (validate()) {
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
            }
        });
    }

    private boolean validate() {
        boolean result = false;
        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        if (!isValidRegex(emailString, EMAIL_REGEX)) {
            email.setError("email is  invalid");
        } else if (passwordString.length() < 6) {
            password.setError("password is invalid");
        } else
            result = true;
        return result;
    }

    private boolean isValidRegex(String mot, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mot);
        return matcher.matches();
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
