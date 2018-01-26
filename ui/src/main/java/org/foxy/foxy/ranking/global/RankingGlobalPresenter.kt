package org.foxy.foxy.ranking.global

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
class RankingGlobalPresenter(private val context: Context) : IRankingGlobalPresenter {

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
}
