package com.kazurayam

public class KatalonProperties {

    public static String propertiesFileName = 'katalon.properties'
    public static String environmentVariableName = 'KATALON_USER_HOME'
    public static String systemPropertyName = 'katalon.user.home'

    private MultiSourcedProperties msp;

    public KatalonProperties() {
        this.msp = new MultiSourcedProperties()
        List<File> dirs = new ArrayList<File>()
        // properties file under the current directory
        File currentDir = new File(".")
        File props = new File(currentDir, propertiesFileName)
        if (props.exists() && props.canRead()) {
            dirs.add(currentDir)
        }
        // properties file under the HOME directory
        File homeDir = MultiSourcedProperties.identifyDirectoryAsHome()
        props = new File(homeDir, propertiesFileName)
        if (props.exists() && props.canRead()) {
            dirs.add(homeDir)
        }
        // properties file under the directory specified by the environment variable KATALON_USER_HOME
        def env = System.getenv(environmentVariableName)
        if (env != null) {
            File envVarDir = new File(env)
            if (envVarDir.exists() && envVarDir.canRead()) {
                dirs.add(envVarDir)
            }
        }
        // properties file under the directory specified by the JVM System Property 'katalon.user.home'
        def kuh = System.getProperty(systemPropertyName)
        if (kuh != null) {
            File systemPropDir = new File(kuh)
            if (systemPropDir.exists() && systemPropDir.canRead()) {
                dirs.add(systemPropDir)
            }
        }
        // then, load values from files
        msp.load(dirs, propertiesFileName)
    }

    public String getProperty(String key) {
        msp.getProperty(key)
    }
}
