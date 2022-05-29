package ch.epfl.sweng.hostme.ui.search;

import static ch.epfl.sweng.hostme.utils.Constants.APART_ID;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_RATING;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;

public class GradeApartment extends Fragment {

    private final static String GRADES_SAVED = "Grades saved!";
    private final DocumentReference reference = Database.getCollection(KEY_COLLECTION_RATING).document(Auth.getUid());
    private RatingBar[] ratingBars;
    private String apartID;

    public GradeApartment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.grade_apartment, container, false);

        RatingBar kitchenRatingBar = root.findViewById(R.id.kitchen_rating_bar);
        RatingBar loungeRatingBar = root.findViewById(R.id.lounge_rating_bar);
        RatingBar bedRoomRatingBar = root.findViewById(R.id.bed_room_rating_bar);
        RatingBar bathRoomRatingBar = root.findViewById(R.id.bath_room_rating_bar);
        RatingBar gardenRatingBar = root.findViewById(R.id.garden_rating_bar);
        RatingBar utilityRatingBar = root.findViewById(R.id.utility_rating_bar);
        RatingBar overallRatingBar = root.findViewById(R.id.overall_rating_bar);
        ratingBars = new RatingBar[]{kitchenRatingBar, loungeRatingBar, bedRoomRatingBar, bathRoomRatingBar, gardenRatingBar, utilityRatingBar, overallRatingBar};
        Button saveRatingButton = root.findViewById(R.id.save_rating);
        saveRatingButton.setOnClickListener(this::saveRatingInfo);

        Bundle bundle = this.getArguments();
        if (bundle != null && !bundle.isEmpty()) {
            apartID = bundle.getString(APART_ID);
            bundle.clear();
        }
        retrieveGradesFromDB();
        return root;
    }

    private void saveRatingInfo(View view) {
        ArrayList<Float> grades = new ArrayList<>();
        for (RatingBar ratingBar : ratingBars) {
            grades.add(ratingBar.getRating());
        }

        reference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                reference.update(apartID, grades);
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put(apartID, grades);
                reference.set(data);
            }
            Toast.makeText(view.getContext(), GRADES_SAVED, Toast.LENGTH_SHORT).show();
        });
    }

    private void retrieveGradesFromDB() {
        reference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ArrayList<Double> grades = (ArrayList<Double>) documentSnapshot.get(apartID);
                if (grades != null) {
                    for (int i = 0; i < ratingBars.length; ++i) {
                        ratingBars[i].setRating(grades.get(i).floatValue());
                    }
                }
            }
        });
    }
}
