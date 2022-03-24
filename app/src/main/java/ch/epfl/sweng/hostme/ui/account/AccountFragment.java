package ch.epfl.sweng.hostme.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import ch.epfl.sweng.hostme.FragmentCreationPage3;
import ch.epfl.sweng.hostme.FragmentCreationPage5;
import ch.epfl.sweng.hostme.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ch.epfl.sweng.hostme.WalletFragment;
import ch.epfl.sweng.hostme.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.account_profile, container, false);
        Button wallet_button = root.findViewById(R.id.wallet_button);
        wallet_button.setOnClickListener(v -> {
            goToWalletFragment();
        });
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Go to wallet fragment
     */
    private void goToWalletFragment() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_menu1, new WalletFragment());
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}