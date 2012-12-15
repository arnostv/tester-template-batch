
class TestDefinitionReader {

    static String ACTION_SCRIPT = "action.groovy"
    static String PARAMS_SCRIPT = "params.groovy"

    File testSuiteDir

    TestDefinitionReader(File testSuiteDir) {
      this.testSuiteDir = testSuiteDir

      assert testSuiteDir.isDirectory()
    }

    Collection<TestCase> readTestSuite() {
        def filesInTestSuite = testSuiteDir.listFiles().toList()
        def directories =  filesInTestSuite.findAll {it.isDirectory()}

        def testCases = directories.collect{
            readTestCase(it.getCanonicalPath())
        }
        testCases
    }

    def readTestCase(String testCasePath) {

        def actionScriptFile = new File(testCasePath, ACTION_SCRIPT)
        def actionScriptPath = null
        if (actionScriptFile.isFile()) {
            actionScriptPath = actionScriptFile.getCanonicalPath()
        }

        def paramsScriptFile = new File(testCasePath, PARAMS_SCRIPT)
        def paramsScriptPath = null
        if (paramsScriptFile.isFile()) {
            paramsScriptPath = paramsScriptFile.getCanonicalPath()
        }

        new TestCase(location: testCasePath, paramsScriptPath: paramsScriptPath, actionScriptPath: actionScriptPath)
    }
}
