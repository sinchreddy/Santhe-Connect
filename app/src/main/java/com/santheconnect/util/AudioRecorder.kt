package com.santheconnect.util


import android.media.MediaRecorder

class AudioRecorder {

    private var recorder: MediaRecorder? = null

    fun start(filePath: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(filePath)
            prepare()
            start()
        }
    }

    fun stop() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}