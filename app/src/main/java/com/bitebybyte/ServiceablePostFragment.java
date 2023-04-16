package com.bitebybyte;

import com.bitebybyte.backend.models.FeedPost;
import com.bitebybyte.holders.AbstractViewHolder;

import java.util.List;

/**
 * Use this interface with PostService to query the Firebase and send Callbacks.
 */
public interface ServiceablePostFragment {

    /**
     * Use this to add the data from the callback to a single global view within the class.
     *
     * @param post the data from the callback.
     */
    void addDataToView(FeedPost post);

    /**
     * Use this to add the data from the callback to the provided view holder.
     *
     * @param post the data from the callback
     * @param holder the view holder to populate with the data
     */
    void addDataToView(FeedPost post, AbstractViewHolder holder);

    /**
     * Use this to get the results of a query gets a list of posts.
     *
     * @param posts callback data
     */
    void getListOfPosts(List<FeedPost> posts);
}
