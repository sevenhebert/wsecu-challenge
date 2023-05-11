package com.vend.metrics

import io.prometheus.client.CollectorRegistry

trait MetricsModule {
  lazy val metricsApi = new MetricsApi(collectorRegistry)
  lazy val collectorRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry
}
