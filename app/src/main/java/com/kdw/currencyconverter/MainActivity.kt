package com.kdw.currencyconverter

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.materialspinner.MaterialSpinner
import com.kdw.currencyconverter.databinding.ActivityMainBinding
import com.kdw.currencyconverter.util.Helper
import com.kdw.currencyconverter.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

private const val API_KEY = BuildConfig.CURRENCY_API_ID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var selectedCountry1: String ?= "AFN"
    private var selectedCountry2: String ?= "AFN"

    private var pressedTime: Int = 0

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var selectedCountry1: String ?= "AFN"
        var selectedCountry2: String ?= "AFN"

        init()

        binding.convertBtn.setOnClickListener {
            convertFunction()
        }
    }

    private fun init() {

        Helper.makeStatusTransparent(this@MainActivity)

        setSpinner(binding.inputFirstCountry)
        setSpinner(binding.inputSecondCountry)

        binding.inputFirstCountry.setOnItemSelectedListener { view, position, id, item ->
            val countryCode = getCode(item.toString())
            val symbol = getSymbol(countryCode)
            selectedCountry1 = symbol
        }

        binding.inputSecondCountry.setOnItemSelectedListener { view, position, id, item ->
            val countryCode = getCode(item.toString())
            val symbol = getSymbol(countryCode)
            selectedCountry2 = symbol
        }

    }

    private fun setSpinner(spinner: MaterialSpinner) {
        spinner.setItems(getAllCountries())
        spinner.setOnClickListener {
            Helper.hideKeyBoard(this@MainActivity)
        }
    }

    //나라 코드로부터 화폐 이름 가져오는 메소드
    // 한국 => KDW
    // 미국 => USD
    private fun getSymbol(countryCode: String?): String? {
        val currentLocales = Locale.getAvailableLocales()
        for(i in currentLocales.indices) {
            if(currentLocales[i].country == countryCode){
                return Currency.getInstance(currentLocales[i]).currencyCode
            }
        }
        return null
    }

    //나라 이름으로부터 나라 코드 얻는 메소드
    private fun getCode(countryName: String) = Locale.getISOCountries().find {
        Locale("", it).displayCountry == countryName
    }

    //전세계 나라들 목록 가져오기
    private fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()

        for(locale in locales) {
            val country = locale.displayCountry
            if(country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)){
                countries.add(country)
            }
        }
        countries.sort()
        return countries
    }

    private fun convertFunction() {

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