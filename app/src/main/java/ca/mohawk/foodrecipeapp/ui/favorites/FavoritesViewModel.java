package ca.mohawk.foodrecipeapp.ui.favorites;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FavoritesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Favorites fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}