package ca.mohawk.foodrecipeapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> myFoodList;
    private int lastPosition = -1;
    String userID;
    String recipeKey = "";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public MyAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_row_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {

        Glide.with(mContext).load(myFoodList.get(position).getItemImage()).into(holder.imageView);

        holder.mTitle.setText(myFoodList.get(position).getItemName());
        holder.mDescription.setText(myFoodList.get(position).getItemDescription());
        holder.mPrice.setText(myFoodList.get(position).getItemPrice());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("RecipeName",myFoodList.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("price",myFoodList.get(holder.getAdapterPosition()).getItemPrice());
                intent.putExtra("Image",myFoodList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",myFoodList.get(holder.getAdapterPosition()).getItemDescription());
                intent.putExtra("keyValue", myFoodList.get(holder.getAdapterPosition()).getKey());

                if (firebaseAuth.getCurrentUser() != null){
                    userID = firebaseAuth.getCurrentUser().getUid();
                    if(myFoodList.get(holder.getAdapterPosition()).getRecipeKey() == null) {
                        final DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                        documentReference.collection("Recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    FoodData foodData = documentSnapshot.toObject(FoodData.class);
                                    if (foodData.getKey().equals(myFoodList.get(holder.getAdapterPosition()).getKey())) {
    //                                    Log.d("TAG inside RecipeKey : ", foodData.getRecipeKey());
                                        recipeKey = foodData.getRecipeKey();
                                        intent.putExtra("recipeKeyValue", recipeKey);
                                        mContext.startActivity(intent);
                                    }
                                }

                                if (recipeKey.equals("")){
    //                            Log.d("TAG outside ",recipeKey);
                                intent.putExtra("recipeKeyValue", myFoodList.get(holder.getAdapterPosition()).getRecipeKey());
                                mContext.startActivity(intent);
                                }
                            }
                        });
                    }
                    else{
                        Log.d("TAG myAdapter",recipeKey + " myAdapter");
                        intent.putExtra("recipeKeyValue", myFoodList.get(holder.getAdapterPosition()).getRecipeKey());
                        mContext.startActivity(intent);
    //                }
                    }
                } else {
                    intent.putExtra("recipeKeyValue", myFoodList.get(holder.getAdapterPosition()).getRecipeKey());
                    mContext.startActivity(intent);
                }
            }
        });

        setAnimation(holder.itemView,position);
    }

    public void setAnimation(View viewToAnimate, int position){
        if(position > lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

    public void filteredList(ArrayList<FoodData> filterList) {
        myFoodList = filterList;
        notifyDataSetChanged();
    }

}

class FoodViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView mTitle, mDescription, mPrice;
    CardView mCardView;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mPrice = itemView.findViewById(R.id.tvPrice);

        mCardView = itemView.findViewById(R.id.myCardView);
    }
}