
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
                locationPath: testSuiteDir.getCanonicalPath(),
                actionScriptPath: actionScriptPathIfExists(testSuiteDir.getCanonicalPath())
            )
    }

    def readTestCase(String testCasePath) {

        def steps = readSteps(testCasePath)

        new TestCase(
                location: testCasePath,
                paramsScriptPath: paramsScriptPathIfExists(testCasePath),
                actionScriptPath: actionScriptPathIfExists(testCasePath),
                steps : steps
             )
    }

    Collection<TestStep> readSteps(String location) {
        File locationDir = new File(location)
        assert locationDir.isDirectory()

        def groupFinder = new FileGroupFinder(locationDir,[ACTION_SCRIPT, PARAMS_SCRIPT])
        def groups = groupFinder.findGroups()
        println "Groups : ${groups}"

        def steps = groups.collect{ group ->
            def xml = group.fileByType(".xml")
            def groovy = group.fileByType(".groovy")

            new TestStep(templateFilePath: xml, actionScriptPath: groovy)
        }

        return steps

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
