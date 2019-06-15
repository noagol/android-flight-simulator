package com.example.ebng.flightsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ebng.flightsimulator.ui.joystick.JoystickViewModel;

public class JoystickActivity extends AppCompatActivity {
    JoystickViewModel viewModel;

    /**
     * Run joystick and send updated values to the plane
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_activity);

        // Get the ip and port
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        Integer port = Integer.parseInt(intent.getStringExtra("port"));

        // Get the view and create the view model
        JoystickView view = findViewById(R.id.joystick);
        viewModel = new JoystickViewModel(view, ip, port);
    }

    /**
     * Stop the model
     */
    public void onDestroy() {
        super.onDestroy();
        viewModel.stop();
    }

}
