package com.example.shakeib.mediaplaybackpractice

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var media:MediaPlayer? = null
    var audioManager:AudioManager? = null

    var afChangeListener = AudioManager.OnAudioFocusChangeListener {
        when(it){
            //if focus is gained
            AudioManager.AUDIOFOCUS_GAIN ->{
                media?.start()
            }
            //focus lost example phone call
            AudioManager.AUDIOFOCUS_LOSS ->{
                media?.pause()
            }
            //focus lost temporarily example notifcation sound
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->{
                audioManager?.adjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing media player and audio manager
        media = MediaPlayer.create(this,R.raw.bts_dna)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        //play button code
        playButton.setOnClickListener {
            //storing result of audio focus by request audio focus and passing audiofocuschangelistener in it
            var result = audioManager?.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)

            //media should only play when focus is granted
            if(result== AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                media?.start()
            }

        }

        pauseButton.setOnClickListener {
            media?.pause()
        }

        stopButton.setOnClickListener {
            media?.stop()

            //reinitializing media object so that the song can be played again
            media = MediaPlayer.create(this,R.raw.bts_dna)

            //abandoning audio focus
            audioManager?.abandonAudioFocus(afChangeListener)
        }


    }

    override fun onStop() {
        super.onStop()
        media?.release()
    }


}

