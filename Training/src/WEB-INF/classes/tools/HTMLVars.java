/***********************************************************************
 * Module:  HTMLVars.java
 * Author:  jsandlin
 * Purpose: Defines the Interface HTMLVars
 ***********************************************************************/

package tools;

import java.util.*;

public interface HTMLVars
{
    public static final int PAGE_WIDTH = 750;
    public static final int TABLE_WIDTH = 700;
    public static final int QUESTION_TD_WIDTH = 695;
    public static final int Q_NUM_TD_WIDTH = 3;
    public static final int Q_TEXT_FIELD_WIDTH = 80;
    public static final int CHOICE_TEXT_FIELD_WIDTH = 80;
    public static final int DESCRIPTION_FIELD_WIDTH = 100;
    public static final int BLANK_WIDTH = 23;
    public static final int RADIO_TD_WIDTH = Q_NUM_TD_WIDTH;
    public static final char[] LETTER = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    public static final int MAX_NUM_ENTRIES = 26;
    public int NUM_COLS_PER_ROW = 4;
}