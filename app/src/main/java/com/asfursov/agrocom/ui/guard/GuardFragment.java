package com.asfursov.agrocom.ui.guard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.ui.common.TitledFragment;

import butterknife.BindView;

public class GuardFragment extends TitledFragment {

    private GuardViewModel mViewModel;
    @BindView(R.id.buttonEnter)
    Button enterButton;
    @BindView(R.id.buttonLeave)
    Button leaveButton;


    public static GuardFragment newInstance() {
        return new GuardFragment();
    }

    @Override
    protected String getTitle() {
        return "ОХРАНА";
    }

    @Override
    protected void initialize() {
        super.initialize();
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToScan(view, OperationId.ENTER);
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToScan(view, OperationId.LEAVE);
            }
        });
    }


    @Override
    protected int getFragmentId() {
        return R.layout.guard_fragment;
    }

    private void RedirectToScan(View view, OperationId operationId) {
        AppData.getInstance().setOperationId(operationId);
        Navigation.findNavController(view).navigate(R.id.action_guardFragment_to_operationFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GuardViewModel.class);
        // TODO: Use the ViewModel
    }

}