package com.niteah.mythril.command.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Completer (val completion: String)