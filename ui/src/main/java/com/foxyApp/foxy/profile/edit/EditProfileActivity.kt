package com.foxyApp.foxy.profile.edit

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.foxyApp.data.model.User
import com.foxyApp.foxy.BaseActivity
import com.foxyApp.foxy.FoxyApp
import com.foxyApp.foxy.R
import com.foxyApp.foxy.profile.dagger.ProfileModule
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * EditProfileActivity class.
 */
class EditProfileActivity : BaseActivity(), IEditProfileView {

    private var mCurrentUser: User? = null
    private var mDatePicker: DatePickerDialog.OnDateSetListener? = null
    private val mDatePattern: String = "yyyy-MM-dd"
    private var mCalendar: Calendar? = null

    @BindView(R.id.edit_profile_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.edit_profile_name)
    lateinit var mProfileName: TextView

    @BindView(R.id.edit_profile_username)
    lateinit var mProfileUsername: TextView

    @BindView(R.id.edit_profile_email_value)
    lateinit var mProfileEmail: TextView

    @BindView(R.id.edit_profile_email_verified_image_view)
    lateinit var mEmailVerified: ImageView

    @BindView(R.id.edit_profile_birthday_value)
    lateinit var mProfileBirthDay: TextView

    @Inject
    lateinit var mPresenter: IEditProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        ButterKnife.bind(this)
        // Register this target with dagger.
        FoxyApp.get(this).getAppComponent()?.plus(ProfileModule())?.inject(this)
        mPresenter.attachView(this)
        mPresenter.getProfile(forceNetworkRefresh = true)
        mCalendar = Calendar.getInstance()
        mDatePicker = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            mCalendar!!.set(Calendar.YEAR, year)
            mCalendar!!.set(Calendar.MONTH, monthOfYear)
            mCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            mProfileBirthDay.text = SimpleDateFormat(mDatePattern, Locale.US).format(mCalendar?.time)
        }
    }

    /**
     * Show profile information
     */
    override fun showProfileInformation(user: User) {
        mCurrentUser = user
        mProfileName.text = getString(R.string.placeholder_name, mCurrentUser?.firstName, mCurrentUser?.lastName)
        mProfileUsername.text = mCurrentUser?.username
        mProfileEmail.text = mCurrentUser?.email
        mEmailVerified.setImageResource(if (user.emailVerified!!) R.drawable.ic_verified else R.drawable.ic_not_verified)
        mCalendar?.time = user.birthday
        mProfileBirthDay.text = SimpleDateFormat(mDatePattern, Locale.US).format(user.birthday)
    }

    @OnClick(R.id.edit_profile_birthday_image_view)
    fun editBirthday() {
        DatePickerDialog(this, mDatePicker, mCalendar!!.get(Calendar.YEAR),
                mCalendar!!.get(Calendar.MONTH), mCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
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
        fun getStartingIntent(callingContext: Context): Intent = Intent(callingContext,
                EditProfileActivity::class.java)
    }
}
