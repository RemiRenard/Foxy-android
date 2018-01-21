package org.foxy.foxy.achievement
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import org.foxy.foxy.R

class AchievementFragment : Fragment() {

    private var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater?.inflate(R.layout.fragment_achievement, container, false)
        ButterKnife.bind(this, mView!!)
        return mView
    }

}
