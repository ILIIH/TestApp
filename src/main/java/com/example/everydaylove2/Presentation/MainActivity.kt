package com.example.everydaylove2.Presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.everydaylove2.Presentation.Dialog.AddMemoryDialog
import com.example.everydaylove2.R
import com.example.everydaylove2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE: Int = 100
    override fun onStart() {

        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val btmnavView: BottomNavigationView = findViewById(R.id.bottom_navbar)
        btmnavView.background = null

        createNotification()
    }

    @SuppressLint("RemoteViewLayout")
    private fun createNotification() {

        val CHANNEL_ID = "2292"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = R.string.channel_name.toString()
            val descriptionText = R.string.channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText.toString()
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notificationLayout = RemoteViews(packageName, R.layout.notification_layout)

        val customNotification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.heart)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayout)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(322, customNotification)
        }
    }

    fun add_new_memory(view: View) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var imageUri = data!!.data
            var dialog = AddMemoryDialog()
            val args = Bundle()
            args.putString("image", imageUri.toString())
            dialog.arguments = args
            dialog.show(supportFragmentManager, "Memory Fragment")
        }
    }
}
