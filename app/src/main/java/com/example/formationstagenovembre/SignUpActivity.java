package com.example.formationstagenovembre;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formationstagenovembre.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private Button signUpBtn;
    private TextView login;
    private EditText fullname, email, phone, password, confirmPassword;
    private String fullnameString, emailString, phoneString, passwordString, confirmPasswordString;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signUpBtn = findViewById(R.id.sign_up_btn);
        login = findViewById(R.id.go_to_login);
        fullname = findViewById(R.id.full_name_sign_up);
        email = findViewById(R.id.email_sign_up);
        phone = findViewById(R.id.phone_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirmPassword = findViewById(R.id.confirm_password_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUpBtn.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Faileddd", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void sendEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser != null) {
            loggedUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendUserData();
                    Toast.makeText(this, "Registration Done! Please check your Email address", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference firebaseReference = firebaseDatabase.getReference("users");
        User user = new User(fullnameString, emailString, phoneString);
        firebaseReference.child(firebaseAuth.getUid() + "").setValue(user);
    }

    private boolean validate() {
        boolean result = false;
        fullnameString = fullname.getText().toString().trim();
        emailString = email.getText().toString().trim();
        phoneString = phone.getText().toString().trim();
        passwordString = password.getText().toString().trim();
        confirmPasswordString = confirmPassword.getText().toString().trim();

        if (fullnameString.length() < 7) {
            fullname.setError("fullname is invalid");
        } else if (!isValidRegex(emailString, EMAIL_REGEX)) {
            email.setError("email is  invalid");
        } else if (phoneString.length() != 8) {
            phone.setError("phone is invalid");
        } else if (passwordString.length() < 6) {
            password.setError("password is invalid");
        } else if (!confirmPasswordString.equals(passwordString)) {
            confirmPassword.setError("confirm password is invalid");
        } else
            result = true;
        return result;
    }

    private boolean isValidRegex(String mot, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mot);
        return matcher.matches();
    }
}



