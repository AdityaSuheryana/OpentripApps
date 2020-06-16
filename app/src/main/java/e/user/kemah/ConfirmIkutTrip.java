package e.user.kemah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Collection;

public class ConfirmIkutTrip extends AppCompatActivity {
    TextView linkGambar,email,namaTrip,deskripsi,tglBack,id_trip,tglPergi,meet;
    ImageView img;
    String nama;
    Button btnKonfirmasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ikut_trip);

        id_trip = findViewById(R.id.edtIdTrip);
        namaTrip = findViewById(R.id.edtTrip);
        email = findViewById(R.id.edtEmail);
        deskripsi = findViewById(R.id.edtDeskripsi);
        img = findViewById(R.id.imgTripBoking);
        linkGambar = findViewById(R.id.txtGambar);
        btnKonfirmasi = findViewById(R.id.btnConfirm);

        nama = getIntent().getStringExtra("namaTrip");
        namaTrip.setText("Nama Trip : "+getIntent().getStringExtra("namaTrip"));
        tglBack = findViewById(R.id.edtTglPulang);
        tglPergi = findViewById(R.id.edtTanggal);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("trip");
        db.collection("trip").document(nama).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder img1 = new StringBuilder("");
                img1.append(doc.get("imageTrip"));
                linkGambar.setText(img1.toString());
                String imageUrl = linkGambar.getText().toString();
                Picasso.with(ConfirmIkutTrip.this).load(imageUrl).into(img);

                tglBack.setText("Tanggal Kembali : "+doc.get("tanggalPulang").toString());
                tglPergi.setText("Tanggal Pergi : "+doc.get("tanggalBerangkat").toString());
                email.setText("Email pembuat Trip : "+doc.get("email").toString());
                deskripsi.setText("Deskripsi perjalanan : "+doc.get("deskripsi").toString());
                id_trip.setText("Id Trip : "+doc.get("idTrip").toString());


            }
        });
        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfirmIkutTrip.this, "Konfirmasi Sukses", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ConfirmIkutTrip.this,Home.class));
            }
        });

    }
}
