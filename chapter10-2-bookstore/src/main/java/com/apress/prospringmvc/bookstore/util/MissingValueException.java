/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.bookstore.util;

import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Iuliana Cosmina on 25/08/2020
 */
@SuppressWarnings("serial")
public class MissingValueException extends RuntimeException {
	private String fieldNames;

	public MissingValueException(String message, String fieldNames) {
		super(message);
		this.fieldNames = fieldNames;
	}

	public MissingValueException(String message, Throwable cause, String fieldNames) {
		super(message, cause);
		this.fieldNames = fieldNames;
	}

	public static MissingValueException of(List<ObjectError> errors) {
		final List<String> fields = new ArrayList<>();
		errors.forEach(err ->
				fields.add(Arrays.stream(err.getArguments()).toArray(String[]::new)[0])
		);
		return new MissingValueException("Some values are missing!", fields.toString());
	}

	public String getFieldNames() {
		return fieldNames;
	}
}
