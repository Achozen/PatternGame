package achozen.rememberme.navigation;

import achozen.rememberme.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Achozen on 2016-02-27.
 */
public class FragmentNavigator {

    public static void navigateToNextFragment(FragmentActivity activity, Fragment
            nextFragment) {

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.game_fragment_place_holder, nextFragment);
        transaction.commit();
    }
}
