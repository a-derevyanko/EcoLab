package org.ecolab.gradle

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.testing.Test

@CacheableTask
class CustomTest extends Test {
    CustomTest() {
        useTestNG()
        testLogging {
            // set options for log level LIFECYCLE
            events "passed", "skipped", "failed", "standardOut"
            showExceptions true
            showStandardStreams = true
            exceptionFormat "full"
            showCauses true
            showStackTraces true

            // set options for log level DEBUG and INFO
            debug {
                events "started", "passed", "skipped", "failed", "standardOut", "standardError"
                exceptionFormat "full"
            }
            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat

            afterSuite { desc, result ->
                if (!desc.parent) { // will match the outermost suite
                    def output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                    def startItem = '|  ', endItem = '  |'
                    def repeatLength = startItem.length() + output.length() + endItem.length()
                    println('\n' + ('-' * repeatLength) + '\n' + startItem + output + endItem + '\n' + ('-' * repeatLength))
                }
            }
        }
    }
}