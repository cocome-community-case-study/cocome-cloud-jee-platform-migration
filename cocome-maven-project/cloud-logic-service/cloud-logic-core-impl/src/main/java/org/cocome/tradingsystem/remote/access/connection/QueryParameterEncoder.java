package org.cocome.tradingsystem.remote.access.connection;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

public class QueryParameterEncoder {
	private static final Logger LOG = Logger.getLogger(QueryParameterEncoder.class);
	
	public static String encodeQueryString(String string) {
		String encString;
		try {
			encString = URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LOG.error("Could not encode string with UTF-8: " + e1.getMessage());
			throw new UnsupportedOperationException(e1);
		}
		return encString;
	}
	
	public static String decodeQueryString(String encString) {
		String decString;
		try {
			decString = URLDecoder.decode(encString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("Could not decode string with UTF-8: " + e.getMessage());
			throw new UnsupportedOperationException(e);
		}
		return decString;
	}
}
