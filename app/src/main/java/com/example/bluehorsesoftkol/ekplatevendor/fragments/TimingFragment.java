package com.example.bluehorsesoftkol.ekplatevendor.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.ConstantClass;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.CustomTimePickerDialog;
import com.example.bluehorsesoftkol.ekplatevendor.Utils.Pref;
import com.example.bluehorsesoftkol.ekplatevendor.activity.vendor.ActivityAddVendor;
import com.example.bluehorsesoftkol.ekplatevendor.bean.VendorTimingItem;
import com.example.bluehorsesoftkol.ekplatevendor.dbpackage.DbAdapter;
import java.util.ArrayList;
import java.util.Calendar;


public class TimingFragment extends Fragment {

    private View rootView;

    private TextView MOC_1_open,MOC_1_close,EOC_2_open,EOC_2_close;
    private TextView MOC_1_open_t,MOC_1_close_t,EOC_2_open_t,EOC_2_close_t;
    private TextView MOC_1_open_w,MOC_1_close_w,EOC_2_open_w,EOC_2_close_w;
    private TextView MOC_1_open_th,MOC_1_close_th,EOC_2_open_th,EOC_2_close_th;
    private TextView MOC_1_open_f,MOC_1_close_f,EOC_2_open_f,EOC_2_close_f;
    private TextView MOC_1_open_s,MOC_1_close_s,EOC_2_open_s,EOC_2_close_s;
    private TextView MOC_1_open_su,MOC_1_close_su,EOC_2_open_su,EOC_2_close_su;
    private CheckBox Chk_Timing;
    private Button btnBack, btnNext;
    private String  HH,MM;
    private int validationFlag = 1,dayPointer;
    private ArrayList<String> dayList, moc_opening,moc_closing,eoc_opening,eoc_closing;
    private ActivityAddVendor activityAddVendor;
    private DbAdapter dbAdapter;
    private Pref _pref;
    private ArrayList<VendorTimingItem> vendorTimingItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timing,container,false);
        initialize();

        dbAdapter.open();
        if(dbAdapter.getVendorCompleteStep()>=3) {
            vendorTimingItems = dbAdapter.getVendorTimingInfo(Integer.parseInt(_pref.getVendorId()));
            if(vendorTimingItems.size()>0){
                setTimeIntoFields(vendorTimingItems);
            }

        }
        dbAdapter.close();

        ClickTimeText();
        Click_for_all();
        NextButtonClickMethod();

        return rootView;
    }

    private void initialize() {
        activityAddVendor = (ActivityAddVendor) getActivity();
        dbAdapter = new DbAdapter(getActivity());
        _pref = new Pref(getActivity());

        Chk_Timing = (CheckBox) rootView.findViewById(R.id.Chk_Timing);
        btnBack = (Button) rootView.findViewById(R.id.btnBack);
        btnNext = (Button) rootView.findViewById(R.id.btnNext);

        //---------------------------------------------FOR MONDAY ----------------------------------------------------//

        MOC_1_open = (TextView)rootView.findViewById(R.id.MOC_1_open);
        MOC_1_close = (TextView)rootView.findViewById(R.id.MOC_1_close);
        EOC_2_open = (TextView)rootView.findViewById(R.id.EOC_2_open);
        EOC_2_close = (TextView)rootView.findViewById(R.id.EOC_2_close);

        //-------------------------------------------FOR TUESDAY ---------------------------------------------------//
        MOC_1_open_t = (TextView)rootView.findViewById(R.id.MOC_1_open_t);
        MOC_1_close_t = (TextView)rootView.findViewById(R.id.MOC_1_close_t);
        EOC_2_open_t = (TextView)rootView.findViewById(R.id.EOC_2_open_t);
        EOC_2_close_t = (TextView)rootView.findViewById(R.id.EOC_2_close_t);

        //-------------------------------------------FOR Wednesday ---------------------------------------------------//
        MOC_1_open_w = (TextView)rootView.findViewById(R.id.MOC_1_open_w);
        MOC_1_close_w = (TextView)rootView.findViewById(R.id.MOC_1_close_w);
        EOC_2_open_w = (TextView)rootView.findViewById(R.id.EOC_2_open_w);
        EOC_2_close_w = (TextView)rootView.findViewById(R.id.EOC_2_close_w);

        //-------------------------------------------FOR Thursday ---------------------------------------------------//
        MOC_1_open_th = (TextView)rootView.findViewById(R.id.MOC_1_open_th);
        MOC_1_close_th = (TextView)rootView.findViewById(R.id.MOC_1_close_th);
        EOC_2_open_th = (TextView)rootView.findViewById(R.id.EOC_2_open_th);
        EOC_2_close_th = (TextView)rootView.findViewById(R.id.EOC_2_close_th);

        //-------------------------------------------FOR Friday ---------------------------------------------------//
        MOC_1_open_f = (TextView)rootView.findViewById(R.id.MOC_1_open_f);
        MOC_1_close_f = (TextView)rootView.findViewById(R.id.MOC_1_close_f);
        EOC_2_open_f = (TextView)rootView.findViewById(R.id.EOC_2_open_f);
        EOC_2_close_f = (TextView)rootView.findViewById(R.id.EOC_2_close_f);

        //-------------------------------------------FOR Saturday ---------------------------------------------------//
        MOC_1_open_s = (TextView)rootView.findViewById(R.id.MOC_1_open_s);
        MOC_1_close_s = (TextView)rootView.findViewById(R.id.MOC_1_close_s);
        EOC_2_open_s = (TextView)rootView.findViewById(R.id.EOC_2_open_s);
        EOC_2_close_s = (TextView)rootView.findViewById(R.id.EOC_2_close_s);

        //-------------------------------------------FOR Sunday ---------------------------------------------------//
        MOC_1_open_su = (TextView)rootView.findViewById(R.id.MOC_1_open_su);
        MOC_1_close_su = (TextView)rootView.findViewById(R.id.MOC_1_close_su);
        EOC_2_open_su = (TextView)rootView.findViewById(R.id.EOC_2_open_su);
        EOC_2_close_su = (TextView)rootView.findViewById(R.id.EOC_2_close_su);

        //-------------------------------------------All Array-----------------------------------------------------//
        dayList = new ArrayList<>();
        moc_opening = new ArrayList<>();
        moc_closing = new ArrayList<>();
        eoc_opening = new ArrayList<>();
        eoc_closing = new ArrayList<>();
    }

    private void setTimeIntoFields(ArrayList<VendorTimingItem> vendorTimingItems){
        for(int i=0; i<=6; i++){
            VendorTimingItem _itemVendorTimingItem = vendorTimingItems.get(i);

            if(_itemVendorTimingItem.getDay().equals("mon")){
                MOC_1_open.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("tus")){
                MOC_1_open_t.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_t.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_t.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_t.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("wed")){
                MOC_1_open_w.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_w.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_w.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_w.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("thu")){
                MOC_1_open_th.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_th.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_th.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_th.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("fri")){
                MOC_1_open_f.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_f.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_f.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_f.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("sat")){
                MOC_1_open_s.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_s.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_s.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_s.setText(_itemVendorTimingItem.getEocClosing());

            } else if(_itemVendorTimingItem.getDay().equals("sun")){
                MOC_1_open_su.setText(_itemVendorTimingItem.getMocOpening());
                MOC_1_close_su.setText(_itemVendorTimingItem.getMocClosing());
                EOC_2_open_su.setText(_itemVendorTimingItem.getEocOpening());
                EOC_2_close_su.setText(_itemVendorTimingItem.getEocClosing());
            }
        }
    }

    private void ClickTimeText() {

        //region For Monday...
        MOC_1_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 0;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if((MOC_1_close.getText().toString().isEmpty()))
                        {
                            MOC_1_open.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_open,dayPointer))
                                {
                                    MOC_1_close.setText("");
                                }
                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }

        });

        MOC_1_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 0;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_open.getText().toString().isEmpty())
                        {
                            MOC_1_close.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_close,dayPointer)) {
                                    MOC_1_open.setText("");
                                    //----------------------------------------------------
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }

        });

        EOC_2_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 0;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close.getText().toString().isEmpty())
                        {
                            EOC_2_open.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))== time4arithmatic((EOC_2_close.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 0;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open.getText().toString().isEmpty())
                        {
                            EOC_2_close.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });



        //region For Tuesday...
        MOC_1_open_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayPointer = 1;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_t.getText().toString().isEmpty())
                        {
                            MOC_1_open_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_t.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_open_t,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_t.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 1;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_t.getText().toString().isEmpty())
                        {
                            MOC_1_close_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_t.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_close_t,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_t.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 1;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_t.getText().toString().isEmpty())
                        {
                            EOC_2_open_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_t.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 1;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_t.getText().toString().isEmpty())
                        {
                            EOC_2_close_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_t.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_t.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        //region For Wednesday...
        MOC_1_open_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 2;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_w.getText().toString().isEmpty())
                        {
                            MOC_1_open_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_w.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_open_w,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_w.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 2;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_w.getText().toString().isEmpty())
                        {
                            MOC_1_close_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_w.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_close_w,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_w.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 2;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_w.getText().toString().isEmpty())
                        {
                            EOC_2_open_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_w.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 2;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_w.getText().toString().isEmpty())
                        {
                            EOC_2_close_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_w.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_w.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        //region For Thursday
        MOC_1_open_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayPointer = 3;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_th.getText().toString().isEmpty())
                        {
                            MOC_1_open_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_th.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_open_th,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_th.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 3;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_th.getText().toString().isEmpty())
                        {
                            MOC_1_close_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_th.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_close_th,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_th.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 3;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_th.getText().toString().isEmpty())
                        {
                            EOC_2_open_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_th.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 3;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_th.getText().toString().isEmpty())
                        {
                            EOC_2_close_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_th.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_th.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        //region For Friday
        MOC_1_open_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 4;

                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_f.getText().toString().isEmpty())
                        {
                            MOC_1_open_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_f.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_open_f,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_f.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 4;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_f.getText().toString().isEmpty())
                        {
                            MOC_1_close_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_f.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_close_f,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_f.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 4;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_f.getText().toString().isEmpty())
                        {
                            EOC_2_open_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_f.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 4;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_f.getText().toString().isEmpty())
                        {
                            EOC_2_close_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_f.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_f.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        //region For Saturday
        MOC_1_open_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayPointer = 5;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_s.getText().toString().isEmpty())
                        {
                            MOC_1_open_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_s.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( ! moc2eoc(MOC_1_open_s,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_s.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 5;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_s.getText().toString().isEmpty())
                        {
                            MOC_1_close_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_s.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if(! moc2eoc(MOC_1_close_s,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_s.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 5;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_s.getText().toString().isEmpty())
                        {
                            EOC_2_open_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_s.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 5;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_s.getText().toString().isEmpty())
                        {
                            EOC_2_close_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_s.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_s.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        //region For Sunday
        MOC_1_open_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 6;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(MOC_1_close_su.getText().toString().isEmpty())
                        {
                            MOC_1_open_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))>=time4arithmatic((MOC_1_close_su.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_open_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_open_su,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_close_su.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------
            }


        });

        MOC_1_close_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 6;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(MOC_1_open_su.getText().toString().isEmpty())
                        {
                            MOC_1_close_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))<=time4arithmatic((MOC_1_open_su.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Close time can not be less than Open time.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                MOC_1_close_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                                if( !moc2eoc(MOC_1_close_su,dayPointer)) {
                                    //Execution Code....
                                    MOC_1_open_su.setText("");
                                }
                            }
                        }

                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }

        });

        EOC_2_open_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 6;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_close_su.getText().toString().isEmpty())
                        {
                            EOC_2_open_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_close_su.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_open_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

        EOC_2_close_su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPointer = 6;
                //-------------------------------------------------------------------------------
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                CustomTimePickerDialog mTimePicker;
                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if(EOC_2_open_su.getText().toString().isEmpty())
                        {
                            EOC_2_close_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);
                        }
                        else
                        {
                            if(time4arithmatic((selectedHour + ":" + selectedMinute))==time4arithmatic((EOC_2_open_su.getText().toString())))
                            {
                                Toast.makeText(getActivity(),"Open & Close Time Cannot be Same!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                EOC_2_close_su.setText(selectedMinute == 0 ? selectedHour + ":" + "00" : selectedHour + ":" + selectedMinute);

                            }
                        }
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                //-------------------------------------------------------------------------------

            }
        });

    }

    private void Click_for_all() {

        Chk_Timing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Chk_Timing.isChecked()) {

                    //region For Checked Condition True
                    //------------------------For Tuesday------------------------------------//
                    MOC_1_open_t.setText(MOC_1_open.getText());
                    MOC_1_close_t.setText(MOC_1_close.getText());
                    EOC_2_open_t.setText(EOC_2_open.getText());
                    EOC_2_close_t.setText(EOC_2_close.getText());

                    //------------------------For Wednesday------------------------------------//
                    MOC_1_open_w.setText(MOC_1_open.getText());
                    MOC_1_close_w.setText(MOC_1_close.getText());
                    EOC_2_open_w.setText(EOC_2_open.getText());
                    EOC_2_close_w.setText(EOC_2_close.getText());

                    //------------------------For Thursday------------------------------------//
                    MOC_1_open_th.setText(MOC_1_open.getText());
                    MOC_1_close_th.setText(MOC_1_close.getText());
                    EOC_2_open_th.setText(EOC_2_open.getText());
                    EOC_2_close_th.setText(EOC_2_close.getText());

                    //------------------------For Friday------------------------------------//
                    MOC_1_open_f.setText(MOC_1_open.getText());
                    MOC_1_close_f.setText(MOC_1_close.getText());
                    EOC_2_open_f.setText(EOC_2_open.getText());
                    EOC_2_close_f.setText(EOC_2_close.getText());

                    //------------------------For Saturday------------------------------------//
                    MOC_1_open_s.setText(MOC_1_open.getText());
                    MOC_1_close_s.setText(MOC_1_close.getText());
                    EOC_2_open_s.setText(EOC_2_open.getText());
                    EOC_2_close_s.setText(EOC_2_close.getText());

                    //------------------------For Saturday------------------------------------//
                    MOC_1_open_su.setText(MOC_1_open.getText());
                    MOC_1_close_su.setText(MOC_1_close.getText());
                    EOC_2_open_su.setText(EOC_2_open.getText());
                    EOC_2_close_su.setText(EOC_2_close.getText());
                    //endregion

                } else {

                    //region For Checked Condition True
                    //------------------------For Tuesday------------------------------------//
                    MOC_1_open_t.setText(null);
                    MOC_1_close_t.setText(null);
                    EOC_2_open_t.setText(null);
                    EOC_2_close_t.setText(null);

                    //------------------------For Wednesday------------------------------------//
                    MOC_1_open_w.setText(null);
                    MOC_1_close_w.setText(null);
                    EOC_2_open_w.setText(null);
                    EOC_2_close_w.setText(null);

                    //------------------------For Thursday------------------------------------//
                    MOC_1_open_th.setText(null);
                    MOC_1_close_th.setText(null);
                    EOC_2_open_th.setText(null);
                    EOC_2_close_th.setText(null);

                    //------------------------For Friday------------------------------------//
                    MOC_1_open_f.setText(null);
                    MOC_1_close_f.setText(null);
                    EOC_2_open_f.setText(null);
                    EOC_2_close_f.setText(null);

                    //------------------------For Saturday------------------------------------//
                    MOC_1_open_s.setText(null);
                    MOC_1_close_s.setText(null);
                    EOC_2_open_s.setText(null);
                    EOC_2_close_s.setText(null);

                    //------------------------For Saturday------------------------------------//
                    MOC_1_open_su.setText(null);
                    MOC_1_close_su.setText(null);
                    EOC_2_open_su.setText(null);
                    EOC_2_close_su.setText(null);
                    //endregion
                }
            }
        });
    }

    private void NextButtonClickMethod() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearArrays();
                if (checkBlankMOCTimes()) {
                    //Catch Values to Array By Clicking......................
                    for (int i = 0; i <= 6; i++) {
                        int resourceID_MOC_OPEN = getResources().getIdentifier(conversionFun("MOC_1_open", i), "id", "com.example.bluehorsesoftkol.ekplatevender");
                        int resourceID_MOC_CLOSE = getResources().getIdentifier(conversionFun("MOC_1_close", i), "id", "com.example.bluehorsesoftkol.ekplatevender");
                        int resourceID_EOC_OPEN = getResources().getIdentifier(conversionFun("EOC_2_open", i), "id", "com.example.bluehorsesoftkol.ekplatevender");
                        int resourceID_EOC_CLOSE = getResources().getIdentifier(conversionFun("EOC_2_close", i), "id", "com.example.bluehorsesoftkol.ekplatevender");
                        if (resourceID_MOC_OPEN != 0) {
                            TextView tv_moc_o = (TextView) rootView.findViewById(resourceID_MOC_OPEN);
                            TextView tv_moc_c = (TextView) rootView.findViewById(resourceID_MOC_CLOSE);
                            TextView tv_eoc_o = (TextView) rootView.findViewById(resourceID_EOC_OPEN);
                            TextView tv_eoc_c = (TextView) rootView.findViewById(resourceID_EOC_CLOSE);

                            if(tv_moc_o.getText().toString().equals("") || tv_moc_c.getText().toString().equals("")){
                                validationFlag = 0;
                            } else {
                                validationFlag = 1;
                            }
                            moc_opening.add(tv_moc_o.getText().toString());
                            moc_closing.add(tv_moc_c.getText().toString());
                            eoc_opening.add(tv_eoc_o.getText().toString());
                            eoc_closing.add(tv_eoc_c.getText().toString());
                        }
                    }
                    if (moc_opening.get(0).toString().equals("") || moc_closing.get(0).toString().equals("")){
                        Toast.makeText(getActivity(), "Please Enter Monday Morning Timings", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Long success;
                        for (int i = 0; i <= 6; i++) {
                            dbAdapter.open();
                            String MocOpening = moc_opening.get(i).toString();
                            String MocClosing = moc_closing.get(i).toString();
                            String EocOpening = eoc_opening.get(i).toString();
                            String EocClosing = eoc_closing.get(i).toString();

                            if(dbAdapter.getVendorCompleteStep()>=3){
                                VendorTimingItem _itemVendorTimingItem = vendorTimingItems.get(i);
                                int updateSuccess = dbAdapter.updateTiming(_itemVendorTimingItem.getId(), MocOpening, MocClosing, EocOpening, EocClosing);
                                Log.e("update time", String.valueOf(updateSuccess));
                            } else {
                                switch (i) {
                                    case 0:
                                        success = dbAdapter.insertTiming("mon", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 1:
                                        success = dbAdapter.insertTiming("tus", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 2:
                                        success = dbAdapter.insertTiming("wed", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 3:
                                        success = dbAdapter.insertTiming("thu", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 4:
                                        success = dbAdapter.insertTiming("fri", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 5:
                                        success = dbAdapter.insertTiming("sat", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                    case 6:
                                        success = dbAdapter.insertTiming("sun", MocOpening, MocClosing, EocOpening, EocClosing);
                                        break;
                                }
                            }
                            String day = conversionFunDays(i);
                            Log.e("DAY", day);
                        }

                        Log.e("K:", moc_opening.toString());
                        Log.e("l:", moc_closing.toString());
                        Log.e("m:", eoc_opening.toString());
                        Log.e("n:", eoc_closing.toString());

                        if(dbAdapter.getVendorCompleteStep()>=3){
                            Toast.makeText(getActivity(), "Timing Info Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            dbAdapter.updateVendorCompleteStep(3);
                            Toast.makeText(getActivity(), "Timing Info Inserted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        dbAdapter.close();
                        activityAddVendor.findViewById(R.id.llFoodSold).setEnabled(true);
                        activityAddVendor.loadFoodSoldFragment();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please Complete Open and Close Timing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activityAddVendor.loadLocationFragment();
            }
        });
    }

    private Boolean checkBlankMOCTimes() {
        Boolean Final_XP = false;
        Boolean xp = false;
        Boolean xpT = false;
        Boolean xpW = false;
        Boolean xpTH = false;
        Boolean xpF = false;
        Boolean xpS = false;
        Boolean xpSU = false;

//region MOC/EOC for Monday...

        if((MOC_1_open.getText().toString().isEmpty())&&((MOC_1_close.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open.getText().toString().isEmpty()) {
                if (EOC_2_close.getText().toString().isEmpty()) {
                    xp = true;
                } else {
                    xp = false;
                }
            } else if (EOC_2_close.getText().toString().isEmpty()) {
                if (EOC_2_open.getText().toString().isEmpty()) {
                    xp = true;
                } else {
                    xp = false;
                }
            } else {
                xp = true;
            }
            //endregion

        }

        else if((EOC_2_open.getText().toString().isEmpty())&&((EOC_2_close.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open.getText().toString().isEmpty())
            {
                if(MOC_1_close.getText().toString().isEmpty())
                {
                    xp = false;
                }
                else
                {
                    xp = false;
                }
            }
            else if(MOC_1_close.getText().toString().isEmpty())
            {
                if(MOC_1_open.getText().toString().isEmpty())
                {
                    xp = false;
                }
                else
                {
                    xp = false;
                }
            }
            else
            {
                xp = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open.getText().toString().isEmpty()))&&(!(MOC_1_close.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open.getText().toString().isEmpty()))&&(!(EOC_2_close.getText().toString().isEmpty())))
                {
                    xp = true;
                }

            }
            else
            {
                xp = false;
            }


        }

        //endregion
//region MOC_t/EOC_t for Monday...

        if((MOC_1_open_t.getText().toString().isEmpty())&&((MOC_1_close_t.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open_t.getText().toString().isEmpty()) {
                if (EOC_2_close_t.getText().toString().isEmpty()) {
                    xpT = true;
                } else {
                    xpT = false;
                }
            } else if (EOC_2_close_t.getText().toString().isEmpty()) {
                if (EOC_2_open_t.getText().toString().isEmpty()) {
                    xpT = true;
                } else {
                    xpT = false;
                }
            } else {
                xpT = true;
            }
            //endregion

        }

        else if((EOC_2_open_t.getText().toString().isEmpty())&&((EOC_2_close_t.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_t.getText().toString().isEmpty())
            {
                if(MOC_1_close_t.getText().toString().isEmpty())
                {
                    xpT = true;
                }
                else
                {
                    xpT = false;
                }
            }
            else if(MOC_1_close_t.getText().toString().isEmpty())
            {
                if(MOC_1_open_t.getText().toString().isEmpty())
                {
                    xpT = true;
                }
                else
                {
                    xpT = false;
                }
            }
            else
            {
                xpT = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_t.getText().toString().isEmpty()))&&(!(MOC_1_close_t.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_t.getText().toString().isEmpty()))&&(!(EOC_2_close_t.getText().toString().isEmpty())))
                {
                    xpT = true;
                }

            }
            else
            {
                xpT = false;
            }


        }

        //endregion
// region MOC_w/EOC_w for Monday...

        if((MOC_1_open_w.getText().toString().isEmpty())&&((MOC_1_close_w.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open_w.getText().toString().isEmpty()) {
                if (EOC_2_close_w.getText().toString().isEmpty()) {
                    xpW = true;
                } else {
                    xpW = false;
                }
            } else if (EOC_2_close_w.getText().toString().isEmpty()) {
                if (EOC_2_open_w.getText().toString().isEmpty()) {
                    xpW = true;
                } else {
                    xpW = false;
                }
            } else {
                xpW = true;
            }
            //endregion

        }

        else if((EOC_2_open_w.getText().toString().isEmpty())&&((EOC_2_close_w.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_w.getText().toString().isEmpty())
            {
                if(MOC_1_close_w.getText().toString().isEmpty())
                {
                    xpW = false;
                }
                else
                {
                    xpW = false;
                }
            }
            else if(MOC_1_close_w.getText().toString().isEmpty())
            {
                if(MOC_1_open_w.getText().toString().isEmpty())
                {
                    xpW = false;
                }
                else
                {
                    xpW = false;
                }
            }
            else
            {
                xpW = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_w.getText().toString().isEmpty()))&&(!(MOC_1_close_w.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_w.getText().toString().isEmpty()))&&(!(EOC_2_close_w.getText().toString().isEmpty())))
                {
                    xpW = true;
                }

            }
            else
            {
                xpW = false;
            }


        }

        //endregion
// region MOC_th/EOC_th for Monday...

        if((MOC_1_open_th.getText().toString().isEmpty())&&((MOC_1_close_th.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open_th.getText().toString().isEmpty()) {
                if (EOC_2_close_th.getText().toString().isEmpty()) {
                    xpTH = true;
                } else {
                    xpTH = false;
                }
            } else if (EOC_2_close_th.getText().toString().isEmpty()) {
                if (EOC_2_open_w.getText().toString().isEmpty()) {
                    xpTH = true;
                } else {
                    xpTH = false;
                }
            } else {
                xpTH = true;
            }
            //endregion

        }

        else if((EOC_2_open_th.getText().toString().isEmpty())&&((EOC_2_close_th.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_th.getText().toString().isEmpty())
            {
                if(MOC_1_close_th.getText().toString().isEmpty())
                {
                    xpTH = false;
                }
                else
                {
                    xpTH = false;
                }
            }
            else if(MOC_1_close_th.getText().toString().isEmpty())
            {
                if(MOC_1_open_th.getText().toString().isEmpty())
                {
                    xpTH = false;
                }
                else
                {
                    xpTH = false;
                }
            }
            else
            {
                xpTH = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_th.getText().toString().isEmpty()))&&(!(MOC_1_close_th.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_th.getText().toString().isEmpty()))&&(!(EOC_2_close_th.getText().toString().isEmpty())))
                {
                    xpTH = true;
                }

            }
            else
            {
                xpTH = false;
            }


        }

        //endregion
// region MOC_f/EOC_f for Monday...

        if((MOC_1_open_f.getText().toString().isEmpty())&&((MOC_1_close_f.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open_f.getText().toString().isEmpty()) {
                if (EOC_2_close_f.getText().toString().isEmpty()) {
                    xpF = true;
                } else {
                    xpF = false;
                }
            } else if (EOC_2_close_f.getText().toString().isEmpty()) {
                if (EOC_2_open_f.getText().toString().isEmpty()) {
                    xpF = true;
                } else {
                    xpF = false;
                }
            } else {
                xpF = true;
            }
            //endregion

        }

        else if((EOC_2_open_f.getText().toString().isEmpty())&&((EOC_2_close_f.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_f.getText().toString().isEmpty())
            {
                if(MOC_1_close_f.getText().toString().isEmpty())
                {
                    xpF = false;
                }
                else
                {
                    xpF = false;
                }
            }
            else if(MOC_1_close_f.getText().toString().isEmpty())
            {
                if(MOC_1_open_f.getText().toString().isEmpty())
                {
                    xpF = false;
                }
                else
                {
                    xpF = false;
                }
            }
            else
            {
                xpF = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_f.getText().toString().isEmpty()))&&(!(MOC_1_close_f.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_f.getText().toString().isEmpty()))&&(!(EOC_2_close_f.getText().toString().isEmpty())))
                {
                    xpF = true;
                }

            }
            else
            {
                xpF = false;
            }


        }

        //endregion
// region MOC_s/EOC_s for Monday...

        if((MOC_1_open_s.getText().toString().isEmpty())&&((MOC_1_close_s.getText().toString().isEmpty())))
        {

            //region EOC...
            if (EOC_2_open_s.getText().toString().isEmpty()) {
                if (EOC_2_close_s.getText().toString().isEmpty()) {
                    xpS = true;
                } else {
                    xpS = false;
                }
            } else if (EOC_2_close_s.getText().toString().isEmpty()) {
                if (EOC_2_open_s.getText().toString().isEmpty()) {
                    xpS = true;
                } else {
                    xpS = false;
                }
            } else {
                xpS = true;
            }
            //endregion

        }

        else if((EOC_2_open_s.getText().toString().isEmpty())&&((EOC_2_close_s.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_s.getText().toString().isEmpty())
            {
                if(MOC_1_close_s.getText().toString().isEmpty())
                {
                    xpS = false;
                }
                else
                {
                    xpS = false;
                }
            }
            else if(MOC_1_close_s.getText().toString().isEmpty())
            {
                if(MOC_1_open_s.getText().toString().isEmpty())
                {
                    xpS = false;
                }
                else
                {
                    xpS = false;
                }
            }
            else
            {
                xpS = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_s.getText().toString().isEmpty()))&&(!(MOC_1_close_s.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_s.getText().toString().isEmpty()))&&(!(EOC_2_close_s.getText().toString().isEmpty())))
                {
                    xpS = true;
                }

            }
            else
            {
                xpS = false;
            }

        }

        //endregion
// region MOC_su/EOC_su for Monday...

        if((MOC_1_open_su.getText().toString().isEmpty())&&((MOC_1_close_su.getText().toString().isEmpty())))
        {


            //region EOC...
            if (EOC_2_open_su.getText().toString().isEmpty()) {
                if (EOC_2_close_su.getText().toString().isEmpty()) {
                    xpSU = true;
                } else {
                    xpSU = false;
                }
            } else if (EOC_2_close_su.getText().toString().isEmpty()) {
                if (EOC_2_open_su.getText().toString().isEmpty()) {
                    xpSU = true;
                } else {
                    xpSU = false;
                }
            } else {
                xpSU = true;
            }
            //endregion

        }

        else if((EOC_2_open_su.getText().toString().isEmpty())&&((EOC_2_close_su.getText().toString().isEmpty()))) {

            //region MOC ...
            if(MOC_1_open_su.getText().toString().isEmpty())
            {
                if(MOC_1_close_su.getText().toString().isEmpty())
                {
                    xpSU = false;
                }
                else
                {
                    xpSU = false;
                }
            }
            else if(MOC_1_close_su.getText().toString().isEmpty())
            {
                if(MOC_1_open_su.getText().toString().isEmpty())
                {
                    xpSU = false;
                }
                else
                {
                    xpSU = false;
                }
            }
            else
            {
                xpSU = true;
            }
            //endregion
        }
        else
        {
            if((!(MOC_1_open_su.getText().toString().isEmpty()))&&(!(MOC_1_close_su.getText().toString().isEmpty())))
            {
                if((!(EOC_2_open_su.getText().toString().isEmpty()))&&(!(EOC_2_close_su.getText().toString().isEmpty())))
                {
                    xpSU = true;
                }

            }
            else
            {
                xpSU = false;
            }
        }

        //endregion

        if(xp == xpT)
        {
            if(xpT == xpW)
            {
                if(xpW == xpTH)
                {
                    if(xpTH == xpF)
                    {
                        if(xpTH == xpS)
                        {
                            if(xpS == xpSU)
                            {
                                Final_XP = true;
                            }

                        }

                    }

                }

            }

        }
        return Final_XP;
    }

    private int time4arithmatic(String s)
    {
        int z;
        /*s = s.replaceAll("[^\\d.]", "");*/
        int iend = s.indexOf(":");
        if (iend != -1) {
            HH = s.substring(0, iend);
            MM = s.substring(iend+1,s.length());
        }
        //------------------------------------------For HH Time.---------------------------//
        if(HH.length() == 1)
        {
            HH = "0"+HH;
        }
        //-------------------------------------------------------------------------------//

        if(MM.equals("0") )
        {
            MM = "00";
        }
        if(MM.length() == 1)
        {
            MM = "0"+MM;
        }
        Log.e("HH:",HH);
        Log.e("MM:",MM);
        s = HH+MM;

        z = Integer.parseInt(s);
        Log.e("ConvertedString",String.valueOf(z));
        return z;
    }

    private Boolean moc2eoc(TextView et,int in)
    {
        Boolean b = false;
        Log.e("Day Index:",String.valueOf(dayPointer));
            int resourceID_MOC_OPEN = getResources().getIdentifier(conversionFun("MOC_1_open", in), "id", "com.example.bluehorsesoftkol.ekplatevender");
            int resourceID_MOC_CLOSE = getResources().getIdentifier(conversionFun("MOC_1_close", in), "id", "com.example.bluehorsesoftkol.ekplatevender");
            int resourceID_EOC_OPEN = getResources().getIdentifier(conversionFun("EOC_2_open", in), "id", "com.example.bluehorsesoftkol.ekplatevender");
            int resourceID_EOC_CLOSE = getResources().getIdentifier(conversionFun("EOC_2_close", in), "id", "com.example.bluehorsesoftkol.ekplatevender");
            if (resourceID_MOC_OPEN != 0) {
                TextView tv_moc_o = (TextView) rootView.findViewById(resourceID_MOC_OPEN);
                TextView tv_moc_c = (TextView) rootView.findViewById(resourceID_MOC_CLOSE);
                TextView tv_eoc_o = (TextView) rootView.findViewById(resourceID_EOC_OPEN);
                TextView tv_eoc_c = (TextView) rootView.findViewById(resourceID_EOC_CLOSE);
                if(!((tv_eoc_c.getText() == tv_eoc_o.getText())||(tv_moc_c.getText()==tv_moc_o.getText()))) {
                    if ((time4arithmatic(tv_moc_o.getText().toString()) < time4arithmatic(tv_eoc_o.getText().toString())) && (time4arithmatic(tv_moc_c.getText().toString()) < time4arithmatic(tv_eoc_c.getText().toString()))) {


                       if((time4arithmatic(tv_moc_o.getText().toString()) < time4arithmatic(tv_eoc_c.getText().toString())) && (time4arithmatic(tv_moc_c.getText().toString()) < time4arithmatic(tv_eoc_o.getText().toString()))) {
                           //Toast.makeText(getActivity(), "You Entered Time in Reasonable way.", Toast.LENGTH_SHORT).show();
                           b = true;

                       }
                        else {
                           Toast.makeText(getActivity(),"Evening Time should be after Morning Time",Toast.LENGTH_SHORT).show();
                           et.setText("");
                           b = false;
                       }

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Evening Time should be after Morning Time",Toast.LENGTH_SHORT).show();
                        et.setText("");
                        b = false;
                    }

                }
                else
                {
                    b = true;
                }
            }
        return b;
    }

    private void clearArrays() {
        moc_opening.clear();
        moc_closing.clear();
        eoc_opening.clear();
        eoc_closing.clear();

    }

//region Conversion...
    public String conversionFun(String z,Integer x)
    {
        String p = "";
        switch(x)
        {
            case  0:
            {
                p = z;
                break;
            }

            case  1:
            {
                p = z+"_t";
                break;
            }

            case  2:
            {
                p = z+"_w";
                break;
            }

            case  3:
            {
                p = z+"_th";
                break;
            }

            case  4:
            {
                p = z+"_f";
                break;
            }

            case  5:
            {
                p = z+"_s";
                break;
            }

            case  6:
            {
                p = z+"_su";
                break;
            }
        }

        return p;
    }

    public String conversionFunDays(Integer x)
    {
        String Days = "";
        switch(x)
        {
            case 0:
            {
                Days = "mon";
                break;
            }

            case 1:
            {
                Days = "tue";
                break;
            }

            case 2:
            {
                Days = "wed";
                break;
            }

            case 3:
            {
                Days = "thu";
                break;
            }

            case 4:
            {
                Days = "fri";
                break;
            }

            case 5:
            {
                Days = "sat";
                break;
            }

            case 6:
            {
                Days = "sun";
                break;
            }
        }
        return Days;
    }
    //endregion

}
