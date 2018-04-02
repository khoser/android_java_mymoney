package com.example.ktu.myapplication;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by dum on 15.05.2015.
 */
//public class AuthActivity extends AppCompatActivity {
public class AuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        String tmp_url = new String();
        try {
            // отрываем поток для записи
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(getString(R.string.auth_file))));
            // пишем данные
            br.readLine(); // secure header
            tmp_url = br.readLine(); // url
            // закрываем поток
            br.close();
            //Log.d(LOG_TAG, "Файл записан");

        } catch (Exception e) {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        EditText et_url = (EditText) findViewById(R.id.et_url);
        et_url.setText(tmp_url);
    }

    public void setdefaulturl(View v) {
        EditText et_url = (EditText) findViewById(R.id.et_url);
        et_url.setText(getString(R.string.defaultURL));
    }

    public void onclick(View v) {
        EditText et_url = (EditText) findViewById(R.id.et_url);
        EditText et_login = (EditText) findViewById(R.id.et_login);
        EditText et_pass = (EditText) findViewById(R.id.et_pass);

        String Security_Header_Value_First = et_login.getText().toString();
        String Security_Header_Value_Second = et_pass.getText().toString();

        if (Security_Header_Value_First.isEmpty() || Security_Header_Value_Second.isEmpty()
                || et_url.getText().toString().isEmpty()) {
            return;
        }

        StringBuffer buf = new StringBuffer(Security_Header_Value_First);
        buf.append(getString(R.string.colon)).append(Security_Header_Value_Second);
        byte[] raw = buf.toString().getBytes();
        buf.setLength(0);
        buf.append("Basic ");
        org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
        String res_str = buf.toString();

        boolean allok;

        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(getString(R.string.auth_file), MODE_PRIVATE)));
            // пишем данные
            bw.write(res_str);
            bw.newLine();
            bw.write(et_url.getText().toString());
            // закрываем поток
            bw.close();
            //Log.d(LOG_TAG, "Файл записан");

            allok = true;

        } catch (Exception e) {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            allok = false;
        }

        if (allok) {
            finish();
        }
    }

}
