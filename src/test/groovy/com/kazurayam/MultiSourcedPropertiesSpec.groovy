package com.kazurayam

import spock.lang.Specification

public class MultiSourcedPropertiesSpec extends Specification {

    private MultiSourcedProperties msp;

    def setup() {
        List<File> dirs = new ArrayList<File>()
        dirs.add(new File("./src/test/resources/home"))
        dirs.add(new File("./src/test/resources/other"))
        String fileName = 'katalon.properties'
        this.msp = new MultiSourcedProperties()
        this.msp.load(dirs, fileName)
    }

    def testStringPropertyNames() {
        setup:
        Set<String> names = msp.stringPropertyNames()
        expect:
        4 == names.size()
    }

    def testStoreWithWriter() {
        setup:
        StringWriter sw = new StringWriter()
        msp.store(sw, "testStoreWithWriter")
        System.out.println(sw.toString())
        expect:
        sw.toString().contains("hostname")
    }

    def testUsername() {
        expect:
        "John Doe" == msp.getProperty('GlobalVariable.username')
    }

    def testPassword() {
        expect:
        "ThisIsNotAPassword" == msp.getProperty('GlobalVariable.password')
    }

    def testHostnameOverridden() {
        expect:
        "demoaut.katalon.com" == msp.getProperty('GlobalVariable.hostname')
    }

    def testUndefinedProperty() {
        expect:
        null == msp.getProperty('x-day')
    }


    def testIdentifyDirectoryAsHome() {
        setup:
        def home = MultiSourcedProperties.identifyDirectoryAsHome()
        println("home=${home}")
        expect:
        home != null
        home.exists()
        home.canRead()
    }

    def testIdentifyDirectoryFromSystemPropertyExpectingFailure() {
        setup:
        def prop = 'katalon.user.home'
        def dir = MultiSourcedProperties.identifyDirectoryFromSystemProperty(prop)
        expect:
        dir == null
    }

    def testIdentifyDirectoryFromSystemPropertyExpectingSuccess() {
        setup:
        def prop = 'katalon.user.home'
        def modified = false
        if (System.getProperty(prop) == null) {
            def home = MultiSourcedProperties.identifyDirectoryAsHome()
            System.setProperty(prop, home.toString())
            modified = true
        }
        def dir = MultiSourcedProperties.identifyDirectoryFromSystemProperty(prop)
        expect:
        dir != null
        dir.exists()
        dir.canRead()
        cleanup:
        if (modified) {
            System.clearProperty(prop)
        }
    }

}
