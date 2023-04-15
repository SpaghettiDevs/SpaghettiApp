package com.bitebybyte;

import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.holders.AbstractViewHolder;

import java.util.List;

/**
 * Use this interface with PostService to query the Firebase and send Callbacks.
 */
public interface ServiceablePostFragment {

    void addDataToView(FeedPost post);

    void addDataToView(FeedPost post, AbstractViewHolder holder);

    void getListOfPosts(List<FeedPost> posts);
}
