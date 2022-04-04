package ch.epfl.sweng.hostme.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.epfl.sweng.hostme.Apartment;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Apartment>> apartments;

    /**
     * Constructor of the view model for the search menu
     */
    public SearchViewModel() {
        apartments = new MutableLiveData<>();
    }

    public LiveData<List<Apartment>> getApartments() {
        return apartments;
    }

}
