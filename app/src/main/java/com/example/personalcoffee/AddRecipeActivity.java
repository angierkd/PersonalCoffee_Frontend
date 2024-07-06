package com.example.personalcoffee;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.personalcoffee.Model.Recipe;
import com.example.personalcoffee.databinding.ActivityAddRecipeBinding;
import com.example.personalcoffee.databinding.ActivityLoginBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeActivity  extends AppCompatActivity {

    private ActivityAddRecipeBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Recipe updateRecipe = (Recipe) intent.getSerializableExtra("updateRecipe");

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 표시 안 함
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        }

        //스피너 추가
        Spinner spinner = findViewById(R.id.spinner);
        addSpinner(spinner);
        Spinner spinner2 = findViewById(R.id.spinner2);
        addSpinner(spinner2);
        Spinner spinner3 = findViewById(R.id.spinner3);
        addSpinner(spinner3);

        if(updateRecipe != null){
            binding.editTextTextPersonName6.setText(updateRecipe.getName());
            spinner.setSelection(adapter.getPosition(updateRecipe.getEspresso()));
            spinner2.setSelection(adapter.getPosition(updateRecipe.getWater()));
            spinner3.setSelection(adapter.getPosition(updateRecipe.getSyrup()));
            binding.button2.setText("수정하기");
        }

        //추가하기 버튼
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.editTextTextPersonName6.getText().toString();

                String espresso = spinner.getSelectedItem().toString();
                String water = spinner2.getSelectedItem().toString();
                String syrup = spinner3.getSelectedItem().toString();

                if(updateRecipe == null) {
                    addRecipe(new Recipe("0", espresso, water, syrup, name,"0"));
                }else{
                    updateRecipe(updateRecipe.getRecipeId(), new Recipe("0", espresso, water, syrup, name,"0"));
                }
            }
        });
    }

    private void addRecipe(Recipe recipe){
        Call<ResponseBody> call = Net.getInstance().getApiService().createRecipe(recipe);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("실패");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                t.printStackTrace();
                System.out.println("에러");
            }
        });
    }

    private void updateRecipe(String recipeId, Recipe recipe) {
        Call<ResponseBody> call = Net.getInstance().getApiService().updateRecipe(recipeId, recipe);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    System.out.println("성공");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("실패");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                t.printStackTrace();
                System.out.println("에러");
            }
        });
    }


    private void addSpinner(Spinner spinner) {
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
