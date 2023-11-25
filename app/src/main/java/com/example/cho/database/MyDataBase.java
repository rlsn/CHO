package com.example.cho.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.cho.ui.fit.FitExercise;
import com.example.cho.ui.fit.FitSession;
import com.example.cho.ui.fit.FitSubSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FitExercise.class, FitSession.class}, version = 3, exportSchema = false)
@TypeConverters({MyDataBase.DateConverters.class, MyDataBase.TimeConverters.class, MyDataBase.SubsessionsConverters.class})
public abstract class MyDataBase extends RoomDatabase {
    public abstract FitExerciseDao exerciseDao();
    public abstract FitSessionDao sessionDao();


    private static volatile MyDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MyDataBase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static class DateConverters {
        @TypeConverter
        public LocalDate fromString(String s) {
            return LocalDate.parse(s);
        }

        @TypeConverter
        public String fromDate(LocalDate date) {
            return date.toString();
        }
    }

    public static class TimeConverters {
        @TypeConverter
        public LocalTime fromString(String s) {
            return LocalTime.parse(s);
        }

        @TypeConverter
        public String fromDate(LocalTime time) {
            return time.toString();
        }
    }

    public static class SubsessionsConverters {
        @TypeConverter
        public ArrayList<FitSubSession> fromString(String s) {
            return FitSession.subsessions_fromJson(s);
        }

        @TypeConverter
        public String fromSubsessions(ArrayList<FitSubSession> subSessions) {
            return FitSession.jsonify_subsessions(subSessions);
        }
    }



    private static RoomDatabase.Callback clearDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                FitSessionDao sessionDao = INSTANCE.sessionDao();
                sessionDao.deleteAll();

//                FitSession session = new FitSession();
//                long sessioni_id  = sessionDao.insert(session);
//                FitSubSession subsession = new FitSubSession(sessioni_id);
//                subSessionDao.insert(subsession);
            });
        }
    };
}
