package com.asfursov.agrocom.ui.guard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.state.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuardFragment extends Fragment {

    private GuardViewModel mViewModel;
    @BindView(R.id.buttonEnter)
    Button enterButton;
    @BindView(R.id.buttonLeave)
    Button leaveButton;


    public static GuardFragment newInstance() {
        return new GuardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guard_fragment, container, false);

        ButterKnife.bind(this, root);

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
        return root;

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