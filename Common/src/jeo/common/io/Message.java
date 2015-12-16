/*
 * The MIT License
 *
 * Copyright 2013-2015 Florian Barras.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jeo.common.io;

import jeo.common.io.Messages.IOType;
import jeo.common.io.Messages.SeverityLevel;

public class Message
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private final IOType type;
	private final SeverityLevel level;
	private final String prefix;
	private final String content;
	private final Exception exception;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Message(final IOType type, final SeverityLevel level, final String content)
	{
		this.type = type;
		this.level = level;
		this.prefix = Messages.getPrefix(type, level);
		this.content = content;
		this.exception = null;
	}

	public Message(final IOType type, final SeverityLevel level, final Object content)
	{
		this.type = type;
		this.level = level;
		this.prefix = Messages.getPrefix(type, level);
		this.content = String.valueOf(content);
		this.exception = null;
	}

	public Message(final Exception exception)
	{
		this.type = IOType.OUTPUT;
		this.level = SeverityLevel.ERROR;
		this.prefix = Messages.getPrefix(type, level);
		this.content = String.valueOf(exception.getMessage());
		this.exception = exception;
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the type
	 */
	public IOType getType()
	{
		return type;
	}

	/**
	 * @return the level
	 */
	public SeverityLevel getLevel()
	{
		return level;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @return the exception
	 */
	public Exception getException()
	{
		return exception;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return prefix + " " + content;
	}
}
