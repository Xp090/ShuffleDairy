package me.xp090.shuffledairy.util;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import me.xp090.shuffledairy.ShuffleDairyApplication;
import me.xp090.shuffledairy.model.DairyNoteItem;
import me.xp090.shuffledairy.model.DateHeader;
import me.xp090.shuffledairy.model.MyObjectBox;
import me.xp090.shuffledairy.model.NoteEntity;

/**
 * Created by Xp090 on 16/01/2018.
 */

public class DbHelper {
    private static DbHelper dbHelper;
    private BoxStore mBoxStore;
    private Box<NoteEntity> mNoteBox;

    private DbHelper() {

        mBoxStore = MyObjectBox.builder().androidContext(ShuffleDairyApplication.getAppContext()).build();
        mNoteBox = mBoxStore.boxFor(NoteEntity.class);

    }

    public static DbHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DbHelper();
        }
        return dbHelper;
    }

    public List<IFlexible> getAllItems() {
        if (DemoDataList.isFirstRun()) {
            mNoteBox.put(DemoDataList.getDemoDataList());
        }

        List<IFlexible> mNotesItems = new ArrayList<>();
        List<NoteEntity> items = mNoteBox.getAll();
        DateHeader dateHeader = null;
        long prevDate = -1;
        for (int i=items.size() -1 ; i >=0; i--) {
            NoteEntity noteEntity = items.get(i);
            if (noteEntity.noteDate != prevDate) {
                dateHeader = new DateHeader(noteEntity.noteDate);
                prevDate = noteEntity.noteDate;
            }
            mNotesItems.add(new DairyNoteItem(dateHeader,noteEntity));
        }
        return mNotesItems;
    }


    public NoteEntity getItem(long id) {
        return mNoteBox.get(id);
    }

    public void addItem (NoteEntity newNoteEntity){
        mNoteBox.put(newNoteEntity);
    }

    public UpdateTransaction updateItem(long id) {
        return new UpdateTransaction(id);
    }
    public void removeItem(long id) {
        mNoteBox.remove(id);
    }

    public void removeAll(){
        mNoteBox.removeAll();
    }

    public class UpdateTransaction {
        private NoteEntity noteEntityUnderUpdate;

        public UpdateTransaction(long id) {
            this.noteEntityUnderUpdate = mNoteBox.get(id);
        }

        public UpdateTransaction setContent(String content) {
            noteEntityUnderUpdate.noteContent = content;
            return this;
        }

        public UpdateTransaction setColor(int color) {
            noteEntityUnderUpdate.noteColor = color;
            return this;
        }

        public void commit() {
            mNoteBox.put(noteEntityUnderUpdate);
        }

    }


}
