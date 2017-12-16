package cpsc.songlist.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cpsc.songlist.R;
import cpsc.songlist.objects.Song_model;

public class ListAdapter extends ArrayAdapter
{
    private final Activity context;
    private ArrayList<Song_model> song_models;

    public ListAdapter(Activity context, ArrayList<Song_model> song_models)
    {
        super(context, R.layout.song_rows, song_models);
        this.context = context;
        this.song_models = song_models;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rows = inflater.inflate(R.layout.song_rows, null, true);

        TextView name = (TextView) rows.findViewById(R.id.song_name);
        TextView artist = (TextView) rows.findViewById(R.id.song_artist);
        name.setText(song_models.get(position).getName());
        artist.setText(song_models.get(position).getArtist());

        return rows;
    }
}

