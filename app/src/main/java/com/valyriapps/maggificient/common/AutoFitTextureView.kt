package com.valyriapps.maggificient.common

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView

class AutoFitTextureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextureView(context, attrs, defStyle) {

    private var ratioWidth = 0
    private var ratioHeight = 0

    // Mevcut görünüm için en-boy oranını ayarlar
    fun setAspectRatio(width: Int, height: Int) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }
        this.ratioWidth = width
        this.ratioHeight = height

        this.requestLayout()
    }

    // Görünümün ölçüsünü alır
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (this.ratioWidth == 0 || this.ratioHeight == 0) {
            this.setMeasuredDimension(width, height)
        } else {
            if (this.width < ((this.height * this.ratioWidth) / this.ratioHeight)) {
                this.setMeasuredDimension(width, (width * this.ratioHeight) / this.ratioWidth)
            } else {
                this.setMeasuredDimension((height * this.ratioWidth) / this.ratioHeight, height)
            }
        }
    }
}