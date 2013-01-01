
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

        def allFiles = locationDir.listFiles()
        def files = allFiles.findAll {
            it.isFile() && it.getName().endsWith(".xml")
        }

        files.collect{
            def path = it.getCanonicalPath()
            def groovyScript = path.replaceAll("\\.xml", ".groovy")
            def groovyScriptPath = null
            if (new File(groovyScript).isFile()) {
                println "Found action script for step $groovyScript"
                groovyScriptPath = groovyScript
            }
            new TestStep(templateFilePath: path, actionScriptPath: groovyScriptPath)
        }
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
