println "Running assertion for ${key_value}"
def response = ImaginaryTestedSystem.INSTANCE.readResponse(key_value)

println "Response is ${response}"
assert response != null : "response exists"

[
    response: response
]


