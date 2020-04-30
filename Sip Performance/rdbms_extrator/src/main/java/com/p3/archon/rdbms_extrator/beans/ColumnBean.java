package com.p3.archon.rdbms_extrator.beans;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ColumnBean {
	private boolean selected;
	private boolean extOriginalCol;
	private String originalColName;
	private String expColName;
	private String colType;
	private boolean index;
	private boolean encrypt;

	}
