package util;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;

public class Csv {
    private Cursor cursor;

    public Csv(Cursor cursor) {
        this.cursor = cursor;
    }

    public File exportDB() {

        String dataFinal = Data.getDataEHoraAual("ddMMyyyy_HHmm");

        File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Registro");
        if (!exportDir.exists()) exportDir.mkdirs();

        File file = new File(exportDir, "Registros - " + dataFinal + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            cursor.moveToFirst();
            csvWrite.writeNext(cursor.getColumnNames());
            while(cursor.moveToNext()) {
                String arrStr[] ={cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            cursor.close();
        }
        catch(Exception sqlEx) {
            Log.e("Salvando", sqlEx.getMessage(), sqlEx);
        }
        return file;
    }
}
