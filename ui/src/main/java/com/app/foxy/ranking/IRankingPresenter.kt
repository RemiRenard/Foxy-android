package com.app.foxy.ranking

import com.app.foxy.IPresenter

/**
 * Interface of the ranking presenter.
 */
interface IRankingPresenter : IPresenter<IRankingView> {

    fun getRanking()
}