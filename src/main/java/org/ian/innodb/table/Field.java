package org.ian.innodb.table;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record Field(String name, FieldType fieldType, boolean nullable, List<String> enumValues) {

	public FieldValue parse(byte[] buf, Optional<Long> lengthOpt) {
		return switch (fieldType) {
			case TINYINT, SMALLINT, MEDIUMINT, INT, INT6, BIGINT ->
					parseIntField(buf, (int) fieldType.maxLen(null, Optional.empty()), fieldType.isSigned());
			case CHAR, VARCHAR -> parseCharOrVarchar(buf, lengthOpt);
			case TEXT -> lengthOpt.map(len -> parseText(buf, len.intValue())).orElse(new FieldValue.Null());
			case DATE -> parseDate(buf);
			case DATETIME -> parseDateTime(buf);
			case TIMESTAMP -> parseTimestamp(buf);
			case ENUM -> parseEnum(buf);
			case BOOLEAN -> parseBoolean(buf);
			default -> throw new IllegalArgumentException("Unsupported FieldType: " + fieldType);
		};
	}

	private FieldValue parseCharOrVarchar(byte[] buf, Optional<Long> lengthOpt) {
		int length = lengthOpt.orElse(fieldType.maxLen(null, Optional.empty())).intValue();
		String value = new String(buf, 0, length).trim();
		return new FieldValue.String(value);
	}

	private FieldValue parseBoolean(byte[] buf) {
		// Assuming boolean is stored as a single byte where 0 is false and anything else is true
		boolean value = buf[0] != 0;
		return new FieldValue.Boolean(value);
	}

	private FieldValue parseIntField(byte[] buf, int len, boolean signed) {
		long value = parseUInt(buf, len);
		if (signed) {
			value = flipSignBit(value, len);
		}
		return signed ? new FieldValue.SignedInt((int) value) : new FieldValue.UnsignedInt(value);
	}

	private FieldValue parseChar(byte[] buf) {
		String value = new String(buf, StandardCharsets.UTF_8).trim();
		return new FieldValue.String(value);
	}

	private FieldValue parseText(byte[] buf, int len) {
		String value = new String(buf, 0, len, StandardCharsets.UTF_8).trim();
		return new FieldValue.String(value);
	}

	private FieldValue parseDate(byte[] buf) {
		long dateNum = parseUInt(buf, 3);
		int day = (int) (dateNum & 0x1F);
		int month = (int) ((dateNum >> 5) & 0xF);
		int year = (int) (dateNum >> 9);
		return new FieldValue.String(java.lang.String.format("%04d-%02d-%02d", year, month, day));
	}

	private FieldValue parseDateTime(byte[] buf) {
		long datetime = parseUInt(buf, 8);
		int year = (int) (datetime >> 46) / 13;
		int month = (int) ((datetime >> 46) % 13);
		int day = (int) ((datetime >> 41) & 0x1F);
		int hour = (int) ((datetime >> 36) & 0x1F);
		int min = (int) ((datetime >> 30) & 0x3F);
		int sec = (int) ((datetime >> 24) & 0x3F);
		return new FieldValue.String(java.lang.String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, min, sec));
	}

	private FieldValue parseTimestamp(byte[] buf) {
		long ts = parseUInt(buf, 4);
		if (ts == 0) {
			return new FieldValue.String("0000-00-00 00:00:00");
		} else {
			LocalDateTime datetime = LocalDateTime.ofEpochSecond(ts, 0, java.time.ZoneOffset.UTC);
			return new FieldValue.String(datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}
	}

	private FieldValue parseEnum(byte[] buf) {
		int len = enumValues.size() <= 255 ? 1 : 2; // Determine whether the ENUM value is stored in 1 or 2 bytes
		int enumIndex = (int) parseUInt(buf, len); // Parse the enum index value

		if (enumIndex == 0 || enumIndex > enumValues.size())
			return new FieldValue.Null();

		String enumValue = enumValues.get(enumIndex - 1); // Get the corresponding ENUM string
		return new FieldValue.String(enumValue);
	}

	private long parseUInt(byte[] buf, int len) {
		long num = 0;
		for (int i = 0; i < len; i++) {
			num = (num << 8) | (buf[i] & 0xFF);
		}
		return num;
	}

	private long flipSignBit(long num, int len) {
		num ^= 1L << (len * 8 - 1);
		if ((num & (1L << (len * 8 - 1))) != 0) {
			num = ~num + 1;
			num &= (1L << (len * 8)) - 1;
			return -num;
		} else {
			return num;
		}
	}

}
