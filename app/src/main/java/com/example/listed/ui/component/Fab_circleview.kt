package com.example.listed.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.listed.R

class fab_circleview: View {
    private lateinit var paint: Paint
    private lateinit var path: Path

    private var midPoint:Float=0f
    private var leftCurveStartPointWidth:Float=0f
    private var leftCurveStartPointHeight:Float=0f
    private var leftCurveEndPointWidth:Float=0f
    private var leftCurveEndPointHeight:Float=0f
    private var leftCurveControlPoint1Width:Float=0f
    private var leftCurveControlPoint1Height:Float=0f
    private var leftCurveControlPoint2Width:Float=0f
    private var leftCurveControlPoint2Height:Float=0f

    private var rightCurveStartPointWidth:Float=0f
    private var rightCurveStartPointHeight:Float=0f
    private var rightCurveEndPointWidth:Float=0f
    private var rightCurveEndPointHeight:Float=0f
    private var rightCurveControlPoint1Width:Float=0f
    private var rightCurveControlPoint1Height:Float=0f
    private var rightCurveControlPoint2Width:Float=0f
    private var rightCurveControlPoint2Height:Float=0f
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
    private fun init() {
       paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 0f
            style = Paint.Style.FILL
            setShadowLayer(10f, 0f, 0f, getResources().getColor(R.color.inactive_bottom_nav_color)) // Adding shadow
        }



    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas);
        midPoint=(width/2f).toFloat()
        //start point of left curve
        leftCurveStartPointWidth=midPoint-150f
        leftCurveStartPointHeight=height.toFloat()

        //end point of left curve
        leftCurveEndPointWidth=midPoint
        leftCurveEndPointHeight=height-60f

        //control point of left curves
        leftCurveControlPoint1Width=(leftCurveStartPointWidth+leftCurveEndPointWidth)/2
        leftCurveControlPoint1Height=leftCurveStartPointHeight
        leftCurveControlPoint2Width=leftCurveControlPoint1Width
        leftCurveControlPoint2Height=leftCurveEndPointHeight



        //start point right curve
        rightCurveStartPointWidth=midPoint
        rightCurveStartPointHeight=height-60f


        //end point of right curve
        rightCurveEndPointWidth=midPoint+150f
        rightCurveEndPointHeight=height.toFloat()

        //control point of right curve
        rightCurveControlPoint1Width=(rightCurveStartPointWidth+rightCurveEndPointWidth)/2f
        rightCurveControlPoint1Height=rightCurveStartPointHeight
        rightCurveControlPoint2Width=rightCurveControlPoint1Width
        rightCurveControlPoint2Height=rightCurveEndPointHeight


        val path=Path()
        path.moveTo(leftCurveStartPointWidth,height.toFloat())
        path.cubicTo(leftCurveControlPoint1Width,
            leftCurveControlPoint1Height,
            leftCurveControlPoint2Width,
            leftCurveControlPoint2Height,
            leftCurveEndPointWidth,
            leftCurveEndPointHeight
        )
        path.cubicTo(
            rightCurveControlPoint1Width,
            rightCurveControlPoint1Height,
            rightCurveControlPoint2Width,
            rightCurveControlPoint2Height,
            rightCurveEndPointWidth,
            rightCurveEndPointHeight
        )
        path.close()
        canvas?.drawPath(path,paint)





    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)


    }
}