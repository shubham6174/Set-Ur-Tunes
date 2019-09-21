package xyzz.shubhamsingh.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myListViewForSongs;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myListViewForSongs=(ListView)findViewById(R.id.mySongListView);

        runtimePermission();



    }

    public void runtimePermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    /**
                     * Method called whenever a requested permission has been granted
                     *
                     * @param response A response object that contains the permission that has been requested and
                     *                 any additional flags relevant to this response
                     */
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();

                    }

                    /**
                     * Method called whenever a requested permission has been denied
                     *
                     * @param response A response object that contains the permission that has been requested and
                     *                 any additional flags relevant to this response
                     */
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    /**
                     * Method called whenever Android asks the application to inform the user of the need for the
                     * requested permission. The request process won't continue until the token is properly used
                     *
                     * @param permission The permission that has been requested
                     * @param token      Token used to continue or cancel the permission request process. The permission
                     */
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {


                       token.continuePermissionRequest();


                    }
                }).check();


    }

    public ArrayList<File>findSong(File file){

        ArrayList<File> arrayList=new ArrayList<>();
        File[] files=file.listFiles();
        for(File singleFile:files){

            if(singleFile.isDirectory()  &&  !singleFile.isHidden()){

                arrayList.addAll(findSong(singleFile));
            }
            else{
           if(singleFile.getName().endsWith(".mp3")||
                singleFile.getName().endsWith(".wav")){
               arrayList.add(singleFile);



                }


            }


        }
        return arrayList;
    }


void display ()    {

        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){


            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

    ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
     myListViewForSongs.setAdapter(myAdapter);

     myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         /**
          * Callback method to be invoked when an item in this AdapterView has
          * been clicked.
          * <p>
          * Implementers can call getItemAtPosition(position) if they need
          * to access the data associated with the selected item.
          *
          * @param parent   The AdapterView where the click happened.
          * @param view     The view within the AdapterView that was clicked (this
          *                 will be a view provided by the adapter)
          * @param position The position of the view in the adapter.
          * @param id       The row id of the item that was clicked.
          */
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         }

         /**
          * Called when a view has been clicked.
          *
          * .
          */
         {


             int position=0;
             String songName=myListViewForSongs.getItemAtPosition(position).toString();

             startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
             .putExtra("songs",mySongs).putExtra("songname",songName)
             .putExtra("pos",position));






         }
     });











}




}



