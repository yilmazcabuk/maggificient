package com.valyriapps.maggificient

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import java.io.File
import kotlin.math.sqrt

// Dokunma geri bildirimi olayını düzenler
fun View.performHapticContextClick() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}

// Multi touch olayını düzenler
fun MotionEvent.calculateFingerSpacing(): Float {
    val x = getX(0) - getX(1)
    val y = getY(0) - getY(1)

    return sqrt((x * x + y * y).toDouble()).toFloat()
}

// Oluşturulan dosyayı girilen yola bağlı olarak hafızaya kaydeder
fun Context.saveFileExternally(videoUrl: String) {
    val dir = Environment.getExternalStorageDirectory().path
    val folder = getString(R.string.app_name)
    val filename = videoUrl.split("/").last()
    val destinationPath = File("$dir/$folder/$filename")

    if (!destinationPath.exists()) {
        destinationPath.mkdir()
    }

    File(videoUrl).copyTo(
        target = destinationPath,
        overwrite = true,
        bufferSize = DEFAULT_BUFFER_SIZE
    )
}

// Girilen paket adına ait Play Store sayfasını açar
fun Context.launchPlayStore(packageName: String) {
    val url = "https://play.google.com/store/apps/details?id=$packageName"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

// Girilen URL'e ait web sitesini açar
fun Context.launchWebsite(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)

    intent.data = Uri.parse(url)
    startActivity(intent)
}

// Uygulama paylaşım ekranındaki mesajı ayarlar
fun Context.shareAppIntent(message: String) {
    val intent = Intent(Intent.ACTION_SEND)

    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, message)

    startActivity(Intent.createChooser(intent, getString(R.string.title_share_app)))
}