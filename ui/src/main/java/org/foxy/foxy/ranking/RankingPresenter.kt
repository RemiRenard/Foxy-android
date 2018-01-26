package org.foxy.foxy.ranking

import android.content.Context
import android.os.Environment
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.foxy.data.model.User
import org.foxy.domain.services.user.IUserService
import org.foxy.foxy.profile.dagger.ProfileScope
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Profile presenter
 */
@ProfileScope
class RankingPresenter(private val context: Context) : IRankingPresenter {

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
}
