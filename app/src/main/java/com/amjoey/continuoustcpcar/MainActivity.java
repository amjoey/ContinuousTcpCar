package com.amjoey.continuoustcpcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import simpletcp.ContinuousTcpClient;
import simpletcp.TcpUtils;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public static final int TCP_PORT = 9001;

    private Button buttonConnect;
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonLeft;
    private Button buttonRight;
    private TextView textViewStatus;
    private EditText editTextIpAddress;

    private ContinuousTcpClient continuousTcpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = findViewById(R.id.textViewStatus);
        editTextIpAddress = findViewById(R.id.editTextIpAddress);
        buttonUp = findViewById(R.id.buttonUp);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonConnect = findViewById(R.id.buttonConnect);

        TcpUtils.forceInputIp(editTextIpAddress);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (buttonConnect.getText().toString().equals("Open")) {
                    buttonConnect.setEnabled(false);
                    editTextIpAddress.setEnabled(false);

                    String ip = editTextIpAddress.getText().toString();
                    continuousTcpClient.connect(ip, TCP_PORT, new ContinuousTcpClient.ConnectionCallback() {
                        public void onConnectionFailed(String ip, Exception e) {
                            textViewStatus.setText("Disconnect");
                            buttonConnect.setEnabled(true);
                            editTextIpAddress.setEnabled(true);
                        }

                        public void onConnected(String hostName, String hostAddress) {
                            textViewStatus.setText("Connect");
                            editTextIpAddress.setEnabled(false);
                            buttonConnect.setEnabled(true);
                            buttonConnect.setText("Close");
                        }
                    });
                } else if (buttonConnect.getText().toString().equals("Close")) {
                    continuousTcpClient.disconnect();
                    textViewStatus.setText("Disconnect");
                    editTextIpAddress.setEnabled(true);
                    buttonConnect.setText("Open");
                }
            }
        });

        buttonUp.setOnTouchListener(buttonTouchListener);
        buttonDown.setOnTouchListener(buttonTouchListener);
        buttonLeft.setOnTouchListener(buttonTouchListener);
        buttonRight.setOnTouchListener(buttonTouchListener);

        continuousTcpClient = new ContinuousTcpClient(TCP_PORT, new ContinuousTcpClient.TcpConnectionListener() {
            public void onDisconnected() {
            }

            public void onDataReceived(String message, String ip) {
            }

            public void onConnected(String hostName, String hostAddress, Socket s) {
            }
        });
    }
    private View.OnTouchListener buttonTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN ) {
                String str = "";
                int id = v.getId();
                if (id == R.id.buttonUp) {
                    str = "buttonUp\r";
                } else if (id == R.id.buttonDown) {
                    str = "buttonDown\r";
                } else if (id == R.id.buttonLeft) {
                    str = "buttonLeft\r";
                } else if (id == R.id.buttonRight) {
                    str = "buttonRight\r";
                }

                continuousTcpClient.send(str);
            }
            if ( action == MotionEvent.ACTION_UP) {
                String str = "";
                int id = v.getId();
                if (id == R.id.buttonUp) {
                    str = "StopCar\r";
                } else if (id == R.id.buttonDown) {
                    str = "StopCar\r";
                } else if (id == R.id.buttonLeft) {
                    str = "StopCar\r";
                } else if (id == R.id.buttonRight) {
                    str = "StopCar\r";
                }

                continuousTcpClient.send(str);
            }
            return false;
        }
    };

    public void onResume() {
        super.onResume();
        //continuousTcpClient.disconnect();
        textViewStatus.setText("Disconnect");
        editTextIpAddress.setEnabled(true);
        buttonConnect.setText("Open");
        continuousTcpClient.start();
    }

    public void onStop() {
        super.onStop();
        continuousTcpClient.stop();
    }
}