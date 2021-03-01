package com.asfursov.agrocom.ui.operation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.state.AppData;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class OperationFragment extends com.asfursov.agrocom.ui.common.ScanningFormFragment {

    public static final String SCAN_DRIVER = "Отсканируйте код с браслета:";
    @BindView(R.id.buttonCommitOperation)
    Button buttonCommit;
    private OperationId operationId;
    private OperationViewModel mViewModel;

    @BindView(R.id.editTextPlate)
    EditText plateNumber;

    public static OperationFragment newInstance() {
        return new OperationFragment();
    }

    @Override
    protected void processParameters(Bundle arguments) {
        super.processParameters(arguments);
        operationId = AppData.getInstance().getOperationId();
    }

    @Override
    protected String getTitle() {
        return operationId.getName().toUpperCase();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    protected int getInformationTextView() {
        return R.id.informationTextOperation;
    }

    @NotNull
    @Override
    protected String getInitialText() {
        return SCAN_DRIVER;
    }

    @Override
    protected int getScanButtonView() {
        return R.id.scanBadgeButtonOperation;
    }

    @Override
    protected int getScanningReturnAction() {
        return R.id.action_barcodeScanningFragment_to_enterFragment;
    }

    @Override
    protected int getProgressBar() {
        return R.id.progressBarOperation;
    }


    @Override
    protected int getFragmentId() {
        return R.layout.operation_fragment;
    }
}