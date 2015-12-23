package pt.ipleiria.dae.eventsmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity {
    private static final String WEB_SERVICE_URI = "http://localhost.com:8080/EventsManagement_RESTWS/webapi";

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Client client;
    private HttpAuthenticationFeature feature;
    protected View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.user_login_form);
        client = ClientBuilder.newClient();
        feature = null;
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;

        feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        //TODO: Search jersey Response
        Response returnedEvents = null;

        try {
            returnedEvents = client.target(WEB_SERVICE_URI)
                    .path("/attendants/all_attendant_events")
                    .request(MediaType.APPLICATION_XML)
                    .get();

            if (returnedEvents == null) {
                Toast.makeText(LoginActivity.this, R.string.string_events_unavailable, Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } catch (Exception e) {
            cancel = true;
            Toast.makeText(LoginActivity.this, "There has been an error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //focusView.requestFocus();
        } else {
            Intent i = new Intent(LoginActivity.this, ListEventsActivity.class);
            //i.putExtra("returnedEvents", returnedEvents);
            startActivity(i);
        }
    }

}

