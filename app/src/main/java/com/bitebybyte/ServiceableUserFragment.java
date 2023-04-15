package com.bitebybyte;

import com.bitebybyte.backend.models.User;
import com.bitebybyte.holders.HomeFeedViewHolder;
import com.bitebybyte.holders.SavedViewHolder;
import com.bitebybyte.ui.post.PostCommentsAdapter;

/**
 * Use this interface with UserService to query the Firebase and send Callbacks.
 */
public interface ServiceableUserFragment {

    void addUserData(User user);

    void addUserData(User user, HomeFeedViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, PostCommentsAdapter.ViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, SavedViewHolder viewHolder); // consider refactoring ViewHolder
}
