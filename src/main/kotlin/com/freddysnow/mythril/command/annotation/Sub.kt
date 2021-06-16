package com.freddysnow.mythril.command.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sub (val labels: Array<String>, val usage: String, val permission: String = "", val async: Boolean = false)