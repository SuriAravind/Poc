package com.p3solutions.writer.base;

import com.p3solutions.writer.exceptions.WriterException;
import com.p3solutions.writer.formattingHelper.JsonFormattingHelper;
import com.p3solutions.writer.options.ColumnInfo;
import com.p3solutions.writer.options.Options;
import com.p3solutions.writer.utility.json.JSONObject;

import java.util.List;
import java.util.logging.Logger;

public abstract class BaseJsonFormatter extends BaseFormatter {

	protected static final Logger LOGGER = Logger.getLogger(BaseJsonFormatter.class.getName());

	protected final JSONObject jsonRoot;

	protected BaseJsonFormatter(Options option, List<ColumnInfo> columnsInfo, String title) throws WriterException {
		super(option,columnsInfo, title);
		jsonRoot = new JSONObject();
	}

	public void end() throws WriterException {
		try {
			((JsonFormattingHelper) formattingHelper).write(jsonRoot);
			out.flush();
			out.close();
		} catch (Exception e) {
			throw new WriterException("Could not write Json", e);
		}
	}

}
