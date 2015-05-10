package com.rocrazy.signview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.roscrazy.signview.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SignActivity extends ActionBarActivity {

    private static final String FRAGMENT_TAG = "tag";
    public static final String KEY_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_layout);

        if (savedInstanceState == null) {
            SignFragment fragment = new SignFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.flContainer, fragment, FRAGMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSave:{
                /**
                 * For best practice we should open a thread to save the image. But this project just a sample code, the code should be simple.
                 */
                SignFragment fragment = (SignFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                if (fragment != null) {
                    File file = null;
                    try {
                        FileOutputStream outputStream = null;
                        try {


                            String filePath = getIntent().getStringExtra(KEY_DATA);
                            // if there is no file path then save it to cache dir
                            if(filePath == null)
                                file = File.createTempFile("temp", "png", getCacheDir());
                            else
                                file = new File(filePath);

                            outputStream = new FileOutputStream(file);
                            fragment.save(outputStream);
                        } finally {
                            // Close the stream if it had init.
                            if (outputStream != null)
                                outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent result = new Intent();
                    if(file != null) {
                        result.putExtra(KEY_DATA, file.getAbsolutePath());
                        this.setResult(RESULT_OK, result);
                    }

                    finish();

                }

                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }


}
