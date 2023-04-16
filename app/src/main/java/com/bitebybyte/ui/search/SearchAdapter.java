package com.bitebybyte.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.holders.SavedViewHolder;

/**
 * Adapter that takes search results from the backend and feeds that to the recycler view.
 */
public class SearchAdapter extends RecyclerView.Adapter<SavedViewHolder>
{
    public SearchAdapter() {

    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.post_saved, parent, false);

        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position)
    {
        // TODO: Retrieve data from the backend
        String[] titles = {"Search result 1", "Another result to show"};

        holder.getPostTitle().setText(titles[position % titles.length]);
        holder.getDeletePostButton().setVisibility(View.GONE);

    }

    // TODO: Get size from backend
    @Override
    public int getItemCount()
    {
        return 4;
    }
}
