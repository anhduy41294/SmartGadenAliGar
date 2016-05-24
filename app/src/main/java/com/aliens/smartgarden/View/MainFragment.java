package com.aliens.smartgarden.View;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Device;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordAction;
import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.ProfileService;
import com.aliens.smartgarden.Service.RecordActionService;
import com.aliens.smartgarden.View.AllProfileView.ProfileFragment;
import com.aliens.smartgarden.View.UI.ArcProgress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    Spinner spinner;
    Button tuoinuocBtn, maiCheBtn;
    GlobalVariable globalVariable;
    RecordAction recordAction;
    TextView nhietDoTxt, doAmTxt, mayTuoiNuocStatus, manCheStatus;
    Handler handlerSituation;
    Handler handlerDevice;
    ImageButton imgAddProfile;
    MaterialNumberPicker numberPicker;
    AlertDialog.Builder alertDialog;
    View view = null;
    ProgressDialog progressDialog;
    ArcProgress arcProgressTemperature;
    ArcProgress arcProgressHumidity;
    ArcProgress arcProgressLight;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        globalVariable = new GlobalVariable();
        setUpTuoiNuocDialog();
        setUpButton();

        nhietDoTxt = (TextView) view.findViewById(R.id.txtTemperature);
        doAmTxt = (TextView) view.findViewById(R.id.txtHumidity);
        mayTuoiNuocStatus = (TextView) view.findViewById(R.id.mayTuoiNuocStatus);
        manCheStatus = (TextView) view.findViewById(R.id.manCheStatus);

        //Add Profile
        imgAddProfile = (ImageButton) view.findViewById(R.id.imgAddProfile);
        imgAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddProfileActivity.class);
                startActivity(i);
            }
        });


        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Đang tải...");
        arcProgressTemperature = (ArcProgress) view.findViewById(R.id.arc_temperature);
        arcProgressHumidity = (ArcProgress) view.findViewById(R.id.arc_humidity);

        arcProgressTemperature.setSuffixText("\u2103");

        RecordSituationAsyncTask recordSituationAsyncTask = new RecordSituationAsyncTask();
        recordSituationAsyncTask.execute();
        RecordSituationDeviceAsyncTask recordSituationDeviceAsyncTask = new RecordSituationDeviceAsyncTask();
        recordSituationDeviceAsyncTask.execute();
        getAllProfile getAllProfile = new getAllProfile();
        getAllProfile.execute();
        SetupPushNoti setupPushNoti = new SetupPushNoti();
        setupPushNoti.execute();

        handlerSituation = new Handler();
        handlerDevice = new Handler();
        //Test
//        imgEdit = (ImageButton) view.findViewById(R.id.imgEditProfile);
//        imgEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(view.getContext(), EditProfileActivity.class);
//                i.putExtra("IdProfile", 2);
//                startActivity(i);
//            }
//        });
        return view;

    }

    private void setUpTuoiNuocDialog() {
        numberPicker = new MaterialNumberPicker.Builder(view.getContext())
                .minValue(1)
                .maxValue(20)
                .defaultValue(10)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        alertDialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Thời gian tưới nước (phút):")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(view.getContext(), String.valueOf(numberPicker.getValue()), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpSpinner() {
        spinner = (Spinner) view.findViewById(R.id.spnUserProfile);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (Profile p:globalVariable.allProfile) {
            categories.add(p.getProfileName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void setUpButton() {
        tuoinuocBtn = (Button) view.findViewById(R.id.btnWater);
        maiCheBtn = (Button) view.findViewById(R.id.btnCover);

        tuoinuocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.isTuoiNuoc) {
                    globalVariable.isTuoiNuoc = false;
                    tuoinuocBtn.setText("Mở tưới nước");
                    mayTuoiNuocStatus.setText("Mở");
                } else {
                    alertDialog.show();
                    globalVariable.isTuoiNuoc = true;
                    tuoinuocBtn.setText("Tắt Tưới nước");
                    mayTuoiNuocStatus.setText("Tắt");
                }
            }
        });

        maiCheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.isManChe) {
                    globalVariable.isManChe = false;
                    maiCheBtn.setText("Tắt mái che");
                    manCheStatus.setText("Mở");
                } else {
                    globalVariable.isManChe = true;
                    maiCheBtn.setText("Mở mái che");
                    manCheStatus.setText("Tắt");
                }
            }
        });
    }

    /**
     * AsyncTask
     */
    public class RecordSituationAsyncTask extends AsyncTask<String, Void, RecordSituation> {

        public RecordSituationAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected RecordSituation doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getLastestSituation();
        }

        @Override
        protected void onPostExecute(RecordSituation o) {
            super.onPostExecute(o);

            //Set value to UI
            nhietDoTxt.setText(String.valueOf((int)o.getTemperature()));
            doAmTxt.setText(String.valueOf((int)o.getHumidity()));

            arcProgressTemperature.setProgress((int)o.getTemperature());
            arcProgressHumidity.setProgress((int)o.getHumidity());
            progressDialog.dismiss();
        }
    }

    /**
     * AsyncTask
     */
    public class SendRecordAction extends AsyncTask<String, Void, String> {

        public SendRecordAction() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            RecordActionService recordActionService = new RecordActionService();
            return recordActionService.postRecordAction(recordAction);
        }

        @Override
        protected void onPostExecute(String respond) {
            super.onPostExecute(respond);

        }
    }

    /**
     * AsyncTask
     */
    public class RecordSituationDeviceAsyncTask extends AsyncTask<String, Void, ArrayList<Device>> {

        public RecordSituationDeviceAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<Device> doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getAllDeviceStatus();
        }

        @Override
        protected void onPostExecute(ArrayList<Device> devices) {
            super.onPostExecute(devices);

            //Set value to UI
            globalVariable.isTuoiNuoc = devices.get(0).isDeviceStatus();
            globalVariable.isManChe = devices.get(1).isDeviceStatus();
            tuoinuocBtn.setText(devices.get(0).isDeviceStatus()?"Tắt tưới nước" : "Mở tưới nước");
            maiCheBtn.setText(devices.get(1).isDeviceStatus()?"Tắt mái che" : "Mở mái che");
            mayTuoiNuocStatus.setText(devices.get(0).isDeviceStatus()?"Mở" : "Tắt");
            manCheStatus.setText(devices.get(1).isDeviceStatus()?"Mở" : "Tắt");
            progressDialog.dismiss();
        }
    }

    /**
     * AsyncTask
     */
    public class getAllProfile extends AsyncTask<String, Void, ArrayList<Profile>> {

        public getAllProfile() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<Profile> doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getAllProfile();
        }

        @Override
        protected void onPostExecute(ArrayList<Profile> allProfile) {
            super.onPostExecute(allProfile);
            globalVariable.allProfile = allProfile;
            setUpSpinner();

            progressDialog.dismiss();
        }
    }

    public class SetupPushNoti extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /**
             * Push Notification
             */
            Platform.loadPlatformComponent(new AndroidPlatformComponent());

            String host = "http://aligarapi.apphb.com/";
            HubConnection connection = new HubConnection(host);

            HubProxy hub = connection.createHubProxy("MyHub");

            SignalRFuture<Void> awaitConnection = connection.start();
            try {
                awaitConnection.get();
            } catch (InterruptedException e) {
                // Handle ...
                e.printStackTrace();
            } catch (ExecutionException e) {
                // Handle ...
                e.printStackTrace();
            }

            hub.subscribe(this);

            hub.on("notifyNewSituation", new SubscriptionHandler1<String>() {
                @Override
                public void run(String msg) {
                    //Log.d("result := ", msg);
                    String[] separated = msg.split("=");
                    final String fStatusTem = separated[0].substring(0, 2);
                    final String fStatusHum = separated[1].substring(0, 2);
                    Log.i("=======Notify", msg);
                    handlerSituation.post( new Runnable() {
                        @Override
                        public void run() {
                            nhietDoTxt.setText(fStatusTem);
                            doAmTxt.setText(fStatusHum);
                            arcProgressTemperature.setProgress(Integer.valueOf(fStatusTem));
                            arcProgressHumidity.setProgress(Integer.valueOf(fStatusHum));
                        }
                    } );
                }
            }, String.class);

            hub.on("notifyNew", new SubscriptionHandler1<String>() {
                @Override
                public void run(String msg) {
                    //Log.d("result := ", msg);
                    Log.i("=======Notify", msg);
                }
            }, String.class);

            hub.on("notifyNewDeviceStatus", new SubscriptionHandler1<String>() {
                @Override
                public void run(String msg) {
                    //Log.d("result := ", msg);
                    String[] separated = msg.split("=");
                    final String fDevice = separated[0];
                    final String fStatus = separated[1];
                    Log.i("=======Notify", msg);
                    handlerDevice.post( new Runnable() {
                        @Override
                        public void run() {
                            if (Integer.valueOf(fDevice) == 1)
                            {
                                if (Integer.valueOf(fStatus) == 1)
                                {
                                    tuoinuocBtn.setText("Tắt Tưới nước");
                                    mayTuoiNuocStatus.setText("Mở");
                                }else
                                {
                                    tuoinuocBtn.setText("Mở Tưới nước");
                                    mayTuoiNuocStatus.setText("Tắt");
                                }
                            }else
                            {
                                if (Integer.valueOf(fStatus) == 1)
                                {
                                    maiCheBtn.setText("Tắt Mái che");
                                    manCheStatus.setText("Mở");
                                }else
                                {
                                    maiCheBtn.setText("Mở Mái che");
                                    manCheStatus.setText("Tắt");
                                }
                            }
                        }
                    } );
                }
            }, String.class);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressDialog.dismiss();
        }
    }
}
