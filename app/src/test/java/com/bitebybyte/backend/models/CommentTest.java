package com.bitebybyte.backend.models;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {
    private final String ownerId = "tester";
    private final String content = "testing content";
    private final AbstractContent instance = new Comment(ownerId, content);

    /* Test for checking that the content is not null when instantiated. */
    @Test
    public void getContentNullTest() {
        assertNotNull(instance.getContent());
    }

    /* Test for checking that the owner id is not null when instantiated. */
    @Test
    public void getOwnerIdNullTest() {
        assertNotNull(instance.getIdOwner());
    }

    /* Test for checking that the Date is not null when instantiated. */
    @Test
    public void getDateNullTest() {
        assertNotNull(instance.getDate());
    }

    /* Test for checking that the likes counter is not null when instantiated. */
    @Test
    public void getLikesNullTest() {
        assertNotNull(instance.getLikes());
    }

    /* Test for checking that the Post id is not null when instantiated. */
    @Test
    public void getPostIdNullTest() {
        assertNotNull(instance.getPostId());
    }

    /* Test for checking that the comments list is not null when instantiated. */
    @Test
    public void getCommentsNullTest() {
       //there is no getter for the comments arraylist in the comment class
    }

    /* Test for checking if the getContent returns the same content as instantiated with. */
    @Test
    public void getContentTest() {
        assertEquals(instance.getContent(), content);
    }

    /* Test for checking if the getOwnerId returns the same Id as instantiated with. */
    @Test
    public void getOwnerIdTest() {
        assertEquals(instance.getIdOwner(), ownerId);
    }
}
