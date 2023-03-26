package com.bitebybyte.backend.database;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.AbstractContent;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.Recipe;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PostService implements OnSuccessListener, OnFailureListener {
        FirebaseFirestore db;
        FirebaseStorage dbStore;
        FirebaseAuth auth;

        public PostService() {
                db = FirebaseFirestore.getInstance();
                dbStore = FirebaseStorage.getInstance();
                auth = FirebaseAuth.getInstance();
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

        public void saveImageToDatabase(Uri imageUri, ImageView imageView, String postId) {
                StorageReference storageReference = dbStore.getReference("images/" + postId);
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

        public void loadImage(ImageView imageView, String postId) {
                StorageReference storageReference = dbStore.getReference("images/" + postId);
                System.out.println(storageReference);
                storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri ->
                                Glide.with(imageView.getContext())
                                        .load(uri)
                                        .into(imageView))
                        .addOnFailureListener(exception ->
                                Glide.with(imageView.getContext())
                                        .load("https://firebasestorage.googleapis.com/v0/b/bitebybyte-ac8f2.appspot.com/o/default_Image%20(1).jpg?alt=media&token=db6bf7f0-6c34-4f9f-892c-e8cc031f23c8")
                                        .into(imageView));
        }

        public String createPostWithRecipe(String idOwner, String content, String title,
                                         String images, List<String> labels,
                        String methods, String ingredients, int preparationTime) {
                Recipe recipe = createRecipe(methods, ingredients, preparationTime);
                return createPost(idOwner, content, title, images, labels, recipe);
        }

        public String createPost(String idOwner, String content, String title,
                                     String images, List<String> labels, Recipe recipe) {
                System.out.println("CreatePost IN");
                FeedPost post = new FeedPost(idOwner, content, title,
                                images, labels, recipe);

                this.saveToDatabase(post);
                System.out.println("CreatePost OUT");

                return post.getPostId();
        }

        public Recipe createRecipe(String methods, String ingredients, int preparationTime) {
                return new Recipe(methods, ingredients, preparationTime);
        }

        public void updateLikes(AbstractContent post) {
                // update the likes amount
                DocumentReference postRef = db.collection("posts").document(post.getPostId());

                if (post.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                        postRef
                                .update("likes", (post.getLikes().remove(auth.getCurrentUser().getUid())))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                                System.out.println("DocumentSnapshot successfully updated!");
                                                post.getLikes().remove(auth.getCurrentUser().getUid());
                                        }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                                System.out.println("Error updating document:" + e);
                                        }
                                });
                } else {
                        postRef
                                .update("likes", (post.getLikes().put(auth.getCurrentUser().getUid(), true)))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                                System.out.println("DocumentSnapshot successfully updated!");
                                                post.getLikes().put(auth.getCurrentUser().getUid(), true);
                                        }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                                System.out.println("Error updating document:" + e);
                                        }
                                });
                }

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
