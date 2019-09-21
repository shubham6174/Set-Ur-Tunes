package xyzz.shubhamsingh.musicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;

    static MediaPlayer myMediaPlayer;
    int position;
String sname;
    ArrayList<File> mySongs;
    Thread updateseekBar;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

      btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);

        songTextLabel=(TextView)findViewById(R.id.songLabel);
        songSeekbar=(SeekBar)findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("NOW PLAYING!!(by Shubham)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





        updateseekBar =new Thread(){
            /**
             * If this thread was constructed using a separate
             * <code>Runnable</code> run object, then that
             * <code>Runnable</code> object's <code>run</code> method is called;
             * otherwise, this method does nothing and returns.
             * <p>
             * Subclasses of <code>Thread</code> should override this method.
             *
             * @see #start()
             * @see #stop()
             * @see  Thread(ThreadGroup, Runnable, String)
             */
            @Override
            public void run() {


                int totalDuration=myMediaPlayer.getDuration();
                int currentPosition=0;
                while(currentPosition<totalDuration){

                    try{
                      sleep(500)  ;
                      currentPosition=myMediaPlayer.getCurrentPosition();
                      songSeekbar.setProgress(currentPosition);


                    }
                    catch(InterruptedException e){

                        e.printStackTrace();
                    }

                }

            }
        };




        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();

        }


        Intent i=getIntent();
        Bundle bundle=i.getExtras();


        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

sname=mySongs.get(position).getName().toString();

String songName=i.getStringExtra("songname");
songTextLabel.setText(songName);
songTextLabel.setSelected(true);



position=bundle.getInt("pos",0);
        Uri  u=Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();

        songSeekbar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();
        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);



        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * Notification that the progress level has changed. Clients can use the fromUser parameter
             * to distinguish user-initiated changes from those that occurred programmatically.
             *
             * @param seekBar  The SeekBar whose progress has changed
             * @param progress The current progress level. This will be in the range min..max where min
             *                 and max were set by {@link (int)} and
             *                 {@link (int)}, respectively. (The default values for
             *                 min is 0 and max is 100.)
             * @param fromUser True if the progress change was initiated by the user.
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            /**
             * Notification that the user has started a touch gesture. Clients may want to use this
             * to disable advancing the seekbar.
             *
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * Notification that the user has finished a touch gesture. Clients may want to use this
             * to re-enable advancing the seekbar.
             *
             * @param seekBar The SeekBar in which the touch gesture began
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                myMediaPlayer.seekTo(seekBar.getProgress());

                btn_pause.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {
                        songSeekbar.setMax(myMediaPlayer.getDuration());
                        if (myMediaPlayer.isPlaying()) {
                            btn_pause.setBackgroundResource(R.drawable.icon_play);
                            myMediaPlayer.pause();

                        } else {
                            btn_pause.setBackgroundResource(R.drawable.icon_pause);
                            myMediaPlayer.start();

                        }
                    }

                });

                    }





        });

                btn_next.setOnClickListener(new View.OnClickListener() {
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {

                        myMediaPlayer.stop();
                        myMediaPlayer.release();
                        position=((position+1)%mySongs.size());

                        Uri u=Uri.parse(mySongs.get(position).toString());
                        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                        sname=mySongs.get(position).getName().toString();
                        songTextLabel.setText(sname);
                        myMediaPlayer.start();


                    }


                });

btn_previous.setOnClickListener(new View.OnClickListener() {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        myMediaPlayer.stop();
        myMediaPlayer.release();

        position=((position+1)<0)?(mySongs.size()-1):(position-1);
        Uri u=Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);

        sname=mySongs.get(position).getName().toString();
        songTextLabel.setText(sname);
        myMediaPlayer.start();




    }
});











    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       if(item.getItemId()==android.R.id.home) {
           onBackPressed();

       }




        return super.onOptionsItemSelected(item);
    }
}