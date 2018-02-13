package com.foxyApp.foxy.main

import com.foxyApp.foxy.IPresenter

/**
 * Interface of the main presenter.
 */
interface IMainPresenter : IPresenter<IMainView> {

    fun refreshToken()
}