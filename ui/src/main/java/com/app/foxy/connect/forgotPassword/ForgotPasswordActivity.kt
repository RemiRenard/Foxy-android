package com.app.foxy.connect.forgotPassword

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.foxy.BaseActivity
import com.app.foxy.FoxyApp
import com.app.foxy.R
import com.app.foxy.connect.dagger.ConnectModule
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity(), IForgotPasswordView {

    @BindView(R.id.forgot_pw_email)
    lateinit var mEmail: EditText

    @BindView(R.id.forgot_pw_send_mail_button)
    lateinit var mButton: Button

    @BindView(R.id.forgot_pw_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.forgot_pw_app_name)
    lateinit var mAppName: TextView

    @BindView(R.id.forgot_pw_text)
    lateinit var mText: TextView

    @Inject
    lateinit var mPresenter: IForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ConnectModule())?.inject(this)
        mPresenter.attachView(this)
        setFonts()
    }

    /**
     * Set fonts.
     */
    private fun setFonts() {
        val appTitle = Typeface.createFromAsset(this.assets, "fonts/appNameFont.otf")
        val standard = Typeface.createFromAsset(this.assets, "fonts/SourceSansPro-Regular.ttf")
        mAppName.typeface = appTitle
        mText.typeface = standard
    }

    @OnClick(R.id.forgot_pw_back_arrow)
    fun back() {
        onBackPressed()
    }

    @OnClick(R.id.forgot_pw_send_mail_button)
    fun forgotPassword() {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.text).matches()) {
            mPresenter.forgotPassword(mEmail.text.toString())
        } else {
            Toast.makeText(this, R.string.Please_enter_a_valid_email, Toast.LENGTH_SHORT).show()
        }
    }

    override fun resetPasswordComplete() {
        onBackPressed()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun enableButton(isEnable: Boolean) {
        mButton.isEnabled = isEnable
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.view_fade_in, R.anim.view_fade_out)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    companion object {

        /**
         * Return the intent to start this activity.
         * @param callingContext Context
         * @return Intent
         */
        fun getStartingIntent(callingContext: Context): Intent =
                Intent(callingContext, ForgotPasswordActivity::class.java)
    }
}
