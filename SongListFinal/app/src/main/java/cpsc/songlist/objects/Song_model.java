package cpsc.songlist.objects;

import java.io.Serializable;

public class Song_model implements Serializable
{
    private String name;
    private String artist;
    private Boolean checked;

    public Song_model(String name, String artist, boolean checked)
    {
        this.name = name;
        this.artist = artist;
        this.checked = checked;
    }
    public String getName()
    {
        return name;
    }
    public String getArtist() {
        return artist;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setArtist(String artist)
    {
        this.artist = artist;
    }
    public boolean isChecked()
    {
        return checked;
    }
    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

}
