package com.example.mipt_4;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import android.os.Bundle;
        import com.google.android.material.navigation.NavigationView;

        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;
    TextView emptyText;

    static List<String> notes;
    static ArrayAdapter adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.mipt_4", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.notes_ListView);

        notes = new ArrayList<>();

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.notes_row, R.id.notesText, notes);
        notesListView.setAdapter(adapter);

        onOptionsItemSelected();

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        loadNotes();
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Really?")
                .setMessage("Delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notes.remove(position);
                        adapter.notifyDataSetChanged();

                        HashSet<String> noteSet = new HashSet<>(notes);
                        sharedPreferences.edit().putStringSet("notes", noteSet).apply();

                        if (noteSet.isEmpty() || noteSet == null) {
                            emptyText.setVisibility(View.VISIBLE);
                        }
                    }
                }).setNegativeButton("No", null)
                .show();
    }

    private void loadNotes() {
        Set<String> noteSet = sharedPreferences.getStringSet("notes", new HashSet<>());
        notes.addAll(noteSet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onOptionsItemSelected() {
        NavigationView navigationView = findViewById(R.id.navMenu);

        final int addNote = R.id.add_note;

        NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == addNote) {
                    startActivity(new Intent(MainActivity.this, NoteEditorActivity.class));
                }

                return true;
            }
        };

        navigationView.setNavigationItemSelectedListener(listener);

    }

}