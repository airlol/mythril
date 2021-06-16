package com.freddysnow.mythril.command.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command (val labels: Array<String>, val usage: String, val permission: String = "", val async: Boolean = false)