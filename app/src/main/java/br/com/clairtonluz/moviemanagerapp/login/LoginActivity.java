package br.com.clairtonluz.moviemanagerapp.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import br.com.clairtonluz.moviemanagerapp.R;
import br.com.clairtonluz.moviemanagerapp.config.retrofit.CallbackRest;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private TextInputEditText mUsernamelView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authService = new AuthService(this);
        // Set up the login form.
        mUsernamelView = (TextInputEditText) findViewById(R.id.username);

        mPasswordView = (TextInputEditText) findViewById(R.id.password);
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        // Reset errors.
        mUsernamelView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernamelView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernamelView.setError(getString(R.string.error_field_required));
            focusView = mUsernamelView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            authService.login(username, password).enqueue(new CallbackRest<User>(this) {
                @Override
                protected void onSuccess(Call<User> call, Response<User> response) {
                    User user = response.body();
                    new AuthService(getApplicationContext()).storeCredentials(user, username, password);
                }

                @Override
                protected void onUnsuccess(Call<User> call, Response<User> response) {
                    super.onUnsuccess(call, response);
                    if (response.code() == 401) {
                        mUsernamelView.setError(getString(R.string.invalid_credentials));
                        mPasswordView.setError(getString(R.string.invalid_credentials));
                    }
                }

                @Override
                protected void onComplete(Call<User> call, boolean success) {
                    super.onComplete(call, success);
                    showProgress(false);
                }
            });

//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void loginAction(View view) {
        attemptLogin();
    }
}

