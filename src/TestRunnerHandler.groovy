
class TestRunnerHandler {

    def handle(TestStepData testStepData) {
        println "Test step ${testStepData.testStep} has template evaluated to \n${testStepData.evaluatedTemplate}"

        if (testStepData.testStep.testCase.actionScriptPath) {
            def bindings = new HashMap()
            bindings['payload'] = testStepData.evaluatedTemplate

            def actionFile = new File(testStepData.testStep.testCase.actionScriptPath)
            new GroovyShell(new Binding(bindings)).evaluate(actionFile)
        } else {
            println "UNKNOWN ACTION for ${testStepData}"
        }

    }
}
