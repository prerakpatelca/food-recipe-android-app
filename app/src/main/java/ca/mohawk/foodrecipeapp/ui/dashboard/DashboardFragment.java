package ca.mohawk.foodrecipeapp.ui.dashboard;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.foodrecipeapp.FoodData;
import ca.mohawk.foodrecipeapp.LoginActivity;
import ca.mohawk.foodrecipeapp.MyAdapter;
import ca.mohawk.foodrecipeapp.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    RecyclerView mRecyclerView;
    TextView txt_no_result_found;
    EditText txt_search;
    ProgressDialog progressDialog;
    List<FoodData> myFoodList;
    MyAdapter myAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mRecyclerView = root.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(root.getContext(),1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        txt_no_result_found = root.findViewById(R.id.txt_no_result_found);
        txt_search = root.findViewById(R.id.txt_searchtext);
        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setMessage("Loading Recipes...");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            String warning = "You need to Login First to unlock this feature!!";
            intent.putExtra("warning",warning);
            startActivity(intent);
        } else {
            userID = firebaseAuth.getCurrentUser().getUid();

            myFoodList = new ArrayList<FoodData>();

            myAdapter = new MyAdapter(root.getContext(), myFoodList);
            mRecyclerView.setAdapter(myAdapter);


            progressDialog.show();

            final DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
            documentReference.collection("Recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    myFoodList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FoodData foodData = documentSnapshot.toObject(FoodData.class);
                        foodData.setKey(foodData.getKey());
                        foodData.setRecipeKey(foodData.getRecipeKey());
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
