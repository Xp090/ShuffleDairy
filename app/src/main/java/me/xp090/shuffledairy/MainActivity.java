package me.xp090.shuffledairy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import me.xp090.shuffledairy.design.AnimationsUtil;
import me.xp090.shuffledairy.design.ShrinkableButton;
import me.xp090.shuffledairy.design.TimelineDecor;
import me.xp090.shuffledairy.model.DairyNoteItem;
import me.xp090.shuffledairy.model.DateHeader;
import me.xp090.shuffledairy.model.NoteEntity;
import me.xp090.shuffledairy.util.DbHelper;

public class MainActivity extends AppCompatActivity {
    public static final String NEW_NOTE_SHOWN_KEY = "new_note_shown";
    public static final String NEW_NOTE_COLOR_KEY = "new_note_color_key";
    public static final String NEW_NOTE_TEXT_KEY = "new_note_text_key";

    RecyclerView dairyList;
    FrameLayout newNoteFrame;
    CardView newNoteCard;
    ShrinkableButton saveButton;
    ShrinkableButton colorButton;
    ShrinkableButton cancelButton;
    FloatingActionButton newNoteFab;
    EditText noteText;
    FlexibleAdapter<IFlexible> adapter;

    boolean isNewNoteShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dairyList = findViewById(R.id.dairy_list);
        saveButton = findViewById(R.id.btn_positive);
        colorButton = findViewById(R.id.btn_color);
        cancelButton = findViewById(R.id.btn_negative);
        newNoteFab= findViewById(R.id.fab_add);
        noteText = findViewById(R.id.etxt_note);
        newNoteFrame = findViewById(R.id.new_note_frame);
        newNoteCard = findViewById(R.id.card_container);

        newNoteCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNoteColor();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNote();
            }
        });
        newNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowNewNoteDialog();

            }
        });
        newNoteFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHideNewNoteDialog();
            }
        });
        noteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onCheckEmptyNoteText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dairyList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && newNoteFab.isShown()) {
                    newNoteFab.hide();
                } else if (dy < 0) {
                    newNoteFab.show();
                }
            }

        });

        adapter = new FlexibleAdapter<>(DbHelper.getInstance().getAllItems());
        adapter.setDisplayHeadersAtStartUp(true);
        adapter.setStickyHeaders(true);
        dairyList.setAdapter(adapter);
        dairyList.setLayoutManager(new LinearLayoutManager(this));
        TimelineDecor.DecorPrams decorPrams = new TimelineDecor.DecorPrams.Builder(this)
                .setInnerDotColor(Color.WHITE)
                .setInnerLineColor(Color.WHITE)
                .setInnerLineWidth(getResources().getDimension(R.dimen.inner_line_width))
                .setInnerDotRadius(getResources().getDimension(R.dimen.inner_dot_radius))
                .setinnerSpace(getResources().getDimension(R.dimen.inner_space))
                .setOuterLineColor(Color.WHITE)
                .setOuterDotRadius(getResources().getDimension(R.dimen.outer_dot_radius))
                .setOuterLineWidth(getResources().getDimension(R.dimen.outer_line_width))
                .build();
        dairyList.addItemDecoration(new TimelineDecor(decorPrams));





    }

    private void cancelNote() {
        noteText.setText("");
        onHideNewNoteDialog();
    }

    private void changeNoteColor() {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.note_colors)
                .setDismissOnColorSelected(true)
                .setSelectedColor(newNoteCard.getCardBackgroundColor().getDefaultColor())
                .setOutlineWidth(0)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, int color) {
                        if (positiveResult) {
                            newNoteCard.setCardBackgroundColor(color);
                        }
                    }
                }).build().show(getSupportFragmentManager(), "");
    }

    private void saveNote() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long currentDate = cal.getTimeInMillis();



        NoteEntity noteEntity = new NoteEntity(currentDate, noteText.getText().toString(), newNoteCard.getCardBackgroundColor().getDefaultColor());
        DbHelper.getInstance().addItem(noteEntity);

        DateHeader dateHeader = new DateHeader(currentDate);
        int insertPos = 1;
        if (adapter.getItemCount() ==0 ||!adapter.getItem(0).equals(dateHeader)) {
            adapter.addItem(0,dateHeader);
        }
        adapter.addItem(insertPos,new DairyNoteItem(dateHeader, noteEntity));
        dairyList.scrollToPosition(0);
        noteText.setText("");
        onHideNewNoteDialog();

    }

    private void onCheckEmptyNoteText(CharSequence s) {
        if (s.toString().trim().length() > 0) {
            saveButton.setEnabled(true);
            saveButton.setBackgroundResource(R.drawable.ic_action_save);
        } else {
            saveButton.setBackgroundResource(R.drawable.ic_action_check_unclickable);
            saveButton.setEnabled(false);
        }

    }

    private void onShowNewNoteDialog() {
        isNewNoteShown =true;
        Animation fadeInAnim = AnimationsUtil.fadeIn(150);
        fadeInAnim.setAnimationListener(new AnimationsUtil.NewNoteAnimListener(newNoteFrame,View.VISIBLE));
        newNoteFrame.startAnimation(fadeInAnim);
        Animation slideDownAnim = AnimationsUtil.slideDown(-newNoteCard.getHeight(),300);
        slideDownAnim.setAnimationListener(new AnimationsUtil.NewNoteAnimListener(newNoteCard,View.VISIBLE));
        newNoteCard.startAnimation(slideDownAnim);
        newNoteFab.hide();
    }
    private void onHideNewNoteDialog() {
        isNewNoteShown =false;

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(noteText.getWindowToken(), 0);

        Animation fadeOutAnim = AnimationsUtil.fadeOut(150);
        fadeOutAnim.setAnimationListener(new AnimationsUtil.NewNoteAnimListener(newNoteFrame,View.GONE));
        newNoteFrame.startAnimation(fadeOutAnim);

        Animation slideUp = AnimationsUtil.slideUp(-newNoteCard.getHeight(),300);
        slideUp.setAnimationListener(new AnimationsUtil.NewNoteAnimListener(newNoteCard,View.GONE));
        newNoteCard.startAnimation(slideUp);
        newNoteFab.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEW_NOTE_SHOWN_KEY,isNewNoteShown);
        outState.putInt(NEW_NOTE_COLOR_KEY, newNoteCard.getCardBackgroundColor().getDefaultColor());
        outState.putString(NEW_NOTE_TEXT_KEY,noteText.getText().toString());
        outState.putInt("MyLayoutVisibility",newNoteCard.getVisibility());
        ArrayList<String> st = new ArrayList<>();

    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isNewNoteShown = savedInstanceState.getBoolean(NEW_NOTE_SHOWN_KEY);
        if (isNewNoteShown) {
            newNoteFrame.setVisibility(View.VISIBLE);
            newNoteCard.setVisibility(View.VISIBLE);
            newNoteFab.hide();
        }

        newNoteCard.setCardBackgroundColor(savedInstanceState.getInt(NEW_NOTE_COLOR_KEY));
        noteText.setText(savedInstanceState.getString(NEW_NOTE_TEXT_KEY));
    }

    @Override
    public void onBackPressed() {
        if (isNewNoteShown) {
            onHideNewNoteDialog();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_remove_all:
                DbHelper.getInstance().removeAll();
                adapter.clear();
                Snackbar.make(dairyList, "ALL ITEMS REMOVED!!", Snackbar.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
