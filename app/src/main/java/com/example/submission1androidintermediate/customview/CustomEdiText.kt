package com.example.submission1androidintermediate.customview

import android.app.Activity
import android.content.Context
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
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.helper.FormType
import com.example.submission1androidintermediate.helper.StringHelper.isEmailValid
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomEdiText : RelativeLayout {
    private val tilForm by lazy { TextInputLayout(context,null,R.style.InputFormCustomTheme) }
    private val editTextForm by lazy { TextInputEditText(context) }
    private val llParent by lazy { LinearLayout(context) }
    private val viewUnderline by lazy { View(context) }
    private var isError = false
    var errorTextListener: ((Boolean) -> Unit)? = null

    fun setFormType(type: FormType){
        when(type){
            is FormType.Email -> {
                setFormAsEmail()
                validateForm(type)
                setHintPlaceHolder(type.hint)
            }
            is FormType.Password -> {
                setFormAsPassword()
                validateForm(type)
                setHintPlaceHolder(type.hint)
            }
        }
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

    private fun setFormAsEmail(){
        editTextForm.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        tilForm.apply {
            endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        }
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

    fun setHintPlaceHolder(hint: String){
        tilForm.hint = hint
    }

    private fun initView() {
        initParent()
    }

    private fun initParent(){
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        addChildView()
        addView(llParent)
    }

    private fun addChildView() {
        viewUnderline.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,4)
        }
        editTextForm.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            setBackgroundColor(resources.getColor(R.color.transparent))
            typeface = ResourcesCompat.getFont(context,R.font.urbanist_regular)
            inputType = InputType.TYPE_CLASS_TEXT
            setTextColor(ResourcesCompat.getColor(resources,R.color.grey,null))
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
        tilForm.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(resources.getColor(R.color.transparent))
            addView(editTextForm)
            addView(viewUnderline)
            isErrorEnabled = true
        }
        llParent.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            addView(tilForm)
        }
        setDefaultForm()
    }

    private fun setDefaultForm(){
        viewUnderline.setBackgroundColor(resources.getColor(R.color.underlineFormDefaultColor))
    }
    private fun setTypingForm() {
        viewUnderline.setBackgroundColor(resources.getColor(R.color.underlineFormBlueColor))
    }
    private fun setErrorForm() {
        viewUnderline.setBackgroundColor(resources.getColor(R.color.underlineFormRedColor))
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (event.keyCode == KeyEvent.KEYCODE_BACK && imm.hideSoftInputFromWindow(
                editTextForm.getWindowToken(),
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
            addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    when (type){
                        is FormType.Email -> {
                            if (p0.toString().isNotEmpty() && p0?.toString()?.isEmailValid() != true){
                                showErrorMessage(type.message)
                            }
                            else hideErrorMessage()
                        }
                        is FormType.Password -> {
                            if (p0.toString().length < 6 && p0.toString().isNotEmpty()) {
                                showErrorMessage(type.message)
                            }
                            else hideErrorMessage()
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.toString().isNullOrEmpty())
                        hideErrorMessage()
                    setTypingForm()
                    if (isError) setErrorForm()
                    errorTextListener?.invoke(isError)
                }
            })
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    setTypingForm()
                    if (isError) setErrorForm()
                }
                else {
                    if (isError) setErrorForm()
                    else setDefaultForm()
                }

            }
            setOnEditorActionListener((
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



    private fun hideErrorMessage(){
        isError = false
        setErrorMessage(null)
    }

    private fun showErrorMessage(message: String){
        isError = true
        setErrorMessage(message)
        setErrorForm()
    }
    fun dismissKeyboard() {
        val iMgr: InputMethodManager
        iMgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMgr.hideSoftInputFromWindow(editTextForm.getWindowToken(), 0)
    }

    interface ErrorTextListener{
        fun onErrorText()
    }
}