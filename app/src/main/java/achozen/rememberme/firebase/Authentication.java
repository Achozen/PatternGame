package achozen.rememberme.firebase;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Achozen on 2016-11-30.
 */

public class Authentication {

    private FirebaseAuth firebaseAuth;
    private Set<FirebaseAuth.AuthStateListener> activeListeners = new HashSet<>();
    private Authentication instance;

    public Authentication getInstance(){
        if(instance == null) {
            instance = new Authentication();
            firebaseAuth =FirebaseAuth.getInstance();
        }
        return instance;
    }
    public void setOnAuthenticateListener(FirebaseAuth.AuthStateListener listener){
        firebaseAuth.addAuthStateListener(listener);
        activeListeners.add(listener);
    }

    public void removeAllListeners(){
        for(FirebaseAuth.AuthStateListener listener: activeListeners){
            firebaseAuth.removeAuthStateListener(listener);
        }
    }


}
