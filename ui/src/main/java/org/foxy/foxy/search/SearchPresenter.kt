package org.foxy.foxy.search

import android.content.Context
import android.widget.Toast
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.model.Game
import org.foxy.domain.services.game.IGameService
import org.foxy.foxy.search.dagger.SearchScope

/**
 * Search presenter
 */
@SearchScope
class SearchPresenter(private val mContext: Context, private val mGameService: IGameService) : ISearchPresenter {

    private var mView: ISearchView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: ISearchView) {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mCompositeDisposable?.clear()
        mView = null
    }

    override fun getGames(forceNetworkRefresh: Boolean) {
        mView?.showProgressBar()
        mGameService.getGames(forceNetworkRefresh)
                .subscribe(object : Observer<List<Game>> {
                    override fun onSubscribe(@NonNull d: Disposable) {
                        mCompositeDisposable?.add(d)
                    }

                    override fun onNext(@NonNull games: List<Game>) {
                        mView?.displayGames(games)
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
}