package org.foxy.foxy.ranking

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.network.ExceptionHandler
import org.foxy.data.network.api_response.RankingResponse
import org.foxy.domain.services.ranking.IRankingService
import org.foxy.foxy.event_bus.RankingCompleteEvent
import org.foxy.foxy.profile.dagger.ProfileScope
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
