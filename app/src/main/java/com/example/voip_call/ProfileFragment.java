package com.example.voip_call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    int[] ids = {R.id.etuser, R.id.etuserid, R.id.etemail, R.id.etmob};
    de.hdodenhof.circleimageview.CircleImageView profileimg;
    EditText[] ets = new EditText[4];
    String[] values = new String[5];
    ImageView camera, gallery;
    AlertDialog alertDialog;
    Button save, update;
    String encodedImage;
    Bitmap bitmap;
    String email;
    int i;
    TextView cgpass;
    Context context;

    ProfileFragment(Context context, String id) {
        this.context = context;
        email = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        update = v.findViewById(R.id.update);
        save = v.findViewById(R.id.save);
        cgpass = v.findViewById(R.id.cgpass);
        profileimg = v.findViewById(R.id.profileimg);

        for (i = 0; i < ets.length; i++) {
            ets[i] = v.findViewById(ids[i]);
            ets[i].setEnabled(false);
        }
        update.setOnClickListener(v1 -> update());
        return v;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            profileimg.setImageBitmap(bitmap);
            Toast.makeText(getContext(), "" + bitmap, Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            profileimg.setImageBitmap(bitmap);
            alertDialog.dismiss();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update() {

        ets[2].setEnabled(false);
        save.animate().alpha(1).setDuration(1000);
        cgpass.animate().alpha(1).setDuration(1000);
        update.animate().alpha(0).setDuration(500);
        update.setEnabled(false);
        save.setEnabled(true);
        cgpass.setEnabled(true);
        profileimg.setEnabled(true);

        profileimg.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Image", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder ld = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams")
            View dialogView = inflater.inflate(R.layout.custom_img, null);
            ld.setView(dialogView);
            alertDialog = ld.create();
            alertDialog.show();
            camera = alertDialog.findViewById(R.id.camera);
            gallery = alertDialog.findViewById(R.id.gallery);
            camera.setOnClickListener(v1 -> {
                Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, 0);
            });
            gallery.setOnClickListener(v12 -> {
                Toast.makeText(getContext(), "gallery", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, 1);
            });
        });

        save.setOnClickListener(v -> {


            save.animate().alpha(0).setDuration(500);
            cgpass.animate().alpha(0).setDuration(500);
            update.animate().alpha(1).setDuration(500);
            save.setEnabled(false);
            update.setEnabled(true);
            profileimg.setEnabled(false);
            cgpass.setEnabled(false);
            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
        });
    }

}
