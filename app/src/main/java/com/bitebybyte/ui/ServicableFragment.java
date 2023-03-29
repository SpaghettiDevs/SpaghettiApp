package com.bitebybyte.ui;

import com.bitebybyte.backend.local.FeedPost;

import java.util.List;

/**
 * Use this interface with PostService to query the Firebase and send Callbacks.
 */
public interface ServicableFragment {

    void addDataToView(FeedPost post);

    void getListOfPosts(List<FeedPost> posts);
}
