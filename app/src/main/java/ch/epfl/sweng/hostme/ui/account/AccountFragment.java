package ch.epfl.sweng.hostme.ui.account;

import static ch.epfl.sweng.hostme.utils.Constants.CAMERA_PERM_CODE;
import static ch.epfl.sweng.hostme.utils.Constants.DATA;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_COLLECTION_USERS;
import static ch.epfl.sweng.hostme.utils.Constants.KEY_FCM_TOKEN;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import ch.epfl.sweng.hostme.LogInActivity;
import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.database.Database;
import ch.epfl.sweng.hostme.database.Storage;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.EmailValidator;
import ch.epfl.sweng.hostme.utils.Profile;
import ch.epfl.sweng.hostme.utils.UserManager;
import ch.epfl.sweng.hostme.wallet.WalletActivity;

public class AccountFragment extends Fragment {

    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    private static final String PROFILE_SUCCEED = "Profile's update succeeded";
    private static final String PROFILE_FAILED = "Profile's update failed";
    private static final String DELETE_SUCCEED = "Profile picture deleted";
    private static final String DELETE_FAILED = "Failed to delete profile picture";
    private static final String UPDATE_SUCCEED = "Profile picture updated";
    private static final String UPDATE_FAILED = "Failed to update profile picture";
    private static final String PERMISSION_REQUIRED = "Camera permission is required to use camera";
    public static Uri uri_to_save = null;
    public static boolean deletePic = false;
    public static boolean profilePicInDB = false;
    private View view;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioGroup editGender;
    private RadioButton buttonM;
    private RadioButton buttonF;
    private Button saveButton;
    private Profile dbProfile;
    private String school;
    private ImageView editProfilePicture;
    private AccountUtils accountUtils;
    private UserManager userManager;

    private final ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get(DATA);
                        editProfilePicture.setImageBitmap(imageBitmap);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), imageBitmap, "Title", null);
                        uri_to_save = Uri.parse(path);
                        deletePic = false;
                        saveButton.setEnabled(true);
                    }
                }
            }
        });

    private final ActivityResultLauncher<Intent> activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                    } catch (Exception ignored) {
                    }
                    editProfilePicture.setImageBitmap(thumbnail);
                    uri_to_save = selectedImage;
                    deletePic = false;
                    saveButton.setEnabled(true);
                }
            }
        });

    /**
     * Watcher for any modifications of the text in the fields of the profile
     */
    private final TextWatcher saveProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkIfProfileIsModified();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    /**
     * Watcher for any modifications of the gender button that is checked
     */
    private final RadioGroup.OnCheckedChangeListener saveProfileCheckWatcher = (group, checkedId) -> checkIfProfileIsModified();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        this.view = view;
        this.accountUtils = new AccountUtils(this, activityResultLauncherGallery, activityResultLauncherCamera, view);

        this.userManager = new UserManager(requireContext());
        this.editFirstName = view.findViewById(R.id.user_profile_first_name);
        this.editLastName = view.findViewById(R.id.user_profile_last_name);
        this.editEmail = view.findViewById(R.id.user_profile_email);
        this.editGender = view.findViewById(R.id.user_profile_radio_g);
        this.buttonM = view.findViewById(R.id.user_profile_gender_m);
        this.buttonF = view.findViewById(R.id.user_profile_gender_f);
        this.saveButton = view.findViewById(R.id.user_profile_save_button);
        this.saveButton.setEnabled(false);
        this.editProfilePicture = view.findViewById(R.id.user_profile_image);
        this.editProfilePicture.setImageBitmap(null);
        this.editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);

        deletePic = false;
        profilePicInDB = false;
        uri_to_save = null;

        Button logOutButton = view.findViewById(R.id.user_profile_log_out_button);
        logOutButton.setOnClickListener(v -> this.logUserOut());

        Button changePasswordButton = view.findViewById(R.id.user_profile_change_password_button);
        changePasswordButton.setOnClickListener(v -> this.goToChangePasswordActivity());

        FloatingActionButton changePictureButton = view.findViewById(R.id.user_profile_change_photo_button);
        changePictureButton.setOnClickListener(v -> accountUtils.showImagePickDialog());

        Button wallet_button = view.findViewById(R.id.wallet_button);
        wallet_button.setOnClickListener(v -> this.goToWalletFragment());

        this.loadProfileFieldsDB();
        this.loadProfilePictureDB();

        return view;
    }

    private void loadProfilePictureDB() {
        String pathString = "profilePicture/" + Auth.getUid() + "/profile.jpg";
        StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);
        try {
            final File localFile = File.createTempFile("profile", "jpg");
            fileRef.getFile(localFile)
            .addOnSuccessListener(result -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                this.editProfilePicture.setImageBitmap(bitmap);
                profilePicInDB = true;
            })
            .addOnFailureListener(error -> {
                this.editProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
                profilePicInDB = false;
            });
        } catch (Exception ignored) {
        }
    }

    private void loadProfileFieldsDB() {
        DocumentReference docRef = Database.getCollection(KEY_COLLECTION_USERS).document(Auth.getUid());
        docRef.get().addOnSuccessListener(result -> {
            dbProfile = result.toObject(Profile.class);
            displayUIFromDB(Objects.requireNonNull(dbProfile));
            editFirstName.addTextChangedListener(saveProfileWatcher);
            editLastName.addTextChangedListener(saveProfileWatcher);
            editEmail.addTextChangedListener(saveProfileWatcher);
            editGender.setOnCheckedChangeListener(saveProfileCheckWatcher);
            addListenerToSaveButton();
        });
    }

    /**
     * Only for The camera
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accountUtils.openCamera();
            } else {
                Toast.makeText(this.requireContext(), PERMISSION_REQUIRED, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Logs the user out of the app.
     */
    private void logUserOut() {
        DocumentReference documentReference = Database.getCollection(KEY_COLLECTION_USERS).document(Auth.getUid());
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            Auth.signOut();
            userManager.clear();
            Intent intent = new Intent(requireActivity(), LogInActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        });
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
        this.school = userInDB.getSchool();
        this.editFirstName.setText(dbFirstName);
        this.editLastName.setText(dbLastName);
        this.editEmail.setText(dbEmail);
        RadioButton selectButton = dbGender.equals(MALE) ? buttonM : buttonF;
        selectButton.setChecked(true);
        this.userManager.putString(Constants.KEY_FIRSTNAME, dbProfile.getFirstName());
        this.userManager.putString(Constants.KEY_LASTNAME, dbProfile.getLastName());
        this.userManager.putString(Constants.KEY_SENDER_NAME, dbProfile.getFirstName() + " " + dbProfile.getLastName());
    }

    /**
     * Take data present in the UI and turn it into a Profile class
     *
     * @return Profile
     */
    private Profile getProfileFromUI() {
        String firstName = this.editFirstName.getText().toString().trim();
        String lastName = this.editLastName.getText().toString().trim();
        String email = this.editEmail.getText().toString().trim();
        int selectedGender = this.editGender.getCheckedRadioButtonId();
        RadioButton selectedButton = this.view.findViewById(selectedGender);
        String gender = selectedButton.getText().toString().equals(MALE) ? MALE : FEMALE;
        return new Profile(firstName, lastName, email, gender, this.school);
    }

    private void checkIfProfileIsModified() {
        Profile local = getProfileFromUI();
        boolean allTheSame = local.equals(this.dbProfile);
        this.saveButton.setEnabled(!allTheSame && EmailValidator.isValid(local.getEmail()));
    }

    /**
     * Add a listener to the button Save
     */
    private void addListenerToSaveButton() {
        this.saveButton.setOnClickListener(v -> {
            Profile toUpdateUser = getProfileFromUI();
            if (EmailValidator.isValid(toUpdateUser.getEmail())) {
                saveUserProperties(toUpdateUser);
            }
            this.saveButton.setEnabled(false);
        });
    }

    /**
     * Save updated user's profile on the  database
     *
     * @param toUpdateUser the updated profile
     */
    private void saveUserProperties(Profile toUpdateUser) {
        Database.getCollection(KEY_COLLECTION_USERS).document(Auth.getUid()).set(toUpdateUser)
        .addOnSuccessListener(result -> {
            dbProfile.setFirstName(toUpdateUser.getFirstName());
            dbProfile.setLastName(toUpdateUser.getLastName());
            dbProfile.setEmail(toUpdateUser.getEmail());
            dbProfile.setGender(toUpdateUser.getGender());

            Auth.updateEmail(toUpdateUser.getEmail())
            .addOnSuccessListener(result2 -> {
                dbProfile.setEmail(toUpdateUser.getEmail());
                Toast.makeText(requireContext(), PROFILE_SUCCEED, Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(error2 -> Toast.makeText(requireContext(), PROFILE_FAILED, Toast.LENGTH_SHORT).show());

            String pathString = "profilePicture/" + Auth.getUid() + "/" + "profile.jpg";
            StorageReference fileRef = Storage.getStorageReferenceByChild(pathString);

            if (deletePic && uri_to_save == null && profilePicInDB) {
                fileRef.delete()
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(requireContext(), DELETE_SUCCEED, Toast.LENGTH_SHORT).show();
                    uri_to_save = null;
                    deletePic = false;
                    profilePicInDB = false;
                }).addOnFailureListener(exception -> Toast.makeText(requireContext(), DELETE_FAILED, Toast.LENGTH_SHORT).show());
            }

            if (uri_to_save != null && !deletePic) {
                fileRef.putFile(uri_to_save)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(requireContext(), UPDATE_SUCCEED, Toast.LENGTH_SHORT).show();
                    uri_to_save = null;
                    deletePic = false;
                    profilePicInDB = true;
                }).addOnFailureListener(exception -> Toast.makeText(requireContext(), UPDATE_FAILED, Toast.LENGTH_SHORT).show());
            }
        }).addOnFailureListener(error ->Toast.makeText(requireContext(), PROFILE_FAILED, Toast.LENGTH_SHORT).show());
    }

    /**
     * Go to change password Activity
     */
    private void goToChangePasswordActivity() {
        Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }

    /**
     * Go to wallet fragment
     */
    private void goToWalletFragment() {
        Intent intent = new Intent(requireActivity(), WalletActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}