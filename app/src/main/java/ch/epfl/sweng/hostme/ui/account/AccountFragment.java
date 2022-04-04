package ch.epfl.sweng.hostme.ui.account;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.userCreation.MainActivity;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.EmailValidator;
import ch.epfl.sweng.hostme.utils.Profile;
import ch.epfl.sweng.hostme.wallet.WalletActivity;

public class AccountFragment extends Fragment {

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
    private String dbFirstName;
    private String dbLastName;
    private String dbEmail;
    private String dbGender;

    private ImageView editProfilePicture;
    private FloatingActionButton changePictureButton;
    private ActivityResultLauncher<Intent> activityResultLauncherGallery;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private static final int CAMERA_PERM_CODE = 101;


    /**
     * Watcher for any modifications of the text in the fields of the profile
     */
    private final TextWatcher SaveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            int selectedGender = editGender.getCheckedRadioButtonId();
            RadioButton selectedButton = view.findViewById(selectedGender);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    && lastName.equals(dbLastName)
                    && firstName.equals(dbFirstName)
                    && email.equals(dbEmail)
                    && gender.equals(dbGender);


            if (allTheSame || !EmailValidator.isValid(email)) {
                saveButton.setEnabled(false);
            } else {
                saveButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    /**
     * Watcher for any modifications of the gender button that is checked
     */
    private RadioGroup.OnCheckedChangeListener SaveProfileCheckWatcher = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            String firstName = editFirstName.getText().toString().trim();
            String lastName = editLastName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            RadioButton selectedButton = view.findViewById(checkedId);
            String gender = selectedButton.getText().toString().equals("Male") ? "Male" : "Female";

            Boolean allTheSame = firstName.equals(dbFirstName)
                    && lastName.equals(dbLastName)
                    && firstName.equals(dbFirstName)
                    && email.equals(dbEmail)
                    && gender.equals(dbGender);


            if (allTheSame || !EmailValidator.isValid(email)) {
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
        changePictureButton = view.findViewById(R.id.userProfileChangePhotoButton);

        Button wallet_button = view.findViewById(R.id.wallet_button);
        wallet_button.setOnClickListener(v -> {
            goToWalletFragment();
        });

        //Image From Camera and Gallery
        ContentResolver cont = this.getActivity().getContentResolver();
        activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                                editProfilePicture.setImageBitmap(imageBitmap);
                            }
                        }
                    }
                });

        activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {

                                Uri selectedImage = result.getData().getData();
                                String[] filePath = {MediaStore.Images.Media.DATA};
                                Cursor c = cont.query(selectedImage, filePath, null, null, null);
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePath[0]);
                                String picturePath = c.getString(columnIndex);
                                c.close();
                                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                                editProfilePicture.setImageBitmap(thumbnail);

                            }
                        }
                    }
                });

        DocumentReference docRef = Database.getCollection("users")
                .document(Auth.getUid());

        docRef.get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        Profile userInDB = task.getResult().toObject(Profile.class);
                        displayUIFromDB(userInDB);

                        editFirstName.addTextChangedListener(SaveProfileWatcher);
                        editLastName.addTextChangedListener(SaveProfileWatcher);
                        editEmail.addTextChangedListener(SaveProfileWatcher);
                        editGender.setOnCheckedChangeListener(SaveProfileCheckWatcher);

                        addListenerToSaveButton();
                        addListenerToChangePasswordButton();
                        addListenerToChangePicture();

                        logOutButton.setOnClickListener(v -> {
                            logUserOut();
                        });

                    }
                }
        );


        return view;
    }


    /**
     * Add a listener to the button Add/Change Picture
     */
    private void addListenerToChangePicture() {

        changePictureButton.setOnClickListener(v -> {
            showImagePickDialog();
        });
    }


    /**
     * Showing dialog where you can take select to take image from either
     * the Gallery or the Camera
     */
    private void showImagePickDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        pickFromCamera();
                    } else if (which == 1) {
                        pickFromGallery();
                    }
                }
        );
        builder.create().show();
    }


    /**
     * Pick user Profile image from Gallery
     */
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        activityResultLauncherGallery.launch(galleryIntent);
    }

    /**
     * Pick user Profile image from Camera
     */
    private void pickFromCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
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
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Open Camera
     */
    private void openCamera() {
        Toast.makeText(getActivity(), "Camera Open Request", Toast.LENGTH_SHORT).show();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncherCamera.launch(cameraIntent);
    }

    /**
     * Display to the UI the profile previously fetched from the database
     *
     * @param userInDB Profile in Database
     */
    private void displayUIFromDB(Profile userInDB) {

        dbFirstName = userInDB.getFirstName();
        dbLastName = userInDB.getLastName();
        dbEmail = userInDB.getEmail();
        dbGender = userInDB.getGender();

        editFirstName.setText(dbFirstName);
        editLastName.setText(dbLastName);
        editEmail.setText(dbEmail);
        RadioButton selectButton = dbGender.equals("Male") ? buttonM : buttonF;
        selectButton.setChecked(true);
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

        return new Profile(firstName, lastName, email, gender);

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
     * Add a listener to the change password button
     */
    private void addListenerToChangePasswordButton() {

        changePasswordButton.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);

        });
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
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
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
                                dbFirstName = toUpdateUser.getFirstName();
                                dbLastName = toUpdateUser.getLastName();
                                dbEmail = toUpdateUser.getEmail();
                                dbGender = toUpdateUser.getGender();

                                Toast.makeText(getActivity(), "Profile's update succeeded.",
                                        Toast.LENGTH_SHORT).show();

                                Auth.updateEmail(toUpdateUser.getEmail()).addOnCompleteListener(
                                        task2 -> {
                                            if (task2.isSuccessful()) {
                                                dbEmail = toUpdateUser.getEmail();
                                                Toast.makeText(getActivity(), "Email's update succeeded.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "Email's update failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            } else {
                                Toast.makeText(getActivity(), "Profile's update failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    /**
     * Go to wallet fragment
     */
    private void goToWalletFragment() {
        Intent intent = new Intent(getActivity(), WalletActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

}