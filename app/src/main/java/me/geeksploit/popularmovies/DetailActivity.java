package me.geeksploit.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import me.geeksploit.popularmovies.fragments.DetailsFragment;
import me.geeksploit.popularmovies.model.MovieModel;

public class DetailActivity extends AppCompatActivity implements DetailsFragment.OnClickFavoritesListener {

    public static final String EXTRA_MOVIE = "MOVIE_DATA";

    private MovieModel mMovie;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment targetFragment;

            switch (item.getItemId()) {
                case R.id.navigation_overview:
                    targetFragment = DetailsFragment.newInstance(mMovie);
                    break;
                default:
                    throw new UnsupportedOperationException(getString(R.string.error_not_implemented, item));
            }

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.detail_fragment, targetFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent.getExtras() == null) return;

        mMovie = (MovieModel) intent.getExtras().get(EXTRA_MOVIE);

        BottomNavigationView navigation = findViewById(R.id.detail_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (null == savedInstanceState)
            navigation.setSelectedItemId(R.id.navigation_overview);
    }

    @Override
    public void onClickFavorites(Uri uri) {
        // TODO: handle favorites button click
    }
}
