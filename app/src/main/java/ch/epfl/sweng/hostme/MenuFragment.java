package ch.epfl.sweng.hostme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ch.epfl.sweng.hostme.ui.account.AccountFragment;
import ch.epfl.sweng.hostme.ui.add.AddFragment;
import ch.epfl.sweng.hostme.ui.favorites.FavoritesFragment;
import ch.epfl.sweng.hostme.ui.messages.MessagesFragment;
import ch.epfl.sweng.hostme.ui.search.SearchFragment;

public class MenuFragment extends Fragment {

    private ViewPager2 viewPager;
    private View root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_menu1, container, false);
        BottomNavigationView navView = root.findViewById(R.id.nav_view);
        viewPager = root.findViewById(R.id.view_pager);
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
        return root;
    }

    /**
     * set the current item
     *
     * @param item
     */
    @SuppressLint("NonConstantResourceId")
    private void setCurrentItem(MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_search:
                viewPager.setCurrentItem(0, false);
                selectedFragment = new SearchFragment();
                break;
            case R.id.navigation_add:
                viewPager.setCurrentItem(1, false);
                selectedFragment = new AddFragment();
                break;
            case R.id.navigation_favorites:
                viewPager.setCurrentItem(2, false);
                selectedFragment = new FavoritesFragment();
                break;
            case R.id.navigation_messages:
                viewPager.setCurrentItem(3, false);
                selectedFragment = new MessagesFragment();
                break;
            case R.id.navigation_account:
                viewPager.setCurrentItem(4, false);
                selectedFragment = new AccountFragment();
                break;
        }

        if (selectedFragment != null) {
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
    }

    /**
     * Set the corresponding Item to checked
     *
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
     * set up the viewPage
     * @param viewPager
     */
    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());

        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new FavoritesFragment());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new MessagesFragment());
        adapter.addFragment(new AccountFragment());

        viewPager.setAdapter(adapter);
    }

}