package com.example.submission1androidintermediate.customview

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import com.example.domain.model.validator.Validator
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.customview.helper.restoreChildViewStates
import com.example.submission1androidintermediate.customview.helper.saveChildViewStates
import com.example.submission1androidintermediate.helper.FormType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

class CustomEdiText : RelativeLayout {
    private val tilForm = TextInputLayout(context, null, R.style.InputFormCustomTheme)
    private val editTextForm = TextInputEditText(context)
    private val llParent = LinearLayout(context)
    private val viewUnderline = View(context)
    private var isError = false
    var errorTextListener: ((Boolean) -> Unit)? = null
    private var validator: Validator? = null

    fun setFormType(type: FormType) {
        when (type) {
            is FormType.Username<*> -> {
                validator = type.data
                setFormAsUsername()
                validateForm(type)
                setHintPlaceHolder(type.data.getHintMessage())
            }
            is FormType.Email<*> -> {
                validator = type.data
                setFormAsEmail()
                validateForm(type)
                setHintPlaceHolder(type.data.getHintMessage())
            }
            is FormType.Password<*> -> {
                validator = type.data
                setFormAsPassword()
                validateForm(type)
                setHintPlaceHolder(type.data.getHintMessage())
            }
        }
    }


    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val childrenState = newState.getSparseParcelableArray<Parcelable>(SPARSE_STATE_KEY)
            childrenState?.let { restoreChildViewStates(it) }
            newState = newState.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }

    fun getText(): String {
        return editTextForm.text.toString()
    }

    private fun setFormAsPassword() {
        editTextForm.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        tilForm.apply {
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }

    }

    private fun setFormAsEmail() {
        editTextForm.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        tilForm.apply {
            endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
    }

    private fun setFormAsUsername() {
        tilForm.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
    }

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
        initView()
    }

    fun setHintPlaceHolder(hint: String) {
        tilForm.hint = hint
    }

    private fun initView() {
        initParent()
    }

    private fun initParent() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addChildView()
        addView(llParent)
    }

    private fun addChildView() {
        viewUnderline.apply {
            id = generateViewId()
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4)
        }
        editTextForm.apply {
            val mId = generateViewId()
            id = mId
            Timber.d(id.toString())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(ResourcesCompat.getColor(resources, R.color.transparent, null))
            typeface = ResourcesCompat.getFont(context, R.font.urbanist_regular)
            inputType = InputType.TYPE_CLASS_TEXT
            setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
        tilForm.apply {
            id = generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(ResourcesCompat.getColor(resources, R.color.transparent, null))
            addView(editTextForm)
            addView(viewUnderline)
            isErrorEnabled = true
        }
        llParent.apply {
            id = generateViewId()
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            addView(tilForm)
        }
        setDefaultForm()
    }

    private fun setDefaultForm() {
        viewUnderline.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.underlineFormDefaultColor,
                null
            )
        )

    }

    private fun setTypingForm() {
        viewUnderline.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.underlineFormBlueColor,
                null
            )
        )
    }

    private fun setErrorForm() {
        viewUnderline.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.underlineFormRedColor,
                null
            )
        )
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event.keyCode == KeyEvent.KEYCODE_BACK && imm.hideSoftInputFromWindow(
                editTextForm.windowToken,
                0
            )
        ) {
            editTextForm.clearFocus()
            setDefaultForm()
            if (!isError) {
                setDefaultForm()
            } else {
                setErrorForm()
            }
            return true
        }
        return super.dispatchKeyEventPreIme(event)
    }


    private fun validateForm(type: FormType) {
        editTextForm.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Do nothing
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    when (type) {
                        is FormType.Email<*> -> {
                            if (type.data.isValid(p0.toString())) {
                                hideErrorMessage()
                            } else showErrorMessage(type.data.getErrorMessage())
                        }
                        is FormType.Password<*> -> {
                            if (type.data.isValid(p0.toString())) {
                                hideErrorMessage()
                            } else showErrorMessage(type.data.getErrorMessage())
                        }
                        is FormType.Username<*> -> {
                            if (type.data.isValid(p0.toString())) {
                                hideErrorMessage()
                            } else showErrorMessage(type.data.getErrorMessage())
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    setTypingForm()
                    if (isError) setErrorForm()
                    errorTextListener?.invoke(isError)
                    if (p0?.toString().isNullOrEmpty()) {
                        hideErrorMessage()
                        errorTextListener?.invoke(true)
                    }
                }
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    setTypingForm()
                    if (isError) setErrorForm()
                } else {
                    if (isError) setErrorForm()
                    else setDefaultForm()
                }

            }
            setOnEditorActionListener(
                (
                        { _, actionId: Int, _: KeyEvent? ->
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                editTextForm.clearFocus()
                                dismissKeyboard()
                                setDefaultForm()
                                if (!isError) {
                                    setDefaultForm()
                                } else {
                                    setErrorForm()
                                }
                            }
                            false
                        })
            )
        }
    }

    private fun setErrorMessage(msg: String?) {
        tilForm.apply {
            error = msg
        }
    }


    private fun hideErrorMessage() {
        isError = false
        setErrorMessage(null)
    }

    private fun showErrorMessage(message: String) {
        isError = true
        setErrorMessage(message)
        setErrorForm()
    }

    private fun dismissKeyboard() {
        val iMgr: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMgr.hideSoftInputFromWindow(editTextForm.windowToken, 0)
    }
}