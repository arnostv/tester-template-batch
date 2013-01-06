println "***       action called  - printing in lowercase      ***"
def key = ImaginaryTestedSystem.INSTANCE.sendMessage(payload)
println "Key is ${key}"

[
    message_key : key
]