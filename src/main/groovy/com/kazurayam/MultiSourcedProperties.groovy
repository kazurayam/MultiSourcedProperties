package com.kazurayam

/**
 *
 */
public class MultiSourcedProperties {

    private Properties properties

    /**
     *
     */
    public MultiSourcedProperties() {
        this.properties = new Properties()
    }

    /**
     * load Java Properties from files located in the directories given.
     * For example,
     *   1. when found, load ./katalon.properties from the current directory
     *   2. when environment variable $HOME or %USERPROFILE% is given, then load $HOME/.katalon/katalon.properties
     *   3. when JVM system properties -Dkatalon.user.home=/tmp is given in command line, then load /tmp/katalon.properties
     *
     * If multiple locations are specified, the last one wins.
     * If no valid Properties file found, then this object will be same as empty Properties object.
     *
     * @param propertiesFileName
     * @param directories
     */
    public load(List<File> directories, String fileName) throws IOException {
        for (File dir : directories) {
            File file = new File(dir, fileName)
            if (file.exists() && file.canRead()) {
                InputStream istream = new FileInputStream(new File(dir, fileName));
                this.properties.load(istream)
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key)
    }

    public void store(Writer writer, String comment) throws IOException {
        properties.store(writer, comment)
    }

    public Set<String> stringPropertyNames() {
        return properties.stringPropertyNames()
    }



    /**
     * Find out User Home directory in the runtime environment.
     * On Mac and Linux, return the value of $HOME,
     * On Windows, return the value of %USERPROFILE%
     * Return Optional.empty() when failed to find out either.
     * @return Optonal<Path>
     */
    public static File identifyDirectoryAsHome() {
        def home = System.getenv('HOME')
        def userprofile = System.getenv('USERPROFILE')
        if (home != null) {
            return new File(home)
        }
        if (userprofile != null) {
            return new File(userprofile)
        }
        return null
    }

    /**
     *
     * @param systemPropertyName 'katalon.user.home' for example
     * @return
     */
    public static File identifyDirectoryFromSystemProperty(String systemPropertyName) {
        if (systemPropertyName == null) {
            return null
        }
        def value = System.getProperty(systemPropertyName)
        if (value == null) {
            return null
        }
        File dir = new File(value)
        return (dir.exists() && dir.canRead()) ? dir : null
    }
}
