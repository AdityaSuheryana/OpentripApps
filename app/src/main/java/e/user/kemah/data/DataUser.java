package e.user.kemah.data;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import e.user.kemah.Register;


public class DataUser extends Register {
    private String email;
    private String nama;
    private String noHp;
    private static final String TAG = "Register";

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    private String UserID;

    public String Uid(FirebaseAuth auth) {
        return auth.getCurrentUser().getUid();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public DataUser(String email, String nama, String noHp, String userID) {
        this.email = email;
        this.nama = nama;
        this.noHp = noHp;
        UserID = userID;

        this.email = email;
        this.nama = nama;
        this.noHp = noHp;
    }
    public void tambahUser(String email, String nama, String noHp, final String userID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("users").document(userID);

        Map<String, Object> user = new HashMap<>();
        user.put("Nama", nama);
        user.put("email", email);
        user.put("No Hp", noHp);

        //user.put("password",pw);
        //user.put("alamat",alamatP);

        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }
}
