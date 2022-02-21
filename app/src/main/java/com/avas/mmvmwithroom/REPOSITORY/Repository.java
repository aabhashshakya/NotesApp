package com.avas.mmvmwithroom.REPOSITORY;

//Repository is just a simple JAVA class. It is not part of the MVVM Architecture but it is recommended as it provides an additional
//abstraction layer
//It provides abstraction to the data of the MODEL(Room database or any other MODEL) for the ViewModel
//good practice is to have one repository for each MODEL
//So the ViewModel doesn't need to care from which model the data came from

import android.app.Application;
import android.content.Context;
import android.media.AsyncPlayer;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.avas.mmvmwithroom.MODELwhichisROOM.NotesDAO;
import com.avas.mmvmwithroom.MODELwhichisROOM.NotesEntity;
import com.avas.mmvmwithroom.MODELwhichisROOM.ROOMDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    //We use executor as we need to run the db queries in a new thread
    Executor executor;
    private NotesDAO notesDAO;
    private ROOMDatabase db;

    //we take Application as parameter for this constructor as the ViewModel takes a application parameter so it is easy to pass
    //this from the ViewModel
    public Repository(Application application) {
        //initializing the database
        db = ROOMDatabase.getInstance(application);
        //getting the DAO from the database
        this.notesDAO = db.getNotesDAO();

        executor = Executors.newSingleThreadExecutor();
    }

    //NOW we write the methods of database transactions and run them in a new thread as ROOM requires to
    //See the use of repository now? if we had no repository, we would have to do this in ViewModel and it would be crowded
    //So repository provides this abstraction layer, and ViewModel can just call the repository's methods without caring anything

    public void insertNote(final NotesEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notesDAO.insertNote(note);
            }
        });
    }

    public void updateNote(final NotesEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notesDAO.updateNote(note);
            }
        });
    }

    public void deleteNote(final NotesEntity note) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notesDAO.deleteNote(note);
            }
        });
    }

    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notesDAO.deleteAllNotes();
            }
        });
    }

    public LiveData<List<NotesEntity>> getAllNotes(){
        //we know if Room returns LiveData no need to do that operation in a separate thread
        return notesDAO.getAllNotes();
    }





}
