package ch.epfl.sweng.hostme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserCreationPage4 extends AppCompatActivity {

    public static final String MAIL = "Mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_mail);

        EditText mail = findViewById(R.id.mail);

        Button nextMailButt = findViewById(R.id.nextButtonMail);
        nextMailButt.setOnClickListener(view -> {
            String mailText = mail.getText().toString();
            UserCreationPage5.DATA.put(MAIL, mailText);
            goToPage5();
        });
    }

    private void goToPage5() {
        Intent intent = new Intent(UserCreationPage4.this, UserCreationPage5.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
    }
}
