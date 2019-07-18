package com.example.alarma

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    var hour: Int = 0
    var min: Int = 0
    lateinit var pi: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //this.con = this
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = findViewById(R.id.tp) as TimePicker
        update_text = findViewById(R.id.update_text) as TextView
        btnStart = findViewById(R.id.start_alarm) as Button
        btnStop = findViewById(R.id.stop_alarm) as Button
        var calendar: Calendar = Calendar.getInstance()
        var myIntent: Intent = Intent(this, AlarmReceiver::class.java)
        btnStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar.set(Calendar.HOUR_OF_DAY, tp.hour)
                    calendar.set(Calendar.MINUTE, tp.minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = tp.hour
                    min = tp.minute
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, tp.currentHour)
                    calendar.set(Calendar.MINUTE, tp.currentMinute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    hour = tp.currentHour
                    min = tp.currentMinute
                }

                var hr_str: String = hour.toString()
                var min_str: String = min.toString()
                if (hour > 12) {
                    hr_str = (hour - 12).toString()

                }

                if (min < 10) {
                    min_str = "0$min"
                }

                set_alarm_text("Alarma Configurada: $hr_str : $min_str")
                myIntent.putExtra("extra", "on")
                pi = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        })

        btnStop.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                set_alarm_text("Alarma desactivada")
                pi = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                am.cancel(pi)
                myIntent.putExtra("extra", "off")
                sendBroadcast(myIntent)

            }

        })
    }

    private fun set_alarm_text(s: String) {
        update_text.setText(s)
    }
}