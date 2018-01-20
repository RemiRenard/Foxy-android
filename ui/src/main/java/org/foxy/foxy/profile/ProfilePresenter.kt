package org.foxy.foxy.profile

import android.content.Context
import android.os.Environment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.model.User
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.profile.dagger.ProfileScope
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Profile presenter
 */
@ProfileScope
class ProfilePresenter(private val context: Context, private val mUserService: IUserService) : IProfilePresenter {

    private var mView: IProfileView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IProfileView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getProfile(forceNetworkRefresh: Boolean) {
        mView?.showProgressBar()
        mUserService.getCurrentUser(forceNetworkRefresh)
                .subscribe(object : Observer<User> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull user: User) {
                        mView?.showProfileInformation(user)
                    }

                    override fun onError(@NonNull e: Throwable) {
                        mView?.hideProgressBar()
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                    }
                })
    }

    override fun updateProfilePicture(picture: File) {
        mView?.showProgressBar()
        mUserService.updateProfilePicture(picture)
                .subscribe(object : Observer<User> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull user: User) {
                        mView?.showProfileInformation(user)
                    }

                    override fun onError(@NonNull e: Throwable) {
                        mView?.hideProgressBar()
                    }

                    override fun onComplete() {
                        mView?.hideProgressBar()
                    }
                })
    }

    override fun setUpPhotoFile(): File? {
        val file = File(Environment.getExternalStorageDirectory().toString() + "/DCIM/")
        var f: File? = null
        try {
            f = File.createTempFile("Foxy_profile_picture_" + Calendar.getInstance().time, ".jpg", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }
}
