package com.example.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_main)
        setContentView(binding.root)

        fetchWeatherData("Dubai")
        searchcity()
    }
    private fun searchcity(){
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener { //Changed Interface Name
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })


    }

    private fun fetchWeatherData(cityname: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(
            cityname,
            "03697b212e8d55835e5c4a4d9923a962",
            "metric"
        )

        response.enqueue(object : Callback<WeatherX> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherX>, response: Response<WeatherX>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temprature = responseBody.main.temp.toString()
                   // val description = responseBody.weather[0].description
                    binding.temp.text="$temprature °C"
                    val humidity=responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    val sunrise=responseBody.sys.sunrise
                    val sunset=responseBody.sys.sunset
                    val sealevel=responseBody.main.pressure
                    val condition=responseBody.weather.firstOrNull()?.main?:"unknown"

                    val maxtemp=responseBody.main.temp_max
                    val mintemp=responseBody.main.temp_min
                    binding.weather.text=condition
                    binding.maxtemp.text="$maxtemp "
                    binding.mintemp.text="$mintemp "
                    binding.humidity.text="$humidity %"
                    binding.windspeed.text="$windspeed m/s"
                    binding.sunrise.text="$sunrise"
                    binding.sunset.text="$sunset"
                    binding.sea.text="$sealevel hPa"
                    binding.condition.text=condition
                    binding.day.text=date()
                        binding.date.text=dayname()
                        binding.cityname.text= cityname
                    changeback(condition)


                    //  Log.d("TAG", "Temperature: $temperature, Description: $description")
                }
            }

            override fun onFailure(call: Call<WeatherX>, t: Throwable) {
                Log.e(TAG, "Error fetching weather data", t)
            }
        })
    }
    @SuppressLint("WeekBasedYear")
    fun dayname(): String{
        val sdf=SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun date(): String{
        val sdf=SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun changeback(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {  // Corrected "Sheeer's" to "Showers" and "-" to "->"
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }
}
