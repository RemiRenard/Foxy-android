package com.foxyApp.foxy.connect.fragment

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.connect.IConnectPresenter
import com.foxyApp.foxy.connect.IConnectView
import com.foxyApp.foxy.connect.dagger.ConnectModule
import com.foxyApp.foxy.event_bus.LoginViewClickedEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignupFragment : Fragment(), IConnectView {

    private var mView: View? = null
    private var mDatePicker: DatePickerDialog.OnDateSetListener? = null
    private val mDatePattern: String = "yyyy-MM-dd"
    private var mCalendar: Calendar? = null

    @BindView(R.id.signup_email)
    lateinit var mEmail: EditText

    @BindView(R.id.signup_first_name)
    lateinit var mFirstName: EditText

    @BindView(R.id.signup_surname)
    lateinit var mLastName: EditText

    @BindView(R.id.signup_username)
    lateinit var mUsername: EditText

    @BindView(R.id.signup_password)
    lateinit var mPassword: EditText

    @BindView(R.id.signup_confirm_password)
    lateinit var mConfirmPassword: EditText

    @BindView(R.id.signup_birthday)
    lateinit var mBirthday: TextView

    @BindView(R.id.signup_title)
    lateinit var mTitle: TextView

    @BindView(R.id.signup_register_button)
    lateinit var mRegister: Button

    @BindView(R.id.signup_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @Inject
    lateinit var mPresenter: IConnectPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_signup, container, false)
        ButterKnife.bind(this, mView!!)
        // Register this target with dagger.
        FoxyApp.get(context).getAppComponent()?.plus(ConnectModule())?.inject(this)
        setFonts()
        mCalendar = Calendar.getInstance()
        mDatePicker = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            mCalendar!!.set(Calendar.YEAR, year)
            mCalendar!!.set(Calendar.MONTH, monthOfYear)
            mCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            mBirthday.text = SimpleDateFormat(mDatePattern, Locale.US).format(mCalendar?.time)
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        mPresenter.attachView(this)
    }

    override fun onPause() {
        mPresenter.detachView()
        super.onPause()
    }

    @OnClick(R.id.signup_birthday)
    fun displayDatePicker() {
        DatePickerDialog(context, mDatePicker, mCalendar!!.get(Calendar.YEAR),
                mCalendar!!.get(Calendar.MONTH), mCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
    }

    @OnClick(R.id.signup_register_button)
    fun createAccount() {
        // Check the format.
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.text).matches()
                && !TextUtils.isEmpty(mPassword.text)
                && !TextUtils.isEmpty(mFirstName.text)
                && !TextUtils.isEmpty(mLastName.text)
                && !TextUtils.isEmpty(mUsername.text)
                && !TextUtils.isEmpty(mBirthday.text)
                && TextUtils.equals(mPassword.text, mConfirmPassword.text)) {
            enableButtons(false)
            mPresenter.createAccount(mEmail.text.toString(), mPassword.text.toString(), mFirstName.text.toString(),
                    mLastName.text.toString(), mUsername.text.toString(), mCalendar!!.time)
        } else {
            Toast.makeText(context, R.string.error_form, Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.signup_login_button)
    fun loginClicked() {
        EventBus.getDefault().post(LoginViewClickedEvent())
    }

    /**
     * Set fonts.
     */
    private fun setFonts() {
        val standard = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.ttf")
        mTitle.typeface = standard
        mConfirmPassword.typeface = standard
        mEmail.typeface = standard
        mPassword.typeface = standard
        mFirstName.typeface = standard
        mLastName.typeface = standard
        mUsername.typeface = standard
        mBirthday.typeface = standard
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun enableButtons(isEnable: Boolean) {
        mRegister.isEnabled = isEnable
    }
}
