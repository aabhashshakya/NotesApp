package com.avas.mmvmwithroom.MODELwhichisROOM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


//this is the interface that we'll be using for database transactions
//this is similar to the implementation of retrofit, we define the methods and ROOM will give us the implementation when we create
//our database

@Dao  //dont forget this annotation
public interface NotesDAO {

    //THESE METHODS MUST BE CALLED FROM A SEPARATE THREAD(except for that which return a LiveData)
    //SO CALL THEN IN A NEW THREAD FROM THE REPOSITORY

    //we annotate our methods with the type of db transaction and ROOM will generate that code for us automatically when we create
    //our database
    @Insert
    void insertNote(NotesEntity note);

    @Delete
    void deleteNote(NotesEntity note);

    @Update
    void updateNote(NotesEntity note);

    //for querying we have to write our own syntax like this
    @Query("select * from Notes")
    LiveData<List<NotesEntity>> getAllNotes();
    //since ROOM is a part of Android Architecture components, it can return a LiveData object directly which we can observe
    //for changes and update our views accordingly

    @Query("delete from Notes")
    void deleteAllNotes();



}
