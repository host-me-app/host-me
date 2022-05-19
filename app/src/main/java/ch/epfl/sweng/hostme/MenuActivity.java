package ch.epfl.sweng.hostme;

import static ch.epfl.sweng.hostme.utils.Constants.REQ_IMAGE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.Objects;

import ch.epfl.sweng.hostme.ui.IOnBackPressed;
import ch.epfl.sweng.hostme.ui.account.AccountFragment;
import ch.epfl.sweng.hostme.ui.add.AddFragment;
import ch.epfl.sweng.hostme.ui.favorites.FavoritesFragment;
import ch.epfl.sweng.hostme.ui.messages.MessageService;
import ch.epfl.sweng.hostme.ui.messages.MessagesFragment;
import ch.epfl.sweng.hostme.ui.search.SearchFragment;
import ch.epfl.sweng.hostme.utils.Constants;
import ch.epfl.sweng.hostme.utils.ListImage;

public class MenuActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private static final String PREF_USER_NAME = "username";
    boolean isFromNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        if (PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_USER_NAME, "").length() == 0) {
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

            /*isFromNotif = getIntent().getBooleanExtra(Constants.FROM_NOTIF, false);
            if(isFromNotif){
                View view = navView.findViewById(R.id.navigation_messages);
                view.performClick();
                System.out.println("NOTIF");
            }*/
        }

    }

    /**
     * Set the corresponding Item to checked
     * @param position
     * @param navView
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
     * set the current item
     * @param item
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
     * @param viewPager
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_IMAGE) {
            ListImage.onAcceptImage(resultCode, intent.getData());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, MessageService.class));
    }
}