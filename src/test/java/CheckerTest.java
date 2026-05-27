import org.junit.Test;
import ru.itmo.web.AreaChecker;

import java.math.BigDecimal;
import static org.junit.Assert.*;

public class CheckerTest {
    // (x>=0, y>=0)
    @Test
    public void testTriangleHit() {
        //  y <= -x + R/2
        assertTrue(AreaChecker.checkHit(new BigDecimal("1.0"), new BigDecimal("1.0"), new BigDecimal("4.0")));
    }

    @Test
    public void testTriangleMiss() {
        // y > -x + R/2
        assertFalse(AreaChecker.checkHit(new BigDecimal("1.0"), new BigDecimal("2.0"), new BigDecimal("4.0")));
        assertFalse(AreaChecker.checkHit(new BigDecimal("2.5"), new BigDecimal("0.5"), new BigDecimal("4.0")));
        assertFalse(AreaChecker.checkHit(new BigDecimal("3.0"), new BigDecimal("0.0"), new BigDecimal("4.0")));
    }

    // x<=0, y<=0)
    @Test
    public void testSectorHit() {
        // x² + y² <= R²
        assertTrue(AreaChecker.checkHit(new BigDecimal("-1.0"), new BigDecimal("-1.0"), new BigDecimal("2.0")));
    }

    @Test
    public void testSectorMiss() {
        // x² + y² > R²
        assertFalse(AreaChecker.checkHit(new BigDecimal("-2.0"), new BigDecimal("-2.0"), new BigDecimal("2.0")));
    }

    // x>=0, y<=0
    @Test
    public void testRectangleHit() {
        //x <= R и y >= -R/2
        assertTrue(AreaChecker.checkHit(new BigDecimal("1.0"), new BigDecimal("-1.0"), new BigDecimal("3.0")));
    }

    @Test
    public void testRectangleMiss() {
        // x > R или y < -R/2
        assertFalse(AreaChecker.checkHit(new BigDecimal("3.5"), new BigDecimal("-1.0"), new BigDecimal("3.0")));
    }
}