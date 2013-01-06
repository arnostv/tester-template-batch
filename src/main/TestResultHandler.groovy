class TestResultHandler {
    Map<TestStepData, TestResult> results = [:]

    static String TEST_OUTPUT_DIRECTORY = "testoutput"

    def handleTestResult(TestStepData testStepData, def result) {
        if (result!=null) {
            File testOutputDirectory = findTestOutputDirectory(testStepData.testSTS);

            File testResultDir = findTestResultDirectory(testStepData.testSTS, testOutputDirectory)

            println "Result base dir is ${testOutputDirectory} --> output to ${testResultDir}"

            def testName = testStepData.testSTS.step.fileGroup.prefix
            File testResultFile = new File(testResultDir, testName + ".txt")
            testResultFile.write(result.toString())

            if (TestResult.values().contains(result)) {
                results[testStepData] = result
            }

        } else {
            println "Undefined result"
        }
    }

    File findTestResultDirectory(TestSTS testSTS,File testOutputDirectory) {
        File testSuiteDir = new File(testSTS.suite.locationPath)
        File testCaseDir = new File(testSTS.testCase.location)

        def testCasePath = testCaseDir.absolutePath
        def testSuitePath = testSuiteDir.absolutePath

        File testResultDir
        if (testCasePath.startsWith(testSuitePath)) {
            testResultDir = new File(testOutputDirectory, testCasePath.substring(1+testSuitePath.length()))

            if (!testResultDir.isDirectory()) {
                def directoryCreated = testResultDir.mkdirs()
                if (directoryCreated) {
                    println "Created ${testResultDir}"
                } else {
                    println "Failed to create ${testResultDir}"
                }
            }
        }

        assert testResultDir != null

        testResultDir
    }

    File findTestOutputDirectory(TestSTS testSTS) {
        def testSuiteDir = new File(testSTS.suite.locationPath)
        def testOutput = new File(testSuiteDir.parentFile, TEST_OUTPUT_DIRECTORY)

        assert testOutput.isDirectory()
        assert testOutput.canWrite()
        testOutput
    }

    def summarizeTestCase(TestSTS testSTS) {
        File testOutputDir = findTestOutputDirectory(testSTS)
        File testResultDir = findTestResultDirectory(testSTS, testOutputDir)

        File testSummary = new File(testResultDir, "RESULT.txt")

        def testCases = results.findAll {it.key.testSTS.testCase == testSTS.testCase}

        println "Test cases are  ${testCases}"

        def passedCount = testCases.count {it.value == TestResult.PASSED}
        def failedCount = testCases.count {it.value == TestResult.FAILED}

        def resultSummary
        if (passedCount == 0 && failedCount ==0) {
            resultSummary = "UNKNOWN"
        } else if (failedCount > 0) {
            resultSummary = "FAILED"
                            //+ "\n" + testCases.find {it.value = TestResult.FAILED}
        } else {
            resultSummary = "PASSED"
        }

        testSummary.write(resultSummary)
    }
}
