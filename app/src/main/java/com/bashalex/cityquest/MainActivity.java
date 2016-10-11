package com.bashalex.cityquest;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    int PLACE_PICKER_REQUEST_1 = 1;
    int PLACE_PICKER_REQUEST_2 = 2;

    private Double location_latitude, location_longitude;
    private Double destination_latitude, destination_longitude;
    private Integer arrivalHour, arrivalMinute;
    private String destination_name;
    private boolean activeRequest = false;

    @BindView(R.id.button_from)
    Button fromBtn;

    @BindView(R.id.button_to)
    Button toBtn;

    @BindView(R.id.time_chooser)
    TextView timeChooser;

    @BindView(R.id.pr_bar)
    ProgressBar prBar;

    @BindView(R.id.start)
    Button startBtn;

    @BindView(R.id.main_inputs)
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        cardView.bringToFront();
        startBtn.bringToFront();

        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(timeChooser.getWindowToken(), 0);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST_1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, "place: " + place);
                if (place.getViewport() == null) {
                    if (place.getLatLng() == null) {
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        location_latitude = place.getLatLng().latitude;
                        location_longitude = place.getLatLng().longitude;
                    }
                } else {
                    location_latitude = place.getViewport().getCenter().latitude;
                    location_longitude = place.getViewport().getCenter().longitude;
                }
                fromBtn.setText(place.getName());
                String toastMsg = String.format("Place: %s", place.getAddress());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST_2) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place.getViewport() == null) {
                    if (place.getLatLng() == null) {
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        destination_latitude = place.getLatLng().latitude;
                        destination_longitude = place.getLatLng().longitude;
                    }
                } else {
                    destination_latitude = place.getViewport().getCenter().latitude;
                    destination_longitude = place.getViewport().getCenter().longitude;
                }
                destination_name = place.getName().toString();
                toBtn.setText(place.getName());
                String toastMsg = String.format("Place: %s", place.getAddress());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.button_from)
    public void select_location() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST_1);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.button_to)
    public void select_destination() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST_2);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.time_chooser)
    public void showPickChooser(TextView view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
                    view.setText( selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute);
                    arrivalMinute = selectedMinute;
                    arrivalHour = selectedHour;
                }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.start)
    public void start_quest() {
        if (activeRequest) return;
        if (location_latitude != null && location_longitude != null &&
                destination_latitude != null && destination_longitude != null &&
                arrivalHour != null && arrivalMinute != null) {
            API.saveParams(location_latitude, location_longitude,
                    destination_latitude,
                    destination_longitude,
                    arrivalHour, arrivalMinute, destination_name);
            API.getRoute(true)
                .doOnSubscribe(() -> {
                    activeRequest = true;
                    runOnUiThread(() -> {
                        prBar.setVisibility(View.VISIBLE);
                        prBar.bringToFront();
                    });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> {
                    Log.d(TAG, "onCompleted()");
                    activeRequest = false;
                    runOnUiThread(() -> prBar.setVisibility(View.GONE));
                })
                .subscribe(response -> {
                    if (response.getError() != null || response.getImages() == null) {
                        Toast.makeText(this, "Server error :(", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d(TAG, "response:" +  response);
                    Intent intent = new Intent(this, RouteActivity.class);
                    intent.putExtra("name", response.getName());
                    intent.putExtra("images", response.getImages());
                    intent.putExtra("way", response.getWay());
                    intent.putExtra("address", response.getAddress());
                    intent.putExtra("distances", response.getDistances());
                    intent.putExtra("image", response.getImage());
                    intent.putExtra("lastPoint", false);
                    startActivity(intent);
                });
        }
    }
}
