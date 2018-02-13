package com.foxyApp.foxy.friends.requests

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.data.network.api_response.FriendsRequestsResponse
import com.foxyApp.data.network.api_response.SimpleSuccessResponse
import com.foxyApp.domain.services.friend.IFriendService
import com.foxyApp.foxy.R
import com.foxyApp.foxy.profile.dagger.ProfileScope

/**
 * Friends requests presenter
 */
@ProfileScope
class FriendsRequestsPresenter(private val mFriendService: IFriendService, private val mContext: Context) : IFriendsRequestsPresenter {

    private var mView: IFriendsRequestsView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IFriendsRequestsView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getFriendsRequests() {
        mView?.showProgressBar()
        mFriendService.getFriendsRequests().subscribe(object : Observer<List<FriendsRequestsResponse>> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull friendsRequests: List<FriendsRequestsResponse>) {
                mView?.displayFriendsRequests(friendsRequests)
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

    override fun acceptRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse) {
        mView?.showProgressBar()
        mFriendService.acceptFriendRequest(requestId, notificationId).subscribe(object : Observer<SimpleSuccessResponse> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull response: SimpleSuccessResponse) {
                if (response.success) {
                    mView?.friendRequestCompleted(request)
                    Toast.makeText(mContext, R.string.Friend_request_accepted, Toast.LENGTH_SHORT).show()
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

    override fun declineRequest(requestId: String, notificationId: String, request: FriendsRequestsResponse) {
        mView?.showProgressBar()
        mFriendService.declineFriendRequest(requestId, notificationId).subscribe(object : Observer<SimpleSuccessResponse> {
            override fun onSubscribe(@NonNull d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(@NonNull response: SimpleSuccessResponse) {
                if (response.success) {
                    mView?.friendRequestCompleted(request)
                    Toast.makeText(mContext, R.string.Friend_request_declined, Toast.LENGTH_SHORT).show()
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