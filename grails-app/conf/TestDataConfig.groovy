testDataConfig {
  sampleData {
   'aaf.base.EmailView' {
      def i = 1
      name = {-> "name${i++}" }
    }
  }
}
