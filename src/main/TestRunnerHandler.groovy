
class TestRunnerHandler {
    TestResultHandler testResultHandler;

    TestRunnerHandler(TestResultHandler testResultHandler) {
        this.testResultHandler = testResultHandler;
    }

    def runTestStep(TestStepData testStepData) {
        def result = runScript(testStepData)

        testResultHandler.handleTestResult(testStepData, result)
    }


    private  runScript(TestStepData testStepData) {
        def result = null
        def actionScriptPath = testStepData.testSTS.findActionScriptPath()

        if (actionScriptPath) {
            def bindings = new HashMap(testStepData.testSTS.params)

            bindings['payload'] = testStepData.evaluatedTemplate

            bindings['out'] = System.out

            def actionFile = new File(actionScriptPath)
            def classLoader = new TestSuiteClassloader(testStepData.testSTS)
            result = new GroovyShell(classLoader, new Binding(bindings)).evaluate(actionFile)
        } else {
            println "UNKNOWN ACTION for ${testStepData}"
        }
        result
    }


}
