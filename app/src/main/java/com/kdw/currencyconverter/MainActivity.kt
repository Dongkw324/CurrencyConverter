package com.kdw.currencyconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kdw.currencyconverter.databinding.ActivityMainBinding
import com.kdw.currencyconverter.model.Rates
import com.kdw.currencyconverter.util.Helper
import com.kdw.currencyconverter.util.Resource
import com.kdw.currencyconverter.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

// 객체를 주입할 Android 클래스에 @AndroidEntryPoint Annotation을 추가
// 이것만으로 자동으로 생명주기에 따라 적절한 시점에 Hilt 요소로 인스턴스화 되어 처리된다.
// 이 Annotation으로 의존성 주입의 시작점을 나타낸다.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var selectedCountry1: String ?= "AFN"
    private var selectedCountry2: String ?= "AFN"

    private var pressedTime: Int = 0

    private val mainViewModel: MainViewModel by viewModels()

    private val API_KEY = BuildConfig.CURRENCY_API_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.makeStatusTransparent(this)

        init()

        setUpClickListener()
    }

    private fun init(){

        setSpin(binding.inputFirstCountry)
        //Handle selected item, by getting the item and storing the value in a  variable - selectedItem1
        binding.inputFirstCountry.setOnItemSelectedListener { view, position, id, item ->
            //Set the currency code for each country as hint
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedCountry1 = currencySymbol
        }

        setSpin(binding.inputSecondCountry)
        //Handle selected item, by getting the item and storing the value in a  variable - selectedItem2,
        binding.inputSecondCountry.setOnItemSelectedListener { view, position, id, item ->
            //Set the currency code for each country as hint
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedCountry2 = currencySymbol
        }

    }

    private fun setSpin(spinner : MaterialSpinner) {
        spinner.setOnClickListener {
            Helper.hideKeyBoard(this)
        }

        spinner.setItems(getAllCountries())
    }

    private fun getSymbol(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    private fun getCountryCode(countryName: String) = Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    private fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()

        return countries
    }

    private fun setUpClickListener() {

        //Convert button clicked - check for empty string and internet then do the conersion
        binding.convertBtn.setOnClickListener {

            //check if the input is empty
            val numberToConvert = binding.inputAmount.editText?.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                Snackbar.make(
                    binding.mainLayout,
                    "Input a value in the first text field, result will be shown in the second text field",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            //check if internet is available
            else if (!Helper.isNetWordConnected(this)) {
                Snackbar.make(
                    binding.mainLayout,
                    "You are not connected to the internet",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            //carry on and convert the value
            else {
                doConversion()
            }
        }
    }

    private fun doConversion(){

        //hide keyboard
        Helper.hideKeyBoard(this)

        //make progress bar visible
        binding.progressWork.visibility = View.VISIBLE

        //Get the data inputed
        val apiKey = API_KEY
        val from = selectedCountry1.toString()
        val to = selectedCountry2.toString()
        val amount = binding.inputAmount.editText!!.text.toString().toDouble()

        //do the conversion
        mainViewModel.getConvertData(apiKey, from, to, amount)

        //observe for changes in UI
        observeUi()

    }

    @SuppressLint("SetTextI18n")
    private fun observeUi() {


        mainViewModel.data.observe(this, androidx.lifecycle.Observer {result ->

            when(result.status){
                Resource.Status.SUCCESS -> {
                    if (result.data?.status == "success"){

                        val map: Map<String, Rates>

                        map = result.data.rates

                        map.keys.forEach {

                            val rateForAmount = map[it]?.rate_for_amount

                            mainViewModel.convertedRate.value = rateForAmount

                            //format the result obtained e.g 1000 = 1,000
                            val formattedString = String.format("%,.4f", mainViewModel.convertedRate.value)

                            //set the value in the second edit text field
                            binding.resultCurrencyAmount.setText(formattedString)

                        }


                        binding.progressWork.visibility = View.GONE

                    }
                    else if(result.data?.status == "fail"){
                        val layout = binding.mainLayout
                        Snackbar.make(layout,"Ooops! something went wrong, Try again", Snackbar.LENGTH_LONG)
                            .show()

                        binding.progressWork.visibility = View.GONE

                    }
                }
                Resource.Status.ERROR -> {

                    val layout = binding.mainLayout
                    Snackbar.make(layout,  "Oopps! Something went wrong, Try again", Snackbar.LENGTH_LONG)
                        .show()

                    binding.progressWork.visibility = View.GONE
                }
            }
        })
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if(pressedTime == 0){
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            pressedTime = System.currentTimeMillis().toInt()
        } else {
            val seconds = System.currentTimeMillis().toInt() - pressedTime

            if(seconds > 2000) {
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                pressedTime = 0
            } else{
                super.onBackPressed()
            }
        }

    }
}