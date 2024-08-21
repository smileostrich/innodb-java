package org.ian.innodb.table;

import org.ian.innodb.InnoDBCharset;

import java.util.Optional;

public enum FieldType {

	TINYINT(true),
	SMALLINT(true),
	MEDIUMINT(true),
	VARCHAR(true),
	INT(true),
	INT6(true),
	BIGINT(true),
	FLOAT(false),
	DOUBLE(false),
	ENUM(false),
	TEXT(true),
	CHAR(false),
	DATE(false),
	DATETIME(false),
	TIMESTAMP(false),
	BOOLEAN(false);

	private final boolean isVariable;

	FieldType(boolean isVariable) {
		this.isVariable = isVariable;
	}

	public boolean isVariable() {
		return isVariable;
	}

	public boolean isSigned() {
		return this == TINYINT || this == SMALLINT || this == MEDIUMINT || this == INT || this == INT6 || this == BIGINT;
	}

	public long maxLen(InnoDBCharset charset, Optional<Long> length) {
		return switch (this) {
			case TINYINT -> 1;
			case SMALLINT -> 2;
			case MEDIUMINT -> 3;
			case INT -> 4;
			case INT6 -> 6;
			case BIGINT -> 8;
			case FLOAT -> 4;
			case DOUBLE -> 8;
			case ENUM -> 2;
			case TEXT, CHAR -> length.map(l -> l * charset.getMaxLen()).orElse(0L);
			case DATE -> 3;
			case DATETIME -> 8;
			case TIMESTAMP -> 4;
			default -> throw new IllegalArgumentException("Unsupported FieldType");
		};
	}

	public static FieldType fromSQLType(String sqlType) {
		return switch (sqlType.toUpperCase()) {
			case "INT", "INTEGER" -> INT;
			case "VARCHAR" -> VARCHAR;
			case "TEXT" -> TEXT;
			case "DATE" -> DATE;
			case "DATETIME" -> DATETIME;
			case "TIMESTAMP" -> TIMESTAMP;
			case "BIGINT" -> BIGINT;
			case "BOOLEAN" -> BOOLEAN;
			default -> throw new IllegalArgumentException("Unsupported SQL type: " + sqlType);
		};
	}

}
