package cpsc.songlist.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cpsc.songlist.R;
import cpsc.songlist.adapters.ListAdapter;
import cpsc.songlist.fragments.New_Song;
import cpsc.songlist.fragments.SongInfoFragment;
import cpsc.songlist.objects.Song_model;

public class MainActivity extends Activity
{
    ArrayList<Song_model> songs;
    ListView listView;
    boolean zero = false;
    Button button;
    ListAdapter listAdapter;
    ListAdapter newlistadapter;
    CheckBox checkBox;
    ArrayList<Song_model> temp_songs_fave;
    ArrayList<Song_model> empty;

    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.song_full);

        if (!zero)
        {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("StoredArray", null);
            songs = new ArrayList<>();
            temp_songs_fave = new ArrayList<>();
            songs = gson.fromJson(json, new TypeToken<ArrayList<Song_model>>(){}.getType());

            listAdapter = new ListAdapter(this, songs);
            listView = (ListView) findViewById(R.id.list_viewID);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    CheckBox checkBox = findViewById(R.id.delete);
                    if (checkBox.isChecked())
                    {
                        CheckBox checkBox1 = findViewById(R.id.favorite_sort);
                        if (checkBox1.isChecked())
                        {
                            String s = temp_songs_fave.get(position).getName();
                            temp_songs_fave.remove(position);
                            newlistadapter.notifyDataSetChanged();
                            for (int i =0; i<songs.size(); ++i)
                            {
                                if (songs.get(i).getName().equals(s))
                                {
                                    songs.remove(i);
                                    break;
                                }
                            }
                        }
                        else
                        {
                            songs.remove(position);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, SongInfoFragment.class);
                        Song_model sm = songs.get(position);
                        intent.putExtra("song", sm);
                        startActivity(intent);
                    }
                }
            });
            button = (Button) findViewById(R.id.create);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listView.setAdapter(listAdapter);
                    Intent intent = new Intent(MainActivity.this, New_Song.class);
                    startActivityForResult(intent, 1);
                    listAdapter.notifyDataSetChanged();
                }
            });
            checkBox = (CheckBox) findViewById(R.id.favorite_sort);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (checkBox.isChecked())
                    {
                        for (int i = 0; i<songs.size(); ++i)
                        {
                            if (songs.get(i).isChecked())
                            {
                                temp_songs_fave.add(songs.get(i));
                            }
                        }
                        newlistadapter = new ListAdapter(MainActivity.this, temp_songs_fave);
                        listView.setAdapter(newlistadapter);
                        newlistadapter.notifyDataSetChanged();
                    }
                    else
                    {
                        temp_songs_fave.clear();
                        listView.setAdapter(listAdapter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
            zero = true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Song_model song_model = (Song_model) intent.getSerializableExtra("result");
                songs.add(song_model);
                temp_songs_fave.clear();
                listAdapter.notifyDataSetChanged();
                checkBox.setChecked(false);
            }
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        SharedPreferences sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songs);
        editor.putString("StoredArray", json);
        editor.commit();
    }
}
