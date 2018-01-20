package org.foxy.foxy.play.findit.home

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.foxy.domain.services.ws.IWsService
import org.foxy.foxy.play.findit.dagger.FinditScope

/**
 * Findit home presenter
 */
@FinditScope
class FinditHomePresenter(private val mContext: Context, private val mWsService: IWsService) : IFinditHomePresenter {

    private var mView: IFinditHomeView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IFinditHomeView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun connectWebSocket() {
        mWsService.connectWebSocket()
    }
}
