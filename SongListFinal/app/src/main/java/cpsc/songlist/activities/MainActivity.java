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
    boolean zero = false;   //Handles if its the first time the program has run
    Button button;
    ListAdapter listAdapter;
    ListAdapter newlistadapter;
    CheckBox checkBox;
    ArrayList<Song_model> temp_songs_fave;

    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.song_full);

        if (!zero)  //If this is the first time the activity has been created
        {
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("StoredArray", null);
            songs = new ArrayList<>();
            temp_songs_fave = new ArrayList<>();
            songs = gson.fromJson(json, new TypeToken<ArrayList<Song_model>>(){}.getType());    //Grab an arraylist of objects from shared preferences with Gson

            listAdapter = new ListAdapter(this, songs);
            listView = (ListView) findViewById(R.id.list_viewID);
            listView.setAdapter(listAdapter);   //Call the adapter to display the rows of songs from the arraylist
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    CheckBox checkBox = findViewById(R.id.delete);
                    if (checkBox.isChecked())   //If the checkbox to delete selected songs is checked
                    {
                        CheckBox checkBox1 = findViewById(R.id.favorite_sort);
                        if (checkBox1.isChecked())  //If the list is sorted by favorites and the adapter is showing the modified arraylist
                        {
                            String s = temp_songs_fave.get(position).getName();
                            String f = temp_songs_fave.get(position).getArtist();
                            temp_songs_fave.remove(position);       //Delete the song at this position from this view
                            newlistadapter.notifyDataSetChanged();  //Tell the adapter the view has changed and the list modified
                            for (int i =0; i<songs.size(); ++i)
                            {
                                if (songs.get(i).getName().equals(s) && songs.get(i).getArtist().equals(f))
                                {
                                    songs.remove(i);    //Remove the same song from the main arraylist
                                    break;
                                }
                            }
                        }
                        else    //Otherwise remove the song from the list and update the adapter
                        {
                            songs.remove(position);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                    else    //If the checkbox for delete is not selected, then display the song information onClick
                    {
                        Intent intent = new Intent(MainActivity.this, SongInfoFragment.class);
                        Song_model sm = songs.get(position);
                        intent.putExtra("song", sm);    //Pass the object from the list as a Serializable intent to the next activity
                        startActivity(intent);
                    }
                }
            });
            button = (Button) findViewById(R.id.create);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    //Click on button to move to create a new song
                    listView.setAdapter(listAdapter);
                    Intent intent = new Intent(MainActivity.this, New_Song.class);
                    startActivityForResult(intent, 1);  //Start activity for intent as we need the info entered from the new activity
                    listAdapter.notifyDataSetChanged();
                }
            });
            checkBox = (CheckBox) findViewById(R.id.favorite_sort);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)  //Favorite sort check box control, Displays the favorites when checked and all songs when not checked
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
                        newlistadapter.notifyDataSetChanged();  //Start a new adapter to show the contents of this new favorite list.  Logic at each handles what to do for each adapter
                    }
                    else
                    {
                        temp_songs_fave.clear();    //Wipe the favorite list for when it is checked again to handle new additions to the arraylist
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
            zero = true;    //Create has run before so do not run again
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
                //When we get the new object, add it to the list and reset the activity to when the favorite list checkbox is unchecked
            }
        }
    }
    @Override
    protected void onStop() //When we close the app
    {
        super.onStop();
        SharedPreferences sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson(); //Using Gson convert the arraylist object to Json string and store in the SharedPreferences
        String json = gson.toJson(songs);
        editor.putString("StoredArray", json);
        editor.commit();
        //Commit the transaction to store the preferences
    }
}
