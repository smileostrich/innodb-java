package org.ian.innodb.table;

public sealed interface FieldValue permits FieldValue.Boolean, FieldValue.Null, FieldValue.PartialString, FieldValue.SignedInt, FieldValue.Skipped, FieldValue.String, FieldValue.UnsignedInt {
	Object getValue();
	Long getLength();

	record SignedInt(long value) implements FieldValue {
		@Override
		public Long getLength() {
			return 8L;
		}

		@Override
		public Integer getValue() {
			return (int) value;
		}
	}

	record UnsignedInt(long value) implements FieldValue {
		@Override
		public Long getLength() {
			return 8L;
		}

		@Override
		public Integer getValue() {
			return (int) value;
		}
	}

	record String(java.lang.String value) implements FieldValue {
		@Override
		public Long getLength() {
			return (long) value.length();
		}

		@Override
		public java.lang.String getValue() {
			return value;
		}
	}

	record PartialString(java.lang.String partial, int totalLen) implements FieldValue {
		@Override
		public Long getLength() {
			return (long) totalLen;
		}

		@Override
		public java.lang.String getValue() {
			return partial;
		}
	}

	record Null() implements FieldValue {
		@Override
		public Long getLength() {
			return 0L;
		}

		@Override
		public Object getValue() {
			return null;
		}
	}

	record Skipped() implements FieldValue {
		@Override
		public Long getLength() {
			return 0L;
		}

		@Override
		public Object getValue() {
			return null;
		}
	}

	record Boolean(boolean value) implements FieldValue {
		@Override
		public Long getLength() {
			return 1L;
		}

		@Override
		public java.lang.Boolean getValue() {
			return value;
		}
	}

}
