package com.bitebybyte.backend.database;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.app.usage.StorageStats;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.Ingredient;
import com.bitebybyte.backend.local.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.media.Image;
import androidx.camera.core.ImageCapture;
import android.provider.MediaStore;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;

import java.util.List;
public class PostService implements OnSuccessListener, OnFailureListener {
        FirebaseFirestore db;
        FirebaseStorage dbStore;

        public PostService() {
                db = FirebaseFirestore.getInstance();
                dbStore = FirebaseStorage.getInstance();
        }

        public void saveToDatabase(FeedPost post) {
                System.out.println("SaveToDatabase IN");
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("posts")
                                .document(post.getPostId()).set(post)
                                .addOnSuccessListener(this)
                                .addOnFailureListener(this);

                System.out.println("SavetoDatabase OUT");
        }

        public void saveImageToDatabase(Uri imageUri, ImageView imageView) {
                StorageReference storageReference = dbStore.getReference("images/image");
                try {
                        storageReference.putFile(imageUri)
                                .addOnSuccessListener(this)
                                .addOnFailureListener(this);
                } catch (SecurityException e) {
                        imageView.setDrawingCacheEnabled(true);
                        imageView.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnFailureListener(this)
                                .addOnSuccessListener(this);
                } catch (Exception e) {
                        System.out.println("Error Uploading photo");
                }
        }

        public void createPostWithRecipe(String idOwner, String content, String title,
                                         String images, List<String> labels,
                        String methods, String ingredients, int preparationTime) {
                System.out.println("CreatePostWithRecipe IN");
                Recipe recipe = createRecipe(methods, ingredients, preparationTime);
                createPost(idOwner, content, title, images, labels, recipe);
                System.out.println("CreatePostWithRecipe OUT");
        }

        public void createPost(String idOwner, String content, String title,
                               String images, List<String> labels, Recipe recipe) {
                System.out.println("CreatePost IN");
                FeedPost post = new FeedPost(idOwner, content, title,
                                images, labels, recipe);

                this.saveToDatabase(post);
                System.out.println("CreatePost OUT");
        }

        public Recipe createRecipe(String methods, String ingredients, int preparationTime) {
                return new Recipe(methods, ingredients, preparationTime);
        }

        @Override
        public void onSuccess(Object o) {
                System.out.println("Successfully added");
        }

        @Override
        public void onFailure(@NonNull Exception e) {
                System.out.println("Failed add" + e);
        }
}
