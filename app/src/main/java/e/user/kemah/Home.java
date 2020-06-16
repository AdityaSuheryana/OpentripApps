package e.user.kemah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    private TextView image1,image2,image3,image4,view1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomNavigationView nMainNavBar;
    FirebaseAuth auth;

    ImageButton btnTambah,btnIkut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView imageView1 = findViewById(R.id.imageview1);
        ImageView imageView2 = findViewById(R.id.imageview2);
        ImageView imageView3 = findViewById(R.id.imageview3);
        ImageView imageView4 = findViewById(R.id.imageview4);

        image1 = findViewById(R.id.img1);
        image2 = findViewById(R.id.img2);
        image3 = findViewById(R.id.img3);
        image4 = findViewById(R.id.img4);

        auth = FirebaseAuth.getInstance();
        nMainNavBar = findViewById(R.id.bn_main);

        //variabel Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //view1 = findViewById(R.id.textView);
        btnTambah = findViewById(R.id.imgBtn1);
        btnIkut = findViewById(R.id.imgBtn);

        Picasso.with(Home.this).load("https://firebasestorage.googleapis.com/v0/b/kemah-a20bb." +
                "appspot.com/o/Curug-parigi-traveling-yuk.jpg?alt=media&token=d17ed453-b9e3-47af-9aea-d0219a2f0a2f").into(imageView1);
        Picasso.with(Home.this).load("https://firebasestorage.googleapis.com/v0/b/kemah-a20bb." +
                "appspot.com/o/Raja-Ampat-Papua-CekAja-2048x1076.jpg?alt=media&token=ce52432c-223d-4b9c-8e06-dfc269d3baa4").into(imageView2);
        Picasso.with(Home.this).load("https://firebasestorage.googleapis.com/v0/b/kemah-a20bb.appspot.com" +
                "/o/jam_gadang_10820.jpg?alt=media&token=38403b9b-f312-42fa-afdf-44ee70a199c2").into(imageView3);
        Picasso.with(Home.this).load("https://firebasestorage.googleapis.com/v0/b/kemah-a20bb." +
                "appspot.com/o/tugu-1.jpg?alt=media&token=56678dfe-8cbe-4042-8605-1189a05343a9").into(imageView4);
        nMainNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.logout:
                        auth.signOut();
                        mGoogleSignInClient.signOut();
                        Intent iLog = new Intent(Home.this,MainActivity.class);
                        startActivity(iLog);
                        return true;
                    case R.id.menuTambahTrip:
                        startActivity(new Intent(Home.this,TambahTrip.class));
                        return true;
                    case R.id.menuHome:
                        return true;
                    default:
                        return false;
                }
            }
        });

        btnIkut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iIkut = new Intent(Home.this,IkutTrip.class);
                startActivity(iIkut);
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSignUp = new Intent(Home.this,TambahTrip.class);
                startActivity(iSignUp);
            }
        });

    }
/*

        DocumentReference user=db.collection("galery").document("images");

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    //Untuk Image 1
                    StringBuilder img1 = new StringBuilder("");
                    img1.append(doc.get("image1"));
                    image1.setText(img1.toString());
                    String image1url = image1.getText().toString();


                    StringBuilder img2 = new StringBuilder("");
                    img2.append(doc.get("imgUrl2"));
                    image2.setText(img2.toString());
                    String image2url = image2.getText().toString();
                    Picasso.with(Home.this).load(image2url).into(imageView2);

                    StringBuilder img3= new StringBuilder();
                    img3.append(doc.get("imgUrl3"));
                    image3.setText(img3.toString());
                    String image3url = image3.getText().toString();
                    Picasso.with(Home.this).load(image3url).into(imageView3);

                    StringBuilder img4 = new StringBuilder("");
                    img4.append(doc.get("imgUrl4"));
                    image4.setText(img4.toString());
                    String image4url = image4.getText().toString();
                    Picasso.with(Home.this).load(image4url).into(imageView4);

                    Toast.makeText(Home.this,"Berhasil",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this,"Gambar Gagal dimuat",Toast.LENGTH_SHORT).show();
            }
        });
 */
}

