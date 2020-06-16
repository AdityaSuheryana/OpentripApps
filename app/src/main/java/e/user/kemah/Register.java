package e.user.kemah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import e.user.kemah.data.DataUser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText email,password,nama,noHp,alamat;
    Button btnSignUp;

    FirebaseAuth auth;
    //Date ttl;
    String userID;
    FirebaseFirestore fStore;
    private static final String TAG = "Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        nama = findViewById(R.id.edtNama);
        noHp = findViewById(R.id.edtNoHp);

        btnSignUp = findViewById(R.id.btnDaftar);

        fStore = FirebaseFirestore.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String emailP = email.getText().toString().trim();
                final String namaP=nama.getText().toString().trim();
                final String pw = password.getText().toString().trim();
                final String PnoHp = noHp.getText().toString().trim();
                //final String alamatP = alamat.getText().toString().trim();


                if(TextUtils.isEmpty(emailP)){
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(pw)){
                    password.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    password.setError("Password Must be >= 6 Characters");
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(emailP,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = auth.getCurrentUser().getUid();
                           /* DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Nama",namaP);
                            user.put("email",emailP);
                            user.put("No Hp",PnoHp);

                            //user.put("password",pw);
                            //user.put("alamat",alamatP);
                            */
                            DataUser data = new DataUser(emailP,namaP,PnoHp,userID);
                            data.tambahUser(emailP,namaP,PnoHp,userID);
                            startActivity(new Intent(Register.this,Home.class));

                        }else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        /*auth.createUserWithEmailAndPassword(emailP, pw).addOnCompleteListener(RegisterActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "registrasi tidak berhasil",Toast.LENGTH_SHORT).show();
                                    }else{


                                        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                                        Map<String,Object> user = new HashMap<>();
                                        user.put("nama","adit");
                                        user.put("email","fandit@gmail.com");


                                        database.collection("user").add(user);

                                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                        // Write a message to the database
                                        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("messages");
                                        User u = new User(namaP,emailP);
                                        myRef.child("id").setValue(namaP);


                                    }
                                }
                            });
                }
                //else{
                  //  Toast.makeText(RegisterActivity.this, "registrasi error",Toast.LENGTH_SHORT).show();
                //}
            }
        });

         */

    }
}

