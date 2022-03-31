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
        outputTextView = findViewById(R.id.textView2)
    }

    fun euro(view: View){
        currentCurrency = "EUR"
        R.layout.activity_main
    }
    fun dollar(view: View){
        currentCurrency = "USD"
        R.layout.activity_main
    }
    fun ruble(view: View){
        currentCurrency = "RUB"
        R.layout.activity_main
    }
    fun wtf(view: View){
        currentCurrency = "WTF"
        R.layout.activity_main
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
        R.layout.activity_welcome
    }

    fun exit() { this.finishAffinity(); }

    fun result(view: View)
    {
        if(lastNumaric && !stateError)
        {
//            val expression= ExpressionBuilder(text).build()
            try
            {
                val number = outputTextView.text.toString().toInt()

//        todo: read file & realize algorithm & output result

//                val result= expression.evaluate()
                val result = "I just wanna life that’s worth living"
                outputTextView.text= result.toString()
                lastDot=true
            } catch (ex:Exception)
            {
                outputTextView.text="Error"
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