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
import android.widget.Toast
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
    private var formType = 0
    private var errorMsg = ""
    private var hintMsg = ""
    private var isError = false

    fun setFormType(type: FormType){
        when(type){
            is FormType.Email -> {
                errorMsg = type.message
                hintMsg = type.hint
                formType = 0
            }
            is FormType.Password -> {
                errorMsg = type.message
                hintMsg = type.hint
                formType = 1
                setFormAsPassword()
            }
        }
        setHintPlaceHolder(hintMsg)
        validateForm(formType)
    }

    private fun setFormAsPassword() {
        editTextForm.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        tilForm.apply {
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
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
        setHintPlaceHolder(hintMsg)
    }

    private fun initParent(){
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        setPadding(16,16,16,16)
        addChildView()
        addView(llParent)
    }

    private fun addChildView() {
        viewUnderline.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1)
        }
        editTextForm.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(resources.getColor(R.color.transparent))
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
        tilForm.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(resources.getColor(R.color.transparent))
            hint = hintMsg
            setPadding(2,2,2,2)
            addView(editTextForm)
            addView(viewUnderline)
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




    private fun validateForm(type: Int) {
        editTextForm.apply {
            addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (type == 0){
                        if (p0.toString().isNotEmpty() && p0?.toString()?.isEmailValid() != true){
                            Toast.makeText(context,"ss",Toast.LENGTH_LONG).show()
                            showErrorMessage()
                        }
                        else hideErrorMessage()
                    }
                    else {
                        if (p0.toString().length < 6 && type == 1 && p0.toString().isNotEmpty()) {
                            showErrorMessage()
                        }
                        else hideErrorMessage()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.toString().isNullOrEmpty())
                        setErrorMessage(null)
                    setTypingForm()
                    if (isError) setErrorForm()
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

    private fun showErrorMessage(){
        isError = true
        setErrorMessage(errorMsg)
        setErrorForm()
    }
    fun dismissKeyboard() {
        val iMgr: InputMethodManager
        iMgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iMgr.hideSoftInputFromWindow(editTextForm.getWindowToken(), 0)
    }
}