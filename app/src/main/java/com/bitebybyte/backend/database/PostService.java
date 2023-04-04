package com.bitebybyte.backend.database;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bitebybyte.backend.local.AbstractContent;
import com.bitebybyte.backend.local.Comment;
import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.backend.local.Recipe;
import com.bitebybyte.ui.ServicableFragment;
import com.bitebybyte.ui.saved.ViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostService implements OnSuccessListener, OnFailureListener {
        FirebaseFirestore db;
        FirebaseStorage dbStore;
        FirebaseAuth auth;
        UserService userService;

        public PostService() {
                db = FirebaseFirestore.getInstance();
                dbStore = FirebaseStorage.getInstance();
                auth = FirebaseAuth.getInstance();
                userService = new UserService();
        }

        /**
         * Save post to database given a local object!
         *
         * @param post
         */
        public void saveToDatabase(FeedPost post) {
                db.collection("posts")
                                .document(post.getPostId()).set(post)
                                .addOnSuccessListener(unused -> {
                                        userService.updateMyPosts(post.getPostId());
                                        Log.d("Firebase", "Successfully added");
                                })
                                .addOnFailureListener(this);
        }

        /**
         * Delete a post from the database given the postId.
         *
         * @param postId
         */
        public void deletePost(String postId) {
                DocumentReference reference = db.collection("posts").document(postId);

                reference.get()
                        .addOnCompleteListener(task -> {
                                if(!task.getResult().exists()) {
                                        Log.d("Database",  "Post with id " + postId + " does not exist.");
                                        return;
                                }
                                reference.delete()
                                        .addOnSuccessListener(unused1 -> {
                                                userService.updateMyPosts(postId);
                                                Log.d("Database", "Successfully deleted post " + postId);
                                        })
                                        .addOnFailureListener(error -> {
                                                Log.d("Database", "Failed to delete post " + postId);
                                                Log.d("Database error", String.valueOf(error));
                                        });
                        })
                        .addOnFailureListener(e -> {
                                Log.d("Database",  "Error fetching " + postId + " does not exist.");
                        });
        }

        public void addComment(FeedPost post, String commentText) {
                String idOwner = userService.getUsername();
                Comment comment = new Comment(idOwner, commentText);
                post.getComments().add(comment);
                saveToDatabase(post);
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
                                .addOnSuccessListener(uri -> Glide.with(imageView.getContext())
                                                .load(uri)
                                                .into(imageView))
                                .addOnFailureListener(exception -> Glide.with(imageView.getContext())
                                                .load("https://firebasestorage.googleapis.com/v0/b/bitebybyte-ac8f2.appspot.com/o/default_Image%20(1).jpg?alt=media&token=db6bf7f0-6c34-4f9f-892c-e8cc031f23c8")
                                                .into(imageView));
        }

        public String dateFormat(Date date) {
                Date currentTime = Calendar.getInstance().getTime();
                long difference = currentTime.getTime() - date.getTime();

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = difference / daysInMilli;
                difference %= daysInMilli;

                long elapsedHours = difference / hoursInMilli;
                difference %= hoursInMilli;

                long elapsedMinutes = difference / minutesInMilli;
                difference %= minutesInMilli;

                long elapsedSeconds = difference / secondsInMilli;

                return "Posted: " + (elapsedDays > 0 ? elapsedDays + (elapsedDays == 1 ? " day" : " days") + " ago" :
                        elapsedHours > 0 ? elapsedHours + (elapsedHours == 1 ? " hour" : " hours") + " ago" :
                                elapsedMinutes > 0 ? elapsedMinutes + (elapsedMinutes == 1 ? " minute" : " minutes") + " ago" :
                                        elapsedSeconds + (elapsedSeconds == 1 ? " second" : " seconds") + " ago");
        }

        public String createPostWithRecipe(String idOwner, String content, String title,
                        String images, List<String> labels,
                        String methods, String ingredients, int preparationTime, String preparationTimeUnit) {
                Recipe recipe = createRecipe(methods, ingredients, preparationTime, preparationTimeUnit);
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

        public Recipe createRecipe(String methods, String ingredients, int preparationTime, String preparationTimeUnit) {
                return new Recipe(methods, ingredients, preparationTime, preparationTimeUnit);
        }

        /**
         * Updates the likes locally and in the Firebase.
         *
         * @param post
         */
        public void updateLikes(FeedPost post) {
                DocumentReference postRef = db.collection("posts").document(post.getPostId());
                if (post.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                        post.getLikes().remove(auth.getCurrentUser().getUid());
                        postRef
                                        .update("likes", post.getLikes())
                                        .addOnSuccessListener(this)
                                        .addOnFailureListener(this);
                } else {
                        post.getLikes().put(auth.getCurrentUser().getUid(), true);
                        postRef
                                        .update("likes", post.getLikes())
                                        .addOnSuccessListener(this)
                                        .addOnFailureListener(this);
                }
        }

        /**
         * Updates the likes of a comment locally and in the Firebase.
         *
         * @param comments
         * @param postId
         * @param index of comment
         */
        public void updateLikes(List<Comment> comments, String postId, int index) {
                DocumentReference postRef = db.collection("posts").document(postId);
                Comment comment = comments.get(index);
                if (comment.getLikes().containsKey(auth.getCurrentUser().getUid())) {
                        comment.getLikes().remove(auth.getCurrentUser().getUid());
                        postRef
                                .update("comments", comments)
                                .addOnSuccessListener(this)
                                .addOnFailureListener(this);
                } else {
                        comment.getLikes().put(auth.getCurrentUser().getUid(), true);
                        postRef
                                .update("comments", comments)
                                .addOnSuccessListener(this)
                                .addOnFailureListener(this);
                }
        }

        /**
         * Checks if the current user has liked the post.
         * @param post The post to check.
         * @return True if the current user has liked the post, false otherwise.
         */
        public boolean hasLikedPost(AbstractContent post) {
                return post.getLikes().containsKey(auth.getCurrentUser().getUid());
        }

        @Override
        public void onSuccess(Object o) {
                Log.v("Firebase", "Successfully added");
        }

        @Override
        public void onFailure(@NonNull Exception e) {
                Log.v("Firebase", "Failed add" + e);
        }

        /**
         * Query Firebase given a postId to find a single document.
         *
         * @param postId
         * @param fragment callback
         */
        public void getPostById(String postId, ServicableFragment fragment) {
                DocumentReference postRef = db.collection("posts").document(postId);
                postRef.get().addOnSuccessListener(documentSnapshot -> {
                        Log.v("Firebase", "Post fetched successfully");
                        fragment.addDataToView(documentSnapshot.toObject(FeedPost.class));
                });
        }

        /**
         * Query Firebase given a postId to find a single document.
         *
         * @param postId
         * @param fragment callback
         * @param holder callback in package ui.saved ViewHolder
         */
        public void getPostById(String postId, ServicableFragment fragment, ViewHolder holder) {
                DocumentReference postRef = db.collection("posts").document(postId);
                postRef.get().addOnSuccessListener(documentSnapshot -> {
                        Log.v("Firebase", "Post fetched successfully");
                        fragment.addDataToView(documentSnapshot.toObject(FeedPost.class), holder);
                });
        }

        /**
         * Query Firebase to get all posts in descending order by time published.
         *
         * @param fragment callback
         */
        public void getAllPosts(ServicableFragment fragment) {
                db.collection("posts")
                                .orderBy("date", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                                List<FeedPost> posts = new ArrayList<>();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                        try {
                                                                posts.add(document.toObject(FeedPost.class));
                                                                Log.v("Firebase", "Successfully loaded document "
                                                                                + document.getId());
                                                        } catch (Exception e) {
                                                                Log.v("Firebase", "Failed to load document "
                                                                                + document.getId());
                                                        }
                                                }
                                                fragment.getListOfPosts(posts);
                                        } else {
                                                Log.v("Firebase", "Error getting documents: " + task.getException());
                                        }
                                });
        }
}
