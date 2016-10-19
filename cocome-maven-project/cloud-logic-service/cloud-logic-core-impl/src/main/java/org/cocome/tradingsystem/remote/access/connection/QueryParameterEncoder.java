package org.cocome.tradingsystem.remote.access.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

public class QueryParameterEncoder {
	private static final Logger LOG = Logger.getLogger(QueryParameterEncoder.class);
	
	public static String encodeQueryString(String encString) {
		try {
			encString = URLEncoder.encode(encString, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LOG.error("Could not encode enterprise name with UTF-8: " + e1.getMessage());
			throw new UnsupportedOperationException(e1);
		}
		return encString;
	}
}
