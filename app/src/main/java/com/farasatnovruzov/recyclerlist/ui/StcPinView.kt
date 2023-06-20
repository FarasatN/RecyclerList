package com.farasatnovruzov.recyclerlist.ui


import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

import android.text.InputFilter
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.farasatnovruzov.recyclerlist.R

class StcPinView(
    private val cnt: Context,
    @Nullable private val attrs: AttributeSet?,
    private val defStyleAttr: Int
) : AppCompatEditText(cnt, attrs, defStyleAttr) {

    constructor(cnt: Context) : this(cnt, null)
    constructor(cnt: Context, @Nullable attrs: AttributeSet?) : this(
        cnt,
        attrs,
        R.attr.StcPinViewStyle
    )

    private var mPaint: Paint

    private var mAnimatorTextPaint = TextPaint()

    private var mIsCursorVisible: Boolean = false

    private val mViewType: Int

    companion object {
        const val VIEW_TYPE_RECTANGLE: Int = 0
        const val DEFAULT_COUNT: Int = 4
        const val VIEW_TYPE_LINE: Int = 1
        const val BLINK: Long = 500
        const val DBG: Boolean = false

        private val HIGHLIGHT_STATES = intArrayOf(android.R.attr.state_selected)
    }

    private var mPinItemCount: Int

    private var pinItemHeight: Float

    private var pinItemWidth: Float

    private var mPinItemSpacing: Int

    private var mPinItemSpacingColor: ColorStateList?

    private var mPinItemRadius: Float

    private var mLineWidth: Float

    private var mLineColor: ColorStateList?
    private var mItemBorderRect = RectF()
    private var mTextRect = Rect()

    private var mCursorColor: Int

    private var mCursorWidth: Int

    private var mItemBackground: Drawable?

    private var mHideLineWhenFilled: Boolean

    private var mCurLineColor = Color.BLACK

    private lateinit var mDefaultAddAnimator: ValueAnimator
    private val mPath: Path = Path()

    private val NO_FILTERS = arrayOfNulls<InputFilter>(0)

    private var mCursorHeight: Float = 0.0f

    init {
        val res = resources
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mPaint.style = Paint.Style.STROKE

        mAnimatorTextPaint.set(paint)

        val theme: Resources.Theme = cnt.theme

        val a: TypedArray =
            theme.obtainStyledAttributes(attrs, R.styleable.StcPinView, defStyleAttr, 0)

        mViewType = a.getInt(R.styleable.StcPinView_viewType, VIEW_TYPE_RECTANGLE)
        mPinItemCount = a.getInt(R.styleable.StcPinView_itemCount, DEFAULT_COUNT)
        pinItemHeight = (a.getDimension(
            R.styleable.StcPinView_itemHeight,
            res.getDimensionPixelSize(R.dimen.pv_pin_view_item_size).toFloat()
        ))
        pinItemWidth = (a.getDimension(
            R.styleable.StcPinView_itemWidth,
            res.getDimensionPixelSize(R.dimen.pv_pin_view_item_size).toFloat()
        ))
        mPinItemSpacing = a.getDimensionPixelSize(
            R.styleable.StcPinView_itemSpacing,
            res.getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing)
        )
        mPinItemRadius = a.getDimension(R.styleable.StcPinView_itemRadius, 0.toFloat())
        mLineWidth = a.getDimension(
            R.styleable.StcPinView_lineWidth,
            res.getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width).toFloat()
        )
        mLineColor = a.getColorStateList(R.styleable.StcPinView_lineColor)
        mPinItemSpacingColor = a.getColorStateList(R.styleable.StcPinView_itemSpacingColor)

        mIsCursorVisible = a.getBoolean(R.styleable.StcPinView_android_cursorVisible, true)

        mCursorColor = a.getColor(R.styleable.StcPinView_cursorColor, currentTextColor)

        mCursorWidth = a.getDimensionPixelSize(
            R.styleable.StcPinView_cursorWidth,
            res.getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width)
        )

        mItemBackground = a.getDrawable(R.styleable.StcPinView_android_itemBackground)
        mHideLineWhenFilled = a.getBoolean(R.styleable.StcPinView_hideLineWhenFilled, false)

        a.recycle()

        mCurLineColor = (mLineColor as ColorStateList).defaultColor

        updateCursorHeight()
        checkItemRadius()
        setMaxLength(mPinItemCount)
        mPaint.strokeWidth = mLineWidth
        setupAnimator()

        super.setCursorVisible(false)
        setTextIsSelectable(false)
    }

    private fun checkItemRadius() {
        if (mViewType == VIEW_TYPE_LINE) {
            val halfOfLineWidth: Float = mLineWidth / 2
            if (mPinItemRadius > halfOfLineWidth) {
                throw IllegalArgumentException("The itemRadius can not be greater than lineWidth when viewType is line")
            }
        } else if (mViewType == VIEW_TYPE_RECTANGLE) {
            val halfOfItemWidth = pinItemWidth.div(2)
            if (mPinItemRadius > halfOfItemWidth) {
                throw IllegalArgumentException("The itemRadius can not be greater than itemWidth")
            }
        }
    }

    private fun setupAnimator() {
        mDefaultAddAnimator = ValueAnimator.ofFloat(0.5f, 1f)
        mDefaultAddAnimator.duration = 150
        mDefaultAddAnimator.interpolator = DecelerateInterpolator()
        mDefaultAddAnimator.addUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            val alpha = (255 * scale).toInt()
            mAnimatorTextPaint.textSize = textSize * scale
            mAnimatorTextPaint.alpha = alpha
            postInvalidate()
        }
    }

    private fun setMaxLength(maxLength: Int) {
        filters = if (maxLength >= 0) {
            arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        } else {
            NO_FILTERS
        }
    }

    private fun updateCursorHeight() {
        val delta = 2 * dpToPx(2f)
        mCursorHeight = if (pinItemHeight.minus(textSize) > delta) textSize + delta else textSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var width: Int
        val height: Int

        val boxHeight = pinItemHeight

        if (widthMode == View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize
        } else {
            val boxesWidth = (mPinItemCount - 1) * mPinItemSpacing + mPinItemCount * pinItemWidth

            width =
                (boxesWidth + ViewCompat.getPaddingEnd(this) + ViewCompat.getPaddingStart(this)).toInt()

            if (mPinItemSpacing == 0) {
                width -= (mPinItemCount.minus(1)) * mLineWidth.toInt()
            }
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize
        } else {
            height = (boxHeight + paddingTop + paddingBottom).toInt()
        }

        setMeasuredDimension(width, height)
    }

    private var isAnimationEnable: Boolean = false

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (start != text?.length) {
            moveSelectionToEnd()
        }

        makeBlink()

        if (isAnimationEnable) {
            val isAdd: Boolean = (lengthAfter - lengthBefore) > 0
            if (isAdd) {
                mDefaultAddAnimator.end()
                mDefaultAddAnimator.start()
            }
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        super.onSelectionChanged(selStart, selEnd)

        if (selEnd != text?.length) {
            moveSelectionToEnd()
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        if (mLineColor == null || (mLineColor as ColorStateList).isStateful) {
            updateColors()
        }
    }

    private fun drawPinView(canvas: Canvas) {
        val highlightIdx = text!!.length
        for (i in 0 until mPinItemCount) {
            val highlight = isFocused && highlightIdx == i
            mPaint.color = if (highlight) getLineColorForState(HIGHLIGHT_STATES) else mCurLineColor

            updateItemRectF(i)
            updateCenterPoint()

            canvas.save()
            if (mViewType == VIEW_TYPE_RECTANGLE) {
                updatePinBoxPath(i)
                canvas.clipPath(mPath)
            }
            drawItemBackground(canvas, highlight)
            canvas.restore()

            if (highlight) {
                drawCursor(canvas)
            }

            if (mViewType == VIEW_TYPE_RECTANGLE) {
                drawPinBox(canvas, i)
            } else if (mViewType == VIEW_TYPE_LINE) {
                drawPinLine(canvas, i)
            }

            if (DBG) {
                drawAnchorLine(canvas)
            }

            if (text!!.length > i) {
                if (isPasswordInputType(inputType)) {
                    drawCircle(canvas, i)
                } else {
                    drawText(canvas, i)
                }
            } else if (!TextUtils.isEmpty(hint) && hint.length == mPinItemCount) {
                drawHint(canvas, i)
            }
        }

        // highlight the goToNextPage item
        if (isFocused && text!!.length != mPinItemCount && mViewType == VIEW_TYPE_RECTANGLE) {
            val index = text!!.length
            updateItemRectF(index)
            updateCenterPoint()
            updatePinBoxPath(index)
            mPaint.color = getLineColorForState(HIGHLIGHT_STATES)
            drawPinBox(canvas, index)
        }
    }

    private fun drawItemBackground(canvas: Canvas, highlight: Boolean) {
        if (mItemBackground == null) {
            return
        }
        val delta = mLineWidth / 2
        val left = Math.round(mItemBorderRect.left - delta)
        val top = Math.round(mItemBorderRect.top - delta)
        val right = Math.round(mItemBorderRect.right + delta)
        val bottom = Math.round(mItemBorderRect.bottom + delta)

        (mItemBackground as Drawable).setBounds(left, top, right, bottom)
        (mItemBackground as Drawable).state = if (highlight) HIGHLIGHT_STATES else drawableState
        (mItemBackground as Drawable).draw(canvas)
    }

    private fun getLineColorForState(states: IntArray): Int {
        return mLineColor?.getColorForState(states, mCurLineColor) ?: mCurLineColor
    }

    private fun updatePinBoxPath(i: Int) {
        var drawRightCorner = false
        var drawLeftCorner = false
        if (mPinItemSpacing != 0) {
            drawRightCorner = true
            drawLeftCorner = drawRightCorner
        } else {
            if (i == 0 && i != mPinItemCount - 1) {
                drawLeftCorner = true
            }
            if (i == mPinItemCount - 1 && i != 0) {
                drawRightCorner = true
            }
        }
        updateRoundRectPath(
            mItemBorderRect,
            mPinItemRadius,
            mPinItemRadius,
            drawLeftCorner,
            drawRightCorner
        )
    }

    private fun drawPinBox(canvas: Canvas, i: Int) {
        if (mHideLineWhenFilled && i < text!!.length) {
            return
        }
        canvas.drawPath(mPath, mPaint)
    }

    private val mItemLineRect = RectF()

    private fun drawPinLine(canvas: Canvas, i: Int) {
        if (mHideLineWhenFilled && i < text!!.length) {
            return
        }
        var l: Boolean
        var r: Boolean
        r = true
        l = r
        if (mPinItemSpacing == 0 && mPinItemCount > 1) {
            when (i) {
                0 -> // draw only left round
                    r = false
                mPinItemCount - 1 -> // draw only right round
                    l = false
                else -> {
                    // draw rect
                    r = false
                    l = r
                }
            }
        }
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = mLineWidth / 10
        val halfLineWidth = mLineWidth / 2
        mItemLineRect.set(
            mItemBorderRect.left - halfLineWidth,
            mItemBorderRect.bottom - halfLineWidth,
            mItemBorderRect.right + halfLineWidth,
            mItemBorderRect.bottom + halfLineWidth
        )

        updateRoundRectPath(mItemLineRect, mPinItemRadius, mPinItemRadius, l, r)
        canvas.drawPath(mPath, mPaint)
    }


    private val mItemCenterPoint = PointF()
    private var drawCursor: Boolean = false

    private fun drawCursor(canvas: Canvas) {
        if (drawCursor) {
            val cx = mItemCenterPoint.x
            val cy = mItemCenterPoint.y
            val y = cy - mCursorHeight.div(2)

            val color = mPaint.color
            val width = mPaint.strokeWidth
            mPaint.color = mCursorColor
            mPaint.strokeWidth = mCursorWidth.toFloat()

            canvas.drawLine(cx, y, cx, y.plus(mCursorHeight), mPaint)

            mPaint.color = color
            mPaint.strokeWidth = width
        }
    }

    private fun updateRoundRectPath(rectF: RectF, rx: Float, ry: Float, l: Boolean, r: Boolean) {
        updateRoundRectPath(rectF, rx, ry, l, r, r, l)
    }

    private fun updateRoundRectPath(
        rectF: RectF, rx: Float, ry: Float,
        tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
    ) {
        mPath.reset()

        val l = rectF.left
        val t = rectF.top
        val r = rectF.right
        val b = rectF.bottom

        val w = r - l
        val h = b - t

        val lw = w - 2 * rx// line width
        val lh = h - 2 * ry// line height

        mPath.moveTo(l, t + ry)

        if (tl) {
            mPath.rQuadTo(0F, -ry, rx, -ry)// top-left corner
        } else {
            mPath.rLineTo(0F, -ry)
            mPath.rLineTo(rx, 0F)
        }

        mPath.rLineTo(lw, 0F)

        if (tr) {
            mPath.rQuadTo(rx, 0F, rx, ry)// top-right corner
        } else {
            mPath.rLineTo(rx, 0F)
            mPath.rLineTo(0F, ry)
        }

        mPath.rLineTo(0F, lh)

        if (br) {
            mPath.rQuadTo(0F, ry, -rx, ry)// bottom-right corner
        } else {
            mPath.rLineTo(0F, ry)
            mPath.rLineTo(-rx, 0F)
        }

        mPath.rLineTo(-lw, 0F)

        if (bl) {
            mPath.rQuadTo(-rx, 0F, -rx, -ry)// bottom-left corner
        } else {
            mPath.rLineTo(-rx, 0F)
            mPath.rLineTo(0F, -ry)
        }

        mPath.rLineTo(0F, -lh)

        mPath.close()
    }


    private fun updateItemRectF(i: Int) {
        val halfLineWidth = mLineWidth / 2
        var left =
            scrollX + ViewCompat.getPaddingStart(this) + i * (mPinItemSpacing + pinItemWidth) + halfLineWidth
        if (mPinItemSpacing == 0 && i > 0) {
            left -= mLineWidth * i
        }
        val right = left + pinItemWidth - mLineWidth
        val top = scrollY.toFloat() + paddingTop.toFloat() + halfLineWidth
        val bottom = top + pinItemHeight - mLineWidth

        mItemBorderRect.set(left, top, right, bottom)
    }

    private fun drawText(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex(i)
        // 1, Rect(4, -39, 20, 0)
        // 您, Rect(2, -47, 51, 3)
        // *, Rect(0, -39, 23, -16)
        // =, Rect(4, -26, 26, -10)
        // -, Rect(1, -19, 14, -14)
        // +, Rect(2, -32, 29, -3)

        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        drawTextAtBox(canvas, paint, text.toString(), i)
    }


    private fun drawTextAtBox(canvas: Canvas, paint: Paint, text: CharSequence, charAt: Int) {
        paint.getTextBounds(text.toString(), charAt, charAt + 1, mTextRect)
        val cx = mItemCenterPoint.x
        val cy = mItemCenterPoint.y
        val x = cx - Math.abs(mTextRect.width()) / 2 - mTextRect.left
        val y = cy + Math.abs(mTextRect.height()) / 2 - mTextRect.bottom// always center vertical
        canvas.drawText(text, charAt, charAt + 1, x, y, paint)
    }

    private fun drawCircle(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex(i)

        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)
        (mItemBackground as Drawable).draw(canvas)

        val cx = mItemCenterPoint.x
        val cy = mItemCenterPoint.y
        canvas.drawCircle(cx, cy, paint.textSize / 2, paint)
    }

    private fun getPaintByIndex(i: Int): Paint {
        return if (isAnimationEnable && i == text!!.length - 1) {
            mAnimatorTextPaint.color = paint.color
            mAnimatorTextPaint
        } else {
            paint
        }
    }

    private fun drawAnchorLine(canvas: Canvas) {
        var cx = mItemCenterPoint.x
        var cy = mItemCenterPoint.y
        mPaint.strokeWidth = 1F
        cx -= mPaint.strokeWidth / 2
        cy -= mPaint.strokeWidth / 2

        mPath.reset()
        mPath.moveTo(cx, mItemBorderRect.top)
        mPath.lineTo(cx, mItemBorderRect.top + Math.abs(mItemBorderRect.height()))
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(mItemBorderRect.left, cy)
        mPath.lineTo(mItemBorderRect.left + Math.abs(mItemBorderRect.width()), cy)
        canvas.drawPath(mPath, mPaint)

        mPath.reset()

        mPaint.strokeWidth = mLineWidth
    }

    private fun updateColors() {
        var inval = false

        val color: Int
        if (mLineColor != null) {
            color = (mLineColor as ColorStateList).getColorForState(drawableState, 0)
        } else {
            color = currentTextColor
        }

        if (color != mCurLineColor) {
            mCurLineColor = color
            inval = true
        }

        if (inval) {
            invalidate()
        }
    }

    private fun updateCenterPoint() {
        val cx = mItemBorderRect.left + Math.abs(mItemBorderRect.width()) / 2
        val cy = mItemBorderRect.top + Math.abs(mItemBorderRect.height()) / 2
        mItemCenterPoint.set(cx, cy)
    }

    private fun drawHint(canvas: Canvas, i: Int) {
        val paint = getPaintByIndex(i)
        paint.color = currentHintTextColor
        drawTextAtBox(canvas, paint, hint, i)
    }

    private fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                || variation == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD
                || variation == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    override fun getDefaultMovementMethod(): MovementMethod {
        return DefaultMovementMethod.getInstance()
    }

    fun setLineColor(@ColorInt color: Int) {
        mLineColor = ColorStateList.valueOf(color)
        updateColors()
    }

    fun getLineColors(): ColorStateList? {
        return mLineColor
    }

    fun getCurrentLineColor(): Int {
        return mCurLineColor
    }

    fun setLineWidth(@Px borderWidth: Int) {
        mLineWidth = borderWidth.toFloat()
        checkItemRadius()
        requestLayout()
    }

    fun getLineWidth(): Int {
        return mLineWidth.toInt()
    }

    fun setItemCount(count: Int) {
        mPinItemCount = count
        setMaxLength(count)
        requestLayout()
    }

    fun getItemCount(): Int {
        return mPinItemCount
    }

    fun setItemRadius(@Px itemRadius: Int) {
        mPinItemRadius = itemRadius.toFloat()
        checkItemRadius()
        requestLayout()
    }

    fun getItemRadius(): Int {
        return mPinItemRadius.toInt()
    }

    fun setItemSpacing(@Px itemSpacing: Int) {
        mPinItemSpacing = itemSpacing
        requestLayout()
    }

    fun getItemSpacing(): Int {
        return mPinItemSpacing
    }

    fun setItemHeight(@Px itemHeight: Int) {
        pinItemHeight = itemHeight.toFloat()
        updateCursorHeight()
        requestLayout()
    }

    fun getItemHeight(): Int {
        return pinItemHeight.toInt()
    }

    fun setItemWidth(@Px itemWidth: Int) {
        pinItemWidth = itemWidth.toFloat()
        checkItemRadius()
        requestLayout()
    }

    fun getItemWidth(): Int {
        return pinItemWidth.toInt()
    }

    fun setAnimationEnable(enable: Boolean) {
        isAnimationEnable = enable
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        updateCursorHeight()
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        updateCursorHeight()
    }

    private var mItemBackgroundResource: Int = 0

    fun setItemBackgroundResources(@DrawableRes resId: Int) {
        if (resId != 0 && mItemBackgroundResource != resId) {
            return
        }
        mItemBackground = ResourcesCompat.getDrawable(resources, resId, context.theme)
        setItemBackground(mItemBackground)
        mItemBackgroundResource = resId
    }

    fun setItemBackgroundColor(@ColorInt color: Int) {
        if (mItemBackground is ColorDrawable) {
            (mItemBackground?.mutate() as ColorDrawable).color = color
            mItemBackgroundResource = 0
        } else {
            setItemBackground(ColorDrawable(color))
        }
    }

    private fun setItemBackground(background: Drawable?) {
        mItemBackgroundResource = 0
        mItemBackground = background
        invalidate()
    }

    fun setCursorWidth(@Px width: Int) {
        mCursorWidth = width
        if (mIsCursorVisible) {
            invalidateCursor(true)
        }
    }

    fun setCursorColor(@ColorInt color: Int) {
        mCursorColor = color
        if (mIsCursorVisible) {
            invalidateCursor(true)
        }
    }

    fun getCursorColor(): Int {
        return mCursorColor
    }

    override fun setCursorVisible(visible: Boolean) {
        if (mIsCursorVisible != visible) {
            mIsCursorVisible = visible
            invalidateCursor(mIsCursorVisible)
            makeBlink()
        }
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        when (screenState) {
            View.SCREEN_STATE_ON -> resumeBlink()
            View.SCREEN_STATE_OFF -> suspendBlink()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        suspendBlink()
    }

    private fun shouldBlink(): Boolean {
        return mIsCursorVisible && isFocused
    }

    private var mBlink: Blink? = null

    private fun suspendBlink() {
        mBlink?.cancel()
        invalidateCursor(false)
    }

    private fun resumeBlink() {
        mBlink?.uncancel()
        makeBlink()
    }

    private fun dpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

    private inner class Blink : Runnable {
        private var mCancelled: Boolean = false

        override fun run() {
            if (mCancelled) {
                return
            }

            removeCallbacks(this)

            if (shouldBlink()) {
                invalidateCursor(!drawCursor)
                postDelayed(this, BLINK)
            }
        }

        fun cancel() {
            if (!mCancelled) {
                removeCallbacks(this)
                mCancelled = true
            }
        }

        fun uncancel() {
            mCancelled = false
        }
    }

    private fun invalidateCursor(showCursor: Boolean) {
        if (drawCursor != showCursor) {
            drawCursor = showCursor
            invalidate()
        }
    }

    override fun isCursorVisible(): Boolean {
        return mIsCursorVisible
    }

    fun getCursorWidth(): Int {
        return mCursorWidth
    }

    fun setHideLineWhenFilled(hideLineWhenFilled: Boolean) {
        this.mHideLineWhenFilled = hideLineWhenFilled
    }

    fun setLineColor(colors: ColorStateList?) {
        if (colors == null) {
            throw NullPointerException()
        }

        mLineColor = colors
        updateColors()
    }

    private fun updatePaints() {
        mPaint.color = mCurLineColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mLineWidth
        paint.color = currentTextColor
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        updatePaints()
        drawPinView(canvas)

        canvas.restore()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        if (focused) {
            moveSelectionToEnd()
            makeBlink()
        }
    }

    private fun moveSelectionToEnd() {
        setSelection(text!!.length)
    }


    private fun makeBlink() {
        if (shouldBlink()) {
            mBlink = Blink()
            removeCallbacks(mBlink)
            drawCursor = false
            postDelayed(mBlink, BLINK)
        } else {
            removeCallbacks(mBlink)
        }
    }


}