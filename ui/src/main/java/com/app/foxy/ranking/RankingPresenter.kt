package com.app.foxy.ranking

import android.content.Context
import android.widget.Toast
import com.app.data.network.ExceptionHandler
import com.app.data.network.apiResponse.RankingResponse
import com.app.domain.services.global.IGlobalService
import com.app.domain.services.ranking.IRankingService
import com.app.foxy.eventBus.RankingCompleteEvent
import com.app.foxy.ranking.dagger.RankingScope
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Ranking presenter
 */
@RankingScope
class RankingPresenter(private val rankingService: IRankingService, private val mContext: Context,
                       private val mGlobalService: IGlobalService) : IRankingPresenter {

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

    override fun manageTutorial() {
        if (!mGlobalService.isRankingTutorialShowed(mContext)) {
            mView?.showTutorial()
            mGlobalService.rankingTutorialShowed(mContext)
        }
    }

    override fun getRanking() {
        //mView?.showProgressBar()
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
