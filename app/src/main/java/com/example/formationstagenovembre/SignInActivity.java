package com.example.formationstagenovembre;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

public class SignInActivity extends AppCompatActivity {
    private Button sign_in_btn;
    private TextView forgot_psw;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        forgot_psw = findViewById(R.id.forgot_psw);
        register = findViewById(R.id.register);
        Button signInButton = findViewById(R.id.sign_in_btn);



        forgot_psw.setOnClickListener(v -> {

                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);

        });
        register.setOnClickListener(v -> {

                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);

        });
    }
}
