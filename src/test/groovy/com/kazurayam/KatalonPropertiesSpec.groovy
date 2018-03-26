package com.kazurayam

import spock.lang.Specification

/**
 * Test com.kazurayam.KatalonProperties class using the Spock testing framework
 */
class KatalonPropertiesSpec extends Specification {

    def setupSpec() {
        File home = MultiSourcedProperties.identifyDirectoryAsHome()
        File props = new File(home, KatalonProperties.propertiesFileName)
        if (!props.exists()) {
            Properties pr = new Properties()
            pr.setProperty('GlobalVariable.hostname', 'demoaut.katalon.com')
            pr.store(new FileOutputStream(props), "KatalonPropertiesSpec#setupSpec()")
        }
    }

    /**
     * Here we assume that we have $HOME/katalon.properties file where
     * you find the following line:
     * <pre>
     * {@code
     * GlobalVariable.hostname=demoaut.katalon.com
     * }
     * </pre>
     * @return
     */
    def testLoadingFromHomeDir() {
        setup:
        KatalonProperties kp = new KatalonProperties()
        String hostname = kp.getProperty('GlobalVariable.hostname')
        println("testLoadingFromHomeDir:${hostname}")
        expect:
        hostname != null
        hostname.equals('demoaut.katalon.com')
    }

}
