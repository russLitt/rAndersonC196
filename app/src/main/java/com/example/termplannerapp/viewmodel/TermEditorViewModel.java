package com.example.termplannerapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.termplannerapp.database.AppRepository;
import com.example.termplannerapp.database.TermEntity;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermEditorViewModel extends AndroidViewModel {

    public MutableLiveData<TermEntity> mLiveTerms = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TermEditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int termId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TermEntity term = mRepository.getTermById(termId);
                mLiveTerms.postValue(term);
            }
        });
    }

    public void saveNote(String termText) {
        TermEntity term = mLiveTerms.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(termText.trim())) {
                return;
            }
            term = new TermEntity(new Date(), termText.trim());
        } else {
            term.setTermTitle(termText);
        }
        mRepository.insertTerm(term);
    }

    public void deleteTerm() {
        mRepository.deleteTerm(mLiveTerms.getValue());
    }
}