package com.example.a16022774.taskmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText etName, etDesc, etTimer;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = (EditText) findViewById(R.id.editTextName);
        etDesc = (EditText) findViewById(R.id.editTextDescription);
        etTimer = (EditText) findViewById(R.id.editTextTimer);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(AddActivity.this);
                db.insertTask(etName.getText().toString(), etDesc.getText().toString());
                db.close();
                Intent returnIntent = getIntent();
                returnIntent.putExtra("name", etName.getText().toString());
                returnIntent.putExtra("time", Integer.parseInt(etTimer.getText().toString()));
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
