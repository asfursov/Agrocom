package com.asfursov.agrocom.ui.result;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.state.Constants;
import com.asfursov.agrocom.ui.common.TitledFragment;

import butterknife.BindView;

public class ResultFragment extends TitledFragment {


    @BindView(R.id.resultText)
    TextView resultText;

    @BindView(R.id.result_Ok_Button)
    Button okButton;

    private int returnAction = R.id.action_resultFragment_to_nav_home;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    protected String getTitle() {
        return "РЕЗУЛЬТАТ";
    }

    @Override
    protected int getFragmentId() {
        return R.layout.result_fragment;
    }

    @Override
    protected void processParameters(Bundle arguments) {
        super.processParameters(arguments);
        if (arguments.containsKey(Constants.RETURN_ACTION))
            returnAction = arguments.getInt(Constants.RETURN_ACTION);
        if (arguments.containsKey(Constants.MESSAGE)) {
            resultText.setText(arguments.getString(Constants.MESSAGE));
            resultText.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }
        if (arguments.containsKey(Constants.ERROR)) {
            resultText.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getNavController().navigate(returnAction);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}