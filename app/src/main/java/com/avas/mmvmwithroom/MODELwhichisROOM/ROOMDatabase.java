package com.avas.mmvmwithroom.MODELwhichisROOM;

//this is the ROOM database where our Entity/table resides. It is basically a SQL database encapsulated by ROOM

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


//with this annotation we define it as a Database, and also select its tables/entities, here it only has 1 table
//we also specify the database version, increase here for each db updates your app gets
@Database(entities = {NotesEntity.class}, version = 1)
public abstract class ROOMDatabase extends RoomDatabase {
    //here we make the class abstract as we don't want to instantiate this class to get our database object
    //Instead we make this database object a singleton, meaning it only has one instance, which we will get from ROOM library

    private static ROOMDatabase instance;

    //we call this static method to get an instance of the database
    //synchronized means two threads cannot access this at once
    public static synchronized ROOMDatabase getInstance(Context context){
        //this function is basically making the database instance a singleton
        if(instance==null){
            //we pass the whole Application Context, the database class i.e this and the name of the database
            instance = Room.databaseBuilder(context.getApplicationContext(), ROOMDatabase.class, "YourNotes")
            //if we don't do fallback and increase the version number of database, it will crash so do this
            .fallbackToDestructiveMigration()
                    //we can also add the callback we created below here
                    .addCallback(roomCallback).build();

        }
        return instance;
    }

    //we dont need to provide a body to this method as ROOM will take care of it //just like retrofit remember
    public abstract NotesDAO getNotesDAO();

    //we use this callback to populate table (if we want) for the first time the db is created or opened. we did a similar thing
    //in sqlite database tutorial as well, we had to implement onCreate(..) and onUpdate(..) methods. this is the same
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            final NotesEntity notes = new NotesEntity("sadasd", "ASdasd", 3);
            final NotesEntity notes2 = new NotesEntity("ssddssdsd", "ASsddddddddddddddddasd", 1);
            final NotesEntity notes3 = new NotesEntity("sd", "Asd", 2);
            final NotesDAO dao = instance.getNotesDAO();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.insertNote(notes);
                    dao.insertNote(notes2);
                    dao.insertNote(notes3);
                }
            }).start();


        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }


    };






}
