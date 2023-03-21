package com.bitebybyte;

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
import com.google.common.util.concurrent.ListenableFuture;
import android.net.Uri;

public class CameraActivity extends AppCompatActivity {
    //Consts
    private static final String TAG = "CameraXApp";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
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
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA}, CAMERA);
        }


        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        viewBinding.searchGallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGallary();
            }
        });

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        if (imageCapture == null) {
            return;
        }

        // Create time stamped name and MediaStore entry.
        String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        // Create output options object which contains file + metadata
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions
                .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        String msg = "Photo capture succeeded: ${output.savedUri}";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                        selectedImageUri = outputFileResults.getSavedUri();
                        terminate();
                    }
                    @Override
                    public void onError(ImageCaptureException exc) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc);
                    }
                }
        );
    }

    private void searchGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        // Preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(viewBinding.viewFinder.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();

        // Select back camera as a default
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll();

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

        } catch(Exception exc) {
            Log.e(TAG, "Use case binding failed", exc);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void terminate() {
        Intent resultIntent = new Intent();

        if (selectedImageUri != null) {
            resultIntent.putExtra(URI_ID_CODE, selectedImageUri.toString());
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            setResult(Activity.RESULT_CANCELED, resultIntent);
        }

        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
            super.onActivityResult(requestCode, resultCode, data);
            terminate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
