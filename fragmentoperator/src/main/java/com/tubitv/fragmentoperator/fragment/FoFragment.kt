package com.tubitv.fragmentoperator.fragment

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.tubitv.fragments.FragmentOperator

open class FoFragment : Fragment() {
    private val TAG = FoFragment::class.simpleName

    private val PREVIOUS_FRAGMENT_TAG = "previous_fragment_tag"
    private val CURRENT_FRAGMENT_TAG = "current_fragment_tag"
    private val FRAGMENT_TAG_SEPERATOR = ":"

    var skipOnPop = false // Flag to mark if current instance should be skipped when pop back stack
    var previousFragmentTag: String? = null // Tag for fragment that will go to when pop back from current instance

    private var mCurrentFragmentTag: String? = null // Tag for current fragment instance

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save previous fragment tag and current fragment tag, so we don't lose them during fragment recreation
        outState.putString(PREVIOUS_FRAGMENT_TAG, previousFragmentTag)
        outState.putString(CURRENT_FRAGMENT_TAG, getFragmentTag())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            previousFragmentTag = savedInstanceState.getString(PREVIOUS_FRAGMENT_TAG)
            mCurrentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG)
        }
    }

    /**
     * Return the unique tag per fragment instance
     * Since fragment can be re-created when activity configuration changed (i.e. screen rotation) and hashCode will
     * change as well, we should only and always use the initial hashCode.
     *
     * @return Unique tag string per instance
     */
    fun getFragmentTag(): String? {
        if (mCurrentFragmentTag == null) {
            mCurrentFragmentTag = this.javaClass.simpleName + FRAGMENT_TAG_SEPERATOR + this.hashCode()
        }
        return mCurrentFragmentTag
    }

    /**
     * Add a initial child fragment.
     *
     * @param fragment    Fragment Instance
     * @param containerId Fragment Container ID
     */
    fun addInitialChildFragment(fragment: FoFragment, containerId: Int) {
        childFragmentManager.beginTransaction().add(containerId, fragment).commit()
    }

    /**
     * Show a fragment in a container with control of clearing previous back stack and setting fragment to be skipped on back
     *
     * @param fragment Fragment instance to be displayed
     * @param clearStack Flag to clear back stack or not
     * @param skipOnPop Flag for fragment to be skipped when pop back stack
     * @param containerId Container resource ID to display the fragment
     */
    fun showFragment(fragment: FoFragment,
                     clearStack: Boolean,
                     skipOnPop: Boolean,
                     @IdRes containerId: Int) {
        FragmentOperator.showFragment(childFragmentManager, fragment, clearStack, skipOnPop, containerId)
    }

    fun getCurrentChildFragment(@IdRes containerId: Int): FoFragment? {
        return FragmentOperator.getCurrentFragment(childFragmentManager, containerId)
    }

}