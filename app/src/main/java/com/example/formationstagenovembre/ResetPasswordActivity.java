package com.example.formationstagenovembre;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextView backToSignIn;
    private Button resetBtn;
    private EditText email;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        backToSignIn = findViewById(R.id.back_to_sign_in);
        email = findViewById(R.id.email_reset_pws);
        resetBtn = findViewById(R.id.reset_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        backToSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
        });

        resetBtn.setOnClickListener(v -> {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Password reset email sent ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, SignInActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Failed request", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            });
        });
    }
}
