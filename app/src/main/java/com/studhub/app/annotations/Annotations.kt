package com.studhub.app.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
internal annotation class ExcludeFromGeneratedTestCoverage
