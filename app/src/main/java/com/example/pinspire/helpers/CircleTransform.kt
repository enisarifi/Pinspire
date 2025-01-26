package com.example.pinspire.helpers

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.squareup.picasso.Transformation

class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap? {
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)
        if (squared != source) {
            source.recycle()
        }

        val output = source.config?.let { Bitmap.createBitmap(size, size, it) }
        val canvas = output?.let { Canvas(it) }
        val paint = Paint()
        val shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        paint.shader = shader
        val radius = size / 2f
        if (canvas != null) {
            canvas.drawCircle(radius, radius, radius, paint)
        }

        squared.recycle()
        return output
    }

    override fun key(): String {
        return "circle"
    }
}
