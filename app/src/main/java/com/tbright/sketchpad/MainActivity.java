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

import com.tbright.sketchpad.listactivity.ListActivity;
import com.tbright.sketchpad.scale.ScaleActivity;
import com.tbright.sketchpad.utils.BitmapUtils;
import com.tbright.sketchpad.view.scaledrawinboard.ScaleSketchView;
import com.tbright.sketchpad.view.pinchimageview.PinchImageView;

public class MainActivity extends AppCompatActivity {
    private static final int PICTURE_REQUEST_GALLERY_PERMISSION = 120;
    private static final int PICTURE_REQUEST_GALLERY = 110;
    private ScaleSketchView pathView;
    private PinchImageView one;
    private PinchImageView two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PICTURE_REQUEST_GALLERY_PERMISSION);
//        pathView = (ScaleSketchView) findViewById(R.id.pathView);

        one = (PinchImageView) findViewById(R.id.one);
        two = (PinchImageView) findViewById(R.id.two);
        one.addOuterMatrixChangedListener(new PinchImageView.OuterMatrixChangedListener() {
            @Override
            public void onOuterMatrixChanged(PinchImageView pinchImageView) {
                two.outerMatrixTo(pinchImageView.getOuterMatrix(null),200);
            }
        });
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
        int type = 3;
        if(type == 1){
            startActivity(new Intent(this, ListActivity.class));
        }else if(type == 2){
            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(picture, PICTURE_REQUEST_GALLERY);
        }else {
            startActivity(new Intent(this, ScaleActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data == null)
                return;
            Bitmap resultBimtap = BitmapUtils.getBitmapPathFromData(data, getApplicationContext());
//            pathView.setBackgroundBitmap(resultBimtap);
        }
    }
}
