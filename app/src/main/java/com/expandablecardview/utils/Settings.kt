package com.expandablecardview.utils

class Settings {
    companion object {
        internal val EXPAND_DURATION = 300
    }

    internal var expandDuration = EXPAND_DURATION
    internal var expandWithParentScroll: Boolean = false
    internal var expandScrollTogether: Boolean = false
}