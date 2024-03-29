package com.example.alarma

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.example.alarma.RingtoneService.Companion.r

class RingtoneService: Service() {
    companion object {
        lateinit var r: Ringtone
    }
    var id: Int=0
    var isRunning: Boolean =false

    override fun onBind(intent: Intent?): IBinder?
    {
        return null
    }

    override fun onStartCommand(intent:Intent?, flags: Int, startId: Int): Int {
        var state: String = intent!!.getStringExtra("extra")
        assert(state != null)
        when (state) {
            "on" -> id = 1
            "off" -> id = 0
        }

        if (!this.isRunning && id == 1) {
            playAlarm()
            this.isRunning = true
            this.id = 0
            fireNotification()
        } else if (!this.isRunning && id == 0) {
            this.isRunning = false
            this.id = 0
        } else if (!this.isRunning && id == 0) {
            this.isRunning = false
            this.id = 0
        } else if (!this.isRunning && id == 1) {
            this.isRunning = true
            this.id = 1
        } else {
        }

        return START_NOT_STICKY
    }
        private fun  fireNotification() {
            var main_activity_intent : Intent = Intent(this,MainActivity::class.java)
            var pi: PendingIntent = PendingIntent.getActivities(this,0, arrayOf(main_activity_intent),0)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            var notify_manager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var notification : Notification = NotificationCompat.Builder(this)
                .setContentTitle("Alarma apagada")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentText("Tocame")
                .setContentIntent(pi)
                .setAutoCancel (true)
                .build()
            notify_manager.notify(0,notification)

        }

    private fun playAlarm(){
        var alarmUri : Uri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri==null)
        {
            alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        r = RingtoneManager.getRingtone(baseContext, alarmUri)
        r.play()
    }


    override fun onDestroy()
    {

        super.onDestroy()
        this.isRunning = false
    }


}