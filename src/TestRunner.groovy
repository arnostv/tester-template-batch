import groovy.text.XmlTemplateEngine

class TestRunner {

    def testRunnerHandler = new TestRunnerHandler()

    def xmlTemplateEngine = new XmlTemplateEngine() //GStringTemplateEngine

    def runTestSuite(Collection<TestCase> testCases) {
        testCases.each{runTestCase(it)}
    }

    def runTestCase(TestCase testCase) {

        println "Running ${testCase}"

        def defaultMappings = evalTestCaseParams(testCase)

        def steps = testCase.readSteps()

        steps.each{runTestStep(defaultMappings, it)}

    }

    def runTestStep(def testCaseParams, TestStep testStep) {
        println "Running step ${testStep}"

        def testStepParams = evalTestStepParams(testStep, testCaseParams)

        def params = new HashMap(testStepParams) // template engine seems to fail with immutable map


        def template = xmlTemplateEngine.createTemplate(new File(testStep.templateFilePath)).make(params)
        def evaluatedTemplate =  template.toString()
        testRunnerHandler.handle(new TestStepData(testStep, evaluatedTemplate))
    }

    def evalTestCaseParams(TestCase testCase) {
        if (testCase.paramsScriptPath) {
            println "Evaluating parameters from ${testCase.paramsScriptPath}"
            Map mappings = new GroovyShell().evaluate(new File(testCase.paramsScriptPath))
            mappings.asImmutable()
        } else {
            Map mappingsUndefined = [_parametersForTestCaseNotSet : true]
            mappingsUndefined.asImmutable()
        }
    }

    def evalTestStepParams(TestStep testStep, Map alreadyDefinedParams) {
        def testStepParams = new File(testStep.templateFilePath.replaceAll("\\.xml",".params.groovy"))
        Map paramsForStep
        if (testStepParams.isFile()) {
            println "Evaluating parameters from ${testStepParams}"
            paramsForStep = new GroovyShell().evaluate(testStepParams)
        } else {
            paramsForStep = [_parametersForTestStepNotSet : true]
        }

        Map union = (alreadyDefinedParams + paramsForStep)
        union.asImmutable()
    }


}
