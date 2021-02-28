package com.asfursov.agrocom.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultFragment extends Fragment {


    @BindView(R.id.resultText)
    TextView resultText;

    @BindView(R.id.result_Ok_Button)
    Button okButton;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.result_fragment, container, false);
        ButterKnife.bind(this,root);
        Initialize();
        return root;
    }

    private void Initialize() {
        Bundle arg = getArguments();
        if(arg.containsKey(Constants.MESSAGE)){
            resultText.setText(arg.getString(Constants.MESSAGE));
            resultText.setTextColor(ContextCompat.getColor(getContext(),R.color.green));
        }
        if(arg.containsKey(Constants.ERROR)){
            resultText.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).UpdateMenu();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}