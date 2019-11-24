package com.madrecipes.app.madrecipes.AddScreen;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.TouchHelper.ItemTouchHelperAdapter;
import com.madrecipes.app.madrecipes.TouchHelper.OnDragStartListener;

import java.util.Collections;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//adapter for recyclerview that displays steplist
public class AddStepsAdapter extends RecyclerView.Adapter<AddStepsViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<String> stepList;
    private final OnDragStartListener mDragStartListener;

    //adapter constructor
    public AddStepsAdapter(Context context, List<String> stepList, OnDragStartListener dragStartListener) {
        this.context = context;
        this.stepList = stepList;
        mDragStartListener = dragStartListener;
    }

    //set layout for steplist
    @Override
    public AddStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_add_steps,
                        parent,
                        false);
        return new AddStepsViewHolder(item);
    }

    //set text of steplist
    @Override
    public void onBindViewHolder(final AddStepsViewHolder holder, int position) {
        String s = stepList.get(position);
        holder.addStep.setText(s);

        //when user touch handle
        //start drag action
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onDragStarted(holder);
                }
                return false;
            }
        });
    }

    //get size of steplist
    @Override
    public int getItemCount() {
        return stepList.size();
    }

    //if item is reordered, switch position
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(stepList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(stepList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    //when item is swiped to dismiss, remove from list
    @Override
    public void onItemDismiss(int position) {
        stepList.remove(position);
        notifyItemRemoved(position);
    }
}
