package com.example.termplannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termplannerapp.database.AssessmentEntity;
import com.example.termplannerapp.database.MentorEntity;
import com.example.termplannerapp.ui.AssessmentsAdapter;
import com.example.termplannerapp.ui.MentorsAdapter;
import com.example.termplannerapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssessmentListActivity extends AppCompatActivity {

    @BindView(R.id.assessment_recycler_view)
    RecyclerView mAssmntRecyclerView;

    @OnClick(R.id.assessment_edit_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, AssessmentEditorActivity.class);
        startActivity(intent);
    }

    private List<AssessmentEntity> assessmentsData = new ArrayList<>();
    private AssessmentsAdapter mAssessmentsAdapter;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_assessment_list);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Assessments");

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {
        final Observer<List<AssessmentEntity>> assessmentsObserver = assessmentEntities -> {
            assessmentsData.clear();
            assessmentsData.addAll(assessmentEntities);

            if (mAssessmentsAdapter == null) {
                mAssessmentsAdapter = new AssessmentsAdapter(assessmentsData,
                        AssessmentListActivity.this);
                mAssmntRecyclerView.setAdapter(mAssessmentsAdapter);
            } else {
                mAssessmentsAdapter.notifyDataSetChanged();
            }
        };

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.mAssessments.observe(this, assessmentsObserver);
    }

    private void initRecyclerView() {
        mAssmntRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAssmntRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mAssmntRecyclerView.getContext(),
                layoutManager.getOrientation());
        mAssmntRecyclerView.addItemDecoration(divider);

        mAssessmentsAdapter = new AssessmentsAdapter(assessmentsData, this);
        mAssmntRecyclerView.setAdapter(mAssessmentsAdapter);
    }


}