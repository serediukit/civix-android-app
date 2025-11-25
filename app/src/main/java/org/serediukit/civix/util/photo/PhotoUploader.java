package org.serediukit.civix.util.photo;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PhotoUploader {
    private static final String TAG = "PhotoUploader";
    private static final String REPORTS_FOLDER = "reports";

    private final FirebaseStorage storage;

    public PhotoUploader() {
        this.storage = FirebaseStorage.getInstance();
    }

    public interface UploadCallback {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
        void onProgress(int progress);
    }

    public void uploadPhoto(Uri photoUri, UploadCallback callback) {
        if (photoUri == null) {
            Log.e(TAG, "Photo URI is null");
            callback.onFailure(new IllegalArgumentException("Photo URI cannot be null"));
            return;
        }

        Log.d(TAG, "Starting upload for URI: " + photoUri);

        // Generate unique filename
        String filename = UUID.randomUUID().toString() + ".jpg";
        StorageReference photoRef = storage.getReference()
                .child(REPORTS_FOLDER)
                .child(filename);

        Log.d(TAG, "Upload path: " + REPORTS_FOLDER + "/" + filename);

        // Upload file
        UploadTask uploadTask = photoRef.putFile(photoUri);

        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            callback.onProgress((int) progress);
            Log.d(TAG, "Upload progress: " + progress + "%");
        }).addOnSuccessListener(taskSnapshot -> {
            // Get download URL
            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();
                Log.d(TAG, "Upload successful. URL: " + downloadUrl);
                callback.onSuccess(downloadUrl);
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to get download URL", e);
                callback.onFailure(e);
            });
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Upload failed", e);
            callback.onFailure(e);
        });
    }

    public void deletePhoto(String photoUrl, DeleteCallback callback) {
        if (photoUrl == null || photoUrl.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("Photo URL cannot be null or empty"));
            return;
        }

        try {
            StorageReference photoRef = storage.getReferenceFromUrl(photoUrl);
            photoRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Photo deleted successfully");
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to delete photo", e);
                        callback.onFailure(e);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Invalid photo URL", e);
            callback.onFailure(e);
        }
    }

    public interface DeleteCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
