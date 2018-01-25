package org.foxy.foxy.friends

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.model.User
import org.foxy.data.network.ExceptionHandler
import org.foxy.data.network.api_response.FriendsRequestsResponse
import org.foxy.domain.services.friend.IFriendService
import org.foxy.foxy.profile.dagger.ProfileScope

/**
 * Friends presenter
 */
@ProfileScope
class FriendsPresenter(private val mFriendService: IFriendService, private val mContext: Context) : IFriendsPresenter {

    private var mView: IFriendsView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IFriendsView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getFriends() {
        mView?.showProgressBar()
        mFriendService.getFriends().subscribe(object : Observer<List<User>> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull friends: List<User>) {
                mView?.displayFriends(friends)
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

    override fun getFriendsRequests() {
        mFriendService.getFriendsRequests().subscribe(object : Observer<List<FriendsRequestsResponse>> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull friendsRequests: List<FriendsRequestsResponse>) {
                mView?.displayFriendsRequests(friendsRequests)
            }

            override fun onError(@NonNull e: Throwable) {
                Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
            }

            override fun onComplete() {
                // Do nothing
            }
        })
    }
}