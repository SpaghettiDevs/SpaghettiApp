package com.bitebybyte;

import static org.junit.Assert.*;

import com.bitebybyte.backend.models.User;

import org.junit.Test;

public class UserTest {
    private final String userId = "12345";
    private final String username = "tester";
    private final User instance = new User(username, userId);


    /* Test for checking that the userID is not null when instantiated. */
    @Test
    public void getUserIdNullTest() {
        assertNotNull(instance.getUserId());
    }

    /* Test for checking that the username is not null when instantiated. */
    @Test
    public void getUsernameNullTest() {
        assertNotNull(instance.getUsername());
    }

    /* Test for checking that the my posts list is not null when instantiated. */
    @Test
    public void getMyPostsNullTest() {
        assertNotNull(instance.getMyPosts());
    }

    /* Test for checking that the saved posts list is not null when instantiated. */
    @Test
    public void getSavedPostsNullTest() {
        assertNotNull(instance.getSavedPosts());
    }

    /* Test for checking if the getUserId returns the expected value. */
    @Test
    public void getUserIdTest() {
        assertEquals(userId, instance.getUserId());
    }

    /* Test for checking if the getUsername returns the expected value. */
    @Test
    public void getUsernameTest() {
        assertEquals(username, instance.getUsername());
    }

    /* Test for checking if the getMyPosts returns the expected value (empty list in this case). */
    @Test
    public void getMyPostsTest() {
        assertEquals(0, instance.getMyPosts().size());
    }

    /* Test for checking if the getSavedPosts returns the expected value (empty list in this case). */
    @Test
    public void getSavedPostsTest() {
        assertEquals(0, instance.getSavedPosts().size());
    }
}
