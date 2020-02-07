//package com.example.voip_call;
//
//import android.Manifest;
//import android.content.Intent;
//import android.opengl.GLSurfaceView;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.opentok.android.OpentokError;
//import com.opentok.android.Publisher;
//import com.opentok.android.PublisherKit;
//import com.opentok.android.Session;
//import com.opentok.android.Stream;
//import com.opentok.android.Subscriber;
//
//import pub.devrel.easypermissions.AfterPermissionGranted;
//import pub.devrel.easypermissions.EasyPermissions;
//
//public class VideoCallScreen extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
//    final int RC_VIDEO_APP_PERM = 124;
//    String LOG_TAG = MainActivity.class.getSimpleName();
//    int RC_SETTINGS_SCREEN_PERM = 123;
//    String API_KEY = "46502612";
//    String SESSION_ID = "2_MX40NjUwMjYxMn5-MTU4MDgwMDI1NTQ1Mn45TnBjdnkvck1Da21DM3Q2dTZSdjVOSjB-fg";
//    String TOKEN = "T1==cGFydG5lcl9pZD00NjUwMjYxMiZzaWc9MTFlNGM3ODc1NjM3MTUxZjczYTAyNGRhZTU5OGQ5MTk1NWZjZDY3YjpzZXNzaW9uX2lkPTJfTVg0ME5qVXdNall4TW41LU1UVTRNRGd3TURJMU5UUTFNbjQ1VG5CamRua3ZjazFEYTIxRE0zUTJkVFpTZGpWT1NqQi1mZyZjcmVhdGVfdGltZT0xNTgwODAwMjk0Jm5vbmNlPTAuNzIxNTA0MDA3Njc3MzEzOCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTgwODg2NjkxJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
//    Session mSession;
//    Publisher mPublisher;
//    Subscriber mSubscriber;
//    ImageView mutecall, switchcam;
//    TextView rec_name;
//
//    private FrameLayout mPublisherViewContainer;
//    private FrameLayout mSubscriberViewContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_call_screen);
//        mutecall = findViewById(R.id.mutecall);
//        switchcam = findViewById(R.id.switchcam);
//        rec_name = findViewById(R.id.rec_name);
//
//        requestPermissions();
//
//        Intent it = getIntent();
//        rec_name.setText(it.getStringExtra("name"));
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
//    private void requestPermissions() {
//        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
//        if (EasyPermissions.hasPermissions(this, perms))
//        {
//            mPublisherViewContainer = findViewById(R.id.publisher_container);
//            mPublisherViewContainer = findViewById(R.id.publisher_container);
//            mutecall = findViewById(R.id.mutecall);
//            switchcam = findViewById(R.id.switchcam);
//
//            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
//            mSession.setSessionListener(this);
//            mSession.connect(TOKEN);
//
//        } else {
//            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
//        }
//    }
//
//    // SessionListener methods
//
//    @Override
//    public void onConnected(Session session)
//    {
//        Log.i(LOG_TAG, "Session Connected");
//        mPublisher = new Publisher.Builder(this).name("Bob").build();
//        mPublisher.setPublisherListener(this);
//        mPublisherViewContainer.addView(mPublisher.getView());
//        if (mPublisher.getView() instanceof GLSurfaceView)
//        {
//            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
//        }
//        mSession.publish(mPublisher);
//    }
//
//    @Override
//    public void onDisconnected(Session session) {
//        Log.i(LOG_TAG, "Session Disconnected");
//    }
//
//    @Override
//    public void onStreamReceived(Session session, Stream stream) {
//        Log.i(LOG_TAG, "Stream Received");
//        if (mSubscriber == null) {
//            mSubscriber = new Subscriber.Builder(this, stream).build();
//            mSession.subscribe(mSubscriber);
//            mSubscriberViewContainer.addView(mSubscriber.getView());
//
//            switchcam.setOnClickListener(v -> {
//                if (mPublisher == null) {
//                    return;
//                }
//                mPublisher.swapCamera();
//            });
//        }
//    }
//
//    @Override
//    public void onStreamDropped(Session session, Stream stream) {
//        Log.i(LOG_TAG, "Stream Dropped");
//
//        if (mSubscriber != null) {
//            mSubscriber = null;
//            mSubscriberViewContainer.removeAllViews();
//        }
//    }
//
//    @Override
//    public void onError(Session session, OpentokError opentokError) {
//        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
//    }
//
//    // PublisherListener methods
//
//    @Override
//    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
//        Log.i(LOG_TAG, "Publisher onStreamCreated");
//    }
//
//    @Override
//    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
//        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
//    }
//
//    @Override
//    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
//        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
//    }
//
//    public void back(View view) {
//        Intent it = new Intent(this, LandingPage.class);
//        startActivity(it);
//    }
//}
//
