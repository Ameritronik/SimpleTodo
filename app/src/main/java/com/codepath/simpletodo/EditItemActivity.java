package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private final static String TAG = EditItemActivity.class.getSimpleName();
    public static int pos = 0;

    // Open a new editor window with item selected from main screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        setupEditViewListener();
    }

    // Look for edits on this item and update edited item
    private void setupEditViewListener() {
        String edit_item = getIntent().getStringExtra("item");
        pos = getIntent().getIntExtra("pos", 0);
        EditText etValue = (EditText) findViewById(R.id.etValue);
        etValue.setText(edit_item);
        OnButtonClickSendEditedItem();
    }

    // send the results back to main screen for incorporation into
    // the to do list
    private void OnButtonClickSendEditedItem() {
        Button btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editeditem="None";
                EditText etValue = (EditText) findViewById(R.id.etValue);
                editeditem = etValue.getEditableText().toString();
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("etm", editeditem);
                data.putExtra("pos", pos);
                Log.w(TAG, "etm = "+editeditem+" pos = "+pos );
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });
    }

}
