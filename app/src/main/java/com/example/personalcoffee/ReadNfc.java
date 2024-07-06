package com.example.personalcoffee;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;

public class ReadNfc extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        textView = findViewById(R.id.textView19);

        // PendingIntent 설정
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage ndefMessage = readNdefMessage(intent);
            if (ndefMessage != null) {
                NdefRecord[] records = ndefMessage.getRecords();
                if (records != null && records.length > 0) {
                    NdefRecord record = records[0];
                    String payload = new String(record.getPayload(), Charset.forName("UTF-8"));
                    textView.setText(payload);
                    Toast.makeText(this, "NFC 읽기 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "NFC 태그에 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "NFC 태그를 읽을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private NdefMessage readNdefMessage(Intent intent) {
        NdefMessage ndefMessage = null;
        try {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null && rawMessages.length > 0) {
                ndefMessage = (NdefMessage) rawMessages[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ndefMessage;
    }

}

