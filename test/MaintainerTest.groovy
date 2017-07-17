import org.junit.BeforeClass
import org.junit.Test
import maintainer

class MaintainerTest {

    /**
     * Use Groovy metaclass programming to add methods to the Jenkins pipeline shared library exposed class.
     * This allows for unit testing of classes that makes use of Jenkins pipeline steps, such as
     * 'sh', 'echo' or e.g. other steps available through the workflow-basic-steps-plugin
     */
    @BeforeClass
    static void setup() {
        maintainer.metaClass.echo {
            println it
            return it
        }
    }

    @Test
    void shouldOutputMaintainerInformation() {
        def varsFile = new maintainer()
        def expectedArg = 'Diabol AB'
        def returnVal = varsFile.call(expectedArg)

        assert returnVal != null
        assert returnVal == "Project maintained by $expectedArg"
    }

}