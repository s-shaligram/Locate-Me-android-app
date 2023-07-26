package com.example.finalexam_1121367

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import com.google.android.gms.maps.GoogleMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Sameer Android 1 Final Exam"
    }

    fun displayOnClick(view: View)
    {
//        43.664103, -79.343009
        val lat: Double = 43.664103
        val lng: Double = -79.343009
        val intent = Intent(this@MainActivity,MapsActivity::class.java)

        intent.putExtra("lat",lat)
        intent.putExtra("lng", lng)

        startActivity(intent)
    }
}