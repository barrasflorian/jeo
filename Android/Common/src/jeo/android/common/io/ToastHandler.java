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
package jeo.android.common.io;

import android.content.Context;
import android.widget.Toast;

import jeo.common.io.IOManager;
import jeo.common.io.Messages;
import jeo.common.io.Messages.SeverityLevel;

public class ToastHandler
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private ToastHandler()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Shows the specified message on the screen.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void show(final Context context, final Object message)
	{
		Toast.makeText(context, String.valueOf(message), Toast.LENGTH_LONG).show();
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT MESSAGE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.RESULT}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showResult(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.RESULT, message));
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.INFO}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showInfo(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.INFO, message));
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.TEST}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showTest(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.TEST, message));
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.DEBUG}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showDebug(final Context context, final Object message)
	{
		if (IOManager.DEBUG_MODE)
		{
			show(context, Messages.createOutputMessage(SeverityLevel.DEBUG, message));
		}
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.WARNING}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showWarning(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.WARNING, message));
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.ERROR}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showError(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.ERROR, message));
	}

	/**
	 * Shows the specified message on the screen indicating the severity level
	 * {@link SeverityLevel.CRITICAL}.
	 * <p>
	 * @param context the {@link Context} of Android
	 * @param message the {@link Object} to be showed
	 */
	public static void showFatalError(final Context context, final Object message)
	{
		show(context, Messages.createOutputMessage(SeverityLevel.CRITICAL, message));
	}
}
