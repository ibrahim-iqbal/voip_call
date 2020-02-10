package com.example.voip_call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("SetTextI18n")
public class CallScreen extends AppCompatActivity {
    final int RC_AUDIO = 124;
    AppCompatImageButton record, hold, addcall, videoswitch, mute, speaker;
    androidx.cardview.widget.CardView endbtn;
    TextView state, recipient_id;
    Call call;
    String num;
    SinchClient sinchClient;
    MediaRecorder recorder;
    private File audiofile;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);

        record = findViewById(R.id.record);
        recipient_id = findViewById(R.id.recipient_id);
        state = findViewById(R.id.state);
        endbtn = findViewById(R.id.endbtn);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

        Intent it = getIntent();
        num = it.getStringExtra("callerid");

        sinchClient = Sinch.getSinchClientBuilder().context(this)
                .userId("145896")
                .applicationKey("129abf9f-2871-48d4-86bf-b4745ce5833b")
                .applicationSecret("7YHhXfCTyE+oyWT7e5ErHQ==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.start();

        if (requestPermissions()) {
            Toast.makeText(this, "Call Start", Toast.LENGTH_SHORT).show();
            if (call == null) {
                recipient_id.setText(num);
                call = sinchClient.getCallClient().callPhoneNumber("+46000000000");
                state.setText("");

                call.addCallListener(new SinchCallListener());
            } else {
                call.hangup();
            }
        } else {
            Toast.makeText(this, "Call Not Started", Toast.LENGTH_SHORT).show();
        }

        endbtn.setOnClickListener(v ->
        {
            SinchCallListener listener = new SinchCallListener();
            listener.onCallEnded(call);
        });


        record.setOnClickListener(v -> {
            startMediaRecorder(MediaRecorder.AudioSource.VOICE_CALL);
        });
    }

    private boolean startMediaRecorder(final int audioSource) {
        recorder = new MediaRecorder();
        try {
            recorder.reset();
            recorder.setAudioSource(audioSource);
            recorder.setAudioSamplingRate(8000);
            recorder.setAudioEncodingBitRate(12200);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            String fileName = audiofile.getAbsolutePath();
            recorder.setOutputFile(fileName);

            MediaRecorder.OnErrorListener errorListener = (arg0, arg1, arg2) -> {
                terminateAndEraseFile();
            };
            recorder.setOnErrorListener(errorListener);

            MediaRecorder.OnInfoListener infoListener = (arg0, arg1, arg2) -> {
                terminateAndEraseFile();
            };
            recorder.setOnInfoListener(infoListener);

            recorder.prepare();
            // Sometimes prepare takes some time to complete
            Thread.sleep(2000);
            recorder.start();
            isRecordStarted = true;
            return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_AUDIO)
    private boolean requestPermissions() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_AUDIO, perms);
            return false;
        }
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            state.setText("Call Ended");
            endedCall.hangup();
            sinchClient.stopListeningOnActiveConnection();
            sinchClient.terminate();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            Intent it = new Intent(CallScreen.this, LandingPage.class);
            startActivity(it);
            finish();
        }

        @Override
        public void onCallEstablished(Call establishedCall) {

            state.setText("Connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {

            state.setText("Ringing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }
}
