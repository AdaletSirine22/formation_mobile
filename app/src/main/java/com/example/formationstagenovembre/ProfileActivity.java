package com.example.formationstagenovembre;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText name, email, phone;
    private Button logOut, editProfile;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser loggedUser;
    private DatabaseReference userDetailsReference;
    private ProgressDialog progressDialog;
    private String updatedPhone ,updatedName;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phone = findViewById(R.id.phone_profile);
        logOut = findViewById(R.id.log_out_profile);
        editProfile = findViewById(R.id.edit_profile_btn);

        drawerLayout = findViewById(R.id.drawer_layout_profile);
        navigationView = findViewById(R.id.navigation_view_profile);
        iconMenu = findViewById(R.id.menu_profile);

        navigationDrawer();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedUser = firebaseAuth.getCurrentUser();
        userDetailsReference = firebaseDatabase.getReference().child("users").child(loggedUser.getUid());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        userDetailsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userDetails) {
                String nameString = userDetails.child("fullName").getValue().toString();
                String emailString = userDetails.child("email").getValue().toString();
                String phoneString = userDetails.child("phone").getValue().toString();
                name.setText(nameString);
                email.setText(emailString);
                phone.setText(phoneString);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "" + databaseError, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        logOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
            SharedPreferences preferences = getSharedPreferences("checkBoxRemember", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isChecked", false);
            editor.apply();
            finish();
        });

        editProfile.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.show();
                userDetailsReference.child("fullName").setValue(updatedName);
                userDetailsReference.child("phone").setValue(updatedPhone);
                Toast.makeText(this, "Your data has been changed successfully", Toast.LENGTH_SHORT).show();
                name.clearFocus();
                phone.clearFocus();
                progressDialog.dismiss();
            }
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.profile);
        iconMenu.setOnClickListener(v -> {
            if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.gray));

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.devices){
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this,HomeActivity.class));
        } else if (menuItem.getItemId() == R.id.ticket) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this,TicketActivity.class));
        } else if (menuItem.getItemId() == R.id.profile) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private boolean validate() {
        boolean result = false;
        updatedName = name.getText().toString().trim();
        updatedPhone = phone.getText().toString().trim();
        if (updatedName.length() < 7) {
            name.setError("name is invalid");
        } else if (updatedPhone.length() != 8) {
            phone.setError("phone is invalid");
        } else
            result = true;
        return result;
    }
}