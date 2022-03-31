package ch.epfl.sweng.hostme.database;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {

    public static StorageReference getStorageReferenceByChild(String pathString) {
        return FirebaseStorage.getInstance().getReference().child(pathString);
    }

}
