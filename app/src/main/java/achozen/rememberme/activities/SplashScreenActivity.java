package achozen.rememberme.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import achozen.rememberme.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int VIEWS_FADE_IN_DELAY = 1000;
    private static final int VIEWS_FADE_OUT_DELAY = 3000;
    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.headerImage)
    ImageView headerImage;
    @BindView(R.id.belowHeaderImage)
    ImageView belowHeaderImage;
    @BindView(R.id.loggingLayout)
    LinearLayout loggingLayout;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.email)
    EditText emailEditText;

    private GoogleApiClient mGoogleApiClient;
    private Handler handler;
    private int mLongAnimationDuration;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        handler = new Handler();
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        setupViews();
        mAuth = FirebaseAuth.getInstance();

        onAuthListenerSetup();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


      }
    private void onAuthListenerSetup(){
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("TAGTAG", "onAuthStateChanged:signed_in:" + user.getUid());
                Toast.makeText(SplashScreenActivity.this,"Authenticated",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("TAGTAG", "onAuthStateChanged:signed_out");
            }
        };
    }

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d("TAGTAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Log.w("TAGTAG", "signInWithEmail", task.getException());
                        Toast.makeText(SplashScreenActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                        createNewUser(email,password);
                  }
                });
    }

    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d("TAGTAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Toast.makeText(SplashScreenActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
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
                .addOnCompleteListener(this, task -> {
                    Log.d("TAGTAG", "signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w("TAGTAG", "signInWithCredential", task.getException());
                        Toast.makeText(SplashScreenActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewsInAnimation(){
        handler.postDelayed(() -> {
            headerImage.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(null);

            belowHeaderImage.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(null);

        }, VIEWS_FADE_IN_DELAY);

        }
    private void setupViewsOutAnimation(){
        handler.postDelayed(() -> {
            headerImage.animate().alpha(0f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            headerImage.setVisibility(View.INVISIBLE);

                        }
                    });
            belowHeaderImage.animate().alpha(0f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            belowHeaderImage.setVisibility(View.INVISIBLE);
                        }
                    });
            loggingLayout.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loggingLayout.setVisibility(View.VISIBLE);
                }
            });;
        }, VIEWS_FADE_OUT_DELAY);
    }
    public void setupViews(){
        headerImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.header));
        belowHeaderImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.below_header));

        headerImage.setAlpha(0f);
        belowHeaderImage.setAlpha(0f);
        loggingLayout.setAlpha(0f);

        headerImage.setVisibility(View.VISIBLE);
        belowHeaderImage.setVisibility(View.VISIBLE);

        setupViewsInAnimation();
        setupViewsOutAnimation();
    }

    @OnClick(R.id.login)
    public void onLogingClicked(){
        login(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }
    @OnClick(R.id.gmail_login)
    public void onGmailLogingClicked(){
        signIn();
    }
    @OnClick(R.id.email)
    public void onEmailClicked(){
        emailEditText.setText("");
    }
    @OnClick(R.id.password)
    public void onPasswordClicked(){
        passwordEditText.setText("");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAGTAG", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}
