package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.content.Context
import java.io.IOException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    lateinit var outputTextView: TextView
    var lastNumaric: Boolean = false
    var stateError: Boolean = false
    var lastDot :Boolean = false
    var currencies: List<Currency> = listOf()
    var currentCurrency: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        readFile()
        outputTextView = findViewById(R.id.textView1)
    }

    fun euro(view: View){
        clear(view)
        currentCurrency = "EUR"
        Log.i("currency current", "$currentCurrency")
        setContentView(R.layout.activity_main)
        outputTextView = findViewById(R.id.textView2)
    }
    fun dollar(view: View){
        clear(view)
        currentCurrency = "USD"
        Log.i("currency current", "$currentCurrency")
        setContentView(R.layout.activity_main)
        outputTextView = findViewById(R.id.textView2)
    }
    fun ruble(view: View){
        clear(view)
        currentCurrency = "RUB"
        Log.i("currency current", "$currentCurrency")
        setContentView(R.layout.activity_main)
    }
    fun wtf(view: View){
        clear(view)
        currentCurrency = "WTF"
        Log.i("currency current", "$currentCurrency")
        setContentView(R.layout.activity_main)
    }

    fun addDigit(view: View)
    {
        if(stateError)
        {
            outputTextView.text=(view as Button).text
            stateError=false
        }else {
            outputTextView.append((view as Button).text)
        }
        lastNumaric=true
        Log.i("currentDigit", outputTextView.text.toString())
    }

    fun addPoint(view: View)
    {
        if(lastNumaric && !stateError && !lastDot)
        {
            outputTextView.append(".")
            lastNumaric=false
            lastDot=true
        }
    }


    fun clear(view: View)
    {
        this.outputTextView.text= ""
        lastNumaric=false
        stateError=false
        lastDot=false
        currentCurrency = ""
        setContentView(R.layout.activity_welcome)
    }

    fun exit() { this.finishAffinity(); }

    fun result(view: View)
    {
        if(lastNumaric && !stateError)
        {
            try
            {
                var number = outputTextView.text.toString().toInt()
                Log.i("size of list", "${currencies.size}")
                Log.i("check", currencies[0].currency)
                Log.i("check2", currentCurrency)
                val banknotesList = currencies.filter {  it.currency.equals(currentCurrency) }
                Log.i("size of banknotesList", "${banknotesList.size}")
                val banknotes = banknotesList[0].banknotes.toIntArray()
                banknotes.sort()
                banknotes.reverse()
                var output: String = ""
                for (value in banknotes) {
                    val count = number / value
                    number -= value * count
                    output += "$value: $count шт, "
                }
//                val result = "I just wanna life that’s worth living"
//                outputTextView.text= result.toString()
                Log.i("answer", output)
                outputTextView.text= output
                lastDot=true
            } catch (ex:Exception)
            {
                outputTextView.text="Error"
                ex.printStackTrace()
//                Log.i("answer", "Error: ${ex.message}\n${ex.stackTrace}")
                stateError=true
                lastNumaric=false
            }
        }

    }

    fun readFile(){

        val jsonFileString = getJsonDataFromAsset(applicationContext, "currencies.json")
        if (jsonFileString != null) {
            Log.i("data", jsonFileString)
        }
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Currency>>() {}.type
        var localCurrencies: List<Currency> = gson.fromJson(jsonFileString, listPersonType)
        localCurrencies.forEachIndexed { idx, currency -> Log.i("data", "> Item $idx:\n$currency") }
        currencies = localCurrencies
    }
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    data class Currency(val currency: String, val banknotes: List<Int>)
}