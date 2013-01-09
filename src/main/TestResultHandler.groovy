import java.text.SimpleDateFormat

class TestResultHandler {
    Map<TestStepData, TestResult> results = [:]
    File testOutputDirectory

    static String TEST_OUTPUT_DIRECTORY = "testoutput"
    static boolean TEST_OUTPUT_NUMBERED = true
    static int NUMBER_LENGTH = 3

    def handleTestResult(TestStepData testStepData, def result) {
        if (result!=null) {
            File testResultDir = findTestResultDirectory(testStepData.testSTS)

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

    File findTestResultDirectory(TestSTS testSTS) {
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
        File testResultDir = findTestResultDirectory(testSTS)

        File testSummary = new File(testResultDir, "RESULT.txt")

        def testCases = results.findAll {it.key.testSTS.testCase == testSTS.testCase}
        println "Test cases are  ${testCases}"

        def resultSummary = summaryForTestResults(testCases)

        testSummary.write(resultSummary)
    }

    def summarizeTestSuite(TestSTS testSTS) {
        File testSummary = new File(testOutputDirectory, "RESULT.txt")

        def testSuites = results.findAll {it.key.testSTS.suite == testSTS.suite}

        def resultSummary = summaryForTestResults(testSuites)

        testSummary.write(resultSummary)
    }

    private summaryForTestResults(Map<TestStepData, TestResult> testResults) {
        def resultSummary
        def passedCount = testResults.count { it.value == TestResult.PASSED }
        def failedCount = testResults.count { it.value == TestResult.FAILED }

        if (passedCount == 0 && failedCount == 0) {
            resultSummary = "UNKNOWN"
        } else if (failedCount > 0) {
            resultSummary = "FAILED"
            //+ "\n" + testCases.find {it.value = TestResult.FAILED}
        } else {
            resultSummary = "PASSED"
        }
        resultSummary
    }

    def initializeTestOutputDirectory(TestSTS testSTS) {
        def baseTestOutput = findTestOutputDirectory(testSTS)

        if (TEST_OUTPUT_NUMBERED) {
            def suffix = "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
            def allFiles = baseTestOutput.listFiles().toList()
            def allDirectoriesWithNumbers = allFiles.findAll {it -> it.isDirectory() && it.name=~ /[0-9]+.*/}

            def allNumber = allDirectoriesWithNumbers.collect {fle ->
                def matcher = fle.name =~ /^([0-9]+).*$/
                def matches = matcher.matches()
                def result = matcher[0][1]
                result.toInteger()
            }
            int number = 1
            if (!allDirectoriesWithNumbers.empty) {
                number = allNumber.last() + 1
            }
            def numberFormated = String.format("%0" + NUMBER_LENGTH + "d", number)
            testOutputDirectory = new File(baseTestOutput,numberFormated + suffix)

        } else {
            testOutputDirectory = baseTestOutput
        }
    }
}
