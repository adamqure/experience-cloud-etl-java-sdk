package java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Static API class to be used with the API calls
 */
public class API {
    private static API ourInstance = new API();
    private final String baseURL = "";


    public static API getInstance() {
        return ourInstance;
    }

    private API() {
    }


    /**
     * Insert API Calls here in the following format
     *
     * @CallType("URI") Call<Response Format> methodName(@Body NonGsonFormattedBodyType variableName)
     * <p>
     * See https://square.github.io/retrofit/ for documentation
     */

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    OkHttpClient okHttpClient = new OkHttpClient();

    Retrofit retrofitAuth = new Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
