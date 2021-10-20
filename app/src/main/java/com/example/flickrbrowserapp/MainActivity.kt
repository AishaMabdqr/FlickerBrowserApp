package com.example.flickrbrowserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_view.view.*
import kotlinx.android.synthetic.main.item_row.*
import kotlinx.android.synthetic.main.item_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var eInput : EditText
    lateinit var bSearch : Button
    lateinit var rvItems : RecyclerView
    lateinit var rvAdapter: RVAdapter
    lateinit var items : ArrayList<PhotoList>
    lateinit var clMain : ConstraintLayout

    var id = ""
    var secret = ""
    var server = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        items = ArrayList()

        eInput = findViewById(R.id.eInput)
        bSearch = findViewById(R.id.bSearch)
        rvItems = findViewById(R.id.rvItems)
        clMain = findViewById(R.id.clMain)



        rvAdapter = RVAdapter(this, items)
        rvItems.adapter = rvAdapter
        rvItems.layoutManager = LinearLayoutManager(this)



        bSearch.setOnClickListener {
            requestAPI()
        }

        llImage.setOnClickListener{
            closeImage()
        }
    }

    private fun requestAPI(){
        CoroutineScope(IO).launch {
            val data = async { fetchData() }.await()

            if (data.isNotEmpty()){
                populateData(data)
                Log.d("Main", "Data : $data")
            }
        }
    }

    private fun fetchData() : String{
        var response = ""
        try{
            response = URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=7fdbf2b412b6b3adc28816636497c229&tags=${eInput.text}&format=json&nojsoncallback=1").readText(Charsets.UTF_8)
        }catch (e: Exception){
            Log.d("Main", "Issue: $e")
        }
        return response
    }

    private suspend fun populateData(result : String) {
        withContext(Main) {
            val jsonObject = JSONObject(result)

           val photos =  jsonObject.getJSONObject("photos").getJSONArray("photo")


            for (i in 0 until photos.length()){
                var title = photos.getJSONObject(i).getString("title")
                id = photos.getJSONObject(i).getString("id")
                server = photos.getJSONObject(i).getString("server")
                secret = photos.getJSONObject(i).getString("secret")
                var image = "https://live.staticflickr.com/${server}/${id}_${secret}_q.jpg"
                items.add(PhotoList(title,image))

            }

            Log.d("Main", "elements: +${items}")

        }
    }

    fun showImage(link : String){
        Glide.with(this).load(link).into(bigImage)
        rvItems.isVisible = false
        llMain.isVisible = false
        llImage.isVisible = true
    }

    fun closeImage(){
        rvItems.isVisible = true
        llMain.isVisible = true
        llImage.isVisible = false
    }
}