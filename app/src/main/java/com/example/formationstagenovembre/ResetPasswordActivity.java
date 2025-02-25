package com.example.formationstagenovembre;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {

                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                startActivity(intent);

        });
    }
}
