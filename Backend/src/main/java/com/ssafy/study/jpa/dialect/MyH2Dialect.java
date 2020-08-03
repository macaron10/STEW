package com.ssafy.study.jpa.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {
	public MyH2Dialect() {
		registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
		
		registerFunction("concat", new StandardSQLFunction("concat", StandardBasicTypes.STRING));
		
//		registerFunction("match",
//				new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "MATCH(?1) AGAINST (?2 in boolean mode)"));
	}
}