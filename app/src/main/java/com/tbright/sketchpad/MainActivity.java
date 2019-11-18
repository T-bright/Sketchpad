package com.tbright.sketchpad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.tbright.sketchpad.utils.BitmapUtils;
import com.tbright.sketchpad.view.PathView;
import com.tbright.sketchpad.view.ScaleSketchView;

public class MainActivity extends AppCompatActivity {
    private static final int PICTURE_REQUEST_GALLERY_PERMISSION = 120;
    private static final int PICTURE_REQUEST_GALLERY = 110;
    private ScaleSketchView pathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PICTURE_REQUEST_GALLERY_PERMISSION);
        pathView = (ScaleSketchView) findViewById(R.id.pathView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PICTURE_REQUEST_GALLERY_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    public void selectPicture(View view) {
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, PICTURE_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data == null)
                return;
            Bitmap resultBimtap = BitmapUtils.getBitmapPathFromData(data, getApplicationContext());
            pathView.setBackgroundBitmap(resultBimtap);
        }
    }
}
