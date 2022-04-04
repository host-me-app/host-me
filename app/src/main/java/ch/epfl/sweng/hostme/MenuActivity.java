package ch.epfl.sweng.hostme;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import ch.epfl.sweng.hostme.databinding.ActivityMenu1Binding;
import ch.epfl.sweng.hostme.ui.account.AccountFragment;
import ch.epfl.sweng.hostme.ui.add.AddFragment;
import ch.epfl.sweng.hostme.ui.favorites.FavoritesFragment;
import ch.epfl.sweng.hostme.ui.messages.MessagesFragment;
import ch.epfl.sweng.hostme.ui.search.SearchFragment;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenu1Binding binding;
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        binding = ActivityMenu1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.view_pager);

        navView.setOnItemSelectedListener(item -> {
            setCurrentItem(item);
            return true;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
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
        });

        setupViewPager(viewPager);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_add, R.id.navigation_favorites,
                R.id.navigation_messages, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_menu1);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/
    }

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

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new FavoritesFragment());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new MessagesFragment());
        adapter.addFragment(new AccountFragment());

        viewPager.setAdapter(adapter);
    }

}