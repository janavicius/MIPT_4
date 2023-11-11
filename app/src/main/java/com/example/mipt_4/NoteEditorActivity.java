package com.example.mipt_4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    EditText noteEditText;
    int noteId;
    Button deleteButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        sharedPreferences = this.getSharedPreferences("com.example.mipt_4", Context.MODE_PRIVATE);
        noteEditText = findViewById(R.id.note_EditText);
        deleteButton = findViewById(R.id.delete);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            noteEditText.setText(MainActivity.notes.get(noteId));
            setTitle("Edit Note");
        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            setTitle("Add Note");
        }

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.adapter.notifyDataSetChanged();

                HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", noteSet).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(); // Call the deleteNote method
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save_note) {
            // Save the note
            saveNote();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return true;
        }
        return false;
    }

    private void saveNote() {
        String noteContent = noteEditText.getText().toString();
        MainActivity.notes.set(noteId, noteContent);
        MainActivity.adapter.notifyDataSetChanged();

        HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", noteSet).apply();
    }

    private void deleteNote() {
        MainActivity.notes.remove(noteId);
        MainActivity.adapter.notifyDataSetChanged();

        HashSet<String> noteSet = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", noteSet).apply();
    }
}
