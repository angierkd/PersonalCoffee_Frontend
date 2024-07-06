package com.example.personalcoffee;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.personalcoffee.databinding.ActivityAddNfcBinding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNfcActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private ActivityAddNfcBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddNfcBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.nfcToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 표시 안 함
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        }

        // NFC 어댑터 설정
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC 기능을 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
        if (intent != null &&
                (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())||NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] uidBytes = tag.getId();
            String uid = bytesToHexString(uidBytes);
            String formattedString = hexStringToFormattedString(uid);
            String nfcId = convertToPartialFormat(formattedString);
            addNfc(nfcId);
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String hexStringToFormattedString(String hexString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexByte = hexString.substring(i, i + 2);
            int decimal = Integer.parseInt(hexByte, 16);
            sb.append(decimal).append(" ");
        }
        // 마지막 공백 제거
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public String convertToPartialFormat(String input) {
        String[] values = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i < values.length) {
                sb.append(values[i]).append("*");
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private void addNfc(String nfcId) {
        Call<ResponseBody> call = Net.getInstance().getApiService().addNfc(nfcId);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    Toast.makeText(getApplication(), "NFC 칩 등록 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "NFC 칩 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("실패");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                t.printStackTrace();
                Toast.makeText(getApplication(), "NFC 칩 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                System.out.println("에러");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

