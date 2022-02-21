package com.avas.mmvmwithroom.VIEW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.avas.mmvmwithroom.R;

public class AddNotesActivity extends AppCompatActivity {



    EditText titleView, descriptionView;
    Button addButton;
    NumberPicker numberPicker;
    int priority=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        addButton = findViewById(R.id.button);
        titleView = findViewById(R.id.titleView);
        descriptionView = findViewById(R.id.descriptionView);
        numberPicker = findViewById(R.id.numberPicker);




        //setting up the number picker
        numberPicker.setEnabled(true);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(0);
        //this is triggered every time the use uses the number picker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //the priority values is always the last value user selected in the number picker
                priority = newVal;

            }
        });

        //we get the intent to know if it was for editing note or adding a note
        Intent intent = getIntent();
        //if the intent has id extra, we know it is for editing the note
        if(intent.hasExtra("id")){
            //we set the view to the notes's contents
            titleView.setText(intent.getStringExtra("title"));
            descriptionView.setText(intent.getStringExtra("description"));
            numberPicker.setValue(intent.getIntExtra("priority", 1));
        }



    }

    public void save(View v){
        //on clicking the button we set the intent with those data as the result and then finish the activity
        String title = titleView.getText().toString();
        String description = descriptionView.getText().toString();
        Intent result = new Intent();
        result.putExtra("title",title);
        result.putExtra("description",description);
        result.putExtra("priority", priority);

        int id =getIntent().getIntExtra("id", -1);

        if(id!=-1){
            //this case is for when we edited a note, we put the id in as we need id to update note
            result.putExtra("id", id);
        }
        setResult(RESULT_OK, result);
        finish();


    }
}