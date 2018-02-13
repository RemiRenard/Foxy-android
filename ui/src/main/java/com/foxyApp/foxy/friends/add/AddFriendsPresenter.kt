package com.foxyApp.foxy.friends.add

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.model.User
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.data.network.api_response.SimpleSuccessResponse
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.domain.services.user.IUserService
import com.foxyApp.foxy.R
import com.foxyApp.foxy.profile.dagger.ProfileScope

/**
 * AddFriends presenter
 */
@ProfileScope
class AddFriendsPresenter(private val mUserService: IUserService, private val mFriendService: IFriendService,
                          private val mContext: Context) : IAddFriendsPresenter {

    private var mView: IAddFriendsView? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private val mLimit: Int = 20

    override fun attachView(view: IAddFriendsView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun findUsers(username: String) {
        mView?.showProgressBar()
        //@TODO pagination with the skip
        mUserService.findUsers(username, mLimit, 0).subscribe(object : Observer<List<User>> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull users: List<User>) {
                mView?.displayUsers(users)
            }

            override fun onError(@NonNull e: Throwable) {
                Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
                mView?.hideProgressBar()
            }

            override fun onComplete() {
                mView?.hideProgressBar()
            }
        })
    }

    override fun addFriends(user: User) {
        mView?.showProgressBar()
        mFriendService.sendFriendRequest(user).subscribe(object : Observer<SimpleSuccessResponse> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull simpleSuccessResponse: SimpleSuccessResponse) {
                if (simpleSuccessResponse.success) {
                    mView?.addFriendsComplete(user)
                    Toast.makeText(mContext, mContext.getString(R.string.Request_sent_to, user.username),
                            Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(@NonNull e: Throwable) {
                Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                mView?.hideProgressBar()
            }

            override fun onComplete() {
                mView?.hideProgressBar()
            }
        })
    }
}