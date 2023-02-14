package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {


    private TextView mTitleView;
    private TextView mImgDescView;
    private TextView mDescView;
    private ImageView mImageView;
    private TextView mDateView;
    private MyRSSsaxHandler Handler;
    private int mIndex;
    private String mMain_url = "https://www.lemonde.fr/international/rss_full.xml";
    private ArrayList<Article> articleList;
    Executor executor;
    private static final int[] BUTTON_IDS = {
            R.id.buttonNext,
            R.id.buttonPrev
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler = new MyRSSsaxHandler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executor = Executors.newSingleThreadExecutor();
        mIndex = 0;
        Future<?> future = ((ExecutorService) executor).submit(new Runnable() {
            @Override
            public void run() {
                Handler.setUrl(mMain_url);
                Handler.processFeed();
            }
        });
        try {
            future.get();  // wait for the thread to finish
        } catch (ExecutionException e) {
            Log.e("ERROR", "Problem here");
            // handle the exception
        } catch (InterruptedException e) {
            Log.e("ERROR", "Problem here");
            // handle the exception
        }
        Log.d("ONCREATE", " ABOUT TO RESET DISPLAY");
        // resetDisplay(Handler.get_title().toString(),Handler.get_date().toString(), Handler.get_image(), Handler.get_desc().toString());
        articleList = MyRSSsaxHandler.getArticleList();
        resetDisplay(articleList.get(mIndex).get_title(),articleList.get(mIndex).get_date(), articleList.get(mIndex).get_image(), articleList.get(mIndex).get_description(), articleList.get(mIndex).get_imageDescription() );
        Log.d("ARRAYLIST", "Got it");
        ArrayList<Button> buttons = new ArrayList<Button>();
        Button buttonNext = (Button)findViewById(R.id.buttonNext);
        Button buttonPrev = (Button)findViewById(R.id.buttonPrev);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex < articleList.size()-1)
                    mIndex++;
                    resetDisplay(articleList.get(mIndex).get_title(),articleList.get(mIndex).get_date(), articleList.get(mIndex).get_image(), articleList.get(mIndex).get_description(), articleList.get(mIndex).get_imageDescription());
                    }
            });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIndex > 0)
                    mIndex--;
                    resetDisplay(articleList.get(mIndex).get_title(),articleList.get(mIndex).get_date(), articleList.get(mIndex).get_image(), articleList.get(mIndex).get_description(), articleList.get(mIndex).get_imageDescription());
            }
        });
}

    public void resetDisplay(String title, String date, Bitmap image, String description, String imageDescription) {
        Log.d("resetDisplay", " title =" + title + " desc = " + description + " imgdesc = " + imageDescription);
        mTitleView = (TextView) findViewById(R.id.imageTitle);
        mImageView = (ImageView) findViewById(R.id.imageDisplay);
        mImgDescView = (TextView) findViewById(R.id.imageDescription);
        mDescView = (TextView) findViewById(R.id.textDescription);
        mDateView = (TextView) findViewById(R.id.imageDate);
        mTitleView.setText(title);
        mImgDescView.setText(imageDescription);
        mDescView.setText(description);
        mDateView.setText(date);
        mImageView.setImageBitmap(image);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.International)
        {
            Toast.makeText(getApplicationContext(), "International !", Toast.LENGTH_LONG).show();
            Handler.setUrl("https://www.lemonde.fr/international/rss_full.xml");

        } else if (id == R.id.Economie)
        {
            Toast.makeText(getApplicationContext(), "Economie !", Toast.LENGTH_LONG).show();
            Handler.setUrl("https://www.lemonde.fr/economie/rss_full.xml");
        } else if (id == R.id.Sport)
        {
            Toast.makeText(getApplicationContext(), "Sport !", Toast.LENGTH_LONG).show();
            Handler.setUrl("https://www.lemonde.fr/sport/rss_full.xml");
        }
        articleList.clear();
        mIndex = 0;
        Future<?> future = ((ExecutorService) executor).submit(new Runnable() {
            @Override
            public void run() {
                Handler.processFeed();
            }
        });
        try {
            future.get();  // wait for the thread to finish
        } catch (ExecutionException e) {
            Log.e("ERROR", "Problem here");
            // handle the exception
        } catch (InterruptedException e) {
            Log.e("ERROR", "Problem here");
            // handle the exception
        }
        articleList = MyRSSsaxHandler.getArticleList();
        resetDisplay(articleList.get(mIndex).get_title(),articleList.get(mIndex).get_date(), articleList.get(mIndex).get_image(), articleList.get(mIndex).get_description(), articleList.get(mIndex).get_imageDescription() );
        return super.onOptionsItemSelected(item);
    }
}