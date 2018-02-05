package org.foxy.foxy.ranking

import org.foxy.foxy.IPresenter

/**
 * Interface of the ranking presenter.
 */
interface IRankingPresenter : IPresenter<IRankingView> {

    fun getRanking()
}