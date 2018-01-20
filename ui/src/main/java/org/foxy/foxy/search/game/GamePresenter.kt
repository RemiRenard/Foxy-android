package org.foxy.foxy.search.game

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import org.foxy.foxy.search.dagger.SearchScope

/**
 * Game presenter
 */
@SearchScope
class GamePresenter(private val mContext: Context) : IGamePresenter {

    private var mView: IGameView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: IGameView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }
}