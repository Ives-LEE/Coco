package idv.leeicheng.coco.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import idv.leeicheng.coco.recording.ListItem;
import idv.leeicheng.coco.recording.RecordingItem;
import idv.leeicheng.coco.setting.ItemType;

public class ItemSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Recoding";
    private static final int DB_VERSION = 8;
    private static final String TABLE_RECORDING = "RecordingItem";
    private static final String COL_id = "id";
    private static final String COL_date = "date";
    private static final String COL_type = "type";
    private static final String COL_itemName = "itemName";
    private static final String COL_spent = "spent";
    private static final String COL_image = "image";

    private static final String TABLE_TYPE = "ItemType";
    private static final String COL_typeId = "id";
    private static final String COL_typeName = "name";
    private static final String COL_typeRed = "red";
    private static final String COL_typeGreen = "green";
    private static final String COL_typeBlue = "blue";


    private static final String TABLE_RECORDING_CREATE =
            "CREATE TABLE " + TABLE_RECORDING + " ( " +
                    COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_date + " TEXT NOT NULL, " +
                    COL_type + " TEXT NOT NULL, " +
                    COL_itemName + " TEXT, " +
                    COL_spent + " TEXT NOT NULL, " +
                    COL_image + " BLOB ); ";


    private static final String TABLE_TYPE_CREATE =
            "CREATE TABLE " + TABLE_TYPE + " ( " +
                    COL_typeId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_typeName + " TEXT NOT NULL, " +
                    COL_typeRed + " INTEGER, " +
                    COL_typeGreen + " INTEGER, " +
                    COL_typeBlue + " INTEGER); ";

    List<ItemType> itemTypes;


    public ItemSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_RECORDING_CREATE);
        db.execSQL(TABLE_TYPE_CREATE);
        DefultInsert(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVewsion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        onCreate(db);
    }

    public ListItem getRecordingItem(int id) {
        ListItem item = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {
                COL_id, COL_date, COL_type, COL_itemName, COL_spent, COL_image
        };
        String selection = COL_id + " = ?;";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = sqLiteDatabase.query(TABLE_RECORDING, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToNext()) {
            String itemDate = cursor.getString(1);
            String type = cursor.getString(2);
            String itemName = cursor.getString(3);
            long spent = Long.valueOf(cursor.getString(4));
            byte[] image = cursor.getBlob(5);

            item = new RecordingItem(id, itemDate, type, spent, itemName, image);
        }
        cursor.close();
        return item;
    }


    public List<ListItem> getAllRecording(String date) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {
                COL_id, COL_date, COL_type, COL_itemName, COL_spent, COL_image
        };

        String selection = COL_date + " LIKE ?;";
        String[] selectionArgs = {date + "%"};
        String orderBy = COL_id + " DESC";
        Cursor cursor = sqLiteDatabase.query(TABLE_RECORDING, columns, selection, selectionArgs, null, null, orderBy);
        List<ListItem> recordingItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String itemDate = cursor.getString(1);
            String type = cursor.getString(2);
            String itemName = cursor.getString(3);
            long spent = Long.valueOf(cursor.getString(4));
            byte[] image = cursor.getBlob(5);

            ListItem item = new RecordingItem(id, itemDate, type, spent, itemName, image);

            recordingItems.add(item);
        }


        cursor.close();
        Collections.reverse(recordingItems);
        return recordingItems;
    }

    public long insertRecording(RecordingItem recordingItem) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_date, recordingItem.getDate());
        values.put(COL_type, recordingItem.getItemType());
        values.put(COL_spent, String.valueOf(recordingItem.getSpent()));

        if (recordingItem.getImage() != null) {
            values.put(COL_image, recordingItem.getImage());
        }
        if (recordingItem.getItemName() != null) {
            values.put(COL_itemName, recordingItem.getItemName());
        }

        return sqLiteDatabase.insert(TABLE_RECORDING, null, values);
    }

    public long updateRecording(RecordingItem recordingItem) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_date, recordingItem.getDate());
        values.put(COL_type, recordingItem.getItemType());
        values.put(COL_spent, String.valueOf(recordingItem.getSpent()));

        if (recordingItem.getImage() != null) {
            values.put(COL_image, recordingItem.getImage());
        }
        if (recordingItem.getItemName() != null) {
            values.put(COL_itemName, recordingItem.getItemName());
        }

        String whereClause = COL_id + "= ? ;";
        String[] whereArgs = {Integer.toString(recordingItem.getId())};

        return sqLiteDatabase.update(TABLE_RECORDING, values, whereClause, whereArgs);
    }

    public long deleteRecordingById(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_id + " = ? ;";
        String[] whereArgs = {String.valueOf(id)};
        return sqLiteDatabase.delete(TABLE_RECORDING, whereClause, whereArgs);
    }

    public List<ItemType> getAllTypes() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {
                COL_typeId, COL_typeName, COL_typeRed, COL_typeGreen, COL_typeBlue
        };
        Cursor cursor = sqLiteDatabase.query(TABLE_TYPE, columns, null, null, null, null, null);

        List<ItemType> types = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int red = cursor.getInt(2);
            int green = cursor.getInt(3);
            int blue = cursor.getInt(4);
            if (red == 0 && green == 0 && blue == 0) {
                red = 240;
                green = 240;
                blue = 240;
            }
            ItemType itemType = new ItemType(id, name, red, green, blue);
            types.add(itemType);
        }
        cursor.close();
        return types;
    }

    public int selectTypeColor(String type) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {
                COL_typeRed, COL_typeGreen, COL_typeBlue
        };
        String selection = COL_typeName + " = ?;";
        String[] selectionArgs = {type};
        Cursor cursor = sqLiteDatabase.query(TABLE_TYPE, columns, selection, selectionArgs, null, null, null);
        int red = 0, green = 0, blue = 0;
        if (cursor.moveToNext()) {
            red = cursor.getInt(0);
            green = cursor.getInt(1);
            blue = cursor.getInt(2);

        } else {
            red = 240;
            green = 240;
            blue = 240;
        }
        return Color.rgb(red, green, blue);
    }

    public long insertType(ItemType type) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_typeName, type.getName());
        values.put(COL_typeRed, type.getRed());
        values.put(COL_typeGreen, type.getGreen());
        values.put(COL_typeBlue, type.getBlue());

        return sqLiteDatabase.insert(TABLE_TYPE, null, values);
    }

    public int updateType(ItemType itemType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_typeName, itemType.getName());
        values.put(COL_typeRed, itemType.getRed());
        values.put(COL_typeGreen, itemType.getGreen());
        values.put(COL_typeBlue, itemType.getBlue());
        String whereClause = COL_id + "= ? ;";
        String[] whereArgs = {Integer.toString(itemType.getId())};

        return sqLiteDatabase.update(TABLE_TYPE, values, whereClause, whereArgs);
    }

    public long deleteTypeById(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_id + " = ? ;";
        String[] whereArgs = {String.valueOf(id)};
        return sqLiteDatabase.delete(TABLE_TYPE, whereClause, whereArgs);
    }


    public void DefultInsert(SQLiteDatabase db) {
        itemTypes = getDefult();
        for (ItemType itemType : itemTypes) {
            SQLiteDatabase sqLiteDatabase = db;
            ContentValues values = new ContentValues();
            values.put(COL_typeName, itemType.getName());
            values.put(COL_typeRed, itemType.getRed());
            values.put(COL_typeGreen, itemType.getGreen());
            values.put(COL_typeBlue, itemType.getBlue());

            sqLiteDatabase.insert(TABLE_TYPE, null, values);
        }
    }

    List<ItemType> getDefult() {
        List<ItemType> itemTypes = new ArrayList<>();
        itemTypes.add(new ItemType("娛樂", 192, 57, 43));
        itemTypes.add(new ItemType("交通", 255, 0, 64));
        itemTypes.add(new ItemType("家居", 191, 0, 255));
        itemTypes.add(new ItemType("健康", 41, 128, 185));
        itemTypes.add(new ItemType("食物", 0, 173, 255));
        itemTypes.add(new ItemType("社交", 0, 255, 191));
        itemTypes.add(new ItemType("服飾", 22, 160, 133));
        itemTypes.add(new ItemType("雜物", 39, 174, 96));
        itemTypes.add(new ItemType("房租", 128, 255, 0));
        itemTypes.add(new ItemType("水費", 255, 255, 0));
        itemTypes.add(new ItemType("電費", 243, 165, 18));
        itemTypes.add(new ItemType("其他", 255, 105, 180));
        return itemTypes;
    }


}
