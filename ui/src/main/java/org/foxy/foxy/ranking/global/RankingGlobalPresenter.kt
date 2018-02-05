package org.foxy.foxy.ranking.global

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.foxy.data.model.User
import org.foxy.data.network.api_response.RankingResponse
import org.foxy.domain.services.ranking.IRankingService
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.profile.dagger.ProfileScope
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Profile presenter
 */
@ProfileScope
class RankingGlobalPresenter(private val mContext: Context, private val mRankingService: IRankingService) : IRankingGlobalPresenter {

    private var mView: IRankingGlobalView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IRankingGlobalView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getRankings() {
        mView?.showProgressBar()
        Log.i("getRankings", "called")
        mRankingService.getRanking()
                .subscribe {
                    object : Observer<RankingResponse> {
                        override fun onSubscribe(@NonNull d: Disposable) {
                            Log.i("getRankings", "onSubscribe")
                            mCompositeDisposable?.add(d)
                        }

                        override fun onNext(@NonNull rankings: RankingResponse) {
                            Log.i("getRankings", "onComplete")
                            mView?.displayRankings(rankings.globalRanking)
                        }

                        override fun onError(@NonNull e: Throwable) {
                            Toast.makeText(mContext, e.message, Toast.LENGTH_SHORT).show()
                            mView?.hideProgressBar()
                        }

                        override fun onComplete() {
                            mView?.hideProgressBar()
                        }

                    }
                }
    }
}
