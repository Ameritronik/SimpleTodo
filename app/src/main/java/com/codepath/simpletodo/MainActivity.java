package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final int REQUEST_CODE = 20;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    // Kick off the main activity with OnCreate..
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupOnclickListener();
    }

    // Have an utility for reading items in the list
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    // Have an utility for adding to the list
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // Have an utility for writing in the list
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Have an utility for replacing in the list
    private void replaceItems(int pos, String NewValue) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
            items.set(pos,NewValue);
            itemsAdapter.notifyDataSetChanged();
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Begin "listeners" code section. Here add Listeners
    // and returned activity data updaters etc.

    // Begin the on click listener
    private void setupOnclickListener(){
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter,
                    View item, int pos, long id) {
                    launchEditorView(item, pos, id);
            }
        });
     }

    // Add a listener to delete an item for a long click
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                });
    }

    // Initiate a new view with the launch of an editor for
    // a short click
    public void launchEditorView(View item, int pos, long id) {
        // Create a new intent to launch editor screen
         Intent i = new Intent(this, EditItemActivity.class);
        // get to be edited item
         String eitem = lvItems.getItemAtPosition(pos).toString();
        // setup intent with new bundle
         i.putExtra("item",eitem);
         i.putExtra("pos", pos);
        // start editor
         startActivityForResult(i, REQUEST_CODE);
    }

    // Handle the result of the editor
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract updated item at position from result extras
            String eStr = data.getExtras().getString("etm");
            int pos = data.getExtras().getInt("pos");
            replaceItems(pos,eStr);
        }
    }

    // Finally update any additions or edits and refresh this screen
    @Override
    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupOnclickListener();
    }

}
