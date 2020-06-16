package e.user.kemah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import e.user.kemah.adapter.IkutTripAdapter;
import e.user.kemah.data.DataSetFireIkut;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.FirestoreClient;

import java.util.ArrayList;

public class IkutTrip extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("trip");
    private IkutTripAdapter adapter;
    Button btnDetail, btnFavorit;

    //private RecyclerView recyclerView;
    //private ArrayList<DataSetFireIkut> arrayList;
    //private FirestoreClient dataIkut;
    //FirebaseRecyclerOptions
    // private FirebaseRecyclerAdapter<DataSetFireIkut, IkutTripViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikut_trip);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query = ref.orderBy("namaTrip", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<DataSetFireIkut> options =
                new FirestoreRecyclerOptions.Builder<DataSetFireIkut>()
                        .setQuery(query, DataSetFireIkut.class)
                        .build();

        adapter = new IkutTripAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(IkutTrip.this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
    //FirestoreRecyclerOptions.Builder<DataSetFireIkut>()
}
