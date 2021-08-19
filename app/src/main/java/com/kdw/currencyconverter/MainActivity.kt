package com.kdw.currencyconverter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kdw.currencyconverter.databinding.ActivityMainBinding
import com.kdw.currencyconverter.util.Helper
import com.kdw.currencyconverter.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        binding.inputFirstCountry.setItems(getAllCountries())
        binding.inputFirstCountry.setOnClickListener {
            Helper.hideKeyBoard(this@MainActivity)
        }


        binding.inputSecondCountry.setItems(getAllCountries())

    }

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
}