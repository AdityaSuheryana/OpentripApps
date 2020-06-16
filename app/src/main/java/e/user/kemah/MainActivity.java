package e.user.kemah;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    //private String TAG = "MainActivity";
    //  ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText email, password;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseAuth auth;
    private static final int RC_SIGN_IN = 9001;
    Button btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = auth.getCurrentUser();
        btnGoogle = findViewById(R.id.btnLoginGoogle);
        //btnSignOut = findViewById(R.id.btnSignOut);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        Button btnSignIn = findViewById(R.id.button2);
        TextView tvSignUp = findViewById(R.id.textView3);
        //    progressBar = findViewById(R.id.progress_circular);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        authListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser uFirebaseUser = auth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (uFirebaseUser != null) {
                    Toast.makeText(MainActivity.this,
                            "Selamat Datang", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSignUp = new Intent(MainActivity.this, Register.class);
                startActivity(iSignUp);
            }
        });

    }

    public void SignInGoogle() {
        //  progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Sign in berhasil", Toast.LENGTH_SHORT).show();
            assert acc != null;
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(MainActivity.this, "Sign in gagal", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "SUKSES KUY", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                    startActivity(new Intent(MainActivity.this, Home.class));
                } else {
                    Toast.makeText(MainActivity.this, "yahhh , Gagal", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });


    }

    private void signIn() {
        String emailP = email.getText().toString().trim();
        String pw = password.getText().toString().trim();
        if (emailP.isEmpty() && pw.isEmpty()) {
            Toast.makeText(MainActivity.this, " Tolong isi email dan password " +
                    "anda ", Toast.LENGTH_SHORT).show();
        } else if (emailP.isEmpty()) {
            email.setError("Tolong isi email");
            email.requestFocus();
        } else if (pw.isEmpty()) {
            password.setError("Tolong isi Passwordnya");
            password.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(emailP, pw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        //updateUI(null);
                        Toast.makeText(MainActivity.this, "Login Error, silahkan ulangi !", Toast.LENGTH_SHORT).show();
                    } else {
                        //FirebaseUser user = auth.getCurrentUser();
                        //updateUI(user);
                        Intent iHome = new Intent(MainActivity.this, Home.class);
                        startActivity(iHome);
                    }
                }
            });
        }
        //else{
        // Toast.makeText(MainActivity.this, "Terjadi kesalahan",Toast.LENGTH_SHORT).show();
        //  }

    }


    private void updateUI(FirebaseUser fUser) {
        //btnSignOut.setVisibility(View.VISIBLE);
        if (fUser != null) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (account != null) {
                String nama = account.getDisplayName();
                String Email = account.getEmail();
                //Uri foto = account.getPhotoUrl();

                Toast.makeText(MainActivity.this, nama + Email, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
