package com.voltek.newsfeed.navigation.command

import com.voltek.newsfeed.navigation.proxy.Command

/**
 * Show result of previous screen execution.
 * (Toast or SnackBar)
 */
class CommandShowResult(
        val message: String? = null,
        val error: Exception? = null
) : Command()