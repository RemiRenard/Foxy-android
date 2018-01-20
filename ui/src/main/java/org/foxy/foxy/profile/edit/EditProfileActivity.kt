package org.foxy.foxy.profile.edit

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import org.foxy.data.Constants
import org.foxy.data.model.User
import org.foxy.foxy.BaseActivity
import org.foxy.foxy.BuildConfig
import org.foxy.foxy.FoxyApp
import org.foxy.foxy.R
import org.foxy.foxy.event_bus.CameraPermsResultEvent
import org.foxy.foxy.event_bus.WriteStoragePermResultEvent
import org.foxy.foxy.profile.dagger.ProfileModule
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * EditProfileActivity class.
 */
class EditProfileActivity : BaseActivity(), IEditProfileView {

    private var mBottomSheetDialog: BottomSheetDialog? = null
    private val ACTIVITY_RESULT_LIBRARY = 1
    private val ACTIVITY_RESULT_CAMERA = 2
    private var mCameraFile: File? = null
    private var mCurrentUser: User? = null
    private var mDatePicker: DatePickerDialog.OnDateSetListener? = null
    private val mDatePattern: String = "yyyy-MM-dd"
    private var mCalendar: Calendar? = null

    @BindView(R.id.edit_profile_progress_bar)
    lateinit var mProgressBar: ProgressBar

    @BindView(R.id.edit_profile_avatar)
    lateinit var mProfileAvatar: ImageView

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
        if (mCurrentUser!!.avatar != null) {
            Glide.with(this).load(mCurrentUser?.avatar).apply(RequestOptions()
                    .signature(ObjectKey(System.currentTimeMillis()))
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_circle_gray))
                    .into(mProfileAvatar)
        }
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

    @OnClick(R.id.edit_profile_avatar)
    fun editAvatar() {
        openBottomSheetDialog()
    }

    @OnClick(R.id.toolbar_back)
    fun back() {
        onBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWriteStoragePermGrantedEvent(event: WriteStoragePermResultEvent) {
        if (event.isGranted) openLibrary() else displaySnackBar()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCameraPermsResultEvent(event: CameraPermsResultEvent) {
        if (event.isGranted) openCamera() else displaySnackBar()
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_PERMISSION_WRITE_STORAGE) {
            EventBus.getDefault().post(WriteStoragePermResultEvent(grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED))
        }
        if (requestCode == Constants.REQUEST_PERMISSION_GROUP_CAMERA) {
            EventBus.getDefault().post(CameraPermsResultEvent(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED))
        }
    }

    /**
     * This method brings up the Bottom Sheet Dialog for picture Selection.
     */
    private fun openBottomSheetDialog() {
        mBottomSheetDialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet_edit_profile, null)
        val btnAddFromCamera = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_camera)
        val btnAddFromLibrary = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_library)
        val btnCancel = view.findViewById<Button>(R.id.dialog_bottom_edit_profile_cancel)
        mBottomSheetDialog!!.setContentView(view)
        mBottomSheetDialog!!.show()
        btnAddFromCamera.setOnClickListener({
            openCamera()
            mBottomSheetDialog!!.dismiss()
        })
        btnAddFromLibrary.setOnClickListener({
            openLibrary()
            mBottomSheetDialog!!.dismiss()
        })
        btnCancel.setOnClickListener({ mBottomSheetDialog!!.dismiss() })
    }

    /**
     * Open the library of the phone
     */
    private fun openLibrary() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_PERMISSION_WRITE_STORAGE)
        } else {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, ACTIVITY_RESULT_LIBRARY)
        }
    }

    /**
     * Open the native camera of the phone
     */
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            mCameraFile = mPresenter.setUpPhotoFile()
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile))
            if (Build.VERSION.SDK_INT >= 24) {
                startActivityForResult(Intent.createChooser(takePictureIntent, ""), ACTIVITY_RESULT_CAMERA)
            } else {
                startActivityForResult(takePictureIntent, ACTIVITY_RESULT_CAMERA)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.REQUEST_PERMISSION_GROUP_CAMERA)
        }
    }

    /**
     * Display SnackBar if permissions is denied.
     */
    private fun displaySnackBar() {
        showSnackbar(R.string.permission_denied_explanation, R.string.settings, View.OnClickListener {
            // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })
    }

    /**
     * Shows a [Snackbar].
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_LONG)
                .setAction(getString(actionStringId), listener).show()
    }

    /**
     * Compress the picture.
     *
     * @param bitmap Bitmap
     * @return File
     * @throws IOException IOException
     */
    @Throws(IOException::class)
    private fun compressPicture(bitmap: Bitmap): File {
        //create a file to write bitmap data
        val f = File(cacheDir, "tmpPicture")
        f.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos)
        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        return f
    }

    /**
     * Get the real path of an URI
     *
     * @param contentURI Uri
     * @return path String?
     */
    private fun getRealPathFromURI(contentURI: Uri): String? {
        var result: String? = null
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }

    /**
     * CallBack library
     *
     * @param requestCode int
     * @param resultCode  int
     * @param data        Intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_LIBRARY) {
            val bitmap = BitmapFactory.decodeFile(getRealPathFromURI(data!!.data))
            val resized = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
            mPresenter.updateProfilePicture(compressPicture(resized))
        }
        if (resultCode == Activity.RESULT_OK && requestCode == ACTIVITY_RESULT_CAMERA) {
            val bitmap = BitmapFactory.decodeFile(mCameraFile?.path)
            val resized = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
            mPresenter.updateProfilePicture(compressPicture(resized))
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
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
