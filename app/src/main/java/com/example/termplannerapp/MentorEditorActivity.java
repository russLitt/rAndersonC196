package com.example.termplannerapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.termplannerapp.viewmodel.MentorEditorViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.termplannerapp.utilities.Constants.COURSE_ID_KEY;
import static com.example.termplannerapp.utilities.Constants.EDITING_MENTOR_KEY;
import static com.example.termplannerapp.utilities.Constants.MENTOR_ID_KEY;

public class MentorEditorActivity extends AppCompatActivity {

    @BindView(R.id.mentor_name)
    EditText mMentorName;

    @BindView(R.id.mentor_email)
    EditText mMentorEmail;

    @BindView(R.id.mentor_phone)
    EditText mMentorPhone;

    private MentorEditorViewModel mViewModel;
    private boolean mNewMentor, mEditingMentor;
    private int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mEditingMentor = savedInstanceState.getBoolean(EDITING_MENTOR_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MentorEditorViewModel.class);
        mViewModel.mLiveMentors.observe(this, (MentorEntity) -> {
            mMentorName.setText(MentorEntity.getMentorName());
            mMentorEmail.setText(MentorEntity.getMentorEmail());
            mMentorPhone.setText(MentorEntity.getMentorPhone());
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_mentor));
            mNewMentor = true;
        } else if (extras.containsKey(COURSE_ID_KEY)) {
            courseId = extras.getInt(COURSE_ID_KEY);
        } else {
            setTitle(getString(R.string.edit_mentor));
            int mentorId = extras.getInt(MENTOR_ID_KEY);
            mViewModel.loadData(mentorId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNewMentor) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_mentor_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete_mentor) {
            mViewModel.deleteMentor();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        mViewModel.saveMentor(mMentorName.getText().toString(),
                mMentorEmail.getText().toString(),
                mMentorPhone.getText().toString(),
                courseId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_MENTOR_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
