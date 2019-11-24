package com.madrecipes.app.madrecipes.ShoppingList;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madrecipes.app.madrecipes.R;

import java.util.ArrayList;

//Muhd Hadith Bin Haron Aminal Rasid
//Team 6
//10182504F

//shopping list
public class ShopListFragment extends Fragment {

    private TaskHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private View view;
    private ArrayList<String> taskList;
    private static final String TAG = "ShopListFragment";

    //oncreateview set layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_shoppinglist, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    //after view created
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHelper = new TaskHelper(getActivity());
        mTaskListView = (ListView) view.findViewById(R.id.list_todo);

        updateUI();

        //open dialog to prompt user if delete the item
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete " + taskList.get(position) + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTask(position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    //set menu bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //when menu bar icon is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_item: //if add icon clicked
                final EditText taskEditText = new EditText(getActivity());

                //dialog to prompt for add new text
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Add New Item")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogue, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                if (!task.isEmpty()) { //if edittext is not empty, add task
                                    values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                                    db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                    db.close();
                                    updateUI();
                                } else { //edit text empty, toast error
                                    Toast.makeText(getContext(), "Enter something before adding!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //upddate recyclerview after add/delete
    private void updateUI() {
        taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row, R.id.task_title, taskList);
            mTaskListView.setAdapter(mAdapter);

        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    //delete item
    public void deleteTask(int position) {
        String task = taskList.get(position);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE, Task.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {task});
        db.close();
        updateUI();

    }
}
