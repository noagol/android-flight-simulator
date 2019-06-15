package com.example.ebng.flightsimulator.ui.joystick;

import android.arch.lifecycle.ViewModel;
import android.view.MotionEvent;
import android.view.View;

import com.example.ebng.flightsimulator.JoystickView;
import com.example.ebng.flightsimulator.model.ConnectModel;

public class JoystickViewModel extends ViewModel {
    private final ConnectModel connectModel;

    /**
     * Joystick view mode
     * @param view the joystick view
     * @param ip the ip
     * @param port the port
     */
    public JoystickViewModel(JoystickView view, String ip, Integer port){
        // Initialize model
        connectModel = new ConnectModel(ip, port);

        // Set touch listener
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Model update
                view.onTouchEvent(motionEvent);

                JoystickView v = (JoystickView) view;

                connectModel.updateAileron(v.getAileron());
                connectModel.updateElevator(v.getElevator());

                System.out.println(String.format("Elevator: %f", v.getElevator()));
                System.out.println(String.format("Aileron: %f", v.getAileron()));

                return true;
            }
        });
    }

    /**
     * Stop the model
     */
    public void stop() {
        connectModel.stop();
    }
}
