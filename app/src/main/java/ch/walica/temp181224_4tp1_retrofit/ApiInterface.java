package ch.walica.temp181224_4tp1_retrofit;

import java.util.List;

import ch.walica.temp181224_4tp1_retrofit.models.Country;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("europe")
    Call<List<Country>> getEuropeCountries();

    @GET("{region}")
    Call<List<Country>> getCountries(@Path("region") String region);
}
