package ru.clevertec.taskspring.dao;

public final class Query {

    private Query() {
    }

    public static final String SCHEMA = "custom";
    public static final String TAG_TABLE = String.format("%s.tag", SCHEMA);
    public static final String GC_TABLE = String.format("%s.gift_certificate", SCHEMA);

    /**
     * Column names
     */
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String GC_ID = "id";
    public static final String GC_NAME = "name";
    public static final String GC_DESCRIPTION = "description";
    public static final String GC_PRICE = "price";
    public static final String GC_DURATION = "duration";
    public static final String GC_CREATE_DATE = "create_date";
    public static final String GC_LAST_UPDATE_DATE = "last_update_date";

    /**
     * Query for the "tag" table
     */

    public static final String FIND_ALL_TAG = String.format("SELECT * FROM %s", TAG_TABLE);
    public static final String FIND_BY_ID_TAG = String.format("SELECT * FROM %s WHERE %s=?", TAG_TABLE, TAG_ID);
    public static final String FIND_BY_NAME = String.format("SELECT * FROM %s WHERE %s=?", TAG_TABLE, TAG_NAME);
    public static final String SAVE_TAG = String.format("INSERT INTO %s (%s) VALUES(?)", TAG_TABLE, TAG_NAME);
    public static final String UPDATE_TAG = String.format("UPDATE %s SET %s=? WHERE %s=?", TAG_TABLE, TAG_NAME, TAG_ID);
    public static final String DELETE_TAG = String.format("DELETE FROM %s WHERE %s=?", TAG_TABLE, TAG_ID);
    public static final String CALL_GET_BY_PARTIAL_NAME = String.format("{call %s.get_by_partial_name(?)}", SCHEMA);

    /**
     * Query for the "gift_certificate" table
     */

    public static final String FIND_ALL_GC = String.format("SELECT * FROM %s", GC_TABLE);
    public static final String FIND_BY_ID_GC = String.format("SELECT * FROM %s WHERE %s=?", GC_TABLE, GC_ID);
    public static final String SAVE_GC = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?)",
            GC_TABLE, GC_NAME, GC_DESCRIPTION, GC_PRICE, GC_DURATION, GC_CREATE_DATE, GC_LAST_UPDATE_DATE);
    public static final String UPDATE_GC = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            GC_TABLE, GC_NAME, GC_DESCRIPTION, GC_PRICE, GC_DURATION, GC_CREATE_DATE, GC_LAST_UPDATE_DATE, GC_ID);
    public static final String DELETE_GC = String.format("DELETE FROM %s WHERE %s=?", GC_TABLE, GC_ID);
    public static final String CALL_GET_BY_PARTIAL_DESCRIPTION =
            String.format("{call %s.get_by_partial_description(?)}", SCHEMA);

}