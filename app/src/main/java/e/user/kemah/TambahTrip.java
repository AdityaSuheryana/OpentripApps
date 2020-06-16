package e.user.kemah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TambahTrip extends AppCompatActivity {
    Button btnTambahTrip,btnPilih;
    //ImageButton btnPilih;
    EditText namaTrip,deskripsi,tglBack,id_trip,tglPergi,meet;
    String userID,emailP;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    //TextView a1;
    private StorageReference mStorageRef;
    private  FirebaseAuth.AuthStateListener uAuth;
    public Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ProgressDialog progressDialog;
    private Bitmap compressor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_trip);
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fStore = FirebaseFirestore.getInstance();

        btnTambahTrip = findViewById(R.id.btnDaftarTrip);
        btnPilih = findViewById(R.id.btnPilihFile);


        namaTrip = findViewById(R.id.edtTrip);
        tglPergi = findViewById(R.id.edtTanggal);
        tglBack = findViewById(R.id.edtTglPulang);
        deskripsi = findViewById(R.id.edtDeskripsi);
        id_trip = findViewById(R.id.edtIdTrip);
        meet = findViewById(R.id.edtMeetingPoint);

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
        btnTambahTrip
                .setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 //FileUpload();
                                                 progressDialog.setMessage("Mengirim data...");
                                                 progressDialog.show();
                                                 final String emailP = auth.getCurrentUser().getEmail();
                                                 final String namaT = namaTrip.getText().toString();
                                                 final String desk = deskripsi.getText().toString();
                                                 final String tglPergi1 = tglPergi.getText().toString();
                                                 final String tglKembali = tglBack.getText().toString();
                                                 final String meet1 = meet.getText().toString();
                                                 final String id_trip1 = id_trip.getText().toString();

                                                 //validasi
                                                 if (!TextUtils.isEmpty(namaT) && !TextUtils.isEmpty(desk)) {
                                                     final File newFile = new File(filePath.getPath());
                                                     try {
                                                         compressor = new Compressor(TambahTrip.this).setMaxHeight(125).setMaxWidth(125).setQuality(50).compressToBitmap(newFile);
                                                     } catch (IOException e) {
                                                         e.printStackTrace();
                                                     }
                                                     getExtension(filePath);

                                                     final UploadTask image_path = mStorageRef.child("Trip_images").child(namaT).putFile(filePath);
                                                     image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                             if (task.isSuccessful()) {
                                                                 StorageReference url = mStorageRef.child("Trip_images").child(namaT);
                                                                 url.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                     @Override
                                                                     public void onSuccess(Uri uri) {
                                                                         Uri download_uri = uri;
                                                                         String img = mStorageRef.child("Trip_images").child(namaT).getDownloadUrl().toString();
                                                                         Map<String, String> main = new HashMap<>();
                                                                         main.put("email",emailP);
                                                                         main.put("namaTrip",namaT);
                                                                         main.put("deskripsi",desk);
                                                                         main.put("imageTrip",download_uri.toString());
                                                                         main.put("tanggalBerangkat",tglPergi1);
                                                                         main.put("tanggalPulang",tglKembali);
                                                                         main.put("meetingPoint",meet1);
                                                                         main.put("idTrip",id_trip1);
                                                                         fStore.collection("trip").document(namaT).set(main);
                                                                     }
                                                                 });
                                                                 //storeTripData(task, emailP, namaT, desk);

                                                                 Toast.makeText(TambahTrip.this, "Sukses = ", Toast.LENGTH_LONG).show();
                                                                 progressDialog.dismiss();
                                                                 startActivity(new Intent(TambahTrip.this,Home.class));
                                                             } else {
                                                                 Toast.makeText(TambahTrip.this, "gambar error", Toast.LENGTH_SHORT).show();
                                                                 progressDialog.dismiss();
                                                             }

                                                         }
                                                     });
                                                 } else {
                                                     Toast.makeText(TambahTrip.this, "Isi semua kolomnya", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                                         });
        // private Void fileUpload(){
    }

 //       return storeTripData(img,namaT,desk,emailP);


    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),PICK_IMAGE_REQUEST);
    }/*
    private void FileUpload(){
        //final String pw = tglBack.getText().toString().trim();
        //final String PnoHp = noHp.getText().toString().trim();
        //Uri file = Uri.fromFile(new File(""));
        StorageReference riversRef = mStorageRef.child("images/"+ UUID.randomUUID().toString());
        //StorageReference riversRef = mStorageRef.child("images/"+getExtension(file));
        //StorageReference riversRef = mStorageRef.child("images/"+ UUID.randomUUID().toString());
        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        DocumentReference documentReference = fStore.collection("trips").document(""+UUID.randomUUID());
                        Map<String,Object> trip = new HashMap<>();
                        trip.put("Nama trip",emailP);
                        trip.put("Tgl Berangkat",namaP);
                        //trip.put("Tgl Pulang",pw);
                        trip.put("foto","");
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(TambahTrip.this, "Data Berhasil disimpan",Toast.LENGTH_LONG).show();
                        Intent iHome = new Intent(TambahTrip.this,Home.class);
                        startActivity(iHome);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(TambahTrip.this, "gagal , cek koneksi internet anda ",Toast.LENGTH_LONG).show();
                    }
                });

    private void storeTripData(Task<UploadTask.TaskSnapshot> task, String namaT, String desk, String emailP) {
        //final StorageReference Store=
        Uri download_uri;
        String img = mStorageRef.child("Trip_images").child(namaT).getDownloadUrl().toString();
        //Toast.makeText(TambahTrip.this, "Trip di tambahkan", Toast.LENGTH_SHORT).show();
        //userID = auth.getCurrentUser().getUid();
        Map<String,Object> user = new HashMap<>();
        user.put("Nama Trip",namaT);
        user.put("email",emailP);
        user.put("Deskripsi",desk);
        DocumentReference documentReference = fStore.collection("trip").document(""+UUID.randomUUID());
        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahTrip.this, "berhasil aja", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TambahTrip.this, Home.class));
                } else {
                    Toast.makeText(TambahTrip.this, "Firestore nya error ", Toast.LENGTH_SHORT).show();

                }
            }
        });

*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&
                data.getData()!= null){

            //  && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                //gambar.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            //gambar.setImageURI(filePath);
            //a1.setText(filePath.toString());
        }
    }
}
