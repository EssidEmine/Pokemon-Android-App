package com.bforbank.pokemon.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Production

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Mock