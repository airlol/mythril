package com.niteah.mithryl.command.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command (val labels: Array<String>, val permission: String, val async: Boolean = false)