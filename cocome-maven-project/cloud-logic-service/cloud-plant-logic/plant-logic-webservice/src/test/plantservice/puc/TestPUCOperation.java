package plantservice.puc;

/**
 * Test class, just for alleviating the work with plant-specific data structures in test cases
 *
 * @author Rudolf Biczok
 */
public interface TestPUCOperation {

    /**
     * @return returns the operation id
     */
    String getOperationId();

    /**
     * @return returns a human readable name
     */
    String getName();
}
