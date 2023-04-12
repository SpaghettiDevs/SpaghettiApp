package com.bitebybyte;

import com.bitebybyte.backend.local.User;
import com.bitebybyte.ui.home.HomeFeedAdapter;
import com.bitebybyte.ui.post.PostCommentsAdapter;
import com.bitebybyte.ui.saved.ViewHolder;

/**
 * Use this interface with UserService to query the Firebase and send Callbacks.
 */
public interface ServiceableUserFragment {

    void addUserData(User user);

    void addUserData(User user, HomeFeedAdapter.ViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, PostCommentsAdapter.ViewHolder viewHolder); // consider refactoring ViewHolder
    void addUserData(User user, ViewHolder viewHolder); // consider refactoring ViewHolder
}
