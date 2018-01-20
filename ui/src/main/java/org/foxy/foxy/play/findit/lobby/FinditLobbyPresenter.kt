package org.foxy.foxy.play.findit.lobby

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.foxy.domain.services.ws.IWsService
import org.foxy.foxy.play.findit.dagger.FinditScope

/**
 * Findit lobby presenter
 */
@FinditScope
class FinditLobbyPresenter(private val mContext: Context, private val mWsService: IWsService) : IFinditLobbyPresenter {

    private var mView: IFinditLobbyView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IFinditLobbyView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun disconnectWebSocket() {
        mWsService.disconnectWebSocket()
    }
}
