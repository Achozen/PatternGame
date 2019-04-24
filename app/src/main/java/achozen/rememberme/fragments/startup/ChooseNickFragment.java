package achozen.rememberme.fragments.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import achozen.rememberme.R;
import achozen.rememberme.activities.MenuActivity;
import achozen.rememberme.engine.PeferencesUtil;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseNickFragment extends Fragment {

    @BindView(R.id.textInputEditText)
    EditText textInputEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_nick_splash_view,
                container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.confirmButton)
    public void onConfirmClicked() {
        PeferencesUtil.storeInPrefs(getContext(), PeferencesUtil.Preferences.USERNAME, textInputEditText.getText().toString());
        Intent intent = new Intent(getContext(), MenuActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
