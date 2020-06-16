package e.user.kemah.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import e.user.kemah.ConfirmIkutTrip;
import e.user.kemah.Home;
import e.user.kemah.IkutTrip;
import e.user.kemah.R;
import e.user.kemah.data.DataSetFireIkut;

import static androidx.core.content.ContextCompat.startActivity;

public class IkutTripAdapter extends FirestoreRecyclerAdapter<DataSetFireIkut,IkutTripAdapter.IkutTripHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //private static final String TAG = "IkutTrip";
        public IkutTripAdapter(@NonNull FirestoreRecyclerOptions<DataSetFireIkut> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final IkutTripHolder holder, final int position, @NonNull DataSetFireIkut model) {
        holder.tv_nama.setText(model.getNamaTrip());
        holder.tv_deskripsi.setText(model.getDeskripsi());
        Picasso.with(null).load(model.getImageTrip()).into(holder.image_trip);
        holder.btnIkut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.btnIkut.getContext(), ConfirmIkutTrip.class);
                i.putExtra("namaTrip",holder.tv_nama.getText().toString());
                //Toast.makeText(holder.btnIkut.getContext(), "Berhasil mengikuti trip "+holder.tv_nama.getText(), Toast.LENGTH_SHORT).show();
                holder.btnIkut.getContext().startActivity(i);
            }
        });
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailP = auth.getCurrentUser().getEmail();
                DocumentReference ref = db.collection("listIkutTrip").document(String.valueOf(UUID.randomUUID()));
                Map<String, Object> user = new HashMap<>();
                user.put("NamaTrip",holder.tv_nama.getText());
                user.put("email",emailP);
                //user.put("password",pw);
                //user.put("alamat",alamatP);
                ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(holder.btnFav.getContext(), "berhasil ditambahkan ke wishlist", Toast.LENGTH_SHORT).show();
                        Intent iH = new Intent(holder.btnFav.getContext(),Home.class);
                        holder.btnFav.getContext().startActivity(iH);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
    }

    @NonNull
    @Override
    public IkutTripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new IkutTripHolder(v);
    }

    /**
     *
     */
class IkutTripHolder extends RecyclerView.ViewHolder{

        TextView tv_nama,tv_deskripsi;
        ImageView image_trip;
        Button btnIkut,btnFav;

        public IkutTripHolder(View itemView){
            super(itemView);
            tv_nama = itemView.findViewById(R.id.tv_item_name);
            tv_deskripsi = itemView.findViewById(R.id.tv_item_detail);
            image_trip = itemView.findViewById(R.id.img_item_photo);
            btnIkut = itemView.findViewById(R.id.btn_set_Detail);
            btnFav = itemView.findViewById(R.id.btn_set_Favorit);

        }
    }

}
