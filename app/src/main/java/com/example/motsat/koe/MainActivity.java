package com.example.motsat.koe;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.widget.Button;
import android.view.View;
import android.media.MediaRecorder;
import java.io.File;
import java.io.IOException;

import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;

// sample
// http://puyooboe.blogspot.com/2016/04/androidmediarecordermediaplayer.html
// https://ptyagicodecamp.github.io/requesting-audio-permission-at-runtime.html

public class MainActivity extends AppCompatActivity {

    private MediaRecorder mediarecorder; //録音用のメディアレコーダークラス
    static final String filePath = "/sdcard/sampleWav.wav"; //録音用のファイルパス

    public void playRecordedAudio() throws IOException {
        String file = getFilesDir().getPath() + "/sample.3gp";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(file);
        mediaPlayer.prepare(); // no need to call prepare(); create() does that for you
        mediaPlayer.setLooping(true);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
        Toast.makeText(MainActivity.this, "started play", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // see https://akira-watson.com/android/imageview.html
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.mamanokoe);
        //mediaPlayer.setLooping(true);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you


        //Button button1 = (Button)findViewById(R.id.startRecordButton);
        //button1.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // text1.setText("はろーわーるど！");
        //        try{
        //            File mediafile = new File(filePath);
        //            if(mediafile.exists()) {
        //                //ファイルが存在する場合は削除する
        //                mediafile.delete();
        //            }
        //            mediafile = null;
        //            mediarecorder = new MediaRecorder();
        //            //マイクからの音声を録音する
        //            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
        //            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //            //音声のエンコーダーも合わせてdefaultにする
        //            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //            //ファイルの保存先を指定
        //            mediarecorder.setOutputFile(filePath);
        //            //録音の準備をする
        //            mediarecorder.prepare();
        //            //録音開始
        //            mediarecorder.start();
        //        } catch(Exception e){
        //            e.printStackTrace();
        //        }
        //    }
        //});
        //ここまで入力
    }
    public void recordAudio() {
        try{
            String file = getFilesDir().getPath() + "/sample.3gp";
            File mediafile = new File(file);
            if(mediafile.exists()) {
                //ファイルが存在する場合は削除する
                mediafile.delete();
            }
            //mediafile = null;
            mediarecorder = new MediaRecorder();
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(file);
            //録音の準備をする
            mediarecorder.prepare();
            //録音開始
            mediarecorder.start();
            Toast.makeText(MainActivity.this, " record start", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            Toast.makeText(MainActivity.this, " record audio failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void startRecord(View view){
        requestAudioPermissions();
    }

    //停止
    public void stopRecord(View view){
        if(mediarecorder == null){
            Toast.makeText(MainActivity.this, "mediarecorder = null", Toast.LENGTH_SHORT).show();
        }else{
            try{
            //録音停止
            mediarecorder.stop();
            mediarecorder.reset();
            mediarecorder.release();
            mediarecorder = null;
            Toast.makeText(MainActivity.this, "mediarecorder stoped", Toast.LENGTH_SHORT).show();
            playRecordedAudio();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
    }
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
            recordAudio();
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    recordAudio();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
