package com.bitebybyte.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitebybyte.R;
import com.bitebybyte.holders.CompactViewHolder;

/**
 * Adapter that takes search results from the backend and feeds that to the recycler view.
 */
public class SearchAdapter extends RecyclerView.Adapter<CompactViewHolder>
{
    public SearchAdapter() {

    }

    @NonNull
    @Override
    public CompactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.post_saved, parent, false);

        return new CompactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompactViewHolder holder, int position)
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
