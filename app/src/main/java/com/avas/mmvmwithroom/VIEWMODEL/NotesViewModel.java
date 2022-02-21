package com.avas.mmvmwithroom.VIEWMODEL;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.avas.mmvmwithroom.REPOSITORY.Repository;
import com.avas.mmvmwithroom.MODELwhichisROOM.NotesEntity;

import java.util.List;

//Now finally ViewModel // it extends AndroidViewModel NOT ViewModel/ViewModelProvider
//It basically has methods that call the methods in the repository. It will be directly accessed by the View
public class NotesViewModel extends AndroidViewModel {

    Repository repository;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository= new Repository(application);
    }
    public void insertNote(NotesEntity note) {
        repository.insertNote(note);
    }

    public void updateNote(NotesEntity note) {
        repository.updateNote(note);
    }

    public void deleteNote(NotesEntity note) {
        repository.deleteNote(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<NotesEntity>> getAllNotes(){
        return repository.getAllNotes();
    }
}
