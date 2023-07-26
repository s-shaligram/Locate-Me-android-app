package com.example.finalexam_1121367

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalexam_1121367.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val locationPermission = 1

    private var lat by Delegates.notNull<Double>()
    private var lng by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Sameer Android 1 Final Exam"

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val zoomLevel = 15f

//        43.664103, -79.343009
        val intent = intent

        val lati = intent.getDoubleExtra("lat",43.664103)
        val lngi = intent.getDoubleExtra("lng", -79.343009)

        lat = 43.664103
        lng = -79.343009

        val toronto = LatLng(lati, lngi) //toronto
        map.addMarker(MarkerOptions().position(toronto).title("Marker in Toronto"))
        //map.moveCamera(CameraUpdateFactory.newLatLng(london))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto,zoomLevel))
        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)

        enableMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when (item.itemId) {
            R.id.normal_map ->{
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.hybrid_map -> {
                map.mapType = GoogleMap.MAP_TYPE_HYBRID

            }
            R.id.satellite_map -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE

            }
            R.id.terrain_map -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN

            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setMapLongClick(map:GoogleMap){

        val ic = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
        map.setOnMapLongClickListener { latLang ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLang.latitude,
                latLang.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLang)
                    .title(getString(R.string.pin))
                    .snippet(snippet)
                    .icon(ic)
            )

            Log.i("lat", latLang.latitude.toString())
            Log.i("lng", latLang.longitude.toString())
            lat = latLang.latitude
            lng = latLang.longitude
        }
    }

    private fun setPoiClick(map: GoogleMap){

        val icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        map.setOnPoiClickListener { poi ->

            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
                    .icon(icon)
            )
            poiMarker?.showInfoWindow()
        }
    }

    private fun isPermissionGranted(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(

        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
        if (requestCode == locationPermission){
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                enableMyLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation(){
        if(isPermissionGranted()){
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermission
            )
        }
    }

    fun onDisplayClick(view: View){


        loadCoordinates(lat,lng)

        Log.i("lng", lng.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun loadCoordinates(lat: Double, lng: Double){
        val latitude = findViewById<View>(R.id.latVal) as TextView
        val longitude = findViewById<View>(R.id.longiVal) as TextView

        latitude.text = "Latitude is: $lat"
        longitude.text = "Longitude is: $lng"
    }

    private fun setMapStyle(map:GoogleMap){
        try{
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )
            if (!success){
                Log.e("Tag", "Style parsing failed.")
            }
        } catch (e:Resources.NotFoundException){
            Log.e("Tag", "Can't find style. Error", e)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this@MapsActivity, MainActivity::class.java)

        startActivity(intent)
    }
}

//private fun Intent.getDoubleExtra(s: String): Double {
// return 0.0
//}
