package com.example.formationstagenovembre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        iconMenu = findViewById(R.id.menu_home);

        navigationDrawer();

    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);
        iconMenu.setOnClickListener(v -> {
            if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.darkRed));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.devices){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (menuItem.getItemId() == R.id.profile) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this,ProfileActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}