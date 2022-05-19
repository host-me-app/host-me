package ch.epfl.sweng.hostme.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.EmailValidator;
import ch.epfl.sweng.hostme.utils.Profile;
import ch.epfl.sweng.hostme.wallet.WalletActivity;
import ch.epfl.sweng.hostme.utils.UserManager;

public class AccountFragment extends Fragment {

    private static final String PREF_USER_NAME = "username";
    public static Uri uri_to_save = null;
    public static boolean deletePic = false;
    public static boolean profilePicinDb = false;
    private View view;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioGroup editGender;
    private RadioButton buttonM;
    private RadioButton buttonF;
    private Button saveButton;
    private Button logOutButton;
    private Button changePasswordButton;
    private Profile dbProfile;
    private String school;
    /**
     * Watcher for any modifications of the text in the fields of the profile
     */
    private final TextWatcher SaveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            Profile local = getProfileFromUI();
            Boolean allTheSame = local.equals(dbProfile);

            if (allTheSame || !EmailValidator.isValid(local.getEmail())) {
                saveButton.setEnabled(false);
            } else {
                saveButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private ImageView editProfilePicture;
    private FloatingActionButton changePictureButton;
    private AccountUtils accountUtils;

    private UserManager userManager;

    private ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                            editProfilePicture.setImageBitmap(imageBitmap);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageBitmap, "Title", null);
                            uri_to_save = Uri.parse(path);
                            deletePic = false;
                            saveButton.setEnabled(true);
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri selectedImage = result.getData().getData();
                            Bitmap thumbnail = null;
                            try {
                                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } catch (Exception ignored) {
                            }
                            editProfilePicture.setImageBitmap(thumbnail);
                            uri_to_save = selectedImage;
                            deletePic = false;
                            saveButton.setEnabled(true);
                        }
                    }
                }
            });
    /**
     * Watcher for any modifications of the gender button that is checked
     */
    private RadioGroup.OnCheckedChangeListener SaveProfileCheckWatcher = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            Profile local = getProfileFromUI();
            Boolean allTheSame = local.equals(dbProfile);

            if (allTheSame || !EmailValidator.isValid(local.getEmail())) {
                saveButton.setEnabled(false);
            } else {
                saveButton.setEnabled(true);
            }

        }

    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        this.view = view;
        accountUtils = new AccountUtils(getActivity(), activityResultLauncherGallery, activityResultLauncherCamera, view);

        userManager = new UserManager(getActivity().getApplicationContext());

        editFirstName = view.findViewById(R.id.userProfileFirstName);
        editLastName = view.findViewById(R.id.userProfileLastName);
        editEmail = view.findViewById(R.id.userProfileEmail);
        editGender = view.findViewById(R.id.userProfileRadioG);
        buttonM = view.findViewById(R.id.userProfileGenderM);
        buttonF = view.findViewById(R.id.userProfileGenderF);

        logOutButton = view.findViewById(R.id.userProfilelogOutButton);
        saveButton = view.findViewById(R.id.userProfileSaveButton);
        changePasswordButton = view.findViewById(R.id.userProfileChangePasswordButton);

        saveButton.setEnabled(false);

        editProfilePicture = view.findViewById(R.id.userProfileImage);
        editProfilePicture.setImageBitmap(null);
        deletePic = false;
        profilePicinDb = false;
        uri_to_save = null;

        editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
        changePictureButton = view.findViewById(R.id.userProfileChangePhotoButton);

        Button wallet_button = view.findViewById(R.id.wallet_button);

        //Listener to Buttons:
        wallet_button.setOnClickListener(v -> {
            goToWalletFragment();
        });
        logOutButton.setOnClickListener(v -> {
            logUserOut();
        });
        changePictureButton.setOnClickListener(v -> {
            accountUtils.showImagePickDialog();
        });
        changePasswordButton.setOnClickListener(v -> {
            goToChangePasswordActivity();
        });

        loadProfileFieldsDB();
        loadProfilePictureDB();

        return view;
    }


    private void loadProfilePictureDB() {
        //Load user profile picture from database
        String pathString = "profilePicture/" + Auth.getUid() + "/profile.jpg";
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            fileRef.getFile(localFile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            editProfilePicture.setImageBitmap(bitmap);
                            profilePicinDb = true;

                        } else {
                            profilePicinDb = false;
                            editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
                        }
                    });

        } catch (Exception ignored) {
        }
    }


    private void loadProfileFieldsDB() {
        //Load user profile fields from database
        DocumentReference docRef = Database.getCollection("users")
                .document(Auth.getUid());

        docRef.get().addOnSuccessListener(
                result -> {

                    dbProfile = result.toObject(Profile.class);
                    displayUIFromDB(dbProfile);

                    editFirstName.addTextChangedListener(SaveProfileWatcher);
                    editLastName.addTextChangedListener(SaveProfileWatcher);
                    editEmail.addTextChangedListener(SaveProfileWatcher);
                    editGender.setOnCheckedChangeListener(SaveProfileCheckWatcher);

                    addListenerToSaveButton();


                }
        );
    }


    /**
     * Only for The camera
     *
     * @param requestCode  The code of the request
     * @param permissions  The permissions that are given
     * @param grantResults The result of the permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AccountUtils.CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accountUtils.openCamera();
            } else {
                Toast.makeText(getActivity(), "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Logs the user out of the app.
     */
    private void logUserOut() {
        // delete token for messaging part
        DocumentReference documentReference =
                Database.getCollection(Constants.KEY_COLLECTION_USERS).document(Auth.getUid());

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            Auth.signOut();
            userManager.clear();
            resetSharedPref();
            Intent intent = new Intent(getActivity(), LogInActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });
    }

    private void resetSharedPref() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(PREF_USER_NAME, "");
        editor.apply();
    }

    /**
     * Go to change password Activity
     */
    private void goToChangePasswordActivity() {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Go to wallet fragment
     */
    private void goToWalletFragment() {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }


    /**
     * Display to the UI the profile previously fetched from the database
     *
     * @param userInDB Profile in Database
     */
    private void displayUIFromDB(Profile userInDB) {

        String dbFirstName = userInDB.getFirstName();
        String dbLastName = userInDB.getLastName();
        String dbEmail = userInDB.getEmail();
        String dbGender = userInDB.getGender();
        school = userInDB.getSchool();

        editFirstName.setText(dbFirstName);
        editLastName.setText(dbLastName);
        editEmail.setText(dbEmail);
        RadioButton selectButton = dbGender.equals("Male") ? buttonM : buttonF;
        selectButton.setChecked(true);

        // put names on userManager
        userManager.putString(Constants.KEY_FIRSTNAME, dbProfile.getFirstName());
        userManager.putString(Constants.KEY_LASTNAME, dbProfile.getLastName());
        userManager.putString(Constants.KEY_SENDER_NAME, dbProfile.getFirstName() + " " + dbProfile.getLastName());
    }

    /**
     * Take data present in the UI and turn it into a Profile class
     *
     * @return Profile
     */
    private Profile getProfileFromUI() {

        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        int selectedGender = editGender.getCheckedRadioButtonId();
        RadioButton selectedButton = view.findViewById(selectedGender);
        String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

        return new Profile(firstName, lastName, email, gender, school);

    }

    /**
     * Add a listener to the button Save
     */
    private void addListenerToSaveButton() {

        saveButton.setOnClickListener(v -> {

            Profile toUpdateUser = getProfileFromUI();

            if (EmailValidator.isValid(toUpdateUser.getEmail())) {
                saveUserProperties(toUpdateUser);
            }
            saveButton.setEnabled(false);

        });

    }

    /**
     * Save updated user's profile on the  database
     *
     * @param toUpdateUser the updated profile
     */
    private void saveUserProperties(Profile toUpdateUser) {

        Database.getCollection("users").document(Auth.getUid()).set(toUpdateUser)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                dbProfile.setFirstName(toUpdateUser.getFirstName());
                                dbProfile.setLastName(toUpdateUser.getLastName());
                                dbProfile.setEmail(toUpdateUser.getEmail());
                                dbProfile.setGender(toUpdateUser.getGender());

                                Auth.updateEmail(toUpdateUser.getEmail()).addOnCompleteListener(
                                        task2 -> {
                                            if (task2.isSuccessful()) {
                                                dbProfile.setEmail(toUpdateUser.getEmail());
                                                Toast.makeText(getActivity(), "Profile's update succeeded.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Profile's update failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );

                                String pathString = "profilePicture/" + Auth.getUid() + "/" + "profile.jpg";
                                StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);

                                if (deletePic && uri_to_save == null && profilePicinDb) {
                                    fileRef.delete()
                                            .addOnSuccessListener(taskSnapshot -> {
                                                Toast.makeText(getActivity(), "Profile Pic Deleted", Toast.LENGTH_SHORT).show();
                                                uri_to_save = null;
                                                deletePic = false;
                                                profilePicinDb = false;
                                            }).addOnFailureListener(exception -> Toast.makeText(getActivity(), "Failed to Delete Profile Pic", Toast.LENGTH_SHORT).show());
                                }

                                if (uri_to_save != null && deletePic == false) {
                                    fileRef.putFile(uri_to_save)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                Toast.makeText(getActivity(), "Profile Pic Updated", Toast.LENGTH_SHORT).show();
                                                uri_to_save = null;
                                                deletePic = false;
                                                profilePicinDb = true;
                                            }).addOnFailureListener(exception -> Toast.makeText(getActivity(), "Failed to update Profile Pic", Toast.LENGTH_SHORT).show());
                                }

                            } else {
                                Toast.makeText(getActivity(), "Profile's update failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }



}