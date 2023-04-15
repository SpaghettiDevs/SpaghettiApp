package com.bitebybyte;

import com.bitebybyte.backend.models.User;
import com.bitebybyte.holders.AbstractViewHolder;

/**
 * Use this interface with UserService to query the Firebase and send Callbacks.
 */
public interface ServiceableUserFragment {

    void addUserData(User user);

    void addUserData(User user, AbstractViewHolder viewHolder); // consider refactoring ViewHolder
}
