package cpsc.songlist.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cpsc.songlist.R;
import cpsc.songlist.objects.Song_model;

public class New_Song extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsong);
        final EditText name = (EditText) findViewById(R.id.song_name_edit);
        final EditText artist = (EditText) findViewById(R.id.song_artist_edit);
        final CheckBox checkBox = findViewById(R.id.favorites_edit);
        final Button button = findViewById(R.id.submit);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                {
                    button.setEnabled(false);
                }
                else
                {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(""))
                {
                    button.setEnabled(false);
                }
                else
                {
                    button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song_model newsong = new Song_model(name.getText().toString(), artist.getText().toString(), checkBox.isChecked());
                Intent returnintent = new Intent();
                returnintent.putExtra("result", newsong);
                setResult(Activity.RESULT_OK, returnintent);
                finish();
            }
        });
    }
}
