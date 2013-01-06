println "Running assertion for ${key_value}"
def response = ImaginaryTestedSystem.INSTANCE.readResponse(key_value)

println "Response is ${response}"

if (response!=null) {
    return TestResult.PASSED
} else {
    return TestResult.FAILED
}

