package com.foxyApp.foxy.ranking

import com.foxyApp.data.model.UserRank
import com.foxyApp.foxy.IView

/**
 * Interface of the profile view.
 */
interface IRankingView : IView {

    fun showCurrentUserData(currentUserData: UserRank)

}
