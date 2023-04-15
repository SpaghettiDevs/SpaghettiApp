package com.bitebybyte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;

import androidx.camera.core.ImageCapture;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.View;
import android.widget.Toast;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.core.Preview;
import androidx.camera.core.CameraSelector;

import android.util.Log;

import androidx.camera.core.ImageCaptureException;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.bitebybyte.databinding.ActivityCameraBinding;
import com.bitebybyte.databinding.FragmentCreateBinding;
import com.google.common.util.concurrent.ListenableFuture;

import android.net.Uri;


/**
 * This class is responsible for the camera activity.
 * It is responsible for taking a picture and returning the URI of the picture.
 * It is also responsible for searching the gallery and returning the URI of the picture.
 * It is also responsible for requesting the camera permission.
 */
public class CameraActivity extends AppCompatActivity {
    //Constants used for the camera
    private static final String TAG = "CameraXApp";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int CAMERA = 100;
    private static final int PICK_IMAGE = 200;

    public static final String URI_ID_CODE = "URI_ID_1001";

    //Must be initialized
    private ActivityCameraBinding viewBinding;
    private ExecutorService cameraExecutor;

    //Might not be initialized
    private ImageCapture imageCapture = null;

    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // Request camera permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            startCamera();
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA);
        }


        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        viewBinding.searchGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGallery();
            }
        });

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Takes a photo using the camera, creates an output file, and saves the picture to the gallery.
     *
     * @modifies selectedImageUri - the URI of the saved image
     */
    private void takePhoto() {
        // Check if image capture is initialized
        if (imageCapture == null) {
            Toast.makeText(getApplicationContext(), "Image capture is not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a time-stamped name and MediaStore entry
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        // Get the ContentResolver for the application context
        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        // Create output options object which contains file + metadata
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions
                .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // Display success message
                        String msg = "Photo capture succeeded.";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);

                        // Save the URI of the saved image
                        selectedImageUri = outputFileResults.getSavedUri();

                        // Terminate the photo capture process
                        terminate();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        // Display error message
                        Log.e(TAG, "Photo capture failed: ${exc.message}", e);
                    }
                }
        );
    }

    /**
     * This method is responsible for opening the device's gallery to select an image.
     */
    private void searchGallery() {
        // Create an intent to pick an image from the gallery
        Intent intent = new Intent();
        intent.setType("image/*"); // Set the type of file to be picked as an image
        intent.setAction(Intent.ACTION_GET_CONTENT); // Set the action to get the content
        // Start an activity to pick an image and wait for the result
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    /**
     * Binds preview and image capture use cases to the camera and sets the surface provider for the preview.
     *
     * @param cameraProvider ProcessCameraProvider instance to bind use cases to.
     */
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        // Initialize preview use case
        Preview preview = new Preview.Builder().build();

        // Set surface provider for preview
        preview.setSurfaceProvider(viewBinding.viewFinder.getSurfaceProvider());

        // Initialize image capture use case
        imageCapture = new ImageCapture.Builder().build();

        // Set camera selector to use the back camera as default
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            // Unbind any existing use cases before binding new ones
            cameraProvider.unbindAll();

            // Bind use cases to camera provider
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        } catch (Exception exc) {
            Log.e(TAG, "Use case binding failed", exc);
        }
    }

    /**
     * Starts the camera preview by binding it to a CameraProvider instance.
     */
    private void startCamera() {
        // Obtains a CameraProvider instance asynchronously.
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        // Attaches a listener to the future to be notified when the CameraProvider instance is available.
        cameraProviderFuture.addListener(() -> {
            try {
                // Obtains the CameraProvider instance from the future.
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Binds the camera preview to the CameraProvider instance.
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Finishes the activity and sets the result Intent with the selected image URI, if available.
     * If no image URI is selected, sets the result as canceled.
     */
    private void terminate() {
        // Creates a new Intent to hold the result data.
        Intent resultIntent = new Intent();

        // Adds the selected image URI to the result data, if available.
        if (selectedImageUri != null) {
            resultIntent.putExtra(URI_ID_CODE, selectedImageUri.toString());
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            // Sets the result as canceled if no image URI is selected.
            setResult(Activity.RESULT_CANCELED, resultIntent);
        }

        // Finishes the activity and returns to the calling activity.
        finish();
    }

    /**
     * Called when an activity launched from this activity exits, giving this activity the requestCode it started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the request code matches PICK_IMAGE, set the selectedImageUri and terminate the activity.
        if (requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
            terminate();
        }
    }

    /**
     * Called when the user has either granted or denied a permission requested by this activity.
     *
     * @param requestCode The request code passed to requestPermissions().
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions, where the array contains PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CAMERA) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            // If the camera permission is granted, start the camera.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                // If the camera permission is denied, display a toast message.
                Toast.makeText(getApplicationContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Called when the activity is about to be destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
