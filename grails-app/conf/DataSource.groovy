dataSource {
  pooled = true
  driverClassName = "org.h2.Driver"
  username = "sa"
  password = ""
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = false
  cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

// environment specific settings
environments {
  development {
    dataSource {
      dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
      url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
    }
  }
  test {
    dataSource {
      pooled = true
      driverClassName = "com.mysql.jdbc.Driver"
      dialect = org.hibernate.dialect.MySQL5InnoDBDialect
      dbCreate = "create-drop"
      loggingSql = false
      
      url = "jdbc:mysql://localhost/aaf_base_dev"
      username = "aaf_base_dev"
      password =  "password"
    }
  }
}
