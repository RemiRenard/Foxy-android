package org.foxy.foxy.main

import org.foxy.foxy.IPresenter

/**
 * Interface of the main presenter.
 */
interface IMainPresenter : IPresenter<IMainView> {

    fun refreshToken()
}