package com.example.a1

import android.media.MediaPlayer
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.os.Handler
import android.os.Looper
import kotlin.random.Random

class PlayerActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var playPauseButton: Button
    private lateinit var songTitle: TextView

    private val songs = listOf(R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4)
    private val songNames = listOf("Eminem - Smack That", "Eminem - The Real Slim Shady", "Eminem - Lose Yourself", "Eminem - You Don't Know")
    private var currentSongIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playPauseButton = findViewById(R.id.playPauseButton)
        songTitle = findViewById(R.id.songTitle)
        seekBar = findViewById(R.id.seekBar)
        volumeSeekBar = findViewById(R.id.seekBar2)

        val prevButton: Button = findViewById(R.id.prevButton)
        val nextButton: Button = findViewById(R.id.nextButton)
        val shuffleButton: Button = findViewById(R.id.shuffleButton)
        val exitButton: Button = findViewById(R.id.exit)

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.max = maxVolume
        volumeSeekBar.progress = currentVolume

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        initMediaPlayer()

        playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseButton.text = "▶"
            } else {
                mediaPlayer.start()
                playPauseButton.text = "⏸"
            }
        }

        nextButton.setOnClickListener {
            nextSong()
        }

        prevButton.setOnClickListener {
            prevSong()
        }

        shuffleButton.setOnClickListener {
            shuffleSong()
        }

        exitButton.setOnClickListener {
            finish()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer.seekTo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        handler.postDelayed(updateSeekBar, 1000)
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex])
        mediaPlayer.setOnCompletionListener {
            nextSong()
        }
        seekBar.max = mediaPlayer.duration
        updateSongTitle()
    }

    private fun updateSongTitle() {
        songTitle.text = songNames[currentSongIndex]
    }

    private fun nextSong() {
        mediaPlayer.release()
        currentSongIndex = (currentSongIndex + 1) % songs.size
        initMediaPlayer()
        mediaPlayer.start()
        playPauseButton.text = "⏸"
    }

    private fun prevSong() {
        mediaPlayer.release()
        currentSongIndex = if (currentSongIndex - 1 < 0) songs.size - 1 else currentSongIndex - 1
        initMediaPlayer()
        mediaPlayer.start()
        playPauseButton.text = "⏸"
    }

    private fun shuffleSong() {
        mediaPlayer.release()
        currentSongIndex = Random.nextInt(songs.size)
        initMediaPlayer()
        mediaPlayer.start()
        playPauseButton.text = "⏸"
    }

    private val updateSeekBar = object : Runnable {
        override fun run() {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
