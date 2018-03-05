package com.foxyApp.foxy.ranking

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.foxyApp.data.network.ExceptionHandler
import com.foxyApp.data.network.apiResponse.RankingResponse
import com.foxyApp.domain.services.ranking.IRankingService
import com.foxyApp.foxy.eventBus.RankingCompleteEvent
import com.foxyApp.foxy.profile.dagger.ProfileScope
import org.greenrobot.eventbus.EventBus

/**
 * Ranking presenter
 */
@ProfileScope
class RankingPresenter(private val rankingService: IRankingService, private val mContext: Context) : IRankingPresenter {

    private var mView: IRankingView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IRankingView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getRanking() {
        mView?.showProgressBar()
        rankingService.getRanking().subscribe(object : Observer<RankingResponse> {
            override fun onComplete() {
                mView?.hideProgressBar()
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable?.add(d)
            }

            override fun onNext(rankingResponse: RankingResponse) {
                EventBus.getDefault().post(RankingCompleteEvent(rankingResponse))
                if (rankingResponse.currentUserData != null) {
                    mView?.showCurrentUserData(rankingResponse.currentUserData!!)
                }
            }

            override fun onError(e: Throwable) {
                Toast.makeText(mContext, ExceptionHandler.getMessage(e, mContext), Toast.LENGTH_LONG).show()
                mView?.hideProgressBar()
            }

        })
    }
}
