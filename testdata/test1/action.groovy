assert payload != null
assert payload.size() > 0

println "--------- action called ----------"
println "Execution timestamp ${timestamp}"
println "Payload contains ${payload.toString().split('\n').size()}  lines"
println ""

return payload
