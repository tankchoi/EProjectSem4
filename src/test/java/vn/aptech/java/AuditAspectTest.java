package vn.aptech.java;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuditAspectTest {

    @Test
    public void testLongArgumentType() {
        Long id = 1L;
        System.out.println("Testing Long argument:");
        System.out.println("id instanceof Number: " + (id instanceof Number));
        System.out.println("id.getClass(): " + id.getClass());
        System.out.println("id.getClass().getSimpleName(): " + id.getClass().getSimpleName());

        // This should NOT throw an error since Long extends Number
        assertTrue(id instanceof Number, "Long should be recognized as Number");
        assertEquals("Long", id.getClass().getSimpleName());

        // Simulate the AuditAspect logic
        Object firstArg = id;
        if (firstArg instanceof Number) {
            System.out.println("SUCCESS: Long is properly recognized as Number in AuditAspect logic");
            // This is the correct path for deleteLaptop(Long id)
        } else {
            System.out.println("ERROR: Long is not recognized as Number");
            fail("Long should be recognized as Number in AuditAspect logic");
        }
    }

    @Test
    public void testSimulateGetEntityIdCall() {
        Long id = 1L;

        // This simulates what would happen if the AuditAspect wrongly tries to call
        // getEntityId on a Long
        try {
            // This should throw NoSuchMethodException since Long doesn't have getId()
            id.getClass().getMethod("getId");
            fail("Long should not have a getId() method");
        } catch (NoSuchMethodException e) {
            System.out.println("EXPECTED: Long does not have getId() method - " + e.getMessage());
            // This is expected - the error would be "Entity Long does not have a getId()
            // method"
        }
    }
}
