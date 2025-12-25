package com.jayden.ad_manager.app.model

data class ParameterUi(
    val paramName: String,
    val paramValue: String?
) {
    override fun toString(): String {
        return "${paramName}: ${paramValue ?: "<null>"}"
    }
}
