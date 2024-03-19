package com.checkinface.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.azure.android.maps.control.AzureMap
import com.azure.android.maps.control.AzureMaps
import com.azure.android.maps.control.MapControl
import com.azure.android.maps.control.controls.ZoomControl
import com.azure.android.maps.control.events.OnClick
import com.azure.android.maps.control.events.OnReady
import com.azure.android.maps.control.layer.SymbolLayer
import com.azure.android.maps.control.source.DataSource
import com.checkinface.R
import com.checkinface.databinding.ActivityAzureMapBinding
import com.mapbox.geojson.Point

class AzureMapActivity : AppCompatActivity() {
    companion object {
        const val LON_KEY = "LON_KEY"
        const val LAT_KEY = "LAT_KEY"
        init {
            AzureMaps.setSubscriptionKey("4AWaaMcQhk29a8NB3UBEA_By3uT_FICEMAZdOiZqjc8");
        }
    }
    private var symbolLayer: SymbolLayer? = null
    private var lon: Double? = null
    private var lat: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityAzureMapBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        mapControl = findViewById(R.id.azure_map_control)

        mapControl?.onCreate(savedInstanceState)

        //Wait until the map resources are ready.
        mapControl?.onReady(OnReady { map: AzureMap ->
            map.controls.add(ZoomControl())

            val source = DataSource()
            map.sources.add(source)

            map.events.add(OnClick { lat: Double, lon: Double ->
                symbolLayer?.let { map.layers.remove(it) }

                source.clear()
                //Map clicked.
                source.add(Point.fromLngLat(lon, lat))
                this.lon = lon
                this.lat = lat
                symbolLayer = SymbolLayer(source)
                map.layers.add(symbolLayer)

                //Return true indicating if event should be consumed and not passed further to other listeners registered afterwards, false otherwise.
                return@OnClick false
            })

        })

        viewBinding.btnSetLocation.setOnClickListener{
            val returnIntent = Intent()
            returnIntent.putExtra(LAT_KEY, lat.toString())
            returnIntent.putExtra(LON_KEY, lon.toString())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    var mapControl: MapControl? = null

    public override fun onStart() {
        super.onStart()
        mapControl?.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapControl?.onResume()
    }

    public override fun onPause() {
        mapControl?.onPause()
        super.onPause()
    }

    public override fun onStop() {
        mapControl?.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        mapControl?.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        mapControl?.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapControl?.onSaveInstanceState(outState)
    }
}