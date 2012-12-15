import groovy.text.XmlTemplateEngine

class TestRunner {

    def xmlTemplateEngine = new XmlTemplateEngine()

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


        def template = xmlTemplateEngine.createTemplate(new File(testStep.location)).make(params)
        println template.toString()
    }

    def evalTestCaseParams(TestCase testCase) {
        def defaultMappingsFile = new File(testCase.location, "params.groovy")
        if (defaultMappingsFile.isFile()) {
            println "Evaluating parameters from ${defaultMappingsFile}"
            Map mappings = new GroovyShell().evaluate(defaultMappingsFile)
            mappings.asImmutable()
        } else {
            Map mappingsUndefined = [_parametersForTestCaseNotSet : true]
            mappingsUndefined.asImmutable()
        }
    }

    def evalTestStepParams(TestStep testStep, Map alreadyDefinedParams) {
        def testStepParams = new File(testStep.location.replaceAll("\\.xml",".groovy"))
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
