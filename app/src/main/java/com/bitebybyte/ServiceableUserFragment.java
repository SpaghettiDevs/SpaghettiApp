package com.bitebybyte;

import com.bitebybyte.backend.models.User;
import com.bitebybyte.holders.CommentsViewHolder;
import com.bitebybyte.holders.HomeFeedViewHolder;
import com.bitebybyte.holders.SavedViewHolder;

/**
 * Use this interface with UserService to query the Firebase and send Callbacks.
 */
public interface ServiceableUserFragment {

    void addUserData(User user);

    void addUserData(User user, HomeFeedViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, CommentsViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, SavedViewHolder viewHolder); // consider refactoring ViewHolder
}
