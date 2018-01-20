package org.foxy.foxy.search.dagger

import dagger.Subcomponent
import org.foxy.foxy.search.SearchFragment

/**
 * Search sub component.
 */
@SearchScope
@Subcomponent(modules = arrayOf(SearchModule::class))
interface SearchComponent {

    // inject target here
    fun inject(target: SearchFragment)
}