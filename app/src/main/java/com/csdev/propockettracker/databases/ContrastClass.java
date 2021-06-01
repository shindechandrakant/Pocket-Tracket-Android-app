package com.csdev.propockettracker.databases;
import android.provider.BaseColumns;

import java.util.Calendar;

public class ContrastClass implements BaseColumns
{


    public static String getMonth(int month)
    {
        switch (month)
        {
            case Calendar.JANUARY:
                return "JANUARY";
            case Calendar.FEBRUARY:
                return "FEBRUARY";
            case Calendar.MARCH:
                return "MARCH";
            case Calendar.APRIL:
                return "APRIL";
            case Calendar.MAY:
                return "MAY";
            case Calendar.JUNE:
                return "JUNE";
            case Calendar.JULY:
                return "JULY";
            case Calendar.AUGUST:
                return "AUGUST";
            case Calendar.SEPTEMBER:
                return "SEPTEMBER";
            case Calendar.OCTOBER:
                return "OCTOBER";
            case Calendar.NOVEMBER:
                return "NOVEMBER";
            case Calendar.DECEMBER:
                return "DECEMBER";
            default:
                return " ";
        }
    }
    public static class TEMP_TRANS implements BaseColumns
    {
        public static final String TABLE_NAME = "TRANSACTION_TABLE";
        public static final String ID = _ID;
        public static final String FOR_WHAT = "FOR_WHAT";
        public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
        public static final String DATE = "DATE";
        public static final String MONTH = "MONTH";
        public static final String AMOUNT = "AMOUNT";
        public static final String TYPE = "TYPE";
    }
    public static class FIX_EXPENSE implements BaseColumns
    {
        public static final String TABLE_NAME = "FIX_TRANSACTION_TABLE";
        public static final String ID = _ID;
        public static final String TITLE = "TITLE";
        public static final String AMOUNT = "AMOUNT";
        public static final String STATUS = "STATUS";
        public static final String DUE_DATE = "DUE_DATE";
        public static final String PAYED_DATE = "PAYED_DATE";
    }
    public static class GRAPHICAL implements BaseColumns
    {
        public static final String TABLE_NAME = "SUMMERY_TRANSACTION_TABLE";
        public static final String ID = _ID;
        public static final String MONTH_NAME = "MONTH_NAME";
        public static final String TOTAL_TRANSACTION = "TOTAL_TRANSACTION";
        public static final String SAVING = "SAVING";
        public static final String EXPENSE = "EXPENSE";
    }

    public static class PERMANENT_TRANSACTION implements BaseColumns
    {
        public static final String TABLE_NAME = "TRANSACTION_TABLE";
        public static final String ID = _ID;
        public static final String MONTH_NAME = "MONTH_NAME";
        public static final String FOR_WHAT = "FOR_WHAT";
        public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
        public static final String DATE = "DATE";
        public static final String AMOUNT = "AMOUNT";
        public static final String TYPE = "TYPE";
    }

}
