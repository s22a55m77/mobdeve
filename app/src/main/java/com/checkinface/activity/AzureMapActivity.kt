package com.checkinface.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azure.android.maps.control.AzureMap
import com.azure.android.maps.control.AzureMaps
import com.azure.android.maps.control.MapControl
import com.azure.android.maps.control.events.OnReady
import com.checkinface.R

class AzureMapActivity : AppCompatActivity() {
    companion object {
        init {
            AzureMaps.setSubscriptionKey("4AWaaMcQhk29a8NB3UBEA_By3uT_FICEMAZdOiZqjc8");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_azure_map)
        mapControl = findViewById(R.id.azure_map_control)

        mapControl?.onCreate(savedInstanceState)

        //Wait until the map resources are ready.
        mapControl?.onReady(OnReady { map: AzureMap -> })
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