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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

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

    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.song_full);

        if (!zero)
        {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("StoredArray", "");
            songs = new ArrayList<>();
            songs = gson.fromJson(json, new TypeToken<ArrayList<Song_model>>(){}.getType());
            Song_model song_model = new Song_model("", "Songs", false);
            if (songs == null)
            {
                songs = new ArrayList<Song_model>();
                songs.add(song_model);
            }
            listAdapter = new ListAdapter(this, songs);
            listView = (ListView) findViewById(R.id.list_viewID);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SongInfoFragment.class);
                    Song_model sm = songs.get(position);
                    intent.putExtra("song", sm);
                    startActivity(intent);
                }
            });
            button = (Button) findViewById(R.id.create);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, New_Song.class);
                    startActivityForResult(intent, 1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            zero = true;
        }
        else
        {
            final ListAdapter listAdapter = new ListAdapter(this, songs);
            listView = (ListView) findViewById(R.id.list_viewID);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, SongInfoFragment.class);
                    Song_model sm = songs.get(position);
                    intent.putExtra("song", sm);
                    startActivity(intent);
                }
            });
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
                listAdapter.notifyDataSetChanged();
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
