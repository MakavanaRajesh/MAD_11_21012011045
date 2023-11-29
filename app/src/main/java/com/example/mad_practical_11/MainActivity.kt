package com.example.mad_practical_11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
class MainActivity : AppCompatActivity() {
    lateinit
var recyclerView : RecyclerView lateinit var
    databaseHelper: DatabaseHelper override fun
            onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)
        var toolBar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        val fetchBtn : FloatingActionButton = findViewById(R.id.btnSwap)
        recyclerView = findViewById(R.id.recyclerView)
        fetchBtn.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data = HttpRequest().makeServiceCall(
                        "https://api.json-generator.com/templates/qjeKFdjkXCdK/data",
                        "rbn0rerl1k0d3mcwgw7dva2xuwk780z1hxvyvrb1"
                    )
                    withContext(Dispatchers.Main) {
                        try {
                            if(data != null)
                            {
                                runOnUiThread{getPersonDetailsFromJson(data)}
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) { R.id.action_button1 -> {
            Toast.makeText(this@MainActivity, "Clicked on item at menu!",
                Toast.LENGTH_SHORT).show()
            return true
        }
            R.id.action_button2 -> { var personList: ArrayList<Person> =
                databaseHelper.getAllPersons() recyclerView.layoutManager =
                LinearLayoutManager(this)
                recyclerView.adapter = PersonAdapter(this, personList)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun getPersonDetailsFromJson(sJson: String?)
    {
        val personList = ArrayList<Person>()
        try {
            val jsonArray = JSONArray(sJson)
            for(i in 0 until jsonArray.length())
            {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                personList.add(person)
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter =PersonAdapter(this, personList)
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }
    }
}
MapsActivity.kt Code: package
com.example.mad_practical_11
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mad_practical_11
.databinding.ActivityMapsBinding
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val TAG = "MapActivity"
    private var lat = -34.0
    private var log = 151.0
    private var title = "Marker in Sydney"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val obj = intent.getSerializableExtra("Object") as Person
        Log.i(TAG, "onCreate: Object:$obj")
        lat = obj.latitude
        log = obj.longitude
        title = obj.name
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this
    case, * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
    install * it inside the SupportMapFragment. This method will only be triggered
    once the user has * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
// Add a marker in Sydney and move the camera
        val sydney = LatLng(lat, log)
        mMap.addMarker(MarkerOptions().position(sydney).title(title)) //

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8.0f))
    }
}
Person.kt Code: package
com.example.mad_practical_11
importorg.json.JSONObject
import java.io.Serializable
class Person ( var
               id:String, var
               name:String, var
               emailId:String, var
               phoneNo: String, var
               address :String, var
               latitude:Double,
               var longitude:Double):Serializable {
    constructor(jsonObject:JSONObject):this ("","","","","",90.0,09.8){
        id = jsonObject.getString("id")
        emailId = jsonObject.getString("email")
        phoneNo = jsonObject.getString("phone")
        val profileJson = jsonObject.getJSONObject("profile")
        name = profileJson.getString("name") address =
        profileJson.getString("address")
        val locationJson = profileJson.getJSONObject("location")
        latitude = locationJson.getDouble("lat")
        longitude = locationJson.getDouble("long")
    }
}
