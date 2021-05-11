package com.nauchara.tams;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.zebra.rfid.api3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.nauchara.tams.MainActivity.intLOCATION;
import static com.nauchara.tams.MainActivity.strLOCATION;
import static com.nauchara.tams.MainActivity.strOPH;

public class ScanFragment extends Fragment {

    private static Readers readers;
    private static ArrayList availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private static String TAG = "MyWatch";
    private boolean beepON = false;
    private boolean beepONLocate = false;
    public static ToneGenerator toneGenerator;
    public Timer tbeep;
    public Timer locatebeep;
    public static BEEPER_VOLUME beeperVolume = BEEPER_VOLUME.HIGH_BEEP;
    private EventHandler eventHandler;

    private int intChangeAntena = 0;
    private int intCountDetail = 0;
    private int intCountExist = 0;
    private int intCountPercentage = 0;
    public static final int INT_PERCENT = 100;
    public static final int INT_PERCENT_DIVISION = 300;
    private int intHeaderID;

    TextView textView, tvconnstatus, tvassetsheader,tvassetsantena, tvprogress;
    ListView lvassetList;
    Spinner spnlocation;
    SeekBar seekantena;
    ProgressBar pbassets;
    RecyclerView rcyscan;
    OpnameDAdapter opnameDAdapter;
    public static List<OpnameDModel> data_list = new ArrayList<>();

    ArrayAdapter assetArrayAdapter;
    ArrayAdapter<LocationModel> locationArrayAdapter;

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        textView = (TextView) view.findViewById(R.id.text_main);
        tvconnstatus = view.findViewById(R.id.tv_conn_status);
        lvassetList = view.findViewById(R.id.lv_assetList);
        tvassetsheader = view.findViewById(R.id.tv_assets_header);
        spnlocation = view.findViewById(R.id.spn_location);
        seekantena = view.findViewById(R.id.seek_antena);
        tvassetsantena = view.findViewById(R.id.tv_assets_antena);
        tvprogress = view.findViewById(R.id.tv_progress);
        pbassets = view.findViewById(R.id.pb_assets);
        rcyscan = view.findViewById(R.id.rcy_scan);

        tvassetsheader.setText("Scanning Assets OPN."+strOPH);
        tvconnstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.notconnect_red));

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.nav_scan).setChecked(true);

        beeperSettings();

        updateTable();

        //updateTable();
        populateSpinner();

        intHeaderID = Integer.parseInt(strOPH);

        double dblChangeAntena = 0;
        intChangeAntena = databaseHelper.getAntenaSignal();
        dblChangeAntena = ((double) intChangeAntena / (double) INT_PERCENT_DIVISION) * INT_PERCENT;
        updateProgressBar();

        //intChangeAntena = databaseHelper.getAntenaSignal();
        tvassetsantena.setText("Antena Power "+ (int)dblChangeAntena +"%");
        seekantena.setProgress((int) dblChangeAntena);
        seekantena.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int intChangeAntena;
            double dblChangeAntena;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvassetsantena.setText("Antena Power "+ Integer.toString(progress) +"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                dblChangeAntena = (((double) seekantena.getProgress() / (double)INT_PERCENT)*(double)INT_PERCENT_DIVISION);
                intChangeAntena = (int)dblChangeAntena;

                databaseHelper.setAntenaSignal(intChangeAntena);
                //Log.d(TAG, "Total = "+seekantena.getProgress());
            }
        });

        lvassetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object item = assetArrayAdapter.getItem(position);

            }
        });

        spnlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationModel locationModel = (LocationModel) parent.getSelectedItem();
                displayLocationData(locationModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(databaseHelper.getStatusHeader(Integer.parseInt(strOPH))!=2) {
            startScanner();
        }

        return view;
    }

    private void startScanner() {
        if (readers == null) {
            readers = new Readers(getContext(), ENUM_TRANSPORT.SERVICE_SERIAL);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (readers != null) {
                        if (readers.GetAvailableRFIDReaderList() != null) {
                            availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                            if (availableRFIDReaderList.size() != 0) {
                                // get first reader from list
                                readerDevice = (ReaderDevice) availableRFIDReaderList.get(0);
                                reader = readerDevice.getRFIDReader();
                                if (!reader.isConnected()) {
                                    // Establish connection to the RFID Reader
                                    reader.connect();
                                    ConfigureReader();
                                }
                            }
                        }
                    }
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                    Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getContext(), "Reader Connected", Toast.LENGTH_LONG).show();
                        textView.setText("Reader connected");
                        tvconnstatus.setText("connected");
                        tvconnstatus.setTextColor(ContextCompat.getColor(getContext(), R.color.connected_green));
                    }
                },0);
            }
        });
    }

    public void getSelectedLocation(View view) {
        LocationModel locationModel = (LocationModel) spnlocation.getSelectedItem();
        displayLocationData(locationModel);
    }

    private void displayLocationData(LocationModel locationModel) {
        int loc_id = locationModel.getLoc_id();
        int loc_value1 = locationModel.getLoc_value1();
        String loc_description = locationModel.getLoc_description();
        int loc_value2 = locationModel.getLoc_value2();
        String loc_room = locationModel.getLoc_room();
        String loc_building = locationModel.getLoc_building();
        String loc_floor = locationModel.getLoc_floor();

        intLOCATION = loc_id;
        strLOCATION = loc_room;

        //String locationData = "loc id = "+loc_id+"\nloc room = "+loc_room;
        //Toast.makeText(getContext(), "LOC ID : "+intLOCATION, Toast.LENGTH_SHORT).show();
    }

    public void populateSpinner() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        locationArrayAdapter = new ArrayAdapter<LocationModel>(getContext(), android.R.layout.simple_spinner_item, databaseHelper.getLocation());
        locationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnlocation.setAdapter(locationArrayAdapter);
    }

    public class EventHandler implements RfidEventsListener {
        // Read Event Notification
        public void eventReadNotify(RfidReadEvents e) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int index = 0; index < myTags.length; index++) {
                                Log.d(TAG, "Tag ID " + myTags[index].getTagID());
                                String s = convertHex(myTags[index].getTagID());
                                Log.d(TAG, "Tag IDC " + s);
                                boolean bsuccess = databaseHelper.updateOPDetail(Integer.parseInt(strOPH), s, intLOCATION, strLOCATION);
                                //if(bsuccess) {
                                    beep();
                                //}
                                if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                                        myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                                    if (myTags[index].getMemoryBankData().length() > 0) {
                                        Log.d(TAG, " Mem Bank Data " + myTags[index].getMemoryBankData());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "Error 1 : "+e.getMessage());
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //update table
                                    updateTable();

                                    //update progressBar
                                    updateProgressBar();
                                }
                                catch (Exception e) {
                                    Log.d(TAG, "Error 2 : "+e.getMessage());
                                }
                            }
                        },500);
                    }
                });
            }
        }

        // Status Event Notification
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                reader.Actions.Inventory.perform();
                            } catch (InvalidUsageException e) {
                                e.printStackTrace();
                            } catch (OperationFailureException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                reader.Actions.Inventory.stop();
                            } catch (InvalidUsageException e) {
                                e.printStackTrace();
                            } catch (OperationFailureException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    private void updateTable() {
        databaseHelper = new DatabaseHelper(getContext());
        rcyscan.setLayoutManager(new LinearLayoutManager(getContext()));
        data_list = databaseHelper.getOPDetail(Integer.parseInt(strOPH), 1);
        opnameDAdapter = new OpnameDAdapter(getContext(), data_list);
        rcyscan.setAdapter(opnameDAdapter);
        opnameDAdapter.notifyDataSetChanged();
//        assetArrayAdapter = new ArrayAdapter<OpnameDModel>(getActivity(), android.R.layout.simple_list_item_1, databaseHelper.getOPDetail(Integer.parseInt(strOPH), 1));
//        lvassetList.setAdapter(assetArrayAdapter);
//        assetArrayAdapter = new ArrayAdapter<OpnameDModel>(getActivity(), android.R.layout.simple_list_item_1, databaseHelper.getOPDetail(Integer.parseInt(strOPH), 1)) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                // Cast the list view each item as text view
//                TextView item = (TextView) super.getView(position,convertView,parent);
//
//                // Set the typeface/font for the current item
//                //item.setTypeface(mTypeface);
//
//                // Set the list view item's text color
//                //item.setTextColor(Color.parseColor("#FF3E80F1"));
//
//                // Set the item text style to bold
//                //item.setTypeface(item.getTypeface(), Typeface.BOLD);
//
//                // Change the item text size
//                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
//
//                // return the view
//                return item;
//            }
//        };
//        lvassetList.setAdapter(assetArrayAdapter);
    }

    private void updateProgressBar() {
        intCountDetail = databaseHelper.getCountDetail(intHeaderID);
        intCountExist = databaseHelper.getCountDetailExist(intHeaderID);
        intCountPercentage = (intCountDetail == intCountExist) ? 100 : (int) (((double) intCountExist / (double) intCountDetail) * (double) INT_PERCENT);

        tvprogress.setText("Progress "+intCountExist+"/"+intCountDetail+" ("+intCountPercentage+"%)");

        pbassets.setProgress(intCountPercentage);
    }

    private void ConfigureReader() {
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null)
                    eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                // application will collect tag using getReadTags API
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);

                // set Antena
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig(1);
                int intantena_power = databaseHelper.getAntenaSignal();
                config.setTransmitPowerIndex(intantena_power);

                reader.Config.Antennas.setAntennaRfConfig(1, config);

                startbeepingTimer();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }

    public String convertHex(String strHex) {

        String inputhex = strHex;

        int stepsstart = 0;
        int stepsend = 2;
        String strconcate = "";
        for(int i = 0; i<12; i++) {
            strconcate += String.valueOf(Integer.parseInt(inputhex.substring(stepsstart, stepsend),16))+".";
            stepsstart+=2;
            stepsend+=2;
        }

        strconcate = strconcate.substring(0, strconcate.length()-1);

        return strconcate;
    }

    public void startbeepingTimer() {
        if (beeperVolume != BEEPER_VOLUME.QUIET_BEEP) {
            if (!beepON) {
                beepON = true;
                beep();
                if (tbeep == null) {
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            stopbeepingTimer();
                            beepON = false;
                        }
                    };
                    tbeep = new Timer();
                    tbeep.schedule(task, 10);
                }
            }
        }
    }

    public void stopbeepingTimer() {
        if (tbeep != null && toneGenerator != null) {
            toneGenerator.stopTone();
            tbeep.cancel();
            tbeep.purge();
        }
        tbeep = null;
    }

    public void stoplocatebeepingTimer() {
        if (locatebeep != null && toneGenerator != null) {
            toneGenerator.stopTone();
            locatebeep.cancel();
            locatebeep.purge();
        }
        locatebeep = null;
    }

    private void beeperSettings() {
        //SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_beeper), Context.MODE_PRIVATE);
        int volume = 1;
        int streamType = AudioManager.STREAM_DTMF;
        int percantageVolume = 100;
        if (volume == 0) {
            beeperVolume = BEEPER_VOLUME.HIGH_BEEP;
            percantageVolume = 100;
        }
        if (volume == 1) {
            beeperVolume = BEEPER_VOLUME.MEDIUM_BEEP;
            percantageVolume = 75;
        }
        if (volume == 2) {
            beeperVolume = BEEPER_VOLUME.LOW_BEEP;
            percantageVolume = 50;
        }
        if (volume == 3) {
            beeperVolume = BEEPER_VOLUME.QUIET_BEEP;
            percantageVolume = 0;
        }

        try {
            toneGenerator = new ToneGenerator(streamType, percantageVolume);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            toneGenerator = null;
        }
    }

    public void beep() {
        if (toneGenerator != null) {
            int toneType = ToneGenerator.TONE_PROP_BEEP;
            toneGenerator.startTone(toneType);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                //Toast.makeText(getContext(), "Disconnecting reader", Toast.LENGTH_LONG).show();
                reader = null;
                readers.Dispose();
                readers = null;
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
