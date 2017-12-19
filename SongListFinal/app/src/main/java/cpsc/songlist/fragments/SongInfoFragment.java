package cpsc.songlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import cpsc.songlist.R;
import cpsc.songlist.activities.MainActivity;
import cpsc.songlist.objects.Song_model;

public class SongInfoFragment extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_info_frag);
        final Song_model args = (Song_model) getIntent().getSerializableExtra("song");  //Take in the serializable intent of the song_model object
        TextView textView = (TextView) findViewById(R.id.song_name_info);
        TextView textView1 = (TextView) findViewById(R.id.song_artist_info);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.favorites_info);
        checkBox.setChecked(args.isChecked());
        textView.setText(args.getName());
        textView1.setText(args.getArtist());    //Display the values contained in this object
    }
}
