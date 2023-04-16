package com.bitebybyte;

import com.bitebybyte.backend.models.User;
import com.bitebybyte.holders.AbstractViewHolder;

/**
 * Use this interface with UserService to query the Firebase and send Callbacks.
 */
public interface ServiceableUserFragment {

    /**
     * Use this to user add the data from the callback to a single global view within the class.
     *
     * @param user the user data from the callback.
     */
    void addUserData(User user);

    /**
     * Use this to add the user data from the callback to the provided view holder.
     *
     * @param user the user data from the callback
     * @param viewHolder the view holder to populate with the user data
     */
    void addUserData(User user, AbstractViewHolder viewHolder);
}
