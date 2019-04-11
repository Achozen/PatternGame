package achozen.rememberme.navigation;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import achozen.rememberme.R;
import achozen.rememberme.fragments.PatternGameFragment;

/**
 * Created by Achozen on 2016-02-27.
 */
public class FragmentNavigator {

    public static void navigateToNextFragment(FragmentActivity activity, PatternGameFragment
            nextFragment) {

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment_place_holder, nextFragment);
        transaction.commit();
    }
}
