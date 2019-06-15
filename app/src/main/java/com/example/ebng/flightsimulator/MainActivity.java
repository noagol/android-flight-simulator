package com.example.ebng.flightsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ebng.flightsimulator.ui.joystick.JoystickViewModel;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    JoystickViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        final Button button = findViewById(R.id.connect_btn);
        final EditText editTextIP = findViewById(R.id.ip_et);
        final EditText editTextPort = findViewById(R.id.port_et);

        // Set button click method
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Validate input
                String ip = editTextIP.getText().toString();
                String port = editTextPort.getText().toString();

                if (ip.isEmpty()){
                    editTextIP.setError(getResources().getString(R.string.no_ip));
                    return;
                }

                if (port.isEmpty()){
                    editTextPort.setError(getResources().getString(R.string.no_port));
                    return;
                }

                if (!validateIP(ip)) {
                    editTextIP.setError(getResources().getString(R.string.bad_ip));
                    return;
                }

                try {
                    Integer.parseInt(port);
                } catch (NumberFormatException ex) {
                    // Unable to cast
                    editTextPort.setError(getResources().getString(R.string.bad_port));
                    return;
                }

                // Start the next activity
                Intent intent = new Intent(MainActivity.this, JoystickActivity.class);

                intent.putExtra("ip", ip);
                intent.putExtra("port", port);

                MainActivity.this.startActivity(intent);
            }
        });


    }


    private static final Pattern IP_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * Validate ip
     * @param ip ip
     * @return true if valid ip
     */
    public static boolean validateIP(final String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }
}
