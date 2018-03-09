package com.foxyApp.foxy.connect.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.connect.IConnectPresenter
import com.foxyApp.foxy.connect.IConnectView
import com.foxyApp.foxy.connect.dagger.ConnectModule
import com.foxyApp.foxy.connect.forgotPassword.ForgotPasswordActivity
import com.foxyApp.foxy.eventBus.CreateAccountViewClickedEvent
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

class LoginFragment : Fragment(), IConnectView {

    private var mCallBackManager: CallbackManager? = null
    private var mView: View? = null

    @BindView(R.id.login_email)
    lateinit var mEmail: EditText

    @BindView(R.id.login_password)
    lateinit var mPassword: EditText

    @BindView(R.id.login_app_name)
    lateinit var mAppName: TextView

    @BindView(R.id.login_forgot_password)
    lateinit var mForgotPassword: TextView

    @BindView(R.id.login_login_facebook)
    lateinit var mDefaultFbBtn: LoginButton

    @BindView(R.id.login_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.login_login_button)
    lateinit var mLoginButton: Button

    @Inject
    lateinit var mPresenter: IConnectPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context!!).getAppComponent()?.plus(ConnectModule())?.inject(this)
        mPresenter.attachView(this)
        setFonts()
        initFacebookLogin()
        return mView
    }

    private fun initFacebookLogin() {
        mDefaultFbBtn.fragment = this
        mCallBackManager = CallbackManager.Factory.create()
        mDefaultFbBtn.registerCallback(mCallBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                mPresenter.loginFacebook(loginResult)
            }

            override fun onCancel() {
                Log.d(javaClass.simpleName, "Login facebook cancelled")
            }

            override fun onError(error: FacebookException) {
                error.printStackTrace()
            }
        })
    }

    @OnClick(R.id.login_connect_facebook)
    fun loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("email", "public_profile", "user_friends"))
    }

    @OnClick(R.id.login_login_button)
    fun loginClicked() {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.text).matches() && !TextUtils.isEmpty(mPassword.text)) {
            enableButtons(false)
            mPresenter.login(mEmail.text.toString(), mPassword.text.toString())
        } else {
            Toast.makeText(context, R.string.error_form, Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.login_forgot_password)
    fun forgotPassword() {
        startActivity(ForgotPasswordActivity.getStartingIntent(context!!).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    @OnClick(R.id.login_create_account_button)
    fun createAccount() {
        EventBus.getDefault().post(CreateAccountViewClickedEvent())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallBackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun enableButtons(isEnable: Boolean) {
        mLoginButton.isEnabled = isEnable
    }

    /**
     * Set fonts.
     */
    private fun setFonts() {
        val appTitle = Typeface.createFromAsset(context!!.assets, "fonts/appNameFont.otf")
        val standard = Typeface.createFromAsset(context!!.assets, "fonts/SourceSansPro-Regular.ttf")
        mAppName.typeface = appTitle
        mForgotPassword.typeface = standard
        mEmail.typeface = standard
        mPassword.typeface = standard
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }
}
