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

        binding.inputFirstCountry.setOnItemSelectedListener { view, position, id, item ->

            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getSymbol(countryCode)
            selectedCountry1 = currencySymbol
        }

        setSpin(binding.inputSecondCountry)

        binding.inputSecondCountry.setOnItemSelectedListener { view, position, id, item ->

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

    //Locale 클래스: 지역의 언어나 나라 등의 정보를 담고 있는 클래스
    private fun getSymbol(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    //국가 코드 얻는 메소드
    private fun getCountryCode(countryName: String) = Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    //모든 국가들의 목록을 가져오는 메소드
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

        binding.convertBtn.setOnClickListener {


            val numberToConvert = binding.inputAmount.editText?.text.toString()

            if (numberToConvert.isEmpty() || numberToConvert == "0") {
                Snackbar.make(
                    binding.mainLayout,
                    "값을 입력하시오",
                    Snackbar.LENGTH_LONG
                ).show()
            }


            else if (!Helper.isNetWordConnected(this)) {
                Snackbar.make(
                    binding.mainLayout,
                    "네트워크 연결을 확인하시오",
                    Snackbar.LENGTH_LONG
                ).show()
            }


            else {
                doConversion()
            }
        }
    }

    private fun doConversion(){


        Helper.hideKeyBoard(this)


        binding.progressWork.visibility = View.VISIBLE


        val apiKey = API_KEY
        val from = selectedCountry1.toString()
        val to = selectedCountry2.toString()
        val amount = binding.inputAmount.editText!!.text.toString().toDouble()


        mainViewModel.getConvertData(apiKey, from, to, amount)


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


                            val formattedString = String.format("%,.4f", mainViewModel.convertedRate.value)


                            binding.resultCurrencyAmount.setText(formattedString)

                        }


                        binding.progressWork.visibility = View.GONE

                    }
                    else if(result.data?.status == "fail"){
                        val layout = binding.mainLayout
                        Snackbar.make(layout,"응답 없음. 다시 시도하세요", Snackbar.LENGTH_LONG)
                            .show()

                        binding.progressWork.visibility = View.GONE

                    }
                }
                Resource.Status.ERROR -> {

                    val layout = binding.mainLayout
                    Snackbar.make(layout,  "응답 없음. 다시 시도하세요", Snackbar.LENGTH_LONG)
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