package com.app.foxy.notification.recordVoice

import com.app.foxy.IPresenter
import java.io.File

/**
 * Interface of the "record voice" presenter.
 */
interface IRecordVoicePresenter : IPresenter<IRecordVoiceView> {

    fun saveTmpNotification(message: String, audioFile: File?)
}
