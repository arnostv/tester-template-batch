
class TestDefinitionReader {

    static String ACTION_SCRIPT = "action.groovy"
    static String PARAMS_SCRIPT = "params.groovy"

    File testSuiteDir

    TestDefinitionReader(File testSuiteDir) {
      this.testSuiteDir = testSuiteDir

      assert testSuiteDir.isDirectory()
    }

    TestSuite readTestSuite() {
        def filesInTestSuite = testSuiteDir.listFiles().toList()
        def directories =  filesInTestSuite.findAll {it.isDirectory()}

        def testCases = directories.collect{
            readTestCase(it.getCanonicalPath())
        }
        new TestSuite(
                testCases:  testCases,
                actionScriptPath: actionScriptPathIfExists(testSuiteDir.getCanonicalPath())
            )
    }

    def readTestCase(String testCasePath) {

        new TestCase(
                location: testCasePath,
                paramsScriptPath: paramsScriptPathIfExists(testCasePath),
                actionScriptPath: actionScriptPathIfExists(testCasePath)
             )
    }

    def actionScriptPathIfExists(def directory) {
        def actionScriptFile = new File(directory, ACTION_SCRIPT)
        def actionScriptPath = null
        if (actionScriptFile.isFile()) {
            actionScriptPath = actionScriptFile.getCanonicalPath()
        }
        actionScriptPath
    }

    def paramsScriptPathIfExists(def directory) {
        def paramsScriptFile = new File(directory, PARAMS_SCRIPT)
        def paramsScriptPath = null
        if (paramsScriptFile.isFile()) {
            paramsScriptPath = paramsScriptFile.getCanonicalPath()
        }
        paramsScriptPath
    }
}
