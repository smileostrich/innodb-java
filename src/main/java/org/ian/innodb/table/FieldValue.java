package org.ian.innodb.table;

public sealed interface FieldValue permits FieldValue.Boolean, FieldValue.Null, FieldValue.PartialString, FieldValue.SignedInt, FieldValue.Skipped, FieldValue.String, FieldValue.UnsignedInt {
	Object getValue();

	record SignedInt(long value) implements FieldValue {
		@Override
		public Object getValue() {
			return value;
		}
	}

	record UnsignedInt(long value) implements FieldValue {
		@Override
		public Object getValue() {
			return value;
		}
	}

	record String(java.lang.String value) implements FieldValue {
		@Override
		public Object getValue() {
			return value;
		}
	}

	record PartialString(java.lang.String partial, int totalLen) implements FieldValue {
		@Override
		public Object getValue() {
			return partial;
		}
	}

	record Null() implements FieldValue {
		@Override
		public Object getValue() {
			return null;
		}
	}

	record Skipped() implements FieldValue {
		@Override
		public Object getValue() {
			return null;
		}
	}

	record Boolean(boolean value) implements FieldValue {
		@Override
		public Object getValue() {
			return value;
		}
	}

}
