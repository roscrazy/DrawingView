package com.rocrazy.signview;

import java.io.File;

import com.roscrazy.signview.R;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SampleActivity extends Activity implements OnClickListener {


    private static final int REQUEST_CODE_DRAWING = 1;
    private ImageView ivImage;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_activity);
        findViewById(R.id.button).setOnClickListener(this);
        ivImage = (ImageView) findViewById(R.id.iv_image);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.button) {
            Intent intent = new Intent(this, SignActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DRAWING);
        }
    }

    ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_DRAWING: {
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = data.getStringExtra(SignActivity.KEY_DATA);
                    if (filePath != null)
                        ivImage.setImageURI(Uri.fromFile(new File(filePath)));
                } else {
                    Toast.makeText(this, "Opp.. there is no image !!!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
