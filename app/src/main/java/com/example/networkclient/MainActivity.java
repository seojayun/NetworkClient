package com.example.networkclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText edtMessage;
    Button btnSend;
    TextView tvReceive;
    InputStream is;
    OutputStream os;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //쓰레드에서 네트워크 처리를 할 수 있도록 지정해주는 부분
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //쓰레드에서 네트워크 처리를 할 수 있도록 지정해주는 부분
        setContentView(R.layout.activity_main);

        edtMessage = (EditText)findViewById(R.id.edtMessage);
        btnSend = (Button)findViewById(R.id.btnSend);
        tvReceive = (TextView)findViewById(R.id.tvReceive);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                tvReceive.setText(msg);
                tvReceive.setText("");
            }
        });

    }

    //통신 준비하는 메소드  (시작)
        public void start() {
         try {
                socket = new Socket("192.168.2.62",7000);
                sendMessage(socket);
                receiveMessage(socket);
         }catch (Exception e) {
             showToast("서버와 연결 할 수 없습니다.");
         }finally {
             try {
                 socket.close();
             }catch (Exception e){
             }
         }
        }
    //통신 준비하는 메소드 (끝)

    //서버로 부터 데이터 보내는 메소드 (시작)
    public void sendMessage(Socket socket) {
        try {

            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
            String sMsg = "서재윤 : "+
                    edtMessage.getText().toString();
            oos.writeObject(sMsg);

        }catch (Exception e) {
            showToast("서버로 데이터를 보낼 수 없습니다.");
        }
    }
    //서버로 부터  데이터 보내는 메소드 (끝)

    //서버로  부터  데이터를 받는 메소드(시작)
    public void receiveMessage(Socket socket) {
        try {

            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            msg=(String)ois.readObject();

        }catch (Exception e) {
            showToast("서버로 데이터를 받을 수 없습니다.");
        }
    }
    //서버로  부터  데이터를 받는 메소드(끝)

    public  void showToast (String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
