package ch.epfl.sweng.hostme.chat;

import ch.epfl.sweng.hostme.users.User;

public interface ConversionListener {
    void onConversionClicked(User user);
}
