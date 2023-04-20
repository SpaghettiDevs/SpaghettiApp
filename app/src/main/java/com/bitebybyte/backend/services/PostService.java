package com.bitebybyte.backend.services;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bitebybyte.ServiceablePostFragment;
import com.bitebybyte.backend.models.AbstractContent;
import com.bitebybyte.backend.models.Comment;
import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.backend.models.Recipe;
import com.bitebybyte.holders.AbstractViewHolder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
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
     * Save post to database given a local object.
     *
     * @param post     the post to save
     * @param location to which collection the post needs to be saved to
     */
    public void saveToDatabase(FeedPost post, String location) {
        db.collection(location)
                .document(post.getPostId()).set(post)
                .addOnSuccessListener(unused -> {
                    if (location.equals("posts")) {
                        userService.updateMyPosts(post.getPostId());
                    }
                    Log.d("Firebase", "Successfully added");
                })
                .addOnFailureListener(this);
    }

    /**
     * Save post to database given a local object.
     *
     * @param post     the post to save
     * @param location to which collection the post needs to be saved to
     */
    public void saveCommentToDatabase(FeedPost post, String location) {
        db.collection(location)
                .document(post.getPostId()).set(post)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Delete a post from the database given the postId.
     *
     * @param postId   the id of the post to delete
     * @param location which collection the post is in
     */
    public void deletePost(String postId, String location) {
        DocumentReference reference = db.collection(location).document(postId);
        if (postExists(reference, postId)) {
            // If the post exists, delete it
            reference.delete()
                    .addOnSuccessListener(unused1 -> {
                        // If the deletion is successful, update the user's posts and log a success
                        // message
                        if (location.equals("posts")) {
                            userService.updateMyPosts(postId);
                        }
                        Log.d("Database", "Successfully deleted post " + postId);
                    })
                    .addOnFailureListener(error -> {
                        // If the deletion fails, log an error message with the details of the error
                        Log.d("Database", "Failed to delete post " + postId);
                        Log.d("Database error", String.valueOf(error));
                    });
        } else {
            throw new IllegalArgumentException("post doesn't exist at reference location");
        }
    }

    /**
     * checks if a posts exist in firebase
     *
     * @param reference a {@link DocumentReference} object for the post
     * @param postId    the id of the post
     */
    private boolean postExists(DocumentReference reference, String postId) {
        Task<DocumentSnapshot> task = reference.get().addOnFailureListener(e -> {
            // If there is an error while checking for the post, log an error message with
            // the details of the error
            Log.d("Database", "Error fetching " + postId);
        });

        while (!task.isComplete()) {
        }

        if (!task.getResult().exists()) {
            // If the post does not exist, log a message
            Log.d("Database", "Post with id " + postId + " does not exist.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 
     * Adds a new comment to a post and saves it to the database.
     * 
     * @param post        the post to add the comment to
     * @param commentText the text of the comment to add
     */
    public void addComment(FeedPost post, String commentText) {
        String idOwner = userService.getCurrentUserId();
        Comment comment = new Comment(idOwner, commentText);
        post.getComments().add(comment); // Add the new comment to the post's comments list
        saveCommentToDatabase(post, "posts"); // Save the updated post to the database
    }

    /**
     * Saves the given image to Firebase Storage with the given postId as the
     * filename.
     *
     * @param imageUri the URI of the image to save
     * @param postId   the postId to use as the filename
     * @param location place of image in the storage
     */
    public void saveImageToDatabase(Uri imageUri, ImageView imageView, String postId, String location) {
        StorageReference storageReference = dbStore.getReference(location + postId);
        try {
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(this)
                    .addOnFailureListener(this);
        } catch (SecurityException e) {
            saveImageToStorageWithoutPermissions(imageView, storageReference);
        } catch (Exception e) {
            System.out.println("Error Uploading photo");
        }
    }

    /**
     * Saves the given image to Firebase Storage with the given postId as the
     * filename,
     * when the app does not have permission to access the imageUri directly.
     *
     * @param imageView        the ImageView containing the image to save
     * @param storageReference the StorageReference to use for saving the image
     */
    private void saveImageToStorageWithoutPermissions(ImageView imageView, StorageReference storageReference) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(this)
                .addOnSuccessListener(this);
    }

    /**
     * Loads an image from Firebase Storage into an ImageView using Glide.
     *
     * @param imageView the ImageView to load the image into
     * @param postId    the ID of the post whose image is being loaded
     * @param location  place of the image in the storage
     */
    public void loadImage(ImageView imageView, String postId, String location) {
        StorageReference storageReference = dbStore.getReference(location + postId);

        // Try to load the image from Firebase Storage.
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            // If the image was loaded successfully, use Glide to load it into the
            // ImageView.
            Glide.with(imageView.getContext()).load(uri).into(imageView);
        }).addOnFailureListener(exception -> {
            // If an error occurred while loading the image, load a default image instead.
            Glide.with(imageView.getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/bitebybyte-ac8f2.appspot.com/o/default_Image%20(1).jpg?alt=media&token=db6bf7f0-6c34-4f9f-892c-e8cc031f23c8")
                    .into(imageView);
        });
    }

    /**
     * Formats the given date as a string that represents how long ago it was
     * posted.
     *
     * @param date the date to format
     * @return a string representing how long ago the given date was posted
     */
    public String dateFormat(Date date) {
        // Get the current time
        Date currentTime = Calendar.getInstance().getTime();
        // Calculate the difference between the current time and the provided date
        long difference = currentTime.getTime() - date.getTime();
        // Get the formatted string representing the elapsed time
        String elapsed = getElapsed(difference);
        // Return the final formatted string
        return "" + elapsed + " ago";
    }

    /**
     * Calculates the elapsed time between the current time and a given time.
     *
     * @param difference the time difference in milliseconds
     * @return a string representing the elapsed time
     */
    private String getElapsed(long difference) {
        // Define constants for time units in milliseconds
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        // Calculate the elapsed time in days, hours, minutes, and seconds
        long elapsedDays = difference / daysInMilli;
        difference %= daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference %= hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference %= minutesInMilli;

        long elapsedSeconds = difference / secondsInMilli;

        // Construct a formatted string representing the elapsed time
        StringBuilder elapsed;

        if (elapsedDays > 0) {
            elapsed = new StringBuilder(elapsedDays + " day");
            if (elapsedDays > 1)
                elapsed.append("s");

        } else if (elapsedHours > 0) {
            elapsed = new StringBuilder(elapsedHours + " hour");
            if (elapsedHours > 1)
                elapsed.append("s");

        } else if (elapsedMinutes > 0) {
            elapsed = new StringBuilder(elapsedMinutes + " minute");
            if (elapsedMinutes > 1)
                elapsed.append("s");
        } else {
            elapsed = new StringBuilder(elapsedSeconds + " second");

            if (elapsedSeconds > 1)
                elapsed.append("s");
        }

        return elapsed.toString();
    }

    /**
     * Creates a new post with a recipe attached, and returns the post ID.
     *
     * @param idOwner             the ID of the user who is creating the post
     * @param content             the content of the post
     * @param title               the title of the post
     * @param images              a comma-separated string of image URLs for the
     *                            post
     * @param labels              a list of labels for the post
     * @param methods             a string containing the preparation methods for
     *                            the recipe
     * @param ingredients         a string containing the ingredients for the recipe
     * @param preparationTime     the preparation time for the recipe
     * @param preparationTimeUnit the unit of time for the preparation time (e.g.
     *                            "minutes", "hours")
     * @param location            which collection to save to in the database
     * @return the ID of the newly-created post
     */
    public String createPostWithRecipe(String idOwner,
            String content,
            String title,
            String images,
            List<String> labels,
            String methods,
            String ingredients,
            int preparationTime,
            String preparationTimeUnit,
            String location) {

        Recipe recipe = createRecipe(methods, ingredients, preparationTime, preparationTimeUnit);
        return createPost(idOwner, content, title, images, labels, recipe, location);

    }

    /**
     * Creates a new post with the given parameters and saves it to the database.
     * Returns the ID of the newly-created post.
     *
     * @param idOwner  the ID of the user who is creating the post
     * @param content  the content of the post
     * @param title    the title of the post
     * @param images   a comma-separated string of image URLs for the post
     * @param labels   a list of labels for the post
     * @param recipe   a Recipe object containing the recipe details for the post
     *                 (can be null if no recipe is included)
     * @param location which collection to save to in the database
     * @return the ID of the newly-created post
     */
    public String createPost(String idOwner,
            String content,
            String title,
            String images,
            List<String> labels,
            Recipe recipe,
            String location) {

        if (idOwner == null || content == null || title == null || images == null
                || labels == null || recipe == null || location == null) {
            throw new IllegalArgumentException("values cannot be null");
        }

        FeedPost post = new FeedPost(idOwner, content, title,
                images, labels, recipe);

        this.saveToDatabase(post, location);
        return post.getPostId();
    }

    /**
     * Creates a new Recipe object with the given parameters.
     *
     * @param methods             a string containing the methods used in the recipe
     * @param ingredients         a string containing the ingredients used in the
     *                            recipe
     * @param preparationTime     the amount of time required to prepare the recipe
     * @param preparationTimeUnit the unit of time used to measure preparation time
     *                            (e.g. "minutes", "hours")
     * @return a new Recipe object with the given parameters
     */
    public Recipe createRecipe(String methods,
            String ingredients,
            int preparationTime,
            String preparationTimeUnit) {
        if (methods == null || ingredients == null || preparationTimeUnit == null) {
            throw new IllegalArgumentException("values cannot be null");
        }

        return new Recipe(methods, ingredients, preparationTime, preparationTimeUnit);
    }

    /**
     * Updates the likes locally and in the Firebase.
     *
     * @param post     post to update
     * @param location which collection the document is in
     */
    public void updateLikes(FeedPost post, String location) {
        DocumentReference postRef = db.collection(location).document(post.getPostId());

        FirebaseUser user = auth.getCurrentUser();
        if (user == null)
            return;

        boolean userHasLikedPost = post.getLikes().containsKey(user.getUid());

        // If the user has already liked the post, remove their like
        if (userHasLikedPost)
            post.getLikes().remove(auth.getCurrentUser().getUid());
        else
            post.getLikes().put(auth.getCurrentUser().getUid(), true);

        // Update the post in the database
        postRef
                .update("likes", post.getLikes())
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Updates the likes of a comment locally and in the Firebase.
     *
     * @param comments list of comments to update
     * @param postId   id of post to update comment from
     * @param index    of comment
     * @param location collection of post of comment
     */
    public void updateLikes(List<Comment> comments, String postId, int index, String location) {
        DocumentReference postRef = db.collection(location).document(postId);
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
     * Delete the comment locally and in the Firebase.
     *
     * @param comments list of comments to delete from
     * @param postId   id of post to delete comment from
     * @param index    of comment to delete
     * @param location collection of post of comment
     */
    public void deleteComment(List<Comment> comments, String postId, int index, String location) {
        DocumentReference postRef = db.collection(location).document(postId);
        if (index < comments.size()) {
            comments.remove(index);
        }
        postRef.update("comments", comments)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    /**
     * Checks if the current user has liked the content (post / comment).
     *
     * @param content The content to check.
     * @return True if the current user has liked the post, false otherwise.
     */
    public boolean hasLikedContent(AbstractContent content) {
        return content.getLikes().containsKey(auth.getCurrentUser().getUid());
    }

    /**
     * Query Firebase given a postId to find a single document.
     *
     * @param postId   of post to find
     * @param fragment callback
     * @param location collection of post
     */
    public void inflatePostById(String postId, ServiceablePostFragment fragment, String location) {
        DocumentReference postRef = db.collection(location).document(postId);
        postRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.v("Firebase", "Post fetched successfully");
            fragment.addDataToView(documentSnapshot.toObject(FeedPost.class));
        });
    }

    /**
     * Query Firebase given a postId to find a single document.
     *
     * @param postId   of post to find
     * @param fragment callback
     * @param holder   callback to AbstractViewHolder
     * @param location collection of post
     */
    public void inflatePostById(String postId, ServiceablePostFragment fragment, AbstractViewHolder holder,
            String location) {
        DocumentReference postRef = db.collection(location).document(postId);
        postRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.v("Firebase", "Post fetched successfully");
            fragment.addDataToView(documentSnapshot.toObject(FeedPost.class), holder);
        });
    }

    /**
     * Query Firebase to get all posts in descending order by time published.
     *
     * @param fragment callback
     * @param location collection of posts
     */
    public void getAllPosts(ServiceablePostFragment fragment, String location) {
        db.collection(location)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FeedPost> posts = createListOfFeedPosts(task);
                        fragment.getListOfPosts(posts);
                    } else {
                        handleDocumentRetrievalError(task);
                    }
                });
    }

    /**
     * Create a list of FeedPost objects from a Firestore task result.
     * Logs the success or failure of each document retrieval to the console.
     *
     * @param task Firestore task result
     * @return a list of FeedPost objects
     */
    private List<FeedPost> createListOfFeedPosts(Task<QuerySnapshot> task) {
        List<FeedPost> posts = new ArrayList<>();
        for (QueryDocumentSnapshot document : task.getResult()) {
            try {
                FeedPost post = createFeedPostFromDocument(document);
                posts.add(post);
                Log.v("Firebase", "Successfully loaded document " + document.getId());
            } catch (Exception e) {
                Log.v("Firebase", "Failed to load document " + document.getId());
            }
        }
        return posts;
    }

    /**
     * Search for posts that have titles matching with a query string.
     *
     * @param query    titles to search for
     * @param fragment callback
     */
    public void searchByTitle(String query, ServiceablePostFragment fragment) {
        db.collection("posts")
                .whereEqualTo("title", query)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<FeedPost> posts = createListOfFeedPosts(task);
                        fragment.getListOfPosts(posts);
                    } else {
                        handleDocumentRetrievalError(task);
                    }
                });
    }

    /**
     * Create a FeedPost object from a Firestore document.
     *
     * @param document Firestore document
     * @return a FeedPost object
     */
    private FeedPost createFeedPostFromDocument(QueryDocumentSnapshot document) {
        return document.toObject(FeedPost.class);
    }

    /**
     * Handles an error that occurs when retrieving documents from Firestore.
     * Logs the error message to the console.
     *
     * @param task Firestore task result
     */
    private void handleDocumentRetrievalError(Task<QuerySnapshot> task) {
        Log.v("Firebase", "Error getting documents: " + task.getException());
    }

    @Override
    public void onSuccess(Object o) {
        Log.v("Firebase", "Successfully added");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.v("Firebase", "Failed add" + e);
    }
}
