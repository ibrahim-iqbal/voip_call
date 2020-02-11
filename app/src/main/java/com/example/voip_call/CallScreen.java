package com.example.voip_call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("SetTextI18n")
public class CallScreen extends AppCompatActivity
{
    private final int RC_AUDIO = 124;
    private TextView state;
    private Call call;
    private SinchClient sinchClient;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);

        TextView recipient_id = findViewById(R.id.recipient_id);
        state = findViewById(R.id.state);
        LinearLayout endbtn = findViewById(R.id.endbtn);
        AppCompatImageButton speaker = findViewById(R.id.speaker);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

        Intent it = getIntent();
        String num = it.getStringExtra("callerid");

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
            }
            else {
                call.hangup();
            }
        }
        else {
            Toast.makeText(this, "Call Not Started", Toast.LENGTH_SHORT).show();
        }
        endbtn.setOnClickListener(v ->
        {
            SinchCallListener listener = new SinchCallListener();
            listener.onCallEnded(call);
        });
        speaker.setOnClickListener(v -> setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            onBackPressed();
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
