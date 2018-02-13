package com.foxyApp.foxy.ranking

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the ranking presenter.
 */
interface IRankingPresenter : IPresenter<IRankingView> {

    fun getRanking()
}