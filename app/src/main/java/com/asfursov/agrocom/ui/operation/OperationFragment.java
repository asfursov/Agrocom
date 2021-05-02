package com.asfursov.agrocom.ui.operation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.asfursov.agrocom.MainActivity;
import com.asfursov.agrocom.R;
import com.asfursov.agrocom.model.EnterLeaveRequest;
import com.asfursov.agrocom.model.OperationAllowedRequest;
import com.asfursov.agrocom.model.OperationAllowedResponse;
import com.asfursov.agrocom.model.OperationId;
import com.asfursov.agrocom.model.OperationResponse;
import com.asfursov.agrocom.model.Platform;
import com.asfursov.agrocom.model.PlatformAvailableRequest;
import com.asfursov.agrocom.model.PlatformAvailableResponse;
import com.asfursov.agrocom.model.VehicleData;
import com.asfursov.agrocom.network.NetworkHelper;
import com.asfursov.agrocom.state.AppData;
import com.asfursov.agrocom.state.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.asfursov.agrocom.state.Constants.ERROR;
import static com.asfursov.agrocom.state.Constants.MESSAGE;

public class OperationFragment extends com.asfursov.agrocom.ui.common.ScanningFormFragment {

    public static final String SCAN_DRIVER = "Отсканируйте код с браслета:";
    public static final String NOT_ALLOWED = "НЕ РАЗРЕШЕНО\n";
    public static final String DOC_DATA_TEMPLATE = "Документ %s №%s.\n" +
            "%s\n";
    public static final String PLATFORM_BUSY_TEMPLATE = "%s.\n" +
            "ЗАНЯТА %s\n%s\n";
    public static final String WRONG_PLATFORM = "Платформа определена некорректно.\n Повторите сканирование";
    public static final String CHECKING_BARCODE = "Проверяем штрихкод";
    public static final String OPERATION_REJECTED = "Операция '%s' завершена c ошибкой\n%s";
    public static final String OPERATION_SUCCESSFULL = "Операция '%s' успешно завершена";
    private static final String SCAN_PLATFORM = "Просканируйте код платформы:";
    public static final String NO_ANALYZIS = "НЕТ АНАЛИЗА\n";
    public static final String ANALYZIS_RESULT = "Влажность:%.1f\n" +
            "Сорность:%.1f\n";
    public static final String TRAILER_SEPARATELY = "ПРИЦЕП ОТДЕЛЬНО!!\n";
    public static final String TRAILER = "ПРИЦЕП";
    public static final String MAINDOC = "ОСНОВНОЙ";
    @BindView(R.id.buttonCommitOperation)
    Button buttonCommit;

    private OperationId operationId;
    @BindView(R.id.plateConfirmGroup)
    Group plateGroup;

    @BindView(R.id.editTextPlate)
    EditText plateNumber;
    private VehicleData vehicle;
    private Platform platform;

    @Override
    protected void initialize() {
        super.initialize();

        plateGroup.setVisibility(View.GONE);

        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitOperation();
            }
        });
    }

    private void commitOperation() {
        startProgressIndicator();
        NetworkHelper.getInstance().getAPI().operate(
                new EnterLeaveRequest(vehicle,
                        AppData.getInstance().getUser(), plateNumber.getText().toString(),
                        operationId, platform)
        ).enqueue(new Callback<OperationAllowedResponse>() {
            @Override
            public void onResponse(Call<OperationAllowedResponse> call, Response<OperationAllowedResponse> response) {
                stopProgressIndicator();

                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isAllowed())
                        processConfirmed(response.body());
                    else
                        processRejected(response.body().getMessage());
                } else
                    processRejected(response.message());

            }

            @Override
            public void onFailure(Call<OperationAllowedResponse> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\r+" + t.getMessage());


            }
        });

    }

    public static OperationFragment newInstance() {
        return new OperationFragment();
    }

    @Override
    protected void processParameters(Bundle arguments) {
        super.processParameters(arguments);
        operationId = AppData.getInstance().getOperationId();
        buttonCommit.setText(operationId.getName());
        platform = AppData.getInstance().getPlatform();
    }

    @Override
    protected String getTitle() {
        return operationId.getName().toUpperCase();
    }


    @Override
    protected int getInformationTextView() {
        return R.id.informationTextOperation;
    }

    @NotNull
    @Override
    protected String getInitialText() {
        switch (operationId) {
            case WEIGH_START:
                return SCAN_PLATFORM;
        }
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

    @Override
    protected void processScannedBarcode(String barcode) {
        information.setText(CHECKING_BARCODE);
        startProgressIndicator();
        if (operationId == OperationId.WEIGH_START
                && platform == null)
            checkPlatform(barcode);
        else
            checkVehicle(barcode);
    }

    private void checkPlatform(String barcode) {
        NetworkHelper.getInstance().getAPI()
                .isAvailable(
                        new PlatformAvailableRequest(AppData.getInstance().getUser(), barcode)
                ).enqueue(new Callback<PlatformAvailableResponse>() {

            @Override
            public void onResponse(Call<PlatformAvailableResponse> call, Response<PlatformAvailableResponse> response) {
                stopProgressIndicator();

                if (response.code() == 200 && response.body() != null) {
                    processPlatform(barcode, response.body());
                } else
                    setErrorText(WRONG_BARCODE + "\n" + decodeResponseMessage(response));


            }

            @Override
            public void onFailure(Call<PlatformAvailableResponse> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\n+" + t.getMessage());

            }
        });
    }

    private void processPlatform(String barcode, PlatformAvailableResponse body) {
        if (body.getPlatform() == null) {
            setErrorText(WRONG_PLATFORM);
            return;
        }
        if (body.isAllowed()) {
            AppData.getInstance().setPlatform(body.getPlatform());
            platform = body.getPlatform();
            information.setText(platform.getName() + "\n" + SCAN_DRIVER);
            information.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        } else {
            String docdata = String.format(
                    PLATFORM_BUSY_TEMPLATE,
                    body.getPlatform().getName(),
                    body.getOccupiedBy(),
                    body.getOccupiedFrom().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:dd")));
            setErrorText(docdata);
        }
    }

    private void checkVehicle(String barcode) {
        NetworkHelper.getInstance().getAPI().isAllowed(
                new OperationAllowedRequest(
                        barcode,
                        AppData.getInstance().getUser().getId().toString(),
                        operationId, platform)
        ).enqueue(new Callback<OperationAllowedResponse>() {
            @Override
            public void onResponse(Call<OperationAllowedResponse> call, Response<OperationAllowedResponse> response) {
                stopProgressIndicator();

                if (response.code() == 200 && response.body() != null) {
                    processAllowance(barcode, response.body());
                } else
                    setErrorText(WRONG_BARCODE + "\n" + decodeResponseMessage(response));

            }

            @Override
            public void onFailure(Call<OperationAllowedResponse> call, Throwable t) {
                stopProgressIndicator();
                setErrorText(Constants.NETWORKING_ERROR + "\n+" + t.getMessage());


            }

        });
    }

    private String decodeResponseMessage(Response<? extends OperationResponse> response) {
        if (response.body() != null)
            return response.body().getMessage();
        if (response.errorBody() != null) {
            try {
                String responseBody = response.errorBody().string();
                try {
                    JsonObject obj = new JsonParser().parse(responseBody).getAsJsonObject();
                    return obj.get("message").getAsString();
                } catch (Throwable t) {

                    return responseBody;
                }
            } catch (IOException e) {
            }
        }
        return response.message();
    }

    private void processAllowance(String barcode, OperationAllowedResponse body) {
        String docdata = String.format(
                (body.getVehicle().getTrailerSeparately() ? TRAILER_SEPARATELY : "") + DOC_DATA_TEMPLATE,
                body.getVehicle().getTrailer() ? TRAILER : MAINDOC,
                body.getVehicle().getNumber(),
                body.getVehicle().getDriver())
                + (operationId == OperationId.UNLOAD_START ?
                body.getHumidity() > 0 ? String.format(ANALYZIS_RESULT, body.getHumidity(), body.getTrash()) : NO_ANALYZIS
                : ""
        );
        if (!body.isAllowed()) {
            setErrorText(docdata + NOT_ALLOWED + body.getMessage());
            plateGroup.setVisibility(View.GONE);
            scanBadgeButton.setVisibility(View.VISIBLE);
        } else {
            information.setText(docdata);
            information.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            scanBadgeButton.setVisibility(View.GONE);
            plateGroup.setVisibility(View.VISIBLE);
            vehicle = body.getVehicle();
        }
    }


    private void processRejected(String message) {
        setErrorText(message);
        buttonCommit.setText("OK");
        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle params = new Bundle();
                params.putString(ERROR, "");
                params.putString(MESSAGE, String.format(
                        OPERATION_REJECTED, operationId.getName(), message)
                );
                ((MainActivity) getActivity()).getNavController().navigate(R.id.resultFragment, params);
            }
        });
    }

    private void processConfirmed(OperationAllowedResponse body) {

        Bundle params = new Bundle();
        params.putString(MESSAGE, String.format(
                OPERATION_SUCCESSFULL, operationId.getName())
        );
        ((MainActivity) getActivity()).getNavController().navigate(R.id.resultFragment, params);
    }
}