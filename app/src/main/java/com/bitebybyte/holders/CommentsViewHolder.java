package com.bitebybyte.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitebybyte.R;

public class CommentsViewHolder extends AbstractViewHolder {
    private final TextView content;
    private final TextView postedDateTime;
    private final TextView likesCount;
    private final ImageView likeButton;
    private final ImageButton deleteButton;


    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.post_comment_text);
        postAuthor = itemView.findViewById(R.id.post_comment_author_name);
        postAuthorImage = itemView.findViewById(R.id.post_comment_author_picture);
        postedDateTime = itemView.findViewById(R.id.post_comment_date_time);
        likesCount = itemView.findViewById(R.id.post_comment_likes_amount);
        likeButton = itemView.findViewById(R.id.post_comment_likes_icon);
        deleteButton = itemView.findViewById(R.id.post_comment_delete_button);
    }

    public TextView getContent() {
        return content;
    }

    public TextView getAuthorName() {
        return postAuthor;
    }

    public ImageView getAuthorImage() {
        return postAuthorImage;
    }

    public TextView getPostedDateTime() {
        return postedDateTime;
    }

    public TextView getLikesCount() {
        return likesCount;
    }

    public ImageView getLikeButton() {
        return likeButton;
    }

    public ImageButton getDeleteButton() {return deleteButton;}
}
