/*
 * @(#)Envelope.java Copyright 2010 Zachary Davis. All rights reserved.
 */

package bbth.engine.util;

import static bbth.engine.util.Envelope.OutOfBoundsHandler.THROW_EXCEPTION;

public class Envelope {
	public static final Envelope ALWAYS_ZERO = new Envelope(0f,
			OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
	public static final Envelope ALWAYS_ONE = new Envelope(1f,
			OutOfBoundsHandler.RETURN_FIRST_OR_LAST);

	public static enum OutOfBoundsHandler {
		THROW_EXCEPTION {
			@Override
			float translateTime(float time, float totalLength) {
				if (time >= 0 && time <= totalLength)
					return time;
				throw new IllegalArgumentException("time was out of bounds"); //$NON-NLS-1$
			}
		},
		RETURN_FIRST_OR_LAST {
			@Override
			float translateTime(float time, float totalLength) {
				if (time > totalLength)
					return totalLength;
				if (time < 0)
					return 0;
				return time;
			}
		},
		WRAP {
			@Override
			float translateTime(float time, float totalLength) {
				return totalLength <= 0 ? 0 : time % totalLength;
			}
		};

		abstract float translateTime(float time, float totalLength);
	}

	static abstract class Entry {
		public final float endTime;
		public final float length;

		public Entry(float endTime, float length) {
			this.endTime = endTime;
			this.length = length;
		}

		public final boolean coversTime(float time) {
			return (time < endTime && time > endTime - length)
					|| time == endTime;
		}

		public abstract double getValueAtTime(float time);
	}

	static class FlatEntry extends Entry {
		double value;

		public FlatEntry(float endTime, float length, double value) {
			super(endTime, length);
			this.value = value;
		}

		@Override
		public double getValueAtTime(float time) {
			return value;
		}
	}

	static class LinearEntry extends Entry {
		double slope;
		double endValue;

		public LinearEntry(float endTime, float length, double startValue,
				double endValue) {
			super(endTime, length);
			this.slope = (endValue - startValue) / length;
			this.endValue = endValue;
		}

		@Override
		public double getValueAtTime(float time) {
			return endValue - slope * (endTime - time); // Linear interpolation
														// using 3 operations.
														// Slick, no?
		}
	}

	Bag<Entry> entrys = new Bag<Entry>();
	float length;
	OutOfBoundsHandler outOfBoundsHandler;

	protected Entry getEntryAtTime(float time) {
		// binary search
		int min = 0;
		int max = entrys.size() - 1;
		int mid;
		Entry midEntry;
		while (min <= max) {
			mid = min + (max - min) / 2;
			midEntry = entrys.get(mid);
			if (midEntry.coversTime(time))
				return midEntry;
			if (time > midEntry.endTime)
				min = mid + 1;
			else
				max = mid - 1;
		}
		throw new IllegalStateException("Envelope.Entry not found"); //$NON-NLS-1$
	}

	public Envelope(double startValue) {
		this(startValue, THROW_EXCEPTION);
	}

	public Envelope(double startValue, OutOfBoundsHandler outOfBoundsHandler) {
		this.outOfBoundsHandler = outOfBoundsHandler;
		entrys.add(new FlatEntry(0f, 0f, startValue));
	}

	private void checkLengthOfTime(float lengthOfTime) {
		if (lengthOfTime <= 0)
			throw new IllegalArgumentException(
					"Length of time must be greater than 0"); //$NON-NLS-1$
	}

	private double getEndValue() {
		return entrys.getLast().getValueAtTime(length);
	}

	public void addFlatSegment(float lengthOfTime) {
		addFlatSegment(lengthOfTime, getEndValue());
	}

	public void addFlatSegment(float lengthOfTime, double value) {
		checkLengthOfTime(lengthOfTime);
		length += lengthOfTime;
		entrys.add(new FlatEntry(length, lengthOfTime, value));
	}

	public void addLinearSegment(float lengthOfTime, double endValue) {
		addLinearSegment(lengthOfTime, endValue, getEndValue());
	}

	public void addLinearSegment(float lengthOfTime, double endValue,
			double startValue) {
		checkLengthOfTime(lengthOfTime);
		length += lengthOfTime;
		entrys.add(new LinearEntry(length, lengthOfTime, startValue, endValue));
	}

	public void scaleTimes(double factor) {
		throw new UnsupportedOperationException(
				"Envelope.scaleTimes(): Not implemented yet"); //$NON-NLS-1$
	}

	public void scaleTimesToTotalLength(float totalLengthOfTime) {
		throw new UnsupportedOperationException(
				"Envelope.scaleTimesToTotalLength(): Not implemented yet"); //$NON-NLS-1$
	}

	public float getTotalLength() {
		return length;
	}

	public double getValueAtTime(float time) {
		time = outOfBoundsHandler.translateTime(time, length);
		return getEntryAtTime(time).getValueAtTime(time);
	}

	public double getValueAtFraction(double frac) {
		return getValueAtTime((int) Math.rint(frac * length));
	}

}
