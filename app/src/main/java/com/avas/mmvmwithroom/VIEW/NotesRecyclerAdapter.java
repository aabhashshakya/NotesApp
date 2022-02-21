package com.avas.mmvmwithroom.VIEW;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.avas.mmvmwithroom.MODELwhichisROOM.NotesEntity;
import com.avas.mmvmwithroom.R;


//here we extend ListAdapter instead of RecyclerAdapter as it is more flexible because of these reasons:
//1. it has all the functionality of RecylerAdapter + more features
//2. we can override its constructor that takes a DiffUtil.ItemCallback object, that we can instantiate to detect changes in the
//list automatically, so we don't have to use notifyDataSetChanged() which is not efficient. it also provides a dope animation
//when recycler view is updated too
//3. we don't have to create an Arraylist object here,this class has methods to access list items from list passed i.e getItem(position)

public class NotesRecyclerAdapter extends ListAdapter<NotesEntity, NotesRecyclerAdapter.NotesViewHolder> {

    //this is viewholder
    static class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView titleView, descriptionView, priorityView;
        View itemView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleView = itemView.findViewById(R.id.titleRecyclerView);
            descriptionView = itemView.findViewById(R.id.descRecyclerView);
            priorityView = itemView.findViewById(R.id.priorityRecyclerView);
        }
    }


    public NotesRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    //we make it static so it is initialize before the constructor as we need to pass this in the constructor
    private static DiffUtil.ItemCallback<NotesEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<NotesEntity>() {
        @Override
        //here we give it the condition when two items in the list are the same, i.e when their primary keys are the same
        public boolean areItemsTheSame(@NonNull NotesEntity oldItem, @NonNull NotesEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        This method is called only if areItemsTheSame(int, int) returns true for these items. if the items are the same
	//now this checks if their contents have changed
        @Override
        public boolean areContentsTheSame(@NonNull NotesEntity oldItem, @NonNull NotesEntity newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getPriority() == newItem.getPriority()
                    && oldItem.getTitle().equals(newItem.getTitle());
        }
        //dont worry how this works, but what it does is detects change in the list, at what position and what type of change
        //items added or deleted, etc. and provides a dope animations too
    };

    //we define an interface to handle onClick events for the recycler view
    interface OnClickListener {
        //we define note as the parameter so that the mainactivity can get the note that was clicked
        //more about this logic in the onBindViewHolder(...) method
        void onClick(NotesEntity note);
    }

    //this method can be called from main activity to set up the interface
    public void setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;

    }

    OnClickListener listener;

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, int position) {
        //we use getItem(...) to get an item from the list
        holder.titleView.setText(getItem(position).getTitle());
        holder.descriptionView.setText(getItem(position).getDescription());
        holder.priorityView.setText(String.valueOf(getItem(position).getPriority()));

        //we set the onclick listener for the itemView and call our interface's listener within it
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (listener != null && position != -1) {
                    //we get the note that was pressed and pass it to the interface method
                    NotesEntity noteToEdit = getNoteAtPosition(position);
                    listener.onClick(noteToEdit);
                    Log.d("edit", "onLongClick: click detected");
                    return;
                }
                Log.d("edit", "onLongClick: Error");

            }
        });
    }



        //this method returns the note at a certain position, it is used when we swipe the recycler view to delete that note at that position
    public NotesEntity getNoteAtPosition(int position) {
        return getItem(position);
    }



    }



