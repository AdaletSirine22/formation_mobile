package com.example.formationstagenovembre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TicketActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnGetTicket;
    private static int i, iA, iB, iC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        drawerLayout = findViewById(R.id.drawer_layout_ticket);
        navigationView = findViewById(R.id.navigation_view_ticket);
        iconMenu = findViewById(R.id.menu_ticket);
        radioGroup = findViewById(R.id.radio_group);
        btnGetTicket = findViewById(R.id.btn_get_ticket);

        navigationDrawer();

        btnGetTicket.setOnClickListener(view -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            try {
                createPdf(radioButton.getText().toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void createPdf(String ticketName) throws FileNotFoundException {
        i++;
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "Ticket" + i + ".pdf");

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(5, 5, 5, 5);

        //image for pdf
        Drawable d = getDrawable(R.drawable.book);
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();
        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER).setHeight(200);

        //paragraph
        Paragraph title = new Paragraph(" Electric Ticket").setBold().setFontSize(17).setTextAlignment(TextAlignment.CENTER);
        Paragraph welcome = new Paragraph("Welcome").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);

        Paragraph ticketNum = null, nameTicket = null;
        if (ticketName.equals("Choix A")) {
            iA++;
            nameTicket = new Paragraph("Choix A").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
            ticketNum = new Paragraph("A0" + iA).setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
        } else if (ticketName.equals("Choix B")) {
            iB++;
            nameTicket = new Paragraph("Choix B").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
            ticketNum = new Paragraph("B0" + iB).setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);

        } else {
            iC++;
            nameTicket = new Paragraph("Choix C").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
            ticketNum = new Paragraph("C0" + iC).setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
        }

        float[] width = {100f, 100f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateTimeFormatter))));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        table.addCell(new Cell().add(new Paragraph("Time")));
        table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter))));

        document.add(image);
        document.add(title);
        document.add(welcome);
        document.add(nameTicket);
        document.add(ticketNum);
        document.add(table);

        document.close();

        Toast.makeText(this, "Pdf generated", Toast.LENGTH_SHORT).show();

    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.ticket);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.devices) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, HomeActivity.class));
        } else if (menuItem.getItemId() == R.id.profile) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (menuItem.getItemId() == R.id.ticket) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

}