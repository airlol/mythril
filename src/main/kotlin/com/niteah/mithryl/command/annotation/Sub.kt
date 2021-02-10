package com.niteah.mithryl.command.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Sub (val labels: Array<String>, val permission: String, val async: Boolean = false)