package com.example.formationstagenovembre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private EditText deviceName, deviceValue;
    private Button addDeviceBtn;
    private String name, value;
    private ListView listDevices;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        iconMenu = findViewById(R.id.menu_home);
        deviceName = findViewById(R.id.device_name);
        deviceValue = findViewById(R.id.device_value);
        addDeviceBtn = findViewById(R.id.add_device_btn);
        listDevices = findViewById(R.id.list_devices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        navigationDrawer();

        addDeviceBtn.setOnClickListener(v -> {
            if (validate()) {
                addDevice(deviceName.getText().toString(), deviceValue.getText().toString());
            }
        });

        ArrayList<String> deviceArrayList = new ArrayList<>();
        ArrayAdapter<String> devicesAdapter = new ArrayAdapter<>(this, R.layout.list_item, deviceArrayList);
        listDevices.setAdapter(devicesAdapter);
        DatabaseReference deviceReference = databaseReference.child("devices");

        deviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot devicesSnapshot) {
                deviceArrayList.clear();
                for (DataSnapshot deviceSnapshot : devicesSnapshot.getChildren()
                ) {
                    String nameString = deviceSnapshot.child("name").getValue().toString();
                    String valueString = deviceSnapshot.child("value").getValue().toString();
                    deviceArrayList.add(nameString + " : " + valueString);
                }
                devicesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "" + databaseError, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addDevice(String name, String value) {
        HashMap<String, String> deviceHashMap = new HashMap<>();
        deviceHashMap.put("name", name);
        deviceHashMap.put("value", value);
        databaseReference.child("devices").push().setValue(deviceHashMap);
        deviceName.setText("");
        deviceValue.setText("");
        deviceName.clearFocus();
        deviceValue.clearFocus();
        Toast.makeText(this, "New device added successfully", Toast.LENGTH_SHORT).show();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);
        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.gray));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.devices) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (menuItem.getItemId() == R.id.profile) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (menuItem.getItemId() == R.id.ticket) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, TicketActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private boolean validate() {
        boolean result = false;
        name = deviceName.getText().toString().trim();
        value = deviceValue.getText().toString().trim();
        if (name.length() < 5) {
            deviceName.setError("device name is invalid");
        } else if (value.length() < 5) {
            deviceValue.setError("device value is invalid");
        } else
            result = true;
        return result;
    }
}