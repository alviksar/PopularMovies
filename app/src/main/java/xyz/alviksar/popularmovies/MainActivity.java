package xyz.alviksar.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
Displays in the main layout via a grid of their corresponding movie poster thumbnails.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
