
class TestRunnerHandler {

    def handle(TestStepData testStepData) {
        def testStep = testStepData.testSTS.step

        def actionScriptPath = findActionScriptPath(testStepData.testSTS)

        if (actionScriptPath) {
            def bindings = new HashMap(testStepData.testSTS.params)
            bindings['payload'] = testStepData.evaluatedTemplate

            bindings['out'] = System.out

            def actionFile = new File(actionScriptPath)
            def classLoader = new GroovyClassLoader()
            classLoader.addClasspath(testStepData.testSTS.suite.locationPath)
            new GroovyShell(classLoader, new Binding(bindings)).evaluate(actionFile)
        } else {
            println "UNKNOWN ACTION for ${testStepData}"
        }

    }

    String findActionScriptPath (TestSTS testSTS) {
        String actionScriptPath = null
        if (testSTS?.step?.actionScriptPath) {
            actionScriptPath = testSTS?.step?.actionScriptPath
            println "Using test Step script ${actionScriptPath}"
        } else if (testSTS?.testCase?.actionScriptPath) {
            actionScriptPath = testSTS?.testCase?.actionScriptPath
            println "Using test Case script ${actionScriptPath}"
        } else if (testSTS?.suite?.actionScriptPath) {
            actionScriptPath = testSTS?.suite?.actionScriptPath
            println "Using test Suite script ${actionScriptPath}"
        }
        actionScriptPath
    }
}
