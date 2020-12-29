package ca.mohawk.foodrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecipeActivity extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    EditText txt_name,txt_description, txt_price;
    String imageUrl, userID;
    String key, oldImageUrl, recipeKey;
    String recipename, recipeDescription, recipePrice;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);

        recipeImage = findViewById(R.id.iv_foodImage);
        txt_name = findViewById(R.id.txt_recipe_name);
        txt_description = findViewById(R.id.text_description);
        txt_price = findViewById(R.id.text_price);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with(UpdateRecipeActivity.this).load(bundle.getString("oldImageUrl")).into(recipeImage);
            txt_name.setText(bundle.getString("recipeNameKey"));
            txt_description.setText(bundle.getString("descriptionKey"));
            txt_price.setText(bundle.getString("priceKey"));
            key = bundle.getString("key");
            recipeKey = bundle.getString("recipeKey");
            oldImageUrl = bundle.getString("oldImageUrl");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child(key);



    }

    public void uploadRecipe() {

        FoodData foodData = new FoodData(
                recipename, recipeDescription, recipePrice,imageUrl);

        databaseReference.setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(imageUrl != oldImageUrl ){
                    StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                    storageReferenceNew.delete();
                }
            }
        });

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID).collection("Recipes").document(recipeKey);
        documentReference.update("itemName",recipename,"itemDescription",recipeDescription, "itemPrice",recipePrice, "itemImage",imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", " Recipe updated to Firestore");
                        Snackbar. make(findViewById(R.id.txt_recipe_name),"Recipe Uploaded",BaseTransientBottomBar.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", " Recipe not updated to Firestore " + e.getMessage());
                Snackbar.make(findViewById(R.id.txt_recipe_name),"Failed to Upload Recipe" + e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        finish();
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra("RecipeName",recipename);
        intent.putExtra("price",recipePrice);
        intent.putExtra("Image",imageUrl);
        intent.putExtra("Description",recipeDescription);
        intent.putExtra("keyValue",key);
        intent.putExtra("recipeKeyValue",recipeKey);

        startActivity(intent);
    }

    public void btnSelectImage(View view) {

        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            uri = data.getData();
            recipeImage.setImageURI(uri);
        }
        else {
            Snackbar.make(findViewById(R.id.txt_recipe_name),"You haven't picked image", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    public void btnUpdateRecipe(View view) {

        recipename = txt_name.getText().toString().trim();
        recipeDescription = txt_description.getText().toString().trim();
        recipePrice = txt_price.getText().toString().trim();



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading...");
        progressDialog.show();


        if(uri == null){
            imageUrl = oldImageUrl;
            uploadRecipe();
            progressDialog.dismiss();
        }
        else{
            storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadRecipe();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }
    }
}