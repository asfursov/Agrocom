package com.asfursov.agrocom.ui.guard.enter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.asfursov.agrocom.R;

import butterknife.BindView;

public class EnterFragment extends Fragment {

    private EnterViewModel mViewModel;
    @BindView(R.id.scanBadgeButtonEnter)
    ImageButton scanBadgeButton;
    @BindView(R.id.informationTextEnter)
    TextView information;
    @BindView(R.id.editTextPlate)
    EditText plateNumber;
    @BindView(R.id.progressBarEnter)
    ProgressBar progressBar;
    @BindView(R.id.buttonCommitEnter)
    Button buttonCommit;

    public static EnterFragment newInstance() {
        return new EnterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enter_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EnterViewModel.class);
        // TODO: Use the ViewModel
    }

}