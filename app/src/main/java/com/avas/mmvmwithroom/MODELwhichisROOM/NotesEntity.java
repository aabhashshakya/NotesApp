package com.avas.mmvmwithroom.MODELwhichisROOM;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

//this entity annotation means that this class in now an entity/table of a ROOM database
//We can change the name of the table within this annotation as well if we don't like the class name
@Entity(tableName="Notes")
public class NotesEntity {

    //this is the class that is going to represent a table in the ROOM database

    //these are the columns of the table
    //We NEED to have a PRIMARY KEY so we annotate the appropriate column for that//we can auto generate it too
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private int priority;

    public NotesEntity(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
