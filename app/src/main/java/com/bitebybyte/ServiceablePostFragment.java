package com.bitebybyte;

import com.bitebybyte.backend.local.FeedPost;
import com.bitebybyte.ui.saved.ViewHolder;

import java.util.List;

/**
 * Use this interface with PostService to query the Firebase and send Callbacks.
 */
public interface ServiceablePostFragment {

    void addDataToView(FeedPost post);

    void addDataToView(FeedPost post, ViewHolder holder);

    void getListOfPosts(List<FeedPost> posts);
}
