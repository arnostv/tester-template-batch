class TestSuiteClassloader extends GroovyClassLoader {

    TestSuiteClassloader(TestSTS testSTS) {
        addClasspath(testSTS.suite.locationPath)
    }
}
