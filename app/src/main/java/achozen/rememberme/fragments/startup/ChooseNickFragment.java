package achozen.rememberme.fragments.startup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import achozen.rememberme.R;
import achozen.rememberme.StartupPresenter;
import achozen.rememberme.engine.PeferencesUtil;
import achozen.rememberme.fragments.PhaseFinishedListener;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseNickFragment extends Fragment {

    private PhaseFinishedListener phaseFinishedListener;
    @BindView(R.id.textInputEditText)
    EditText textInputEditText;

    public static ChooseNickFragment getInstance(PhaseFinishedListener phaseFinishedListener) {
        final ChooseNickFragment fragment = new ChooseNickFragment();
        fragment.phaseFinishedListener = phaseFinishedListener;
        return fragment;
    }

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
        if (textInputEditText.getText() != null && TextUtils.isEmpty(textInputEditText.getText().toString().trim())) {
            textInputEditText.setError("Username cannot be empty !");
        } else {
            PeferencesUtil.storeInPrefs(getContext(), PeferencesUtil.Preferences.USERNAME, textInputEditText.getText().toString());
            phaseFinishedListener.onPhaseFinished(StartupPresenter.StartupPhase.NICKNAME_CHOOSE);
        }

    }

}
