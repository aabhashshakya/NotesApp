package com.avas.mmvmwithroom.VIEW;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.avas.mmvmwithroom.MODELwhichisROOM.NotesEntity;
import com.avas.mmvmwithroom.R;
import com.avas.mmvmwithroom.VIEWMODEL.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * this app showcases the MVVM archtecture in android.
     * We use ROOM Database(Entity+NoteDatabase+NotesDAO) + Repository class as MODEL //Respository class is nothing but a simple JAVA class
     * We use ViewModel with Livedata class as the VIEWMODEL
     * We use Activity such as this as the VIEW
     * <p>
     * The main purpose of using MMVM is to reduce tight coupling of code. More detail about this architecture in copy.
     */
    public static final int EDIT_NOTE = 23;
    public static final int ADD_NOTE = 233;


    NotesRecyclerAdapter recyclerAdapter;
    NotesViewModel viewModel;
    RecyclerView recyclerView;
    FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is how we instantiate the viewModel, not simple way
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NotesViewModel.class);

        //floating button
        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clicking this floating button launched new activity to insert the node
                Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
                startActivityForResult(intent,ADD_NOTE);
            }
        });

        recyclerViewStuffs();


//        we observe the livedata object and do stuff if there is changes
//        this is basically triggered the first time app starts and then when there's changes in the ROOM database
        viewModel.getAllNotes().observe(this, new Observer<List<NotesEntity>>() {
            @Override
            public void onChanged(List<NotesEntity> notesEntities) {
                //before we pass the list to the adapter, we want to sort them, just because, lol// sort them by priority
                Collections.sort(notesEntities, new Comparator<NotesEntity>() {
                    @Override
                    public int compare(NotesEntity o1, NotesEntity o2) {
                        //returns 0 if equal, -1 if o1.getPriority<02.getPriority, 1 if o1.getPriority>02.getPriority
                        return Integer.compare(o1.getPriority(), o2.getPriority());
                    }
                });

                //we use submitList() of the ListAdapter to send a list that we want
                recyclerAdapter.submitList(notesEntities);

            }
        });


    }

    //recyclerView components
    public void recyclerViewStuffs(){
        recyclerAdapter = new NotesRecyclerAdapter();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);

        //Item touch helper is a part of Recycler view, we implement it so we can swipe recycler view
        //if we swipe the view, that note has to be deleted
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //here 0 as we are not concerned with onMove(), then we pass the directions for swipe
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //we can get the adapter position which was swiped, but we need the Note object to delete it
                //so we have created a methods in RecyclerAdapter to get that note by passing this position
                int adapterPosition = viewHolder.getAdapterPosition();
                NotesEntity noteToBeDeleted = recyclerAdapter.getNoteAtPosition(adapterPosition);
                viewModel.deleteNote(noteToBeDeleted);
            }
        }).attachToRecyclerView(recyclerView);

        //we also should be able to edit the note when clicking on it
        //we defined our own interface in the adapter class check it out
        recyclerAdapter.setOnClickListener(new NotesRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(NotesEntity note) {
                //on click we open the same AddNoteActivity with some few changes
                Intent editIntent = new Intent(MainActivity.this, AddNotesActivity.class);
                //we pass the existing details of the note as we want to load them in the activity's views
                editIntent.putExtra("id", note.getId());
                editIntent.putExtra("priority", note.getPriority());
                editIntent.putExtra("title", note.getTitle());
                editIntent.putExtra("description",note.getDescription());
                startActivityForResult(editIntent, EDIT_NOTE);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllNotes) {
            //clicking delete all notes call the method in the ViewModel
            viewModel.deleteAllNotes();
            Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //result of adding a note
        if (requestCode == ADD_NOTE) {
            if (resultCode == RESULT_OK) {
                //when we get the result back from the activity, we create a new NotesEntity object and pass it to insert the new note
                NotesEntity newNote = new NotesEntity(data.getStringExtra("title"),
                        data.getStringExtra("description"), data.getIntExtra("priority", 0));
                viewModel.insertNote(newNote);
                Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode==EDIT_NOTE){
            if(resultCode==RESULT_OK){
                NotesEntity editedNote = new NotesEntity(data.getStringExtra("title"),
                        data.getStringExtra("description"), data.getIntExtra("priority", 0));
                //don't forget the id//we know its required for ROOM to know for updating as it is primary key
                int id =data.getIntExtra("id", -1);
                if(id==-1){
                    //again this case wont happen but just checking
                    Toast.makeText(this, "Error in editing", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.updateNote(editedNote);
                Toast.makeText(this, "Note edited!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}