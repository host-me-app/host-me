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

import java.util.HashMap;
import java.util.Map;

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
    private HashMap<Integer, Integer> indexIDMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (Auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LogInActivity.class));
        } else {
            indexIDMap = new HashMap<>();
            indexIDMap.put(0, R.id.navigation_search);
            indexIDMap.put(1, R.id.navigation_add);
            indexIDMap.put(2, R.id.navigation_favorites);
            indexIDMap.put(3, R.id.navigation_messages);
            indexIDMap.put(4, R.id.navigation_account);
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
        int item = indexIDMap.get(position);
        navView.getMenu().findItem(item).setChecked(true);
    }

    /**
     * Set the current item
     */
    @SuppressLint("NonConstantResourceId")
    private void setCurrentItem(MenuItem item) {
        int itemID = item.getItemId();
        for (Map.Entry<Integer, Integer> entry : indexIDMap.entrySet()) {
            if (itemID == entry.getValue()) {
                viewPager.setCurrentItem(entry.getKey(), false);
            }
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