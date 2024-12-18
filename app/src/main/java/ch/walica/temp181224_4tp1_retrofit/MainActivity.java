package ch.walica.temp181224_4tp1_retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.walica.temp181224_4tp1_retrofit.adapter.CountryAdapter;
import ch.walica.temp181224_4tp1_retrofit.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String[] regions = {"europe", "asia", "americas", "africa", "oceania"};
    String selectedRegion = regions[0];

    List<Country> countries = new ArrayList<>();
    ApiInterface apiInterface;

    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        spinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        countryAdapter = new CountryAdapter(countries);
        recyclerView.setAdapter(countryAdapter);

        Retrofit retrofit = ApiClient.getRetrofit();
        apiInterface = retrofit.create(ApiInterface.class);


        fetchCountries();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRegion = regions[i];
                fetchCountries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void fetchCountries() {
        progressBar.setVisibility(View.VISIBLE);

        Call<List<Country>> call = apiInterface.getCountries(selectedRegion);

        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                countries.clear();
                countries.addAll(response.body());
                progressBar.setVisibility(View.GONE);
                countryAdapter.notifyDataSetChanged();
                Log.d("my_log", "countries: " + countries.get(0).capital()[0]);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}