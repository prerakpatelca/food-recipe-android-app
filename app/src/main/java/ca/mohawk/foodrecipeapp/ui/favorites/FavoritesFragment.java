package ca.mohawk.foodrecipeapp.ui.favorites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.foodrecipeapp.FoodData;
import ca.mohawk.foodrecipeapp.LoginActivity;
import ca.mohawk.foodrecipeapp.MyAdapter;
import ca.mohawk.foodrecipeapp.R;
import ca.mohawk.foodrecipeapp.ui.home.HomeViewModel;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    RecyclerView mRecyclerView;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    TextView txt_empty;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText txt_search;
    MyAdapter myAdapter;
    String userId;
    String TAG = "tag";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        txt_empty = root.findViewById(R.id.txt_empty);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        txt_search = root.findViewById(R.id.txt_searchtext);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            String warning = "You need to Login First to unlock this feature!!";
            intent.putExtra("warning",warning);
            startActivity(intent);
        } else {
            userId = firebaseAuth.getCurrentUser().getUid();

            progressDialog = new ProgressDialog(root.getContext());
            progressDialog.setMessage("Loading Recipes...");

            myFoodList = new ArrayList<FoodData>();

            progressDialog.show();

            final DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

            documentReference.collection("Favorites").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    myFoodList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FoodData foodData = documentSnapshot.toObject(FoodData.class);
                        foodData.setKey(foodData.getKey());
                        foodData.setRecipeKey(documentSnapshot.getId());
                        myFoodList.add(foodData);
                    }
                    myAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
            txt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
                }
            });

            myAdapter = new MyAdapter(root.getContext(), myFoodList);
            mRecyclerView.setAdapter(myAdapter);
        }
        return root;
    }

    private void filter(String text) {
        ArrayList<FoodData> filterList = new ArrayList<>();

        for (FoodData item: myFoodList){
            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }

        myAdapter.filteredList(filterList);
    }
}
