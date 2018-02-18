package me.xp090.shuffledairy.model;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import me.xp090.shuffledairy.MainActivity;
import me.xp090.shuffledairy.R;
import me.xp090.shuffledairy.design.ShrinkableButton;
import me.xp090.shuffledairy.util.DbHelper;

/**
 * Created by Xp090 on 16/01/2018.
 */

public class DairyNoteItem extends AbstractSectionableItem<DairyNoteItem.NoteViewHolder, DateHeader> {
    public static final String ITEM_TAG = "item_tag";
    public static final int VIEW_TYPE_ITEM = 1;

    public static final int MODE_OPENED = 2;
    public static final int MODE_COLLAPSED = 4;

    public static final int EDITABLE = 2;
    public static final int LOCKED = 4;
    public String noteText;
    public int noteColor;
    public long entityID;

    private int currentMode;
    private int editMode;

    public DairyNoteItem(DateHeader header, NoteEntity noteEntity) {
        super(header);
        this.noteText = noteEntity.noteContent;
        this.noteColor = noteEntity.noteColor;
        this.entityID = noteEntity.id;
        currentMode = MODE_COLLAPSED;
        editMode = LOCKED;
    }

    @Override
    public int hashCode() {
        return ((Long) entityID).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DairyNoteItem) {
            DairyNoteItem dairyNoteItem = (DairyNoteItem) o;
            return dairyNoteItem.entityID == this.entityID;
        }
        return o == this;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_dairy_item;
    }

    @Override
    public int getItemViewType() {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public NoteViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new NoteViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, NoteViewHolder holder, int position, List<Object> payloads) {
        holder.id = entityID;
        holder.note.setText(noteText);
        if (noteColor != 0) holder.noteCard.setCardBackgroundColor(noteColor);
        holder.currentMode = currentMode;
        holder.editMode = editMode;

        if (currentMode == MODE_COLLAPSED) {
            holder.collapse();
        } else {
            holder.open();
        }

        if (editMode == LOCKED) {
            holder.lockedNote();
        } else {
            holder.editableNote();
        }
    }


    @SuppressWarnings("ConstantConditions")
    public  class NoteViewHolder extends FlexibleViewHolder {
        EditText note;
        ShrinkableButton positive;
        ShrinkableButton negative;
        ShrinkableButton color;
        CardView noteCard;
        FrameLayout buttonBar;
        View divider;
        Context context;
        LinearLayout container;
        long id;
         int currentMode;
         int editMode;
         private FlexibleAdapter mAdapter;

        public NoteViewHolder(View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            context = view.getContext();
            note = view.findViewById(R.id.etxt_note);
            positive = view.findViewById(R.id.btn_positive);
            negative = view.findViewById(R.id.btn_negative);
            color = view.findViewById(R.id.btn_color);
            noteCard = view.findViewById(R.id.card_container);
            buttonBar = view.findViewById(R.id.note_buttons_bar);
            divider = view.findViewById(R.id.divider);
            container = view.findViewById(R.id.note_container);
            buttonBar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            note.setFocusableInTouchMode(false);
            note.setSingleLine(true);
            mAdapter = adapter;
            View.OnClickListener toggleOpenListner= (new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMode = getCurrentItem().editMode;
                    if (editMode == LOCKED) {
                        currentMode =  getCurrentItem().currentMode;
                        toggleExpandNote(currentMode);
                    }
                }
            });
            note.setOnClickListener(toggleOpenListner);
            container.setOnClickListener(toggleOpenListner);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMode = getCurrentItem().editMode;
                    if (editMode == LOCKED) {
                        editableNote();
                    } else if (editMode == EDITABLE) {
                        DbHelper.getInstance().updateItem(id).setContent(note.getText().toString()).commit();
                        getCurrentItem().noteText =note.getText().toString();
                        lockedNote();
                    }
                }
            });

            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DbHelper.getInstance().removeItem(id);
                    adapter.removeItem(getAdapterPosition());
                }
            });

            color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SpectrumDialog.Builder(context)
                            .setColors(R.array.note_colors)
                            .setDismissOnColorSelected(true)
                            .setSelectedColor(noteCard.getCardBackgroundColor().getDefaultColor())
                            .setOutlineWidth(0)
                            .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                                @Override
                                public void onColorSelected(boolean positiveResult, int color) {
                                    if (positiveResult) {
                                        noteCard.setCardBackgroundColor(color);
                                        DbHelper.getInstance().updateItem(id).setColor(color).commit();
                                        getCurrentItem().noteColor = color;
                                    }
                                }
                            }).build().show(((MainActivity)context).getSupportFragmentManager(), "");

                }
            });
        }

        void toggleExpandNote(int currentMode) {
            if (currentMode == MODE_COLLAPSED) {
                open();
            } else if (currentMode == MODE_OPENED) {
                collapse();
            }
        }

        void open() {
            buttonBar.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            note.setSingleLine(false);
            noteCard.setMinimumHeight((int) context.getResources().getDimension(R.dimen.card_min_hight));
             getCurrentItem().currentMode = MODE_OPENED;
        }

        void collapse() {
            buttonBar.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            note.setSingleLine(true);
            noteCard.setMinimumHeight(0);
             getCurrentItem().currentMode = MODE_COLLAPSED ;
        }

        void editableNote() {
            note.setFocusable(true);
            note.setFocusableInTouchMode(true);
            if(note.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(note, InputMethodManager.SHOW_IMPLICIT);
            }
            positive.setBackgroundResource(R.drawable.ic_action_save);
             getCurrentItem().editMode = EDITABLE;
        }

        void lockedNote() {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
            note.setFocusable(false);
            note.setFocusableInTouchMode(false);
            positive.setBackgroundResource(R.drawable.ic_action_edit);
             getCurrentItem().editMode = LOCKED;
        }
        
        private DairyNoteItem getCurrentItem() {
           return ((DairyNoteItem) mAdapter.getItem(getAdapterPosition()));
        }
    }
}
