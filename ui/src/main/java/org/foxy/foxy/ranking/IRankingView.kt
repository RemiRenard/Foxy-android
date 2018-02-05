package org.foxy.foxy.ranking

import org.foxy.data.model.UserRank
import org.foxy.foxy.IView

/**
 * Interface of the profile view.
 */
interface IRankingView : IView {

    fun showCurrentUserData(currentUserData: UserRank)

}
