package com.app.foxy.ranking

import com.app.data.model.UserRank
import com.app.foxy.IView

/**
 * Interface of the profile view.
 */
interface IRankingView : IView {

    fun showCurrentUserData(currentUserData: UserRank)

    fun showTutorial()
}
