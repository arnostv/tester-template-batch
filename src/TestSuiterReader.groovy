
class TestSuiterReader {

    File testSuiteDir

    TestSuiterReader(File testSuiteDir) {
      this.testSuiteDir = testSuiteDir

      assert testSuiteDir.isDirectory()
    }

    Collection<TestCase> readTestSuite() {
        def directories =  testSuiteDir.listFiles().toList().findAll {it.isDirectory()}

        def testCases = directories.collect{
            new TestCase(location: it.getCanonicalPath())
        }
        testCases
    }
}
