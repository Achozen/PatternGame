package achozen.rememberme.fragments.startup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import achozen.rememberme.R;
import achozen.rememberme.StartupPresenter;
import achozen.rememberme.firebase.AuthFinishListener;
import achozen.rememberme.fragments.PhaseFinishedListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, AuthFinishListener {

    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.password)
    TextInputEditText passwordEditText;
    @BindView(R.id.email)
    TextInputEditText emailEditText;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private PhaseFinishedListener authFinishListener;

    public static LoginFragment getInstance(PhaseFinishedListener authFinishListener) {
        final LoginFragment fragment = new LoginFragment();
        fragment.authFinishListener = authFinishListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        onAuthListenerSetup();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity().getApplicationContext())
                .enableAutoManage(this.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.login_splash_view,
                container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.login)
    public void onLogingClicked() {
        if (emailEditText.getText() != null && TextUtils.isEmpty(emailEditText.getText().toString().trim())) {
            emailEditText.setError("Email cannot be empty !");
        } else if (emailEditText.getText() != null && !isEmailValid(emailEditText.getText().toString())) {
            emailEditText.setError("Invalid email !");
        }

        if (passwordEditText.getText() != null && TextUtils.isEmpty(passwordEditText.getText().toString().trim())) {
            passwordEditText.setError("Password cannot be empty !");
        } else if (passwordEditText.getText() != null && passwordEditText.getText().length() < 6) {
            passwordEditText.setError("Password must have at least 6 characters");
        }

        if (!TextUtils.isEmpty(emailEditText.getText().toString()) &&
                !TextUtils.isEmpty(passwordEditText.getText().toString()) &&
                isEmailValid(emailEditText.getText().toString()) &&
                passwordEditText.getText().length() >= 6) {
            login(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @OnClick(R.id.gmail_login)
    public void onGmailLogingClicked() {
        signIn();
    }

    @OnClick(R.id.email)
    public void onEmailClicked() {
        emailEditText.setText("");
    }

    @OnClick(R.id.password)
    public void onPasswordClicked() {
        passwordEditText.setText("");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAGTAG", "onConnectionFailed:" + connectionResult);
    }

    private void onAuthListenerSetup() {
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("TAGTAG", "onAuthStateChanged:signed_in:" + user.getUid());
                authFinishListener.onPhaseFinished(StartupPresenter.StartupPhase.AUTHENTICATION);
            } else {

                Log.d("TAGTAG", "onAuthStateChanged:signed_out");
            }
        };
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.getActivity(), task -> {
                    Log.d("TAGTAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("TAGTAG", "signInWithEmail", task.getException());

                        createNewUser(email, password);
                    }
                });
    }

    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.getActivity(), task -> {
                    Log.d("TAGTAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAGTAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.getActivity(), task -> {
                    Log.d("TAGTAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w("TAGTAG", "signInWithCredential", task.getException());
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    @Override
    public void onAuthFinished() {

    }
}
