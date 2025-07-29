package com.devokay.and.tasks

class AnonStartTask: Initializer<AnalyticsManager> {
}

class AnalyticsInitializer : Initializer<AnalyticsManager> {
  override fun create(context: Context): AnalyticsManager {
    AnalyticsManager.init(context)
    return AnalyticsManager
  }

  override fun dependencies(): List<Class<out Initializer<*>>> {
    return listOf(DatabaseInitializer::class.java)
  }
}

