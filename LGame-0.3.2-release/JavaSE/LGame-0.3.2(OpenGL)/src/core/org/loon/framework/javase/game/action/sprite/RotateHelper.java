package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.utils.MathUtils;

/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class RotateHelper {

	public static Vector2f calculateVector(float angle, float magnitude) {
		Vector2f v = new Vector2f();
		v.x = MathUtils.sin(MathUtils.toRadians(angle));
		v.x *= magnitude;
		v.y = -MathUtils.cos(MathUtils.toRadians(angle));
		v.y *= magnitude;
		return v;
	}

	public static float calculateAngle(float x, float y, float x1, float y1) {
		float angle = MathUtils.atan2(y - y1, x - x1);
		return (float) (MathUtils.toDegrees(angle) - 90);
	}

	public static float updateAngle(float currentAngle, float targetAngle,
			float step) {
		float pi = MathUtils.PI;

		currentAngle = (currentAngle + pi * 2) % (pi * 2);
		targetAngle = (targetAngle + pi * 2) % (pi * 2);

		if (MathUtils.abs(currentAngle - targetAngle) < step) {
			return targetAngle;
		}

		if (2 * pi - currentAngle + targetAngle < pi
				|| 2 * pi - targetAngle + currentAngle < pi) {
			if (currentAngle < targetAngle) {
				currentAngle -= step;
			} else {
				currentAngle += step;
			}
		} else {
			if (currentAngle < targetAngle) {
				currentAngle += step;
			} else {
				currentAngle -= step;
			}
		}
		return (2 * pi + currentAngle) % (2 * pi);
	}

	public static float updateLine(float value, float target, float step) {
		if (MathUtils.abs(value - target) < step)
			return target;
		if (value > target) {
			return value - step;
		}
		return value + step;
	}

	public static float getAngleDiff(float currentAngle, float targetAngle) {
		float pi = MathUtils.PI;
		currentAngle = (currentAngle + pi * 2) % (pi * 2);
		targetAngle = (targetAngle + pi * 2) % (pi * 2);

		float diff = MathUtils.abs(currentAngle - targetAngle);
		float v = MathUtils.abs(2 * pi - currentAngle + targetAngle);
		if (v < diff) {
			diff = v;
		}
		v = MathUtils.abs(2 * pi - targetAngle + currentAngle);
		if (v < diff) {
			diff = v;
		}
		return diff;
	}

	public static Vector2f rotateVector(Vector2f v, Vector2f center, float angle) {
		Vector2f result = new Vector2f();
		float x = v.x - center.x;
		float y = v.y - center.y;
		result.x = (float) MathUtils.cos(angle) * x
				- (float) MathUtils.sin(angle) * y + center.x;
		result.y = (float) MathUtils.sin(angle) * x
				+ (float) MathUtils.cos(angle) * y + center.y;
		return result;
	}
}
