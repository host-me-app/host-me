package ch.epfl.sweng.hostme.activities;

import static ch.epfl.sweng.hostme.utils.Constants.FROM_NOTIFICATION;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sweng.hostme.R;
import ch.epfl.sweng.hostme.database.Auth;
import ch.epfl.sweng.hostme.fragments.AccountFragment;
import ch.epfl.sweng.hostme.fragments.AddFragment;
import ch.epfl.sweng.hostme.fragments.FavoritesFragment;
import ch.epfl.sweng.hostme.fragments.MessagesFragment;
import ch.epfl.sweng.hostme.fragments.SearchFragment;
import ch.epfl.sweng.hostme.menu.ViewPagerAdapter;
import ch.epfl.sweng.hostme.messages.MessageService;
import ch.epfl.sweng.hostme.utils.IOnBackPressed;

public class MenuActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (Auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LogInActivity.class));
        } else {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            viewPager = findViewById(R.id.view_pager);
            viewPager.setOffscreenPageLimit(5);

            navView.setOnItemSelectedListener(item -> {
                setCurrentItem(item);
                return true;
            });

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCheckedItem(position, navView);
                }
            });

            setupViewPager(viewPager);

            if (getIntent().getBooleanExtra(FROM_NOTIFICATION, false)) {
                findViewById(R.id.navigation_messages).performClick();
            }
        }
    }

    /**
     * Set the corresponding Item to checked
     */
    private void setCheckedItem(int position, BottomNavigationView navView) {
        switch (position) {
            case 0:
                navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
                break;
            case 1:
                navView.getMenu().findItem(R.id.navigation_add).setChecked(true);
                break;
            case 2:
                navView.getMenu().findItem(R.id.navigation_favorites).setChecked(true);
                break;
            case 3:
                navView.getMenu().findItem(R.id.navigation_messages).setChecked(true);
                break;
            case 4:
                navView.getMenu().findItem(R.id.navigation_account).setChecked(true);
                break;
        }
    }

    /**
     * Set the current item
     */
    @SuppressLint("NonConstantResourceId")
    private void setCurrentItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_search:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.navigation_add:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.navigation_favorites:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.navigation_messages:
                viewPager.setCurrentItem(3, false);
                break;
            case R.id.navigation_account:
                viewPager.setCurrentItem(4, false);
                break;
        }
    }

    /**
     * set up the viewPage
     */
    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new FavoritesFragment());
        adapter.addFragment(new MessagesFragment());
        adapter.addFragment(new AccountFragment());

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, MessageService.class));
    }
}