package moletower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MathHelperTest {

	@Test
	void testName() throws Exception {
		
	}
	
	@ParameterizedTest
	@MethodSource("distanceProvider")
	void testGetDistance(Point p1, Point p2, double distance) {
		assertEquals(distance, MathHelper.getDistance(p1, p2));
	}

	static Stream<Arguments> distanceProvider() {
		return Stream.of(
				Arguments.arguments(new Point(0,0), new Point(0,0),0),
				Arguments.arguments(new Point(0,0), new Point(1,0),1),
				Arguments.arguments(new Point(1,0), new Point(0,0),1),
				Arguments.arguments(new Point(2,2), new Point(3,4),Math.sqrt(5)),
				Arguments.arguments(new Point(3,5), new Point(2,2),Math.sqrt(10))
			);
	}
	
	@Test
	void testCalculateAngle() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDistancePointToSegment() {
		fail("Not yet implemented");
	}

}
