package com.mobbile.paul.provider

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass



@Suppress("DEPRECATED_JAVA_ANNOTATION")
@Documented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
