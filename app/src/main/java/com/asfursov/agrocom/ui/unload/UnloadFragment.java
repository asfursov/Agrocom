package com.asfursov.agrocom.ui.unload;

import android.view.View;
import android.widget.Button;

import androidx.navigation.Navigation;

import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.ui.common.TitledFragment;

import butterknife.BindView;

public class UnloadFragment extends TitledFragment {

    public static final String TITLE = "ВЫГРУЗКА";
    @BindView(R.id.buttonEnter)
    Button enterButton;
    @BindView(R.id.buttonLeave)
    Button leaveButton;


    public static UnloadFragment newInstance() {
        return new UnloadFragment();
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToScan(view, OperationId.UNLOAD_START);
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectToScan(view, OperationId.UNLOAD_END);
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

}